package com.example.mydiplomawork.ui.passcode

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.mydiplomawork.R
import com.example.mydiplomawork.ui.auth.AuthActivity
import com.example.mydiplomawork.ui.keyboard.NumPadView
import com.example.mydiplomawork.ui.main.MainActivity
import com.poovam.pinedittextfield.CirclePinField
import java.util.concurrent.Executor

class PassCodeFragment : Fragment() {

    private lateinit var vKeyboard: NumPadView
    private lateinit var pinEditText: CirclePinField
    private lateinit var passCodeStep: String
    private lateinit var pinPass: ArrayList<String>
    private var sharedPref: SharedPreferences? = null
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(requireActivity(), executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                showToast("Authentication error: $errString")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                showToast("Authentication succeeded")
                openMainFlow()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                showToast("Authentication failed")
            }
        })
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Вход по отпечатку пальца")
            .setNegativeButtonText("Отмена")
            .build()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pass_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireContext().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        arguments?.let {
            passCodeStep = it.getString(PASS_CODE_STEP, PASS_CODE_SET_UP)
        }
        pinPass = arrayListOf()
        vKeyboard = view.findViewById(R.id.passcode_fragment_keyboard)
        pinEditText = view.findViewById(R.id.pinEditText)
        vKeyboard.setKeyboardListener(object : NumPadView.OnKeyboardListener {
            override fun onEnterClicked() {
                if (passCodeStep == PASS_CODE_SET_UP) {
                    showToast("Для входа по отпечатку пальца установите пин-код")
                }
                else {
                    biometricPrompt.authenticate(promptInfo)
                }
            }

            override fun onCancelClicked() {
                val length = pinEditText.text?.length
                if (length != null && length != 0) {
                    pinEditText.text?.delete(length - 1, length)
                    pinPass.removeLast()
                }
            }

            override fun onSymbolClicked(symbol: String) {
                pinEditText.append(symbol)
                if (pinPass.size <= 4) pinPass.add(symbol)
                if (passCodeStep == PASS_CODE_SET_UP && pinPass.size == 4) {
                    sharedPref?.edit()?.putString("pin_pass", pinPass.toString())?.apply()
                    openMainFlow()
                }

                if (passCodeStep == PASS_CODE_CHECK && pinPass.size == 4) {
                    if (pinPass.toString() == sharedPref?.getString("pin_pass", "")) {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        showToast("Пароль неверный")
                        pinPass.clear()
                        pinEditText.text?.clear()
                    }
                }
            }
        })
        view.findViewById<TextView>(R.id.btn_exit).setOnClickListener {
            sharedPref?.edit()?.putString("pin_pass", "")?.apply()
            startActivity(Intent(requireContext(), AuthActivity::class.java))
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun openMainFlow() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val PASS_CODE_STEP = "PASS_CODE_STEP"
        private const val PASS_CODE_CHECK = "PASS_CODE_CHECK"
        private const val PASS_CODE_SET_UP = "PASS_CODE_SET_UP"
        fun newInstance(step: String, userName: String? = null, userPassWord: String? = null) = PassCodeFragment().apply {
            arguments = Bundle().apply {
                putString(PASS_CODE_STEP, step)
                putString("username", userName)
                putString("userpassword", userPassWord)
            }
        }
    }
}