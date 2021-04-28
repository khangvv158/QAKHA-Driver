package com.example.qakhadriver.screens.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.Driver
import com.example.qakhadriver.data.model.Feedback
import com.example.qakhadriver.data.repository.FeedbackRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.feedback.adapter.CommentAdapter
import com.example.qakhadriver.utils.makeText
import kotlinx.android.synthetic.main.fragment_feedback.*

class FeedbackFragment : Fragment(), FeedbackContract.View {

    private val presenter by lazy {
        FeedbackPresenter(
            FeedbackRepositoryImpl.getInstance(),
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext()))
        )
    }
    private val commentAdapter by lazy {
        CommentAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        initData()
        handleEvents()
    }

    override fun onGetFeedbackSuccess(feedback: Feedback) {
        ratingBar.rating = feedback.point
        textViewRate.text = feedback.point.toString()
        commentAdapter.updateData(feedback.comments)
        swipeLayout.isRefreshing = false
    }

    override fun onError(message: String) {
        makeText(message)
        swipeLayout.isRefreshing = false
    }

    private fun initData() {
        commentRecyclerView.adapter = commentAdapter
        arguments?.getParcelable<Driver>(BUNDLE_DRIVER)?.let {
            initViews(it)
            presenter.getFeedback(it.idDriver)
        }
    }

    private fun initViews(driver: Driver) {
        Glide.with(requireContext()).load(driver.image.imageUrl).into(imageViewAvatarDriver)
        textViewNameDriver.text = driver.name
        textViewLicensePlateDriver.text = driver.licensePlate
        handleSwipe(driver)
    }

    private fun handleSwipe(driver: Driver) {
        swipeLayout.setOnRefreshListener {
            presenter.getFeedback(driver.idDriver)
        }
    }

    private fun handleEvents() {
        imageViewBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {

        private const val BUNDLE_DRIVER = "BUNDLE_DRIVER"

        fun newInstance(driver: Driver) = FeedbackFragment().apply {
            arguments = bundleOf(BUNDLE_DRIVER to driver)
        }
    }
}
