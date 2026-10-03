package com.example.ems.presentation.payroll

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ems.data.repository.EmsRepository
import com.example.ems.domain.model.PayrollRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PayrollViewModel(private val repository: EmsRepository) : ViewModel() {

    val allPayroll: StateFlow<List<PayrollRecord>> = repository.getAllPayroll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createPayroll(record: PayrollRecord, onDone: () -> Unit) {
        viewModelScope.launch {
            repository.createPayroll(record)
            onDone()
        }
    }
}

class PayrollViewModelFactory(private val repository: EmsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PayrollViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PayrollViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
