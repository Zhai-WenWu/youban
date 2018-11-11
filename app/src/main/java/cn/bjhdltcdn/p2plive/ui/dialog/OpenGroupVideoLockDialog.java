package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.bjhdltcdn.p2plive.R;

/**
 * 开启房间密码对话框
 */

public class OpenGroupVideoLockDialog extends DialogFragment {

    private View rootView;
    private ItemClickListener itemClickListener;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.open_group_video_lock_dialog_layout, null);
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            dismiss();
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

        // 不密码模式
//        if (roomType == 1) {
        rootView.findViewById(R.id.no_password_view).setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.no_password_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.clickItem(0);
                }
            }
        });

//        }

        // 密码模式
        rootView.findViewById(R.id.password_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.clickItem(1);
                }
            }
        });


        // 取消
        rootView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (itemClickListener != null) {
            itemClickListener = null;
        }

        if (rootView != null) {
            rootView = null;
        }

    }

    public interface ItemClickListener {
        void clickItem(int type);
    }
}
