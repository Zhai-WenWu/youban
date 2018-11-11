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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;

/**
 * Created by xiawenquan on 17/11/27.
 */

public class PostOperationFragmentDialog extends DialogFragment {

    private View rootView;

    private LinearLayout layoutView;

    private ViewItemClick itemClick;

    private List<String> list;

    public void setTextList(String... textList) {
        if (textList != null) {
            list = new ArrayList<>(1);
            for (int i = 0; i < textList.length; i++) {
                list.add(textList[i]);
            }
        }
    }

    public void setItemClick(ViewItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(android.app.DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_post_operation_dialog_layout, null);

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


        if (list == null) {
            return;
        }

        layoutView = rootView.findViewById(R.id.layout_view);
        layoutView.removeAllViews();

        for (int i = 1; i <= list.size(); i++) {
            final String itemText = list.get(i - 1);
            TextView itemView = (TextView) View.inflate(App.getInstance(), R.layout.fragment_post_operation_dialog_item_layout, null);
            itemView.setText(itemText);
            layoutView.addView(itemView);

            if ("取消".equals(itemText)) {
                itemView.setTextColor(getResources().getColor(R.color.color_999999));
            }

            final int finalI = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!"取消".equals(itemText) && itemClick != null) {
                        itemClick.onClick(finalI);
                    }

                    dismiss();
                }
            });
        }

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

    @Override
    public void onDetach() {
        super.onDetach();
        if (list != null) {
            list.clear();
        }
        list = null;

        if (itemClick != null) {
            itemClick = null;
        }

        if (rootView != null) {
            rootView = null;
        }

        if (layoutView != null) {
            layoutView.removeAllViews();
        }
        layoutView = null;
    }

    public interface ViewItemClick {
        void onClick(int type);
    }

}
