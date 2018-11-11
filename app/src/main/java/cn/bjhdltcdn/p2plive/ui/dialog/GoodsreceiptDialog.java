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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.LoginRecommendInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.VerificationCodeView;

/**
 * 确认收货弹框
 */
public class GoodsreceiptDialog extends DialogFragment implements BaseView {

    private View rootView;
    private int size;
    List<LoginRecommendInfo> mList = new ArrayList<>();
    private LinearLayout ll_star;
    private TextView tv_good_sreceipt;
    private int starLevel;
    private long orderId;
    private VerificationCodeView verificationcodeview;
    public String codeSrting = "";
    private View view_edit;
    private ItemClick itemClick;
    private boolean isSeller;//是不是卖家
    private TextView tv_title;

    public void setIsSeller(boolean seller) {
        isSeller = seller;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public void setDate(List<LoginRecommendInfo> list) {
        this.mList = list;
    }

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_goods_receipt, null);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ll_star = rootView.findViewById(R.id.ll_star);
        tv_good_sreceipt = rootView.findViewById(R.id.tv_good_sreceipt);
        verificationcodeview = rootView.findViewById(R.id.verificationcodeview);
        tv_title = rootView.findViewById(R.id.tv_title);
        TextView tv_dismiss = rootView.findViewById(R.id.tv_dismiss);
        view_edit = rootView.findViewById(R.id.view_edit);

        if (isSeller) {
            tv_title.setText("确保买家已收到货物且验收完毕，输入买家下单时的取货码，确认买家已收货。");
            tv_good_sreceipt.setText("确认送达");
        }

        view_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < verificationcodeview.getChildCount(); i++) {
                    EditText childView = (EditText) verificationcodeview.getChildAt(i);
                    childView.setText("");
                    codeSrting = "";
                }
                KeyBoardUtils.openKeybord((EditText) verificationcodeview.getChildAt(0), App.getInstance());
            }
        });

        tv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        verificationcodeview.setOnCodeFinishListener(new VerificationCodeView.OnCodeFinishListener() {

            @Override
            public void onComplete(String content) {
                codeSrting = content;
            }
        });

        final OrderPresenter orderPresenter = new OrderPresenter(this);
        tv_good_sreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codeSrting.length() != 6) {
                    Utils.showToastShortTime("请输入正确的取货码");
                }
//                else if (starLevel == 0) {
//                    Utils.showToastShortTime("请对商家进行评价");
//                }
                else {
                    if (isSeller) {
                        orderPresenter.updateOrderStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), orderId, codeSrting, starLevel, 1);
                    } else {
                        orderPresenter.updateOrderStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), orderId, codeSrting, starLevel, 3);
                    }
                }
            }
        });

        for (int i = 0; i < ll_star.getChildCount(); i++) {
            ImageView imageView = (ImageView) ll_star.getChildAt(i);
            imageView.setOnClickListener(new onStarClick(i));
        }

    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_UPDATEORDERSTATUS)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());
                if (response.getCode() == 200 || response.getCode() == 201) {
                    itemClick.itemClick();
                    dismiss();
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    class onStarClick implements View.OnClickListener {
        int i;

        public onStarClick(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            starLevel = i + 1;
            for (int j = 0; j < ll_star.getChildCount(); j++) {
                ImageView imageView = (ImageView) ll_star.getChildAt(j);
                imageView.setImageResource(R.drawable.goods_star_false);
            }
            for (int j = 0; j <= i; j++) {
                ImageView imageView = (ImageView) ll_star.getChildAt(j);
                imageView.setImageResource(R.drawable.goods_star_true);
            }
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

    public interface ItemClick {
        void itemClick();
    }
}
