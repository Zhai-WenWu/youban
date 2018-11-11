package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetServiceInfoResponse;
import cn.bjhdltcdn.p2plive.model.UseTypeInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.PayPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.RechargeListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by Hu_PC on 2017/12/8.
 */

public class ReChargeListActivity extends BaseActivity implements BaseView {
    private RecyclerView recyclerView;
    private RechargeListAdapter chargeListAdapter;
    private PayPresenter payPresenter;
    private boolean needShowLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_list_layout);
        setTitle();
        initView();
        payPresenter = new PayPresenter(this);
        payPresenter.getServiceInfo();
        EventBus.getDefault().register(this);
    }

    public void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recharge_recycle_view);
        chargeListAdapter = new RechargeListAdapter();
        GridLayoutManager hotlayoutManager = new GridLayoutManager(App.getInstance(), 3);
        recyclerView.setLayoutManager(hotlayoutManager);
        recyclerView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false, null));
        recyclerView.setAdapter(chargeListAdapter);
        recyclerView.setHasFixedSize(true);
        chargeListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(ReChargeListActivity.this, WXPayEntryActivity.class);
                intent.putExtra(Constants.Fields.TYPE, 1);
                UseTypeInfo useTypeInfo = chargeListAdapter.getItem(position);
                intent.putExtra("serviceId", useTypeInfo.getServiceId());
                intent.putExtra("goldnum", useTypeInfo.getCoinsNum() + useTypeInfo.getOtherPresentation());
                intent.putExtra("money", useTypeInfo.getAmount());
                startActivity(intent);
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
        fragment.setTitle(R.string.title_recharge);
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_GETSERVICEINFO)) {
            if (object instanceof GetServiceInfoResponse) {
                GetServiceInfoResponse getServiceInfoResponse = (GetServiceInfoResponse) object;
                if (getServiceInfoResponse.getCode() == 200) {
                    chargeListAdapter.setList(getServiceInfoResponse.getServiceList());
                    chargeListAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToastShortTime(getServiceInfoResponse.getMsg());
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

    //接收充值成功消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ClosePayActivityEvent event) {
        if (event == null) {
            return;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
