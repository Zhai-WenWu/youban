package cn.bjhdltcdn.p2plive.ui.dialog;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.event.UpdateWithdrawCashEvent;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 发工资框
 */
public class PayOffDialog extends DialogFragment{

    private View rootView;
    private ItemClick itemClick;
    TextView payOffTextView;
    ImageView closeImg;
    EditText moneyEditText;


    public ItemClick getItemClick() {
        return itemClick;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_pay_off_layout, null);
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

        closeImg= (ImageView) rootView.findViewById(R.id.close_img);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 删除
        moneyEditText = (EditText) rootView.findViewById(R.id.charge_edit);
        // 付款
        payOffTextView = (TextView) rootView.findViewById(R.id.text_payoff);
        payOffTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {
                    String moneyStr=moneyEditText.getText().toString();
                    if(!StringUtils.isEmpty(moneyStr)){
                        itemClick.itemClick(moneyStr);
                    }else{
                        Utils.showToastShortTime("请输入正确的金额");
                    }

                }
            }
        });

//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    getActivity().finish();
//                    return true;
//                }
//                return false;
//            }
//        });
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

        void itemClick(String moneyStr);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateWithdrawCashEvent event) {
        if (event == null) {
            return;
        }
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
