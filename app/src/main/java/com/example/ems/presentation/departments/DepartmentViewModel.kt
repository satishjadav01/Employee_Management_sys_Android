package com.example.ems.presentation.departments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.Department
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepartmentViewModel(private val repository: EmsRepository) : ViewModel() {

    val departments: StateFlow<List<Department>> = repository.getAllDepartments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveDepartment(dept: Department, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.saveDepartment(dept)
            onDone()
        }
    }

    fun deleteDepartment(id: String) {
        viewModelScope.launch {
            repository.deleteDepartment(id)
        }
    }
}

class DepartmentViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DepartmentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DepartmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
