package com.example.qakhadriver.screens.signin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.signup.SignUpFragment
import com.example.qakhadriver.utils.Constants
import com.example.qakhadriver.utils.addFragmentSlideAnim
import com.example.qakhadriver.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(), SignInContract.View {

    private val presenter by lazy {
        SignInPresenter(SignRepositoryImpl.getInstance(
                SharedPrefsImpl.getInstance(requireContext())
        ))
    }
    private var onSignInSuccessListener: OnSignInSuccessListener? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onStop() {
        presenter.onStop()
        onSignInSuccessListener = null
        super.onStop()
    }

    override fun onSignInSuccess() {
        onSignInSuccessListener?.onSignInSuccess()
    }

    override fun onSignInFailure(message: String) {
        emailTextInputLayout.error = Constants.SPACE_STRING
        passwordTextInputLayout.error = message
    }

    override fun onError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onSignInRoleFailure() {
        Toast.makeText(
                context,
                getString(R.string.you_cannot_sign_in_with_this_account),
                Toast.LENGTH_LONG
        ).show()
    }

    fun registerOnSignInSuccessListener(onSignInSuccessListener: OnSignInSuccessListener) {
        this.onSignInSuccessListener = onSignInSuccessListener
    }

    private fun initViews() {
        presenter.setView(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun handleEvents() {
        textViewSignUp.setOnClickListener {
            addFragmentSlideAnim(SignUpFragment.newInstance(), R.id.containerViewAuthentication)
        }
        passwordEditText.setOnTouchListener { _, _ ->
            passwordTextInputLayout.error = null
            passwordTextInputLayout.isErrorEnabled = false
            false
        }
        emailEditText.setOnTouchListener { _, _ ->
            emailTextInputLayout.error = null
            emailTextInputLayout.isErrorEnabled = false
            false
        }
        signInButton.setOnClickListener {
            hideKeyboard()
            presenter.signIn(emailEditText.text.toString(), passwordEditText.text.toString())
        }
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}
