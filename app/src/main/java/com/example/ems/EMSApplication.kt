package com.example.ems

import android.app.Application
import com.example.ems.data.local.UserPreferences
import com.example.ems.data.repository.EmsRepository

class EMSApplication : Application() {
    lateinit var repository: EmsRepository
        private set

    lateinit var userPreferences: UserPreferences
        private set

    override fun onCreate() {
        super.onCreate()
        repository = EmsRepository()
        userPreferences = UserPreferences(this)
    }
}
