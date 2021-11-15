package com.example.project1

import android.content.Context
import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.media.MediaPlayer
import android.view.animation.AnimationUtils
import com.example.project1.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.abs
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var mediaPlayer : MediaPlayer? = MediaPlayer()
    private var isPlaying : Boolean = false
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)?.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 1f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = abs(acceleration * 0.9f + delta)
            if (acceleration > 1.6) {
                if (!isPlaying) {
                    onStartAnimation()
                    mediaPlayer = MediaPlayer.create(applicationContext, R.raw.bell_sound)
                    mediaPlayer!!.isLooping=true
                    mediaPlayer!!.start()
                    isPlaying = true
                }

            }
            else{
                if(isPlaying)
                {
                    isPlaying = false
                    mediaPlayer!!.stop()
                    onsTopAnimation()
                }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun onStartAnimation() {
        val animation = AnimationUtils.loadAnimation( this.applicationContext,R.anim.shake_animation )
        binding.imageView.startAnimation(animation)
    }
    private fun onsTopAnimation() {
        Thread.sleep(10)
        binding.imageView.clearAnimation()
    }



    override fun onResume() {
            sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
           Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL

        )
        super.onResume()
    }
    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }
}

