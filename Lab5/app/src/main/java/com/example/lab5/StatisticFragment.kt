package com.example.lab5

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class StatisticFragment : Fragment() {
    private val dataList: MutableList<Pair<String, Int>> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_statistic, container, false)

        val cardColumn = view.findViewById<LinearLayout>(R.id.card_column)

        val sharedPreferences = requireContext().getSharedPreferences("daysPref", Context.MODE_PRIVATE)
        dataList.clear()

        sharedPreferences.all.forEach { (date, steps) ->
            dataList.add(date to steps as Int)
        }

        dataList.sortBy { (date, _) ->
            try {
                SimpleDateFormat("dd.MM.yy", Locale.getDefault()).parse(date)
            } catch (e: ParseException) {
                null
            }
        }

        dataList.reverse()

        dataList.forEach { (date, _) ->
            val steps = sharedPreferences.getInt(date, 0)
            val cardView = inflater.inflate(R.layout.card_view_template, null) as CardView

            val layoutParams = cardView.layoutParams ?: ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            val marginLayoutParams = layoutParams as? ViewGroup.MarginLayoutParams
            marginLayoutParams?.setMargins(10, 10, 10, 10)

            cardView.layoutParams = marginLayoutParams
            cardView.requestLayout()

            val textViewDate = cardView.findViewById<TextView>(R.id.date)
            textViewDate.text = "Date: ${date}"

            val textViewStepCount = cardView.findViewById<TextView>(R.id.stepCount)
            textViewStepCount.text = "Steps: ${steps}"

            val dayProgress = cardView.findViewById<ProgressBar>(R.id.day_progress)
            dayProgress.max = 100
            dayProgress.progress = steps

            cardColumn.addView(cardView)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatisticFragment()
    }
}