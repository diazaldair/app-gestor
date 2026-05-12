package com.gestorplus.appgestor.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gestorplus.appgestor.designsystem.theme.SoloBookBackground
import com.gestorplus.appgestor.designsystem.theme.SoloBookPrimary
import com.gestorplus.appgestor.presentation.owner.OwnerDashboardScreenContent
import com.gestorplus.appgestor.presentation.requests.RequestsScreen

@Composable
fun MainScreen() {
    var currentTab by remember { mutableStateOf(MainTab.Requests) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentTab = currentTab,
                onTabSelected = { currentTab = it }
            )
        },
        containerColor = SoloBookBackground
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (currentTab) {
                MainTab.Schedule -> OwnerDashboardScreenContent()
                MainTab.Requests -> RequestsScreen()
                MainTab.Clients -> PlaceholderScreen("Clients")
                MainTab.Finance -> PlaceholderScreen("Finance")
                MainTab.Profile -> PlaceholderScreen("Profile")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentTab: MainTab,
    onTabSelected: (MainTab) -> Unit
) {
    Surface(
        color = SoloBookBackground,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MainTab.entries.forEach { tab ->
                NavigationItem(
                    tab = tab,
                    isSelected = currentTab == tab,
                    onClick = { onTabSelected(tab) }
                )
            }
        }
    }
}

@Composable
fun NavigationItem(
    tab: MainTab,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) SoloBookPrimary else Color.Gray
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
    ) {
        Box {
            Icon(
                imageVector = tab.icon,
                contentDescription = tab.label,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            if (tab.badgeCount > 0) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(Color.Red, CircleShape)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-4).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.badgeCount.toString(),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = tab.label,
            color = color,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "$name Screen", color = Color.White, fontSize = 20.sp)
    }
}

enum class MainTab(val label: String, val icon: ImageVector, val badgeCount: Int = 0) {
    Schedule("Schedule", Icons.Default.DateRange),
    Requests("Requests", Icons.Default.Inbox, 4),
    Clients("Clients", Icons.Default.Group),
    Finance("Finance", Icons.Default.BarChart),
    Profile("Profile", Icons.Default.Settings)
}
