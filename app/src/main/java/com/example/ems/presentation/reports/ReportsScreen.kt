package com.example.ems.presentation.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class ReportType(val id: String, val title: String, val description: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBackClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var exportMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(exportMessage) {
        if (exportMessage != null) {
            snackbarHostState.showSnackbar(exportMessage!!)
            exportMessage = null
        }
    }

    val reportTypes = listOf(
        ReportType("1", "Attendance Report", "Detailed daily check-in, check-out, working hours, and late stats.", Icons.Default.Schedule),
        ReportType("2", "Employee Summary Report", "Headcount, department breakdown, designation, and status logs.", Icons.Default.People),
        ReportType("3", "Leave & Absence Report", "Leave history, approved vs pending leaves, and sick leaves taken.", Icons.Default.EventNote),
        ReportType("4", "Payroll & Compensation Report", "Monthly salary disbursements, tax deductions, bonuses, and net payouts.", Icons.Default.AttachMoney),
        ReportType("5", "Task & Performance Report", "Task completion rate, pending deadlines, and priority distributions.", Icons.Default.Assignment)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HR Reports & Analytics", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
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
            items(reportTypes) { report ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = report.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = report.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = report.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { exportMessage = "${report.title} exported as PDF" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export PDF")
                            }
                            OutlinedButton(
                                onClick = { exportMessage = "${report.title} exported as Excel / CSV" },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(imageVector = Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Export CSV")
                            }
                        }
                    }
                }
            }
        }
    }
}
