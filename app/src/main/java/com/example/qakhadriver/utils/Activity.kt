package com.example.qakhadriver.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.addFragment(fragment: Fragment, id: Int) {
    supportFragmentManager.inTransaction {
        add(id, fragment)
    }
}

fun AppCompatActivity.addFragmentBackStack(fragment: Fragment, id: Int) {
    supportFragmentManager.inTransaction {
        add(id, fragment)
        addToBackStack(null)
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, id: Int) {
    supportFragmentManager.inTransaction {
        replace(id, fragment)
        addToBackStack(null)
    }
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    supportFragmentManager.inTransaction { remove(fragment) }
}
