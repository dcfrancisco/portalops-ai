package com.portalops.api.command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PortalOpsCommandResult implements Serializable {

    public PortalOpsCommandResult(
            PortalOpsCommandType commandType, List<String> lines, String summary,
            String title) {

        _commandType = Objects.requireNonNull(commandType);
        _lines = Collections.unmodifiableList(new ArrayList<>(lines));
        _summary = Objects.requireNonNull(summary);
        _title = Objects.requireNonNull(title);
    }

    public PortalOpsCommandType getCommandType() {
        return _commandType;
    }

    public List<String> getLines() {
        return _lines;
    }

    public String getSummary() {
        return _summary;
    }

    public String getTitle() {
        return _title;
    }

    private final PortalOpsCommandType _commandType;
    private final List<String> _lines;
    private final String _summary;
    private final String _title;

}