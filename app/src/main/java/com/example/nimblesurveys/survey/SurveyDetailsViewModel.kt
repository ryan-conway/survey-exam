package com.example.nimblesurveys.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimblesurveys.domain.model.Result
import com.example.nimblesurveys.domain.model.Survey
import com.example.nimblesurveys.domain.repository.DispatcherRepository
import com.example.nimblesurveys.domain.usecase.GetSurveyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SurveyDetailsViewModel @Inject constructor(
    private val getSurveyUseCase: GetSurveyUseCase,
    private val dispatchers: DispatcherRepository
) : ViewModel() {

    private val _survey = MutableLiveData<Survey>()
    val survey: LiveData<Survey> get() = _survey

    private val _eventError = MutableLiveData<Boolean>()
    val eventError: LiveData<Boolean> get() = _eventError

    fun loadSurvey(surveyId: String) = viewModelScope.launch {
        withContext(dispatchers.io()) {
            val surveyResult = getSurveyUseCase.execute(surveyId)
            if (surveyResult is Result.Success) {
                _survey.postValue(surveyResult.value)
            } else {
                onError()
            }
        }
    }

    private fun onError() = _eventError.postValue(true)

    fun onDoneError() = _eventError.postValue(null)
}