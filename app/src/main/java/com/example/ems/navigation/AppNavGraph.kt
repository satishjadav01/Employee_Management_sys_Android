package com.example.ems.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.ems.components.BottomNavBar
import com.example.ems.domain.model.*
import com.example.ems.presentation.announcements.AnnouncementsScreen
import com.example.ems.presentation.attendance.AttendanceScreen
import com.example.ems.presentation.attendance.AttendanceViewModel
import com.example.ems.presentation.auth.AuthState
import com.example.ems.presentation.auth.AuthViewModel
import com.example.ems.presentation.auth.ForgotPasswordScreen
import com.example.ems.presentation.auth.LoginScreen
import com.example.ems.presentation.auth.SplashScreen
import com.example.ems.presentation.dashboard.AdminDashboardScreen
import com.example.ems.presentation.dashboard.DashboardUiState
import com.example.ems.presentation.dashboard.DashboardViewModel
import com.example.ems.presentation.dashboard.EmployeeDashboardScreen
import com.example.ems.presentation.dashboard.ManagerDashboardScreen
import com.example.ems.presentation.departments.AddEditDepartmentScreen
import com.example.ems.presentation.departments.DepartmentListScreen
import com.example.ems.presentation.departments.DepartmentViewModel
import com.example.ems.presentation.employees.AddEditEmployeeScreen
import com.example.ems.presentation.employees.EmployeeDetailScreen
import com.example.ems.presentation.employees.EmployeeListScreen
import com.example.ems.presentation.employees.EmployeeViewModel
import com.example.ems.presentation.leaves.ApplyLeaveScreen
import com.example.ems.presentation.leaves.LeaveViewModel
import com.example.ems.presentation.leaves.LeavesScreen
import com.example.ems.presentation.notifications.NotificationsScreen
import com.example.ems.presentation.payroll.CreatePayrollScreen
import com.example.ems.presentation.payroll.PayrollScreen
import com.example.ems.presentation.payroll.PayrollViewModel
import com.example.ems.presentation.profile.ProfileScreen
import com.example.ems.presentation.reports.ReportsScreen
import com.example.ems.presentation.settings.SettingsScreen
import com.example.ems.presentation.tasks.CreateTaskScreen
import com.example.ems.presentation.tasks.TaskDetailScreen
import com.example.ems.presentation.tasks.TaskListScreen
import com.example.ems.presentation.tasks.TaskViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    currentUser: User?,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
    authViewModel: AuthViewModel,
    dashboardViewModel: DashboardViewModel,
    employeeViewModel: EmployeeViewModel,
    departmentViewModel: DepartmentViewModel,
    attendanceViewModel: AttendanceViewModel,
    leaveViewModel: LeaveViewModel,
    payrollViewModel: PayrollViewModel,
    taskViewModel: TaskViewModel
) {
    val authState by authViewModel.authState.collectAsState()
    val dashboardUiState by dashboardViewModel.uiState.collectAsState()

    val employees by employeeViewModel.filteredEmployees.collectAsState()
    val searchQuery by employeeViewModel.searchQuery.collectAsState()
    val selectedDeptFilter by employeeViewModel.selectedDepartmentFilter.collectAsState()

    val departments by departmentViewModel.departments.collectAsState()
    val attendanceList by attendanceViewModel.allAttendance.collectAsState()
    val actionMessage by attendanceViewModel.actionMessage.collectAsState()

    val leaveList by leaveViewModel.allLeaves.collectAsState()
    val payrollList by payrollViewModel.allPayroll.collectAsState()
    val tasks by taskViewModel.allTasks.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (currentUser == null) NavRoutes.Splash.route else NavRoutes.Dashboard.route
    ) {
        // Splash Screen
        composable(NavRoutes.Splash.route) {
            SplashScreen(
                currentUser = currentUser,
                onNavigateNext = { isLoggedIn ->
                    if (isLoggedIn) {
                        navController.navigate(NavRoutes.Dashboard.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(NavRoutes.Login.route) {
                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                        }
                    }
                }
            )
        }

        // Login Screen
        composable(NavRoutes.Login.route) {
            LoginScreen(
                authState = authState,
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onForgotPasswordClick = {
                    navController.navigate(NavRoutes.ForgotPassword.route)
                }
            )

            LaunchedEffect(authState) {
                if (authState is AuthState.Success) {
                    navController.navigate(NavRoutes.Dashboard.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                    authViewModel.resetState()
                }
            }
        }

        // Forgot Password
        composable(NavRoutes.ForgotPassword.route) {
            ForgotPasswordScreen(
                onBackClick = { navController.popBackStack() },
                onResetSent = { navController.popBackStack() }
            )
        }

        // Main Dashboard Host (Role Based Navigation)
        composable(NavRoutes.Dashboard.route) {
            val role = currentUser?.role ?: UserRole.ADMIN
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Dashboard.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    when (role) {
                        UserRole.ADMIN -> AdminDashboardScreen(
                            user = currentUser ?: User("admin", "admin@company.com", UserRole.ADMIN, "token"),
                            uiState = dashboardUiState,
                            onNavigateToEmployees = { navController.navigate(NavRoutes.Employees.route) },
                            onNavigateToDepartments = { navController.navigate(NavRoutes.Departments.route) },
                            onNavigateToAttendance = { navController.navigate(NavRoutes.Attendance.route) },
                            onNavigateToLeaves = { navController.navigate(NavRoutes.Leaves.route) },
                            onNavigateToPayroll = { navController.navigate(NavRoutes.Payroll.route) },
                            onNavigateToAnnouncements = { navController.navigate(NavRoutes.Announcements.route) },
                            onNavigateToReports = { navController.navigate(NavRoutes.Reports.route) },
                            onNavigateToNotifications = { navController.navigate(NavRoutes.Notifications.route) }
                        )
                        UserRole.MANAGER -> ManagerDashboardScreen(
                            user = currentUser ?: User("mgr", "manager@company.com", UserRole.MANAGER, "token"),
                            uiState = dashboardUiState,
                            onNavigateToTeam = { navController.navigate(NavRoutes.Employees.route) },
                            onNavigateToTeamAttendance = { navController.navigate(NavRoutes.Attendance.route) },
                            onNavigateToLeaveRequests = { navController.navigate(NavRoutes.Leaves.route) },
                            onNavigateToTasks = { navController.navigate(NavRoutes.Tasks.route) }
                        )
                        UserRole.EMPLOYEE -> EmployeeDashboardScreen(
                            user = currentUser ?: User("emp", "employee@company.com", UserRole.EMPLOYEE, "token"),
                            uiState = dashboardUiState,
                            onCheckInClick = { attendanceViewModel.checkIn(currentUser!!) },
                            onCheckOutClick = { attendanceViewModel.checkOut(currentUser!!) },
                            onNavigateToTasks = { navController.navigate(NavRoutes.Tasks.route) },
                            onNavigateToLeaves = { navController.navigate(NavRoutes.Leaves.route) },
                            onNavigateToPayroll = { navController.navigate(NavRoutes.Payroll.route) },
                            onNavigateToAnnouncements = { navController.navigate(NavRoutes.Announcements.route) }
                        )
                    }
                }
            }
        }

        // Employees List Screen
        composable(NavRoutes.Employees.route) {
            val role = currentUser?.role ?: UserRole.ADMIN
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Employees.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    EmployeeListScreen(
                        userRole = role,
                        employees = employees,
                        searchQuery = searchQuery,
                        onSearchChange = { employeeViewModel.onSearchQueryChanged(it) },
                        selectedDeptFilter = selectedDeptFilter,
                        onDeptFilterChange = { employeeViewModel.onDepartmentFilterChanged(it) },
                        onEmployeeClick = { id -> navController.navigate(NavRoutes.EmployeeDetail.createRoute(id)) },
                        onAddEmployeeClick = { navController.navigate(NavRoutes.AddEditEmployee.createRoute()) }
                    )
                }
            }
        }

        // Employee Detail Screen
        composable(
            route = NavRoutes.EmployeeDetail.route,
            arguments = listOf(navArgument("employeeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val empId = backStackEntry.arguments?.getString("employeeId") ?: ""
            var targetEmp by remember { mutableStateOf<Employee?>(null) }

            LaunchedEffect(empId) {
                targetEmp = employeeViewModel.getEmployeeById(empId)
            }

            EmployeeDetailScreen(
                employee = targetEmp,
                userRole = currentUser?.role ?: UserRole.ADMIN,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(NavRoutes.AddEditEmployee.createRoute(id)) },
                onDeleteClick = { id ->
                    employeeViewModel.deleteEmployee(id) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Add / Edit Employee
        composable(
            route = NavRoutes.AddEditEmployee.route,
            arguments = listOf(navArgument("employeeId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val empId = backStackEntry.arguments?.getString("employeeId")
            var targetEmp by remember { mutableStateOf<Employee?>(null) }

            LaunchedEffect(empId) {
                if (!empId.isNullOrEmpty()) {
                    targetEmp = employeeViewModel.getEmployeeById(empId)
                }
            }

            AddEditEmployeeScreen(
                existingEmployee = targetEmp,
                onBackClick = { navController.popBackStack() },
                onSaveClick = { newEmp ->
                    employeeViewModel.saveEmployee(newEmp) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Departments Screen
        composable(NavRoutes.Departments.route) {
            DepartmentListScreen(
                userRole = currentUser?.role ?: UserRole.ADMIN,
                departments = departments,
                onBackClick = { navController.popBackStack() },
                onAddDepartmentClick = { navController.navigate(NavRoutes.AddEditDepartment.createRoute()) },
                onDeleteDepartmentClick = { id -> departmentViewModel.deleteDepartment(id) }
            )
        }

        // Add Department Screen
        composable(NavRoutes.AddEditDepartment.route) {
            AddEditDepartmentScreen(
                onBackClick = { navController.popBackStack() },
                onSaveClick = { dept ->
                    departmentViewModel.saveDepartment(dept) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Attendance Screen
        composable(NavRoutes.Attendance.route) {
            val role = currentUser?.role ?: UserRole.ADMIN
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Attendance.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    AttendanceScreen(
                        user = currentUser ?: User("emp", "emp@company.com", UserRole.EMPLOYEE, "token"),
                        attendanceList = attendanceList,
                        actionMessage = actionMessage,
                        onCheckIn = { attendanceViewModel.checkIn(currentUser!!) },
                        onCheckOut = { attendanceViewModel.checkOut(currentUser!!) },
                        onClearMessage = { attendanceViewModel.clearMessage() },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // Leaves Screen
        composable(NavRoutes.Leaves.route) {
            val role = currentUser?.role ?: UserRole.ADMIN
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Leaves.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    LeavesScreen(
                        userRole = role,
                        leaveList = leaveList,
                        onApplyLeaveClick = { navController.navigate(NavRoutes.ApplyLeave.route) },
                        onApproveClick = { leaveViewModel.approveLeave(it) },
                        onRejectClick = { leaveViewModel.rejectLeave(it, "Overlapping schedule") },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // Apply Leave Screen
        composable(NavRoutes.ApplyLeave.route) {
            ApplyLeaveScreen(
                user = currentUser ?: User("emp", "emp@company.com", UserRole.EMPLOYEE, "token"),
                onBackClick = { navController.popBackStack() },
                onSubmit = { req ->
                    leaveViewModel.applyLeave(req, currentUser!!) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Tasks Screen
        composable(NavRoutes.Tasks.route) {
            val role = currentUser?.role ?: UserRole.ADMIN
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Tasks.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    TaskListScreen(
                        userRole = role,
                        tasks = tasks,
                        onCreateTaskClick = { navController.navigate(NavRoutes.CreateTask.route) },
                        onTaskClick = { id -> navController.navigate(NavRoutes.TaskDetail.createRoute(id)) },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // Create Task
        composable(NavRoutes.CreateTask.route) {
            CreateTaskScreen(
                onBackClick = { navController.popBackStack() },
                onSubmit = { newTask ->
                    taskViewModel.createTask(newTask) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Task Detail
        composable(
            route = NavRoutes.TaskDetail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            val task = tasks.find { it.id == taskId }

            TaskDetailScreen(
                task = task,
                onBackClick = { navController.popBackStack() },
                onStatusChange = { newStatus ->
                    if (taskId != null) {
                        taskViewModel.updateTaskStatus(taskId, newStatus)
                    }
                }
            )
        }

        // Payroll Screen
        composable(NavRoutes.Payroll.route) {
            PayrollScreen(
                userRole = currentUser?.role ?: UserRole.ADMIN,
                payrollList = payrollList,
                onCreatePayrollClick = { navController.navigate(NavRoutes.CreatePayroll.route) },
                onBackClick = { navController.popBackStack() }
            )
        }

        // Create Payroll Screen
        composable(NavRoutes.CreatePayroll.route) {
            CreatePayrollScreen(
                onBackClick = { navController.popBackStack() },
                onSubmit = { record ->
                    payrollViewModel.createPayroll(record) {
                        navController.popBackStack()
                    }
                }
            )
        }

        // Announcements Screen
        composable(NavRoutes.Announcements.route) {
            AnnouncementsScreen(
                userRole = currentUser?.role ?: UserRole.ADMIN,
                announcements = dashboardUiState.let { if (it is DashboardUiState.Success) it.announcements else emptyList() },
                onBackClick = { navController.popBackStack() },
                onCreateAnnouncementClick = {}
            )
        }

        // Reports Screen
        composable(NavRoutes.Reports.route) {
            ReportsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Notifications Screen
        composable(NavRoutes.Notifications.route) {
            NotificationsScreen(
                notifications = emptyList<NotificationItem>(),
                onBackClick = { navController.popBackStack() }
            )
        }

        // Profile Screen
        composable(NavRoutes.Profile.route) {
            val role = currentUser?.role ?: UserRole.EMPLOYEE
            Scaffold(
                bottomBar = {
                    BottomNavBar(
                        currentRoute = NavRoutes.Profile.route,
                        userRole = role,
                        onNavigate = { route -> navController.navigate(route) }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    ProfileScreen(
                        user = currentUser ?: User("emp", "emp@company.com", UserRole.EMPLOYEE, "token"),
                        onNavigateToSettings = { navController.navigate(NavRoutes.Settings.route) },
                        onLogoutClick = {
                            authViewModel.logout()
                            navController.navigate(NavRoutes.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }

        // Settings Screen
        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                isDarkMode = isDarkMode,
                onDarkModeToggle = onDarkModeToggle,
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
