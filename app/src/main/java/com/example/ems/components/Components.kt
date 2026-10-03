package com.example.ems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.domain.model.UserRole
import com.example.ems.navigation.NavRoutes

@Composable
fun StatusBadge(status: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor) = when (status.uppercase()) {
        "ACTIVE", "APPROVED", "COMPLETED", "PRESENT", "PAID", "PROCESSED" ->
            Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "PENDING", "IN_PROGRESS", "MEDIUM", "LATE" ->
            Color(0xFFFFF3E0) to Color(0xFFE65100)
        "URGENT", "HIGH", "REJECTED", "ABSENT" ->
            Color(0xFFFFEBEE) to Color(0xFFC62828)
        "LOW" ->
            Color(0xFFE3F2FD) to Color(0xFF1565C0)
        else ->
            Color(0xFFF5F5F5) to Color(0xFF616161)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = status.replace("_", " "),
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = singleLine,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled
    ) {
        Text(text = text, fontWeight = FontWeight.SemiBold)
    }
}

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun BottomNavBar(
    currentRoute: String,
    userRole: UserRole,
    onNavigate: (String) -> Unit
) {
    val items = when (userRole) {
        UserRole.ADMIN -> listOf(
            BottomNavItem(NavRoutes.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
            BottomNavItem(NavRoutes.Employees.route, "Staff", Icons.Default.People),
            BottomNavItem(NavRoutes.Attendance.route, "Attendance", Icons.Default.Schedule),
            BottomNavItem(NavRoutes.Leaves.route, "Leaves", Icons.Default.EventNote),
            BottomNavItem(NavRoutes.Profile.route, "Profile", Icons.Default.Person)
        )
        UserRole.MANAGER -> listOf(
            BottomNavItem(NavRoutes.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
            BottomNavItem(NavRoutes.Employees.route, "Team", Icons.Default.Group),
            BottomNavItem(NavRoutes.Tasks.route, "Tasks", Icons.Default.Assignment),
            BottomNavItem(NavRoutes.Leaves.route, "Leaves", Icons.Default.EventNote),
            BottomNavItem(NavRoutes.Profile.route, "Profile", Icons.Default.Person)
        )
        UserRole.EMPLOYEE -> listOf(
            BottomNavItem(NavRoutes.Dashboard.route, "Dashboard", Icons.Default.Dashboard),
            BottomNavItem(NavRoutes.Tasks.route, "Tasks", Icons.Default.Assignment),
            BottomNavItem(NavRoutes.Attendance.route, "Clock", Icons.Default.Schedule),
            BottomNavItem(NavRoutes.Leaves.route, "Leaves", Icons.Default.EventNote),
            BottomNavItem(NavRoutes.Profile.route, "Profile", Icons.Default.Person)
        )
    }

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigate(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}
