package com.example.ems

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ems.navigation.AppNavGraph
import com.example.ems.presentation.attendance.AttendanceViewModel
import com.example.ems.presentation.attendance.AttendanceViewModelFactory
import com.example.ems.presentation.auth.AuthViewModel
import com.example.ems.presentation.auth.AuthViewModelFactory
import com.example.ems.presentation.dashboard.DashboardViewModel
import com.example.ems.presentation.dashboard.DashboardViewModelFactory
import com.example.ems.presentation.departments.DepartmentViewModel
import com.example.ems.presentation.departments.DepartmentViewModelFactory
import com.example.ems.presentation.employees.EmployeeViewModel
import com.example.ems.presentation.employees.EmployeeViewModelFactory
import com.example.ems.presentation.leaves.LeaveViewModel
import com.example.ems.presentation.leaves.LeaveViewModelFactory
import com.example.ems.presentation.payroll.PayrollViewModel
import com.example.ems.presentation.payroll.PayrollViewModelFactory
import com.example.ems.presentation.tasks.TaskViewModel
import com.example.ems.presentation.tasks.TaskViewModelFactory
import com.example.ems.theme.EMSTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as EMSApplication
        val repository = app.repository

        setContent {
            val systemDark = isSystemInDarkTheme()
            val isDarkModeSaved by app.userPreferences.isDarkModeFlow.collectAsState(initial = systemDark)
            var isDarkMode by remember { mutableStateOf(isDarkModeSaved) }

            val currentUser by repository.currentUser.collectAsState(initial = null)

            val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(repository))
            val dashboardViewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(repository))
            val employeeViewModel: EmployeeViewModel = viewModel(factory = EmployeeViewModelFactory(repository))
            val departmentViewModel: DepartmentViewModel = viewModel(factory = DepartmentViewModelFactory(repository))
            val attendanceViewModel: AttendanceViewModel = viewModel(factory = AttendanceViewModelFactory(repository))
            val leaveViewModel: LeaveViewModel = viewModel(factory = LeaveViewModelFactory(repository))
            val payrollViewModel: PayrollViewModel = viewModel(factory = PayrollViewModelFactory(repository))
            val taskViewModel: TaskViewModel = viewModel(factory = TaskViewModelFactory(repository))

            EMSTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavGraph(
                        currentUser = currentUser,
                        isDarkMode = isDarkMode,
                        onDarkModeToggle = { enabled ->
                            isDarkMode = enabled
                            runBlocking { app.userPreferences.setDarkMode(enabled) }
                        },
                        authViewModel = authViewModel,
                        dashboardViewModel = dashboardViewModel,
                        employeeViewModel = employeeViewModel,
                        departmentViewModel = departmentViewModel,
                        attendanceViewModel = attendanceViewModel,
                        leaveViewModel = leaveViewModel,
                        payrollViewModel = payrollViewModel,
                        taskViewModel = taskViewModel
                    )
                }
            }
        }
    }
}
