package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindClertApplyResponse;
import cn.bjhdltcdn.p2plive.model.ApplyClert;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.UserIconDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by zhaiww on 2018/5/17.
 */

public class ApplyClerkForMasterActivity extends BaseActivity implements BaseView, View.OnClickListener {

    private ImageView iv_user_icon;
    private TextView tv_user_name;
    private TextView tv_user_id;
    private TextView tv_phone_number;
    private TextView tv_addr;
    private TextView tv_desc;
    private ImageView iv_student_card;
    private ImageView iv_student_card_include_info;
    private TextView tv_agree;
    private TextView tv_disagree;
    private GetStoreListPresenter getStoreListPresenter;
    private ApplyClert applyClert;
    private long userId;
    private long applyId;
    private BaseUser baseUser;
    private LinearLayout ll_bottom_btn;
    private int type;
    private ToolBarFragment titleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_staff_for_master);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        applyId = getIntent().getLongExtra(Constants.Fields.APPLY_ID, 0);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        getStoreListPresenter().findClertApply(userId, applyId);
        setTitle();
        initView();
    }

    private void initView() {
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_id = findViewById(R.id.tv_user_id);
        tv_phone_number = findViewById(R.id.tv_phone_number);
        tv_addr = findViewById(R.id.tv_addr);
        tv_desc = findViewById(R.id.tv_desc);
        iv_student_card = findViewById(R.id.iv_student_card);
        iv_student_card_include_info = findViewById(R.id.iv_student_card_include_info);
        tv_agree = findViewById(R.id.tv_agree);
        tv_disagree = findViewById(R.id.tv_disagree);
        ll_bottom_btn = findViewById(R.id.ll_bottom_btn);
        tv_agree.setOnClickListener(this);
        tv_disagree.setOnClickListener(this);
        iv_student_card.setOnClickListener(this);
        iv_student_card_include_info.setOnClickListener(this);
    }


    private void initDate() {
        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), iv_user_icon);
        tv_user_name.setText(baseUser.getNickName());
        tv_user_id.setText("ID:" + baseUser.getUserId());
        tv_phone_number.setText("手机号：" + applyClert.getPhoneNumber());
        tv_addr.setText("地址：" + applyClert.getAddr());
        tv_desc.setText("自我描述：" + applyClert.getSelfDesc());
        Utils.ImageViewDisplayByUrl(applyClert.getCardFrontImg(), iv_student_card);
        Utils.ImageViewDisplayByUrl(applyClert.getCardBackImg(), iv_student_card_include_info);
        if (applyClert.getStatus() == 2 || applyClert.getStatus() == 3) {
            type = 1;
        }
        if (type == 1) {
            ll_bottom_btn.setVisibility(View.INVISIBLE);
            titleFragment.setTitleView("店员简介");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_agree:
                getStoreListPresenter().authClertApply(userId, applyId, 1);
                break;
            case R.id.tv_disagree:
                getStoreListPresenter().authClertApply(userId, applyId, 2);
                break;
            case R.id.iv_student_card:
                UserIconDialog.newInstance(applyClert.getCardFrontImg()).show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.iv_student_card_include_info:
                UserIconDialog.newInstance(applyClert.getCardBackImg()).show(getSupportFragmentManager(), "dialog");
                break;
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDCLERTAPPLY:
                if (object instanceof FindClertApplyResponse) {
                    FindClertApplyResponse findClertApplyResponse = (FindClertApplyResponse) object;
                    if (findClertApplyResponse.getCode() == 200) {
                        applyClert = findClertApplyResponse.getApplyClert();
                        baseUser = applyClert.getBaseUser();
                        initDate();
                    } else {
                        Utils.showToastShortTime(findClertApplyResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_AUTHCLERTAPPLY:
                if (object instanceof BaseResponse) {
                    BaseResponse baseRespons = (BaseResponse) object;
                    Utils.showToastShortTime(baseRespons.getMsg());
                    finish();
                }
                break;
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("申请店员");
    }

}
