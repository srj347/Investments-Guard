package com.guard.investments

import RequestOtpFragment
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.authentication.PhoneOtpAuthService
import com.google.firebase.auth.FirebaseAuth
import com.guard.investments.viewmodels.AuthViewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view to the XML layout
        setContentView(R.layout.activity_main)
        authViewModel.phoneOtpAuthServices = PhoneOtpAuthService(this, FirebaseAuth.getInstance())
        // Replace the container with the RequestOtpFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RequestOtpFragment()).commit()
        }
    }
}
