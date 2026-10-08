package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FolderShared
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NoEncryption
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WifiNode
import com.example.ui.theme.Error
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnPrimary
import com.example.ui.theme.OnPrimaryContainer
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
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer
import com.example.viewmodel.NetPulseUiState
import com.example.viewmodel.NetPulseViewModel

@Composable
fun SavedProfilesScreen(
    uiState: NetPulseUiState,
    viewModel: NetPulseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var hiddenSsidInput by remember { mutableStateOf("") }
    var expandedMenuNodeId by remember { mutableStateOf<String?>(null) }

    val filteredProfiles = uiState.savedProfiles.filter {
        uiState.savedSearchQuery.isBlank() ||
                it.ssid.contains(uiState.savedSearchQuery, ignoreCase = true) ||
                it.security.contains(uiState.savedSearchQuery, ignoreCase = true)
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
            // Subtitle Context Bar (PROFILE STORE // 0X4E | 4 KEYS)
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
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "PROFILE STORE // 0X4E",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 0.1.sp,
                            color = Primary
                        )
                    }
                    Text(
                        text = "Manage authorized telemetry nodes & cellular repeater",
                        fontFamily = SpaceMono,
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${uiState.savedProfiles.size} KEYS",
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.1.sp,
                        color = Secondary
                    )
                }
            }
        }

        item {
            // Hotspot Manager Card (Tactical Pod)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainer)
                    .border(1.dp, Primary.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("hotspot_manager_card")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerHighest),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CellTower,
                                    contentDescription = null,
                                    tint = SecondaryContainer,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Personal Hotspot",
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        color = OnSurface
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (uiState.isHotspotActive) Primary.copy(alpha = 0.2f)
                                                else SurfaceContainerHigh
                                            )
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (uiState.isHotspotActive) "ACTIVE" else "INACTIVE",
                                            fontFamily = SpaceMono,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp,
                                            color = if (uiState.isHotspotActive) Primary else OnSurfaceVariant
                                        )
                                    }
                                }
                                Text(
                                    text = "SSID: ${uiState.hotspotSsid}",
                                    fontFamily = SpaceMono,
                                    fontSize = 11.sp,
                                    color = SecondaryContainer
                                )
                            }
                        }

                        // Hotspot Toggle Switch
                        Switch(
                            checked = uiState.isHotspotActive,
                            onCheckedChange = { viewModel.toggleHotspot() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = SurfaceContainerLowest,
                                checkedTrackColor = PrimaryContainer,
                                uncheckedThumbColor = OnSurfaceVariant,
                                uncheckedTrackColor = SurfaceContainerHighest
                            ),
                            modifier = Modifier.testTag("hotspot_switch")
                        )
                    }

                    // Hotspot Telemetry Matrix (Clients & Carrier Band)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLow)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Devices,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text("CLIENTS", fontFamily = SpaceMono, fontSize = 9.sp, color = OnSurfaceVariant)
                                Text("${uiState.hotspotClientsCount} Connected", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = OnSurface)
                            }
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WifiTethering,
                                contentDescription = null,
                                tint = OnSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Column {
                                Text("CARRIER BAND", fontFamily = SpaceMono, fontSize = 9.sp, color = OnSurfaceVariant)
                                Text(uiState.hotspotBand, fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Secondary)
                            }
                        }
                    }

                    // Active Connected Clients Micro-Pill Preview
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
                                    .size(6.dp)
                                    .background(Primary, CircleShape)
                            )
                            Text(
                                text = uiState.hotspotClientsPreview,
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                        Text(
                            text = "TX: ${uiState.hotspotTxRate}",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = SecondaryContainer
                        )
                    }

                    // Configure Hotspot Button
                    Button(
                        onClick = {
                            Toast.makeText(context, "Hotspot configuration: WPA3 personal key set to AES-256", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHigh,
                            contentColor = SecondaryContainer
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SettingsInputAntenna,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "CONFIGURE HOTSPOT & KEYS",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            // Visual Divider / Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FolderShared,
                        contentDescription = null,
                        tint = Secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Saved Profiles",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = OnSurface
                    )
                }
                Text(
                    text = "[ AUTO-SYNC: ON ]",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 0.1.sp,
                    color = OnSurfaceVariant
                )
            }
        }

        item {
            // Terminal Search Filter Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerLow)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = ">_",
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Secondary
                    )
                    BasicTextField(
                        value = uiState.savedSearchQuery,
                        onValueChange = { viewModel.setSavedSearchQuery(it) },
                        textStyle = TextStyle(
                            fontFamily = SpaceMono,
                            fontSize = 12.sp,
                            color = OnSurface
                        ),
                        cursorBrush = SolidColor(Primary),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (uiState.savedSearchQuery.isEmpty()) {
                                Text(
                                    text = "Search saved networks (SSID, Security)...",
                                    fontFamily = SpaceMono,
                                    fontSize = 11.sp,
                                    color = OnSurfaceVariant.copy(alpha = 0.6f)
                                )
                            }
                            innerTextField()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("saved_search_input")
                    )
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Empty Search Feedback
        if (filteredProfiles.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(
                        text = "No profile matching query",
                        fontFamily = SpaceGrotesk,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = OnSurface
                    )
                    Text(
                        text = "Check SSID filter or add as hidden node below",
                        fontFamily = SpaceMono,
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                }
            }
        }

        // Saved Network Cards
        items(filteredProfiles, key = { it.id }) { profile ->
            SavedNetworkCard(
                node = profile,
                isMenuExpanded = expandedMenuNodeId == profile.id,
                onToggleMenu = {
                    expandedMenuNodeId = if (expandedMenuNodeId == profile.id) null else profile.id
                },
                onToggleAutoJoin = { viewModel.toggleAutoJoin(profile.id) },
                onShareQr = { viewModel.openQrModal(profile) },
                onForget = {
                    viewModel.forgetNetwork(profile.id)
                    expandedMenuNodeId = null
                }
            )
        }

        item {
            // Bottom Action Matrix (Add Hidden SSID & Connect via WPS)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAddDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("add_hidden_ssid_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHigh,
                            contentColor = Secondary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(
                                text = "ADD HIDDEN SSID",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp
                            )
                        }
                    }

                    Button(
                        onClick = { viewModel.startWpsPush() },
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("wps_push_btn"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SurfaceContainerHigh,
                            contentColor = Primary
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.SettingsEthernet, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(
                                text = "CONNECT VIA WPS",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp
                            )
                        }
                    }
                }

                // WPS Pairing Banner (If active)
                AnimatedVisibility(visible = uiState.isWpsPushActive) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(SurfaceContainerHighest)
                            .border(1.dp, Primary.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(PrimaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Sync,
                                        contentDescription = null,
                                        tint = OnPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "WPS Push Active",
                                        fontFamily = SpaceGrotesk,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = OnSurface
                                    )
                                    Text(
                                        text = "Press PBC on router (${uiState.wpsRemainingSeconds}s remaining)",
                                        fontFamily = SpaceMono,
                                        fontSize = 10.sp,
                                        color = Primary
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(SurfaceContainerLow)
                                    .clickable { viewModel.cancelWps() }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ABORT",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Error
                                )
                            }
                        }
                    }
                }

                // Telemetry Diagnostic Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceContainerLowest)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
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
                                .background(Secondary, CircleShape)
                        )
                        Text(
                            text = "CREDENTIALS VAULT: ENCRYPTED (AES-256)",
                            fontFamily = SpaceMono,
                            fontSize = 9.sp,
                            color = OnSurfaceVariant
                        )
                    }
                    Text(
                        text = "RAM: 4.8MB",
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        color = SecondaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Dialog for adding Hidden SSID
    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text(
                    text = "Add Hidden Network",
                    fontFamily = SpaceGrotesk,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Enter manual network SSID to register into encrypted profile vault:",
                        fontFamily = SpaceMono,
                        fontSize = 11.sp,
                        color = OnSurfaceVariant
                    )
                    OutlinedTextField(
                        value = hiddenSsidInput,
                        onValueChange = { hiddenSsidInput = it },
                        placeholder = { Text("Hidden_Node_SSID", fontFamily = SpaceMono, fontSize = 12.sp) },
                        textStyle = TextStyle(fontFamily = SpaceMono, fontSize = 13.sp, color = OnSurface),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (hiddenSsidInput.isNotBlank()) {
                            viewModel.addHiddenSsid(hiddenSsidInput)
                            hiddenSsidInput = ""
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = OnPrimary)
                ) {
                    Text("ADD NODE", fontFamily = SpaceMono, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("CANCEL", fontFamily = SpaceMono, fontSize = 11.sp, color = OnSurfaceVariant)
                }
            },
            containerColor = SurfaceContainer
        )
    }
}

@Composable
private fun SavedNetworkCard(
    node: WifiNode,
    isMenuExpanded: Boolean,
    onToggleMenu: () -> Unit,
    onToggleAutoJoin: () -> Unit,
    onShareQr: () -> Unit,
    onForget: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceContainer)
            .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag("saved_card_${node.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (node.isConnected) PrimaryContainer
                                else SurfaceContainerHighest
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when {
                            node.ssid.contains("Home") -> Icons.Default.Router
                            node.ssid.contains("Studio") -> Icons.Default.NoEncryption
                            node.ssid.contains("CoWorking") -> Icons.Default.Domain
                            else -> Icons.Default.Wifi
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (node.isConnected) OnPrimaryContainer else Secondary,
                            modifier = Modifier.size(18.dp)
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
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                color = OnSurface
                            )
                            if (node.isConnected) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Primary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(5.dp)
                                                .background(Primary, CircleShape)
                                        )
                                        Text(
                                            text = "CONNECTED",
                                            fontFamily = SpaceMono,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 8.sp,
                                            color = Primary
                                        )
                                    }
                                }
                            } else if (node.isPrioritized) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(SurfaceContainerHigh)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "PRIORITIZED",
                                        fontFamily = SpaceMono,
                                        fontSize = 8.sp,
                                        color = OnSurfaceVariant
                                    )
                                }
                            } else if (node.isOpen) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(ErrorContainer.copy(alpha = 0.4f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "OPEN / CAPTIVE",
                                        fontFamily = SpaceMono,
                                        fontSize = 8.sp,
                                        color = Error
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(SurfaceContainerHigh)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = if (node.isOpen) "UNENCRYPTED" else node.security,
                                    fontFamily = SpaceMono,
                                    fontSize = 9.sp,
                                    color = if (node.isOpen) Error else Secondary
                                )
                            }
                            Text("•", fontFamily = SpaceMono, fontSize = 9.sp, color = OnSurfaceVariant)
                            Text(
                                text = "Synced ${node.syncedTime}",
                                fontFamily = SpaceMono,
                                fontSize = 9.sp,
                                color = if (node.isConnected) Primary else OnSurfaceVariant
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceContainerHigh)
                        .clickable { onShareQr() }
                        .padding(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Details",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // Controls Matrix (Auto-join toggle, Share QR, More menu)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceContainerLow)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Auto-join toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (node.autoJoin) Primary.copy(alpha = 0.12f)
                            else SurfaceContainerHighest
                        )
                        .clickable(onClick = onToggleAutoJoin)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (node.autoJoin) Icons.Default.Autorenew else Icons.Default.PauseCircle,
                            contentDescription = null,
                            tint = if (node.autoJoin) Primary else OnSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = if (node.autoJoin) "AUTO-JOIN: ON" else "AUTO-JOIN: OFF",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = if (node.autoJoin) Primary else OnSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Share QR
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHigh)
                            .clickable(onClick = onShareQr)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.QrCode2,
                                contentDescription = null,
                                tint = SecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "SHARE QR",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                color = SecondaryContainer
                            )
                        }
                    }

                    // More Menu
                    IconButton(
                        onClick = onToggleMenu,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Options",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Quick Action Drawer (Hidden by Default)
            AnimatedVisibility(visible = isMenuExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceContainerHighest)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable(onClick = onForget)
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = null,
                            tint = Error,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "FORGET NETWORK",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Error
                        )
                    }

                    Row(
                        modifier = Modifier
                            .clickable { onShareQr() }
                            .padding(horizontal = 6.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = "EDIT IP & DNS",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            color = Secondary
                        )
                    }
                }
            }
        }
    }
}
