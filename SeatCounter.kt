package com.example.iot_assignment

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize

class SeatCounter : AppCompatActivity(), SensorEventListener {

    val ownResource = FirebaseOptions.Builder()
            .setProjectId("testing-9b50f")
            .setApiKey("AIzaSyBKPWe1gPlksZfgx39dCswv10ws29GrDeI")
            .setApplicationId("1:519600774390:android:7d1edb87ec98fe66aa60d7")
            .setDatabaseUrl("https://testing-9b50f-default-rtdb.firebaseio.com/")
            .build()

    val commonResource = FirebaseOptions.Builder()
            .setProjectId("bait2123-202101-12")
            .setApiKey("AIzaSyDA5Pzgz6ZyID6ixd-HfbYAANumPtpvwqE")
            .setApplicationId("1:265617828931:android:d60b37c9b6e3c34fdc78f6")
            .setDatabaseUrl("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
            .build()


    var database = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    var data = database.getReference("PI_12_CONTROL")

    var sensorManager: SensorManager? = null
    var sensor: Sensor? = null
    var vibrator: Vibrator? = null

    var availableSeat: Int ?= null
    var totalNumber: Int ?= null
    var usedSeat:Int ?=null

    lateinit var numberSeat: TextView
    lateinit var totalNum: TextView
    lateinit var sensorDetect: TextView
    lateinit var imageAvailable: ImageView

    var isProximitySensorAvailable: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.testing)

        resetFBData()


        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Seat Detector")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        Firebase.initialize(this, ownResource, "secondary")

        // Retrieve my other app.
        val app = FirebaseApp.getInstance("secondary")
        // Get the database for the other app.
        val secondaryDatabase = FirebaseDatabase.getInstance(app)
        val seatAvailable = secondaryDatabase.getReference("seat")

        numberSeat = findViewById(R.id.numberOfSeat)
        totalNum = findViewById(R.id.totalNumber)

        seatAvailable.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //storing the amount into code variable
                var available = dataSnapshot.child("seatLeft").value.toString().toInt()
                var totalSeatUsed = dataSnapshot.child("totalNumber").value.toString().toInt()
                var usedSanitizer = dataSnapshot.child("seatUsed").value.toString().toInt()

                availableSeat = available
                totalNumber = totalSeatUsed
                usedSeat = usedSanitizer

                totalNum!!.text = "$totalNumber"

                Log.i("testing", "$seatAvailable")

                if (availableSeat != 0) {

                    numberSeat!!.text = "$availableSeat Seat(s) left"
                    imageAvailable.setImageDrawable(getResources().getDrawable(R.drawable.tick))
                }
                else{
                    numberSeat!!.text = "No more seat"
                    imageAvailable.setImageDrawable(getResources().getDrawable(R.drawable.cancel))
                }

               data.child("lcdtxt").setValue(String.format("%02d", availableSeat) + " is Seat Left." )


            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager?

//        sensorDetect = findViewById(R.id.sensorDeTECT)
        imageAvailable = findViewById(R.id.tickORnot)

        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY) != null) {

            sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            isProximitySensorAvailable = true

        } else {
//            sensorDetect!!.text = "Proximity sensor is not available"
            isProximitySensorAvailable = false
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {

        // Retrieve my other app.
        val app = FirebaseApp.getInstance("secondary")
        // Get the database for the other app.
        val secondaryDatabase = FirebaseDatabase.getInstance(app)
        val seatAvailable = secondaryDatabase.getReference("seat")

        val seat = seatAvailable.child("seatLeft")
        val seatUsed = seatAvailable.child("seatUsed")

        if (event?.sensor?.type == Sensor.TYPE_PROXIMITY) {

            val proximitySensor = event.values[0]
            val sensordetect = sensor?.maximumRange
            Log.i("test","$proximitySensor")

//            sensorDetect.setText("Sensor is activate now")

            if(proximitySensor < sensordetect!!){
                usedSeat = usedSeat?.plus(1)

                //when sanitizer still available
                if(availableSeat!! > 0){

                    imageAvailable.setImageDrawable(getResources().getDrawable(R.drawable.tick))

                    if(usedSeat!! > this!!.totalNumber!!){
                        resetFBData()
                        //blue light shows if they use too much
//                        data.child("lcdbkB").setValue("255")

                    }

                    //if no: continue to check stock ability
                    else{

                        resetFBData()

                        availableSeat = availableSeat!! -1

                        numberSeat!!.text = "Number of seat"


                        data.child("ledlgt").setValue("1")
                        data.child("lcdtxt").setValue(String.format("%02d", availableSeat) + " Seat Left...." )
                        data.child("lcdbkB").setValue("20")

                        data.child("lcdscr").setValue("1")
                        data.child("camera").setValue("1")

                        //write data to firebase
                        seat.setValue(availableSeat)
                        seatUsed.setValue(usedSeat)



                    }

                    if(availableSeat == 0){
                        //set to red light cause no more dy but no nee notify user unless they try to get
                        data.child("ledlgt").setValue("1")
                        data.child("lcdbkR").setValue("50")
                        data.child("lcdtxt").setValue(String.format("%02d", availableSeat) + " Seat Left...." )
                        data.child("lcdscr").setValue("1")
                        data.child("camera").setValue("1")
                    }


                }
                else{
                    resetFBData()
                    imageAvailable.setImageDrawable(getResources().getDrawable(R.drawable.cancel))
                    numberSeat!!.text = "No more seat"

                    data.child("ledlgt").setValue("1")
                    data.child("lcdbkR").setValue("50")
                    data.child("lcdtxt").setValue(String.format("%02d", availableSeat) + " Seat Left...." )

                    data.child("lcdscr").setValue("1")
                    data.child("camera").setValue("1")

                }

            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onResume() {
        super.onResume()

        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY),SensorManager.SENSOR_DELAY_NORMAL)
    }

    //When the sensor is paused, stop reading value
    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
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