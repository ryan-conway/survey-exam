package com.example.nimblesurveys.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View

fun View.fade(
    duration: Long,
    fromAlpha: Float = 0f,
    toAlpha: Float = 1f,
    onEndListener: (() -> Unit)? = null
): ObjectAnimator =
    ObjectAnimator.ofFloat(this, "alpha", fromAlpha, toAlpha)
        .apply {
            setDuration(duration)
            onEndListener?.let {
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) = onEndListener()
                })
            }
        }