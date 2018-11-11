package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserAuthStatusResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.RealNameDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.AnonymousChatRoomFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.VideoChatRoomFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * 聊天频道
 * Created by ZHUDI on 2017/11/23.
 */

public class ChatRoomActivity extends BaseActivity implements BaseView {
    private UserPresenter userPresenter;
    private long currentUserId;
    private TabLayout mTabLayout;
    private AnonymousChatRoomFragment anonymousChatRoomFragment;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setTitle();
        init();
        currentUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    private void init() {
        final CustomViewPager mViewPager = findViewById(R.id.vp_chat);
        mViewPager.setIsCanScroll(true);
        mTabLayout = findViewById(R.id.tl_chat);
        AssociationTabFragmentAdapter adapter = new AssociationTabFragmentAdapter(getSupportFragmentManager());

        anonymousChatRoomFragment = new AnonymousChatRoomFragment();
        adapter.addFragment(anonymousChatRoomFragment, "匿名聊天室");

        VideoChatRoomFragment videoChatRoomFragment = new VideoChatRoomFragment();
        adapter.addFragment(videoChatRoomFragment, "视频聊天室");


        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //匿名为0，视频为1
        int type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
            TextView textView = tab.getCustomView().findViewById(R.id.text_view);
            if (i == type) {
                textView.setTextColor(getResources().getColor(R.color.color_333333));
                tab.getCustomView().setBackgroundColor(getResources().getColor(R.color.color_ffee00));
            }else{
                textView.setTextColor(getResources().getColor(R.color.color_999999));
            }
            tab.getCustomView().findViewById(R.id.tab_indicator_view).setVisibility(View.GONE);
        }
        mViewPager.setCurrentItem(type);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_333333));
                tab.getCustomView().setBackgroundColor(getResources().getColor(R.color.color_ffee00));
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textView = tab.getCustomView().findViewById(R.id.text_view);
                textView.setTextColor(getResources().getColor(R.color.color_999999));
                tab.getCustomView().setBackgroundColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (InterfaceUrl.URL_FINDUSERAUTHSTATUS.equals(apiName)) {
            if (object instanceof FindUserAuthStatusResponse) {
                FindUserAuthStatusResponse findUserAuthStatusResponse = (FindUserAuthStatusResponse) object;
                if (findUserAuthStatusResponse.getCode() == 200) {
                    int authStatus = findUserAuthStatusResponse.getAuthStatus();
                    if (authStatus == 2) {
                        startActivity(new Intent(ChatRoomActivity.this, CreateVideoChatRoomActivity.class));
                    } else if (authStatus == 0 || authStatus == 3) {
                        RealNameDialog realNameDialog = new RealNameDialog();
                        realNameDialog.setItemClick(new RealNameDialog.ItemClick() {
                            @Override
                            public void itemClick() {
                                startActivity(new Intent(ChatRoomActivity.this, RealNameCertificationActivity.class));
                            }
                        });
                        realNameDialog.show(getSupportFragmentManager());
                    } else if (authStatus == 1) {
                        Utils.showToastShortTime("实名认证审核中，请等待审核结果");
                    }
                }
            }
        }
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_list_video);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setRightViewColor(R.color.color_ffb700);
        titleFragment.setRightViewTitle("开启聊天室", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                if (Utils.isAllowClick()) {
                    if (mTabLayout.getSelectedTabPosition() == 0) {
                        Intent intent = new Intent(ChatRoomActivity.this, CreateAnonymousChatRoomActivity.class);
                        if (anonymousChatRoomFragment.getChatInfo() != null) {
                            intent.putExtra(Constants.Fields.CHAT_INFO, anonymousChatRoomFragment.getChatInfo());
                        }
                        startActivity(intent);
                    } else {
                        getUserPresenter().findUserAuthStatus(currentUserId);
                    }
                }
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
