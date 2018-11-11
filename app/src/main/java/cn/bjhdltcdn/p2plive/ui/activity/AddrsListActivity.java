package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateAddrsListEvent;
import cn.bjhdltcdn.p2plive.event.UpdateGoodsPayAddressEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetH5ReceiptAddressListResponse;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AddrsRecycleViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 选择收货地址列表
 */
public class AddrsListActivity extends BaseActivity implements BaseView {
    private AddrsRecycleViewAdapter adapter;
    private long userId;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private int pageNumber = 1;
    private int pageSize = 50;
    private GetStoreListPresenter presenter;
    private int position;
    private AddressInfo addressInfo;
    private View emptyView;
    private TextView emptyTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrs_list_layout);
        EventBus.getDefault().register(this);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        presenter=new GetStoreListPresenter(this);
        setTitle();
        initView();
        presenter.getH5ReceiptAddressList(userId,pageSize,pageNumber);
    }


    public void initView() {
        emptyView=findViewById(R.id.empty_view);
        emptyTextView=findViewById(R.id.empty_textView);
        refreshLayout=findViewById(R.id.refresh_layout_view);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableRefresh(false);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        adapter = new AddrsRecycleViewAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnEditClick(new AddrsRecycleViewAdapter.OnEditClick() {
            @Override
            public void onEdit(int position, long addressId) {
                Intent intent=new Intent(AddrsListActivity.this, AddAddrsActivity.class);
                intent.putExtra("addressInfo",adapter.getItem(position));
                startActivity(intent);
            }

            @Override
            public void onDefault(AddressInfo address,int pos) {
                position=pos;
                addressInfo=address;
                presenter.updateH5ReceiptAddress(userId,address);
            }
        });

    }


    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_addrs_list);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setRightViewTitle("新增地址", new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                Intent intent=new Intent(AddrsListActivity.this, AddAddrsActivity.class);
                startActivity(intent);
            }
        });
        titleFragment.setRightViewColor(R.color.color_ffb700);
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        switch (apiName) {
            case InterfaceUrl.URL_GETH5RECEIPTADDRESSLIST:
                if (object instanceof GetH5ReceiptAddressListResponse) {
                    GetH5ReceiptAddressListResponse response = (GetH5ReceiptAddressListResponse) object;
                    if (response.getCode() == 200) {
                        List<AddressInfo> addressInfoList=response.getList();
                        if(addressInfoList!=null&addressInfoList.size()>0)
                        {
                            emptyView.setVisibility(View.GONE);
                            adapter.setAddressInfoList(addressInfoList);
                            adapter.notifyDataSetChanged();
                        }else{
                            emptyView.setVisibility(View.VISIBLE);
                            emptyTextView.setText(response.getBlankHint());
                        }
                    }else{
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEH5RECEIPTADDRESS:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                    if(response.getCode()==200){
                        adapter.getAddressInfoList().remove(position);
                        adapter.getAddressInfoList().add(0,addressInfo);
                        adapter.notifyDataSetChanged();
                        EventBus.getDefault().post(new UpdateGoodsPayAddressEvent(addressInfo));
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateAddrsListEvent event) {
        if (event == null) {
            return;
        }
        //刷新地址列表
        presenter.getH5ReceiptAddressList(userId,pageSize,pageNumber);
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
