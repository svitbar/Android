package com.example.lab5

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MeasureFragment : Fragment() {
    private lateinit var text: TextView
    private lateinit var progressBar: CircularProgressBar
    private lateinit var openStatistic: Button
    private lateinit var stepCounterSensor: StepCounterSensor

    private var totalSteps = 0
    private var previousTotalSteps = 0
    private var currentDate: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_measure, container, false)

        text = view.findViewById(R.id.text)
        progressBar = view.findViewById(R.id.circularProgressBar)
        stepCounterSensor = StepCounterSensor(requireContext())

        if (!isSharedPreferencesEmpty()) {
            text.text = "0"
        }

        loadData()
        resetSteps()

        openStatistic = view.findViewById(R.id.open_statistic)

        openStatistic.setOnClickListener {
            saveStepCountAndDate()

            val fragmentManager = requireActivity().supportFragmentManager
            fragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, StatisticFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        stepCounterSensor.startListening()

        stepCounterSensor.getSensorData().observe(viewLifecycleOwner, Observer {value ->
            totalSteps = value.toInt()

            val currentSteps = totalSteps - previousTotalSteps
            text.text = currentSteps.toString()

            progressBar.apply {
                setProgressWithAnimation(currentSteps.toFloat())
            }
        })
    }

    override fun onPause() {
        super.onPause()
        stepCounterSensor.stopListening()
    }

    private fun resetSteps() {
        text.setOnLongClickListener {
            previousTotalSteps = totalSteps
            text.text = "0"
            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {
        val sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getInt("key1", 0)
        previousTotalSteps = savedNumber
    }

    private fun isSharedPreferencesEmpty(): Boolean {
        val sharedPreferences = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        return !sharedPreferences.contains("key1")
    }


    private fun saveStepCountAndDate() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        currentDate = dateFormat.format(calendar.time)

        val sharedPreferences = requireContext().getSharedPreferences("daysPref", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val currentSteps = totalSteps - previousTotalSteps
        editor.putInt(currentDate, currentSteps)
        editor.apply()
    }

    private fun sortPreferencesAndWriteToList(): MutableList<Pair<String, Int>> {
        val dataList: MutableList<Pair<String, Int>> = mutableListOf()

        val sharedPreferences = requireContext().getSharedPreferences("daysPref", Context.MODE_PRIVATE)
        sharedPreferences.all.forEach { (date, steps) ->
            dataList.add(date to (steps as Int))
        }

        dataList.sortBy { (date, _) ->
            try {
                SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(date)
            } catch (e: ParseException) {
                // Handle parsing exception
                null
            }
        }

        return dataList
    }

    fun checkDate() {
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time

        val lastDateStr = sortPreferencesAndWriteToList().lastOrNull()?.first

        if (lastDateStr != null) {
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            val lastDate = dateFormat.parse(lastDateStr)

            if (lastDate == currentDate) {
                Log.d("MeasureFragment", "true")
                resetSteps()
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = MeasureFragment()
    }
}