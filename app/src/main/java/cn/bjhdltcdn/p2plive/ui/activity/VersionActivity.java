package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class VersionActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_version);
    }

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && setting == 1) {
            killApp();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void killApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.TAG_EXIT, true);
        startActivity(intent);
    }*/
}
