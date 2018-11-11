package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyTransactionResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.TransactionRecordListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class TransactionRecordListActivity extends BaseActivity implements BaseView {

    private RecyclerView rvRecordlist;
    private UserPresenter userPresenter;
    private int pageNumber = 1, pageSize = 20;
    private TransactionRecordListAdapter orgainRecyclerAapter;
    private View emptyView;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_recordlist);
        setTitle();
        initView();
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("交易记录");
    }

    private void initView() {
        userPresenter = new UserPresenter(this);
        userPresenter.myTransactionRecordList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);
        emptyView = findViewById(R.id.empty_view);
        emptyTextView = findViewById(R.id.empty_textView);
        rvRecordlist = findViewById(R.id.rv_record_list);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
        rvRecordlist.addItemDecoration(linearLayoutSpaceItemDecoration);
        orgainRecyclerAapter = new TransactionRecordListAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvRecordlist.setHasFixedSize(true);
        rvRecordlist.setLayoutManager(linearLayoutManager);
        rvRecordlist.setAdapter(orgainRecyclerAapter);
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETTRANSACTIONRECORDLIST:
                if (object instanceof GetMyTransactionResponse) {
                    GetMyTransactionResponse myTransactionResponse = (GetMyTransactionResponse) object;
                    if (myTransactionResponse.getCode() == 200) {
                        emptyTextView.setText(myTransactionResponse.getBlankHint());
                        if (myTransactionResponse.getTotal() > 0) {
                            emptyView.setVisibility(View.GONE);
                        }
                        orgainRecyclerAapter.setList(myTransactionResponse.getList());
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
