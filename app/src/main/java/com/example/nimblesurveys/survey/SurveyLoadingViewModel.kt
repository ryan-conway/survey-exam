package com.example.nimblesurveys.survey

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nimblesurveys.domain.repository.DispatcherRepository
import com.example.nimblesurveys.domain.usecase.GetSurveysUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SurveyLoadingViewModel @Inject constructor(
    private val getSurveysUseCase: GetSurveysUseCase,
    private val dispatchers: DispatcherRepository
): ViewModel() {

    private val _eventSurveysFetched = MutableLiveData<Boolean>()
    val eventSurveysFetched: LiveData<Boolean> get() = _eventSurveysFetched

}