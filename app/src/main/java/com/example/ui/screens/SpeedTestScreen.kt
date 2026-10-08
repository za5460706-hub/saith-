package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnSurface
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SpaceGrotesk
import com.example.ui.theme.SpaceMono
import com.example.ui.theme.SurfaceContainer
import com.example.ui.theme.SurfaceContainerHigh
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest
import com.example.ui.theme.TertiaryContainer
import com.example.viewmodel.NetPulseUiState
import com.example.viewmodel.NetPulseViewModel

@Composable
fun SpeedTestScreen(
    uiState: NetPulseUiState,
    viewModel: NetPulseViewModel,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uiState.speedProgressPercent,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "gaugeProgress"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceContainerLowest)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // 1. Gauge Bento Pod
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, Primary.copy(alpha = 0.2f), RoundedCornerShape(14.dp))
                    .padding(16.dp)
                    .testTag("speed_gauge_pod")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Status Header Inside Pod
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(Primary, CircleShape)
                            )
                            Text(
                                text = "RF_LINK // 5.8GHZ",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.1.sp,
                                color = Primary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceContainerHigh)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "BUFF_OK",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    // Gauge Dial Stage Canvas
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val neonBrush = Brush.sweepGradient(
                            listOf(
                                SecondaryContainer,
                                Primary,
                                PrimaryContainer,
                                SecondaryContainer
                            )
                        )

                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val trackColor = Color(0xFF1C1F2A)

                            // Background Track Arc (240 degree arc)
                            drawArc(
                                color = trackColor,
                                startAngle = 150f,
                                sweepAngle = 240f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Active Neon Arc
                            val activeSweep = (240f * animatedProgress).coerceIn(4f, 240f)
                            drawArc(
                                brush = neonBrush,
                                startAngle = 150f,
                                sweepAngle = activeSweep,
                                useCenter = false,
                                style = Stroke(width = strokeWidth + 2f, cap = StrokeCap.Round)
                            )
                        }

                        // Center Holographic Digital Readout
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "DOWNLOAD SPEED",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.12.sp,
                                color = Secondary
                            )
                            Text(
                                text = String.format("%.1f", uiState.currentDownloadSpeed),
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 46.sp,
                                lineHeight = 48.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "MBPS",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 0.08.sp,
                                color = Primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SurfaceContainerHigh.copy(alpha = 0.8f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "PEAK 210 Mbps",
                                        fontFamily = SpaceMono,
                                        fontSize = 10.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    // Primary Action Button (TEST AGAIN)
                    Button(
                        onClick = { viewModel.triggerSpeedTest() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("trigger_test_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryContainer,
                            contentColor = OnPrimary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (uiState.isTestingSpeed) "TESTING..." else "TEST AGAIN",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                letterSpacing = 0.1.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            // 2. Real-time Diagnostics Strip (Latency, Jitter, Packet Loss)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricStripBox(
                    label = "LATENCY",
                    value = "${uiState.latencyMs}",
                    unit = "ms",
                    color = SecondaryContainer,
                    barPercent = 0.15f,
                    modifier = Modifier.weight(1f)
                )
                MetricStripBox(
                    label = "JITTER",
                    value = "${uiState.jitterMs}",
                    unit = "ms",
                    color = Primary,
                    barPercent = 0.10f,
                    modifier = Modifier.weight(1f)
                )
                MetricStripBox(
                    label = "PACKET LOSS",
                    value = "${uiState.packetLossPercent}",
                    unit = "%",
                    color = Secondary,
                    barPercent = 0.02f,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            // 3. Dual Result Cards (Download & Upload)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Download Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = null,
                                    tint = SecondaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "DOWNLOAD",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = OnSurface
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(SecondaryContainer, CircleShape)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format("%.1f", uiState.currentDownloadSpeed),
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mbps",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceContainerHigh.copy(alpha = 0.6f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("PEAK REF", fontFamily = SpaceMono, fontSize = 9.sp, color = OnSurfaceVariant)
                                Text("210 Mbps", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = Secondary)
                            }
                        }
                    }
                }

                // Upload Card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainerLow)
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Upload,
                                    contentDescription = null,
                                    tint = TertiaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "UPLOAD",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = OnSurface
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(TertiaryContainer, CircleShape)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = String.format("%.1f", uiState.currentUploadSpeed),
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Mbps",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(4.dp))
                                .background(SurfaceContainerHigh.copy(alpha = 0.6f))
                                .padding(horizontal = 6.dp, vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("PEAK REF", fontFamily = SpaceMono, fontSize = 9.sp, color = OnSurfaceVariant)
                                Text("50 Mbps", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 9.sp, color = TertiaryContainer)
                            }
                        }
                    }
                }
            }
        }

        item {
            // 4. Server & ISP Intelligence Pod
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NETWORK CARRIER & NODE",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.1.sp,
                            color = OnSurfaceVariant
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Primary.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "OPTIMIZED",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = Primary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Hub,
                                contentDescription = null,
                                tint = SecondaryContainer,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "ISP IDENTIFIER",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = "PTCL / CyberNet Fiber Optics",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "IPv4: 182.185.124.90 • AS45652",
                                fontFamily = SpaceMono,
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Dns,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "ROUTING TARGET",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = OnSurfaceVariant
                            )
                            Text(
                                text = "Islamabad / Karachi Node #04",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "Direct Optical Peer (Ultra-Low Hop)",
                                fontFamily = SpaceMono,
                                fontSize = 10.sp,
                                color = SecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        item {
            // 5. Recent Test History Logs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DIAGNOSTIC LOGS",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.1.sp,
                    color = OnSurfaceVariant
                )
                Text(
                    text = "VIEW FULL ARCHIVE",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Secondary
                )
            }
        }

        items(uiState.speedHistory, key = { it.id }) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainerLow)
                    .padding(12.dp)
            ) {
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
                                .size(32.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Column {
                            Text(
                                text = item.timestamp,
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "SSID: ${item.ssid}",
                                fontFamily = SpaceMono,
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${item.downloadMbps.toInt()}",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Primary
                            )
                            Text(
                                text = " / ${item.uploadMbps.toInt()}",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                        Text(
                            text = "DOWN / UP (MBPS)",
                            fontFamily = SpaceMono,
                            fontSize = 8.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MetricStripBox(
    label: String,
    value: String,
    unit: String,
    color: Color,
    barPercent: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainerLow)
            .padding(10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                fontFamily = SpaceMono,
                fontSize = 9.sp,
                color = OnSurfaceVariant
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = color
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = unit,
                    fontFamily = SpaceMono,
                    fontSize = 10.sp,
                    color = color
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SurfaceContainerHigh)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(barPercent)
                        .height(3.dp)
                        .background(color)
                )
            }
        }
    }
}
