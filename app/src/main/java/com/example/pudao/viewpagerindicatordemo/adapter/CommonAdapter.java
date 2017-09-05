package com.example.pudao.viewpagerindicatordemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.pudao.viewpagerindicatordemo.ui.CommonFragment;

import java.util.List;

/**
 * Created by pucheng on 2017/9/5.
 */

public class CommonAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public CommonAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CommonFragment fragment = (CommonFragment) fragments.get(position);
        return fragment.getTitle();
    }
}
