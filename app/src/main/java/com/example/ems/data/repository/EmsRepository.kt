package com.example.ems.data.repository

import com.example.ems.domain.model.*
import com.example.ems.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EmsRepository {

    private val _currentUser = MutableStateFlow<User?>(
        User("u-admin", "admin@company.com", UserRole.ADMIN, "mock-admin-jwt", "Admin User", "EMP-001")
    )
    val currentUser = _currentUser.asStateFlow()

    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    private val _departments = MutableStateFlow<List<Department>>(emptyList())
    private val _attendance = MutableStateFlow<List<AttendanceRecord>>(emptyList())
    private val _leaves = MutableStateFlow<List<LeaveRequest>>(emptyList())
    private val _payroll = MutableStateFlow<List<PayrollRecord>>(emptyList())
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())

    init {
        seedInitialDataIfEmpty()
    }

    fun seedInitialDataIfEmpty() {
        if (_employees.value.isNotEmpty()) return

        _departments.value = listOf(
            Department("dept-1", "Engineering", "ENG", "Michael Scott", 14, "Core product development & infrastructure"),
            Department("dept-2", "Human Resources", "HR", "Pam Beesly", 4, "Talent acquisition, payroll & compliance"),
            Department("dept-3", "Sales & Marketing", "MKT", "Jim Halpert", 8, "Enterprise partnerships & brand growth"),
            Department("dept-4", "Finance", "FIN", "Oscar Martinez", 5, "Accounting, audit & financial strategy")
        )

        _employees.value = listOf(
            Employee("emp-1", "EMP-001", "Admin Administrator", "admin@company.com", "+1 555-0101", "Chief Executive Officer", "dept-1", "Engineering", "2021-01-15", 125000.0, "ACTIVE"),
            Employee("emp-2", "EMP-002", "Michael Scott", "manager@company.com", "+1 555-0102", "Regional VP / Manager", "dept-1", "Engineering", "2021-06-01", 95000.0, "ACTIVE"),
            Employee("emp-3", "EMP-102", "Alex Johnson", "employee@company.com", "+1 555-0103", "Senior Android Developer", "dept-1", "Engineering", "2022-03-10", 85000.0, "ACTIVE"),
            Employee("emp-4", "EMP-103", "Sarah Williams", "sarah.w@company.com", "+1 555-0104", "UI/UX Product Designer", "dept-1", "Engineering", "2023-01-20", 78000.0, "ACTIVE"),
            Employee("emp-5", "EMP-104", "David Miller", "david.m@company.com", "+1 555-0105", "HR Operations Specialist", "dept-2", "Human Resources", "2022-08-15", 62000.0, "ACTIVE"),
            Employee("emp-6", "EMP-105", "Emma Brown", "emma.b@company.com", "+1 555-0106", "Sales Lead", "dept-3", "Sales & Marketing", "2023-05-11", 72000.0, "ACTIVE")
        )

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        _attendance.value = listOf(
            AttendanceRecord("att-1", "EMP-102", "Alex Johnson", "Engineering", today, "09:02 AM", null, "PRESENT", 8.0),
            AttendanceRecord("att-2", "EMP-002", "Michael Scott", "Engineering", today, "08:50 AM", null, "PRESENT", 8.0),
            AttendanceRecord("att-3", "EMP-103", "Sarah Williams", "Engineering", today, "09:35 AM", null, "LATE", 7.5),
            AttendanceRecord("att-4", "EMP-104", "David Miller", "Human Resources", today, "09:00 AM", "05:00 PM", "PRESENT", 8.0)
        )

        _leaves.value = listOf(
            LeaveRequest("lv-1", "EMP-102", "Alex Johnson", "Annual Vacation", "2025-04-10", "2025-04-14", "Family trip out of state", "APPROVED", "2025-02-15"),
            LeaveRequest("lv-2", "EMP-103", "Sarah Williams", "Sick Leave", "2025-03-01", "2025-03-02", "Fever and doctor recommendation", "PENDING", "2025-02-28"),
            LeaveRequest("lv-3", "EMP-105", "Emma Brown", "Personal Leave", "2025-03-10", "2025-03-11", "Personal matters", "PENDING", "2025-02-26")
        )

        _payroll.value = listOf(
            PayrollRecord("pay-1", "EMP-102", "Alex Johnson", "Senior Android Developer", "February", 2025, 7000.0, 500.0, 300.0, 450.0, 250.0, 7000.0, "PAID", "2025-02-28"),
            PayrollRecord("pay-2", "EMP-103", "Sarah Williams", "UI/UX Product Designer", "February", 2025, 6500.0, 400.0, 250.0, 400.0, 200.0, 6450.0, "PAID", "2025-02-28"),
            PayrollRecord("pay-3", "EMP-002", "Michael Scott", "Regional VP / Manager", "February", 2025, 8000.0, 800.0, 400.0, 600.0, 500.0, 8300.0, "PAID", "2025-02-28")
        )

        _tasks.value = listOf(
            Task("t-1", "Implement Jetpack Compose Navigation", "Set up deep linking and animated transitions across all app screens.", "EMP-102", "Alex Johnson", "EMP-002", "Michael Scott", "HIGH", "2025-03-15", "IN_PROGRESS", "2025-02-20"),
            Task("t-2", "Revamp HR Analytics Dashboard", "Create exportable PDF/Excel reporting modules for executive staff.", "EMP-103", "Sarah Williams", "EMP-001", "Admin Administrator", "URGENT", "2025-03-05", "PENDING", "2025-02-22"),
            Task("t-3", "Migrate Database to Room 2.6", "Implement type converters and background KSP code generation.", "EMP-102", "Alex Johnson", "EMP-002", "Michael Scott", "MEDIUM", "2025-03-20", "PENDING", "2025-02-24")
        )

        _announcements.value = listOf(
            Announcement("ann-1", "Annual Company Offsite Announced!", "We will be hosting our yearly tech summit in Lake Tahoe from April 20 to April 24.", "All Company", "Admin Administrator", "2025-02-20"),
            Announcement("ann-2", "New Healthcare Benefits Enrollment", "Open enrollment for supplementary wellness and medical packages ends next Friday.", "Human Resources", "David Miller", "2025-02-18")
        )
    }

    suspend fun login(email: String, pass: String): Resource<User> {
        val role = when {
            email.contains("admin", ignoreCase = true) -> UserRole.ADMIN
            email.contains("manager", ignoreCase = true) -> UserRole.MANAGER
            else -> UserRole.EMPLOYEE
        }
        val user = User(
            id = "u-${System.currentTimeMillis()}",
            email = email,
            role = role,
            token = "jwt-mock-token-${System.currentTimeMillis()}"
        )
        _currentUser.value = user
        return Resource.Success(user)
    }

    suspend fun logout() {
        _currentUser.value = null
    }

    // Attendance
    fun getAllAttendance(): Flow<List<AttendanceRecord>> = _attendance.asStateFlow()

    suspend fun checkIn(empId: String, empName: String, dept: String): Resource<AttendanceRecord> {
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newRecord = AttendanceRecord(
            id = "att-${System.currentTimeMillis()}",
            employeeId = empId,
            employeeName = empName,
            department = dept,
            date = date,
            checkInTime = time,
            status = "PRESENT"
        )
        _attendance.value = listOf(newRecord) + _attendance.value
        return Resource.Success(newRecord)
    }

    suspend fun checkOut(empId: String): Resource<AttendanceRecord> {
        val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val currentList = _attendance.value.toMutableList()
        val index = currentList.indexOfFirst { it.employeeId == empId && it.checkOutTime == null }
        if (index != -1) {
            val updated = currentList[index].copy(checkOutTime = time)
            currentList[index] = updated
            _attendance.value = currentList
            return Resource.Success(updated)
        }
        return Resource.Error("No active check-in record found for today.")
    }

    // Departments
    fun getAllDepartments(): Flow<List<Department>> = _departments.asStateFlow()

    suspend fun saveDepartment(dept: Department) {
        val list = _departments.value.toMutableList()
        val index = list.indexOfFirst { it.id == dept.id }
        if (index != -1) {
            list[index] = dept
        } else {
            list.add(dept)
        }
        _departments.value = list
    }

    suspend fun deleteDepartment(id: String) {
        _departments.value = _departments.value.filter { it.id != id }
    }

    // Employees
    fun getAllEmployees(): Flow<List<Employee>> = _employees.asStateFlow()

    suspend fun getEmployeeById(id: String): Employee? {
        return _employees.value.find { it.id == id }
    }

    suspend fun saveEmployee(employee: Employee): Resource<Employee> {
        val list = _employees.value.toMutableList()
        val index = list.indexOfFirst { it.id == employee.id }
        if (index != -1) {
            list[index] = employee
        } else {
            list.add(employee)
        }
        _employees.value = list
        return Resource.Success(employee)
    }

    suspend fun deleteEmployee(id: String) {
        _employees.value = _employees.value.filter { it.id != id }
    }

    // Leaves
    fun getAllLeaves(): Flow<List<LeaveRequest>> = _leaves.asStateFlow()

    suspend fun applyLeave(request: LeaveRequest, user: User) {
        val newReq = request.copy(
            id = "lv-${System.currentTimeMillis()}",
            employeeId = user.employeeId ?: "EMP-102",
            employeeName = user.name
        )
        _leaves.value = listOf(newReq) + _leaves.value
    }

    suspend fun approveLeave(id: String) {
        _leaves.value = _leaves.value.map {
            if (it.id == id) it.copy(status = "APPROVED") else it
        }
    }

    suspend fun rejectLeave(id: String, reason: String) {
        _leaves.value = _leaves.value.map {
            if (it.id == id) it.copy(status = "REJECTED", rejectionReason = reason) else it
        }
    }

    // Payroll
    fun getAllPayroll(): Flow<List<PayrollRecord>> = _payroll.asStateFlow()

    suspend fun createPayroll(record: PayrollRecord) {
        _payroll.value = listOf(record) + _payroll.value
    }

    // Tasks
    fun getAllTasks(): Flow<List<Task>> = _tasks.asStateFlow()

    suspend fun createTask(task: Task) {
        _tasks.value = listOf(task) + _tasks.value
    }

    suspend fun updateTaskStatus(taskId: String, status: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) it.copy(status = status) else it
        }
    }

    // Announcements
    fun getAnnouncements(): Flow<List<Announcement>> = _announcements.asStateFlow()
}
