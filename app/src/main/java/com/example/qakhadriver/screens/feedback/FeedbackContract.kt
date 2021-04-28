package com.example.qakhadriver.screens.feedback

import com.example.qakhadriver.data.model.Feedback
import com.example.qakhadriver.utils.BasePresenter

interface FeedbackContract {

    interface View {

        fun onGetFeedbackSuccess(feedback: Feedback)
        fun onError(message: String)
    }

    interface Presenter : BasePresenter<View> {

        fun getFeedback(idDriver: Int)
    }
}
