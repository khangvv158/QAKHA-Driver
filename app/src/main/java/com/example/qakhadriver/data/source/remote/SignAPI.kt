package com.example.qakhadriver.data.source.remote

import com.example.qakhadriver.data.model.TokenAccess
import com.example.qakhadriver.data.source.remote.schema.request.*
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface SignAPI {

    @POST("sign_in_driver")
    fun signIn(@Body parameterName: CredentialRequest): Observable<TokenAccess>

    @POST("sign_up")
    fun signUp(@Body register: RegisterRequest): Observable<MessageResponse>

    @POST("check_email")
    fun checkEmail(@Body emailRequest: EmailRequest): Observable<Boolean>

    @POST("check_phone_number")
    fun checkPhoneNumber(@Body phoneRequest: PhoneRequest): Observable<Boolean>

    @POST("passwords/forgot_pw_driver")
    fun forgotPassword(@Body emailRequest: EmailRequest): Observable<MessageResponse>

    @POST("passwords/reset_pw_driver")
    fun resetPasswordByVerificationCode(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Observable<MessageResponse>

    @POST("check_id_card_driver")
    fun checkIdentifyCard(@Body identifyCardRequest: IdentifyCardRequest): Observable<Boolean>

    @POST("check_license_plate_driver")
    fun checkLicensePlate(@Body licensePlateRequest: LicensePlateRequest): Observable<Boolean>

    @POST("activated_account_driver")
    fun activateAccount(
        @Body activateRequest: ActivateRequest
    ): Observable<MessageResponse>

    @POST("driver/confirmation")
    fun resendCodeActivate(@Body emailRequest: EmailRequest): Observable<MessageResponse>
}
