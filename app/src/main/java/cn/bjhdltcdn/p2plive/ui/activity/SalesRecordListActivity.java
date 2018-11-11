package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.ui.adapter.SalesRecordTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopDetailTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.SalesRecordRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * 销售记录界面
 */
public class SalesRecordListActivity extends BaseActivity{
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private SalesRecordTabFragmentAdapter adapter;
    private SalesRecordRecycleFragment salesRecordRecycleFragment1,salesRecordRecycleFragment2;
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_record_list_layout);
        Intent intent = getIntent();
        storeId= intent.getLongExtra("storeId",0);
        setTitle();
        getView();
    }

    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitle(R.string.title_sales_record);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        fragment.setRightViewColor(R.color.color_ffb700);
    }

    public void getView() {

        mViewPager = (CustomViewPager) findViewById(R.id.sales_record_list_view_page);
        mViewPager.setIsCanScroll(true);
        mTabLayout = (TabLayout)findViewById(R.id.tabs);

        adapter = new SalesRecordTabFragmentAdapter(getSupportFragmentManager());

        salesRecordRecycleFragment1 = new SalesRecordRecycleFragment();
        salesRecordRecycleFragment1.setType(1);
        salesRecordRecycleFragment1.setStoreId(storeId);
        adapter.addFragment(salesRecordRecycleFragment1, "本周");

        salesRecordRecycleFragment2 = new SalesRecordRecycleFragment();
        salesRecordRecycleFragment2.setType(0);
        salesRecordRecycleFragment2.setStoreId(storeId);
        adapter.addFragment(salesRecordRecycleFragment2, "全部");
        mViewPager.setOffscreenPageLimit(2);

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));
                textView.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_ffee00));
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));
                textView.setBackgroundColor(App.getInstance().getResources().getColor(R.color.color_ffffff));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                KLog.d("onTabReselected()");
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
