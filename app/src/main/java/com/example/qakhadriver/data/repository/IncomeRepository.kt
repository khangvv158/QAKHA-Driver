package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.source.remote.IncomeAPI
import com.example.qakhadriver.data.source.remote.RetrofitClient
import com.example.qakhadriver.data.source.remote.schema.response.CoinResponse
import com.example.qakhadriver.data.source.remote.schema.response.IncomeDayResponse
import com.example.qakhadriver.data.source.remote.schema.response.IncomeMonthResponse
import com.example.qakhadriver.data.source.remote.schema.response.IncomeYearResponse
import io.reactivex.rxjava3.core.Observable

interface IncomeRepository {

    fun getIncomeByDay(dayMonthYear: String, token: String): Observable<IncomeDayResponse>
    fun getIncomeByMonth(monthYear: String, token: String): Observable<IncomeMonthResponse>
    fun getIncomeByYear(year: String, token: String): Observable<IncomeYearResponse>
    fun getCoin(token: String): Observable<CoinResponse>
}

class IncomeRepositoryImpl : IncomeRepository {

    private val client = RetrofitClient.getInstance().create(IncomeAPI::class.java)

    override fun getIncomeByDay(
        dayMonthYear: String,
        token: String
    ): Observable<IncomeDayResponse> = client.getIncomeByDay(dayMonthYear, token)

    override fun getIncomeByMonth(
        monthYear: String,
        token: String
    ): Observable<IncomeMonthResponse> = client.getIncomeByMonth(monthYear, token)

    override fun getIncomeByYear(year: String, token: String): Observable<IncomeYearResponse> =
        client.getIncomeByYear(year, token)

    override fun getCoin(token: String) = client.getCoin(token)

    companion object {

        @Volatile
        private var instance: IncomeRepositoryImpl? = null

        fun getInstance(): IncomeRepositoryImpl =
            instance ?: synchronized(this) {
                instance ?: IncomeRepositoryImpl().also {
                    instance = it
                }
            }
    }
}
