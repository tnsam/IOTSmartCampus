package com.example.iot_assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class OrderCalling : AppCompatActivity() {

    lateinit var btn: Button
    lateinit var image: ImageView
    lateinit var text: TextView

    val database =
        FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    val data = database.getReference("PI_12_CONTROL")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_calling)

        btn = findViewById(R.id.btn)
        image = findViewById(R.id.imageView)
        text = findViewById(R.id.textView)

        image.setImageDrawable(getResources().getDrawable(R.drawable.preparing))
        text.setText("Your order is preparing now")
        data.child("relay1").setValue("0")
        data.child("relay2").setValue("0")
        data.child("lcdbkB").setValue("10")
        data.child("lcdbkG").setValue("10")
        data.child("lcdbkR").setValue("10")
        data.child("oledsc").setValue("0")
        data.child("buzzer").setValue("0")
        data.child("lcdtxt").setValue("Preparing order.")



        btn.setOnClickListener {


            image.setImageDrawable(getResources().getDrawable(R.drawable.done))
            text.setText("Pls come and take ur order now !!! ")

            data.child("relay1").setValue("1")
            data.child("relay2").setValue("1")
            data.child("lcdbkB").setValue("100")
            data.child("lcdbkG").setValue("100")
            data.child("lcdbkR").setValue("100")
            data.child("lcdtxt").setValue("Pls take ur food")
            data.child("oledsc").setValue("1")
            data.child("buzzer").setValue("1")
            data.child("camera").setValue("1")


        }

    }

}