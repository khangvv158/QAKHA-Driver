package com.example.qakhadriver.screens.bill.tabs.done

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qakhadriver.R
import com.example.qakhadriver.data.model.OrderDone
import com.example.qakhadriver.data.repository.OrderRepositoryImpl
import com.example.qakhadriver.data.repository.TokenRepositoryImpl
import com.example.qakhadriver.data.source.local.sharedprefs.SharedPrefsImpl
import com.example.qakhadriver.screens.bill.tabs.done.adapter.DoneAdapter
import com.example.qakhadriver.screens.bill.tabs.done.adapter.DoneRecyclerViewOnClickListener
import com.example.qakhadriver.screens.donedetail.DoneDetailFragment
import com.example.qakhadriver.utils.addFragmentSlideAnim
import com.example.qakhadriver.utils.makeText
import kotlinx.android.synthetic.main.fragment_done.*

class DoneFragment : Fragment(), DoneContract.View, DoneRecyclerViewOnClickListener {

    private val presenter by lazy {
        DonePresenter(
            TokenRepositoryImpl.getInstance(SharedPrefsImpl.getInstance(requireContext())),
            OrderRepositoryImpl.getInstance()
        )
    }
    private val doneAdapter by lazy {
        DoneAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_done, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initData()
        handleEvents()
    }

    private fun handleEvents() {
        swipeLayout.setOnRefreshListener {
            presenter.getOrderDone()
        }
    }

    override fun onGetOrderDoneSuccess(ordersDone: MutableList<OrderDone>) {
        doneAdapter.updateData(ordersDone)
        swipeLayout.isRefreshing = false
    }

    override fun onError(message: String) {
        makeText(message)
        swipeLayout.isRefreshing = false
    }

    override fun onItemClickListener(orderDone: OrderDone) {
        parentFragment?.parentFragment?.addFragmentSlideAnim(
            DoneDetailFragment.newInstance(orderDone),
            R.id.containerViewAuthentication
        )
    }

    private fun initViews() {
        presenter.setView(this)
        doneRecyclerView.apply {
            adapter = doneAdapter.apply {
                registerRecyclerViewListener(this@DoneFragment)
            }
        }
    }

    private fun initData() {
        presenter.getOrderDone()
    }

    companion object {
        fun newInstance() = DoneFragment()
    }
}
