package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.umeng.analytics.MobclickAgent;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.VersionResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.RecommendAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

public class AboutUsActivity extends BaseActivity implements BaseView {
    private ListView listView;
    private RecommendAdapter adapter;
    private UserPresenter userPresenter;
    private TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getView();
        setTitle();
        addListener();
    }

    public void getView() {
        listView = findViewById(R.id.list_about_us);
        tv_version = findViewById(R.id.tv_version);
        tv_version.setText("友伴 " + BuildConfig.VERSION_NAME);
        adapter = new RecommendAdapter("aboutus");
        listView.setAdapter(adapter);

    }

    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitle("关于我们");
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    public UserPresenter getUserPresenter() {

        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public void addListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: //用户协议
                        Intent intent = new Intent(AboutUsActivity.this, WXPayEntryActivity.class);
                        intent.putExtra(Constants.Action.ACTION_BROWSE, 1);
                        startActivity(intent);
                        break;

                    case 1://版本更新
                        getUserPresenter().getUpgradeVersion(BuildConfig.VERSION_NAME + "", 1);
                        break;

                    case 2: //去评分
                        try {
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                        } catch (Exception e) {
                            Utils.showToastShortTime("很抱歉，不能启动应用市场！");
                        }
                        break;

                }
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof VersionResponse) {
            VersionResponse response = (VersionResponse) object;
            if (response.getCode() == 200) {
                // 是否升级 0 不升级 1 强制升级 2 普通升级,
                if (response.getIsUpgrade() == 1) {

                    MaterialDialog dialog = new MaterialDialog.Builder(AboutUsActivity.this).content(R.string.str_update_version_tips_1)
                            .contentColor(getResources().getColor(R.color.color_333333))
                            .backgroundColor(getResources().getColor(R.color.white))
                            .negativeText("暂不升级").negativeColor(getResources().getColor(R.color.color_1b9e4e)).onNegative(new MaterialDialog.SingleButtonCallback() {

                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                                    RongIMutils.exitApp();
                                    finish();
                                }

                            }).positiveText("立即升级").positiveColor(getResources().getColor(R.color.color_1b9e4e)).onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    try {
                                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                        Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent1);

                                        dialog.dismiss();
                                        MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                                        RongIMutils.exitApp();
                                        finish();

                                    } catch (Exception e) {
                                        Utils.showToastShortTime("很抱歉，不能启动应用市场！");
                                    }
                                }

                            }).show();

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            MobclickAgent.onKillProcess(App.getInstance());//方法，用来保存统计数据
                            RongIMutils.exitApp();
                            finish();
                        }
                    });


                } else if (response.getIsUpgrade() == 2) {
                    new MaterialDialog.Builder(AboutUsActivity.this).content(R.string.str_update_version_tips_2)
                            .contentColor(getResources().getColor(R.color.color_333333))
                            .backgroundColor(getResources().getColor(R.color.white))
                            .negativeText("暂时忽略").negativeColor(getResources().getColor(R.color.color_1b9e4e)).onNegative(new MaterialDialog.SingleButtonCallback() {

                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }

                    }).positiveText("立即升级").positiveColor(getResources().getColor(R.color.color_1b9e4e)).onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            try {
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent1);

                            } catch (Exception e) {
                                Utils.showToastShortTime("很抱歉，不能启动应用市场！");
                            }
                        }

                    }).show();

                } else if (response.getIsUpgrade() == 0) {
                    Utils.showToastShortTime("您已经是最新的版本啦");
                }
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
