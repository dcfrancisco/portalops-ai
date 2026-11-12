# ADR-0010: Physical-world perception and actuation

Status: Proposed

## Context

ADR-0009 gives the OS voice and vision for portals and digital artifacts. Portal operations also touch the physical world: kiosks and digital signage that display portal content, meeting rooms and service desks where operators speak to the system, cameras and microphones on site, and facility, data-center and IoT sensors (temperature, power, door state) that affect service health. Operators want the OS to hear, see and, in limited cases, act in these spaces. Physical sensing and actuation raise privacy, safety and reliability risks beyond software-only operations.

## Decision

Extend the senses model to physical devices through an **edge gateway** and a **device registry**, under the same kernel, policy and audit path.

- **Edge gateway** (`portalops-edge`, a separate deployable, not an OSGi bundle): runs near devices, speaks device protocols (MQTT, RTSP and WebRTC for media, OPC UA and HTTP for sensors), performs on-device preprocessing (voice activity detection, motion detection, redaction, local inference where configured) and forwards events over mutually authenticated, outbound-only connections. It buffers when offline.
- **Device registry** (`portalops-device`): identity (certificate per device), type, location and zone, owner, capabilities, firmware, health, consent and retention settings. Unregistered devices are ignored.
- **Physical modalities** added to the senses contract: `PHYSICAL_AUDIO`, `PHYSICAL_VIDEO`, `SENSOR_READING`, `DEVICE_EVENT`. They become `Observation`s with device id, zone and timestamp, flowing through intent, policy and audit unchanged.
- **Telemetry storage:** sensor time series use TimescaleDB on the same PostgreSQL instance (ADR-0006), with retention and downsampling policies. Raw video and audio stay at the edge unless policy allows upload. Derived events and captions are embedded in pgvector.
- **Speak physically:** TTS can be routed to registered speakers, kiosks and signage as spoken or displayed announcements, with zone targeting, rate limits and a content allow-list.
- **Actuation is a tool class** (`effect: PHYSICAL`), disabled by default. Allowed actuations are narrow and reversible (display content on signage, play an announcement, send a notification to a device, raise a ticket). Anything affecting safety systems (locks, power, HVAC, fire, machinery) is out of scope. Each actuation needs a registered device, a policy grant, risk tier 2 or higher, human approval (no autonomy above L2), a rate limit, and a hardware or software kill switch that halts all actuation per zone and globally.
- **Privacy by design:**
  - Face recognition, speaker identification and other biometric identification are **off** and not planned. Detection (presence, count, object, anomaly) is allowed, identification is not.
  - Sensing requires a registered purpose per zone, visible notice, and a retention period. Audio and video are not stored by default.
  - On-device redaction (blur faces and screens, drop audio) runs before anything leaves the edge, unless policy states otherwise.
  - Capture is excluded in sensitive zones (restrooms, private offices) by registry rule.
  - A DPIA template and regional compliance review are required before enabling any camera or microphone.
- **Untrusted physical input:** spoken or displayed instructions in the physical world (a QR code, a sign, a bystander) are data, never commands. Wake word plus an authenticated operator session is required to issue intent by voice.
- **Safety and reliability:** fail safe. If the gateway, the kernel or policy is unreachable, actuators revert to their safe default state and sensing continues locally with buffering. Device and gateway heartbeats feed the health domain. Firmware and gateway updates are signed.
- **Degradation:** physical features are optional. Portal-only deployments are unaffected.

## Consequences

- Positive: operations reach kiosks, signage, rooms and facility signals. Physical incidents (a hot server room, a dead kiosk) become investigations with correlated portal evidence.
- Positive: edge preprocessing keeps raw media local, which reduces bandwidth and privacy exposure.
- Negative: a new deployable, device lifecycle management and a larger attack surface (mitigated by certificates, outbound-only links, signed updates, network segmentation).
- Negative: legal and regulatory exposure for audio and video (consent, works councils, GDPR and biometric laws). Needs legal review before pilots.
- Negative: physical actuation can cause harm, so scope is intentionally narrow and gated by approval, rate limits and kill switches.
- Negative: TimescaleDB adds an extension to operate. If it proves heavy, native PostgreSQL partitioning is the fallback.
