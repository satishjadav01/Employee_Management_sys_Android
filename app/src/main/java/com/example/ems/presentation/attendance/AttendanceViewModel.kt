package com.example.ems.presentation.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.AttendanceRecord
import com.example.ems.domain.model.User
import com.example.ems.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AttendanceViewModel(private val repository: EmsRepository) : ViewModel() {

    val allAttendance: StateFlow<List<AttendanceRecord>> = repository.getAllAttendance()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _actionMessage = MutableStateFlow<String?>(null)
    val actionMessage: StateFlow<String?> = _actionMessage.asStateFlow()

    fun checkIn(user: User) {
        viewModelScope.launch {
            val empId = user.employeeId ?: "EMP-102"
            val empName = user.name.ifEmpty { "Employee" }
            when (val res = repository.checkIn(empId, empName, "Development")) {
                is Resource.Success -> _actionMessage.value = "Checked in successfully at ${res.data?.checkInTime}"
                is Resource.Error -> _actionMessage.value = res.message
                is Resource.Loading -> {}
            }
        }
    }

    fun checkOut(user: User) {
        viewModelScope.launch {
            val empId = user.employeeId ?: "EMP-102"
            when (val res = repository.checkOut(empId)) {
                is Resource.Success -> _actionMessage.value = "Checked out successfully at ${res.data?.checkOutOutTime()}"
                is Resource.Error -> _actionMessage.value = res.message
                is Resource.Loading -> {}
            }
        }
    }

    private fun AttendanceRecord.checkOutOutTime() = checkOutTime ?: "Now"

    fun clearMessage() {
        _actionMessage.value = null
    }
}

class AttendanceViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AttendanceViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
