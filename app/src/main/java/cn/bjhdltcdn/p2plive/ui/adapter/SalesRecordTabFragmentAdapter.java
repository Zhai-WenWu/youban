package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;

/**
 * Created by huwenhua on 2016/6/1.
 */
public class SalesRecordTabFragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<Fragment>();
    private final List<String> mFragmentTitles = new ArrayList<String>();

    public String getFragmentTitles(int position) {
        return mFragmentTitles.get(position);
    }

    public SalesRecordTabFragmentAdapter(FragmentManager fm) {
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
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles != null && mFragmentTitles.size() > 0 ? mFragmentTitles.get(position) : null;
    }

    public View getTabView(final int position) {
        View view = View.inflate(App.getInstance(), R.layout.sales_record_tab_item_layout, null);
        TextView textView = view.findViewById(R.id.text_view);
        textView.setText(getFragmentTitles(position));
        switch (position) {
            case 0:
                textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                textView.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_ffee00));
                break;

            default:
                textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                textView.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_ffffff));
                break;

        }
        return view;
    }
}
