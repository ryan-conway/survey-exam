package com.example.nimblesurveys.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.exception.MissingCredentialsException
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

    private val _eventLoading = MutableLiveData<Boolean>()
    val eventLoading: LiveData<Boolean> get() = _eventLoading

    fun login(username: String, password: String) = viewModelScope.launch {
        withContext(dispatchers.io()) {
            setLoading(true)
            val result = loginUseCase.execute(username, password)
            if (result is Result.Success) {
                onLoginSuccess()
            } else if (result is Result.Failure) {
                onLoginFailed(result.cause)
            }
            setLoading(false)
        }
    }

    private fun setLoading(isLoading: Boolean) = _eventLoading.postValue(isLoading)

    private fun onLoginSuccess() = _eventLoginSuccess.postValue(true)

    private fun onLoginFailed(e: Throwable) {
        _eventError.postValue(e.message)
    }

    fun onDoneLoginSuccess() = _eventLoginSuccess.postValue(null)

    fun onDoneError() = _eventError.postValue(null)
}