package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.w3c.dom.Text

class RoomLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room_login)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Hall Login")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        val validRoomList = arrayOf<String>("K101", "K102", "K103", "K104", "K105")

        //check if the room number entered valid or not when the user press login button
        findViewById<Button>(R.id.login_button).setOnClickListener {
            var input = findViewById<EditText>(R.id.input_room_no).text.toString()
            if(validRoomList.contains(input)){
                val intent = Intent(this, SpeechRecognition::class.java)
                startActivity(intent)
            }
            else{
                //invalid room number
                Toast.makeText(applicationContext, "Please enter valid hall ID!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}