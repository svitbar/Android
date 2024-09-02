package com.example.lab5

import android.content.Context
import android.hardware.Sensor

class StepCounterSensor(
    context: Context
) : MySensorsManager(context, Sensor.TYPE_STEP_COUNTER) {}
