package com.example.qakhadriver.utils

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject

object EditTextObservable {

    fun fromView(view: EditText): Observable<String> {
        val subject = PublishSubject.create<String>()
        view.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                subject.onNext(view.text.toString())
            }

            override fun afterTextChanged(s: Editable?) = Unit
        })
        view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.clearFocus()
                return@setOnEditorActionListener false
            }
            false
        }
        return subject
    }
}
