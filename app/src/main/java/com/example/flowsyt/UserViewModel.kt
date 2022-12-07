package com.example.flowsyt

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowsyt.data.converter.DataProgressInfo
import com.example.flowsyt.data.converter.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository = UserRepository(),
) : ViewModel() {

    var progressState by mutableStateOf(0)
        private set

    var isLoading by mutableStateOf(false)
        private set

    private val userData = mutableListOf<Float>()

    init {
        for (i in 0..1000000) {
            userData.add(i * 2f)
        }
    }

    fun startGenerate() {
        isLoading = true
        viewModelScope.launch {
            userRepository.generateUserData(userData).map {
                DataProgressInfo(it.progressPercentage * 10)
            }.map {
                it.progressPercentage / 10

            }
                .collect {
                    progressState = it

                }
            isLoading = false
            progressState = 0
        }
    }
}

