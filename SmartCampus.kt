package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.cardview.widget.CardView

class SmartCampus : AppCompatActivity() {

    lateinit var canteen: CardView
    lateinit var library: CardView
    lateinit var hall: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_campus)

        canteen = findViewById(R.id.smartCanteen)
        library = findViewById(R.id.smartLibrary)
        hall = findViewById(R.id.smartHall)

        canteen.setOnClickListener() {
            Toast.makeText(this, "Welcome to Smart Canteen Module", Toast.LENGTH_LONG).show()
            val intent = Intent(this, CanteenMain::class.java)
            startActivity(intent)
        }

        library.setOnClickListener() {
            Toast.makeText(this, "Welcome to Smart Library Module", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LibraryMain::class.java)
            startActivity(intent)
        }

        hall.setOnClickListener() {
            Toast.makeText(this, "Welcome to Smart Hall Module", Toast.LENGTH_LONG).show()
            val intent = Intent(this, HallMain::class.java)
            startActivity(intent)
        }


    }
}