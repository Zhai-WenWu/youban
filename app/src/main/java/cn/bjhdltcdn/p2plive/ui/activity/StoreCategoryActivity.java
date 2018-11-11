package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetServiceInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetShopLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.model.UseTypeInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PayPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.RechargeListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.StoreCategoryFirstRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by Hu_PC on 2018/8/3.
 */

public class StoreCategoryActivity extends BaseActivity implements BaseView {
    private RecyclerView recyclerView;
    private StoreCategoryFirstRecycleAdapter storeCategoryFirstRecycleAdapter;
    private GetStoreListPresenter getStoreListPresenter;
    private long userId;
    private TextView okTextView,rulesTextView;
    private CheckBox rulesCheckBox;
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_category_layout);
        userId= SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        storeId=getIntent().getLongExtra(Constants.Fields.STORE_ID,0);
        setTitle();
        initView();
        getStoreListPresenter = new GetStoreListPresenter(this);
        getStoreListPresenter.getShopLabelList(userId);
    }

    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.label_first_recycle_view);
        storeCategoryFirstRecycleAdapter = new StoreCategoryFirstRecycleAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(storeCategoryFirstRecycleAdapter);
        recyclerView.setHasFixedSize(true);
        rulesCheckBox=findViewById(R.id.rules_checkbox);
        rulesTextView=findViewById(R.id.rules_view);
        rulesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StoreCategoryActivity.this, WXPayEntryActivity.class);
                intent.putExtra(Constants.Action.ACTION_BROWSE, 4);
                startActivity(intent);
            }
        });
        okTextView=findViewById(R.id.ok_text_view);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!rulesCheckBox.isChecked()){
                    Utils.showToastShortTime("请先同意“已阅读并执行此规章制度”");
                    return;
                }
                FirstLabelInfo firstLabelInfo=storeCategoryFirstRecycleAdapter.getFirstLabelInfo();
                if(firstLabelInfo.getList()==null||firstLabelInfo.getList().size()<=0){
                    Utils.showToastShortTime("请选择店铺类型");
                    return;
                }
                getStoreListPresenter.saveUserStoreLabel(userId,firstLabelInfo);
            }
        });
    }


    private void setTitle() {
        TitleFragment fragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        fragment.setTitle(R.string.title_shop_category);
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETSHOPLABELLIST)) {
            if (object instanceof GetShopLabelListResponse) {
                final GetShopLabelListResponse getShopLabelListResponse = (GetShopLabelListResponse) object;
                if (getShopLabelListResponse.getCode() == 200) {
                            storeCategoryFirstRecycleAdapter.setList(getShopLabelListResponse.getList());
                            storeCategoryFirstRecycleAdapter.notifyDataSetChanged();
//                    List<FirstLabelInfo> firstLabelInfoList=new ArrayList<FirstLabelInfo>();
//                    for (int i=0;i<10;i++){
//                        FirstLabelInfo firstLabelInfo=new FirstLabelInfo();
//                        firstLabelInfo.setFirstLabelId(i+1);
//                        firstLabelInfo.setFirstLabelName("一级标签"+i);
//                        List<SecondLabelInfo> secondLabelInfoList=new ArrayList<SecondLabelInfo>();
//                        for (int j=0;j<10;j++){
//                            SecondLabelInfo secondLabelInfo=new SecondLabelInfo();
//                            secondLabelInfo.setSecondLabelId(1+j);
//                            secondLabelInfo.setSecondLabelName("二级标签"+i+j);
//                            secondLabelInfoList.add(secondLabelInfo);
//                        }
//                        firstLabelInfo.setList(secondLabelInfoList);
//                        firstLabelInfoList.add(firstLabelInfo);
//                    }
//                    storeCategoryFirstRecycleAdapter.setList(firstLabelInfoList);
//                    storeCategoryFirstRecycleAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToastShortTime(getShopLabelListResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_SAVEUSERSTORELABEL)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                if (baseResponse.getCode() == 200) {
                    getStoreListPresenter.judgeCreateStoreAuth(userId);
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_JUDGECREATESTOREAUTH)) {
            if (object instanceof JudgeCreateStoreAuthResponse) {
                JudgeCreateStoreAuthResponse judgeCreateStoreAuthResponse = (JudgeCreateStoreAuthResponse) object;
                if (judgeCreateStoreAuthResponse.getCode() == 200) {
                    //1 没有用户店铺标签，跳转创建店铺标签;
//                    2 没有实名认证和申请开店记录;
//                    或开店申请拒绝同时实名认证被拒绝,调用实名认证整合接口;
//                    3 未实名认证或被拒绝,申请开店审核中或通过,跳转实名认证
//                    4 实名认证审核中或通过，未申请开店或申请被拒绝，跳转申请开店;
//                    5 实名认证审核中或通过，开店申请通过或审核中，跳转创建店铺;
//                    6 已开店并且实名认证和开店申请都通过,调用店铺详情

                    int isCreateStore=judgeCreateStoreAuthResponse.getIsCreateStore();
                    long storeId=judgeCreateStoreAuthResponse.getStoreId();
                    //根据状态判断是跳转到哪个界面
                    switch (isCreateStore){
                        case 1:
                            //用户没有选择开店标签
                            Intent intent1=new Intent(StoreCategoryActivity.this,StoreCategoryActivity.class);
                            intent1.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent1);
                            break;
                        case 2:
                            //实名 开店两个都没都没申请 或者都拒绝
                            Intent intent2=new Intent(StoreCategoryActivity.this,RealNameCertificationAndShopActivity.class);
                            intent2.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent2);
                            break;
                        case 3:
                            //跳转实名认证
                            Intent intent3=new Intent(StoreCategoryActivity.this,RealNameCertificationActivity.class);
                            intent3.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent3);
                            break;
                        case 4:
                            //跳转申请开店界面
                            Intent intent4=new Intent(StoreCategoryActivity.this, ApplyForShopActivity.class);
                            intent4.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent4);
                            break;
                        case 5:
                            //创建店铺
                            Intent intent5=new Intent(StoreCategoryActivity.this,CreateShopActivity.class);
                            startActivity(intent5);
                            break;
                        case 6:
                            //店铺详情
                            Intent intent6=new Intent(StoreCategoryActivity.this,ShopDetailActivity.class);
                            intent6.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent6);
                            break;
                    }
                    finish();
                } else {
                    Utils.showToastShortTime(judgeCreateStoreAuthResponse.getMsg());
                }
            }
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



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
