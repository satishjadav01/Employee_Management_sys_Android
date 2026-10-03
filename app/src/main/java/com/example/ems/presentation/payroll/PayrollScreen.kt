package com.example.ems.presentation.payroll

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.components.StatusBadge
import com.example.ems.domain.model.PayrollRecord
import com.example.ems.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(
    userRole: UserRole,
    payrollList: List<PayrollRecord>,
    onCreatePayrollClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var downloadedMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(downloadedMessage) {
        if (downloadedMessage != null) {
            snackbarHostState.showSnackbar(downloadedMessage!!)
            downloadedMessage = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payroll & Payslips", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = onCreatePayrollClick) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Process Payroll")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(payrollList) { item ->
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
                            Column {
                                Text(
                                    text = item.employeeName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${item.month} ${item.year} • ${item.designation}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            StatusBadge(status = item.status)
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Basic Salary:", style = MaterialTheme.typography.bodySmall)
                            Text("$${item.basicSalary}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Allowances & Bonus:", style = MaterialTheme.typography.bodySmall)
                            Text("+$${item.allowances + item.bonus}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Deductions & Tax:", style = MaterialTheme.typography.bodySmall)
                            Text("-$${item.deductions + item.tax}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Net Salary: $${item.netSalary}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            OutlinedButton(
                                onClick = { downloadedMessage = "Payslip for ${item.month} downloaded as PDF." },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Payslip PDF")
                            }
                        }
                    }
                }
            }
        }
    }
}
