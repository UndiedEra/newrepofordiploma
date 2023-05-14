package com.example.mydiplomawork.ui.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragment(
    fragmentContainerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    block: FragmentTransaction.() -> FragmentTransaction = { this }
): Fragment {
    val transaction = supportFragmentManager.beginTransaction()
    transaction
        .block()
        .replace(fragmentContainerId, fragment, fragment.javaClass.name)
        .apply {
            if (addToBackStack) this.addToBackStack("root")
        }
        .commitAllowingStateLoss()

    return fragment
}

fun AppCompatActivity.addFragment(
    fragmentContainerId: Int,
    fragment: Fragment,
    addToBackStack: Boolean = false,
    block: FragmentTransaction.() -> FragmentTransaction = { this }
): Fragment {
    val transaction = supportFragmentManager.beginTransaction()
    transaction
        .block()
        .add(fragmentContainerId, fragment, fragment.javaClass.name)
        .apply {
            if (addToBackStack) this.addToBackStack("root")
        }
        .commitAllowingStateLoss()

    return fragment
}