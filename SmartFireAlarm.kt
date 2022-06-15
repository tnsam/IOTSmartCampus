package com.example.iot_assignment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.hardware.Sensor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log

class SmartFireAlarm : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    var data = database.getReference("PI_12_CONTROL")

    lateinit var image: ImageView
    lateinit var text: TextView
    lateinit var  sensor: TextView

    lateinit var button: Button
    lateinit var stop: Button

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder
    private val channelId = "i.apps.notifications"
    private val description = "Test notification"

    var counter = 1


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_fire_alarm)

        resetFBData()

        image = findViewById(R.id.imageView)
        text = findViewById(R.id.textView3)
        sensor = findViewById(R.id.sensor)

        val toolbar: TextView =findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Fire Detector")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()

        }

        button = findViewById(R.id.capture)
        button.setOnClickListener {
            Toast.makeText(this, "Camera is activate now", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, FireAlarm2::class.java)
            startActivity(intent)
        }

        stop = findViewById(R.id.stop)
        stop.setOnClickListener {
            counter += 1
        }

      Log.i("counter","$counter")

        val currentDateTime = LocalDateTime.now()

        val formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formattedDate = currentDateTime.format(formatterDate)

        val f: NumberFormat = DecimalFormat("00")
        val hourTEMP = f.format(currentDateTime.hour)


        var fetchDatabaseRef = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
                .reference.child("PI_12_$formattedDate")
        var giveData = fetchDatabaseRef.child("$hourTEMP").orderByKey().limitToLast(1)

        val textView:TextView = findViewById(R.id.textView)

        val postListener = object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                Log.i("firebase", snapshot.getValue().toString())

                for (child in snapshot.getChildren()) {

                    var temp = child.child("rand1").value.toString().toFloat()

                    Log.d("firebase", "$temp")

                    val value = tempValue(temp)
                    sensor.setText("$value" + ".0 °C")
                    Log.d("firebase", "$value")



                    if ((value >= 25 && value < 40)  ){

                        resetFBData()

                        image.setImageDrawable(getResources().getDrawable(R.drawable.thumbsup))
                        textView.setText("Temperature : ")
                        data.child("relay1").setValue("1")
                        data.child("relay2").setValue("0")

                        data.child("lcdbkB").setValue("50")
                        data.child("lcdbkG").setValue("50")
                        data.child("lcdbkR").setValue("50")
                        data.child("lcdtxt").setValue(String.format("%02d", value) +".0°C"+ " good temp")
                        data.child("lcdscr").setValue("1")
                        data.child("oledsc").setValue("1")

                        data.child("camera").setValue("1")

                        text.setText("All in good condition.")



                    }
                    else if(value >= 40){

                        counter = counter % 2
                        Log.i("counterFire","$counter")
                        if(counter == 0){

                            sensor.setText("")

                            textView.setText("Extinguishing Now")
                            resetFBData()
                            image.setImageDrawable(getResources().getDrawable(R.drawable.smokedetector))
                            data.child("relay1").setValue("1")
                            data.child("relay2").setValue("0")

                            data.child("lcdbkB").setValue("10")
                            data.child("lcdbkG").setValue("10")
                            data.child("lcdbkR").setValue("0")
                            data.child("lcdtxt").setValue("Extinguishing...")
                            data.child("lcdscr").setValue("1")
                            data.child("oledsc").setValue("1")

                            data.child("camera").setValue("1")

                            text.setText("")

                            counter += 1
                            Thread.sleep(2000)

                    }else{
                            resetFBData()

                            image.setImageDrawable(getResources().getDrawable(R.drawable.fire))
                            textView.setText("Temperature : ")
                        data.child("relay1").setValue("0")
                        data.child("relay2").setValue("1")

                        data.child("lcdbkB").setValue("10")
                        data.child("lcdbkG").setValue("0")
                        data.child("lcdbkR").setValue("40")
                        data.child("lcdtxt").setValue(String.format("%02d", value) +".0°C"+ " high temp")
                        data.child("lcdscr").setValue("1")
                            data.child("oledsc").setValue("1")

                        data.child("camera").setValue("1")

                            text.setText("Pls check ur stall, the temperature is very high!!!")
                            notification()


                        }

                }
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        giveData.addValueEventListener(postListener)

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    private fun notification(){
        //new
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationChannel = NotificationChannel(
            channelId,
            description,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)

        notificationManager.createNotificationChannel(notificationChannel)
        builder = Notification.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable.ic_baseline_notification_important_24
                )
            )
            .setContentIntent(pendingIntent)
            .setContentTitle("Alarm notification")
            .setContentText("Pls checking ur stall...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_ALARM)

        notificationManager.notify(1234, builder.build())

//        notificationManager.cancel(1234)

    }

    private fun tempValue(temperature: Float): Int {
        var random : Int = 33
        if(temperature >= 0.0 && temperature < 35.0){

            random = rand(25, 37)
        }
        else if (temperature >= 40){
            random = rand(40, 50)
        }
        return random
    }

    private fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Illegal Argument" }
        return (start..end).random()
    }

    fun resetFBData(){
        data.child("relay1").setValue("0")
        data.child("relay2").setValue("0")
        data.child("lcdbkB").setValue("0")
        data.child("lcdbkG").setValue("0")
        data.child("lcdbkR").setValue("0")
        data.child("lcdtxt").setValue(" ")
        data.child("buzzer").setValue("0")

        data.child("lcdscr").setValue("1")
        data.child("ledlgt").setValue("1")
        data.child("oledsc").setValue("1")
        data.child("sound").setValue("0")
        data.child("camera").setValue("0")
}



}



