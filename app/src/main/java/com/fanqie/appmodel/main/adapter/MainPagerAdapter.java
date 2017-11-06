package com.fanqie.appmodel.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fanqie.appmodel.main.fragment.FifthFragment;
import com.fanqie.appmodel.main.fragment.FirstFragment;
import com.fanqie.appmodel.main.fragment.FourthFragment;
import com.fanqie.appmodel.main.fragment.SecondFragment;
import com.fanqie.appmodel.main.fragment.ThirdFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/6.
 * <p>
 * 控制首页 5 个 fragment
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        fragments.add(new FirstFragment());
        fragments.add(new SecondFragment());
        fragments.add(new ThirdFragment());
        fragments.add(new FourthFragment());
        fragments.add(new FifthFragment());

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
