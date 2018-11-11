//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AttenttionResultEvent;
//import cn.bjhdltcdn.p2plive.event.UpdatePayResultEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.MyAccountResponse;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * Created by ZHUDI on 2017/12/6.
// */
//
//public class MyAccountActivity extends BaseActivity implements BaseView {
//    private ImageView img_rank;
//    private TextView tv_num_recharge, tv_recharge, tv_num_balance, tv_transaction_record, tv_level,
//            tv_level_next, tv_num_experience, tv_num_experience_next;
//    private ProgressBar progressBar;
//    private UserPresenter userPresenter;
//    private int pageNumber, pageSize;
//    private boolean needShowLoading = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_myaccount);
//        EventBus.getDefault().register(this);
//        userPresenter = new UserPresenter(this);
//        setTitle();
//        init();
//        userPresenter.myAccount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//    }
//
//    private void init() {
//        img_rank = findViewById(R.id.img_rank);
//        tv_num_recharge = findViewById(R.id.tv_num_recharge);
//        tv_recharge = findViewById(R.id.tv_recharge);
//        tv_num_balance = findViewById(R.id.tv_num_balance);
//        tv_transaction_record = findViewById(R.id.tv_transaction_record);
//        tv_level = findViewById(R.id.tv_level);
//        tv_level_next = findViewById(R.id.tv_level_next);
//        tv_num_experience = findViewById(R.id.tv_num_experience);
//        tv_num_experience_next = findViewById(R.id.tv_num_experience_next);
//        progressBar = findViewById(R.id.progressBar);
//        tv_recharge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //调到支付界面
//                startActivity(new Intent(MyAccountActivity.this, ReChargeListActivity.class));
//            }
//        });
//        tv_transaction_record.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MyAccountActivity.this, TransactionRecordListActivity.class));
//                userPresenter.myTransactionRecordList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);
//            }
//        });
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        switch (apiName) {
//            case InterfaceUrl.URL_MYACCOUNT:
//                if (object instanceof MyAccountResponse) {
//                    MyAccountResponse myAccountResponse = (MyAccountResponse) object;
//                    if (myAccountResponse.getCode() == 200) {
//                        int userLevel = myAccountResponse.getUserLevel();
//                        TypedArray levelZeroIcon = App.getInstance().getResources().obtainTypedArray(R.array.user_level_icon);
//                        Drawable userlevelImage = levelZeroIcon.getDrawable(userLevel);
//                        levelZeroIcon.recycle();
//                        img_rank.setImageDrawable(userlevelImage);
//                        //累计充值
//                        String rechargeStr = String.format(getResources().getString(R.string.str_num_recharge), myAccountResponse.getPayAmount());
//                        tv_num_recharge.setText(rechargeStr);
//                        //余额
//                        String balanceStr = String.format(getResources().getString(R.string.str_num_balance), myAccountResponse.getUserBalance());
//                        tv_num_balance.setText(balanceStr);
//                        tv_level.setText("VIP" + userLevel);
//                        tv_level_next.setText("VIP" + (userLevel + 1));
//                        //当前经验值
//                        int currentLevelValue = myAccountResponse.getCurrentLevelValue();
//                        String experienceStr = String.format(getResources().getString(R.string.str_num_experience), currentLevelValue);
//                        tv_num_experience.setText(experienceStr);
//                        //下一等级经验值
//                        int nextLevelValue = myAccountResponse.getNextLevelValue();
//                        int experienceNext = nextLevelValue - currentLevelValue;
//                        String experienceNextStr = String.format(getResources().getString(R.string.str_num_experience_next), experienceNext);
//                        tv_num_experience_next.setText(experienceNextStr);
//                        progressBar.setMax(nextLevelValue);
//                        progressBar.setProgress(currentLevelValue);
//                    }
//                }
//                break;
//
//        }
//    }
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//        titleFragment.setTitleView("我的账户");
//
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onAttentionEvent(UpdatePayResultEvent event) {
//        if (event != null) {
//            userPresenter.myAccount(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
