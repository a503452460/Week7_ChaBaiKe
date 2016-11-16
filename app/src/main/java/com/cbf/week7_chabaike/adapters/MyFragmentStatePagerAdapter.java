package com.cbf.week7_chabaike.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class MyFragmentStatePagerAdapter extends FragmentStatePagerAdapter{
    private  List<Fragment> data;
    public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> data) {
        super(fm);
        this.data =data;
    }

    @Override
    public Fragment getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data!=null?data.size():0;
    }
}
