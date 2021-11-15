package com.example.project1

import android.content.Context
import android.hardware.Sensor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.SensorManager
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.Toast
import com.example.project1.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var acceleration = 0f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private var mediaPlayer : MediaPlayer? = MediaPlayer()
    private var isplaying : Boolean = false
    //private var imageView = null findViewById<ImageView>(R.id.image_view)
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        acceleration = 1f
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

        mediaPlayer!!.setLooping(true)
        mediaPlayer = MediaPlayer.create(this, R.raw.bell_sound)


        //val imageView = findViewById<ImageView>(R.id.image_view)
        //imageView.animation = AnimationUtils.loadAnimation( this,R.anim.shake_animation)


    }
    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            //onstopAnimation()
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta

            if (acceleration > 5) {
                Log.d("Teszt####", acceleration.toString())
                if (!isplaying) {
                    mediaPlayer!!.start()
                    onStartAnimation()
                    Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
                    isplaying = true
                }

            }
            else{
                if(isplaying)
                {
                    isplaying = false
                    //mediaPlayer!!.stop()
                    onstopAnimation()
                }
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun onStartAnimation() {

        val animation = AnimationUtils.loadAnimation( this.applicationContext,R.anim.shake_animation )
        //binding.imageView.
        binding.imageView.startAnimation(animation)
        //animation = AnimationUtils.loadAnimation( this.applicationContext,R.anim.shake_animation)
        //binding.imageView.animation.start()


    }
    private fun onstopAnimation() {
        Log.d("Teszt####", "Haliitt vagyok")
        Thread.sleep(2000)
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

