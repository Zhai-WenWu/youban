package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.VideoPlayFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class VideoPlayFullScreenActivity extends BaseActivity {


    private VideoPlayFragment mFragment;
    private int comeInType;//0:从帖子/表白/帮帮忙 列表进入1：从店铺推荐进入
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_full_screen);

        Intent intent = getIntent();

        mFragment = (VideoPlayFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = new VideoPlayFragment();
        }

        String videoPath = intent.getStringExtra(Constants.Fields.VIDEO_PATH);
        String videoImgUrl = intent.getStringExtra(Constants.Fields.VIDEO_IMAGE_URL);

        Logger.d("videoPath === " + videoPath + "  ===videoImgUrl==== " + videoImgUrl);

        if (!StringUtils.isEmpty(videoPath)) {
            mFragment.setData(videoPath, videoImgUrl, 2);
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.video_frame);
        comeInType=intent.getIntExtra("comeInType",0);
        storeId=intent.getLongExtra("storeId",0);
        if(comeInType==1){
            TextView forwardTextView=findViewById(R.id.forward_text_view);
            forwardTextView.setVisibility(View.VISIBLE);
            forwardTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(VideoPlayFullScreenActivity.this,ShopDetailActivity.class);
                    intent.putExtra(Constants.Fields.STORE_ID,storeId);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (BuildConfig.LOG_DEBUG) {
            String message = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "屏幕设置为：横屏" : "屏幕设置为：竖屏";
            Utils.showToastShortTime(message);
        }
    }



}
