//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
//import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
//import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.AssociationInfoEditorEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetJoinOrganListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetOrganizationListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSchoolOrganizationListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSecondTypeListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SchoolfellowCreateResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationSecondTypeExpandeAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.AssociationTypeListItemAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.SecondHobbyInfoListDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
//
///**
// * 一级标签类型下的所有圈子
// */
//public class AssociationTypeListActivity extends BaseActivity implements BaseView {
//
//    private HobbyInfo hobbyInfo;
//
//    private RecyclerView recyclerView;
//    private ImageView chevron_image;
//    private AssociationTypeListItemAdapter adapter;
//
//    private RecyclerView tagRecyclerView;
//    private AssociationSecondTypeExpandeAdapter tagAdapter;
//
//
//    private AssociationPresenter presenter;
//
//    private int pageSize = 10;
//    private int pageNumber = 1;
//
//    private long hobbyId;
//    private ArrayList<Long> secondHobbyIdArr;
//    private HashMap<String, Long> map;
//
//    // 点击的item下标
//    private int selectPosition = -1;
//    private int type;//2.我的 3.他人
//    private TwinklingRefreshLayout refreshLayout;
//    private long myUserId;
//    private long toUserId;
//    private View emptyView;
//    private TextView empty_tv;
//    private LinearLayoutManager layoutManager;
//    private TextView finishView;
//
//    public AssociationPresenter getPresenter() {
//        if (presenter == null) {
//            presenter = new AssociationPresenter(this);
//        }
//        return presenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_association_type_list_layout);
//        EventBus.getDefault().register(this);
//        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
//
//        if (type == 0) {
//            hobbyInfo = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);
//            hobbyId = hobbyInfo.getHobbyId();
//        }
//
//
//        toUserId = getIntent().getLongExtra(Constants.Fields.Ta_USER_ID, 0);
//        myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//
//        if (toUserId == 0) {
//            toUserId = myUserId;
//        }
//
//        setTitle();
//
//        initView();
//
//        initTagView();
//
//        if (secondHobbyIdArr == null) {
//            secondHobbyIdArr = new ArrayList(1);
//        }
//
//        if (map == null) {
//            map = new HashMap(1);
//        }
//
//
//        if (type == 2) {
//            getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//            tagRecyclerView.setVisibility(View.GONE);
//            chevron_image.setVisibility(View.GONE);
//        } else if (type == 3) {
//            tagRecyclerView.setVisibility(View.GONE);
//            chevron_image.setVisibility(View.GONE);
//            getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//        } else {
//
//
//            if (hobbyId > 0) {
//                // 获取二级标签列表
//                getPresenter().getSecondTypeList(hobbyInfo.getHobbyId());
//                // 获取二级标签下的所有圈子列表
//                getPresenter().getOrganizationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, hobbyId, 0, secondHobbyIdArr, pageSize, pageNumber, true);
//
//            } else if (hobbyId == -1) {
//                // 学校圈子入口进入
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                getPresenter().schoolfellowCreate(userId);
//                getPresenter().getSchoolOrganizationList(userId, secondHobbyIdArr, pageSize, pageNumber);
//            } else if (hobbyId == -2) {
//                //校友录入口
//                tagRecyclerView.setVisibility(View.GONE);
//                chevron_image.setVisibility(View.GONE);
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                getPresenter().getAlumniOrganizationList(userId, pageSize, pageNumber);
//
//            }
//
//        }
//
//    }
//
//
//    private void setTitle() {
//        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        if (type == 2) {
//            titleFragment.setTitleView("我加入的圈子");
//        } else if (type == 3) {
//            titleFragment.setTitleView("Ta加入的圈子");
//        } else {
//            titleFragment.setTitleView(hobbyInfo.getHobbyName());
//        }
//    }
//
//    private void initView() {
//
//
//        recyclerView = findViewById(R.id.recycle_view);
//        finishView = findViewById(R.id.finish_view);
//        emptyView = findViewById(R.id.empty_view);
//        empty_tv = findViewById(R.id.empty_textView);
//
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(App.getInstance());
//        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
//        layoutManager2.setAutoMeasureEnabled(true);
//        recyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(1), true));
//        recyclerView.setLayoutManager(layoutManager2);
//
//        adapter = new AssociationTypeListItemAdapter();
//        recyclerView.setAdapter(adapter);
//
//        // 加入按钮回调事件
//        adapter.setBtnClickListener(new AssociationTypeListItemAdapter.BtnClickListener() {
//            @Override
//            public void onClick(final OrganizationInfo organizationInfo, final int position) {
//
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
//        adapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                OrganizationInfo organizationInfo = adapter.getList().get(position);
//
//                startActivity(new Intent(AssociationTypeListActivity.this, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo).putExtra(Constants.Fields.POSITION, position));
//            }
//        });
//
//        chevron_image = findViewById(R.id.chevron_image);
//
//
//        // 刷新框架
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//
//        // 头部加载样式
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(recyclerView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(true);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNumber = 1;
//
//                if (type == 2) {
//                    getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//                } else if (type == 3) {
//                    getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//                } else {
//
//                    if (hobbyId > 0) {
//                        // 获取二级标签列表
//                        // 获取二级标签下的所有圈子列表
//                        getPresenter().getOrganizationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, hobbyId, 0, secondHobbyIdArr, pageSize, pageNumber, true);
//
//                    } else if (hobbyId == -1) {
//                        // 学校圈子入口进入
//                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                        getPresenter().getSchoolOrganizationList(userId, secondHobbyIdArr, pageSize, pageNumber);
//                    } else if (hobbyId == -2) {
//                        //校友录入口
//                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                        getPresenter().getAlumniOrganizationList(userId, pageSize, pageNumber);
//
//                    }
//
//                }
//
//                // 获取二级标签下的所有圈子列表
//
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//
//                // 获取二级标签下的所有圈子列表
//
//                if (type == 2) {
//                    getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//                } else if (type == 3) {
//                    getPresenter().getJoinOrganList(myUserId, toUserId, 0, 1, pageSize, pageNumber, false);
//                } else {
//
//                    if (hobbyId > 0) {
//                        // 获取二级标签下的所有圈子列表
//                        getPresenter().getOrganizationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, hobbyId, 0, secondHobbyIdArr, pageSize, pageNumber, true);
//
//                    } else if (hobbyId == -1) {
//                        // 学校圈子入口进入
//                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                        getPresenter().getSchoolOrganizationList(userId, secondHobbyIdArr, pageSize, pageNumber);
//                    } else if (hobbyId == -2) {
//                        //校友录入口
//                        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                        getPresenter().getAlumniOrganizationList(userId, pageSize, pageNumber);
//
//                    }
//
//                }
//
//                refreshLayout.finishLoadmore();
//            }
//        });
//
//
//    }
//
//
//    private void initTagView() {
//
//        findViewById(R.id.chevron_image).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                // TODO 跳转到新的页面
//
//                final SecondHobbyInfoListDialog dialog = new SecondHobbyInfoListDialog();
//                dialog.setList(tagAdapter.getList());
//                dialog.setSelectItemCallback(new SecondHobbyInfoListDialog.SelectItemCallback() {
//                    @Override
//                    public void selectItem(int position, int isSelect) {
//
//                        updateItemStatus(position, false, isSelect);
//
//                    }
//
//                    @Override
//                    public void run() {
//                        pageNumber = 1;
//                        if (hobbyId > 0) {
//                            // 获取二级标签下的所有圈子列表
//                            getPresenter().getOrganizationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, hobbyId, 0, secondHobbyIdArr, pageSize, pageNumber, true);
//
//                        } else {
//                            // 学校圈子入口进入
//                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                            getPresenter().getSchoolOrganizationList(userId, secondHobbyIdArr, pageSize, pageNumber);
//                        }
//                    }
//                });
//                dialog.show(getSupportFragmentManager());
//
//
//            }
//        });
//
//        tagRecyclerView = findViewById(R.id.tag_recycle_view);
//
//        layoutManager = new LinearLayoutManager(App.getInstance());
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        layoutManager.setAutoMeasureEnabled(true);
//        tagRecyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10), false));
//        tagRecyclerView.setLayoutManager(layoutManager);
//
//        tagAdapter = new AssociationSecondTypeExpandeAdapter(null);
//        tagRecyclerView.setAdapter(tagAdapter);
//        tagAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//                updateItemStatus(position, true, -1);
//
//
//            }
//        });
//
//    }
//
//
//    private void initEmptyView(boolean isVisibility) {
//        if (emptyView == null) {
//            emptyView = findViewById(R.id.empty_view);
//        }
//        emptyView.setVisibility(isVisibility ? View.GONE : View.VISIBLE);
//
//    }
//
//    private void updateItemStatus(int position, boolean isRunNetWork, int isSelect) {
//
//        secondHobbyIdArr.clear();
//        // 点击全部按钮
//        if (position == 0) {
//            int itemIndex = 0;
//            for (HobbyInfo hobbyInfo : tagAdapter.getList()) {
//                if (itemIndex == 0) {
//                    hobbyInfo.setIsSelect(1);
//                } else {
//                    hobbyInfo.setIsSelect(0);
//                }
//                itemIndex++;
//            }
//
//            map.clear();
//
//        } else {
//
//            tagAdapter.getList().get(0).setIsSelect(0);
//
//            HobbyInfo hobbyInfo = tagAdapter.getList().get(position);
//            if (hobbyInfo.getIsSelect() == 1) {
//                hobbyInfo.setIsSelect(0);
//                for (Map.Entry<String, Long> entry : map.entrySet()) {
//                    if ((hobbyInfo.getHobbyId() + "").equals(entry.getKey())) {
//                        map.remove(hobbyInfo.getHobbyId() + "");
//                        break;
//                    }
//                }
//            } else {
//                hobbyInfo.setIsSelect(1);
//                long secondHobbyId = hobbyInfo.getHobbyId();
//                map.put(secondHobbyId + "", secondHobbyId);
//            }
//
//            for (Map.Entry<String, Long> entry : map.entrySet()) {
//                secondHobbyIdArr.add(entry.getValue());
//            }
//
//            switch (isSelect) {
//
//                case 0:
//                    hobbyInfo.setIsSelect(0);
//                    map.remove(hobbyInfo.getHobbyId() + "");
//                    secondHobbyIdArr.remove(hobbyInfo.getHobbyId());
//                    break;
//
//                case 1:
//                    hobbyInfo.setIsSelect(1);
//                    map.put(hobbyInfo.getHobbyId() + "", hobbyInfo.getHobbyId());
//                    secondHobbyIdArr.add(hobbyInfo.getHobbyId());
//                    break;
//            }
//
//            // 如果选择的item全部取消了，则默认为全部按钮选中
//            if (secondHobbyIdArr.size() == 0) {
//                tagAdapter.getList().get(0).setIsSelect(1);
//            }
//        }
//
//        tagAdapter.notifyDataSetChanged();
//
//        if (isRunNetWork) {
//            pageNumber = 1;
//
//            if (hobbyId > 0) {
//                // 获取二级标签下的所有圈子列表
//                getPresenter().getOrganizationList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 1, hobbyId, 0, secondHobbyIdArr, pageSize, pageNumber, true);
//
//            } else {
//                // 学校圈子入口进入
//                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                getPresenter().getSchoolOrganizationList(userId, secondHobbyIdArr, pageSize, pageNumber);
//            }
//
//        }
//
//
//    }
//
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
//        if (InterfaceUrl.URL_GETORGANIZATIONLIST.equals(apiName)) {
//            if (object instanceof GetOrganizationListResponse) {
//                GetOrganizationListResponse response = (GetOrganizationListResponse) object;
//
//                if (response.getCode() == 200) {
//                    if (!TextUtils.isEmpty(response.getBlankHint())) {
//                        empty_tv.setText(response.getBlankHint());
//                    }
//
//                    if (pageNumber == 1) {
//                        adapter.setList(response.getOrganList());
//                    } else {
//                        adapter.addList(response.getOrganList());
//                    }
//
//                    if (response.getTotal() > adapter.getItemCount()) {
//                        pageNumber++;
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//
//                checkEmptyView(response.getTotal());
//
//            }
//        } else if (InterfaceUrl.URL_GETSECONDHOBBYLIST.equals(apiName)) {
//            if (object instanceof GetSecondTypeListResponse) {
//                GetSecondTypeListResponse response = (GetSecondTypeListResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    List<HobbyInfo> list = response.getHobbyList();
//                    HobbyInfo info = new HobbyInfo();
//                    info.setHobbyName("全部圈子");
//                    info.setHobbyId(-1);
//                    info.setIsSelect(1);
//                    list.add(0, info);
//                    tagAdapter.setList(list);
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//            }
//        } else if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {
//
//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {// 申请或加入成功状态
//                    if (selectPosition > -1 && adapter.getList().size() > 0) {
//                        //申请
//                        if (adapter.getList().get(selectPosition).getJoinLimit() == 2) {
//                            adapter.getList().get(selectPosition).setUserRole(4);
//                        } else {
//                            adapter.getList().get(selectPosition).setUserRole(3);
//                        }
//                        adapter.notifyItemChanged(selectPosition);
//                    }
//                }
//
//                selectPosition = -1;
//
//            }
//
//        } else if (InterfaceUrl.URL_GETJOINORGANLIST.equals(apiName)) {
//
//            if (object instanceof GetJoinOrganListResponse) {
//                GetJoinOrganListResponse response = (GetJoinOrganListResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    List<OrganizationInfo> list = response.getList();
//                    int size = list.size();
//                    if (type == 3) {
//                        for (int i = 0; i < size; i++) {
//                            if (list.get(i).getIsPublic() == 0) {//圈子不是私密圈子
//                                if (list.get(i).getContentLimit() == 2 && (list.get(i).getUserRole() == 0 || list.get(i).getUserRole() == 4)) {//仅圈友可见但是我不在圈子里或者正在申请中
//                                    list.remove(i);
//                                    size -= 1;
//                                }
//                            } else {
//                                list.remove(i);
//                            }
//                        }
//                    }
//
//                    if (!TextUtils.isEmpty(response.getBlankHint())) {
//                        empty_tv.setText(response.getBlankHint());
//                    }
//                    if (pageNumber == 1) {
//                        adapter.setList(list);
//                    } else {
//                        adapter.addList(list);
//                    }
//
//                    if (response.getTotal() > adapter.getItemCount()) {
//                        pageNumber++;
//                    }
//
//                    checkEmptyView(response.getTotal());
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
//
//        } else if (InterfaceUrl.URL_SCHOOLFELLOWCREATE.equals(apiName)) {
//
//            if (object instanceof SchoolfellowCreateResponse) {
//                SchoolfellowCreateResponse response = (SchoolfellowCreateResponse) object;
//
//                if (response.getCode() == 200) {
//
//                    List<HobbyInfo> list = response.getHobbyList();
//                    if (list == null) {
//                        list = new ArrayList<>(1);
//                    }
//
//                    HobbyInfo info = new HobbyInfo();
//                    info.setHobbyName("全部圈子");
//                    info.setHobbyId(-1);
//                    info.setIsSelect(1);
//                    list.add(0, info);
//                    tagAdapter.setList(list);
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//
//            }
//
//        } else if (InterfaceUrl.URL_GETSCHOOLORGANIZATIONLIST.equals(apiName)) {
//            if (object instanceof GetSchoolOrganizationListResponse) {
//                GetSchoolOrganizationListResponse response = (GetSchoolOrganizationListResponse) object;
//
//                if (response.getCode() == 200) {
//                    if (!TextUtils.isEmpty(response.getBlankHint())) {
//                        empty_tv.setText(response.getBlankHint());
//                    }
//
//                    if (pageNumber == 1) {
//                        adapter.setList(response.getOrganList());
//                    } else {
//                        adapter.addList(response.getOrganList());
//                    }
//
//                    if (response.getTotal() > adapter.getItemCount()) {
//                        pageNumber++;
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//
//                checkEmptyView(response.getTotal());
//            }
//        } else if (InterfaceUrl.URL_GETALUMNIORGANIZATIONLIST.equals(apiName)) {
//            if (object instanceof GetSchoolOrganizationListResponse) {
//                GetSchoolOrganizationListResponse response = (GetSchoolOrganizationListResponse) object;
//
//                if (response.getCode() == 200) {
//                    if (!TextUtils.isEmpty(response.getBlankHint())) {
//                        empty_tv.setText(response.getBlankHint());
//                    }
//
//                    if (pageNumber == 1) {
//                        adapter.setList(response.getOrganList());
//                    } else {
//                        adapter.addList(response.getOrganList());
//                    }
//
//                    if (response.getTotal() > adapter.getItemCount()) {
//                        pageNumber++;
//                    }
//
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//
//
//                checkEmptyView(response.getTotal());
//            }
//        }
//
//
//    }
//
//    private void checkEmptyView(int total) {
//
//        if (total <= adapter.getItemCount()) {
//            //没有更多数据时  下拉刷新不可用
//            finishView.setVisibility(View.VISIBLE);
//            refreshLayout.setEnableLoadmore(false);
//        } else {
//            //有更多数据时  下拉刷新才可用
//            finishView.setVisibility(View.GONE);
//            refreshLayout.setEnableLoadmore(true);
//        }
//
//        if (adapter.getItemCount() == 0) {
//            emptyView.setVisibility(View.VISIBLE);
//        } else {
//            emptyView.setVisibility(View.GONE);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void callBackEvent(AssociationInfoEditorEvent evnet) {
//
//        if (evnet != null) {
//            if (type == 2) {
//                adapter.getList().remove(evnet.getPosition());
//                adapter.notifyDataSetChanged();
//                if (adapter.getList().size() == 0) {
//                    emptyView.setVisibility(View.VISIBLE);
//                }
//            } else {
//                adapter.getList().get(evnet.getPosition()).setUserRole(0);
//                adapter.notifyItemChanged(evnet.getPosition());
//            }
//        }
//    }
//
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
//    protected void onDestroy() {
//        super.onDestroy();
//
//        EventBus.getDefault().unregister(this);
//
//        if (hobbyInfo != null) {
//            hobbyInfo = null;
//        }
//
//        if (recyclerView != null) {
//            recyclerView.removeAllViews();
//        }
//        recyclerView = null;
//
//        if (adapter != null) {
//            if (adapter.getList() != null) {
//                adapter.getList().clear();
//            }
//            adapter = null;
//        }
//
//
//        if (tagRecyclerView != null) {
//            tagRecyclerView.removeAllViews();
//        }
//        tagRecyclerView = null;
//
//        if (tagAdapter != null) {
//            if (tagAdapter.getList() != null) {
//                tagAdapter.getList().clear();
//            }
//            tagAdapter = null;
//        }
//
//        if (presenter != null) {
//            presenter.onDestroy();
//        }
//        presenter = null;
//
//        if (refreshLayout != null) {
//            refreshLayout.removeAllViews();
//        }
//
//        if (emptyView != null) {
//            emptyView = null;
//        }
//
//        if (secondHobbyIdArr != null) {
//            secondHobbyIdArr.clear();
//        }
//        secondHobbyIdArr = null;
//
//        if (map != null) {
//            map.clear();
//        }
//        map = null;
//
//    }
//}
