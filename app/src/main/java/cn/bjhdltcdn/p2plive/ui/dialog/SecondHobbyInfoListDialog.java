package cn.bjhdltcdn.p2plive.ui.dialog;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import cn.bjhdltcdn.p2plive.widget.tagview.TagView;

/**
 * 二级标签列表
 */

public class SecondHobbyInfoListDialog extends DialogFragment {

    private View rootView;

    private SelectItemCallback selectItemCallback;
    private TagContainerLayout tagContainerLayout;

    public void setSelectItemCallback(SelectItemCallback selectItemCallback) {
        this.selectItemCallback = selectItemCallback;
    }

    private List<HobbyInfo> list;

    public void setList(List<HobbyInfo> list) {
        this.list = list;
    }

    /**
     * 选择的个数
     */
    private int selectCount = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.color_cc333333_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.second_hobbyinfo_list_dialog_layout, container, false);

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initTagView();

    }

    private void initTagView() {

        View chevronImage = rootView.findViewById(R.id.chevron_image);
        chevronImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ObjectAnimator.ofFloat(chevronImage, View.ROTATION.getName(), 0, -180).start();

        tagContainerLayout = rootView.findViewById(R.id.tag_container_view);
        tagContainerLayout.setListTags(list, false);
        int childCount = tagContainerLayout.getChildCount();
        if (childCount > 0) {
            tagContainerLayout.removeAllViews();
        }

        for (int i = 0; i < list.size(); i++) {
            HobbyInfo hobbyInfo = list.get(i);
            View itemView = View.inflate(App.getInstance(), R.layout.organization_second_list_item_layout, null);
            TextView textView = itemView.findViewById(R.id.text_view);
            ImageView popView = itemView.findViewById(R.id.pop_view);
            tagContainerLayout.addView(itemView);
            textView.setOnClickListener(new ViewOnClickListener(i));

            // 名称
            textView.setText(hobbyInfo.getHobbyName());


            // new标签
            if (hobbyInfo.getSecondInterestType() == 2) {
                popView.setVisibility(View.VISIBLE);
            } else {
                popView.setVisibility(View.GONE);
            }


            if (hobbyInfo.getIsSelect() == 1) {// 已选中
                textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                textView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);

                if (i != 0) {
                    selectCount ++;
                }

            } else {
                textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                textView.setBackgroundResource(R.drawable.shape_round_40_stroke_d8d8d8_solid_fafafa);
            }

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
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (selectItemCallback != null) {
            selectItemCallback.run();
        }

    }

    public interface SelectItemCallback {
        /**
         * isSelect 是否选择(0未选,1已选),
         * 默认是-1
         */
        void selectItem(int position,int isSelect);

        void run();
    }

    class ViewOnClickListener implements View.OnClickListener {

        private int position;

        public ViewOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {


            if (position == 0) {

                selectCount = 0;

                for (int i = 0; i < tagContainerLayout.getChildCount(); i++) {

                    View viewObject = tagContainerLayout.getChildAt(i);
                    if (viewObject instanceof RelativeLayout) {
                        RelativeLayout relativeLayout = (RelativeLayout) viewObject;
                        TextView textView = relativeLayout.findViewById(R.id.text_view);

                        HobbyInfo hobbyInfo = list.get(position);
                        hobbyInfo.setIsSelect(0);

                        if (i == 0) {
                            textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                            textView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
                            hobbyInfo.setIsSelect(1);
                        } else {

                            textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                            textView.setBackgroundResource(R.drawable.shape_round_40_stroke_d8d8d8_solid_fafafa);

                        }

                    }
                }

                if (selectItemCallback != null) {
                    selectItemCallback.selectItem(position,-1);
                }

                return;

            }

            if (tagContainerLayout.getChildCount() > position) {

                // 全部按钮
                TextView allTextView = null;
                View viewObject = tagContainerLayout.getChildAt(0);
                if (viewObject instanceof RelativeLayout) {
                    RelativeLayout relativeLayout = (RelativeLayout) viewObject;
                    allTextView = relativeLayout.findViewById(R.id.text_view);

                }

                // 被选中的按钮
                viewObject = tagContainerLayout.getChildAt(position);

                HobbyInfo hobbyInfo = list.get(position);

                if (viewObject instanceof RelativeLayout) {
                    RelativeLayout relativeLayout = (RelativeLayout) viewObject;
                    TextView textView = relativeLayout.findViewById(R.id.text_view);
                    if (hobbyInfo.getIsSelect() == 0) {
                        textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                        textView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
                        hobbyInfo.setIsSelect(1);

                        selectCount ++;

                    } else {

                        textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                        textView.setBackgroundResource(R.drawable.shape_round_40_stroke_d8d8d8_solid_fafafa);
                        hobbyInfo.setIsSelect(0);

                        selectCount --;

                    }

                }

                HobbyInfo allHobbyInfo = list.get(0);
                if (selectCount == 0 ) {

                    allTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                    allTextView.setBackgroundResource(R.drawable.shape_round_20_solid_ffee00);
                    allHobbyInfo.setIsSelect(1);

                } else {
                    allTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                    allTextView.setBackgroundResource(R.drawable.shape_round_40_stroke_d8d8d8_solid_fafafa);
                    allHobbyInfo.setIsSelect(0);
                }

                if (selectItemCallback != null) {
                    selectItemCallback.selectItem(selectCount == 0 ? 0 : position,selectCount == 0 ? -1 : hobbyInfo.getIsSelect());
                }


            }




        }
    }

}
