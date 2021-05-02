package com.example.qakhadriver.screens.me.navigate.settings.changepassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.ProfileRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.request.ChangePasswordRequest
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_change_password.*

class ChangePasswordFragment : Fragment(), ChangePasswordContract.View {

    private val presenter by lazy {
        ChangePasswordPresenter(
            ProfileRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onChangePasswordSuccess() {
        makeText(getString(R.string.change_password_successfully))
        clearText()
        loadingProgress.gone()
        parentFragmentManager.popBackStack()
    }

    override fun onChangePasswordSuccessFailure(message: String) {
        loadingProgress.gone()
        if (message.contains("Current password don't match")) {
            editTextLayoutCurrentPassword.error = getString(R.string.current_password_dont_match)
        }
    }

    override fun onError(message: String) {
        makeText(message)
        loadingProgress.gone()
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            back()
        }
        changePasswordButton.setOnClickListener {
            val checkPassword = validatePassword(editTextNewPassword.text.toString())
            val checkPasswordConfirmation =
                validatePasswordConfirmation(editTextConfirmationPassword.text.toString())
            if (checkPassword && checkPasswordConfirmation) {
                hideKeyboard()
                loadingProgress.show()
                presenter.changePassword(
                    ChangePasswordRequest(
                        editTextCurrentPassword.text.toString(),
                        editTextNewPassword.text.toString(),
                        editTextConfirmationPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun clearText() {
        editTextLayoutCurrentPassword.error = null
        editTextCurrentPassword.text = null
        editTextLayoutNewPassword.error = null
        editTextNewPassword.text = null
        editTextLayoutConfirmationPassword.error = null
        editTextConfirmationPassword.text = null
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

    private fun validatePasswordConfirmation(passwordConfirmation: String): Boolean {
        return when {
            passwordConfirmation.isEmpty() -> {
                editTextLayoutConfirmationPassword.error =
                    getString(R.string.password_cant_be_blank)
                false
            }
            passwordConfirmation != editTextNewPassword.text.toString() -> {
                editTextLayoutConfirmationPassword.error =
                    getString(R.string.description_validate_confirmation_password)
                false
            }
            else -> {
                editTextLayoutConfirmationPassword.error = null
                true
            }
        }
    }

    companion object {
        fun newInstance() = ChangePasswordFragment()
    }
}
