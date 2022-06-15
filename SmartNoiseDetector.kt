package com.example.iot_assignment

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_smart_noise_detector.*
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.String as String

class SmartNoiseDetector : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_noise_detector)

        val database1 = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
        val data1 = database1.getReference("PI_12_CONTROL")

        val currentDateTime = LocalDateTime.now()

        val formatterDate = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formattedDate = currentDateTime.format(formatterDate)

        val f: NumberFormat = DecimalFormat("00")
        val hourTEMP = f.format(currentDateTime.hour)

        var fetchDatabaseRef = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
                .reference.child("PI_12_$formattedDate")
        var lastQuery = fetchDatabaseRef.child("$hourTEMP").orderByKey().limitToLast(1)
//        var lastQuery = fetchDatabaseRef.child("$hour").orderByKey().limitToLast(1)
        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snapshot in snapshot.children ){
                    var rand1 = snapshot.child("rand1").getValue().toString()
                    var soundValue = snapshot.child("rand1").getValue()
                    Log.i("firebase", snapshot.getValue().toString())
                    Log.i("rand1", "$rand1")
                    sound_level.setText(rand1 +" dB")

                    val rand1Int = rand1.toDouble()

                    if(rand1Int>=40.0){
                        warning_message.setText("Please Be Quiet!")
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.quiet))
                        data1.child("relay1").setValue("0")
                        data1.child("relay2").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        data1.child("lcdtxt").setValue("Please Be Quiet!")
                        data1.child("oledsc").setValue("1")
                        data1.child("buzzer").setValue("1")
                        data1.child("lcdbkB").setValue("50")
                        data1.child("camera").setValue("1")
                    }

                    else{
                        warning_message.setText("Great! No Noise Detected")
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.volume))
                        data1.child("relay1").setValue("0")
                        data1.child("relay2").setValue("0")
                        data1.child("lcdscr").setValue("1")
                        data1.child("lcdtxt").setValue("Great! No Noise!")
                        data1.child("oledsc").setValue("1")
                        data1.child("lcdbkB").setValue("50")
                        data1.child("buzzer").setValue("0")
                        data1.child("camera").setValue("1")
                    }
                }
//                for (child in snapshot.getChildren()) {
//
//                    Log.d("firebase", child.child("rand2").value.toString())
//                    val temp = child.child("rand2").value.toString()
//                    Log.d("firebase", "$temp")
//
//                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        lastQuery.addValueEventListener(postListener)


        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Noise Detector")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

    }
}
