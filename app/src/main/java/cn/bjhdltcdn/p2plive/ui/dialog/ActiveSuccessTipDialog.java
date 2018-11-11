package cn.bjhdltcdn.p2plive.ui.dialog;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * 活动发布成功框
 */
public class ActiveSuccessTipDialog extends DialogFragment{

    private View rootView;
    private ItemClick itemClick;
    TextView textPicView;
    ImageView closeImg;
    private int comeInType=2;//1:从一建发布进入2：从附近活动3:从圈子详情进入

    public void setComeInType(int comeInType) {
        this.comeInType = comeInType;
    }

    public ItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private int gravity;
    private String item1Text;
    private String item2Text;

    public void setItemText(String item1Text, String item2Text){
        this.item1Text = item1Text;
        this.item2Text = item2Text;
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_active_success_tip_layout, null);
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
//                            dismiss();
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 修改
        textPicView = (TextView) rootView.findViewById(R.id.tip_textview);
        if (!StringUtils.isEmpty(item1Text)) {
            textPicView.setText(item1Text);
        }

        closeImg= (ImageView) rootView.findViewById(R.id.close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comeInType==1){
                    //一建发布进入   成功后   跳转到活动列表页面
                }
                dismiss();
                getActivity().finish();
            }
        });

        // 删除
        TextView textCameraView = (TextView) rootView.findViewById(R.id.text_cancel);
        textCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {
                    itemClick.itemClick();
                }
                dismiss();
            }
        });

        if (!StringUtils.isEmpty(item2Text)) {
            textCameraView.setText(item2Text);
        }

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    getActivity().finish();
                    return true;
                }
                return false;
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



    public interface ItemClick {

        void itemClick();
    }
}
