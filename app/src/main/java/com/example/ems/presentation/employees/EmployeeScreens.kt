package com.example.ems.presentation.employees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ems.components.CustomTextField
import com.example.ems.components.PrimaryButton
import com.example.ems.components.StatusBadge
import com.example.ems.domain.model.Employee
import com.example.ems.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    userRole: UserRole,
    employees: List<Employee>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedDeptFilter: String,
    onDeptFilterChange: (String) -> Unit,
    onEmployeeClick: (String) -> Unit,
    onAddEmployeeClick: () -> Unit
) {
    val departments = listOf("ALL", "Engineering", "Human Resources", "Sales & Marketing", "Finance")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Staff Directory", fontWeight = FontWeight.Bold) },
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = onAddEmployeeClick) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Employee")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN) {
                FloatingActionButton(onClick = onAddEmployeeClick) {
                    Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Add Employee")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Search by name, role or employee code...") },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Department Filter Chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(departments) { dept ->
                    FilterChip(
                        selected = selectedDeptFilter.equals(dept, ignoreCase = true),
                        onClick = { onDeptFilterChange(dept) },
                        label = { Text(dept) }
                    )
                }
            }

            // Employee List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(employees) { emp ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEmployeeClick(emp.id) },
                        shape = RoundedCornerShape(16.dp),
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
                                modifier = Modifier.size(48.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = emp.fullName.take(1),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = emp.fullName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${emp.designation} • ${emp.departmentName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "ID: ${emp.employeeCode}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            StatusBadge(status = emp.status)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDetailScreen(
    employee: Employee?,
    userRole: UserRole,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Employee Profile") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (employee != null && userRole == UserRole.ADMIN) {
                        IconButton(onClick = { onEditClick(employee.id) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        IconButton(onClick = { onDeleteClick(employee.id) }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (employee == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(72.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = employee.fullName.take(1),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(text = employee.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(text = employee.designation, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        Spacer(modifier = Modifier.height(8.dp))
                        StatusBadge(status = employee.status)
                    }
                }

                // General Information
                DetailSection(title = "Contact Information") {
                    DetailRow(icon = Icons.Default.Email, label = "Email Address", value = employee.email)
                    DetailRow(icon = Icons.Default.Phone, label = "Phone Number", value = employee.phone)
                }

                // Employment Details
                DetailSection(title = "Employment Information") {
                    DetailRow(icon = Icons.Default.Badge, label = "Employee Code", value = employee.employeeCode)
                    DetailRow(icon = Icons.Default.CorporateFare, label = "Department", value = employee.departmentName)
                    DetailRow(icon = Icons.Default.CalendarToday, label = "Date of Joining", value = employee.dateOfJoining)
                    if (userRole == UserRole.ADMIN) {
                        DetailRow(icon = Icons.Default.AttachMoney, label = "Base Salary", value = "$${employee.salary} / year")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEmployeeScreen(
    existingEmployee: Employee?,
    onBackClick: () -> Unit,
    onSaveClick: (Employee) -> Unit
) {
    var fullName by remember(existingEmployee) { mutableStateOf(existingEmployee?.fullName ?: "") }
    var email by remember(existingEmployee) { mutableStateOf(existingEmployee?.email ?: "") }
    var phone by remember(existingEmployee) { mutableStateOf(existingEmployee?.phone ?: "") }
    var designation by remember(existingEmployee) { mutableStateOf(existingEmployee?.designation ?: "") }
    var departmentName by remember(existingEmployee) { mutableStateOf(existingEmployee?.departmentName ?: "Engineering") }
    var salary by remember(existingEmployee) { mutableStateOf(existingEmployee?.salary?.toString() ?: "75000") }
    var code by remember(existingEmployee) { mutableStateOf(existingEmployee?.employeeCode ?: "EMP-${(100..999).random()}") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingEmployee == null) "New Employee" else "Edit Employee") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            CustomTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Name")
            CustomTextField(value = code, onValueChange = { code = it }, label = "Employee ID Code")
            CustomTextField(value = email, onValueChange = { email = it }, label = "Corporate Email")
            CustomTextField(value = phone, onValueChange = { phone = it }, label = "Phone Number")
            CustomTextField(value = designation, onValueChange = { designation = it }, label = "Designation / Title")
            CustomTextField(value = departmentName, onValueChange = { departmentName = it }, label = "Department")
            CustomTextField(value = salary, onValueChange = { salary = it }, label = "Annual Salary ($)")

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = if (existingEmployee == null) "Add Employee" else "Update Employee",
                onClick = {
                    val emp = Employee(
                        id = existingEmployee?.id ?: "emp-${System.currentTimeMillis()}",
                        employeeCode = code,
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        designation = designation,
                        departmentId = "dept-1",
                        departmentName = departmentName,
                        dateOfJoining = existingEmployee?.dateOfJoining ?: "2025-01-10",
                        salary = salary.toDoubleOrNull() ?: 60000.0,
                        status = "ACTIVE"
                    )
                    onSaveClick(emp)
                },
                enabled = fullName.isNotBlank() && email.isNotBlank()
            )
        }
    }
}

@Composable
fun DetailSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
        }
    }
}
