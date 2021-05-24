package com.example.qakhadriver.screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFlags()
        Handler(mainLooper).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
            finish()
        }, SPLASH_DISPLAY_TIME)
    }

    private fun setFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }

    companion object {
        private const val SPLASH_DISPLAY_TIME = 1000L
    }
}
