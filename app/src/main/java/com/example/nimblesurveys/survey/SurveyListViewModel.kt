package com.example.nimblesurveys.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.provider.DispatcherProvider
import com.example.nimblesurveys.domain.usecase.GetSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurveyListViewModel @Inject constructor(
    private val getSurveysUseCase: GetSurveysUseCase,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _surveys = MutableLiveData<List<Survey>>()
    val surveys: LiveData<List<Survey>> get() = _surveys

    private val _eventError = MutableLiveData<Boolean>()
    val eventError: LiveData<Boolean> get() = _eventError

    private val _eventViewSurvey = MutableLiveData<Survey>()
    val eventViewSurvey: LiveData<Survey> get() = _eventViewSurvey

    fun getSurveys() = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val surveyResult = getSurveysUseCase.execute()
            if (surveyResult is Result.Success) {
                onGetSurveys(surveyResult.value)
            } else if (surveyResult is Result.Failure) {
                onError()
            }
        }
    }

    private fun onGetSurveys(surveys: List<Survey>) = _surveys.postValue(surveys)

    private fun onError() = _eventError.postValue(true)

    fun onDoneError() = _eventError.postValue(null)

    fun viewSurvey(position: Int) = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val surveys = _surveys.value
            val survey = surveys?.getOrNull(position)
            _eventViewSurvey.postValue(survey)
        }
    }

    fun onDoneViewSurvey() = _eventViewSurvey.postValue(null)
}