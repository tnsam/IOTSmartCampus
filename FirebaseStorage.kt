package com.example.iot_assignment

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_smart_discussion_room_unlock.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FirebaseStorage {

    val storage = Firebase.storage("gs://bait2123-202101-12.appspot.com")
    val storageRef = storage.getReference("Campus/")

    val imagesRef = storage.getReference("images/")

    private val TAG = "FirebaseStorage"

    private lateinit var mProgressDialog:ProgressDialog

    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadImage(mContext: Context, imageURI: Uri){
        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog.setMessage("Please wait, image being uploaded...")
        mProgressDialog.show()


        var imageFileName = "profile/${System.currentTimeMillis()}.png"
        val uploadTask = storageRef.child(imageFileName).putFile(imageURI)

        val imageFileName2 = "oled.jpg"
        val uploadTask2 = imagesRef.child(imageFileName2).putFile(imageURI)

        uploadTask.addOnSuccessListener {
            Log.e(TAG,"Image uploaded successfully")
            val downloadUrLTask = storageRef.child(imageFileName).downloadUrl

            downloadUrLTask.addOnSuccessListener {
                val database2 = FirebaseDatabase.getInstance("https://testing-9b50f-default-rtdb.firebaseio.com/")
                val data2 = database2.getReference("report_photo")
                val now = LocalDateTime.now()
                var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

                val key = data2.push().key
                data2.child(key.toString()).child("image").setValue("${it}")
                data2.child(key.toString()).child("timestamp").setValue(formatter.format(now))


                Log.i("Testing123","IMAGE PATH: ${it}")
                Log.e(TAG,"IMAGE PATH: ${it}")

                mProgressDialog.dismiss()
            }.addOnFailureListener{
                mProgressDialog.dismiss()
            }
        }.addOnFailureListener{
            Log.e(TAG,"Image upload failed ${it.printStackTrace()}")
            mProgressDialog.dismiss()
        }

        uploadTask2.addOnSuccessListener {
            Log.e(TAG, "Image uploaded successfully")
            val downloadUrLTask = storageRef.child(imageFileName).downloadUrl
        }
    }
}