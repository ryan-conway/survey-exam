package com.example.nimblesurveys.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.repository.DispatcherRepository
import com.example.nimblesurveys.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val dispatchers: DispatcherRepository
): ViewModel() {

    private val _eventLoginSuccess = MutableLiveData<Boolean>()
    val eventSuccess: LiveData<Boolean> get() = _eventLoginSuccess

    private val _eventError = MutableLiveData<String>()
    val eventError: LiveData<String> get() = _eventError

    fun login(username: String, password: String) = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val result = loginUseCase.execute(username, password)
            if (result is Result.Success) {
                onLoginSuccess()
            } else if (result is Result.Failure) {
                onLoginFailed(result.cause)
            }
        }
    }

    private fun onLoginSuccess() = _eventLoginSuccess.postValue(true)

    private fun onLoginFailed(e: Throwable) {
        _eventError.postValue(e.message)
    }

    fun onDoneLoginSuccess() = _eventLoginSuccess.postValue(null)

    fun onDoneError() = _eventError.postValue(null)
}