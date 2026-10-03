package com.example.ems.presentation.leaves

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.components.CustomTextField
import com.example.ems.components.PrimaryButton
import com.example.ems.components.StatusBadge
import com.example.ems.domain.model.LeaveRequest
import com.example.ems.domain.model.User
import com.example.ems.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeavesScreen(
    userRole: UserRole,
    leaveList: List<LeaveRequest>,
    onApplyLeaveClick: () -> Unit,
    onApproveClick: (String) -> Unit,
    onRejectClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaves & Time-Off", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onApplyLeaveClick) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Apply Leave")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onApplyLeaveClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Apply Leave")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(leaveList) { leave ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
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
                                text = leave.leaveType,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            StatusBadge(status = leave.status)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Applicant: ${leave.employeeName}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Duration: ${leave.startDate} to ${leave.endDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Reason: ${leave.reason}",
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (leave.status == "PENDING" && (userRole == UserRole.ADMIN || userRole == UserRole.MANAGER)) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = { onRejectClick(leave.id) },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                                ) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Reject")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { onApproveClick(leave.id) }
                                ) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Approve")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyLeaveScreen(
    user: User,
    onBackClick: () -> Unit,
    onSubmit: (LeaveRequest) -> Unit
) {
    var leaveType by remember { mutableStateOf("Annual Vacation") }
    var startDate by remember { mutableStateOf("2025-04-01") }
    var endDate by remember { mutableStateOf("2025-04-05") }
    var reason by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Apply for Leave", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomTextField(
                value = leaveType,
                onValueChange = { leaveType = it },
                label = "Leave Type (e.g., Casual, Sick, Annual)"
            )
            CustomTextField(
                value = startDate,
                onValueChange = { startDate = it },
                label = "Start Date (YYYY-MM-DD)"
            )
            CustomTextField(
                value = endDate,
                onValueChange = { endDate = it },
                label = "End Date (YYYY-MM-DD)"
            )
            CustomTextField(
                value = reason,
                onValueChange = { reason = it },
                label = "Reason for Leave",
                singleLine = false,
                modifier = Modifier.height(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Submit Leave Request",
                onClick = {
                    val req = LeaveRequest(
                        id = "lv-${System.currentTimeMillis()}",
                        employeeId = user.employeeId ?: "EMP-102",
                        employeeName = user.name,
                        leaveType = leaveType,
                        startDate = startDate,
                        endDate = endDate,
                        reason = reason,
                        status = "PENDING",
                        appliedDate = "2025-03-01"
                    )
                    onSubmit(req)
                }
            )
        }
    }
}
