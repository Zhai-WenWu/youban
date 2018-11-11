package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.ui.adapter.UserDetailTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailAgentGoodsFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailGoodsFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.BrandBusinessDetailEvaluateFragment;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * Created by Administrator on 2018/9/13.
 */

public class BrandBusinessDetailActiviy extends BaseActivity {

    private TabLayout tab_jing;
    private TabLayout tab_dong;
    private ScrollView scroll_view;
    private RelativeLayout rv_title;
    private float alpha;
    private int[] location1;
    private int[] location2;
    private CustomViewPager view_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activiy_brand_business_detail);
        initView();
        setTitle();
        initData();
    }

    private void initView() {
        tab_jing = findViewById(R.id.tab_jing);
        tab_dong = findViewById(R.id.tab_dong);
        scroll_view = findViewById(R.id.scroll_view);
        rv_title = findViewById(R.id.rv_title);
        view_page = findViewById(R.id.view_page);
    }

    private void initData() {
        initTab();
    }

    private void initTab() {
        UserDetailTabFragmentAdapter adapter = new UserDetailTabFragmentAdapter(getSupportFragmentManager());
        BrandBusinessDetailGoodsFragment brandBusinessDetailGoodsFragment = new BrandBusinessDetailGoodsFragment();
        adapter.addFragment(brandBusinessDetailGoodsFragment, "商品");
        BrandBusinessDetailEvaluateFragment brandBusinessDetailEvaluateFragment = new BrandBusinessDetailEvaluateFragment();
        adapter.addFragment(brandBusinessDetailEvaluateFragment, "评价");
        BrandBusinessDetailAgentGoodsFragment brandBusinessDetailAgentGoodsFragment = new BrandBusinessDetailAgentGoodsFragment();
        adapter.addFragment(brandBusinessDetailAgentGoodsFragment, "代理商品 ");
        view_page.setAdapter(adapter);
        tab_jing.setupWithViewPager(view_page);
        tab_dong.setupWithViewPager(view_page);
        for (int i = 0; i < tab_dong.getTabCount(); i++) {
            TabLayout.Tab tab = tab_dong.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }

        for (int i = 0; i < tab_jing.getTabCount(); i++) {
            TabLayout.Tab tab = tab_jing.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }

        tab_jing.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                switch (tab.getPosition()) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab_dong.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                switch (tab.getPosition()) {
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTitle() {
        alpha = (float) 0.0;
        location1 = new int[2];
        location2 = new int[2];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scroll_view.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int j, int i1, int i2, int i3) {
                    alpha = (float) i1 / 480;
                    if (alpha > 0 && alpha < 1) {
                        rv_title.setAlpha((float) alpha);
                    }
                    if (alpha == 0) {
                        rv_title.setAlpha(0);
                    }
                    if (alpha >= 1) {
                        rv_title.setAlpha(1);
                    }

                    tab_dong.getLocationOnScreen(location1);
                    tab_jing.getLocationOnScreen(location2);

                    if (location1[1] <= location2[1]) {
                        tab_jing.setVisibility(View.VISIBLE);
                    } else {
                        tab_jing.setVisibility(View.GONE);
                    }

                }
            });
        }
    }
}
