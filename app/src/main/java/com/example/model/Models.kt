package com.example.model

enum class ScreenTab {
    SCANNER,
    DIAGNOSTICS,
    SPEED_TEST,
    SAVED
}

data class WifiNode(
    val id: String,
    val ssid: String,
    val bssid: String,
    val signalDbm: Int,
    val frequencyBand: String, // "2.4 GHz" or "5 GHz"
    val channel: Int,
    val channelWidthMhz: Int = 80,
    val security: String, // "WPA3-SAE", "WPA2-PSK", "WPA2-Enterprise", "Open"
    val speedMbps: Int,
    val isOpen: Boolean = false,
    val isConnected: Boolean = false,
    val isPrioritized: Boolean = false,
    val autoJoin: Boolean = true,
    val syncedTime: String = "Just now",
    val passwordKey: String = "NetPulse@Secure5G!"
)

data class ConnectedClient(
    val id: String,
    val name: String,
    val type: ClientType, // MOBILE, LAPTOP, IOT
    val ip: String,
    val mac: String,
    val band: String,
    val rssiDbm: Int,
    val speedMbps: Int,
    val status: String,
    val trafficDown: String,
    val isThisDevice: Boolean = false,
    val isThrottled: Boolean = false
)

enum class ClientType {
    MOBILE,
    LAPTOP,
    IOT
}

data class SpeedTestResult(
    val id: String,
    val timestamp: String,
    val ssid: String,
    val downloadMbps: Float,
    val uploadMbps: Float,
    val pingMs: Int,
    val jitterMs: Float,
    val packetLossPercent: Float
)
