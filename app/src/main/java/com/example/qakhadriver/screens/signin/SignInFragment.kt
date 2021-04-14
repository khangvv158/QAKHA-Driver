package com.example.qakhadriver.screens.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.signup.SignUpFragment
import com.example.qakhadriver.utils.addFragmentBackStack
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment(), SignInContract.View {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewSignUp.setOnClickListener {
            addFragmentBackStack(SignUpFragment.newInstance(), R.id.containerViewAuthentication)
        }
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}
