package com.example.caqao

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.caqao.onboarding.OnboardingActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DELAY: Long = 1500 // Delay in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        // Set the layout for the splash screen activity
        setContentView(R.layout.splash_screen)

        // Create a handler to delay the launch of the main activity
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }
}
