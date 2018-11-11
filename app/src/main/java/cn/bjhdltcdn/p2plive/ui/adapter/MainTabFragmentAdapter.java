package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wenquan on 2016/6/7.
 */
public class MainTabFragmentAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<Fragment>();
    private final List<String> mFragmentTitles = new ArrayList<String>();

    public MainTabFragmentAdapter(FragmentManager fm) {
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

    public View getTabView(final int position) {
//        View view = View.inflate(App.getInstance(), R.layout.main_tab_layout, null);
//        TextView textView = (TextView) view.findViewById(R.id.tab_text_view);
//        textView.setText(getPageTitle(position));
//        ImageView img = (ImageView) view.findViewById(R.id.tab_image_view);
//        switch (position) {
//            case Constants.TAB_INDEX_0:
//                textView.setTextColor(App.getInstance().getResources().getColor(R.color.tabSelectedTextColor));
//                break;
//            case Constants.TAB_INDEX_1:
//                break;
//            case Constants.TAB_INDEX_2:
//                break;
//
//            case Constants.TAB_INDEX_3:
//                break;
//
//            case Constants.TAB_INDEX_4:
//                break;
//        }
//        return view;
        return null;
    }


}
