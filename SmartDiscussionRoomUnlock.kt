package com.example.iot_assignment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_camera.imgProfile2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_smart_discussion_room_unlock.*
import java.text.SimpleDateFormat
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.schedule

class SmartDiscussionRoomUnlock : AppCompatActivity() {
    companion object{
        const val REQUEST_FROM_CAMERA = 1001;
        const val REQUEST_FROM_GALLERY = 1002;
    }

    private var sensorManager: SensorManager? = null
    private var mProximitySensor: Sensor? = null
    private val mStorageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_discussion_room_unlock)

        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (mProximitySensor == null) {
            proximitySensor.text = "No Proximity Sensor!"
        } else {
            sensorManager!!.registerListener(proximitySensorEventListener, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

        val toolbar: TextView = findViewById(R.id.toolbar_title)
        toolbar.setText("Smart Door")

        val back: ImageView = findViewById(R.id.arrow_back_icon)
        back.setOnClickListener {
            onBackPressed()
        }
    }

    private var proximitySensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // method to check accuracy changed in sensor.
        }

        override fun onSensorChanged(event: SensorEvent) {
            val database1 = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
            val data1 = database1.getReference("PI_12_CONTROL")

            val params = this@SmartDiscussionRoomUnlock.window.attributes
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {

                if (event.values[0] == 0f) {
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    params.screenBrightness = 0f
                    window.attributes = params
                    Log.d("text", "my Message")

                    captureImageUsingCamera()

                } else {
                    params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    params.screenBrightness = -1f
                    window.attributes = params
                }
            }
        }
    }

    private fun captureImageUsingCamera(){
        ImagePicker.with(this).cameraOnly().start(Camera.REQUEST_FROM_CAMERA)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val now = LocalDateTime.now()

        val database1 = FirebaseDatabase.getInstance("https://bait2123-202101-12-default-rtdb.firebaseio.com/")
        val data1 = database1.getReference("PI_12_CONTROL")

        var formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

        if(resultCode== Activity.RESULT_OK){
            when(requestCode){
                Camera.REQUEST_FROM_CAMERA ->{
                    imgProfile2.setImageURI(data!!.data)
                    FirebaseStorage().uploadImage(this,data.data!!)


                    Log.i("testing","test")
                    message.setText("The door is now unlocked!")
                    access_img.setImageDrawable(getResources().getDrawable(R.drawable.tick))
                    timestamp.setText(formatter.format(now))
                    data1.child("relay1").setValue("1")
                    data1.child("relay2").setValue("1")
                    data1.child("lcdscr").setValue("1")
                    data1.child("lcdtxt").setValue("++++Unlocked++++")
                    data1.child("oledsc").setValue("1")
                    data1.child("lcdbkB").setValue("100")
                    data1.child("camera").setValue("1")

                    Handler().postDelayed({
                                Log.i("testing2","test2")
                                message.setText("Please take a photo \nbefore entering the room")
                                access_img.setImageDrawable(getResources().getDrawable(R.drawable.stopicon))
                                //imgProfile2.setImageDrawable(getResources().getDrawable(R.drawable.camera))
                                timestamp.setText(null)
                                data1.child("relay1").setValue("0")
                                data1.child("relay2").setValue("1")
                                data1.child("lcdscr").setValue("1")
                                data1.child("lcdtxt").setValue("+++++Locked+++++")
                                data1.child("oledsc").setValue("1")
                                data1.child("lcdbkB").setValue("100")
                                data1.child("camera").setValue("1")
                    }, 15000)
                }
            }
        }

//        Thread.sleep(20000)
//
//        Log.i("testing2","test2")
//        message.setText("Please take a photo \nbefore entering the room")
//        access_img.setImageDrawable(getResources().getDrawable(R.drawable.stopicon))
//        imgProfile2.setImageBitmap(null)
//        timestamp.setText(formatter.format(now))
//        data1.child("lcdtxt").setValue("+++++Locked+++++\"")
//        data1.child("oledsc").setValue("0")
//        data1.child("lcdbkB").setValue("100")
//        data1.child("camera").setValue("1")
    }
}