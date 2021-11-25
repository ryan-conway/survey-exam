package com.example.nimblesurveys.util

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

fun Fragment.finishOnBackPressed() {
    requireActivity().onBackPressedDispatcher
        .addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
}