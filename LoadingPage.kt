package com.example.iot_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView

class LoadingPage : AppCompatActivity() {

    lateinit var logo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading_page)

        logo = findViewById(R.id.splashScreen_LOGO)

        logo.alpha = 0f

        logo.animate().setDuration(2000).alpha(1f).withEndAction {
            val intent = Intent(this, SmartCampus::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}