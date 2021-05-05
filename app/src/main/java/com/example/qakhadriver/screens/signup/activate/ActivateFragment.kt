package com.example.qakhadriver.screens.signup.activate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.qakhadriver.R
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.utils.makeText
import kotlinx.android.synthetic.main.activate_fragment.*

class ActivateFragment : Fragment(), ActivateContract.View {

    private val presenter by lazy {
        ActivatePresenter(
            SignRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }

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
        parentFragmentManager.popBackStack()
    }

    override fun onActivateAccountFailure() {
        makeText(getString(R.string.content_activate_failure))
    }

    private fun initViews() {
        presenter.setView(this)
    }

    private fun handleEvents() {
        activateButton.setOnClickListener {
            presenter.activateAccount(editTextActivateCode.text.toString().trim())
        }
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        fun newInstance() = ActivateFragment()
    }
}
