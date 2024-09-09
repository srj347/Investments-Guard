package com.example.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat

class IGEdittextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) {
            try {
                // Apply custom font
                ResourcesCompat.getFont(context, R.font.inter)?.let { typeface = it }
            } catch (e: Exception) {
                Log.e("IGEdittextView", "Error setting custom font: ${e.message}")
            }

            try {
                // Apply custom background
                setBackgroundResource(R.drawable.ig_edittext_background)
            } catch (e: Exception) {
                Log.e("IGEdittextView", "Error setting custom background: ${e.message}")
            }
        }
    }

    // Method to set text
    fun setCustomText(text: String) {
        setText(text)
    }

    // Method to set text color
    fun setCustomTextColor(color: Int) {
        setTextColor(color)
    }

    // Method to set text size
    fun setCustomTextSize(size: Float) {
        textSize = size
    }

    // Method to retrieve text
    fun getCustomText(): String {
        return text.toString()
    }
}