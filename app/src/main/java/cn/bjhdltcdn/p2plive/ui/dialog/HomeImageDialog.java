package cn.bjhdltcdn.p2plive.ui.dialog;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.LoginRecommendInfo;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 性别选择弹框
 */
public class HomeImageDialog extends DialogFragment {

    private View rootView;
    private ImageView iv_img;
    private ImageView iv_close;
    private int size;
    List<LoginRecommendInfo> mList = new ArrayList<>();
    private RelativeLayout rl_top;
    private TextView tv_go;

    public void setDate(List<LoginRecommendInfo> list) {
        this.mList = list;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.dialog_home_image, null);

        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        iv_img = rootView.findViewById(R.id.iv_img);
        iv_close = rootView.findViewById(R.id.iv_close);
        tv_go = rootView.findViewById(R.id.tv_go);


        size = mList.size();
        if (mList.size() > 0) {
            LoginRecommendInfo loginRecommendInfo = mList.get(size - 1);
            ViewGroup.LayoutParams layoutParams = iv_img.getLayoutParams();
            layoutParams.width = loginRecommendInfo.getWidth();
            layoutParams.height = loginRecommendInfo.getHeight();
            iv_img.setLayoutParams(layoutParams);
            tv_go.setText(loginRecommendInfo.getTitle());

            RequestOptions options = new RequestOptions().centerCrop()
                    .bitmapTransform(new RoundedCornersTransformation(18, 0, RoundedCornersTransformation.CornerType.TOP))
                    .error(R.mipmap.error_bg);
            Glide.with(App.getInstance()).asBitmap().load(loginRecommendInfo.getImgUrl()).apply(options).into(iv_img);

            size -= 1;
        }

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (size > 0) {
                    LoginRecommendInfo loginRecommendInfo = mList.get(size - 1);
                    ViewGroup.LayoutParams layoutParams = iv_img.getLayoutParams();
                    layoutParams.width = loginRecommendInfo.getWidth();
                    layoutParams.height = loginRecommendInfo.getHeight();
                    iv_img.setLayoutParams(layoutParams);
                    tv_go.setText(loginRecommendInfo.getTitle());

                    RequestOptions options = new RequestOptions().centerCrop()
                            .bitmapTransform(new RoundedCornersTransformation(18, 0, RoundedCornersTransformation.CornerType.TOP))
                            .error(R.mipmap.error_bg);
                    Glide.with(App.getInstance()).asBitmap().load(loginRecommendInfo.getImgUrl()).apply(options).into(iv_img);

                    size -= 1;
                } else {
                    dismiss();
                }
            }
        });

        tv_go.setOnClickListener(new View.OnClickListener() {

            private Intent intent;

            @Override
            public void onClick(View view) {
                if (size >= 0) {
                    LoginRecommendInfo loginRecommendInfo = mList.get(size);
                    switch (loginRecommendInfo.getRecommendType()) {
                        case 1://该活动H5规则页面
                            intent = new Intent(getActivity(), WXPayEntryActivity.class);
                            intent.putExtra(Constants.KEY.KEY_URL, loginRecommendInfo.getGotoUrl() + "?userId=" + SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));//+ "?userId=" + SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
                            getActivity().startActivity(intent);
                            break;
                        case 2://圈子详情
                            break;
                        case 3://同学帮帮忙详情
                            break;
                        case 4://表白墙
                            break;
                        case 5://商圈
                            String gotoUrl = loginRecommendInfo.getGotoUrl();
                            if (!TextUtils.isEmpty(gotoUrl)) {
                                Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                                intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
                                intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
                                startActivity(intent);
                            }
                            break;
                        case 6://附近活动列表
                            break;

                    }
                    if (size - 1 >= 0) {
                        LoginRecommendInfo loginRecommendInfo1 = mList.get(size - 1);
                        ViewGroup.LayoutParams layoutParams = iv_img.getLayoutParams();
                        layoutParams.width = loginRecommendInfo1.getWidth();
                        layoutParams.height = loginRecommendInfo1.getHeight();
                        iv_img.setLayoutParams(layoutParams);
                        tv_go.setText(loginRecommendInfo1.getTitle());

                        RequestOptions options = new RequestOptions().centerCrop()
                                .bitmapTransform(new RoundedCornersTransformation(18, 0, RoundedCornersTransformation.CornerType.TOP))
                                .error(R.mipmap.error_bg);
                        Glide.with(App.getInstance()).asBitmap().load(loginRecommendInfo1.getImgUrl()).apply(options).into(iv_img);
                    } else {
                        dismiss();
                    }
                    size -= 1;


                } else {
                    dismiss();
                }
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
