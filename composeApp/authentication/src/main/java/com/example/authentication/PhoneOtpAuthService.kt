package com.example.authentication

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneOtpAuthService(
    private val activity: Activity,
    private val auth: FirebaseAuth
) {

    private var verificationId: String? = null

    fun sendOtp(
        phoneNumber: String,
        onSuccess: (response: Any) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        val callbacks = getVerificationCallbacks(onSuccess, onFailure)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(0L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(
        code: String,
        onSuccess: (response: Any?) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        if(code.isEmpty()){
            onFailure("Code is empty")
        }
        if(verificationId.isNullOrEmpty()){
            onFailure("Verification id is null or empty")
        } else {
            val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Log.d(TAG, "User sign in successfully")
                        onSuccess(task.result.user)
                    } else {
                        Log.d(TAG, "User sign in failed => ${task.exception?.message.toString()}")
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            onFailure("Otp is invalid")
                        } else {
                            onFailure(task.exception?.message.toString())
                        }
                    }
                }
        }
    }

    private fun getVerificationCallbacks(
        onSuccess: (response: Any) -> Unit,
        onFailure: (error: String) -> Unit
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        return object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Log.d(TAG, "Phone verification completed")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d(TAG, "Phone verification failed => ${p0.message}")
                onFailure(p0.message.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                p1: PhoneAuthProvider.ForceResendingToken,
            ) {
                super.onCodeSent(verificationId, p1)
                Log.d(TAG, "Code sent successfully")
                this@PhoneOtpAuthService.verificationId = verificationId
                onSuccess(verificationId)
            }
        }
    }

    companion object {
        const val TAG = "PhoneOtpAuthService"
    }
}