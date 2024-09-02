package com.example.lab5

import android.content.Context
import android.content.DialogInterface
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.appcompat.app.AlertDialog.*
import androidx.lifecycle.MutableLiveData

abstract class MySensorsManager(

    private val context: Context,
    private val sensorType: Int
): SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private val sensorData = MutableLiveData<Float>()

    fun getSensorData(): MutableLiveData<Float> {
        return sensorData
    }

    private fun doesSensorExist(): Boolean {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager.getDefaultSensor(sensorType)

        if (sensor != null) {
            return true
        } else {
            val builder = Builder(context)
            builder.setTitle(R.string.app_name)
                .setMessage("This sensor is not available on the device")
                .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()
        }

        return false
    }

    fun startListening() {
        if(!doesSensorExist()) {
            return
        }

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopListening() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val value = event.values[0]
        sensorData.postValue(value)

    }
}