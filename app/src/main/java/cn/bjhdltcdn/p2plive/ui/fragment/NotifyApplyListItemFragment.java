package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.greendao.PushdbModelDao;
import cn.bjhdltcdn.p2plive.httpresponse.AuditeApplyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.AuditeGroupApplyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetApplyListResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupApply;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.OrganApply;
import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.NotifyApplyListItemAdapter;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

import static cn.bjhdltcdn.p2plive.ui.adapter.NotifyApplyListItemAdapter.GROUP_ITEM_7;
import static cn.bjhdltcdn.p2plive.ui.adapter.NotifyApplyListItemAdapter.ORGANIZATION_ITEM_6;

/**
 *
 */
public class NotifyApplyListItemFragment extends BaseFragment implements BaseView {


    private View rootView;

    private RecyclerView recycleView;
    private NotifyApplyListItemAdapter adapter;

    private View emptyView;

    private int pageNumber = 1;
    private int pageSize = 20;
    /**
     * 1 通知
     * 2 回复
     * 3 待处理
     */
    private int type;

    /**
     * 群：1同意，2拒绝
     * 圈子：2同意，3拒绝
     */
    private int optionsType;


    private GroupPresenter groupPresenter;
    private int selectPosition = -1;
    private boolean needShowLoading = true;

    public GroupPresenter getGroupPresenter() {
        if (groupPresenter == null) {
            groupPresenter = new GroupPresenter(this);
        }
        return groupPresenter;
    }

    private AssociationPresenter associationPresenter;

    public AssociationPresenter getAssociationPresenter() {
        if (associationPresenter == null) {
            associationPresenter = new AssociationPresenter(this);
        }
        return associationPresenter;
    }


    public static NotifyApplyListItemFragment newInstance(int type) {
        NotifyApplyListItemFragment fragment = new NotifyApplyListItemFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.KEY.KEY_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(Constants.KEY.KEY_TYPE, 0);
        }

        Logger.d("type ====>>>>> " + type);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_notify_list_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        pageNumber = 1;
        getGroupPresenter().getApplyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);

        /**
         * 圈子：申请
         * 群：申请
         */
        String sql = "UPDATE " + PushdbModelDao.TABLENAME + " SET " + " UN_READ = 1 WHERE MESSAGE_TYPE IN(10000,30001) ";
        // 更新未读数
        GreenDaoUtils.getInstance().updatePushdbModelBySq(sql);
        EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());

    }

    private void initView() {

        recycleView = rootView.findViewById(R.id.recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setAutoMeasureEnabled(true);
        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1), true));
        recycleView.setLayoutManager(layoutManager);

        adapter = new NotifyApplyListItemAdapter();
        recycleView.setAdapter(adapter);
        adapter.setUserIconClickListener(new NotifyApplyListItemAdapter.UserIconClickListener() {
            @Override
            public void onClick(BaseUser baseUser, int type) {
                if (baseUser != null && type != 3) {
                    startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                }
            }
        });

        adapter.setApplyOptionsClickListener(new NotifyApplyListItemAdapter.ApplyOptionsClickListener() {
            @Override
            public void onClick(int optionsType, long applyId, long userId, int position) {
                selectPosition = position;

                switch (optionsType) {
                    case 1:
                    case 2:// 群： 1同意,2拒绝
                        NotifyApplyListItemFragment.this.optionsType = optionsType;
                        getGroupPresenter().auditeGroupApply(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), applyId, optionsType);
                        break;

                    case 3:
                    case 4:// 圈子：2同意,3拒绝
                        break;
                }
            }
        });

        // 刷新框架
        TwinklingRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout_view);

        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(App.getInstance());
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(recycleView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                getGroupPresenter().getApplyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                getGroupPresenter().getApplyList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), pageSize, pageNumber);

            }
        });


    }

    /**
     * 提示语
     *
     * @param isVisibility
     * @param blankHint
     */
    private void initEmptyView(boolean isVisibility, String blankHint) {
        if (emptyView == null) {
            emptyView = rootView.findViewById(R.id.empty_view);
        }

        if (!StringUtils.isEmpty(blankHint)) {
            TextView textView = emptyView.findViewById(R.id.empty_textView);
            textView.setText(blankHint);
        }


        emptyView.setVisibility(isVisibility ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (!isAdded()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETAPPLYLIST.equals(apiName)) {
            if (object instanceof GetApplyListResponse) {
                GetApplyListResponse response = (GetApplyListResponse) object;

                if (response.getCode() == 200) {
                    List<HomeInfo> homeInfoList = response.getList();
                    if (homeInfoList != null) {
                        if (pageNumber == 1) {
                            adapter.setList(response.getList());
                        } else {
                            adapter.addList(response.getList());
                        }
                    }
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

                initEmptyView(adapter.getItemCount() == 0 ? true : false, response.getBlankHint());

            }
        } else if (InterfaceUrl.URL_AUDITEAPPLY.equals(apiName)) {
            if (object instanceof AuditeApplyResponse) {
                AuditeApplyResponse response = (AuditeApplyResponse) object;
                Utils.showToastShortTime(response.getMsg());
                if (response.getCode() == 200) {
                    optionsType = response.getStatus();
                    updateItem();

                }

                selectPosition = -1;
                optionsType = -1;
            }
        } else if (InterfaceUrl.URL_AUDITEGROUPAPPLY.equals(apiName)) {
            if (object instanceof BaseResponse) {
                AuditeGroupApplyResponse response = (AuditeGroupApplyResponse) object;
                Utils.showToastShortTime(response.getMsg());

                if (response.getCode() == 200) {
                    optionsType = response.getStatus();
                    updateItem();
                }

                selectPosition = -1;
                optionsType = -1;


            }
        }

    }


    private void updateItem() {

        if (selectPosition < 0) {
            return;
        }

        HomeInfo homeInfo = adapter.getList().get(selectPosition);
        if (homeInfo != null) {
            //类型(1帖子,2圈子,3活动,4房间,5PK挑战,6圈子申请,7群组申请)
            switch (homeInfo.getType()) {

                case ORGANIZATION_ITEM_6:

                    OrganApply organApply = homeInfo.getOrganApply();
                    if (organApply != null) {
                        organApply.setStatus(optionsType);
                    }
                    adapter.notifyItemChanged(selectPosition);
                    break;

                case GROUP_ITEM_7:
                    GroupApply groupApply = homeInfo.getGroupApply();
                    if (groupApply != null) {
                        groupApply.setStatus(optionsType);
                    }
                    adapter.notifyItemChanged(selectPosition);

                    break;
            }
        }
    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());

    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }
}
