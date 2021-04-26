package com.example.qakhadriver.data.repository

import android.content.Context
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.UploadCallback

interface CloudRepository {

    fun uploadFileToCloud(file: String, callback: UploadCallback)
}

class CloudRepositoryImpl private constructor(context: Context) : CloudRepository {
    init {
        MediaManager.init(context)
    }

    override fun uploadFileToCloud(file: String, callback: UploadCallback) {
        MediaManager.get().upload(file).callback(callback).dispatch()
    }

    companion object {

        private var instance: CloudRepositoryImpl? = null

        fun getInstance(context: Context): CloudRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: CloudRepositoryImpl(context).also {
                    instance = it
                }
            }
    }
}
