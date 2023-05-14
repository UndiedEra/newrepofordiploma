package com.example.mydiplomawork.ui.obfuscation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.mydiplomawork.R

class ObfuscationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_obfuscation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnNext).setOnClickListener {
            openUrl("")
        }
    }

    private fun openUrl(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (browserIntent.resolveActivity(requireActivity().packageManager) != null) {
            this.startActivity(browserIntent)
        } else {
            Toast.makeText(
                requireContext(),
                "Нет приложений, которые могут выполнить действие",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {

    }
}