package cn.bjhdltcdn.p2plive.ui.dialog;


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
 * Created by huwenhua on 2016/10/26
 */
public class ActiveJoinSuccessTipDialog extends DialogFragment {

    private View rootView;
    private TextView tv_content,tv_title,tv_left,tv_right;
    private String content,title,leftText,rightText;
    private OnClickListener onClickListener;
    private ImageView closeImg;
    private int gravity;

    public void setText(String title,String content,String leftText,String rightText){
        this.title=title;
        this.content=content;
        this.leftText=leftText;
        this.rightText=rightText;
    }


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public ActiveJoinSuccessTipDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_active_join_success_tip_layout, null);
        initView();
        // 触摸内容区域外的需要关闭对话框
//        rootView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (v instanceof ViewGroup) {
//                    View layoutView = ((ViewGroup) v).getChildAt(0);
//                    if (layoutView != null) {
//                        float y = event.getY();
//                        float x = event.getX();
//                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
//                        if (!rect.contains((int) x, (int) y)) {
//                            onDismiss();
//                        }
//                        rect.setEmpty();
//                        rect = null;
//                    }
//                }
//                return false;
//            }
//        });
        return rootView;
    }

    private void initView() {


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
        tv_right= (TextView) rootView.findViewById(R.id.btn_right);
        if (!StringUtils.isEmpty(rightText)) {
            tv_right.setText(rightText);
        }


        tv_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onLeftClick();
                dismiss();
            }
        });
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickListener!=null){
                    onClickListener.onRightClick();
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

    public interface OnClickListener{
        void onLeftClick();
        void onRightClick();
    }

}
