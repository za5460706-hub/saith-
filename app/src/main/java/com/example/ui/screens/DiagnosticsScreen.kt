package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SettingsInputAntenna
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.ClientType
import com.example.model.ConnectedClient
import com.example.ui.theme.Error
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
fun DiagnosticsScreen(
    uiState: NetPulseUiState,
    viewModel: NetPulseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val filteredClients = uiState.clients.filter { client ->
        val matchesCategory = when (uiState.clientFilter) {
            "MOBILE" -> client.type == ClientType.MOBILE
            "LAPTOP" -> client.type == ClientType.LAPTOP
            "IOT" -> client.type == ClientType.IOT
            else -> true
        }
        val matchesQuery = uiState.clientSearchQuery.isBlank() ||
                client.name.contains(uiState.clientSearchQuery, ignoreCase = true) ||
                client.ip.contains(uiState.clientSearchQuery, ignoreCase = true) ||
                client.mac.contains(uiState.clientSearchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    val mobileClientsCount = uiState.clients.count { it.type == ClientType.MOBILE }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceContainerLowest)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // 1. Live LAN Inspection Banner (192.168.1.1 TP-Link Archer AX73)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, Primary.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
                    .testTag("gateway_telemetry_banner")
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
                                        .background(Primary, CircleShape)
                                )
                                Text(
                                    text = "LIVE LAN INSPECTION",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 0.1.sp,
                                    color = Primary
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "192.168.1.1",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "TP-Link Archer AX73 // Admin Session",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainer)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "SYNCED",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Primary
                                )
                            }
                        }
                    }

                    // Live Bandwidth Strip (Downstream 42.8 MB/s, Upstream 6.4 MB/s)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceContainerLowest.copy(alpha = 0.8f))
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "DOWNSTREAM",
                                    fontFamily = SpaceMono,
                                    fontSize = 9.sp,
                                    color = OnSurfaceVariant
                                )
                                Text(
                                    text = "42.8 MB/s",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Secondary
                                )
                            }
                        }

                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapVert,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                Text(
                                    text = "UPSTREAM",
                                    fontFamily = SpaceMono,
                                    fontSize = 9.sp,
                                    color = OnSurfaceVariant
                                )
                                Text(
                                    text = "6.4 MB/s",
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Primary
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            // 2. Device Allocation Grid: Smartphones (05 Active) & Total Clients (08 Allocated)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Smartphones Pod
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "SMARTPHONES",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp,
                                color = Secondary
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Secondary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Smartphone,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "05",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Active",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = Secondary
                            )
                        }
                        Text(
                            text = "62.5% of total spectrum load",
                            fontFamily = SpaceMono,
                            fontSize = 10.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                // Total Clients Pod
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceContainer)
                        .padding(14.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "TOTAL CLIENTS",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp,
                                color = Primary
                            )
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Primary.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Devices,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "08",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp,
                                color = OnSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Allocated",
                                fontFamily = SpaceMono,
                                fontSize = 11.sp,
                                color = OnSurfaceVariant
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.Laptop, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(11.dp))
                                Text("2", fontFamily = SpaceMono, fontSize = 10.sp, color = OnSurfaceVariant)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                Icon(Icons.Default.Tv, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(11.dp))
                                Text("1", fontFamily = SpaceMono, fontSize = 10.sp, color = OnSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        item {
            // 3. Router Admin Credentials Drawer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
                    .padding(14.dp)
                    .testTag("router_auth_box")
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.toggleAdminCredsDrawer() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SurfaceContainerHighest),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Router Admin Credentials",
                                    fontFamily = SpaceGrotesk,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = OnSurface
                                )
                                Text(
                                    text = "Direct hardware query enabled",
                                    fontFamily = SpaceMono,
                                    fontSize = 10.sp,
                                    color = OnSurfaceVariant
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (uiState.showAdminCreds) "HIDE" else "EDIT",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Secondary
                            )
                            Icon(
                                imageVector = if (uiState.showAdminCreds) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = uiState.showAdminCreds) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceContainerLowest)
                                    .padding(8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "ROUTER MODEL / HOST IP",
                                        fontFamily = SpaceMono,
                                        fontSize = 9.sp,
                                        color = OnSurfaceVariant
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "TP-Link AX73 (WPA3-SAE)",
                                            fontFamily = SpaceMono,
                                            fontSize = 11.sp,
                                            color = OnSurface
                                        )
                                        Text(
                                            text = "192.168.1.1",
                                            fontFamily = SpaceMono,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Primary
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "ADMIN USERNAME",
                                        fontFamily = SpaceMono,
                                        fontSize = 9.sp,
                                        color = OnSurfaceVariant
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SurfaceContainerLowest)
                                            .padding(8.dp)
                                    ) {
                                        Text(
                                            text = uiState.adminUsername,
                                            fontFamily = SpaceMono,
                                            fontSize = 12.sp,
                                            color = OnSurface
                                        )
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "ADMIN PASSWORD",
                                        fontFamily = SpaceMono,
                                        fontSize = 9.sp,
                                        color = OnSurfaceVariant
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SurfaceContainerLowest)
                                            .padding(horizontal = 8.dp, vertical = 5.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = if (uiState.isPasswordMasked) "••••••••••••" else uiState.adminPassword,
                                                fontFamily = SpaceMono,
                                                fontSize = 11.sp,
                                                color = OnSurface,
                                                modifier = Modifier.weight(1f)
                                            )
                                            IconButton(
                                                onClick = { viewModel.toggleAdminPasswordMask() },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (uiState.isPasswordMasked) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = "Toggle Password",
                                                    tint = OnSurfaceVariant,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    Toast.makeText(context, "Direct Router Link: Synced 5 mobiles & 3 other nodes", Toast.LENGTH_SHORT).show()
                                    viewModel.toggleAdminCredsDrawer()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp),
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Primary,
                                    contentColor = OnPrimary
                                )
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = "RE-AUTHENTICATE & REFRESH CLIENTS",
                                        fontFamily = SpaceMono,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // 4. Device Directory Search & Category Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "DEVICE DIRECTORY",
                        fontFamily = SpaceMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.1.sp,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "Showing ${filteredClients.size} of ${uiState.clients.size}",
                        fontFamily = SpaceMono,
                        fontSize = 10.sp,
                        color = Secondary
                    )
                }

                // Search Input
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
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        BasicTextField(
                            value = uiState.clientSearchQuery,
                            onValueChange = { viewModel.setClientSearchQuery(it) },
                            textStyle = TextStyle(
                                fontFamily = SpaceMono,
                                fontSize = 12.sp,
                                color = OnSurface
                            ),
                            cursorBrush = SolidColor(Primary),
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (uiState.clientSearchQuery.isEmpty()) {
                                    Text(
                                        text = "Search phone name, MAC or IP...",
                                        fontFamily = SpaceMono,
                                        fontSize = 11.sp,
                                        color = OnSurfaceVariant.copy(alpha = 0.5f)
                                    )
                                }
                                innerTextField()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("client_search_input")
                        )
                    }
                }

                // Category Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        ClientCategoryChip(
                            label = "ALL (8)",
                            isSelected = uiState.clientFilter == "ALL",
                            onClick = { viewModel.setClientFilter("ALL") }
                        )
                    }
                    item {
                        ClientCategoryChip(
                            label = "MOBILES (5)",
                            icon = Icons.Default.Smartphone,
                            isSelected = uiState.clientFilter == "MOBILE",
                            onClick = { viewModel.setClientFilter("MOBILE") }
                        )
                    }
                    item {
                        ClientCategoryChip(
                            label = "LAPTOPS (2)",
                            icon = Icons.Default.Laptop,
                            isSelected = uiState.clientFilter == "LAPTOP",
                            onClick = { viewModel.setClientFilter("LAPTOP") }
                        )
                    }
                    item {
                        ClientCategoryChip(
                            label = "IOT (1)",
                            icon = Icons.Default.Tv,
                            isSelected = uiState.clientFilter == "IOT",
                            onClick = { viewModel.setClientFilter("IOT") }
                        )
                    }
                }
            }
        }

        // 5. Client Node List
        items(filteredClients, key = { it.id }) { client ->
            ClientCard(
                client = client,
                onThrottle = { viewModel.throttleClient(client.name) }
            )
        }

        item {
            // 6. Rogue Device Watchdog Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceContainerLow)
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(12.dp))
                    .padding(14.dp)
                    .testTag("rogue_device_watchdog")
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
                                .background(Primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Radar,
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Rogue Device Watchdog",
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp,
                                color = OnSurface
                            )
                            Text(
                                text = "Auto-scans ARP table every 10s",
                                fontFamily = SpaceMono,
                                fontSize = 10.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceContainerHighest)
                            .clickable { viewModel.triggerIntruderScan() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("watchdog_scan_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Secondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "SCAN NOW",
                                fontFamily = SpaceMono,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 0.08.sp,
                                color = Secondary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ClientCategoryChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) Secondary else SurfaceContainer)
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFF003824) else OnSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
            }
            Text(
                text = label,
                fontFamily = SpaceMono,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (isSelected) Color(0xFF003824) else OnSurfaceVariant
            )
        }
    }
}

@Composable
private fun ClientCard(
    client: ConnectedClient,
    onThrottle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceContainer)
            .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(10.dp))
            .padding(12.dp)
            .testTag("client_${client.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                            .background(
                                if (client.isThisDevice) Primary.copy(alpha = 0.15f)
                                else SurfaceContainerHigh
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (client.type) {
                            ClientType.MOBILE -> Icons.Default.Smartphone
                            ClientType.LAPTOP -> Icons.Default.Laptop
                            ClientType.IOT -> Icons.Default.Tv
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (client.isThisDevice) Primary else Secondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = client.name,
                                fontFamily = SpaceGrotesk,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                color = OnSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(
                                        if (client.isThisDevice) Primary.copy(alpha = 0.2f)
                                        else Secondary.copy(alpha = 0.15f)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            ) {
                                Text(
                                    text = client.status.uppercase(),
                                    fontFamily = SpaceMono,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp,
                                    color = if (client.isThisDevice) Primary else Secondary
                                )
                            }
                        }

                        Text(
                            text = "${client.ip} • MAC: ${client.mac}",
                            fontFamily = SpaceMono,
                            fontSize = 10.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }

                // Right metrics or Action Button
                if (client.isThisDevice) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${client.band} // ${client.rssiDbm} dBm",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = Primary
                        )
                        Text(
                            text = "Stream: ${client.speedMbps} Mbps",
                            fontFamily = SpaceMono,
                            fontSize = 9.sp,
                            color = OnSurfaceVariant
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (client.isThrottled) Error.copy(alpha = 0.2f) else SurfaceContainerHighest)
                            .clickable(onClick = onThrottle)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("action_${client.id}")
                    ) {
                        Text(
                            text = if (client.isThrottled) "THROTTLED" else "THROTTLE",
                            fontFamily = SpaceMono,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.sp,
                            letterSpacing = 0.08.sp,
                            color = if (client.isThrottled) Error else OnSurface
                        )
                    }
                }
            }

            // Sub-bar with Live Traffic Info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(SurfaceContainerLowest.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Band: ${client.band} • Speed: ${client.speedMbps} Mbps",
                    fontFamily = SpaceMono,
                    fontSize = 10.sp,
                    color = OnSurfaceVariant
                )
                Text(
                    text = "Down: ${client.trafficDown}",
                    fontFamily = SpaceMono,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    color = Secondary
                )
            }
        }
    }
}
