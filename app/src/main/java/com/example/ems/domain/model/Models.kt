package com.example.ems.domain.model

enum class UserRole {
    ADMIN,
    MANAGER,
    EMPLOYEE
}

data class User(
    val id: String,
    val email: String,
    val role: UserRole,
    val token: String? = null,
    val name: String = when (role) {
        UserRole.ADMIN -> "Admin Administrator"
        UserRole.MANAGER -> "Michael Scott"
        UserRole.EMPLOYEE -> "Alex Johnson"
    },
    val employeeId: String? = when (role) {
        UserRole.ADMIN -> "EMP-001"
        UserRole.MANAGER -> "EMP-002"
        UserRole.EMPLOYEE -> "EMP-102"
    }
)

data class Employee(
    val id: String,
    val employeeCode: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val designation: String,
    val departmentId: String,
    val departmentName: String,
    val dateOfJoining: String,
    val salary: Double,
    val status: String = "ACTIVE",
    val avatarUrl: String? = null
)

data class Department(
    val id: String,
    val name: String,
    val code: String,
    val managerName: String,
    val totalEmployees: Int = 0,
    val description: String = ""
)

data class AttendanceRecord(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val department: String,
    val date: String,
    val checkInTime: String,
    val checkOutTime: String? = null,
    val status: String = "PRESENT",
    val workingHours: Double = 8.0
)

data class LeaveRequest(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String = "PENDING",
    val appliedDate: String = "",
    val rejectionReason: String? = null
)

typealias Leave = LeaveRequest

data class PayrollRecord(
    val id: String,
    val employeeId: String,
    val employeeName: String,
    val designation: String,
    val month: String,
    val year: Int,
    val basicSalary: Double,
    val allowances: Double,
    val deductions: Double,
    val tax: Double,
    val bonus: Double,
    val netSalary: Double,
    val status: String,
    val paymentDate: String
)

data class Task(
    val id: String,
    val title: String,
    val description: String,
    val assignedToId: String,
    val assignedToName: String,
    val assignedById: String,
    val assignedByName: String,
    val priority: String,
    val deadline: String,
    val status: String,
    val createdAt: String
)

data class Announcement(
    val id: String,
    val title: String,
    val description: String,
    val targetDepartment: String,
    val createdByName: String,
    val date: String
)

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false
)
