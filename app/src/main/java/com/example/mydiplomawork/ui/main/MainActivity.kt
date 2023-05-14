package com.example.mydiplomawork.ui.main

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mydiplomawork.R
import com.example.mydiplomawork.ui.auth.AuthActivity
import com.example.mydiplomawork.ui.faq.FAQFragment
import com.example.mydiplomawork.ui.main.rootcheck.RootCheckFragment
import com.example.mydiplomawork.ui.obfuscation.ObfuscationFragment
import com.example.mydiplomawork.ui.onboarding.OnboardingFragment
import com.example.mydiplomawork.ui.utils.replaceFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var sharedPref: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setBottomNavigation()
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            replaceFragment(R.id.main_container, OnboardingFragment.newInstance(true))
        }
        findViewById<BottomNavigationView>(R.id.bottom_navigation_view).selectedItemId = R.id.menu_rootcheck
    }

    private val mOnNavigationItemSelectedListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_rootcheck -> {
                    replaceFragment(R.id.main_container, RootCheckFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_obfuscation -> {
                    replaceFragment(R.id.main_container, ObfuscationFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_cloud -> {
                    replaceFragment(R.id.main_container, OnboardingFragment.newInstance(true))
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_authentication -> {
                    logOut()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.menu_faq -> {
                    replaceFragment(R.id.main_container, FAQFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            return@OnNavigationItemSelectedListener false
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setBottomNavigation() {
        val navigation: BottomNavigationView = findViewById(R.id.bottom_navigation_view)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun logOut() {
        sharedPref = this.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString("pin_pass", "")?.apply()
        sharedPref?.edit()?.putBoolean("is_first_launch", false)?.apply()
        finish()
        startActivity(Intent(this, AuthActivity::class.java))
    }
}