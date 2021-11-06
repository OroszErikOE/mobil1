package com.example.project1

import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import java.io.IOException


class MainActivity : AppCompatActivity(), SensorEventListener {
    private val SHAKE_THRESHOLD = 3.25f // m/S**2
    private val MIN_TIME_BETWEEN_SHAKES_MILLISECS = 1000
    private var mLastShakeTime: Long = 0
    private var mSensorMgr: SensorManager? = null
    var mediaPlayer : MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer.create(this, R.raw.bell_sound)

        mSensorMgr = getSystemService(SENSOR_SERVICE) as SensorManager?;
        val accelerometer: Sensor? = mSensorMgr!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            mSensorMgr!!.registerListener(
                this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }




    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val curTime = System.currentTimeMillis()
            if (curTime - mLastShakeTime > MIN_TIME_BETWEEN_SHAKES_MILLISECS) {
                val x = event!!.values[0]
                val y = event!!.values[1]
                val z = event!!.values[2]
                val acceleration = Math.sqrt(
                    Math.pow(x.toDouble(), 2.0) + Math.pow(y.toDouble(), 2.0) + Math.pow(z.toDouble(), 2.0)
                ) - SensorManager.GRAVITY_EARTH
                Log.d("APP_NAME", "Acceleration is " + acceleration + "m/s^2")
                if (acceleration > SHAKE_THRESHOLD) {
                    mLastShakeTime = curTime
                    mediaPlayer!!.start()
                    Log.d("APP_NAME", "Shake, Rattle, and Roll")
                }
            }
        }
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        TODO("Not yet implemented")
    }
}

private fun SensorManager.registerListener(mainActivity: MainActivity, accelerometer: Sensor, sensorDelayNormal: Int) {

}
