package com.example.ems.presentation.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.components.StatusBadge
import com.example.ems.domain.model.AttendanceRecord
import com.example.ems.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    user: User,
    attendanceList: List<AttendanceRecord>,
    actionMessage: String?,
    onCheckIn: () -> Unit,
    onCheckOut: () -> Unit,
    onClearMessage: () -> Unit,
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(actionMessage) {
        if (actionMessage != null) {
            snackbarHostState.showSnackbar(actionMessage)
            onClearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Tracker", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Check-in / Check-out Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Quick Punch Clock", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(text = "Logged in as ${user.name} (${user.role.name})", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onCheckIn,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                        ) {
                            Icon(imageVector = Icons.Default.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Punch In")
                        }
                        Button(
                            onClick = onCheckOut,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828))
                        ) {
                            Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Punch Out")
                        }
                    }
                }
            }

            Text("Daily Punch History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(attendanceList) { att ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = att.employeeName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                StatusBadge(status = att.status)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Date: ${att.date} • ${att.department}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Check-in: ${att.checkInTime}", style = MaterialTheme.typography.bodyMedium)
                                Text(text = "Check-out: ${att.checkOutTime ?: "In Progress"}", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
