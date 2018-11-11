//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.view.View;
//import android.widget.TextView;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.ui.adapter.MyGiftTabFragmentAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.ReceivedGiftFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.SendGiftFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//
///**
// * 我的礼物
// */
//public class MyGiftActivity extends BaseActivity {
//    private CustomViewPager mViewPager;
//    private TabLayout mTabLayout;
//    private MyGiftTabFragmentAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_mygift);
//        setTitle();
//        init();
//    }
//
//    private void init() {
//        mViewPager = findViewById(R.id.pager_gift);
//        mTabLayout = findViewById(R.id.tabs);
//
//        adapter = new MyGiftTabFragmentAdapter(getSupportFragmentManager());
//        ReceivedGiftFragment receivedGiftFragment = new ReceivedGiftFragment();
//        adapter.addFragment(receivedGiftFragment, "收到礼物");
//
//        SendGiftFragment sendGiftFragment = new SendGiftFragment();
//        adapter.addFragment(sendGiftFragment, "送出礼物");
//        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.setIsCanScroll(true);
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
//        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//            TabLayout.Tab tab = mTabLayout.getTabAt(i);
//            View tabView = adapter.getTabView(i);
//            tab.setCustomView(tabView);
//        }
//        mViewPager.setCurrentItem(0);
//        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                TextView textView = tab.getCustomView().findViewById(R.id.text_view);
//                textView.setTextColor(getResources().getColor(R.color.color_333333));
//
//                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
//                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//
//                mViewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                TextView textView = tab.getCustomView().findViewById(R.id.text_view);
//                textView.setTextColor(getResources().getColor(R.color.color_999999));
//
//                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
//                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//
//    }
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("我的礼物");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//    }
//}
