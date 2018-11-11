package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.VideoMoreEvent;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by zhudi on 2016/4/25
 */
public class VideoMoreDialog extends DialogFragment {

    private View rootView;
    /**
     * 麦克风是否开启
     */
    private boolean isLocalMicClose;
    /**
     * 本地摄像头是否开启
     */
    private boolean isLocalVideoOpen;

    public void setLocalMicClose(boolean localMicClose) {
        isLocalMicClose = localMicClose;
    }

    public void setLocalVideoOpen(boolean localVideoOpen) {
        isLocalVideoOpen = localVideoOpen;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_video_more, null);
        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            onDismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_voice_close = (TextView) rootView.findViewById(R.id.tv_voice_close);
        TextView tv_video_close = (TextView) rootView.findViewById(R.id.tv_video_close);
        Drawable drawableClose = null;
        if (isLocalMicClose) {
            tv_voice_close.setText("打开话筒");
            drawableClose = App.getInstance().getResources().getDrawable(R.drawable.video_group_mic_close_n, null);
            if (drawableClose != null) {
                drawableClose.setBounds(0, 0, Utils.dp2px(26), Utils.dp2px(26));
            }
            tv_voice_close.setCompoundDrawables(null, drawableClose, null, null);
        }
        if (isLocalVideoOpen) {
            tv_video_close.setText("关闭摄像头");
            drawableClose = getResources().getDrawable(R.drawable.video_group_video_close);
            if (drawableClose != null) {
                drawableClose.setBounds(0, 0, Utils.dp2px(26), Utils.dp2px(26));
            }
            tv_video_close.setCompoundDrawables(null, drawableClose, null, null);
        }
        rootView.findViewById(R.id.tv_video_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoMoreEvent(1));
                onDismiss();
            }
        });
        tv_voice_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoMoreEvent(2));
                onDismiss();
            }
        });
        tv_video_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoMoreEvent(3));
                onDismiss();
            }
        });
    }

    private void onDismiss() {
        if (rootView != null) {
            rootView = null;
        }

        dismiss();
    }

    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }
}
