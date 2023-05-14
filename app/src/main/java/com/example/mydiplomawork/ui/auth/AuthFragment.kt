package com.example.mydiplomawork.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.example.mydiplomawork.R
import com.example.mydiplomawork.ui.onboarding.OnboardingFragment
import com.example.mydiplomawork.ui.passcode.PassCodeFragment
import com.example.mydiplomawork.ui.utils.addFragment
import com.example.mydiplomawork.ui.utils.replaceFragment

class AuthFragment : Fragment() {

    private var sharedPref: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireActivity().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putBoolean("is_first_launch", true)?.apply()
        if (sharedPref?.getBoolean("is_first_launch", true) == true) (activity as AuthActivity).addFragment(R.id.authContainer, OnboardingFragment.newInstance(false), true)
        val button = view.findViewById<AppCompatButton>(R.id.btnNext)
        val mail = view.findViewById<AppCompatEditText>(R.id.mail)
        val password = view.findViewById<AppCompatEditText>(R.id.password)
        button.setOnClickListener {
            if (mail.text.isNullOrBlank()) mail.error = "Заполните поле"
            else if (password.text.isNullOrBlank()) password.error = "Заполните поле"
            else {
                if (isValidEmail(mail.text.toString())) {
                    (activity as AuthActivity).replaceFragment(
                        R.id.authContainer,
                        PassCodeFragment.newInstance(
                            "PASS_CODE_SET_UP",
                            mail.text?.toString(),
                            password?.toString()
                        )
                    )
                } else {
                    mail.error = "Неправильный email"
                }

            }

        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            101 -> {
                Toast.makeText(requireContext(), "PinCode enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
    }
}