package com.example.nimblesurveys.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.provider.DispatcherProvider
import com.example.nimblesurveys.domain.usecase.IsLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val dispatchers: DispatcherProvider
): ViewModel() {

    private val _isLoggedIn = MutableLiveData<Boolean?>()
    val isLoggedIn: LiveData<Boolean?> get() = _isLoggedIn

    fun checkLoginState() = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val isLoggedIn = isLoggedInUseCase.execute()
            _isLoggedIn.postValue(isLoggedIn)
        }
    }

}