package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.Feedback
import com.example.qakhadriver.data.source.remote.FeedbackAPI
import com.example.qakhadriver.data.source.remote.RetrofitClient
import io.reactivex.rxjava3.core.Observable

interface FeedbackRepository {

    fun getFeedback(idDriver: Int, token: String): Observable<Feedback>
}

class FeedbackRepositoryImpl : FeedbackRepository {

    private val client = RetrofitClient.getInstance().create(FeedbackAPI::class.java)

    override fun getFeedback(idDriver: Int, token: String) = client.getFeedback(idDriver, token)

    companion object {

        @Volatile
        private var instance: FeedbackRepositoryImpl? = null

        fun getInstance(): FeedbackRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: FeedbackRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
