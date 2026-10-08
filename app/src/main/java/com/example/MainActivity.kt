package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ScreenTab
import com.example.ui.components.NetPulseBottomNavigation
import com.example.ui.components.NetPulseHeader
import com.example.ui.components.QrShareModalSheet
import com.example.ui.screens.DiagnosticsScreen
import com.example.ui.screens.SavedProfilesScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SpeedTestScreen
import com.example.ui.theme.NetPulseTheme
import com.example.ui.theme.SurfaceContainerLowest
import com.example.viewmodel.NetPulseViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NetPulseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NetPulseTheme {
                NetPulseApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun NetPulseApp(
    viewModel: NetPulseViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.diagnosticToastMessage) {
        uiState.diagnosticToastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            NetPulseHeader(
                currentTab = uiState.currentTab,
                modifier = Modifier.windowInsetsPadding(WindowInsets.statusBars)
            )
        },
        bottomBar = {
            NetPulseBottomNavigation(
                currentTab = uiState.currentTab,
                onTabSelected = { viewModel.selectTab(it) }
            )
        },
        containerColor = SurfaceContainerLowest
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceContainerLowest)
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                ScreenTab.SCANNER -> ScannerScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                ScreenTab.DIAGNOSTICS -> DiagnosticsScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                ScreenTab.SPEED_TEST -> SpeedTestScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
                ScreenTab.SAVED -> SavedProfilesScreen(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }

    // Modal QR Share sheet overlay
    uiState.qrModalNode?.let { node ->
        QrShareModalSheet(
            node = node,
            isPasswordRevealed = uiState.isPasswordRevealedInQr,
            onTogglePassword = { viewModel.toggleQrPasswordVisibility() },
            onDismiss = { viewModel.closeQrModal() }
        )
    }
}
