package com.guard.investments.screens.auth


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uicomponents.IGButtonView
import com.example.uicomponents.IGImageView
import com.example.uicomponents.IGTextView
import com.guard.investments.R
import `in`.aabhasjindal.otptextview.OtpTextView

class VerifyOtpFragment : Fragment() {

    private lateinit var iv_back: IGImageView
    private lateinit var ov_otp_code: OtpTextView
    private lateinit var btn_verifyCode: IGButtonView
    private lateinit var tv_resendOtp: IGTextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.verify_otp, container, false)
        initViews(view)
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
            onVerifyCodeClicked()
        }
    }

    private fun onVerifyCodeClicked() {
        // Disable the button to prevent double clicks
        btn_verifyCode.isEnabled = false

        // Show the loading animation
        btn_verifyCode.setLoading(true)

        // Use postDelayed to stop the animation and navigate to the next screen
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the loading animation
            btn_verifyCode.setLoading(false)

            // Re-enable the button for future interactions
            btn_verifyCode.isEnabled = true

            // Navigate to the next fragment (or activity)
            navigateToCreatePin()
        }, 1000) // Adjust delay to match the animation duration (e.g., 1000ms = 1 second)
    }

    private fun navigateToCreatePin() {
        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                CreatePinFragment()
            ) // Replace with your CreatePinFragment
            .addToBackStack(null) // Add to back stack for proper navigation
            .commit()
    }
}
