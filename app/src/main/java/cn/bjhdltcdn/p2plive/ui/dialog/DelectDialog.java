package cn.bjhdltcdn.p2plive.ui.dialog;



import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by wenquan on 2015/10/14.
 */
public class DelectDialog extends DialogFragment {

    private View rootView;
    private View layoutView;
    private SelectItemListener itemListener;
    private TextView titleView ;
    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelectListenner(SelectItemListener itemListener) {
        this.itemListener = itemListener;
    }

    public void setSelectListenner(String title, SelectItemListener itemListener) {
        this.title = title;
        this.itemListener = itemListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        rootView = inflater.inflate(R.layout.delect_image_dialog_layout, null);
        layoutView = rootView.findViewById(R.id.layout_view);
        layoutView.setAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_enter));
        titleView = (TextView) rootView.findViewById(R.id.edit_one_textview);

        if (!StringUtils.isEmpty(title)) {
            titleView.setText(title);
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
        initView();

        return rootView;
    }

    private void initView() {
        TextView confirm = (TextView) rootView.findViewById(R.id.text_ok);
        TextView cancel = (TextView) rootView.findViewById(R.id.text_cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.confirmItemClick();
                }
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        show(manager,"dialog");
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

    public interface SelectItemListener {
        void confirmItemClick();
    }
}
