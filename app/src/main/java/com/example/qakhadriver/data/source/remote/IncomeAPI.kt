package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.source.remote.schema.response.IncomeDayResponse
import com.example.qakhadriver.data.source.remote.schema.response.IncomeMonthResponse
import com.example.qakhadriver.data.source.remote.schema.response.IncomeYearResponse
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IncomeAPI {

    @GET("drivers/statistics/day")
    fun getIncomeByDay(
        @Query("day") dayMonthYear: String,
        @Header(Constants.AUTHORIZATION) token: String
    ): Observable<IncomeDayResponse>

    @GET("drivers/statistics/month")
    fun getIncomeByMonth(
        @Query("month") monthYear: String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Observable<IncomeMonthResponse>

    @GET("drivers/statistics/year")
    fun getIncomeByYear(
        @Query("year") year: String,
        @Header(Constants.AUTHORIZATION) token: String
    ) : Observable<IncomeYearResponse>
}
