package com.example.ems.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ems.components.CustomTextField
import com.example.ems.components.PrimaryButton
import com.example.ems.domain.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskScreen(
    onBackClick: () -> Unit,
    onSubmit: (Task) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var assignedToName by remember { mutableStateOf("Alex Johnson") }
    var priority by remember { mutableStateOf("HIGH") }
    var deadline by remember { mutableStateOf("2025-03-15") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create New Task") },
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
            CustomTextField(value = title, onValueChange = { title = it }, label = "Task Title")
            CustomTextField(value = description, onValueChange = { description = it }, label = "Description", singleLine = false, modifier = Modifier.height(100.dp))
            CustomTextField(value = assignedToName, onValueChange = { assignedToName = it }, label = "Assign To (Employee Name)")
            CustomTextField(value = priority, onValueChange = { priority = it }, label = "Priority (LOW, MEDIUM, HIGH, URGENT)")
            CustomTextField(value = deadline, onValueChange = { deadline = it }, label = "Deadline (YYYY-MM-DD)")

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Assign Task",
                onClick = {
                    val task = Task(
                        id = "t-${System.currentTimeMillis()}",
                        title = title,
                        description = description,
                        assignedToId = "EMP-102",
                        assignedToName = assignedToName,
                        assignedById = "u-mgr",
                        assignedByName = "Michael Scott",
                        priority = priority,
                        deadline = deadline,
                        status = "PENDING",
                        createdAt = "2025-02-25"
                    )
                    onSubmit(task)
                }
            )
        }
    }
}
