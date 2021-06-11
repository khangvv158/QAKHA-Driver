package com.example.qakhadriver.screens.signup.imagesignup

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Event
import com.example.qakhadriver.data.repository.CloudRepositoryImpl
import com.example.qakhadriver.data.repository.SignRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.data.source.remote.schema.request.EmailRequest
import com.example.qakhadriver.data.source.remote.schema.request.RegisterRequest
import com.example.qakhadriver.screens.signup.activate.ActivateFragment
import com.example.qakhadriver.utils.*
import kotlinx.android.synthetic.main.fragment_image_sign_up.*
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException
import java.lang.Exception

class ImageSignUpFragment : Fragment(), ImageSignUpContract.View {

    private val presenter by lazy {
        ImageSignUpPresenter(
            SignRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext())),
            CloudRepositoryImpl.getInstance(requireContext())
        )
    }
    private var registerRequest: RegisterRequest? = null
    private var filePath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleEvents()
    }

    override fun onSignUpSuccess() {
        progressBar.gone()
        replaceFragment(
            ActivateFragment.newInstance(registerRequest?.email?.let { EmailRequest(it) }),
            R.id.containerViewImageSignUp
        )
    }

    override fun onSignUpFailure(throwable: Throwable) {
        progressBar.gone()
        makeText(throwable.localizedMessage)
        if ((throwable as HttpException).code() == 500) {
            parentFragmentManager.popBackStack()
        }
    }

    override fun uploadFileToCloudSuccess(url: String) {
        registerRequest?.image = url
        progressBar.gone()
    }

    override fun onError(message: String) {
        makeText(message)
        progressBar.gone()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        filePath = data?.data?.let { getRealPathFromUri(it) }
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            try {
                Glide.with(requireContext()).load(data?.data).centerCrop().into(avatarImageView)
                filePath?.let {
                    presenter.uploadFileToCloud(it)
                    progressBar.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                accessTheGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initViews() {
        arguments?.getParcelable<RegisterRequest>(BUNDLE_REGISTER).let {
            registerRequest = it
        }
        presenter.setView(this)
    }

    private fun handleEvents() {
        selectImageView.setOnSafeClickListener {
            requestPermission()
        }
        signUpButton.setOnSafeClickListener {
            if (!registerRequest?.image.isNullOrEmpty()) {
                registerRequest?.let { registerRequest -> presenter.signUp(registerRequest) }
                progressBar.show()
            }
        }
        imageViewBack.setOnClickListener {
            EventBus.getDefault().post(Event(EVENT_BUS_FRESH, EVENT_BUS_FRESH))
            parentFragmentManager.popBackStack()
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            accessTheGallery()
        } else {
            ActivityCompat.requestPermissions(
                activity as Activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }
    }

    private fun accessTheGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun getRealPathFromUri(imageUri: Uri): String? {
        val cursor: Cursor? =
            requireContext().contentResolver?.query(imageUri, null, null, null, null)
        return if (cursor == null) {
            imageUri.path
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    companion object {

        private const val BUNDLE_REGISTER = "BUNDLE_REGISTER"
        private const val PERMISSION_CODE = 1
        private const val PICK_IMAGE = 1
        const val EVENT_BUS_FRESH = "EVENT_BUS_FRESH"

        fun newInstance(registerRequest: RegisterRequest) = ImageSignUpFragment().apply {
            arguments = bundleOf(BUNDLE_REGISTER to registerRequest)
        }
    }
}
