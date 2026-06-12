package com.portalops.ai.openai.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringUtil;

import com.portalops.ai.api.AIProviderType;
import com.portalops.ai.api.AIRequest;
import com.portalops.ai.api.AIResponse;
import com.portalops.ai.openai.internal.configuration.OpenAIProviderConfiguration;
import com.portalops.llm.spi.AIProvider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

@Component(
        configurationPid = "com.portalops.ai.openai.internal.configuration.OpenAIProviderConfiguration",
        service = AIProvider.class
)
public class PortalOpsOpenAiProviderComponent implements AIProvider {

    @Activate
    @Modified
    protected void activate(Map<String, Object> properties) {
        _apiKey = _getString(properties, "apiKey", "");
        _modelName = _getString(properties, "modelName", "gpt-5-mini");
    }

    @Override
    public AIResponse complete(AIRequest aiRequest) {
        if ((_apiKey == null) || _apiKey.isBlank()) {
            return new AIResponse(
                    false, "", getProviderType(), _modelName,
                    "OpenAI API key is not configured.");
        }

        if ((_modelName == null) || _modelName.isBlank()) {
            return new AIResponse(
                    false, "", getProviderType(), "",
                    "OpenAI model name is not configured.");
        }

        try {
            JSONObject requestJSONObject = JSONFactoryUtil.createJSONObject();

            requestJSONObject.put("model", _modelName);
            requestJSONObject.put("messages", _createMessagesJSONArray(aiRequest));

            HttpRequest httpRequest = HttpRequest.newBuilder(
                    URI.create("https://api.openai.com/v1/chat/completions")
            ).header(
                    "Authorization", "Bearer " + _apiKey
            ).header(
                    "Content-Type", "application/json"
            ).POST(
                    HttpRequest.BodyPublishers.ofString(
                            requestJSONObject.toString(),
                            StandardCharsets.UTF_8)
            ).timeout(
                    Duration.ofSeconds(60)
            ).build();

            HttpResponse<String> httpResponse = _httpClient.send(
                    httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() >= 400) {
                return new AIResponse(
                        false, "", getProviderType(), _modelName,
                        _getErrorMessage(httpResponse));
            }

            JSONObject responseJSONObject = JSONFactoryUtil.createJSONObject(
                    httpResponse.body());

            JSONArray choicesJSONArray = responseJSONObject.getJSONArray(
                    "choices");

            if ((choicesJSONArray == null) || (choicesJSONArray.length() == 0)) {
                return new AIResponse(
                        false, "", getProviderType(), _modelName,
                        "OpenAI returned no completion choices.");
            }

            JSONObject messageJSONObject = choicesJSONArray.getJSONObject(
                    0
            ).getJSONObject(
                    "message"
            );

            String content = StringUtil.trim(
                    messageJSONObject.getString("content"));

            if (content.isEmpty()) {
                return new AIResponse(
                        false, "", getProviderType(), _modelName,
                        "OpenAI returned an empty assistant response.");
            }

            return new AIResponse(
                    true, content, getProviderType(), _modelName, "");
        }
        catch (IOException | InterruptedException | JSONException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            _log.error("Unable to complete OpenAI chat request", exception);

            return new AIResponse(
                    false, "", getProviderType(), _modelName,
                    "OpenAI request failed: " + exception.getMessage());
        }
    }

    @Override
    public String getProviderType() {
        return AIProviderType.OPENAI.name();
    }

    private JSONArray _createMessagesJSONArray(AIRequest aiRequest) {
        JSONArray jsonArray = JSONFactoryUtil.createJSONArray();
        JSONObject systemJSONObject = JSONFactoryUtil.createJSONObject();

        StringBundler systemPromptSB = new StringBundler();

        systemPromptSB.append(aiRequest.getSystemPrompt());

        if (!aiRequest.getRuntimeContext().isBlank()) {
            systemPromptSB.append("\n\n");
            systemPromptSB.append(aiRequest.getRuntimeContext());
        }

        systemJSONObject.put("content", systemPromptSB.toString());
        systemJSONObject.put("role", "system");

        JSONObject userJSONObject = JSONFactoryUtil.createJSONObject();

        userJSONObject.put("content", aiRequest.getPrompt());
        userJSONObject.put("role", "user");

        jsonArray.put(systemJSONObject);
        jsonArray.put(userJSONObject);

        return jsonArray;
    }

    private String _getErrorMessage(HttpResponse<String> httpResponse) {
        try {
            JSONObject responseJSONObject = JSONFactoryUtil.createJSONObject(
                    httpResponse.body());
            JSONObject errorJSONObject = responseJSONObject.getJSONObject(
                    "error");

            if (errorJSONObject != null) {
                String message = errorJSONObject.getString("message");

                if ((message != null) && !message.isBlank()) {
                    return StringBundler.concat(
                            "OpenAI returned ", String.valueOf(httpResponse.statusCode()),
                            ": ", message);
                }
            }
        }
        catch (Exception exception) {
            _log.debug("Unable to parse OpenAI error response", exception);
        }

        return "OpenAI returned " + httpResponse.statusCode() + ".";
    }

    private String _getString(
            Map<String, Object> properties, String key, String defaultValue) {

        Object value = properties.get(key);

        if (value == null) {
            return defaultValue;
        }

        String stringValue = String.valueOf(value).trim();

        if (stringValue.isEmpty()) {
            return defaultValue;
        }

        return stringValue;
    }

    private static final Log _log = LogFactoryUtil.getLog(
            PortalOpsOpenAiProviderComponent.class);

    private String _apiKey;
    private final HttpClient _httpClient = HttpClient.newHttpClient();
    private String _modelName;

}
