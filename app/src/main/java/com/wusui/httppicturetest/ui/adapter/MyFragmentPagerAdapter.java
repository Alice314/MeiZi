package com.wusui.httppicturetest.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wusui.httppicturetest.ui.fragment.CommunityFragment;
import com.wusui.httppicturetest.ui.fragment.ExploreFragment;
import com.wusui.httppicturetest.ui.fragment.FavoritesFragment;

/**
 * Created by fg on 2016/3/26.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 3;
    private String[] titles = new String[]{"COMMUNITY", "EXPLORE", "FAVORITES"};
    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        // TODO 你这里怎么三个都一样的Fragment
        switch (position) {
            case 0: return new CommunityFragment();
            case 1: return new ExploreFragment();
            case 2: return new FavoritesFragment();
            default: return new CommunityFragment();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
