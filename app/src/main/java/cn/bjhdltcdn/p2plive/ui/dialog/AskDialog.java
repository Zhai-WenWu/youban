package cn.bjhdltcdn.p2plive.ui.dialog;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by zhaiww on 2018/5/26
 */
@SuppressLint("ValidFragment")
public class AskDialog extends DialogFragment {

    private View rootView;
    private TextView tv_content, tv_left, tv_right;
    private String content, title, leftText, rightText;
    private OnLeftClickListener onLeftClickListener;
    private OnRightClickListener onRightClickListener;
    private ImageView closeImg;
    private int gravity;

    public interface OnLeftClickListener {
        void onClick();
    }

    public interface OnRightClickListener {
        void onClick();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public AskDialog(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.leftText = builder.leftText;
        this.rightText = builder.rightText;
        this.onLeftClickListener = builder.onLeftClickListener;
        this.onRightClickListener = builder.onRightClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_active_launch_success_tip_layout, null);
        initView();
        return rootView;
    }

    private void initView() {


        TextView title_text_view = rootView.findViewById(R.id.title_text_view);
        if (!StringUtils.isEmpty(title)) {
            title_text_view.setVisibility(View.VISIBLE);
            title_text_view.setText(title);
        }

        tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        if (!StringUtils.isEmpty(content)) {
            tv_content.setText(content);
        }

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv_content.getLayoutParams();
        //此处相当于布局文件中的Android:layout_gravity属性
        lp.gravity = gravity;
        tv_content.setLayoutParams(lp);

        tv_left = (TextView) rootView.findViewById(R.id.btn_left);
        if (!StringUtils.isEmpty(leftText)) {
            tv_left.setText(leftText);
        }
        tv_right = (TextView) rootView.findViewById(R.id.btn_right);
        if (!StringUtils.isEmpty(rightText)) {
            tv_right.setText(rightText);
        }


        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onLeftClickListener != null) {
                    onLeftClickListener.onClick();
                }
                dismiss();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightClickListener != null) {
                    onRightClickListener.onClick();
                }
                dismiss();
            }
        });


    }

    private void onDismiss() {

        if (closeImg != null) {
            closeImg = null;
        }

        if (rootView != null) {
            rootView = null;
        }


        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    public static class Builder {
        private String content, title, leftText, rightText;
        private OnLeftClickListener onLeftClickListener;
        private OnRightClickListener onRightClickListener;

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder leftClickListener(OnLeftClickListener onLeftClickListener) {
            this.onLeftClickListener = onLeftClickListener;
            return this;
        }

        public Builder rightClickListener(OnRightClickListener onRightClickListener) {
            this.onRightClickListener = onRightClickListener;
            return this;
        }

        public Builder leftBtnText(String leftText) {
            this.leftText = leftText;
            return this;
        }

        public Builder rightBtnText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public AskDialog build() {
            return new AskDialog(this);
        }
    }

}
