package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.TokenAccess
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefs
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsKey
import com.example.qakhadriver.utils.Constants.DEFAULT_STRING

interface TokenRepository {

    fun getToken(): String

    fun clearToken()
}

class TokenRepositoryImpl private constructor(
    private val sharedPrefs: SharedPrefs
) : TokenRepository {

    override fun getToken(): String {
        return if (sharedPrefs.get(SharedPrefsKey.TOKEN_KEY, TokenAccess::class.java) != null) {
            sharedPrefs
                .get(SharedPrefsKey.TOKEN_KEY, TokenAccess::class.java)
                .token ?: DEFAULT_STRING
        } else {
            DEFAULT_STRING
        }
    }

    override fun clearToken() {
        sharedPrefs.clearKey(SharedPrefsKey.TOKEN_KEY)
    }

    companion object {

        private var instance: TokenRepositoryImpl? = null

        fun getInstance(sharedPrefs: SharedPrefs): TokenRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: TokenRepositoryImpl(sharedPrefs).also {
                    instance = it
                }
            }
    }
}
