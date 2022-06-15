package com.example.iot_assignment

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.service.autofill.Sanitizer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*


class SmartEntrance : AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var sensor : Sensor? = null
    private var resume = false

    lateinit var open: Button
    lateinit var close: Button
    lateinit var text: TextView

    lateinit var seatText: TextView

    var database = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    var data = database.getReference("PI_12_CONTROL")


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_entrance)

        resetFBData()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        open = findViewById(R.id.open)
        close = findViewById(R.id.close)
        text = findViewById(R.id.text)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Entrance")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        open.setOnClickListener {
            on()

        }

        close.setOnClickListener {
            off()

        }


    }

    override fun onSensorChanged(event: SensorEvent?) {
        text = findViewById(R.id.text)

        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {
            val light1 = event.values[0]
            val sensordetect = sensor?.maximumRange

            if (light1 < sensordetect!!){

                on()

            }
            else

                off()

        }

    }

    private fun on(){
        resetFBData()
        data.child("relay1").setValue("1")
        data.child("relay2").setValue("0")
        data.child("lcdbkB").setValue("0")
        data.child("lcdbkG").setValue("50")

        data.child("lcdbkR").setValue("50")

        data.child("oledsc").setValue("1")
        data.child("lcdscr").setValue("1")
        data.child("buzzer").setValue("1")
        data.child("lcdtxt").setValue("Door is open....")
        data.child("camera").setValue("1")
        text.setText("Door is opening !!!")
        window.decorView.setBackgroundColor(Color.CYAN)

    }

    private fun off(){
        resetFBData()
        data.child("relay1").setValue("0")
        data.child("relay2").setValue("1")
        data.child("lcdbkB").setValue("0")
        data.child("lcdbkG").setValue("10")
        data.child("lcdbkR").setValue("50")
        data.child("lcdtxt").setValue("Door is close...")
        data.child("oledsc").setValue("0")
        data.child("lcdscr").setValue("1")
        data.child("camera").setValue("1")
        text.setText("Door is closing !!!")
        window.decorView.setBackgroundColor(Color.GRAY)


    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    //When the sensor is resumed, call back the sensor manager
    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY), 2 * 1000 * 1000)
    }

    //When the sensor is paused, stop reading value
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    fun resumeReading(view: View) {
        this.resume = true
    }

    fun pauseReading(view: View) {
        this.resume = false
    }

    fun resetFBData(){
        data.child("relay1").setValue("0")
        data.child("relay2").setValue("0")
        data.child("lcdbkB").setValue("0")
        data.child("lcdbkG").setValue("0")
        data.child("lcdbkR").setValue("0")
        data.child("lcdtxt").setValue(" ")
        data.child("buzzer").setValue("0")
        data.child("lcdscr").setValue("0")
        data.child("ledlgt").setValue("0")
        data.child("sound").setValue("0")
        data.child("camera").setValue("0")
    }

}