package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateSelectPersonNumListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetFriendListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyPropsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SendActivityInviationResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveActivePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.BaseActivity;
import cn.bjhdltcdn.p2plive.ui.activity.WithdrawalsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.AddGroupAdministratorsAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.MyPropsListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.SendInviationSelectPersonListAdapter;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
/**
 * Created by huwenhua on 2016/6/1.
 * 指定人 校友fragment
 */
public class SendInviationSelectAlumnusFragment extends BaseFragment implements BaseView {
    private BaseActivity mActivity;
    private View rootView;
    private final int pageSize = 20;
    private int pageNumber = 1;
    private SendInviationSelectPersonListAdapter sendInviationSelectPersonListAdapter;
    private SaveActivePresenter mPresenter;
    private RecyclerView recyclerView;
    private View emptyView;
    private TextView empty_tv;
    private TextView finishView;
    private TwinklingRefreshLayout refreshLayout;
    private long userId;
    private List<Long> hobbyIds,selectAlumnusPerson;
    private int sexLimit;
    private int sendNumber;
    private int type=1;
    private int maxSelectNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_send_inviation_select_person, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (BaseActivity) context;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
        if(sendInviationSelectPersonListAdapter!=null){
            sendInviationSelectPersonListAdapter.setMaxSelectNum(maxSelectNum);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId= SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        hobbyIds= (List<Long>) mActivity.getIntent().getSerializableExtra(Constants.Fields.HOBBY_IDS);
        sexLimit = mActivity.getIntent().getIntExtra(Constants.Fields.SEX_LIMIT, 0);
        sendNumber = mActivity.getIntent().getIntExtra(Constants.Fields.SEND_NUMBER, 0);
        selectAlumnusPerson= (List<Long>) mActivity.getIntent().getSerializableExtra(Constants.Fields.SELECT_ALUMNUS_PERSON);
        init();
    }

    private void init() {
        recyclerView = rootView.findViewById(R.id.recycle_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        empty_tv = rootView.findViewById(R.id.empty_textView);
        finishView=rootView.findViewById(R.id.finish_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(0.5f));
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.addItemDecoration(linearLayoutSpaceItemDecoration);

        sendInviationSelectPersonListAdapter = new SendInviationSelectPersonListAdapter();
        sendInviationSelectPersonListAdapter.setMaxSelectNum(maxSelectNum);
        recyclerView.setAdapter(sendInviationSelectPersonListAdapter);

        // 刷新框架
        refreshLayout = rootView.findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(getContext());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                requestData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNumber++;
                refreshLayout.finishLoadmore();
                requestData();
            }
        });


    }


    @Override
    protected void onVisible(boolean isInit) {
        if (isInit) {
            userId= SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            hobbyIds= (List<Long>) mActivity.getIntent().getSerializableExtra(Constants.Fields.HOBBY_IDS);
            sexLimit = mActivity.getIntent().getIntExtra(Constants.Fields.SEX_LIMIT, 0);
            sendNumber = mActivity.getIntent().getIntExtra(Constants.Fields.SEND_NUMBER, 0);
            requestData();
        }
    }

    public SaveActivePresenter getmPresenter() {
        if (mPresenter == null) {
            mPresenter = new SaveActivePresenter(this);
        }
        return mPresenter;
    }


    private void requestData() {
        getmPresenter().sendActivityInvitation(userId,0L,hobbyIds,sexLimit,sendNumber,type,1,null,pageSize,pageNumber);
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_SENDACTIVITYINVITATION:
                if (object instanceof SendActivityInviationResponse) {
                    SendActivityInviationResponse sendActivityInviationResponse = (SendActivityInviationResponse) object;
                    if (sendActivityInviationResponse.getCode() == 200) {
                        List<BaseUser> list = sendActivityInviationResponse.getList();
                        if (list != null) {
                            if (pageNumber == 1) {
                                refreshLayout.finishRefreshing();
                                sendInviationSelectPersonListAdapter.setBaseUserList(list);
                                sendInviationSelectPersonListAdapter.setBeforeSelectIdsList(selectAlumnusPerson);
                            } else {
                                refreshLayout.finishLoadmore();
                                sendInviationSelectPersonListAdapter.addList(list);
                                sendInviationSelectPersonListAdapter.setBeforeSelectIdsList(selectAlumnusPerson);
                            }
                            sendInviationSelectPersonListAdapter.notifyDataSetChanged();
                            if (sendActivityInviationResponse.getTotal() <= pageNumber * pageSize) {
                                //没有更多数据时  下拉刷新不可用
                                refreshLayout.setEnableLoadmore(false);
                                finishView.setVisibility(View.VISIBLE);
                            } else {
                                //有更多数据时  下拉刷新才可用
                                refreshLayout.setEnableLoadmore(true);
                                finishView.setVisibility(View.GONE);
                            }
                            if (sendActivityInviationResponse.getTotal() == 0) {
                                emptyView.setVisibility(View.VISIBLE);
                                emptyView.findViewById(R.id.empty_image).setVisibility(View.GONE);
                                finishView.setVisibility(View.GONE);
                                empty_tv.setText(sendActivityInviationResponse.getBlankHint());
                            } else {
                                emptyView.setVisibility(View.GONE);
                            }
                        }
                    }
                }
                break;
        }
    }

    public List<Long> getSelectPerson(){
        if(sendInviationSelectPersonListAdapter!=null){
            List<Long> selectIdsList=sendInviationSelectPersonListAdapter.getSelectIdsList();
            return selectIdsList;
        }else {
            return  new ArrayList<Long>();
        }
    }


    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(mActivity);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdateSelectPersonNumListEvent event) {
        if (event == null) {
            return;
        }
        sendInviationSelectPersonListAdapter.setSelectNum(event.getNum());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (sendInviationSelectPersonListAdapter != null) {
            sendInviationSelectPersonListAdapter = null;
        }

        if (mPresenter != null) {
            mPresenter = null;
        }

        if (rootView != null) {
            ((ViewGroup) rootView).removeAllViews();
            rootView.destroyDrawingCache();
            rootView = null;
        }
    }
}
