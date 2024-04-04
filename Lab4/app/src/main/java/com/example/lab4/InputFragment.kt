package com.example.lab4

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope

class InputFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var getContentLauncher: ActivityResultLauncher<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_input, container, false)
        getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {it ->
            if (it != null) {
                dataModel.uri.value = it.toString()
            } else {
                Toast.makeText(context, "Cannot open this file", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioType = view.findViewById<RadioGroup>(R.id.radio_type)
        val radioStorage = view.findViewById<RadioGroup>(R.id.radio_storage)
        val fileUriEditText = view.findViewById<EditText>(R.id.file_uri)
        val openButton = view.findViewById<Button>(R.id.open)
        val uri = view.findViewById<RadioButton>(R.id.uri)
        fileUriEditText.visibility = View.VISIBLE

        openButton.setOnClickListener {
            val audio = view.findViewById<RadioButton>(R.id.audio)
            val video = view.findViewById<RadioButton>(R.id.video)

            val external = view.findViewById<RadioButton>(R.id.external)
            val internal = view.findViewById<RadioButton>(R.id.internal)

            val selectedType = view.findViewById<RadioButton>(radioType.checkedRadioButtonId)
            val selectedStorage = view.findViewById<RadioButton>(radioStorage.checkedRadioButtonId)

            if (uri.isChecked) {
                dataModel.uri.value = fileUriEditText.text.toString()
            }

            if (audio.isChecked && internal.isChecked) {
                dataModel.uri.value = "android.resource://" + requireActivity().packageName + "/" + R.raw.forestella_chosen_one
            }

            if (video.isChecked && internal.isChecked) {
                dataModel.uri.value = "android.resource://" + requireActivity().packageName + "/" + R.raw.forestella_bad_romance
            }

            if (audio.isChecked && external.isChecked) {
                openFile("audio/*")
                Log.d("InputFragment", "Observed URI: ${dataModel.uri.value}")
            }

            if (video.isChecked && external.isChecked) {
                openFile("video/*")
            }

            if (selectedType == null || selectedStorage == null) {
                Toast.makeText(view.context, "Please select both file type and file storage", Toast.LENGTH_SHORT).show()
            } else if (uri.isChecked && fileUriEditText.text.isNullOrEmpty()) {
                Toast.makeText(view.context, "Please enter file URI", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("InputFragment", "Observed URI: ${dataModel.uri.value}")
                dataModel.uri.observe(viewLifecycleOwner) { uri ->
                    if (uri != null) {
                        val fragmentManager = requireActivity().supportFragmentManager
                        fragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, PlayerFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }

    }

    private fun openFile(type: String) {
        getContentLauncher.launch(type)
    }

    companion object {
        @JvmStatic
        fun newInstance() = InputFragment()
    }
}
