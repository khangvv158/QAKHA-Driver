package com.example.qakhadriver.screens.forgotpassword.resetpassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_reset_password.*

class ResetPasswordFragment : Fragment(), ResetPasswordContract.View {

    private val presenter by lazy {
        ResetPasswordPresenter(
            SignRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(
                    requireContext()
                )
            )
        )
    }
    private var resetPasswordSuccess: ResetPasswordSuccess? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
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

    override fun onResetPasswordSuccess() {
        progressBar.gone()
        makeText(getString(R.string.create_a_new_password_successfully))
        resetPasswordSuccess?.onResetPasswordSuccess()
        parentFragmentManager.popBackStack()
    }

    override fun onResetPasswordFailure() {
        progressBar.gone()
        makeText(getString(R.string.code_not_valid_or_expired_try_generating_a_new_code))
    }

    override fun onGeneratingCodeSuccess() {
        progressBar.gone()
        makeText(getString(R.string.generating_code))
    }

    override fun onGeneratingCodeFailure() {
        progressBar.gone()
    }

    fun registerListener(resetPasswordSuccess: ResetPasswordSuccess) {
        this.resetPasswordSuccess = resetPasswordSuccess
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        resetPasswordButton.setOnSafeClickListener {
            val validatePassword = validatePassword(editTextNewPassword.text.toString())
            val validateVerificationCode = validateVerificationCode(
                editTextVerificationCode.text.toString()
            )
            if (validatePassword && validateVerificationCode) {
                progressBar.show()
                presenter.resetPassword(
                    editTextNewPassword.text.toString(),
                    editTextVerificationCode.text.toString().trim()
                )
            }
        }
        textViewGeneratingCode.setOnSafeClickListener {
            arguments?.getString(BUNDLE_EMAIL)?.let {
                presenter.generatingCode(it)
            }
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            editTextLayoutNewPassword.error = getString(R.string.password_cant_be_blank)
            false
        } else if (!password.validWithRegex(RegexKey.VALID_PASSWORD_REGEX)) {
            editTextLayoutNewPassword.error =
                getString(R.string.description_validate_password)
            false
        } else {
            editTextLayoutNewPassword.error = null
            true
        }
    }

    private fun validateVerificationCode(verificationCode: String): Boolean {
        return if (verificationCode.isEmpty()) {
            editTextLayoutVerificationCode.error = getString(R.string.token_not_present)
            false
        } else {
            editTextLayoutVerificationCode?.error = null
            true
        }
    }

    companion object {
        private const val BUNDLE_EMAIL = "BUNDLE_EMAIL"

        fun newInstance(email: String) = ResetPasswordFragment().apply {
            arguments = bundleOf(BUNDLE_EMAIL to email)
        }
    }
}
