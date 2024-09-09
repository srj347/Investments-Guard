package com.example.uicomponents

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView

class IGButtonView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val textView: TextView
    private val animationView: LottieAnimationView
    private var loadingDuration: Long = 0L // Duration in milliseconds
    private val handler = Handler(Looper.getMainLooper()) // Handler to manage delay

    init {
        val root = LayoutInflater.from(context).inflate(R.layout.ig_button_view, this, true)
        textView = root.findViewById(R.id.ig_textview)
        animationView = root.findViewById(R.id.ig_button_animationView)
        loadAttributes(attrs, defStyleAttr)
        setupClickListener()
    }

    private fun loadAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        val arr = context.obtainStyledAttributes(
            attrs, R.styleable.AnimatedButton, defStyleAttr, 0
        )

        val buttonText = arr.getString(R.styleable.AnimatedButton_text)
        val isLoading = arr.getBoolean(R.styleable.AnimatedButton_loading, false)
        val isEnabled = arr.getBoolean(R.styleable.AnimatedButton_enabled, true)
        val animationResId =
            arr.getResourceId(R.styleable.AnimatedButton_animation_resId, R.raw.ig_button_animation)
        loadingDuration = arr.getInt(R.styleable.AnimatedButton_loadingDuration, 0).toLong()

        arr.recycle()

        textView.text = buttonText ?: ""
        textView.isEnabled = isEnabled
        setLoading(isLoading)
        setLottieAnimation(animationResId)
    }

    private fun setupClickListener() {
        textView.setOnClickListener {
            setLoading(true)
            textView.isVisible = false
            animationView.isVisible = true

            // Automatically stop loading after the specified duration
            if (loadingDuration > 0) {
                handler.postDelayed({
                    setLoading(false)
                }, loadingDuration)
            }
        }
    }

    private fun setLottieAnimation(resId: Int) {
        try {
            animationView.setAnimation(resId)
        } catch (e: Exception) {
            Log.e("LottieError", "Error setting Lottie animation: ${e.message}")
        }
    }

    fun setLoading(isLoading: Boolean) {
        textView.isClickable = !isLoading
        textView.isVisible = !isLoading
        animationView.isVisible = isLoading
        if (isLoading) {
            animationView.playAnimation()
        } else {
            animationView.pauseAnimation()
            textView.isVisible = true
            animationView.isVisible = false
        }
    }

    fun setText(text: String) {
        textView.text = text
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        textView.isEnabled = enabled
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        handler.removeCallbacksAndMessages(null) // Remove any pending callbacks
        animationView.cancelAnimation()
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable("superState", superState)
            putBoolean("isLoading", animationView.isVisible)
            putCharSequence("buttonText", textView.text)
            putBoolean("isEnabled", isEnabled)
            putLong("loadingDuration", loadingDuration)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is Bundle) {
            super.onRestoreInstanceState(state.getParcelable("superState"))
            setLoading(state.getBoolean("isLoading"))
            setText(state.getCharSequence("buttonText", "").toString())
            isEnabled = state.getBoolean("isEnabled")
            loadingDuration = state.getLong("loadingDuration")
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}
