package com.example.qakhadriver.utils

interface BasePresenter<T> {

    fun onStart()
    fun onStop()
    fun setView(view: T?)
}
