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

class VerifyPinFragment : Fragment() {

    private lateinit var iv_backBtn: IGImageView
    private lateinit var tv_verifyPin: IGTextView
    private lateinit var ov_verifyPin: OtpTextView
    private lateinit var btn_VerifyPin: IGButtonView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.create_verify_pin, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View) {
        iv_backBtn = view.findViewById(R.id.iv_back_btn)
        tv_verifyPin = view.findViewById(R.id.tv_create_pin)
        ov_verifyPin = view.findViewById(R.id.ov_create_pin)
        btn_VerifyPin = view.findViewById(R.id.btn_create_pin)

        initViewsData()

        iv_backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btn_VerifyPin.setButtonClickListener {
            onVerifyPinClicked()
        }
    }

    private fun initViewsData() {
        tv_verifyPin.text = "Verify Your PIN"
        btn_VerifyPin.setText("VERIFY PIN")
    }

    private fun onVerifyPinClicked() {
        // Disable the button to prevent double clicks
        btn_VerifyPin.isEnabled = false

        // Show the loading animation
        btn_VerifyPin.setLoading(true)

        // Use postDelayed to stop the animation and perform an action
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the loading animation
            btn_VerifyPin.setLoading(false)

            // Re-enable the button if needed for subsequent clicks
            btn_VerifyPin.isEnabled = true

            // Navigate to the next fragment
            navigateToNextScreen()
        }, 1000) // Adjust delay to match the animation duration (e.g., 1000ms = 1 second)
    }

    private fun navigateToNextScreen() {
        // Replace with the desired next screen navigation logic
        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                RequestOtpFragment()
            ) // Replace with your actual next fragment
            .addToBackStack(null) // Adds this transaction to the back stack
            .commit()
    }
}
