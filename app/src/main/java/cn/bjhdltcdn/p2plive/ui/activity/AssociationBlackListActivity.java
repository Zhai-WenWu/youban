//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.os.Bundle;
//import android.support.design.widget.TabLayout;
//import android.view.View;
//import android.widget.TextView;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationBlackListTabFragmentAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.AssociationBannedPostUsersFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.AssociationKickUsersFragment;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
//
//
///**
// * 圈子黑名单
// */
//public class AssociationBlackListActivity extends BaseActivity {
//    private CustomViewPager mViewPager;
//    private TabLayout mTabLayout;
//    private AssociationBlackListTabFragmentAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_black_list);
//        setTitle();
//        init();
//    }
//
//    private void init() {
//        mViewPager = findViewById(R.id.view_pager);
//        mTabLayout = findViewById(R.id.tabs);
//
//        adapter = new AssociationBlackListTabFragmentAdapter(getSupportFragmentManager());
//        AssociationBannedPostUsersFragment associationBannedPostUsersFragment = new AssociationBannedPostUsersFragment();
//        adapter.addFragment(associationBannedPostUsersFragment, "禁言的用户");
//        AssociationKickUsersFragment associationKickUsersFragment = new AssociationKickUsersFragment();
//        adapter.addFragment(associationKickUsersFragment, "踢出的用户");
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
//                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
//                textView.setTextColor(getResources().getColor(R.color.color_333333));
//                textView.setBackgroundColor(getResources().getColor(R.color.color_ffee00));
//                mViewPager.setCurrentItem(tab.getPosition());
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                TextView textView = tab.getCustomView().findViewById(R.id.tv_tab);
//                textView.setTextColor(getResources().getColor(R.color.color_999999));
//                textView.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });
//    }
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("圈子黑名单");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mViewPager != null) {
//            mViewPager = null;
//        }
//        if (mTabLayout != null) {
//            mTabLayout = null;
//        }
//    }
//}
