package com.example.ems.presentation.departments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CorporateFare
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ems.components.CustomTextField
import com.example.ems.components.PrimaryButton
import com.example.ems.domain.model.Department
import com.example.ems.domain.model.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentListScreen(
    userRole: UserRole,
    departments: List<Department>,
    onBackClick: () -> Unit,
    onAddDepartmentClick: () -> Unit,
    onDeleteDepartmentClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Departments", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (userRole == UserRole.ADMIN) {
                        IconButton(onClick = onAddDepartmentClick) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Dept")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (userRole == UserRole.ADMIN) {
                FloatingActionButton(onClick = onAddDepartmentClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Dept")
                }
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
            items(departments) { dept ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CorporateFare,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = dept.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(text = "Code: ${dept.code} • Manager: ${dept.managerName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "${dept.totalEmployees} Assigned Employees", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        }
                        if (userRole == UserRole.ADMIN) {
                            IconButton(onClick = { onDeleteDepartmentClick(dept.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
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
fun AddEditDepartmentScreen(
    onBackClick: () -> Unit,
    onSaveClick: (Department) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var managerName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Department") },
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
            CustomTextField(value = name, onValueChange = { name = it }, label = "Department Name")
            CustomTextField(value = code, onValueChange = { code = it }, label = "Department Code (e.g. ENG, FIN)")
            CustomTextField(value = managerName, onValueChange = { managerName = it }, label = "Department Manager")
            CustomTextField(value = description, onValueChange = { description = it }, label = "Description", singleLine = false, modifier = Modifier.height(100.dp))

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Save Department",
                onClick = {
                    val dept = Department(
                        id = "dept-${System.currentTimeMillis()}",
                        name = name,
                        code = code,
                        managerName = managerName,
                        description = description
                    )
                    onSaveClick(dept)
                },
                enabled = name.isNotBlank() && code.isNotBlank()
            )
        }
    }
}
