package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.ClientType
import com.example.model.ConnectedClient
import com.example.model.ScreenTab
import com.example.model.SpeedTestResult
import com.example.model.WifiNode
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

data class NetPulseUiState(
    val currentTab: ScreenTab = ScreenTab.SCANNER,
    // Scanner
    val isScanning: Boolean = false,
    val activeFilter: String = "ALL", // "ALL", "5GHZ", "OPEN", "SECURE"
    val connectedNode: WifiNode = WifiNode(
        id = "active_1",
        ssid = "Office_CyberNet_5G",
        bssid = "74:83:C2:A1:9F:30",
        signalDbm = -42,
        frequencyBand = "5 GHz",
        channel = 36,
        channelWidthMhz = 80,
        security = "WPA3-SAE",
        speedMbps = 866,
        isConnected = true,
        autoJoin = true,
        passwordKey = "CyberNet#2026Wpa3Key!"
    ),
    val discoveredNodes: List<WifiNode> = listOf(
        WifiNode(
            id = "node_1",
            ssid = "SkyLink_Fiber_Guest",
            bssid = "20:4E:7F:1B:90:3A",
            signalDbm = -55,
            frequencyBand = "5 GHz",
            channel = 149,
            channelWidthMhz = 80,
            security = "Open",
            speedMbps = 300,
            isOpen = true,
            autoJoin = false,
            syncedTime = "3 mins ago",
            passwordKey = ""
        ),
        WifiNode(
            id = "node_2",
            ssid = "Falcon_Private_HQ",
            bssid = "50:C7:BF:33:14:8E",
            signalDbm = -68,
            frequencyBand = "2.4 GHz",
            channel = 6,
            channelWidthMhz = 20,
            security = "WPA2-Enterprise",
            speedMbps = 144,
            autoJoin = true,
            syncedTime = "1 hour ago",
            passwordKey = "FalconSecure_99"
        ),
        WifiNode(
            id = "node_3",
            ssid = "Cafe_Aroma_FreeWiFi",
            bssid = "D8:07:B6:81:9A:2F",
            signalDbm = -72,
            frequencyBand = "2.4 GHz",
            channel = 11,
            channelWidthMhz = 20,
            security = "Open / Captive",
            speedMbps = 72,
            isOpen = true,
            autoJoin = false,
            syncedTime = "Yesterday",
            passwordKey = ""
        ),
        WifiNode(
            id = "node_4",
            ssid = "Quantum_Mesh_Ext",
            bssid = "BC:A5:11:09:44:CD",
            signalDbm = -78,
            frequencyBand = "5 GHz",
            channel = 157,
            channelWidthMhz = 80,
            security = "WPA3",
            speedMbps = 450,
            autoJoin = true,
            syncedTime = "3 days ago",
            passwordKey = "Quantum_Mesh!2026"
        )
    ),
    // Diagnostics & LAN Inspection
    val isTestingDiagnostics: Boolean = false,
    val diagnosticResultAvailable: Boolean = true,
    val diagnosticToastMessage: String? = null,
    val showAdminCreds: Boolean = false,
    val adminUsername: String = "admin_root",
    val adminPassword: String = "NetFiber@2024Secure",
    val isPasswordMasked: Boolean = true,
    val clientFilter: String = "ALL", // "ALL", "MOBILE", "LAPTOP", "IOT"
    val clientSearchQuery: String = "",
    val clients: List<ConnectedClient> = listOf(
        ConnectedClient(
            id = "c_1",
            name = "iPhone 15 Pro Max",
            type = ClientType.MOBILE,
            ip = "192.168.1.105",
            mac = "3C:06:30:8F:1E:44",
            band = "5.8 GHz",
            rssiDbm = -45,
            speedMbps = 866,
            status = "This Device",
            trafficDown = "3.2 MB/s",
            isThisDevice = true
        ),
        ConnectedClient(
            id = "c_2",
            name = "Galaxy S24 Ultra",
            type = ClientType.MOBILE,
            ip = "192.168.1.112",
            mac = "8A:94:10:E2:B5:7C",
            band = "5 GHz",
            rssiDbm = -52,
            speedMbps = 433,
            status = "Mobile",
            trafficDown = "18.2 MB/s"
        ),
        ConnectedClient(
            id = "c_3",
            name = "Redmi Note 13",
            type = ClientType.MOBILE,
            ip = "192.168.1.118",
            mac = "44:D8:1A:3C:99:A1",
            band = "2.4 GHz",
            rssiDbm = -68,
            speedMbps = 72,
            status = "Mobile",
            trafficDown = "1.1 MB/s"
        ),
        ConnectedClient(
            id = "c_4",
            name = "OnePlus 12",
            type = ClientType.MOBILE,
            ip = "192.168.1.125",
            mac = "62:B1:84:F0:32:E9",
            band = "5 GHz",
            rssiDbm = -48,
            speedMbps = 650,
            status = "Streaming 4K",
            trafficDown = "12.4 MB/s"
        ),
        ConnectedClient(
            id = "c_5",
            name = "Vivo V30 Pro",
            type = ClientType.MOBILE,
            ip = "192.168.1.130",
            mac = "9C:35:5B:74:10:3B",
            band = "2.4 GHz",
            rssiDbm = -75,
            speedMbps = 54,
            status = "Sleep Mode",
            trafficDown = "0.0 MB/s"
        ),
        ConnectedClient(
            id = "c_6",
            name = "MacBook Pro M3",
            type = ClientType.LAPTOP,
            ip = "192.168.1.102",
            mac = "A0:78:17:92:EF:55",
            band = "5 GHz",
            rssiDbm = -39,
            speedMbps = 1200,
            status = "Workstation",
            trafficDown = "8.6 MB/s"
        ),
        ConnectedClient(
            id = "c_7",
            name = "Dell XPS 15",
            type = ClientType.LAPTOP,
            ip = "192.168.1.108",
            mac = "74:E5:F9:88:21:40",
            band = "5 GHz",
            rssiDbm = -55,
            speedMbps = 866,
            status = "Laptop",
            trafficDown = "2.3 MB/s"
        ),
        ConnectedClient(
            id = "c_8",
            name = "Samsung 4K QLED TV",
            type = ClientType.IOT,
            ip = "192.168.1.140",
            mac = "F0:2F:A8:61:9D:C2",
            band = "5 GHz Band",
            rssiDbm = -46,
            speedMbps = 866,
            status = "Ethernet Mode",
            trafficDown = "11.8 MB/s"
        )
    ),
    // Speed Test
    val isTestingSpeed: Boolean = false,
    val speedProgressPercent: Float = 0.85f,
    val currentDownloadSpeed: Float = 184.5f,
    val currentUploadSpeed: Float = 42.8f,
    val peakDownloadSpeed: Float = 210.0f,
    val peakUploadSpeed: Float = 50.0f,
    val latencyMs: Int = 12,
    val jitterMs: Float = 2.1f,
    val packetLossPercent: Float = 0.0f,
    val speedHistory: List<SpeedTestResult> = listOf(
        SpeedTestResult("h_1", "Today, 2:15 PM", "CyberPulse-5G", 179f, 40f, 11, 1.8f, 0.0f),
        SpeedTestResult("h_2", "Yesterday, 9:30 AM", "CyberPulse-5G", 165f, 38f, 14, 2.5f, 0.0f),
        SpeedTestResult("h_3", "Oct 06, 4:10 PM", "Office_CyberNet_5G", 188f, 44f, 10, 1.9f, 0.0f)
    ),
    // Saved Profiles & Hotspot
    val isHotspotActive: Boolean = true,
    val hotspotSsid: String = "NetPulse_Portable",
    val hotspotBand: String = "5.0 GHz Ultra",
    val hotspotClientsCount: Int = 2,
    val hotspotClientsPreview: String = "Pixel_9_Pro, Rig_Node_02",
    val hotspotTxRate: String = "4.2 MB/s",
    val savedSearchQuery: String = "",
    val savedProfiles: List<WifiNode> = listOf(
        WifiNode(
            id = "saved_1",
            ssid = "Office_CyberNet_5G",
            bssid = "74:83:C2:A1:9F:30",
            signalDbm = -42,
            frequencyBand = "5 GHz",
            channel = 149,
            security = "WPA3-SAE",
            speedMbps = 866,
            isConnected = true,
            autoJoin = true,
            syncedTime = "Just now",
            passwordKey = "CyberNet#2026Wpa3Key!"
        ),
        WifiNode(
            id = "saved_2",
            ssid = "Home_Fiber_Ultra",
            bssid = "C8:3A:35:10:8B:11",
            signalDbm = -54,
            frequencyBand = "5 GHz",
            channel = 36,
            security = "WPA2-PSK",
            speedMbps = 600,
            isPrioritized = true,
            autoJoin = true,
            syncedTime = "Yesterday, 22:40",
            passwordKey = "HomeFiberUlt77!"
        ),
        WifiNode(
            id = "saved_3",
            ssid = "Studio_WiFi_Guests",
            bssid = "14:DD:A9:72:EE:90",
            signalDbm = -66,
            frequencyBand = "2.4 GHz",
            channel = 1,
            security = "Open / Captive",
            speedMbps = 54,
            isOpen = true,
            autoJoin = false,
            syncedTime = "3 days ago",
            passwordKey = ""
        ),
        WifiNode(
            id = "saved_4",
            ssid = "CoWorking_Space_Main",
            bssid = "00:26:86:F0:4B:AA",
            signalDbm = -58,
            frequencyBand = "5 GHz",
            channel = 44,
            security = "WPA2-Enterprise",
            speedMbps = 450,
            autoJoin = true,
            syncedTime = "1 week ago",
            passwordKey = "CorpRadius2026!"
        )
    ),
    // QR Code Share Sheet State
    val qrModalNode: WifiNode? = null,
    val isPasswordRevealedInQr: Boolean = false,
    val isWpsPushActive: Boolean = false,
    val wpsRemainingSeconds: Int = 118,
    val toastMessage: String? = null
)

class NetPulseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NetPulseUiState())
    val uiState: StateFlow<NetPulseUiState> = _uiState.asStateFlow()

    private var speedTestJob: Job? = null
    private var wpsJob: Job? = null

    fun selectTab(tab: ScreenTab) {
        _uiState.update { it.copy(currentTab = tab) }
    }

    fun rescanSpectrum() {
        if (_uiState.value.isScanning) return
        viewModelScope.launch {
            _uiState.update { it.copy(isScanning = true) }
            delay(1400)
            // Mutate signal levels subtly for authentic dynamic telemetry
            val updated = _uiState.value.discoveredNodes.map { node ->
                val delta = Random.nextInt(-3, 4)
                node.copy(signalDbm = (node.signalDbm + delta).coerceIn(-95, -35))
            }
            _uiState.update {
                it.copy(
                    isScanning = false,
                    discoveredNodes = updated,
                    toastMessage = "RF Spectrum sweep finished. 14 channels calibrated."
                )
            }
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun setScannerFilter(filter: String) {
        _uiState.update { it.copy(activeFilter = filter) }
    }

    fun toggleHotspot() {
        _uiState.update {
            val newState = !it.isHotspotActive
            it.copy(
                isHotspotActive = newState,
                toastMessage = if (newState) "Personal Hotspot beacon broadcasting on 5GHz" else "Personal Hotspot disabled"
            )
        }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun setSavedSearchQuery(query: String) {
        _uiState.update { it.copy(savedSearchQuery = query) }
    }

    fun toggleAutoJoin(nodeId: String) {
        _uiState.update { state ->
            val updatedProfiles = state.savedProfiles.map {
                if (it.id == nodeId) it.copy(autoJoin = !it.autoJoin) else it
            }
            val target = updatedProfiles.find { it.id == nodeId }
            state.copy(
                savedProfiles = updatedProfiles,
                toastMessage = target?.let { "${it.ssid} auto-join set to ${if (it.autoJoin) "ON" else "OFF"}" }
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun forgetNetwork(nodeId: String) {
        _uiState.update { state ->
            val removed = state.savedProfiles.find { it.id == nodeId }
            state.copy(
                savedProfiles = state.savedProfiles.filterNot { it.id == nodeId },
                toastMessage = removed?.let { "Network profile forgotten: ${it.ssid}" }
            )
        }
        viewModelScope.launch {
            delay(2000)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun openQrModal(node: WifiNode) {
        _uiState.update { it.copy(qrModalNode = node, isPasswordRevealedInQr = false) }
    }

    fun closeQrModal() {
        _uiState.update { it.copy(qrModalNode = null, isPasswordRevealedInQr = false) }
    }

    fun toggleQrPasswordVisibility() {
        _uiState.update { it.copy(isPasswordRevealedInQr = !it.isPasswordRevealedInQr) }
    }

    fun triggerSpeedTest() {
        if (_uiState.value.isTestingSpeed) return
        speedTestJob?.cancel()
        speedTestJob = viewModelScope.launch {
            _uiState.update { it.copy(isTestingSpeed = true) }
            val baseTarget = 184.5f + Random.nextInt(-10, 20)
            var currentVal = 12f
            for (step in 1..25) {
                delay(80)
                currentVal = (baseTarget * (step / 25f)) + Random.nextFloat() * 8f
                val ping = (10 + Random.nextInt(0, 5))
                val jitter = (1.5f + Random.nextFloat() * 1.2f)
                _uiState.update {
                    it.copy(
                        currentDownloadSpeed = currentVal,
                        latencyMs = ping,
                        jitterMs = (jitter * 10).toInt() / 10f,
                        speedProgressPercent = (currentVal / 220f).coerceIn(0.1f, 1.0f)
                    )
                }
            }
            val finalDown = baseTarget
            val finalUp = 42.8f + Random.nextInt(-3, 6)
            val newRecord = SpeedTestResult(
                id = "h_${System.currentTimeMillis()}",
                timestamp = "Just now",
                ssid = _uiState.value.connectedNode.ssid,
                downloadMbps = (finalDown * 10).toInt() / 10f,
                uploadMbps = (finalUp * 10).toInt() / 10f,
                pingMs = _uiState.value.latencyMs,
                jitterMs = _uiState.value.jitterMs,
                packetLossPercent = 0.0f
            )
            _uiState.update {
                it.copy(
                    isTestingSpeed = false,
                    currentDownloadSpeed = (finalDown * 10).toInt() / 10f,
                    currentUploadSpeed = (finalUp * 10).toInt() / 10f,
                    speedHistory = listOf(newRecord) + it.speedHistory.take(4),
                    toastMessage = "Speed test complete: ${newRecord.downloadMbps} Mbps Download"
                )
            }
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun runDiagnosticsTest() {
        if (_uiState.value.isTestingDiagnostics) return
        viewModelScope.launch {
            _uiState.update { it.copy(isTestingDiagnostics = true, diagnosticToastMessage = null) }
            delay(2200)
            _uiState.update {
                it.copy(
                    isTestingDiagnostics = false,
                    diagnosticResultAvailable = true,
                    diagnosticToastMessage = "Diagnostic Complete: 30/30 packets received • 0% loss • 1.1ms jitter"
                )
            }
            delay(4000)
            _uiState.update { it.copy(diagnosticToastMessage = null) }
        }
    }

    fun toggleAdminCredsDrawer() {
        _uiState.update { it.copy(showAdminCreds = !it.showAdminCreds) }
    }

    fun toggleAdminPasswordMask() {
        _uiState.update { it.copy(isPasswordMasked = !it.isPasswordMasked) }
    }

    fun setClientFilter(filter: String) {
        _uiState.update { it.copy(clientFilter = filter) }
    }

    fun setClientSearchQuery(q: String) {
        _uiState.update { it.copy(clientSearchQuery = q) }
    }

    fun throttleClient(clientName: String) {
        _uiState.update { state ->
            val updated = state.clients.map {
                if (it.name == clientName) it.copy(isThrottled = !it.isThrottled) else it
            }
            val target = updated.find { it.name == clientName }
            state.copy(
                clients = updated,
                toastMessage = target?.let {
                    if (it.isThrottled) "Traffic rule applied: ${it.name} bandwidth capped"
                    else "Traffic rule lifted: ${it.name} restored"
                }
            )
        }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun triggerIntruderScan() {
        viewModelScope.launch {
            _uiState.update { it.copy(toastMessage = "Scanning ARP & routing tables for rogue devices...") }
            delay(1500)
            _uiState.update { it.copy(toastMessage = "Watchdog: 8 clients verified. No rogue or unauthorized nodes.") }
            delay(3000)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }

    fun startWpsPush() {
        wpsJob?.cancel()
        _uiState.update { it.copy(isWpsPushActive = true, wpsRemainingSeconds = 120) }
        wpsJob = viewModelScope.launch {
            for (sec in 120 downTo 0) {
                delay(1000)
                _uiState.update { it.copy(wpsRemainingSeconds = sec) }
            }
            _uiState.update { it.copy(isWpsPushActive = false) }
        }
    }

    fun cancelWps() {
        wpsJob?.cancel()
        _uiState.update { it.copy(isWpsPushActive = false) }
    }

    fun addHiddenSsid(ssid: String) {
        if (ssid.isBlank()) return
        val newProfile = WifiNode(
            id = "hidden_${System.currentTimeMillis()}",
            ssid = ssid,
            bssid = "00:00:00:00:00:00",
            signalDbm = -50,
            frequencyBand = "5 GHz",
            channel = 36,
            security = "WPA3-SAE",
            speedMbps = 600,
            autoJoin = true,
            syncedTime = "Just now"
        )
        _uiState.update {
            it.copy(
                savedProfiles = listOf(newProfile) + it.savedProfiles,
                toastMessage = "Hidden node profile '$ssid' registered in Vault"
            )
        }
        viewModelScope.launch {
            delay(2500)
            _uiState.update { it.copy(toastMessage = null) }
        }
    }
}
