package com.example.ems.navigation

sealed class NavRoutes(val route: String) {
    object Splash : NavRoutes("splash")
    object Login : NavRoutes("login")
    object ForgotPassword : NavRoutes("forgot_password")
    object Dashboard : NavRoutes("dashboard")
    object Employees : NavRoutes("employees")
    object EmployeeDetail : NavRoutes("employee_detail/{employeeId}") {
        fun createRoute(employeeId: String) = "employee_detail/$employeeId"
    }
    object AddEditEmployee : NavRoutes("add_edit_employee?employeeId={employeeId}") {
        fun createRoute(employeeId: String? = null) =
            if (employeeId != null) "add_edit_employee?employeeId=$employeeId" else "add_edit_employee"
    }
    object Departments : NavRoutes("departments")
    object AddEditDepartment : NavRoutes("add_edit_department") {
        fun createRoute() = "add_edit_department"
    }
    object Attendance : NavRoutes("attendance")
    object Leaves : NavRoutes("leaves")
    object ApplyLeave : NavRoutes("apply_leave")
    object Tasks : NavRoutes("tasks")
    object CreateTask : NavRoutes("create_task")
    object TaskDetail : NavRoutes("task_detail/{taskId}") {
        fun createRoute(taskId: String) = "task_detail/$taskId"
    }
    object Payroll : NavRoutes("payroll")
    object CreatePayroll : NavRoutes("create_payroll")
    object Announcements : NavRoutes("announcements")
    object Reports : NavRoutes("reports")
    object Notifications : NavRoutes("notifications")
    object Profile : NavRoutes("profile")
    object Settings : NavRoutes("settings")
}
