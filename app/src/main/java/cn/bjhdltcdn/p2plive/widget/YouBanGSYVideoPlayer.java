package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shuyu.gsyvideoplayer.video.NormalGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by xiawenquan on 17/12/23.
 */

public class YouBanGSYVideoPlayer extends NormalGSYVideoPlayer {

    private ImageView thumbView;

    public ImageView getThumbView() {
        return thumbView;
    }

    public YouBanGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public YouBanGSYVideoPlayer(Context context) {
        super(context);
    }

    public YouBanGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        thumbView = findViewById(R.id.thumb_view);

    }

    @Override
    public int getLayoutId() {
        return R.layout.youban_gsy_video_player;
    }

    @Override
    protected void updateStartImage() {
        if(mStartButton instanceof ImageView) {
            ImageView imageView = (ImageView) mStartButton;
            if (mCurrentState == CURRENT_STATE_PLAYING) {
                imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_pause_selector);
            } else if (mCurrentState == CURRENT_STATE_ERROR) {
                imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_play_selector);
            } else {
                imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_play_selector);
            }
        }
    }

}
