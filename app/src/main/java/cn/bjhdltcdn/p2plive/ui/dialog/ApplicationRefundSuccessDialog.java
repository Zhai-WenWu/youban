package cn.bjhdltcdn.p2plive.ui.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.ui.activity.AlipayInfoActivity;

/**
 * 申请退款框
 */
public class ApplicationRefundSuccessDialog extends DialogFragment {

    private View rootView;
    private ItemClick itemClick;
    TextView tip_textview;
    ImageView closeImg;
    private int comeInType = 2;//1:从一建发布进入2：从附近活动3:从圈子详情进入
    private boolean tipTextGone;

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

    public void setItemText(String item1Text, String item2Text) {
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
        rootView = inflater.inflate(R.layout.dialog_application_refund_success_layout, null);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tip_textview = (TextView) rootView.findViewById(R.id.text_cancel);
        tip_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(getActivity(), AlipayInfoActivity.class);
                getActivity().startActivity(intent);
            }
        });
        if (tipTextGone) {
            tip_textview.setVisibility(View.GONE);
        }


        closeImg = (ImageView) rootView.findViewById(R.id.close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
            }
        });

    }

    public void setTipTextGone() {
        tipTextGone = true;
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
