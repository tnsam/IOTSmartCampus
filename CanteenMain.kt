package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class CanteenMain : AppCompatActivity() {

    lateinit var module1: CardView
    lateinit var module2: CardView
    lateinit var module3: CardView
    lateinit var module4: CardView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_canteen_main)

        module1 = findViewById(R.id.module1)
        module2 = findViewById(R.id.module2)
        module3 = findViewById(R.id.module3)


        module1.setOnClickListener{
            val intent = Intent(this, SmartEntrance::class.java)
            startActivity(intent)

        }

        module2.setOnClickListener {
            val intent = Intent(this, SeatCounter::class.java)
            startActivity(intent)

        }

        module3.setOnClickListener {
            val intent = Intent(this, SmartFireAlarm::class.java)
            startActivity(intent)
        }

//        module4.setOnClickListener {
//            val intent = Intent(this, OrderCalling::class.java)
//            startActivity(intent)
//        }

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Canteen Module")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }


    }
}