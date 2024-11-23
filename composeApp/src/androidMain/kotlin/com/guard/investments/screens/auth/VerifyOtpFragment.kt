package com.guard.investments.screens.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.authentication.PhoneOtpAuthService
import com.example.uicomponents.IGButtonView
import com.example.uicomponents.IGImageView
import com.example.uicomponents.IGTextView
import com.guard.investments.R
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.google.firebase.auth.FirebaseAuth
import com.guard.investments.viewmodels.AuthViewModel
import `in`.aabhasjindal.otptextview.OtpTextView
import java.util.regex.Pattern

class VerifyOtpFragment : Fragment() {

    private lateinit var ivBack: IGImageView
    private lateinit var ovOtpCode: OtpTextView
    private lateinit var btnVerifyCode: IGButtonView
    private lateinit var tvResendOtp: IGTextView
    private val authViewModel: AuthViewModel by activityViewModels()

    private val SMS_CONSENT_REQUEST = 2

    override fun onResume() {
        super.onResume()

        registerReceiver(
            requireActivity(),
            smsVerificationReceiver,
            IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION),
            SmsRetriever.SEND_PERMISSION,
            null,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(smsVerificationReceiver)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.verify_otp, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        ivBack = view.findViewById(R.id.iv_back_btn)
        ovOtpCode = view.findViewById(R.id.ov_verify_otp)
        btnVerifyCode = view.findViewById(R.id.btn_verify_otp)
        tvResendOtp = view.findViewById(R.id.tv_resend_otp)

        startSmartUserConsent()

        ivBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btnVerifyCode.setButtonClickListener {
            val otp = ovOtpCode.otp
            if (!otp.isNullOrEmpty()) {
                verifyCode(otp)
            } else {
                Toast.makeText(requireContext(), "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        tvResendOtp.setOnClickListener {
            Toast.makeText(requireContext(), "Resending OTP...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startSmartUserConsent() {
        SmsRetriever.getClient(requireActivity())
            .startSmsUserConsent(null)
            .addOnSuccessListener {
                Log.d(PhoneOtpAuthService.TAG, "startSmartUserConsent: SUCCESS")
            }
            .addOnFailureListener {
                Log.d(PhoneOtpAuthService.TAG, "startSmartUserConsent: FAILURE")
            }
    }

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        // Get consent intent
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            // Start activity to show consent dialog to user, activity must be started in
                            // 5 minutes, otherwise you'll receive another TIMEOUT intent
                            startActivityForResult(consentIntent!!, SMS_CONSENT_REQUEST)
                        } catch (e: ActivityNotFoundException) {
                            // Handle the exception ...
                        }
                    }

                    CommonStatusCodes.TIMEOUT -> {
                        // Time out occurred, handle the error.
                    }
                }
            }
        }
    }


    private fun verifyCode(otp: String) {
        btnVerifyCode.isEnabled = false
        btnVerifyCode.setLoading(true)

        authViewModel.phoneOtpAuthServices?.verifyOtp(otp, {
            Toast.makeText(requireContext(), "OTP Verified", Toast.LENGTH_SHORT).show()
            navigateToCreatePin()
        }, {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            btnVerifyCode.isEnabled = true
            btnVerifyCode.setLoading(false)
        })
    }

    private fun navigateToCreatePin() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, CreatePinFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                val oneTimeCode = parseOneTimeCode(message)
                if (oneTimeCode != null) {
                    verifyCode(oneTimeCode)
                    ovOtpCode.setOTP(oneTimeCode)
                }
            } else {
                Toast.makeText(requireContext(), "Consent denied. Please enter OTP manually.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun parseOneTimeCode(message: String?): String? {
        val pattern = Pattern.compile("\\d{6}")
        val matcher = pattern.matcher(message ?: "")
        return if (matcher.find()) matcher.group(0) else null
    }


}
