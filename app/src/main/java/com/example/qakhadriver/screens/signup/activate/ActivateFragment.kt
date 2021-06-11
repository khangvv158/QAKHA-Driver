package com.example.qakhadriver.screens.signup.activate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.utils.gone
import com.example.qakhadriver.utils.makeText
import com.example.qakhadriver.utils.show
import kotlinx.android.synthetic.main.activate_fragment.*
import org.greenrobot.eventbus.EventBus

class ActivateFragment : Fragment(), ActivateContract.View {

    private val presenter by lazy {
        ActivatePresenter(
            SignRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }
    private var emailRequest: EmailRequest? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activate_fragment, container, false)
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

    override fun onActivateAccountSuccess() {
        Toast.makeText(
            requireContext(),
            getString(R.string.content_activate_success),
            Toast.LENGTH_LONG
        ).show()
        progressBar.gone()
        EventBus.getDefault().post(Event(EVENT_ACTIVATE_SUCCESS, EVENT_ACTIVATE_SUCCESS))
        parentFragmentManager.popBackStack()
    }

    override fun onActivateAccountFailure() {
        makeText(getString(R.string.content_activate_failure))
        progressBar.gone()
    }

    override fun onResendCodeActivateSuccess() {
        progressBar.gone()
        makeText(getString(R.string.generating_code))
    }

    override fun onResendCodeActivateFailure() {
        progressBar.gone()
    }

    override fun onError(message: String) {
        makeText(message)
    }

    private fun initViews() {
        arguments?.getParcelable<EmailRequest>(ARGUMENT_EMAIL)?.let {
            emailRequest = it
        }
        presenter.setView(this)
    }

    private fun handleEvents() {
        activateButton.setOnClickListener {
            presenter.activateAccount(editTextActivateCode.text.toString().trim())
            progressBar.show()
        }
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        textViewGeneratingCode.setOnClickListener {
            emailRequest?.let { email ->
                presenter.resendCodeActivate(email)
                progressBar.show()
            }
        }
    }

    companion object {

        private const val ARGUMENT_EMAIL = "ARGUMENT_EMAIL"
        const val EVENT_ACTIVATE_SUCCESS = "EVENT_ACTIVATE_SUCCESS"

        fun newInstance(emailRequest: EmailRequest?) = ActivateFragment().apply {
            arguments = bundleOf(ARGUMENT_EMAIL to emailRequest)
        }
    }
}
