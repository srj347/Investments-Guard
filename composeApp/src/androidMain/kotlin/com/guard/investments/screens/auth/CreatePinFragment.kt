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

class CreatePinFragment : Fragment() {

    private lateinit var iv_backBtn: IGImageView
    private lateinit var tv_createPin: IGTextView
    private lateinit var ov_createPin: OtpTextView
    private lateinit var btn_createPin: IGButtonView

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
        tv_createPin = view.findViewById(R.id.tv_create_pin)
        ov_createPin = view.findViewById(R.id.ov_create_pin)
        btn_createPin = view.findViewById(R.id.btn_create_pin)

        iv_backBtn.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        btn_createPin.setButtonClickListener {
            onCreatePinClicked()
        }
    }

    private fun onCreatePinClicked() {
        // Disable the button to prevent double clicks
        btn_createPin.isEnabled = false

        // Show the loading animation
        btn_createPin.setLoading(true)

        // Use postDelayed to stop the animation and navigate to the next fragment
        Handler(Looper.getMainLooper()).postDelayed({
            // Stop the loading animation
            btn_createPin.setLoading(false)

            // Re-enable the button for future interactions
            btn_createPin.isEnabled = true

            // Navigate to the VerifyPinFragment
            navigateToVerifyPin()
        }, 1000) // Adjust delay to match the animation duration (e.g., 1000ms = 1 second)
    }

    private fun navigateToVerifyPin() {
        requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                VerifyPinFragment()
            ) // Replace with your VerifyPinFragment
            .addToBackStack(null) // Add to back stack for proper navigation
            .commit()
    }
}
