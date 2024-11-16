package com.guard.investments.screens.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.uicomponents.IGButtonView
import com.example.uicomponents.IGEdittextView
import com.example.uicomponents.IGImageView
import com.guard.investments.R

class RequestOtpFragment : Fragment() {

    private lateinit var btn_sendCode: IGButtonView
    private lateinit var tv_phoneNumber: IGEdittextView
    private lateinit var iv_back: IGImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.verify_phone_number, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        iv_back = view.findViewById(R.id.iv_back)
        tv_phoneNumber = view.findViewById(R.id.tv_phoneNumber)
        btn_sendCode = view.findViewById(R.id.btn_sendCode)

        btn_sendCode.setButtonClickListener {
            onSendCodeClicked()
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed() // Navigate back
        }
    }

    private fun onSendCodeClicked() {
        // Disable the button to prevent double clicks
        btn_sendCode.isEnabled = false

        // Show the loading animation
        btn_sendCode.setLoading(true)

        // Use postDelayed to stop the animation and navigate to the next fragment
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the loading animation
            btn_sendCode.setLoading(false)

            // Re-enable the button if needed for subsequent clicks
            btn_sendCode.isEnabled = true

            // Navigate to the next fragment
            navigateToVerifyOtp()
        }, 1000) // Adjust delay to match the animation duration (e.g., 1000ms = 1 second)
    }

    private fun navigateToVerifyOtp() {
        // Replace the current fragment with the VerifyOtpFragment
        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                VerifyOtpFragment()
            ) // Replace with your VerifyOtpFragment
            .addToBackStack(null) // Add to back stack for proper back navigation
            .commit()
    }
}
