package cn.bjhdltcdn.p2plive.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.bjhdltcdn.p2plive.broadcast.RongPushMessageReceiver;


/**
 * 监听uri跳转的页面
 */

public class UriSkipActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = getIntent().getData();
        if (uri != null) {// H5启动
            if (!uri.getScheme().startsWith("youban") || !uri.getHost().equals("cn.bjhdltcdn.p2plive") || !uri.getPath().contains("launchApp")) {
                // 非指定协议启动，则关闭
                finish();
                return;
            }

            RongPushMessageReceiver.doStartApplicationWithPackageName(getPackageName());
            finish();

        }

    }
}
