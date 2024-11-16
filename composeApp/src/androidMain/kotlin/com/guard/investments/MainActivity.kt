package com.guard.investments

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.guard.investments.screens.auth.RequestOtpFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the XML layout
        setContentView(R.layout.activity_main)

        // Replace the container with the RequestOtpFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RequestOtpFragment()).commit()
        }
    }
}
