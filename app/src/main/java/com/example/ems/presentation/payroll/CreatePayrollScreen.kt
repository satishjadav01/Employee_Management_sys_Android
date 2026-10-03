package com.example.ems.presentation.payroll

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ems.components.CustomTextField
import com.example.ems.components.PrimaryButton
import com.example.ems.domain.model.PayrollRecord

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePayrollScreen(
    onBackClick: () -> Unit,
    onSubmit: (PayrollRecord) -> Unit
) {
    var employeeName by remember { mutableStateOf("Alex Johnson") }
    var month by remember { mutableStateOf("March") }
    var year by remember { mutableStateOf("2025") }
    var basicSalary by remember { mutableStateOf("6000") }
    var allowances by remember { mutableStateOf("500") }
    var deductions by remember { mutableStateOf("300") }
    var tax by remember { mutableStateOf("400") }
    var bonus by remember { mutableStateOf("200") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Process Payroll") },
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
            CustomTextField(value = employeeName, onValueChange = { employeeName = it }, label = "Employee Name")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(value = month, onValueChange = { month = it }, label = "Month", modifier = Modifier.weight(1f))
                CustomTextField(value = year, onValueChange = { year = it }, label = "Year", modifier = Modifier.weight(1f))
            }
            CustomTextField(value = basicSalary, onValueChange = { basicSalary = it }, label = "Basic Salary ($)")
            CustomTextField(value = allowances, onValueChange = { allowances = it }, label = "Allowances ($)")
            CustomTextField(value = deductions, onValueChange = { deductions = it }, label = "Deductions ($)")
            CustomTextField(value = tax, onValueChange = { tax = it }, label = "Tax ($)")
            CustomTextField(value = bonus, onValueChange = { bonus = it }, label = "Bonus ($)")

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Process Salary & Generate Payslip",
                onClick = {
                    val basic = basicSalary.toDoubleOrNull() ?: 0.0
                    val allow = allowances.toDoubleOrNull() ?: 0.0
                    val ded = deductions.toDoubleOrNull() ?: 0.0
                    val tx = tax.toDoubleOrNull() ?: 0.0
                    val bn = bonus.toDoubleOrNull() ?: 0.0
                    val net = basic + allow + bn - ded - tx

                    val record = PayrollRecord(
                        id = "pay-${System.currentTimeMillis()}",
                        employeeId = "EMP-102",
                        employeeName = employeeName,
                        designation = "Senior Developer",
                        month = month,
                        year = year.toIntOrNull() ?: 2025,
                        basicSalary = basic,
                        allowances = allow,
                        deductions = ded,
                        tax = tx,
                        bonus = bn,
                        netSalary = net,
                        status = "PROCESSED",
                        paymentDate = "2025-03-01"
                    )
                    onSubmit(record)
                }
            )
        }
    }
}
