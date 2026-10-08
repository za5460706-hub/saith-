package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WifiNode
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
import com.example.ui.theme.SurfaceContainerHighest
import com.example.ui.theme.SurfaceContainerLow
import com.example.ui.theme.SurfaceContainerLowest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrShareModalSheet(
    node: WifiNode,
    isPasswordRevealed: Boolean,
    onTogglePassword: () -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isCopied by remember { mutableStateOf(false) }
    var isImageSaved by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceContainerLow,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 6.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(OnSurfaceVariant.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .testTag("qr_modal_sheet"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Modal Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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
                            text = "// QR_EXCHANGE_PROTOCOL",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.1.sp,
                            color = Primary
                        )
                    }
                    Text(
                        text = "Share Network Access",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = OnSurface
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = OnSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Active Network Info Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainer)
                    .padding(10.dp)
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
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Wifi,
                                contentDescription = null,
                                tint = SecondaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = node.ssid,
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = OnSurface
                                )
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                            Text(
                                text = "${node.frequencyBand} (${node.channelWidthMhz} MHz) • CH ${node.channel}",
                                fontFamily = SpaceMono,
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SurfaceContainerHigh)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Primary, modifier = Modifier.size(12.dp))
                            Text(
                                text = node.security,
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = OnSurface
                            )
                        }
                    }
                }
            }

            // High-Contrast QR Code Module
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLowest)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "LOC: [37.7749,-122.4194]",
                            fontFamily = SpaceMono,
                            fontSize = 9.sp,
                            color = OnSurfaceVariant.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "RX_READY",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Primary
                        )
                    }

                    // Rendered Crisp SVG-style QR Code Matrix
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.White)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val moduleSize = size.width / 25f
                            val darkColor = Color(0xFF0F172A)

                            // Helper to draw QR position finder squares
                            fun drawFinder(x: Float, y: Float) {
                                // Outer 7x7 box
                                drawRect(
                                    color = darkColor,
                                    topLeft = Offset(x, y),
                                    size = Size(moduleSize * 7, moduleSize * 7)
                                )
                                // Inner white 5x5
                                drawRect(
                                    color = Color.White,
                                    topLeft = Offset(x + moduleSize, y + moduleSize),
                                    size = Size(moduleSize * 5, moduleSize * 5)
                                )
                                // Inner black 3x3
                                drawRect(
                                    color = darkColor,
                                    topLeft = Offset(x + moduleSize * 2, y + moduleSize * 2),
                                    size = Size(moduleSize * 3, moduleSize * 3)
                                )
                            }

                            // 3 Corner Finders
                            drawFinder(0f, 0f)
                            drawFinder(size.width - moduleSize * 7, 0f)
                            drawFinder(0f, size.height - moduleSize * 7)

                            // Alignment square at bottom right
                            drawRect(
                                color = darkColor,
                                topLeft = Offset(size.width - moduleSize * 8, size.height - moduleSize * 8),
                                size = Size(moduleSize * 5, moduleSize * 5)
                            )
                            drawRect(
                                color = Color.White,
                                topLeft = Offset(size.width - moduleSize * 7, size.height - moduleSize * 7),
                                size = Size(moduleSize * 3, moduleSize * 3)
                            )
                            drawRect(
                                color = darkColor,
                                topLeft = Offset(size.width - moduleSize * 6, size.height - moduleSize * 6),
                                size = Size(moduleSize, moduleSize)
                            )

                            // Simulated data bits matrix
                            val seed = (node.ssid.hashCode().toLong() and 0xFFFFFFFFL)
                            for (r in 0 until 25) {
                                for (c in 0 until 25) {
                                    // Skip finder zones
                                    val inTopLeft = r < 8 && c < 8
                                    val inTopRight = r < 8 && c >= 17
                                    val inBottomLeft = r >= 17 && c < 8
                                    val inBottomRight = r >= 16 && c >= 16
                                    val inCenterBadge = r in 10..14 && c in 10..14

                                    if (!inTopLeft && !inTopRight && !inBottomLeft && !inBottomRight && !inCenterBadge) {
                                        val hash = ((seed * (r * 29 + c * 31 + 7)) % 100).toInt()
                                        if (hash > 45) {
                                            drawRect(
                                                color = darkColor,
                                                topLeft = Offset(c * moduleSize, r * moduleSize),
                                                size = Size(moduleSize, moduleSize)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Center NetPulse logo badge inside QR
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerLowest)
                                .border(1.dp, Primary.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiTethering,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = "Point mobile camera to auto-connect without typing credentials.",
                        fontFamily = SpaceMono,
                        fontSize = 10.sp,
                        color = OnSurfaceVariant
                    )
                }
            }

            // Credential Key & Copy Tray
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceContainer)
                    .padding(10.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "SECURE PASSWORD KEY",
                            fontFamily = SpaceMono,
                            fontSize = 9.sp,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "[ AES-256 GCM ]",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Secondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerLowest)
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(16.dp))
                                Text(
                                    text = if (isPasswordRevealed) node.passwordKey else "••••••••••••••••",
                                    fontFamily = SpaceMono,
                                    fontSize = 12.sp,
                                    color = OnSurface
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = onTogglePassword,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isPasswordRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Reveal",
                                        tint = OnSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(SurfaceContainerHigh)
                                        .clickable {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("Wi-Fi Password", node.passwordKey)
                                            clipboard.setPrimaryClip(clip)
                                            isCopied = true
                                            Toast.makeText(context, "Password copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        }
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .testTag("copy_password_btn")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isCopied) Icons.Default.Done else Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            tint = if (isCopied) Primary else Primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Text(
                                            text = if (isCopied) "COPIED" else "COPY",
                                            fontFamily = SpaceMono,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            color = Primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "URI: WIFI:S:${node.ssid};T:${node.security};P:********;;",
                            fontFamily = SpaceMono,
                            fontSize = 8.sp,
                            color = OnSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "SYNCED",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 8.sp,
                            color = Primary
                        )
                    }
                }
            }

            // Primary CTA: Save QR Image to Gallery
            Button(
                onClick = {
                    isImageSaved = true
                    Toast.makeText(context, "QR Code saved to Gallery", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("save_qr_image_btn"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isImageSaved) Icons.Default.CheckCircle else Icons.Default.Download,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (isImageSaved) "SAVED TO GALLERY" else "Save QR Image to Gallery",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            // Dual Secondary Tactical Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Nearby Share beacon broadcasting...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainer,
                        contentColor = Secondary
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.NearMe, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text("AIRDROP / NEARBY", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                    }
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Printing Wi-Fi access voucher card...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SurfaceContainer,
                        contentColor = OnSurface
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null, modifier = Modifier.size(14.dp))
                        Text("PRINT WI-FI CARD", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                    }
                }
            }

            // Security footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Local link encapsulation. Guest privileges expire after 24 hours.",
                    fontFamily = SpaceMono,
                    fontSize = 9.sp,
                    color = OnSurfaceVariant
                )
            }
        }
    }
}
