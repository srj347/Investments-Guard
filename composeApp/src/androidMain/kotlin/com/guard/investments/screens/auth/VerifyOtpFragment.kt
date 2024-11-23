package com.guard.investments.screens.auth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.authentication.PhoneOtpAuthService
import com.example.uicomponents.IGButtonView
import com.example.uicomponents.IGImageView
import com.example.uicomponents.IGTextView
import com.guard.investments.R
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import `in`.aabhasjindal.otptextview.OtpTextView
import java.util.regex.Pattern

class VerifyOtpFragment : Fragment() {

    private lateinit var iv_back: IGImageView
    private lateinit var ov_otp_code: OtpTextView
    private lateinit var btn_verifyCode: IGButtonView
    private lateinit var tv_resendOtp: IGTextView
    private var phoneOtpAuthService : PhoneOtpAuthService? = null

    private lateinit var smsReceiver: BroadcastReceiver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.verify_otp, container, false)
        initViews(view)
        startSmsRetriever()
        return view
    }

    private fun initViews(view: View) {
        iv_back = view.findViewById(R.id.iv_back_btn)
        ov_otp_code = view.findViewById(R.id.ov_verify_otp)
        btn_verifyCode = view.findViewById(R.id.btn_verify_otp)
        tv_resendOtp = view.findViewById(R.id.tv_resend_otp)

        iv_back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed() // Navigate back
        }

        btn_verifyCode.setButtonClickListener {
            startSmsRetriever()
        }
    }

    private fun onVerifyCodeClicked(otp: String?) {

        if (otp != null) {
            phoneOtpAuthService?.verifyOtp(otp,{
                Toast.makeText(requireContext(), "OTP Verified", Toast.LENGTH_SHORT).show()
            },{
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })
        }

        // Disable the button to prevent double clicks
        btn_verifyCode.isEnabled = false

        // Show the loading animation
        btn_verifyCode.setLoading(true)

        // Simulate verification
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the loading animation
            btn_verifyCode.setLoading(false)

            // Re-enable the button for future interactions
            btn_verifyCode.isEnabled = true

            // Navigate to the next fragment (or activity)
            navigateToCreatePin()
        }, 1000)
    }

    private fun navigateToCreatePin() {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            CreatePinFragment()
        )
            .addToBackStack(null)
            .commit()
    }

    private fun startSmsRetriever() {
        val client = SmsRetriever.getClient(requireContext())
        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            // SMS Retriever API started successfully
            Toast.makeText(context, "Waiting for OTP...", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            // Failed to start SMS Retriever
            Toast.makeText(context, "Failed to start SMS Retriever", Toast.LENGTH_SHORT).show()
        }

        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                    val extras = intent.extras
                    if (extras != null) {
                        val status = extras[SmsRetriever.EXTRA_STATUS] as Status
                        when (status.statusCode) {
                            CommonStatusCodes.SUCCESS -> {
                                // SMS retrieved successfully
                                val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String

                                // Extract OTP using regex (assuming OTP is a 4-digit code)
                                val pattern = Pattern.compile("\\d{4}")
                                val matcher = pattern.matcher(message)
                                if (matcher.find()) {
                                    val otp = matcher.group(0)
                                    ov_otp_code.setOTP(otp) // Auto-fill OTP
                                    onVerifyCodeClicked(otp)   // Auto-trigger verification
                                }
                            }
                            CommonStatusCodes.TIMEOUT -> {
                                // SMS retrieval timed out
                                Toast.makeText(context, "SMS retrieval timed out", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
        requireContext().registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Unregister the SMS BroadcastReceiver
        requireContext().unregisterReceiver(smsReceiver)
    }
}
