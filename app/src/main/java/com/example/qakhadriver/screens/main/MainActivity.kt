package com.example.qakhadriver.screens.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.authentication.AuthenticationFragment
import com.example.qakhadriver.utils.addFragment

class MainActivity : AppCompatActivity() {

    private var doubleBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFlags()
        initViews()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (!doubleBackPressed) {
            this.doubleBackPressed = true
            Toast.makeText(
                this,
                getString(R.string.back_again_to_exit),
                Toast.LENGTH_SHORT
            ).show()
            Handler().postDelayed(Runnable { doubleBackPressed = false }, 2000)
            return
        } else {
            super.onBackPressed()
            return
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            currentFocus?.let {
                if (it is AppCompatEditText) {
                    hideKeyboard(it)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun initViews() {
        addFragment(AuthenticationFragment.newInstance(), R.id.containerView)
    }

    private fun setFlags() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
    }

    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
