package com.example.qakhadriver.screens.bill.tabs.freepick

import com.example.qakhadriver.data.model.OrderFirebase
import com.example.qakhadriver.data.repository.OrderFirebaseRepository
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class FreePickPresenter(private val orderRepository: OrderFirebaseRepository) : FreePickContract.Presenter {

    private var view: FreePickContract.View? = null

    override fun getOrderFirebase(id: Int) {
        orderRepository.listenerOrder(id, object : ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue(OrderFirebase::class.java)?.let {
                    view?.onGetOrderFirebaseSuccess(it)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) = Unit

            override fun onChildRemoved(snapshot: DataSnapshot) {
                view?.onGetOrderFirebaseRemove()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) = Unit

            override fun onCancelled(error: DatabaseError) {
                view?.onError(error.message)
            }
        })
    }

    override fun onStart() = Unit

    override fun onStop() = Unit

    override fun setView(view: FreePickContract.View?) {
        this.view = view
    }
}
