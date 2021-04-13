package com.example.qakhadriver.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.toString(format: String): String {
    return SimpleDateFormat(format, Locale.US).format(this)
}
