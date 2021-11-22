package com.example.nimblesurveys.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.repository.DispatcherRepository
import com.example.nimblesurveys.domain.usecase.GetAccessTokenUseCase
import com.example.nimblesurveys.domain.usecase.GetSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurveyLoadingViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getSurveysUseCase: GetSurveysUseCase,
    private val dispatchers: DispatcherRepository
): ViewModel() {

    private val _eventSurveysFetched = MutableLiveData<Boolean>()
    val eventSurveysFetched: LiveData<Boolean> get() = _eventSurveysFetched

    private val _eventError = MutableLiveData<Boolean>()
    val eventError: LiveData<Boolean> get() = _eventError

    fun fetchSurveys() = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val tokenResult = getAccessTokenUseCase.execute()
            if (tokenResult is Result.Success) {
                val token = tokenResult.value
                val surveyResult = getSurveysUseCase.execute(token)
                if (surveyResult is Result.Success) {
                    onFetchSurveySuccess()
                } else {
                    onError()
                }
            }
        }
    }

    private fun onFetchSurveySuccess() = _eventSurveysFetched.postValue(true)

    fun onDoneSurveysFetched() = _eventSurveysFetched.postValue(null)

    private fun onError() = _eventError.postValue(true)

    fun onDoneError() = _eventError.postValue(null)

}