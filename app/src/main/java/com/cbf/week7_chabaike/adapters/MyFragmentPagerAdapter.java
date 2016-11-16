package com.cbf.week7_chabaike.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> data;
    private String[] titles;
    public MyFragmentPagerAdapter(FragmentManager supportFragmentManager, List<Fragment> list, String[] titleArray) {
        super(supportFragmentManager);
        this.data=list;
        this.titles = titleArray;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
