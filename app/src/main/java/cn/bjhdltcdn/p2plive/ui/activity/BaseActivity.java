package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PushdbModel;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

public class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;

    private final String simpleName = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Logger.d(simpleName);

        if (!StringUtils.isEmpty(simpleName) && !simpleName.equals(LoginActivity.class.getSimpleName())) {
            if (mReceiver == null) {
                mReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        if (intent != null) {
                            String action = intent.getAction();// 单点登录
                            if (Constants.KEY.ACTION_SINGLE_POINT.equals(action)) {


                                if (!simpleName.equals("MainActivity")) {
                                    finish();
                                    return;
                                }

                                SafeSharePreferenceUtils.clearDataByKey(App.getInstance()
                                        , Constants.KEY.KEY_SYS_TOKEN
                                        , Constants.Fields.USER_ID
                                        , Constants.KEY.KEY_SIGNATURE
                                        , Constants.Fields.PHONE_NUMBER
                                        , Constants.Fields.PASS_WORD
                                        , Constants.Fields.LOGIN_TYPE
                                        , Constants.Fields.AUTHSTATUS);

                                // 取消掉所有的请求
                                OkGo.getInstance().cancelAll();

                                List list = new ArrayList(1);
                                list.add(BaseUser.class);
                                list.add(PushdbModel.class);
                                GreenDaoUtils.getInstance().deleteAll(list);


                                startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                                finish();


                            }
                        }
                    }

                };

                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(Constants.KEY.ACTION_SINGLE_POINT);
                registerReceiver(mReceiver, intentFilter);

            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        // 友盟统计(统计使用时长)
        MobclickAgent.onResume(this);

        App.getInstance().setBackground(false);
//        Logger.d("切换到前台");
    }


    @Override
    protected void onPause() {
        super.onPause();

        // 友盟统计(统计使用时长)当应用在后台运行超过30秒（默认）再回到前端，将被认为是两个独立的session(启动)
        MobclickAgent.onPause(this);

        App.getInstance().setBackground(true);
//        Logger.d("切换到后台");
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        try {
            Configuration config = new Configuration();
            config.setToDefaults();
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!StringUtils.isEmpty(simpleName) && !simpleName.equals(LoginActivity.class.getSimpleName())) {
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
            }
        }

        mReceiver = null;

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
