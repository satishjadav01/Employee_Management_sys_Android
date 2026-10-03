package com.example.ems.presentation.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.domain.model.User

@Composable
fun AdminDashboardScreen(
    user: User,
    uiState: DashboardUiState,
    onNavigateToEmployees: () -> Unit,
    onNavigateToDepartments: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToLeaves: () -> Unit,
    onNavigateToPayroll: () -> Unit,
    onNavigateToAnnouncements: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val stats = (uiState as? DashboardUiState.Success) ?: DashboardUiState.Success()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(54.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = Color.White)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "Hello, ${user.name}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text(text = "Admin Executive Overview", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                }
            }
        }

        // Summary Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Total Staff",
                value = stats.totalEmployees.toString(),
                icon = Icons.Default.People,
                containerColor = Color(0xFFE3F2FD),
                contentColor = Color(0xFF1565C0)
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Present Today",
                value = stats.presentToday.toString(),
                icon = Icons.Default.HowToReg,
                containerColor = Color(0xFFE8F5E9),
                contentColor = Color(0xFF2E7D32)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Leave Requests",
                value = stats.pendingLeaves.toString(),
                icon = Icons.Default.EventBusy,
                containerColor = Color(0xFFFFF3E0),
                contentColor = Color(0xFFE65100)
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Departments",
                value = stats.totalDepartments.toString(),
                icon = Icons.Default.CorporateFare,
                containerColor = Color(0xFFF3E5F5),
                contentColor = Color(0xFF7B1FA2)
            )
        }

        Text("Management Hub", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        // Action Grid
        val actions = listOf(
            Triple("Employee Directory", Icons.Default.PeopleAlt, onNavigateToEmployees),
            Triple("Departments", Icons.Default.CorporateFare, onNavigateToDepartments),
            Triple("Live Attendance", Icons.Default.Schedule, onNavigateToAttendance),
            Triple("Leave Approvals", Icons.Default.EventNote, onNavigateToLeaves),
            Triple("Payroll & Slips", Icons.Default.AttachMoney, onNavigateToPayroll),
            Triple("Announcements", Icons.Default.Campaign, onNavigateToAnnouncements),
            Triple("HR Analytics", Icons.Default.Assessment, onNavigateToReports),
            Triple("System Alerts", Icons.Default.Notifications, onNavigateToNotifications)
        )

        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            for (i in actions.indices step 2) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val first = actions[i]
                    ActionTile(title = first.first, icon = first.second, onClick = first.third, modifier = Modifier.weight(1f))
                    if (i + 1 < actions.size) {
                        val second = actions[i + 1]
                        ActionTile(title = second.first, icon = second.second, onClick = second.third, modifier = Modifier.weight(1f))
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun ManagerDashboardScreen(
    user: User,
    uiState: DashboardUiState,
    onNavigateToTeam: () -> Unit,
    onNavigateToTeamAttendance: () -> Unit,
    onNavigateToLeaveRequests: () -> Unit,
    onNavigateToTasks: () -> Unit
) {
    val stats = (uiState as? DashboardUiState.Success) ?: DashboardUiState.Success()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Welcome, Manager ${user.name}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "Engineering Department Operations", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Team Members",
                value = stats.totalEmployees.toString(),
                icon = Icons.Default.Group,
                containerColor = Color(0xFFE3F2FD),
                contentColor = Color(0xFF1565C0)
            )
            MetricCard(
                modifier = Modifier.weight(1f),
                title = "Active Tasks",
                value = stats.activeTasks.toString(),
                icon = Icons.Default.Assignment,
                containerColor = Color(0xFFFFF3E0),
                contentColor = Color(0xFFE65100)
            )
        }

        Text("Team Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        ActionTile(title = "Team Members Directory", icon = Icons.Default.People, onClick = onNavigateToTeam)
        ActionTile(title = "Team Clock & Attendance", icon = Icons.Default.Schedule, onClick = onNavigateToTeamAttendance)
        ActionTile(title = "Pending Leave Requests (${stats.pendingLeaves})", icon = Icons.Default.EventNote, onClick = onNavigateToLeaveRequests)
        ActionTile(title = "Task Delegation & Monitoring", icon = Icons.Default.AssignmentTurnedIn, onClick = onNavigateToTasks)
    }
}

@Composable
fun EmployeeDashboardScreen(
    user: User,
    uiState: DashboardUiState,
    onCheckInClick: () -> Unit,
    onCheckOutClick: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToLeaves: () -> Unit,
    onNavigateToPayroll: () -> Unit,
    onNavigateToAnnouncements: () -> Unit
) {
    val stats = (uiState as? DashboardUiState.Success) ?: DashboardUiState.Success()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "Hello, ${user.name}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = "Senior Android Developer • ID: ${user.employeeId ?: "EMP-102"}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onCheckInClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(imageVector = Icons.Default.Login, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Clock In")
                    }
                    Button(
                        onClick = onCheckOutClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                    ) {
                        Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Clock Out")
                    }
                }
            }
        }

        Text("Quick Links", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

        ActionTile(title = "My Assigned Tasks (${stats.activeTasks})", icon = Icons.Default.Assignment, onClick = onNavigateToTasks)
        ActionTile(title = "Apply & Track Leaves", icon = Icons.Default.EventNote, onClick = onNavigateToLeaves)
        ActionTile(title = "My Payslips & Compensation", icon = Icons.Default.AttachMoney, onClick = onNavigateToPayroll)
        ActionTile(title = "Company Notice Board", icon = Icons.Default.Campaign, onClick = onNavigateToAnnouncements)
    }
}

@Composable
fun MetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = contentColor)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = contentColor)
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = contentColor.copy(alpha = 0.9f))
        }
    }
}

@Composable
fun ActionTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}
