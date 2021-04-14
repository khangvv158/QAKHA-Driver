package com.example.qakhadriver.screens.container

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.container.adapter.ContainerPagerAdapter
import com.example.qakhadriver.screens.earnings.EarningsFragment
import com.example.qakhadriver.screens.bill.BillFragment
import com.example.qakhadriver.screens.me.MeFragment
import com.example.qakhadriver.utils.TypeMenu
import kotlinx.android.synthetic.main.fragment_container.*

class ContainerFragment : Fragment() {

    private val adapter: ContainerPagerAdapter by lazy {
        ContainerPagerAdapter(childFragmentManager)
    }
    private val billFragment = BillFragment.newInstance()
    private val earningsFragment = EarningsFragment.newInstance()
    private val meFragment = MeFragment.newInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initViews()
        handleEvents()
    }

    private fun initData() {
        adapter.apply {
            addFragment(billFragment)
            addFragment(earningsFragment)
            addFragment(meFragment)
        }
    }

    private fun initViews() {
        containerViewPager.apply {
            adapter = this@ContainerFragment.adapter
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
        }
    }

    private fun handleEvents() {
        containerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
            ) = Unit

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId =
                        bottomNavigationView.menu.getItem(position).itemId
            }

            override fun onPageScrollStateChanged(state: Int) = Unit
        })
        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_menu_nav_bill -> {
                    containerViewPager.currentItem = TypeMenu.BILL.ordinal
                    true
                }
                R.id.item_menu_nav_earnings -> {
                    containerViewPager.currentItem = TypeMenu.EARNINGS.ordinal
                    true
                }
                R.id.item_menu_nav_me -> {
                    containerViewPager.currentItem = TypeMenu.ME.ordinal
                    true
                }
                else -> false
            }
        }
    }

    companion object {

        const val OFF_SCREEN_PAGE_LIMIT = 3

        fun newInstance() = ContainerFragment()
    }
}
