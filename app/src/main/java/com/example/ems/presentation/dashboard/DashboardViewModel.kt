package com.example.ems.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.Announcement
import kotlinx.coroutines.flow.*

sealed class DashboardUiState {
    object Loading : DashboardUiState()
    data class Success(
        val totalEmployees: Int = 6,
        val presentToday: Int = 4,
        val pendingLeaves: Int = 2,
        val activeTasks: Int = 3,
        val totalDepartments: Int = 4,
        val announcements: List<Announcement> = emptyList()
    ) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(private val repository: EmsRepository) : ViewModel() {

    private val flowGroup1 = combine(
        repository.getAllEmployees(),
        repository.getAllAttendance(),
        repository.getAllLeaves()
    ) { emps, atts, leaves ->
        Triple(emps, atts, leaves)
    }

    private val flowGroup2 = combine(
        repository.getAllTasks(),
        repository.getAllDepartments(),
        repository.getAnnouncements()
    ) { tasks, depts, anns ->
        Triple(tasks, depts, anns)
    }

    val uiState: StateFlow<DashboardUiState> = combine(flowGroup1, flowGroup2) { (emps, atts, leaves), (tasks, depts, anns) ->
        DashboardUiState.Success(
            totalEmployees = emps.size,
            presentToday = atts.count { it.status == "PRESENT" || it.status == "LATE" },
            pendingLeaves = leaves.count { it.status == "PENDING" },
            activeTasks = tasks.count { it.status != "COMPLETED" },
            totalDepartments = depts.size,
            announcements = anns
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState.Loading)
}

class DashboardViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
