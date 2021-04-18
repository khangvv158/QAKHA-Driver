package com.example.qakhadriver.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.example.qakhadriver.R

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Context.showDialogWithListener(
    title: String, content: String,
    positiveNegativeListener: IPositiveNegativeListener,
    positiveText: String,
    cancelable: Boolean
) {
    buildDialog(this, title, content)
        .builder
        .positiveText(positiveText)
        .positiveColor(getColor(R.color.colorCarrotOrange, this))
        .onPositive { _, _ -> positiveNegativeListener.onPositive() }
        .cancelable(cancelable)
        .show()
}

private fun getColor(color: Int, context: Context): Int {
    return ContextCompat.getColor(context, color)
}

private fun buildDialog(
    callingClassContext: Context,
    title: String,
    content: String
): MaterialDialog {
    return MaterialDialog.Builder(callingClassContext)
        .title(title)
        .content(content)
        .build()
}
