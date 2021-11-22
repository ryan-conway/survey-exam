package com.example.nimblesurveys.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.repository.DispatcherRepository
import com.example.nimblesurveys.domain.usecase.GetAccessTokenUseCase
import com.example.nimblesurveys.domain.usecase.GetSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurveyListViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val getSurveysUseCase: GetSurveysUseCase,
    private val dispatchers: DispatcherRepository
) : ViewModel() {

    private val _surveys = MutableLiveData<List<Survey>>()
    val surveys: LiveData<List<Survey>> get() = _surveys

    private val _eventError = MutableLiveData<Boolean>()
    val eventError: LiveData<Boolean> get() = _eventError

    fun getSurveys() = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val tokenResult = getAccessTokenUseCase.execute()
            if (tokenResult is Result.Success) {
                val token = tokenResult.value
                val surveyResult = getSurveysUseCase.execute(token)
                if (surveyResult is Result.Success) {
                    onGetSurveys(surveyResult.value)
                } else if (surveyResult is Result.Failure) {
                    onError()
                }
            } else if (tokenResult is Result.Failure) {
                onError()
            }
        }
    }

    private fun onGetSurveys(surveys: List<Survey>) = _surveys.postValue(surveys)

    fun onDoneGetSurveys() = _surveys.postValue(null)

    private fun onError() = _eventError.postValue(true)

    fun onDoneError() = _eventError.postValue(null)
}