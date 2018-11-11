package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.umeng.analytics.MobclickAgent;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.LoginByThirdPartyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.LoginResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.LoginPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.WelcomePagerAdapter;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * 启动页面
 */
public class LaunchActivity extends AppCompatActivity implements BaseView {

    private Handler mHandler;
    private LoginPresenter loginPresenter;
    private CustomViewPager customViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_layout);


        Uri uri = getIntent().getData();
        if (uri != null) {// H5启动
            if (!uri.getScheme().startsWith("youban") || !uri.getHost().equals("cn.bjhdltcdn.p2plive") || !uri.getPath().contains("launchApp")) {
                // 非指定协议启动，则关闭
                finish();
                return;
            }
        }

        loginPresenter = new LoginPresenter(this);
        if (mHandler == null) {
            mHandler = new Handler();
        }
        final boolean isFirst = SafeSharePreferenceUtils.getBoolean(Constants.Fields.IS_FIRST, true);
        final int loginType = SafeSharePreferenceUtils.getInt(Constants.Fields.LOGIN_TYPE, 0);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginType == 0 || isFirst) {
                    if (isFirst) {
                        customViewPager = findViewById(R.id.welcome_view_page);
                        customViewPager.setVisibility(View.VISIBLE);
                        WelcomePagerAdapter adapter = new WelcomePagerAdapter(4);
                        customViewPager.setOffscreenPageLimit(3);
                        customViewPager.setAdapter(adapter);
                        customViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                if (position == 3) {
                                    customViewPager.getChildAt(3).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            SafeSharePreferenceUtils.saveBoolean(Constants.Fields.IS_FIRST, false);
                                            startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                                }
                            }
                        });
                    } else {
                        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    if (loginType == 1) {
                        String phoneStr = SafeSharePreferenceUtils.getString(Constants.Fields.PHONE_NUMBER, "");
                        String pwdStr = SafeSharePreferenceUtils.getString(Constants.Fields.PASS_WORD, "");
                        loginPresenter.loginByPhone(phoneStr, pwdStr, 1);
                    } else {
                        GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), new GreenDaoUtils.ExecuteCallBack() {
                            @Override
                            public void callBack(Object object) {
                                BaseUser baseUser = (BaseUser) object;
                                String thirdId = SafeSharePreferenceUtils.getString(Constants.Fields.THIRD_ID, "");
                                loginPresenter.loginByThirdParty(thirdId, baseUser.getLocation(), baseUser.getSex(), baseUser.getUserIcon(), "", baseUser.getNickName(), loginType, thirdId);
                            }
                        });
                    }
                }
            }
        }, 2000);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        mHandler = null;
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text + "，登录失败！");

            finish();

            return;
        }

        switch (apiName) {
            case InterfaceUrl.URL_LOGINBYPHONE:
                if (object instanceof LoginResponse) {
                    final LoginResponse response = (LoginResponse) object;
                    if (response.getCode() == 200) {
                        User user = response.getUser();
                        if (user != null) {
                            //兴趣爱好必选
                            if (TextUtils.isEmpty(user.getBirthday()) || user.getOccupationInfo() == null ||
                                    user.getSchoolInfo() == null) {
                                Intent intent = new Intent(LaunchActivity.this, CompleteInfoActivity.class);
                                intent.putExtra(Constants.Fields.USER_ID, user.getUserId());
                                intent.putExtra(Constants.Fields.USER_ICON, user.getUserIcon());
                                intent.putExtra(Constants.Fields.NICK_NAME, user.getNickName());
                                intent.putExtra(Constants.Fields.SEX, user.getSex());
                                intent.putExtra(Constants.Fields.BIRTHDAY, user.getBirthday());
                                intent.putExtra(Constants.Fields.OCCUPATION_INFO, user.getOccupationInfo());
                                intent.putExtra(Constants.Fields.SCHOOL_INFO, user.getSchoolInfo());
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                            }

                            //1手机号，2 QQ 3 微信
                            MobclickAgent.onProfileSignIn("Phone", user.getUserId() + "");

                            finish();
                        }
                    } else {
                        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                        finish();
                    }
                }

                break;
            case InterfaceUrl.URL_LOGINBYTHIRDPARTY:
                if (object instanceof LoginByThirdPartyResponse) {
                    final LoginByThirdPartyResponse response = (LoginByThirdPartyResponse) object;
                    if (response.getCode() == 200) {
                        int loginType = SafeSharePreferenceUtils.getInt(Constants.Fields.LOGIN_TYPE, 0);
                        //兴趣爱好必选
                        User user = response.getUser();
                        if (user != null) {
                            if (TextUtils.isEmpty(user.getPhoneNumber())) {
                                Intent intent = new Intent(LaunchActivity.this, BindPhoneNumberActivity.class);
                                intent.putExtra(Constants.Fields.EXTRA, user);
                                startActivity(intent);
                            } else if (TextUtils.isEmpty(user.getBirthday()) || user.getOccupationInfo() == null ||
                                    user.getSchoolInfo() == null) {
                                Intent intent = new Intent(LaunchActivity.this, CompleteInfoActivity.class);
                                intent.putExtra(Constants.Fields.USER_ID, user.getUserId());
                                intent.putExtra(Constants.Fields.USER_ICON, user.getUserIcon());
                                intent.putExtra(Constants.Fields.NICK_NAME, user.getNickName());
                                intent.putExtra(Constants.Fields.SEX, user.getSex());
                                intent.putExtra(Constants.Fields.BIRTHDAY, user.getBirthday());
                                intent.putExtra(Constants.Fields.OCCUPATION_INFO, user.getOccupationInfo());
                                intent.putExtra(Constants.Fields.SCHOOL_INFO, user.getSchoolInfo());
                                startActivity(intent);
                            } else {
                                startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                            }
                            //1手机号，2 QQ 3 微信
                            MobclickAgent.onProfileSignIn(loginType == 2 ? "QQ" : "WX", user.getUserId() + "");

                            finish();
                        }
                    } else {
                        startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
