package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.ui.activity.GroupCreateActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * Created by Hu_PC on 2017/11/8.
 * 消息
 */

public class MessageFragment extends BaseFragment{
    private View rootView;
    private AppCompatActivity mActivity;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private AssociationTabFragmentAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle();

        initView();


    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getChildFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("消息");
        titleFragment.setLeftViewVisibility(View.INVISIBLE);
        titleFragment.setRightView(R.mipmap.create_group_icon, new ToolBarFragment.ViewOnclick() {
            @Override
            public void onClick() {

                startActivity(new Intent(getActivity(), GroupCreateActivity.class));

            }
        });
    }


    private void initView(){
        mViewPager = (CustomViewPager) rootView.findViewById(R.id.message_view_page);
        mViewPager.setIsCanScroll(true);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);

        adapter = new AssociationTabFragmentAdapter(mActivity.getSupportFragmentManager());


        MessageInfoFragment messageInfoFragment = new MessageInfoFragment();
        adapter.addFragment(messageInfoFragment, "信息");

        MessageAddressBookFragment nessageAddressBookFragment = new MessageAddressBookFragment();
        adapter.addFragment(nessageAddressBookFragment, "通讯录");

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

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));

                View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.color_ffffff));

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                KLog.d("onTabReselected()");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
