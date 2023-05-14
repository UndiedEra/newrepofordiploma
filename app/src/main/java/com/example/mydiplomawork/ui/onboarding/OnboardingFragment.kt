package com.example.mydiplomawork.ui.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import com.example.mydiplomawork.R

class OnboardingFragment : Fragment() {

    private var sharedPref: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onobarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.continueButton).isVisible = arguments?.getBoolean(IS_FROM_MAIN, false) == false
        sharedPref = requireActivity().getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)
        view.findViewById<Button>(R.id.continueButton).setOnClickListener {
            sharedPref?.edit()?.putBoolean("is_first_launch", false)?.apply()
            activity?.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPref?.edit()?.putBoolean("is_first_launch", false)?.apply()
    }

    companion object {
        private const val IS_FROM_MAIN = "IS_FROM_MAIN"
        fun newInstance(fromMain: Boolean) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putBoolean(IS_FROM_MAIN, fromMain)
            }
        }
    }
}