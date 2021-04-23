package com.example.qakhadriver.screens.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.signup.activate.ActivateFragment
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.emailEditText
import kotlinx.android.synthetic.main.fragment_sign_up.emailTextInputLayout
import kotlinx.android.synthetic.main.fragment_sign_up.passwordEditText
import kotlinx.android.synthetic.main.fragment_sign_up.passwordTextInputLayout

class SignUpFragment : Fragment(), SignUpContract.View {

    private val presenter by lazy {
        SignUpPresenter(
            SignRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
            )
        )
    }
    private var emailIsExist = false
    private var phoneNumberIsExist = false
    private var idCardIsExist = false
    private var licensePlateIsExist = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        handleEvents()
    }

    override fun onSignUpSuccess() {
        clearEditText()
        loadingProgress.gone()
        addFragmentSlideAnim(ActivateFragment.newInstance(), R.id.containerViewAuthentication)
    }

    override fun onSignUpFailure(message: String) {
        loadingProgress.gone()
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onCheckEmailIsExistSuccess() {
        emailIsExist = true
    }

    override fun onCheckEmailIsExistFailure() {
        emailTextInputLayout.error = getString(R.string.email_is_already_exists_in_database)
        emailIsExist = false
    }

    override fun onCheckPhoneNumberIsExistSuccess() {
        phoneNumberIsExist = true
    }

    override fun onCheckPhoneNumberIsExistFailure() {
        phoneNumberTextInputLayout.error =
            getString(R.string.Phone_umber_is_already_exists_in_database)
        phoneNumberIsExist = false
    }

    override fun onCheckIdCardIsExistSuccess() {
        idCardIsExist = true
    }

    override fun onCheckIdCardIsExistFailure() {
        identityCardTextInputLayout.error =
            getString(R.string.id_card_is_already_exists_in_database)
        idCardIsExist = false
    }

    override fun onCheckLicensePlateSuccess() {
        licensePlateIsExist = true
    }

    override fun onCheckLicensePlateFailure() {
        licensePlateTextInputLayout.error =
            getString(R.string.license_plate_is_already_exists_in_database)
        licensePlateIsExist = false
    }

    override fun onError(message: String) {
        loadingProgress.gone()
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        signUpButton.setOnClickListener {
            handleSendSignUp()
        }
        handleEventsKeyBoard()
        handleEventsAfterTextChanged()
        activateTextView.setOnClickListener {
            addFragmentSlideAnim(ActivateFragment.newInstance(), R.id.containerViewAuthentication)
        }
    }

    private fun handleSendSignUp() {
        val checkEmail = validateEmail(emailEditText.text.toString())
        val checkPassword = validatePassword(passwordEditText.text.toString())
        val checkPasswordConfirmation =
            validatePasswordConfirmation(confirmPasswordEditText.text.toString())
        val checkUserName = validateUsername(nameEditText.text.toString())
        val checkPhoneNumber = validatePhoneNumber(phoneNumberEditText.text.toString())
        val checkIdCard = validateIdentityCard(identityCardEditText.text.toString())
        val checkLicensePlate = validateLicensePlate(licensePlateCardEditText.text.toString())
        presenter.checkPhoneNumberIsExist(phoneNumberEditText.text.toString())
        presenter.checkLicensePlate(licensePlateCardEditText.text.toString())
        presenter.checkIdCardIsExist(identityCardEditText.text.toString())
        if (!emailEditText.text.isNullOrBlank()) {
            presenter.checkEmailIsExist(emailEditText.text.toString())
        }
        if (checkEmail &&
            checkPassword &&
            checkPasswordConfirmation &&
            checkUserName &&
            checkPhoneNumber &&
            checkIdCard &&
            checkLicensePlate &&
            emailIsExist &&
            phoneNumberIsExist &&
            idCardIsExist &&
            licensePlateIsExist
        ) {
            hideKeyboard()
            presenter.signUp(
                emailEditText.text.toString(),
                passwordEditText.text.toString(),
                confirmPasswordEditText.text.toString(),
                phoneNumberEditText.text.toString(),
                nameEditText.text.toString(),
                identityCardEditText.text.toString(),
                licensePlateCardEditText.text.toString()
            )
            emailIsExist = false
            phoneNumberIsExist = false
            idCardIsExist = false
            licensePlateIsExist = false
            loadingProgress.show()
        }
    }

    private fun handleEventsKeyBoard() {
        emailEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                if (validateEmail(view.text.toString())) {
                    presenter.checkEmailIsExist(view.text.toString())
                    true
                }
            }
            false
        }
        phoneNumberEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                if (validatePhoneNumber(view.text.toString())) {
                    presenter.checkPhoneNumberIsExist(view.text.toString())
                    true
                }
            }
            false
        }
        identityCardEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                if (validateIdentityCard(view.text.toString())) {
                    presenter.checkIdCardIsExist(view.text.toString())
                    true
                }
            }
            false
        }
        licensePlateCardEditText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                if (validateLicensePlate(view.text.toString())) {
                    presenter.checkLicensePlate(view.text.toString())
                    true
                }
            }
            false
        }
    }

    private fun handleEventsAfterTextChanged() {
        emailEditText.afterTextChanged {
            if (validateEmail(it)) {
                presenter.checkEmailIsExist(it)
            }
        }
        phoneNumberEditText.afterTextChanged {
            if (validatePhoneNumber(it)) {
                presenter.checkPhoneNumberIsExist(it)
            }
        }
        identityCardEditText.afterTextChanged {
            if (validateIdentityCard(it)) {
                presenter.checkIdCardIsExist(it)
            }
        }
        licensePlateCardEditText.afterTextChanged {
            if (validateLicensePlate(it)) {
                presenter.checkLicensePlate(it)
            }
        }
    }

    private fun clearEditText() {
        emailEditText.text = null
        emailTextInputLayout.error = null
        passwordEditText.text = null
        passwordTextInputLayout.error = null
        confirmPasswordEditText.text = null
        confirmPasswordTextInputLayout.error = null
        nameEditText.text = null
        nameTextInputLayout.error = null
        phoneNumberEditText.text = null
        phoneNumberTextInputLayout.error = null
        identityCardEditText.text = null
        identityCardTextInputLayout.error = null
        licensePlateCardEditText.text = null
        licensePlateTextInputLayout.error = null
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            emailTextInputLayout.error = getString(R.string.email_cant_be_blank)
            false
        } else if (!email.validWithRegex(RegexKey.VALID_EMAIL_REGEX)) {
            emailTextInputLayout.error = getString(R.string.email_is_invalid)
            false
        } else {
            emailTextInputLayout.error = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.isEmpty()) {
            passwordTextInputLayout.error = getString(R.string.password_cant_be_blank)
            false
        } else if (!password.validWithRegex(RegexKey.VALID_PASSWORD_REGEX)) {
            passwordTextInputLayout.error =
                getString(R.string.description_validate_password)
            false
        } else {
            passwordTextInputLayout.error = null
            true
        }
    }

    private fun validatePasswordConfirmation(passwordConfirmation: String): Boolean {
        return when {
            passwordConfirmation.isEmpty() -> {
                confirmPasswordTextInputLayout.error = getString(R.string.password_cant_be_blank)
                false
            }
            passwordConfirmation != passwordEditText.text.toString() -> {
                confirmPasswordTextInputLayout.error =
                    getString(R.string.description_validate_confirmation_password)
                false
            }
            else -> {
                confirmPasswordTextInputLayout.error = null
                true
            }
        }
    }

    private fun validateUsername(name: String): Boolean {
        return if (name.isEmpty()) {
            nameTextInputLayout.error = getString(R.string.name_cant_be_blank)
            false
        } else {
            nameTextInputLayout.error = null
            true
        }
    }

    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return if (phoneNumber.isEmpty()) {
            phoneNumberTextInputLayout.error =
                getString(R.string.phone_number_is_too_short)
            false
        } else if (!phoneNumber.validWithRegex(RegexKey.VALID_PHONE_REGEX)) {
            phoneNumberTextInputLayout.error = getString(R.string.phone_number_is_invalid)
            false
        } else {
            phoneNumberTextInputLayout.error = null
            true
        }
    }

    private fun validateIdentityCard(identityCard: String): Boolean {
        return if (identityCard.isEmpty()) {
            identityCardTextInputLayout.error = getString(R.string.identity_card_cant_be_blank)
            false
        } else if (!identityCard.validWithRegex(RegexKey.VALID_ID_CARD_REGEX)) {
            identityCardTextInputLayout.error = getString(R.string.identity_card_is_invalid)
            false
        } else {
            identityCardTextInputLayout.error = null
            true
        }
    }

    private fun validateLicensePlate(licensePlate: String): Boolean {
        return if (licensePlate.isEmpty()) {
            licensePlateTextInputLayout.error = getString(R.string.license_plate_cant_be_blank)
            false
        } else if (!licensePlate.validWithRegex(RegexKey.VALID_LICENSE_PLATE_REGEX)) {
            licensePlateTextInputLayout.error = getString(R.string.license_plate_is_invalid)
            false
        } else {
            licensePlateTextInputLayout.error = null
            true
        }
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}
