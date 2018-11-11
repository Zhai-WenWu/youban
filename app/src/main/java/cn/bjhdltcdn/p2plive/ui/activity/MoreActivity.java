package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.GlideCacheUtil;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by ZHUDI on 2017/12/2.
 */

public class MoreActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_auth, tv_share, tv_accountandsafe, tv_privacy, tv_num_cache, tv_help, tv_aboutus, tv_version, tv_logout;
    private RelativeLayout rela_cache_clean, rela_version;
    private View view_line_accountandsafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        setTitle();
        init();
    }

    private void init() {
        tv_auth = findViewById(R.id.tv_auth);
        tv_accountandsafe = findViewById(R.id.tv_accountandsafe);
        view_line_accountandsafe = findViewById(R.id.view_line_accountandsafe);
        tv_privacy = findViewById(R.id.tv_privacy);
        tv_num_cache = findViewById(R.id.tv_num_cache);
        tv_help = findViewById(R.id.tv_help);
        tv_aboutus = findViewById(R.id.tv_aboutus);
        tv_share = findViewById(R.id.tv_share);
        tv_version = findViewById(R.id.tv_version);
        tv_logout = findViewById(R.id.tv_logout);
        rela_cache_clean = findViewById(R.id.rela_cache_clean);
        rela_version = findViewById(R.id.rela_version);
        tv_auth.setOnClickListener(this);
        if (SafeSharePreferenceUtils.getInt(Constants.Fields.LOGIN_TYPE, 0) == 1) {
            tv_accountandsafe.setVisibility(View.VISIBLE);
            view_line_accountandsafe.setVisibility(View.VISIBLE);
            tv_accountandsafe.setOnClickListener(this);
        }
        tv_privacy.setOnClickListener(this);
        tv_help.setOnClickListener(this);
        tv_aboutus.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
        rela_cache_clean.setOnClickListener(this);
        rela_version.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        String cacheSize = GlideCacheUtil.getCacheSize(App.getInstance());
        if (cacheSize.contains("Byte")) {
            tv_num_cache.setText("0MB");
        } else {
            tv_num_cache.setText(cacheSize);
        }

        tv_version.setText(PlatformInfoUtils.getAppVersion(App.getInstance()));

        // 实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
        int authStatus = SafeSharePreferenceUtils.getInt(Constants.Fields.AUTHSTATUS, 0);
        if (authStatus == 2) {
            findViewById(R.id.tv_already_authentication).setVisibility(View.VISIBLE);
        }

        findViewById(R.id.zhi_fu_bao_view).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_auth:
                // 实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
                int authStatus = SafeSharePreferenceUtils.getInt(Constants.Fields.AUTHSTATUS, 0);
                if (authStatus == 2) {
                    Utils.showToastShortTime("已认证");
                    return;
                }

                if (authStatus == 1) {
                    Utils.showToastShortTime("等待审核");
                    return;
                }

                startActivity(new Intent(MoreActivity.this, RealNameCertificationActivity.class));
                break;

            case R.id.zhi_fu_bao_view:

                // 实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
                authStatus = SafeSharePreferenceUtils.getInt(Constants.Fields.AUTHSTATUS, 0);
                if (authStatus == 2) {
                    startActivity(new Intent(MoreActivity.this, AlipayInfoActivity.class));
                    return;
                }

                if (authStatus == 0 || authStatus == 3) {
                    Utils.showToastShortTime("未实名认证，不能绑定支付宝");
                    return;
                }

                if (authStatus == 1) {
                    Utils.showToastShortTime("实名认证等待审核中，审核通过后才能绑定支付宝");
                    return;
                }

                break;

            case R.id.tv_accountandsafe:
                startActivity(new Intent(this, ChangePassWordActivity.class));
                break;
            case R.id.tv_privacy:
                startActivity(new Intent(this, BlackListActiviity.class));
                break;
            case R.id.tv_help:
                Intent intent_help = new Intent(this, WXPayEntryActivity.class);
                String url = "";
                if (BuildConfig.LOG_DEBUG) {
                    url = "http://39.105.98.17:8090/youban/view/help/index.html";
                } else {
                    url = "http://bjhdlt.cn/youban/view/help/index.html";
                }
                intent_help.putExtra(Constants.KEY.KEY_URL, url);
                intent_help.putExtra(Constants.Action.ACTION_BROWSE, 3);

                startActivity(intent_help);

                break;
            case R.id.tv_aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.tv_share:
                ShareUtil.getInstance().showShare(MoreActivity.this, ShareUtil.YOUBAN, 0, null, "", "", "", "", false);
                break;
            case R.id.rela_cache_clean:
                GlideCacheUtil.clearImageAllCache(App.getInstance());
                tv_num_cache.setText("0MB");
                break;
            case R.id.rela_version:
                break;
            case R.id.tv_logout:

                // 注销登录(不再接收 Push 消息)
                RongIMutils.logoutApp();
                // 通知主进程
                Intent msgIntent = new Intent(Constants.KEY.ACTION_SINGLE_POINT);
                msgIntent.putExtra(Constants.Action.ACTION_EXTRA, "退出当前账号");
                App.getInstance().sendBroadcast(msgIntent);

                break;

        }

    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_more);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (tv_auth != null) {
            tv_auth = null;
        }

        if (tv_share != null) {
            tv_share = null;
        }

        if (tv_accountandsafe != null) {
            tv_accountandsafe = null;
        }

        if (tv_privacy != null) {
            tv_privacy = null;
        }

        if (tv_num_cache != null) {
            tv_num_cache = null;
        }

        if (tv_help != null) {
            tv_help = null;
        }

        if (tv_aboutus != null) {
            tv_aboutus = null;
        }

        if (tv_version != null) {
            tv_version = null;
        }

        if (tv_logout != null) {
            tv_logout = null;
        }

        if (rela_cache_clean != null) {
            rela_cache_clean.removeAllViews();
            rela_cache_clean = null;
        }

        if (rela_version != null) {
            rela_version.removeAllViews();
            rela_version = null;
        }

        if (view_line_accountandsafe != null) {
            view_line_accountandsafe = null;
        }

    }
}
