package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CellTower
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.Radar
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ScreenTab
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SpaceMono
import com.example.ui.theme.SurfaceContainerLowest

@Composable
fun NetPulseBottomNavigation(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(16.dp, ambientColor = Color.Black, spotColor = Color.Black)
            .background(SurfaceContainerLowest.copy(alpha = 0.95f))
            .navigationBarsPadding()
            .height(64.dp)
            .testTag("bottom_nav_bar")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "SCANNER",
                icon = Icons.Outlined.Radar,
                isSelected = currentTab == ScreenTab.SCANNER,
                onClick = { onTabSelected(ScreenTab.SCANNER) },
                testTag = "nav_scanner"
            )
            NavItem(
                label = "DIAGNOSTICS",
                icon = Icons.Outlined.QueryStats,
                isSelected = currentTab == ScreenTab.DIAGNOSTICS,
                onClick = { onTabSelected(ScreenTab.DIAGNOSTICS) },
                testTag = "nav_diagnostics"
            )
            NavItem(
                label = "SPEED TEST",
                icon = Icons.Outlined.Speed,
                isSelected = currentTab == ScreenTab.SPEED_TEST,
                onClick = { onTabSelected(ScreenTab.SPEED_TEST) },
                testTag = "nav_speed_test"
            )
            NavItem(
                label = "SAVED",
                icon = Icons.Outlined.BookmarkBorder,
                isSelected = currentTab == ScreenTab.SAVED,
                onClick = { onTabSelected(ScreenTab.SAVED) },
                testTag = "nav_saved"
            )
        }
    }
}

@Composable
private fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    val activeColor = SecondaryContainer
    val inactiveColor = OnSurfaceVariant

    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.size(22.dp)
        )
        Text(
            text = label,
            fontFamily = SpaceMono,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 9.sp,
            letterSpacing = 0.1.sp,
            color = if (isSelected) activeColor else inactiveColor,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
