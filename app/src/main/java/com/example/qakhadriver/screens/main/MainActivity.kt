package com.example.qakhadriver.screens.main

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.LanguageRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.authentication.AuthenticationFragment
import com.example.qakhadriver.utils.addFragment
import java.util.*


class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private val presenter by lazy {
        MainActivityPresenter(
            LanguageRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(baseContext))
        )
    }

    private var doubleBackPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.setView(this)
        presenter.onSetupLanguage()
        setFlags()
        initViews()
    }

    override fun onSetupLanguageSuccess(langCode: String) {
        setAppLocale(langCode)
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
        addFragment(AuthenticationFragment.newInstance(), R.id.containerViewMain)
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

    private fun setAppLocale(localeCode: String) {
        val resources: Resources = resources
        val dm: DisplayMetrics = resources.displayMetrics
        val config: Configuration = resources.configuration
        config.setLocale(Locale(localeCode.toLowerCase()))
        resources.updateConfiguration(config, dm)
    }
}
