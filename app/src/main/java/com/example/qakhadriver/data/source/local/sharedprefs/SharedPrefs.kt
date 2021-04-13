package com.example.qakhadriver.data.source.local.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

interface SharedPrefs {
    fun <T> get(key: String, clazz: Class<T>): T
    fun <T> put(key: String, data: T)
    fun clear()
    fun clearKey(key: String)
}

class SharedPrefsImpl private constructor(context: Context) : SharedPrefs {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SharedPrefsKey.PREF_NAME,
        Context.MODE_PRIVATE
    )

    override fun <T> get(key: String, clazz: Class<T>): T {
        return when (clazz) {
            String::class.java -> sharedPreferences.getString(key, "").let { it as T }
            Boolean::class.java -> sharedPreferences.getBoolean(key, false).let { it as T }
            Float::class.java -> sharedPreferences.getFloat(key, 0f).let { it as T }
            Int::class.java -> sharedPreferences.getInt(key, 0).let { it as T }
            Long::class.java -> sharedPreferences.getLong(key, 0).let { it as T }
            else -> Gson().fromJson(sharedPreferences.getString(key, ""), clazz)
        }
    }

    override fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Float -> editor.putFloat(key, data)
            is Int -> editor.putInt(key, data)
            is Long -> editor.putLong(key, data)
            else -> editor.putString(key, Gson().toJson(data))
        }
        editor.apply()
    }

    override fun clear() {
        sharedPreferences.edit()?.clear()?.apply()
    }

    override fun clearKey(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    companion object {

        @Volatile
        private var instance: SharedPrefsImpl? = null

        fun getInstance(context: Context): SharedPrefsImpl =
            instance ?: synchronized(this) {
                instance ?: SharedPrefsImpl(context).also {
                    instance = it
                }
            }
    }
}
