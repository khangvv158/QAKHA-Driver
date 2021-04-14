package com.example.qakhadriver.data.repository

import com.example.qakhadriver.data.model.TokenAccess
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefs
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsKey
import com.example.qakhadriver.data.source.remote.RetrofitClient
import com.example.qakhadriver.data.source.remote.SignAPI
import com.example.qakhadriver.data.source.remote.schema.request.*
import com.example.qakhadriver.data.source.remote.schema.response.MessageResponse
import com.example.qakhadriver.utils.Constants
import io.reactivex.rxjava3.core.Observable

interface SignRepository {

    fun signIn(
            email: String,
            password: String,
    ): Observable<TokenAccess>

    fun signUp(
            email: String,
            password: String,
            passwordConfirmation: String,
            phoneNumber: String,
            name: String,
            idCard: String,
            licensePlate: String
    ): Observable<Boolean>

    fun checkEmail(emailRequest: EmailRequest): Observable<Boolean>

    fun checkPhoneNumber(phoneRequest: PhoneRequest): Observable<Boolean>

    fun forgotPassword(emailRequest: EmailRequest): Observable<MessageResponse>

    fun resetPasswordByVerificationCode(resetPasswordRequest: ResetPasswordRequest): Observable<MessageResponse>
}

class SignRepositoryImpl private constructor(
        private val sharedPrefs: SharedPrefs
) : SignRepository {

    private val client = RetrofitClient.getInstance().create(SignAPI::class.java)

    override fun signIn(
            email: String,
            password: String
    ): Observable<TokenAccess> =
            client.signIn(
                    CredentialRequest(
                            email,
                            password
                    )
            ).doOnNext {
                if (it.role == Constants.ROLE_MEMBER) {
                    sharedPrefs.put(SharedPrefsKey.TOKEN_KEY, it)
                }
            }

    override fun signUp(
            email: String,
            password: String,
            passwordConfirmation: String,
            phoneNumber: String,
            name: String,
            idCard: String,
            licensePlate: String
    ): Observable<Boolean> =
            client.signUp(
                    RegisterRequest(
                            email,
                            password,
                            passwordConfirmation,
                            phoneNumber,
                            name,
                            idCard,
                            licensePlate
                    )
            )

    override fun checkEmail(emailRequest: EmailRequest): Observable<Boolean> =
            client.checkEmail(emailRequest)

    override fun checkPhoneNumber(phoneRequest: PhoneRequest): Observable<Boolean> =
            client.checkPhoneNumber(phoneRequest)

    override fun forgotPassword(emailRequest: EmailRequest): Observable<MessageResponse> =
            client.forgotPassword(emailRequest)

    override fun resetPasswordByVerificationCode(
            resetPasswordRequest: ResetPasswordRequest
    ): Observable<MessageResponse> = client.resetPasswordByVerificationCode(resetPasswordRequest)

    companion object {

        @Volatile
        private var instance: SignRepositoryImpl? = null

        fun getInstance(
                sharedPrefs: SharedPrefs,
        ): SignRepositoryImpl =
                instance ?: synchronized(this) {
                    instance ?: SignRepositoryImpl(sharedPrefs).also {
                        instance = it
                    }
                }
    }
}
