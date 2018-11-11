package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.FindClertApplyResponse;
import cn.bjhdltcdn.p2plive.model.ApplyClert;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by zhaiww on 2018/5/17.
 */

public class ApplyStaffForMasterActivity extends BaseActivity implements BaseView {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_staff_for_master);
        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getStoreListPresenter().findClertApply(userId, 0);
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
    }


    private void initDate() {

    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDCLERTAPPLY:
                if (object instanceof FindClertApplyResponse) {
                    FindClertApplyResponse findClertApplyResponse = (FindClertApplyResponse) object;
                    if (findClertApplyResponse.getCode() == 200) {
                        applyClert = findClertApplyResponse.getApplyClert();
                        initDate();
                    } else {
                        Utils.showToastShortTime(findClertApplyResponse.getMsg());
                    }
                }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
