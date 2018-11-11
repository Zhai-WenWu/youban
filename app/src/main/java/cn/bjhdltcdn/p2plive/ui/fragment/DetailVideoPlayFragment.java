package cn.bjhdltcdn.p2plive.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.YouBanGSYVideoPlayer;

/**
 * 视频播放界面
 * 嵌套页面
 */
public class DetailVideoPlayFragment extends BaseFragment {

    private View rootView;

    private String videoPath;
    private String videoImgUrl;
    private int type;

    private HelpInfo mHelpInfo;
    private YouBanGSYVideoPlayer normalGSYVideoPlayer;


    public void setData(String videoPath, String videoImgUrl, int type) {
        this.videoPath = videoPath;
        this.videoImgUrl = videoImgUrl;
        this.type = type;
    }


    public void setData(HelpInfo helpInfo) {
        this.mHelpInfo = helpInfo;
        this.videoPath = helpInfo.getVideoUrl();
        this.videoImgUrl = helpInfo.getVideoImageUrl();
        //this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_detail_videoplay, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //super.onViewCreated(view, savedInstanceState);

        normalGSYVideoPlayer = rootView.findViewById(R.id.up_video_view);
        if (!StringUtils.isEmpty(videoPath)) {
            normalGSYVideoPlayer.setUp(videoPath, true, "");
        } else {
            Utils.showToastShortTime("视频路径无效");
            getActivity().finish();
        }

        normalGSYVideoPlayer.setDismissControlTime(1000);

        // 放大按钮
        ImageView imageView = normalGSYVideoPlayer.findViewById(R.id.fullscreen);
        if (type == 2) {
            imageView.setImageResource(R.drawable.video_shrink);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 2) {
                    getActivity().finish();
                } else {
                    normalGSYVideoPlayer.startWindowFullscreen(getActivity(), true, true);
                }
            }
        });

        // 背景图片
        Glide.with(getActivity()).asBitmap().load(videoImgUrl).into(normalGSYVideoPlayer.getThumbView());

        // 处理右边业务模块
        VideoPlayRightSeviceFragment mFragment = new VideoPlayRightSeviceFragment();
        mFragment.setData(mHelpInfo);
        ActivityUtils.addFragmentToActivity(getChildFragmentManager(), mFragment, R.id.videoplay_right_frame);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onVisible(boolean isInit) {

    }

}
