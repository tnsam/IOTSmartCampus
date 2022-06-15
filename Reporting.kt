package com.example.iot_assignment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_reporting.*

class Reporting : AppCompatActivity() {

    lateinit var ref:DatabaseReference
    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reporting)

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Reporting")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }

        val database2 = FirebaseDatabase.getInstance("https://testing-9b50f-default-rtdb.firebaseio.com/")
        ref = database2.getReference("report_photo")

        val database_listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    counter++

                    if(counter == 1){
                        val key = ds.key
//                    val imageLink = ds.value.toString()
                        val imageLink = ds.child("image").getValue()
                        val timestamp_msg = ds.child("timestamp").getValue()
                        Log.i("imageLink",imageLink.toString())
                        Log.i("timestamp_msg",timestamp_msg.toString())


                        val imgUri = Uri.parse(imageLink.toString())
                        //val imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/bait2123-202101-12.appspot.com/o/users%2Fprofile1616947747211.png?alt=media&token=5153adb2-5e52-4ba8-8881-0cb459f22b95")
                        Log.i("Testing", imgUri.toString())


                        Glide.with(applicationContext).load(imgUri).into(pic1)
                        text1.setText(timestamp_msg.toString())
                    }

                    else if(counter == 2){
                        val key = ds.key
//                    val imageLink = ds.value.toString()
                        val imageLink = ds.child("image").getValue()
                        val timestamp_msg = ds.child("timestamp").getValue()
                        Log.i("imageLink",imageLink.toString())
                        Log.i("timestamp_msg",timestamp_msg.toString())


                        val imgUri = Uri.parse(imageLink.toString())
                        //val imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/bait2123-202101-12.appspot.com/o/users%2Fprofile1616947747211.png?alt=media&token=5153adb2-5e52-4ba8-8881-0cb459f22b95")
                        Log.i("Testing", imgUri.toString())


                        Glide.with(applicationContext).load(imgUri).into(pic2)
                        text2.setText(timestamp_msg.toString())
                    }

                    else if(counter == 3){
                        val key = ds.key
//                    val imageLink = ds.value.toString()
                        val imageLink = ds.child("image").getValue()
                        val timestamp_msg = ds.child("timestamp").getValue()
                        Log.i("imageLink",imageLink.toString())
                        Log.i("timestamp_msg",timestamp_msg.toString())


                        val imgUri = Uri.parse(imageLink.toString())
                        //val imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/bait2123-202101-12.appspot.com/o/users%2Fprofile1616947747211.png?alt=media&token=5153adb2-5e52-4ba8-8881-0cb459f22b95")
                        Log.i("Testing", imgUri.toString())


                        Glide.with(applicationContext).load(imgUri).into(pic3)
                        text3.setText(timestamp_msg.toString())
                    }

                    else if(counter == 4){
                        val key = ds.key
//                    val imageLink = ds.value.toString()
                        val imageLink = ds.child("image").getValue()
                        val timestamp_msg = ds.child("timestamp").getValue()
                        Log.i("imageLink",imageLink.toString())
                        Log.i("timestamp_msg",timestamp_msg.toString())


                        val imgUri = Uri.parse(imageLink.toString())
                        //val imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/bait2123-202101-12.appspot.com/o/users%2Fprofile1616947747211.png?alt=media&token=5153adb2-5e52-4ba8-8881-0cb459f22b95")
                        Log.i("Testing", imgUri.toString())


                        Glide.with(applicationContext).load(imgUri).into(pic4)
                        text4.setText(timestamp_msg.toString())
                    }

                    else {
                        val key = ds.key
//                    val imageLink = ds.value.toString()
                        val imageLink = ds.child("image").getValue()
                        val timestamp_msg = ds.child("timestamp").getValue()
                        Log.i("imageLink",imageLink.toString())
                        Log.i("timestamp_msg",timestamp_msg.toString())


                        val imgUri = Uri.parse(imageLink.toString())
                        //val imgUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/bait2123-202101-12.appspot.com/o/users%2Fprofile1616947747211.png?alt=media&token=5153adb2-5e52-4ba8-8881-0cb459f22b95")
                        Log.i("Testing", imgUri.toString())


                        Glide.with(applicationContext).load(imgUri).into(pic5)
                        text5.setText(timestamp_msg.toString())
                    }

                }

                counter = 0
            }
        }
        ref.addValueEventListener(database_listener)


    }

}