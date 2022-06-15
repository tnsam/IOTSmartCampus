package com.example.iot_assignment

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_speech_recognition.*
import java.lang.Exception
import java.util.*

class SpeechRecognition : AppCompatActivity() {
    private val REQUEST_CODE_SPEECH_INPUT = 100
    val database1 = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    val data1 = database1.getReference("PI_12_CONTROL")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speech_recognition)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Remote Control")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }


        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                //check if the light is on or off
                val checkingLight = snapshot.child("relay1").getValue().toString()
                if(checkingLight == "1"){
                    lightOnOff.text = "On"
                }
                else{
                    lightOnOff.text = "Off"
                }

                //check if the air conditioner is on or off
                var checkingAirCond = snapshot.child("relay2").getValue().toString()
                if(checkingAirCond == "1"){
                    airCondOnOff.text = "On"
                }
                else{
                    airCondOnOff.text = "Off"
                }

                //check if the projector is on or off
                var checkingProjector = snapshot.child("lcdtxt").getValue().toString()
                if(checkingProjector == "**Projector on**"){
                    projectorOnOff.text = "On"
                }
                else{
                    projectorOnOff.text = "Off"
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        data1.addValueEventListener(postListener)


        //when mic button is clicked
        micButton.setOnClickListener{
            speak()

        }
    }

    private fun speak(){
        //intent to show SpeechToText dialog
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something to control")

        try{
            //show SpeechToText dialog if there is no error
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        }
        catch (e : Exception){
            //display error message by using toast
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_CODE_SPEECH_INPUT ->{
                if(resultCode == Activity.RESULT_OK && null != data){
                    //get text from result
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    //set the text to textview
                    option.text = result!![0]

                    //relay1 is used to control the light
                    var relay1: String?= null

                    //relay 2 is used to control the air conditioner
                    var relay2: String?= null

                    //instruction to turn on
                    val lightOn = "on the light"
                    val airCondOn = "on air conditioner"
                    val projectorOn = "on projector"

                    //instruction to turn off
                    val lightOff = "off the light"
                    val airCondOff = "off air conditioner"
                    val projectorOff = "off projector"

                    //compare the string
                    if(option.text.equals(lightOn)){
                        relay1 = "1"
                        data1.child("relay1").setValue(relay1)
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        data1.child("lcdtxt").setValue("**Light is on***")
                        option.text = "Light is switched on"
                        lightOnOff.text = "On"
                    }
                    else if(option.text.equals(airCondOn)){
                        relay2 = "1"
                        data1.child("relay2").setValue(relay2)
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        data1.child("lcdtxt").setValue("**Air cond on***")
                        option.text = "Air conditioner is switched on"
                        airCondOnOff.text = "On"
//                        Thread.sleep(2000)
                    }
                    else if(option.text.equals(projectorOn)){
                        data1.child("lcdtxt").setValue("**Projector on**")
                        //off the light when the projector is turned on
                        data1.child("relay1").setValue("0")
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        option.text = "Projector is switched on"
                        projectorOnOff.text = "On"
//                        Thread.sleep(2000)
                    }
                    else if(option.text.equals(projectorOff)){
                        //on the light when the projector is switched off
                        data1.child("relay1").setValue("1")
                        data1.child("lcdtxt").setValue("**Projector off*")
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        option.text = "Projector is switched off"
                        projectorOnOff.text = "Off"
//                        Thread.sleep(2000)
                    }
                    else if (option.text.equals(lightOff)){
                        relay1 = "0"
                        data1.child("relay1").setValue(relay1)
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        option.text = "Light is switched off"
                        data1.child("lcdtxt").setValue("**Light is off**")
                        lightOnOff.text = "Off"
//                        Thread.sleep(2000)
                    }
                    else if(option.text.equals(airCondOff)){
                        relay2 = "0"
                        data1.child("relay2").setValue(relay2)
                        data1.child("camera").setValue("1")
                        data1.child("lcdscr").setValue("1")
                        option.text = "Air conditioner is switched off"
                        data1.child("lcdtxt").setValue("**Air cond off**")
                        airCondOnOff.text = "Off"
                    }
                    else{
                        //display error message by using toast
                        Toast.makeText(applicationContext, "Wrong input. Please try again!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


}