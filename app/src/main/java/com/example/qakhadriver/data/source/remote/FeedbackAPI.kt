package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.Feedback
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface FeedbackAPI {

    @GET("feedbacks/driver")
    fun getFeedback(
        @Query("driver_id") idDriver: Int,
        @Header(Constants.AUTHORIZATION) token: String
    ): Observable<Feedback>
}
