package com.example.nimblesurveys.util

import com.example.nimblesurveys.domain.provider.DispatcherProvider
import kotlinx.coroutines.Dispatchers

class DispatcherProviderImpl: DispatcherProvider {
    override fun io() = Dispatchers.IO
    override fun main() = Dispatchers.Main
}