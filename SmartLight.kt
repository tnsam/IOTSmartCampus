package com.example.iot_assignment

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class SmartLight : AppCompatActivity(), SensorEventListener {

    //variables
    private var sensorManager: SensorManager? = null
    private var lightSensor : Sensor? = null
    private lateinit var textViewLightIntensity: TextView
    private lateinit var textViewSwitchOnOff : TextView
    private lateinit var lightButton : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_sensor)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Corridor Lights")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        //get an instance of the sensor service
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        textViewLightIntensity = findViewById(R.id.lightIntensityValue)
        textViewSwitchOnOff = findViewById(R.id.switchOnOff)
        lightButton = findViewById(R.id.lightButton)

        if(event!!.sensor.type == Sensor.TYPE_LIGHT){
            textViewLightIntensity.text = event.values[0].toString()
        }

        val intensity: Float = event.values[0]
        //relay 1 is used to control the corridor lights
        var relay1: String?= null

        //lcdLight is used to adjust the brightness of the lcd
        var lcdLight: String?= null

        val database1 = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
        val data1 = database1.getReference("PI_12_CONTROL")

        //switch on/off the lights based on the surrounding light intensity
        if(intensity < 85){
            //switch on the light when the light intensity is low
            relay1 = "1"
            textViewSwitchOnOff.text = "on"
            lightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_on))

            if(intensity < 60){
                //adjust the brightness of the light based on the surrounding light intensity
                //the light is brighter when the surrounding is dark
                data1.child("lcdbkB").setValue("10")
                data1.child("lcdbkG").setValue("10")
                data1.child("lcdbkR").setValue("10")
            }
            else{
                //adjust the brightness of the light based on the surrounding light intensity
                //the light is dimmer when the surrounding is bright
                //power saving
                data1.child("lcdbkB").setValue("6")
                data1.child("lcdbkG").setValue("6")
                data1.child("lcdbkR").setValue("6")
            }
        }
        else{
            //switch off the light when the light intensity is high
            relay1 = "0"
            textViewSwitchOnOff.text = "off"
            lightButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_light_off))
        }

        data1.child("relay1").setValue(relay1)
        data1.child("relay2").setValue("0")
        data1.child("lcdscr").setValue("1")
        data1.child("camera").setValue("1")

        if(relay1 == "1"){
            data1.child("lcdtxt").setValue("**Light is on***")
        }
        else{
            data1.child("lcdtxt").setValue("**Light is off**")
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    //When the light sensor is resumed, call back the sensor manager
    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
    }

    //When the light sensor is paused, stop reading value
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }


}