package com.example.mydiplomawork.ui.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydiplomawork.R
import com.example.mydiplomawork.ui.onboarding.OnboardingFragment
import com.example.mydiplomawork.ui.passcode.PassCodeFragment
import com.example.mydiplomawork.ui.utils.addFragment
import com.example.mydiplomawork.ui.utils.replaceFragment

class AuthActivity : AppCompatActivity() {

    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        sharedPref = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        if (sharedPref?.getString("pin_pass", "").isNullOrEmpty()) {
            replaceFragment(R.id.authContainer, AuthFragment())
        } else {
            replaceFragment(R.id.authContainer, PassCodeFragment.newInstance("PASS_CODE_CHECK"))
        }
    }
}