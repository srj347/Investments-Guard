package com.example.uicomponents

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat

class IGImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        if (!isInEditMode) {
            try {
                // Optional: Set a default placeholder image or style if needed

            } catch (e: Exception) {
                Log.e("IGImageView", "Error initializing IGImageView: ${e.message}")
            }
        }
    }

    // Method to set the image resource
    fun setCustomImageResource(resourceId: Int) {
        setImageResource(resourceId)
    }

    // Method to set the image from a drawable
    fun setCustomImageDrawable(drawable: Int) {
        val resource = ResourcesCompat.getDrawable(resources, drawable, null)
        setImageDrawable(resource)
    }

    // Method to set the content description
    fun setCustomContentDescription(description: String) {
        contentDescription = description
    }

    // Optional: Set a default placeholder image
    private fun setDefaultImage() {
        setImageResource(android.R.drawable.ic_menu_gallery) // Default icon
    }
}
