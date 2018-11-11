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
import cn.bjhdltcdn.p2plive.event.VideoGroupManageEvent;
import cn.bjhdltcdn.p2plive.utils.Utils;


/**
 * Created by zhudi on 2016/1/12
 */
public class VideoGroupManageDialog extends DialogFragment {

    private View rootView;
    private boolean isLocalMicClose;//本地话筒是否关闭
    private boolean isLocalVideoClose;//本地摄像头是否关闭
    private int userRole;//1主持人
    private int openVote = 1;//1发起 2关闭,
    private int onWheatUserNum;//麦上人数 不包括自己
    private TextView pkTextView;

    public void setLocalMicClose(boolean localMicClose) {
        isLocalMicClose = localMicClose;
    }

    public void setLocalVideoClose(boolean localVideoClose) {
        isLocalVideoClose = localVideoClose;
    }


    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public void setOpenVote(int openVote) {
        this.openVote = openVote;
    }

    public void setOnWheatUserNum(int onWheatUserNum) {
        this.onWheatUserNum = onWheatUserNum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_video_group_manage, null);
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

        View layoutView = rootView.findViewById(R.id.layout_view);
        Drawable drawable = layoutView.getBackground();
        if (drawable != null) {
            drawable.mutate().setAlpha((int) (255 * 0.8));
        }

        TextView tv_mic_close = (TextView) rootView.findViewById(R.id.tv_mic_close);
        TextView tv_video_close = (TextView) rootView.findViewById(R.id.tv_video_close);
        TextView tv_transferhosting = (TextView) rootView.findViewById(R.id.tv_transferhosting);
        Drawable drawableClose = null;
        if (isLocalMicClose) {
            tv_mic_close.setText("打开话筒");
            drawableClose = App.getInstance().getResources().getDrawable(R.drawable.video_group_mic_close_n, null);
            if (drawableClose != null) {
                drawableClose.setBounds(0, 0, Utils.dp2px(26), Utils.dp2px(26));
            }
            tv_mic_close.setCompoundDrawables(null, drawableClose, null, null);
        }
        if (isLocalVideoClose) {
            tv_video_close.setText("打开摄像头");
            drawableClose = getResources().getDrawable(R.drawable.video_group_video_close_n);
            if (drawableClose != null) {
                drawableClose.setBounds(0, 0, Utils.dp2px(26), Utils.dp2px(26));
            }
            tv_video_close.setCompoundDrawables(null, drawableClose, null, null);
        }
        if (userRole == 1) {
            tv_transferhosting.setVisibility(View.VISIBLE);
        } else {
            tv_transferhosting.setVisibility(View.GONE);
        }
        rootView.findViewById(R.id.tv_video_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupManageEvent(1));
                onDismiss();
            }
        });
        tv_mic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupManageEvent(2));
                onDismiss();
            }
        });
        tv_video_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupManageEvent(3));
                onDismiss();
            }
        });
        tv_transferhosting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoGroupManageEvent(4));
                onDismiss();
            }
        });
        pkTextView = (TextView) rootView.findViewById(R.id.tv_video_pk);

//        if (userRole == 1 ) {
//            pkTextView.setVisibility(View.VISIBLE);
//        } else {
//            pkTextView.setVisibility(View.GONE);
//        }
//        if (openVote == 1) {
//            pkTextView.setText("开启PK");
//        } else if (openVote == 2) {
//            pkTextView.setText("关闭PK");
//        }
        pkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWheatUserNum > 0) {
                    EventBus.getDefault().post(new VideoGroupManageEvent(5));
                    onDismiss();
                } else {
                    Utils.showToastShortTime("麦上不少于2人才能发起pk");
                }

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
