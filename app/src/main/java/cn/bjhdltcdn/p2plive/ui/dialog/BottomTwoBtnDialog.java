package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;


/**
 * 底部有2个按钮的对话框
 */

public class BottomTwoBtnDialog extends DialogFragment {

    private View rootView;
    private TextView titleView;
    private Button leftBottomView;
    private Button rightBottomView;

    private String titleStr;
    private String leftBottomViewStr;
    private String rightBottomViewStr;

    private LeftBottomViewClickListener leftBottomViewClickListener;
    private RightBottomViewClickListener rightBottomViewClickListener;

    public RightBottomViewClickListener getRightBottomViewClickListener() {
        return rightBottomViewClickListener;
    }

    public void setRightBottomViewClickListener(RightBottomViewClickListener rightBottomViewClickListener) {
        this.rightBottomViewClickListener = rightBottomViewClickListener;
    }

    public LeftBottomViewClickListener getLeftBottomViewClickListener() {
        return leftBottomViewClickListener;
    }

    public void setLeftBottomViewClickListener(LeftBottomViewClickListener leftBottomViewClickListener) {
        this.leftBottomViewClickListener = leftBottomViewClickListener;
    }

    public void setDialogText(String titleStr, String leftBottomViewStr, String rightBottomViewStr) {
        this.titleStr = titleStr;
        this.leftBottomViewStr = leftBottomViewStr;
        this.rightBottomViewStr = rightBottomViewStr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.bottom_two_btn_dialog_layout, null);
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

        titleView = (TextView) rootView.findViewById(R.id.title_view);
        leftBottomView = (Button) rootView.findViewById(R.id.btn_view_1);
        rightBottomView = (Button) rootView.findViewById(R.id.btn_view_2);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 标题
        titleView.setText(titleStr);

        // 左边按钮
        leftBottomView.setText(leftBottomViewStr);
        leftBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftBottomViewClickListener != null) {
                    leftBottomViewClickListener.onLeftClick();
                }
                dismiss();
            }
        });
        // 右边按钮
        rightBottomView.setText(rightBottomViewStr);
        rightBottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightBottomViewClickListener != null) {
                    rightBottomViewClickListener.onRightClick();
                }
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        try {
            if (rightBottomViewClickListener != null) {
                rightBottomViewClickListener = null ;
            }

            if (leftBottomViewClickListener != null) {
                leftBottomViewClickListener = null ;
            }

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

    public interface LeftBottomViewClickListener {
        void onLeftClick();
    }

    public interface RightBottomViewClickListener {
        void onRightClick();
    }

}
