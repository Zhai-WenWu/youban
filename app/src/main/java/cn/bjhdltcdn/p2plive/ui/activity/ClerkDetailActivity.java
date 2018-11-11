package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.FindClertDetailResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ClertDetail;
import cn.bjhdltcdn.p2plive.model.ClertInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.ClerkDetailAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Created by zhaiww on 2018/5/22.
 */

public class ClerkDetailActivity extends BaseActivity implements BaseView {

    private ToolBarFragment titleFragment;
    private GetStoreListPresenter getStoreListPresenter;
    private long userId;
    private int pageNum = 1;
    private int pageSize = 10;
    private TextView empty_tv;
    private TwinklingRefreshLayout refreshLayout;
    private RecyclerView rv_clerk_detail;
    private View empty_view;
    private ClerkDetailAdapter clerkDetailAdapter;
    public List<ProductOrder> list = new ArrayList<>();
    public int mType = 1;
    private long storeId;
    private long toUserid;
    private int type;
    private ImageView iv_user_icon;
    private TextView tv_user_name;
    private TextView tv_user_id;
    private TextView tv_phone_num;
    private TextView tv_school;
    private TextView tv_time;
    private TextView tv_addr;
    private RadioGroup radio_group;
    private RadioButton rb_day;
    private RadioButton rb_month;
    private RadioButton rb_all;
    private FindClertDetailResponse findClertDetailResponse;
    private ClertDetail clertDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clerk_detail);
        type = getIntent().getIntExtra(Constants.Fields.COME_IN_TYPE, 0);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        storeId = getIntent().getLongExtra(Constants.Fields.STORE_ID, 0);
        toUserid = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
        getStoreListPresenter = new GetStoreListPresenter(this);
        getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, 1);
        setTitle();
        initView();

    }

    private void initView() {
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_user_name = findViewById(R.id.tv_user_name);
        tv_user_id = findViewById(R.id.tv_user_id);
        tv_phone_num = findViewById(R.id.tv_phone_num);
        tv_school = findViewById(R.id.tv_school);
        tv_time = findViewById(R.id.tv_time);
        tv_addr = findViewById(R.id.tv_addr);
        radio_group = findViewById(R.id.radio_group);
        rb_day = findViewById(R.id.rb_day);
        rb_month = findViewById(R.id.rb_month);
        rb_all = findViewById(R.id.rb_all);
        rv_clerk_detail = findViewById(R.id.rv_clerk_detail);
        rv_clerk_detail.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        rv_clerk_detail.setLayoutManager(itemLinearlayoutManager);
        clerkDetailAdapter = new ClerkDetailAdapter(this);
        rv_clerk_detail.setAdapter(clerkDetailAdapter);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(2));
        rv_clerk_detail.addItemDecoration(linearLayoutSpaceItemDecoration);
        empty_view = findViewById(R.id.empty_view);
        empty_tv = empty_view.findViewById(R.id.empty_textView);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        LoadingView loadingView = new LoadingView(getApplicationContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(80);
        refreshLayout.setTargetView(rv_clerk_detail);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, mType);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, mType);
            }
        });
        empty_view.setVisibility(View.GONE);

        rb_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 1;
                getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, mType);
            }
        });
        rb_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 2;
                getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, mType);
            }
        });
        rb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mType = 3;
                getStoreListPresenter.findClertDetail(userId, toUserid, storeId, pageSize, pageNum, mType);
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_FINDCLERTDETAIL:
                if (object instanceof FindClertDetailResponse) {
                    findClertDetailResponse = (FindClertDetailResponse) object;
                    if (findClertDetailResponse.getCode() == 200) {

                        initTopDate();

                        if (pageNum == 1) {
                            clerkDetailAdapter.setmList(findClertDetailResponse.getList());
                        } else {
                            clerkDetailAdapter.addmList(findClertDetailResponse.getList());
                        }

                        clerkDetailAdapter.notifyDataSetChanged();

                        if (!TextUtils.isEmpty(findClertDetailResponse.getBlankHint())) {
                            empty_tv.setText(findClertDetailResponse.getBlankHint());
                        }

                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();

                        findClertDetailResponse.setTotal(findClertDetailResponse.getList().size());

                        if (findClertDetailResponse.getTotal() == 0) {
                            empty_view.setVisibility(View.VISIBLE);
                        } else {
                            empty_view.setVisibility(View.GONE);
                        }

                        if (findClertDetailResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                        }
                    } else {
                        Utils.showToastShortTime(findClertDetailResponse.getMsg());
                    }
                }
                break;
        }
    }

    private void initTopDate() {
        clertDetail = findClertDetailResponse.getClertDetail();
        ClertInfo clertInfo = clertDetail.getClertInfo();
        if (clertInfo != null) {
            tv_phone_num.setText("手机号：" + clertInfo.getPhoneNumber());
            tv_time.setText("入职时间：" + clertInfo.getAddTime());
            tv_addr.setText("地址：" + clertInfo.getAddr());
            tv_school.setText(clertDetail.getSchoolName());
            final BaseUser baseUser = clertInfo.getBaseUser();
            if (baseUser != null) {
                Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), iv_user_icon);
                tv_user_name.setText(baseUser.getNickName());
                tv_user_id.setText("ID:" + baseUser.getUserId());
            }
            iv_user_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ClerkDetailActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                }
            });
        }
        rb_day.setText(String.format(App.getInstance().getResources().getString(R.string.str_day_num), clertDetail.getDayReceiptNum()));
        rb_month.setText(String.format(App.getInstance().getResources().getString(R.string.str_mouth_num), clertDetail.getMonthReceiptNum()));
        rb_all.setText(String.format(App.getInstance().getResources().getString(R.string.str_all_num), clertDetail.getTotalReceiptNum()));
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("店员详情");

        if (type == 1) {
            titleFragment.setRightView("店员简介", R.color.color_ffb700, new ToolBarFragment.ViewOnclick() {
                @Override
                public void onClick() {
                    Intent intent = new Intent(ClerkDetailActivity.this, ApplyClerkForMasterActivity.class);
                    intent.putExtra(Constants.Fields.APPLY_ID, clertDetail.getClertInfo().getApplyId());
                    intent.putExtra(Constants.Fields.TYPE, 1);
                    startActivity(intent);
                }
            });
        }
    }
}
