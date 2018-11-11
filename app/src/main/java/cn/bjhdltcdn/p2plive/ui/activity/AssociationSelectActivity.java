//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationSelectEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetJoinOrganListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSecondTypeBySearchListResponse;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationSelectListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 选择圈子页面
// */
//public class AssociationSelectActivity extends BaseActivity implements BaseView {
//
//    private RecyclerView recyclerView;
//    private TwinklingRefreshLayout refreshLayout;
//
//    private EditText searchEdittextView;
//    private boolean isLabelSelected;
//    private AssociationSelectListAdapter adapter;
//
//    private List<OrganizationInfo> selectList;
//    private AssociationPresenter presenter;
//
//    private int pageSize = 20;
//    private int pageNumber = 1;
//
//
//    // 点击的item下标
//    private int selectPosition = -1;
//    private ToolBarFragment titleFragment;
//
//
//    /**
//     * 选择的圈子列表
//     */
//    private ArrayList<Integer> organIdList;
//    //圈子是否参赛
//    private boolean isMatch;
//    /**
//     * 最大选择数量
//     */
//    private int maxNumber = 3;
//
//    /**
//     * 是否匿名 (1匿名,2不匿名)
//     */
//    private int isAnonymous = 2;
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_select_layout);
//        Bundle bundle = getIntent().getBundleExtra(Constants.KEY.KEY_OBJECT);
//        isLabelSelected = getIntent().getBooleanExtra(Constants.KEY.ISLABELSELECTED, false);
//        isAnonymous = getIntent().getIntExtra(Constants.Fields.IS_ANONYMOUS, 0);
//        if (bundle != null) {
//            organIdList = bundle.getIntegerArrayList(Constants.KEY.KEY_OBJECT);
//            isMatch = bundle.getBoolean(Constants.Fields.IS_MATCH);
//        }
//
//        setTitle();
//
//        initView();
//
//
//    }
//
//
//    private void setTitle() {
//
//        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
//
//        titleFragment.setTitleView("选择圈子");
//
//        titleFragment.setLeftView(new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//                EventBus.getDefault().post(new AssociationSelectEvent(selectList));
//                finish();
//            }
//        });
//
//        titleFragment.setRightView("", new ToolBarFragment.ViewOnclick() {
//            @Override
//            public void onClick() {
//                EventBus.getDefault().post(new AssociationSelectEvent(selectList));
//                finish();
//            }
//        });
//
//        titleFragment.getRightView().setText((organIdList != null && organIdList.size() > 0 ? ("确定(" + organIdList.size() + ")") : "确定"));
//        titleFragment.getRightView().setEnabled(false);
//
//        titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_ffb700));
//        if (organIdList != null && organIdList.size() > 0) {
//            titleFragment.getRightView().setEnabled(true);
//        }
//
//    }
//
//    private void initView() {
//
//        if (isMatch) {
//            findViewById(R.id.activity_association_select_header_view).setVisibility(View.GONE);
//            findViewById(R.id.line_view_1).setVisibility(View.GONE);
//            maxNumber = 1;
//
//        }
//
//        searchEdittextView = findViewById(R.id.search_edittext);
//        searchEdittextView.clearFocus();
//        searchEdittextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!StringUtils.isEmpty(s)) {
//
//                    refreshLayout.setEnableLoadmore(false);
//                    refreshLayout.setEnableRefresh(false);
//
//                    getPresenter().getSecondTypeBySearchList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), s.toString());
//
//                } else {
//                    refreshLayout.setEnableLoadmore(true);
//                    refreshLayout.setEnableRefresh(true);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//        });
//
//        searchEdittextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//
//                switch (actionId) {
//                    case EditorInfo.IME_ACTION_SEARCH:
//
//                        String text = searchEdittextView.getText().toString();
//                        if (!StringUtils.isEmpty(text)) {
//
//                            refreshLayout.setEnableLoadmore(false);
//                            refreshLayout.setEnableRefresh(false);
//
//                            getPresenter().getSecondTypeBySearchList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), text);
//                        }
//
//
//                        break;
//
//                }
//
//                return true;
//            }
//        });
//
//        recyclerView = findViewById(R.id.recycle_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance());
//        layoutManager.setAutoMeasureEnabled(true);
//        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(5)));
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(layoutManager);
//
//        adapter = new AssociationSelectListAdapter(isMatch);
//        recyclerView.setAdapter(adapter);
//
//        adapter.setBtnClickListener(new AssociationSelectListAdapter.BtnClickListener() {
//            @Override
//            public void onClick(final OrganizationInfo organizationInfo, final int position) {
//                //入圈验证(1-->直接加入,2-->申请同意后可加入),
//                int joinLimit = organizationInfo.getJoinLimit();
//                if (joinLimit == 2) {
//                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
//                    dialog.setText("", "本圈子为私密圈子，需要先发送申请，管理员同意后才能加入，发送？", "取消", "发送");
//                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            //取消
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                            //申请
//                            selectPosition = position;
//                            getPresenter().joinOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0), 2);
//                        }
//                    });
//                    dialog.show(getSupportFragmentManager());
//                } else {
//                    selectPosition = position;
//                    getPresenter().joinOrganization(organizationInfo.getOrganId(), SafeSharePreferenceUtils.getInt(Constants.Fields.USER_ID, 0), 1);
//                }
//            }
//        });
//
//
//        adapter.setOnItemListener(new ItemListener() {
//
//            private int contentLimit;
//
//            @Override
//            public void onItemClick(View view, int position) {
//                OrganizationInfo organizationInfo = adapter.getList().get(position);
//                if (organizationInfo == null) {
//                    return;
//                }
//
//                if (selectList == null) {
//                    selectList = new ArrayList<>(1);
//                }
//
//                if (organizationInfo.getContentLimit() != contentLimit) {
//                    selectList.clear();
//                    for (int i = 0; i < adapter.getList().size(); i++) {
//                        adapter.getList().get(i).setIsSelect(0);
//                    }
//                    adapter.notifyDataSetChanged();
//                    contentLimit = organizationInfo.getContentLimit();
//                }
//
//                if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2 || organizationInfo.getUserRole() == 3) {
//
//                    if (selectList.size() >= maxNumber && organizationInfo.getIsSelect() != 1) {
//                        Utils.showToastShortTime("最多可选中" + maxNumber + "个圈子");
//                        organizationInfo.setIsSelect(0);
//                        adapter.notifyItemChanged(position);
//                        return;
//                    }
//
//                    organizationInfo.setIsSelect(organizationInfo.getIsSelect() == 1 ? 0 : 1);
//                    adapter.notifyItemChanged(position);
//
//                    if (organizationInfo.getIsSelect() == 1) {
//                        selectList.add(organizationInfo);
//                    } else {
//
//                        for (int i = 0; i < selectList.size(); i++) {
//                            long id = selectList.get(i).getOrganId();
//                            if (id == organizationInfo.getOrganId()) {
//                                selectList.remove(i);
//                                break;
//                            }
//                        }
//
//                    }
//
//                    if (selectList.size() > 0) {
//                        titleFragment.getRightView().setEnabled(true);
//                    } else {
//                        titleFragment.getRightView().setEnabled(false);
//                    }
//
//                    titleFragment.getRightView().setText(selectList != null && selectList.size() > 0 ? ("确定(" + selectList.size() + ")") : "确定");
//
//                } else {
//
//                    Utils.showToastShortTime("请先加入该圈子");
//                }
//
//            }
//        });
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        LoadingView loadingView = new LoadingView(App.getInstance());
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishRefreshing();
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                pageNumber = 1;
//                getPresenter().getJoinOrganList(userId, userId, 0, 2, pageSize, pageNumber, true);
//
//
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                refreshLayout.finishLoadmore();
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                getPresenter().getJoinOrganList(userId, userId, 0, 2, pageSize, pageNumber, true);
//
//            }
//
//        });
//
//
//        // 获取我加入的圈子列表
//        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        pageNumber = 1;
//
//        getPresenter().getJoinOrganList(userId, userId, 0, 2, pageSize, pageNumber, true);
//
//    }
//
//
//    private void initEmptyView(int isVisibility, String blankHint) {
//
//        View emptyView = findViewById(R.id.empty_view);
//        emptyView.setVisibility(isVisibility);
//        if (emptyView != null) {
//            TextView emptyTextView = emptyView.findViewById(R.id.empty_textView);
//            emptyTextView.setText(blankHint);
//
//            View lookUpView = emptyView.findViewById(R.id.look_up_view);
//            lookUpView.setVisibility(View.VISIBLE);
//            lookUpView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startActivity(new Intent(AssociationSelectActivity.this, AssociationAllListActivity.class));
//                }
//            });
//
//        }
//
//    }
//
//    @Override
//    public void updateView(String apiName, Object object) {
//
//        if (isFinishing()) {
//            return;
//        }
//
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            Utils.showToastShortTime(e.getMessage());
//            return;
//        }
//
//        if (InterfaceUrl.URL_GETJOINORGANLIST.equals(apiName)) {
//            if (object instanceof GetJoinOrganListResponse) {
//                GetJoinOrganListResponse response = (GetJoinOrganListResponse) object;
//
//                if (selectList == null) {
//                    selectList = new ArrayList<>(1);
//                }
//
//                List<OrganizationInfo> list = null;
//                list = response.getList();
//
////                if (!isLabelSelected) {
////                    for (int i = 0; i < list.size(); i++) {
////                        if (list.get(i).getType() == 3) {
////                            list.remove(i);
////                        }
////                    }
////                }
//
//                List<OrganizationInfo> sortList1 = new ArrayList<>();
//                List<OrganizationInfo> sortList2 = new ArrayList<>();
//                boolean sortList1AtFirst = true;
//                boolean sortList2AtFirst = true;
//
//                for (int i = 0; i < list.size(); i++) {
//                    OrganizationInfo organizationInfo = list.get(i);
//                    if (isAnonymous == 2 && organizationInfo.getType() == 3) {
//                        continue;
//                    }
//                    if (organizationInfo.getContentLimit() == 2) {
//                        sortList2.add(organizationInfo);
//                        if (sortList2AtFirst) {
//                            organizationInfo.setAtFirst(true);
//                            sortList2AtFirst = false;
//                        }
//                    } else {
//                        sortList1.add(organizationInfo);
//                        if (sortList1AtFirst) {
//                            organizationInfo.setAtFirst(true);
//                            sortList1AtFirst = false;
//                        }
//                    }
//                }
//
//                list.clear();
//                list.addAll(sortList1);
//                list.addAll(sortList2);
//
//                if (selectList.size() > 0) {
//                    if (list != null && list.size() > 0) {
//                        for (int i = 0; i < list.size(); i++) {
//                            for (int j = 0; j < selectList.size(); j++) {
//                                if (list.get(i).getOrganId() == selectList.get(j).getOrganId()) {
//                                    list.get(i).setIsSelect(1);
//                                }
//                            }
//                        }
//                    }
//
//                } else if (organIdList != null && organIdList.size() > 0) {
//                    if (list != null && list.size() > 0) {
//                        for (int i = 0; i < list.size(); i++) {
//                            for (int j = 0; j < organIdList.size(); j++) {
//                                if (list.get(i).getOrganId() == organIdList.get(j).longValue()) {
//                                    list.get(i).setIsSelect(1);
//                                    selectList.add(list.get(i));
//                                }
//                            }
//                        }
//                    }
//
//                }
//
////                if (list == null) {
////                    list = response.getList();
////                }
//
//                if (response.getCode() == 200) {
//                    if (pageNumber == 1) {
//                        adapter.setList(list, false);
//                    } else {
//                        adapter.addList(list);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//                refreshLayout.setEnableLoadmore(true);
//                if (response.getTotal() < adapter.getItemCount() - 1) {
//                    pageNumber++;
//                } else {
//                    refreshLayout.setEnableLoadmore(false);
//                }
//
//                initEmptyView(View.GONE, response.getBlankHint());
//                if (adapter.getItemCount() <= 0) {
//                    initEmptyView(View.VISIBLE, response.getBlankHint());
//                }
//            }
//        } else if (InterfaceUrl.URL_GETSECONDTYPEBYSEARCHLIST.equals(apiName)) {
//            if (object instanceof GetSecondTypeBySearchListResponse) {
//                GetSecondTypeBySearchListResponse response = (GetSecondTypeBySearchListResponse) object;
//
//                if (selectList == null) {
//                    selectList = new ArrayList<>(1);
//                }
//
//                List<OrganizationInfo> list = null;
//                list = response.getOrganList();
//
////                if (!isLabelSelected) {
////                    for (int i = 0; i < list.size(); i++) {
////                        if (list.get(i).getType() == 3) {
////                            list.remove(i);
////                        }
////                    }
////                }
//
//                List<OrganizationInfo> sortList1 = new ArrayList<>();
//                List<OrganizationInfo> sortList2 = new ArrayList<>();
//                boolean sortList1AtFirst = true;
//                boolean sortList2AtFirst = true;
//                for (int i = 0; i < list.size(); i++) {
//                    OrganizationInfo organizationInfo = list.get(i);
//                    if (isAnonymous == 2 && organizationInfo.getType() == 3) {
//                        continue;
//                    }
//                    if (organizationInfo.getContentLimit() == 2) {
//                        sortList2.add(organizationInfo);
//                        if (sortList2AtFirst) {
//                            organizationInfo.setAtFirst(true);
//                            sortList2AtFirst = false;
//                        }
//                    } else {
//                        sortList1.add(organizationInfo);
//                        if (sortList1AtFirst) {
//                            organizationInfo.setAtFirst(true);
//                            sortList1AtFirst = false;
//                        }
//                    }
//                }
//
//                list.clear();
//                list.addAll(sortList1);
//                list.addAll(sortList2);
//
//                if (organIdList != null && organIdList.size() > 0) {
//                    if (list != null && list.size() > 0) {
//                        for (int i = 0; i < list.size(); i++) {
//
//                            for (int j = 0; j < organIdList.size(); j++) {
//                                if (list.get(i).getOrganId() == organIdList.get(j).longValue()) {
//                                    list.get(i).setIsSelect(1);
//                                    selectList.add(list.get(i));
//                                }
//                            }
//                        }
//                    }
//                }
//
////                if (list == null) {
////                    list = response.getOrganList();
////                }
//
//                if (response.getCode() == 200) {
//                    adapter.setList(list, true);
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//                initEmptyView(View.GONE, response.getBlankHint());
//                if (adapter.getItemCount() <= 0) {
//                    initEmptyView(View.VISIBLE, response.getBlankHint());
//                }
//
//            }
//
//        } else if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//
//                // 申请中状态
//                if (response.getCode() == 202) {
//                    if (selectPosition > -1 && adapter.getList().size() > 0) {
//                        adapter.getList().get(selectPosition).setUserRole(4);
//                        adapter.notifyItemChanged(selectPosition);
//                    }
//                } else if (response.getCode() == 200) {// 申请成功状态
//                    if (selectPosition > -1 && adapter.getList().size() > 0) {
//                        adapter.getList().get(selectPosition).setUserRole(3);
//                        adapter.notifyItemChanged(selectPosition);
//                    }
//                }
//
//                selectPosition = -1;
//
//            }
//
//        }
//
//    }
//
//    @Override
//    public void showLoading() {
//
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//
//            EventBus.getDefault().post(new AssociationSelectEvent(selectList));
//            finish();
//
//        }
//        return false;
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        if (recyclerView != null) {
//            recyclerView.removeAllViews();
//        }
//
//        recyclerView = null;
//
//        if (refreshLayout != null) {
//            refreshLayout.removeAllViews();
//        }
//
//        refreshLayout = null;
//
//        if (searchEdittextView != null) {
//            searchEdittextView = null;
//        }
//
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//        presenter = null;
//
//        if (adapter != null) {
//            adapter.onDestroy();
//        }
//        adapter = null;
//
//    }
//
//
//}
