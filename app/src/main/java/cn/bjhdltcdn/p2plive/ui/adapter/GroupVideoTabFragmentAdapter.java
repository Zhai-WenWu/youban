package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenquan on 2016/6/7.
 */
public class GroupVideoTabFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments = new ArrayList<Fragment>(2);
    private List<String> mFragmentTitles = new ArrayList<String>(2);

    public GroupVideoTabFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        if (mFragments != null) {
            mFragments.add(fragment);
        }

        if (mFragmentTitles != null) {
            mFragmentTitles.add(title);
        }

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments != null && mFragments.size() > 0 ? mFragments.get(position) : null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles != null && mFragmentTitles.size() > 0 ? mFragmentTitles.get(position) : null;
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void clearData() {
        if (mFragments != null) {
            mFragments.clear();
        }
        mFragments = null;

        if (mFragmentTitles != null) {
            mFragmentTitles.clear();
        }
        mFragmentTitles = null;
    }

}
