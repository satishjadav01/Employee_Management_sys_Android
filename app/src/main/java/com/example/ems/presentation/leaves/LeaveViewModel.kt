package com.example.ems.presentation.leaves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.LeaveRequest
import com.example.ems.domain.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LeaveViewModel(private val repository: EmsRepository) : ViewModel() {

    val allLeaves: StateFlow<List<LeaveRequest>> = repository.getAllLeaves()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun applyLeave(request: LeaveRequest, user: User, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.applyLeave(request, user)
            onDone()
        }
    }

    fun approveLeave(leaveId: String) {
        viewModelScope.launch {
            repository.approveLeave(leaveId)
        }
    }

    fun rejectLeave(leaveId: String, reason: String) {
        viewModelScope.launch {
            repository.rejectLeave(leaveId, reason)
        }
    }
}

class LeaveViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaveViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
