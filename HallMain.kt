package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

class HallMain : AppCompatActivity() {
    lateinit var module1: CardView
    lateinit var module2: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hall_main)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Hall Module")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        module1 = findViewById(R.id.module1)
        module2 = findViewById(R.id.module2)


        module1.setOnClickListener{
            val intent = Intent(this, SmartLight::class.java)
            startActivity(intent)

        }

        module2.setOnClickListener {
            val intent = Intent(this, RoomLogin::class.java)
            startActivity(intent)
        }
    }
}