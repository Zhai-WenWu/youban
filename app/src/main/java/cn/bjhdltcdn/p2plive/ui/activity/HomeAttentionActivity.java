package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeAttentionFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.VideoPlayFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class HomeAttentionActivity extends BaseActivity {


    private HomeAttentionFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragent_layout);
        setTitle();
        Intent intent = getIntent();

        mFragment = (HomeAttentionFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = new HomeAttentionFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.video_frame);

    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_home_attention);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }






}
