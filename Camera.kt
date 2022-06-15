package com.example.iot_assignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_camera.*

class Camera : AppCompatActivity() {
    companion object{
        const val REQUEST_FROM_CAMERA = 1001;
        const val REQUEST_FROM_GALLERY = 1002;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initUI()
    }

    private fun initUI(){
        btnCamera.setOnClickListener{
            captureImageUsingCamera()
        }

        btnGallery2.setOnClickListener{
            pickImageFromGallery()
        }
    }

    private fun  pickImageFromGallery(){
        ImagePicker.with(this).galleryOnly().start(REQUEST_FROM_GALLERY)
    }

    private fun captureImageUsingCamera(){
        ImagePicker.with(this).cameraOnly().start(REQUEST_FROM_CAMERA)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK){
            when(requestCode){
                REQUEST_FROM_CAMERA ->{
                    imgProfile2.setImageURI(data!!.data)
                    FirebaseStorage().uploadImage(this,data.data!!)
                }

                REQUEST_FROM_GALLERY -> {
                    imgProfile2.setImageURI(data!!.data)
                    FirebaseStorage().uploadImage(this,data.data!!)
                }
            }

        }
    }
}