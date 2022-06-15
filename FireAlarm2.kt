package com.example.iot_assignment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.imgProfile2
import kotlinx.android.synthetic.main.activity_smart_discussion_room_unlock.*
import java.io.File

class FireAlarm2 : AppCompatActivity() {

    var database = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
    var data = database.getReference("PI_12_CONTROL")


    companion object{
        const val REQUEST_FROM_CAMERA = 102;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_alarm2)

        resetFBData()

        val toolbar: TextView =findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Fire Alarm")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }
        askCameraPermission()

    }

    private fun askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_FROM_CAMERA)
          }
            else{
              openCamera()
        }
    }

    private fun openCamera() {
        resetFBData()
        ImagePicker.with(this).cameraOnly().start(Camera.REQUEST_FROM_CAMERA)

        data.child("relay1").setValue("0")
        data.child("relay2").setValue("0")
        data.child("lcdbkB").setValue("50")
        data.child("lcdbkG").setValue("10")
        data.child("lcdbkR").setValue("10")
        data.child("lcdtxt").setValue("++Taking Photo++")
        data.child("lcdscr").setValue("1")
        data.child("oledsc").setValue("1")
        data.child("camera").setValue("1")


    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

            val captureImage: ImageView
            captureImage = findViewById(R.id.captureImage)


            if(resultCode== Activity.RESULT_OK){
                when(requestCode){
                    Camera.REQUEST_FROM_CAMERA ->{
                        captureImage.setImageURI(data!!.data)
                        FireStorage().uploadImage(this,data.data!!)

                        Log.i("testing","test")



                    }
                }



        }




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


