package com.example.iot_assignment

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FireStorage {

    val storage = Firebase.storage("gs://bait2123-202101-12.appspot.com")
    val storageRef = storage.getReference("Campus/")

    val imagesRef = storage.getReference("images/")
    private val TAG = "FirebaseStorage"

    private lateinit var mProgressDialog: ProgressDialog

    @RequiresApi(Build.VERSION_CODES.O)

    fun uploadImage(mContext: Context, imageURI: Uri){

        mProgressDialog = ProgressDialog(mContext)
        mProgressDialog.setMessage("Please wait, image being uploaded...")
        mProgressDialog.show()

        var imageFileName = "fire/${System.currentTimeMillis()}.png"
        val uploadTask = storageRef.child(imageFileName).putFile(imageURI)

        val imageFileName2 = "oled.jpg"
        val uploadTask2 = imagesRef.child(imageFileName2).putFile(imageURI)

        uploadTask.addOnSuccessListener {
            Log.e(TAG,"Image uploaded successfully")
            val downloadUrLTask = storageRef.child(imageFileName).downloadUrl

            downloadUrLTask.addOnSuccessListener {
                val database2 = FirebaseDatabase.getInstance("https://testing-9b50f-default-rtdb.firebaseio.com/")
                val data2 = database2.getReference("fire")

                val key = data2.push().key
                data2.child(key.toString()).child("image").setValue("${it}")


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