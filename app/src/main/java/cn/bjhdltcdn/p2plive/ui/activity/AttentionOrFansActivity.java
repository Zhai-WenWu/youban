package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AttenttionEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAttentionOrFollowerListResponse;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.AttentionOrFansListAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Created by ZHUDI on 2017/12/2.
 * 关注或粉丝
 */

public class AttentionOrFansActivity extends BaseActivity implements BaseView {
    private int type = 0;//0关注 1粉丝
    private RecyclerView recyclerView;
    private TextView emptyTextView;
    private UserPresenter userPresenter;
    private int pageNumber = 1, pageSize = 10, total;
    private AttentionOrFansListAdapter attentionOrFansListAdapter;
    private TwinklingRefreshLayout refreshLayout;
    private View emptyView;
    private boolean needShowLoading = true;
    private long toUserId;
    private TitleFragment titleFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attentionorfans);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        userPresenter = new UserPresenter(this);
        EventBus.getDefault().register(this);
        init();
        setTitle();
        setRequestByType();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView);
        emptyTextView = findViewById(R.id.empty_textView);
        attentionOrFansListAdapter = new AttentionOrFansListAdapter(this);
        recyclerView.setAdapter(attentionOrFansListAdapter);
        // 刷新框架
        refreshLayout = findViewById(R.id.refresh_layout_view);
        emptyView = findViewById(R.id.empty_view);

        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNumber = 1;
                refreshLayout.finishRefreshing();
                setRequestByType();

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNumber++;
                refreshLayout.finishLoadmore();
                setRequestByType();
            }
        });

    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETATTENTIONORFOLLOWERLIST:
                if (object instanceof GetAttentionOrFollowerListResponse) {
                    GetAttentionOrFollowerListResponse getAttentionOrFollowerListResponse = (GetAttentionOrFollowerListResponse) object;
                    if (getAttentionOrFollowerListResponse.getCode() == 200) {
                        emptyTextView.setText(getAttentionOrFollowerListResponse.getBlankHint());
                        total = getAttentionOrFollowerListResponse.getTotal();
                        if (pageNumber == 1) {
                            attentionOrFansListAdapter.setData(getAttentionOrFollowerListResponse.getList());
                        } else {
                            attentionOrFansListAdapter.getList_user().addAll(getAttentionOrFollowerListResponse.getList());
                            attentionOrFansListAdapter.notifyDataSetChanged();
                        }
                        if (attentionOrFansListAdapter.getList_user().size() < getAttentionOrFollowerListResponse.getTotal()) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(true);
                            //finishView.setVisibility(View.VISIBLE);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(false);
                            //finishView.setVisibility(View.GONE);
                        }
                        if (getAttentionOrFollowerListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);

                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    attentionOrFansListAdapter.notifyDataSetChanged();
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
                break;
        }
    }

    /**
     * 关注状态改变
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAttentionEvent(AttenttionEvent event) {
        if (event != null) {
            userPresenter.attentionOperation(event.getType(), SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), event.getUserId());
        }
    }

    /**
     * 根据type判断请求关注还是粉丝列表
     */
    private void setRequestByType() {

        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);
        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        if (type == 0) {
            if (toUserId == 0) {
                titleFragment.setTitle(R.string.title_attention);
            } else {
                titleFragment.setTitle("Ta的关注");
            }
        } else if (type == 1) {
            if (toUserId == 0) {
                titleFragment.setTitle(R.string.title_fans);
            } else {
                titleFragment.setTitle("Ta的粉丝");
            }
        }

        if (toUserId == 0) {
            toUserId = userId;
        }

        userPresenter.getAttentionOrFollowerList(type, userId, toUserId, pageSize, pageNumber);
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
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
        EventBus.getDefault().unregister(this);
    }
}
