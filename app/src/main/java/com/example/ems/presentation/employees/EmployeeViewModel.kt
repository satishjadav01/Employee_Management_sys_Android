package com.example.ems.presentation.employees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.Employee
import com.example.ems.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class EmployeeListUiState {
    object Loading : EmployeeListUiState()
    data class Success(val employees: List<Employee>) : EmployeeListUiState()
    data class Error(val message: String) : EmployeeListUiState()
}

class EmployeeViewModel(private val repository: EmsRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedDepartmentFilter = MutableStateFlow("ALL")
    val selectedDepartmentFilter: StateFlow<String> = _selectedDepartmentFilter.asStateFlow()

    private val _uiState = MutableStateFlow<EmployeeListUiState>(EmployeeListUiState.Loading)
    val uiState: StateFlow<EmployeeListUiState> = _uiState.asStateFlow()

    val allEmployees: StateFlow<List<Employee>> = repository.getAllEmployees()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredEmployees: StateFlow<List<Employee>> = combine(
        allEmployees,
        _searchQuery,
        _selectedDepartmentFilter
    ) { employees, query, dept ->
        employees.filter { emp ->
            val matchesQuery = query.isBlank() ||
                    emp.fullName.contains(query, ignoreCase = true) ||
                    emp.employeeCode.contains(query, ignoreCase = true) ||
                    emp.designation.contains(query, ignoreCase = true)

            val matchesDept = dept == "ALL" || emp.departmentName.equals(dept, ignoreCase = true) || emp.departmentId == dept
            matchesQuery && matchesDept
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadEmployees()
    }

    fun loadEmployees() {
        viewModelScope.launch {
            _uiState.value = EmployeeListUiState.Loading
            repository.seedInitialDataIfEmpty()
            _uiState.value = EmployeeListUiState.Success(allEmployees.value)
        }
    }

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onDepartmentFilterChanged(dept: String) {
        _selectedDepartmentFilter.value = dept
    }

    fun deleteEmployee(id: String, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEmployee(id)
            onDone()
        }
    }

    suspend fun getEmployeeById(id: String): Employee? {
        return repository.getEmployeeById(id)
    }

    fun saveEmployee(employee: Employee, onSuccess: () -> Unit) {
        viewModelScope.launch {
            when (repository.saveEmployee(employee)) {
                is Resource.Success -> onSuccess()
                is Resource.Error -> {}
                is Resource.Loading -> {}
            }
        }
    }
}

class EmployeeViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmployeeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
