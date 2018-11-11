package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;

/**
 * 性别选择弹框
 */
public class DelectTipDialog extends DialogFragment {

    private View rootView;

    private ItemClick itemClick;
    private String titleStr, rightStr;
    private TextView tv_title;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setRightButtonStr(String rightStr) {
        this.rightStr = rightStr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_sex_select_layout, null);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_title = rootView.findViewById(R.id.tv_title);
        tv_title.setText(titleStr);
        TextView tv_ok = rootView.findViewById(R.id.tv_ok);
        if (TextUtils.isEmpty(rightStr)) {
            tv_ok.setText("确定");
        } else {
            tv_ok.setText(rightStr);
        }
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {
                    itemClick.itemClick();
                }
                dismiss();
            }
        });

        // 取消
        TextView tv_cancel = rootView.findViewById(R.id.tv_reelect);
        tv_cancel.setText("取消");
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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
        } catch (IllegalStateException e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


    public interface ItemClick {
        void itemClick();
    }
}
