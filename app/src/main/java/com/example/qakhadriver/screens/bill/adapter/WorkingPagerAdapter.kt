package com.example.qakhadriver.screens.bill.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.qakhadriver.R
import com.example.qakhadriver.screens.bill.tabs.doing.DoingFragment
import com.example.qakhadriver.screens.bill.tabs.done.DoneFragment
import com.example.qakhadriver.screens.bill.tabs.freepick.FreePickFragment
import com.example.qakhadriver.utils.Constants

class WorkingPagerAdapter(
    manager: FragmentManager,
    private val context: Context,
    private val fragments: MutableList<Fragment> = arrayListOf()
) : FragmentPagerAdapter(
    manager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {
    override fun getCount() = fragments.size

    override fun getItem(position: Int) = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return when (fragments[position]::class.java.simpleName) {
            FreePickFragment::class.java.simpleName -> context.getString(R.string.free_pick)
            DoingFragment::class.java.simpleName -> context.getString(R.string.doing)
            DoneFragment::class.java.simpleName -> context.getString(R.string.done)
            else -> Constants.SPACE_STRING
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
    }
}
