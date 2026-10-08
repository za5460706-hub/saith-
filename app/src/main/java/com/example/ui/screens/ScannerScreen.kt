package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiChannel
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WifiNode
import com.example.ui.theme.Error
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SpaceGrotesk
import com.example.ui.theme.SpaceMono
import com.example.ui.theme.SurfaceBright
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.viewmodel.NetPulseUiState
import com.example.viewmodel.NetPulseViewModel

@Composable
fun ScannerScreen(
    uiState: NetPulseUiState,
    viewModel: NetPulseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val infiniteTransition = rememberInfiniteTransition(label = "scannerGlow")
    val radarRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarSpin"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pingDot"
    )

    val filteredNodes = when (uiState.activeFilter) {
        "5GHZ" -> uiState.discoveredNodes.filter { it.frequencyBand.contains("5") }
        "OPEN" -> uiState.discoveredNodes.filter { it.isOpen }
        "SECURE" -> uiState.discoveredNodes.filter { !it.isOpen }
        else -> uiState.discoveredNodes
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceContainerLowest)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // 1. Radar / RF Pulse Sweep Scanner Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, Primary.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("radar_sweep_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Header row with animated radar widget & Rescan button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceContainerLowest)
                                    .border(1.dp, Primary.copy(alpha = 0.4f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WifiTethering,
                                    contentDescription = "Radar",
                                    tint = Primary,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .rotate(if (uiState.isScanning) radarRotation * 2 else radarRotation)
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "RF SPECTRUM SWEEP",
                                        fontFamily = SpaceMono,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.1.sp,
                                        color = Primary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .background(SecondaryContainer, CircleShape)
                                    )
                                }
                                Text(
                                    text = if (uiState.isScanning) "Scanning 2.4 GHz & 5 GHz channels..." else "Active 2.4 GHz & 5 GHz calibrated",
                                    fontFamily = SpaceMono,
                                    fontSize = 11.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        // Rescan Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHigh)
                                .clickable { viewModel.rescanSpectrum() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                .testTag("rescan_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Rescan",
                                    tint = SecondaryContainer,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .rotate(if (uiState.isScanning) radarRotation else 0f)
                                )
                                Text(
                                    text = if (uiState.isScanning) "SCANNING" else "RESCAN",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.08.sp,
                                    color = Secondary
                                )
                            }
                        }
                    }

                    // Live Band Metrics Visual Segment
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLowest.copy(alpha = 0.8f))
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(Primary, CircleShape)
                                )
                                Text(
                                    text = "CH 1-13 (2.4G)",
                                    fontFamily = SpaceMono,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                            Text(
                                text = "6 ACTIVE",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Primary
                            )
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerLow)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(SecondaryContainer, CircleShape)
                                )
                                Text(
                                    text = "UNII 1-3 (5G)",
                                    fontFamily = SpaceMono,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                            Text(
                                text = "8 ACTIVE",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = SecondaryContainer
                            )
                        }
                    }

                    // Filter Chips (ALL, 5GHZ, OPEN, SECURE)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterChip(
                                label = "ALL (14)",
                                isSelected = uiState.activeFilter == "ALL",
                                onClick = { viewModel.setScannerFilter("ALL") },
                                testTag = "filter_all"
                            )
                        }
                        item {
                            FilterChip(
                                label = "5 GHZ FAST",
                                icon = Icons.Default.Bolt,
                                isSelected = uiState.activeFilter == "5GHZ",
                                onClick = { viewModel.setScannerFilter("5GHZ") },
                                testTag = "filter_5ghz"
                            )
                        }
                        item {
                            FilterChip(
                                label = "OPEN (2)",
                                icon = Icons.Default.LockOpen,
                                isSelected = uiState.activeFilter == "OPEN",
                                onClick = { viewModel.setScannerFilter("OPEN") },
                                testTag = "filter_open"
                            )
                        }
                        item {
                            FilterChip(
                                label = "SECURE (12)",
                                icon = Icons.Default.Shield,
                                isSelected = uiState.activeFilter == "SECURE",
                                onClick = { viewModel.setScannerFilter("SECURE") },
                                testTag = "filter_secure"
                            )
                        }
                    }
                }
            }
        }

        item {
            // 2. Primary Connected Network Card
            val active = uiState.connectedNode
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerHigh)
                    .border(1.dp, Primary.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("active_network_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .alpha(pulseAlpha)
                                        .background(Primary, CircleShape)
                                )
                                Text(
                                    text = "ACTIVE LINK • ONLINE",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.1.sp,
                                    color = Primary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = active.ssid,
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = OnSurface
                            )
                        }

                        // RSSI badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerLowest)
                                .border(1.dp, Primary.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Wifi,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${active.signalDbm} dBm",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Primary
                                )
                            }
                        }
                    }

                    // Metric Strip Grid (PHY Rate, Channel, Protocol)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLowest.copy(alpha = 0.9f))
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "PHY RATE",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = OnSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${active.speedMbps}",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Secondary
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Mbps",
                                    fontFamily = SpaceMono,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "CHANNEL",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = OnSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${active.channel}",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = OnSurface
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "(${active.channelWidthMhz} MHz)",
                                    fontFamily = SpaceMono,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "PROTOCOL",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = "WPA3-P",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Primary
                            )
                        }
                    }

                    // Action Buttons Row (Manage, Share QR)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                Toast.makeText(context, "Configuring ${active.ssid}", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("active_manage_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary,
                                contentColor = OnPrimary
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SettingsEthernet,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "MANAGE",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.08.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.openQrModal(active) },
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("active_share_qr_btn"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SurfaceContainerLowest,
                                contentColor = SecondaryContainer
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.QrCode2,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "SHARE QR",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    letterSpacing = 0.08.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // Discovered Nodes Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "DISCOVERED NODES",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.08.sp,
                        color = OnSurface
                    )
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${filteredNodes.size} Nearby",
                            fontFamily = SpaceMono,
                            fontSize = 10.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "SORT: SIGNAL",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = OnSurfaceVariant
                )
            }
        }

        // Discovered Networks List
        items(filteredNodes, key = { it.id }) { node ->
            DiscoveredNodeCard(
                node = node,
                onConnect = {
                    Toast.makeText(context, "Initiating handshake with ${node.ssid}", Toast.LENGTH_SHORT).show()
                },
                onInfo = {
                    viewModel.openQrModal(node)
                }
            )
        }

        item {
            // Telemetry Footnote / Status Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerLowest)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Primary, CircleShape)
                    )
                    Text(
                        text = "ANTENNA: DUAL POLARITY MIMO",
                        fontFamily = SpaceMono,
                        fontSize = 9.sp,
                        letterSpacing = 0.1.sp,
                        color = OnSurfaceVariant
                    )
                }
                Text(
                    text = "REFRESH: 2.4s",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.sp,
                    color = Secondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Primary else SurfaceContainerHigh)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) OnPrimary else SecondaryContainer,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = label,
                fontFamily = SpaceMono,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.08.sp,
                color = if (isSelected) OnPrimary else OnSurface
            )
        }
    }
}

@Composable
private fun DiscoveredNodeCard(
    node: WifiNode,
    onConnect: () -> Unit,
    onInfo: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainer)
            .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(12.dp)
            .testTag("node_${node.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerLowest),
                    contentAlignment = Alignment.Center
                ) {
                    val icon = when {
                        node.ssid.contains("Cafe", ignoreCase = true) -> Icons.Default.WifiTethering
                        node.ssid.contains("Quantum", ignoreCase = true) -> Icons.Default.WifiChannel
                        else -> Icons.Default.Wifi
                    }
                    val iconColor = when {
                        node.signalDbm > -60 -> Primary
                        node.signalDbm > -70 -> SecondaryContainer
                        else -> OnSurfaceVariant
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = node.ssid,
                            fontFamily = SpaceGrotesk,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = OnSurface
                        )
                        if (node.isOpen) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(SecondaryContainer, CircleShape)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(SurfaceContainerLowest)
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = node.frequencyBand,
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = SecondaryContainer
                            )
                        }

                        if (node.isOpen) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(ErrorContainer.copy(alpha = 0.4f))
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = "OPEN",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = Error
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(SurfaceContainerHighest)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = node.security,
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = "${node.speedMbps} Mbps",
                            fontFamily = SpaceMono,
                            fontSize = 10.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            // Signal dBm & Action Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    val signalColor = when {
                        node.signalDbm > -60 -> Primary
                        node.signalDbm > -70 -> Secondary
                        node.signalDbm > -75 -> OnSurfaceVariant
                        else -> Error
                    }
                    val quality = when {
                        node.signalDbm > -60 -> "GOOD"
                        node.signalDbm > -70 -> "FAIR"
                        node.signalDbm > -75 -> "WEAK"
                        else -> "FRINGE"
                    }
                    Text(
                        text = "${node.signalDbm} dBm",
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = signalColor
                    )
                    Text(
                        text = quality,
                        fontFamily = SpaceMono,
                        fontSize = 8.sp,
                        color = OnSurfaceVariant
                    )
                }

                if (node.isOpen) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Primary.copy(alpha = 0.2f))
                            .clickable(onClick = onConnect)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("connect_${node.id}")
                    ) {
                        Text(
                            text = "CONNECT",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Primary
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighest)
                            .clickable(onClick = onInfo)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("info_${node.id}")
                    ) {
                        Text(
                            text = "INFO",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = OnSurface
                        )
                    }
                }
            }
        }
    }
}
