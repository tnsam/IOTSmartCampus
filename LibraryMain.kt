package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_library_main.*

class LibraryMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library_main)

        soundmeter.setOnClickListener{
            val intent = Intent(this, SmartNoiseDetector::class.java)
            startActivity(intent)

        }

        booktrolley.setOnClickListener{
            val intent = Intent(this, SmartBookTrolley::class.java)
            startActivity(intent)

        }

        door.setOnClickListener {
            val intent = Intent(this, SmartDiscussionRoomUnlock::class.java)
            startActivity(intent)
        }

        reporting.setOnClickListener {
            val intent = Intent(this, Reporting::class.java)
            startActivity(intent)
        }

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Library Module")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }
    }



}