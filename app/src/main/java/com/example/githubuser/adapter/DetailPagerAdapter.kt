package com.example.githubuser.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuser.R
import com.example.githubuser.view.detail.FollowersFragment
import com.example.githubuser.view.detail.FollowingFragment

class DetailPagerAdapter(private val mContext: Context, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    @StringRes
    private val tabTitles = intArrayOf(R.string.followers_gitusers, R.string.following_gitusers)

    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment()
            1 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }
}