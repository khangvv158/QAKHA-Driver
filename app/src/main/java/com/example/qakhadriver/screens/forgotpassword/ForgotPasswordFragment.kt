package com.example.qakhadriver.screens.forgotpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.forgotpassword.resetpassword.ResetPasswordFragment
import com.example.qakhadriver.screens.forgotpassword.resetpassword.ResetPasswordSuccess
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : Fragment(), ForgotPasswordContract.View, ResetPasswordSuccess {

    private val presenter by lazy {
        ForgotPasswordPresenter(
            SignRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(
                    requireContext()
                )
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onStop() {
        presenter.onStop()
        super.onStop()
    }

    override fun onForgotPasswordSuccess(email: String) {
        progressBar.gone()
        addFragmentFadeAnim(
            ResetPasswordFragment.newInstance(email).apply {
                registerListener(this@ForgotPasswordFragment)
            },
            R.id.containerViewAuthentication
        )
    }

    override fun onForgotPasswordFailure() {
        emailEditTextLayout.error =
            getString(R.string.email_address_not_found_please_check_and_try_again)
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onResetPasswordSuccess() {
        parentFragmentManager.popBackStack()
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailEditTextLayout.error = getString(R.string.email_cant_be_blank)
            false
        } else if (!email.validWithRegex(RegexKey.VALID_EMAIL_REGEX)) {
            emailEditTextLayout.error = getString(R.string.email_is_invalid)
            false
        } else {
            emailEditTextLayout.error = null
            true
        }
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            hideKeyboard()
            parentFragmentManager.popBackStack()
        }
        forgotPasswordButton.setOnSafeClickListener {
            val validateEmail = validateEmail(emailEditText.text.toString())
            if (validateEmail) {
                progressBar.show()
                presenter.forgotPassword(emailEditText.text.toString())
            }
        }
    }

    companion object {
        fun newInstance() = ForgotPasswordFragment()
    }
}
