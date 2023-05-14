package com.example.mydiplomawork.ui.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.mydiplomawork.R

class FAQFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_f_a_q, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val view1 = view.findViewById<TextView>(R.id.text1a)
        val view2 = view.findViewById<TextView>(R.id.text2a)
        val view3 = view.findViewById<TextView>(R.id.text3a)
        view.findViewById<TextView>(R.id.text1).setOnClickListener {
            view1.isVisible = !view1.isVisible
        }
        view.findViewById<TextView>(R.id.text2).setOnClickListener {
            view2.isVisible = !view2.isVisible
        }
        view.findViewById<TextView>(R.id.text3).setOnClickListener {
            view3.isVisible = !view3.isVisible
        }
    }

    companion object {
    }
}