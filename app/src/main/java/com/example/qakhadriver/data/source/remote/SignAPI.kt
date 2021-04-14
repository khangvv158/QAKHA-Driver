package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.TokenAccess
import com.example.qakhadriver.data.source.remote.schema.request.*
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SignAPI {

    @POST("sign_in")
    fun signIn(@Body parameterName: CredentialRequest): Observable<TokenAccess>

    @POST("sign_up")
    fun signUp(@Body register: RegisterRequest): Observable<Boolean>

    @POST("check_email")
    fun checkEmail(@Body emailRequest: EmailRequest): Observable<Boolean>

    @POST("check_phone_number")
    fun checkPhoneNumber(@Body phoneRequest: PhoneRequest): Observable<Boolean>

    @POST("passwords/forgot")
    fun forgotPassword(@Body emailRequest: EmailRequest): Observable<MessageResponse>

    @POST("passwords/reset")
    fun resetPasswordByVerificationCode(
            @Body resetPasswordRequest: ResetPasswordRequest
    ): Observable<MessageResponse>
}
