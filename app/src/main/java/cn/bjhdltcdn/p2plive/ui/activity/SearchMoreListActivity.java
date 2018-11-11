package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetSearchDataListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetHotWordsListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.SearchMoreListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * 搜索更多列表
 */
public class SearchMoreListActivity extends BaseActivity implements BaseView {
    private GetHotWordsListPresenter presenter;
    private GroupPresenter groupPresenter;
    private TitleFragment titleFragment;
    private TwinklingRefreshLayout refreshLayout;
    private ListView listView;
    private SearchMoreListAdapter adapter;
    private int pageSize = 20, pageNum = 1;
    private TextView finishView;
    private LoadingView loadingView;
    private View emptyView;
    private int type;
    private String content;
    private long userId;
    private TextView totalNumView;
    private int isExistGroup;//当前用户是否在群中(0不在,1在,2申请中),
    private int groupMode;//成员进群方式(1直接入群,2申请进群),
    private int position;
    private boolean needShowLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_more_list);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        initView();
        setTitle();
        presenter = new GetHotWordsListPresenter(this);
        groupPresenter = new GroupPresenter(this);
        presenter.getSearchDataList(userId, content, type, pageSize, pageNum);
    }

    public void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra("type", 0);
            content = intent.getStringExtra("content");
        }

        emptyView = findViewById(R.id.empty_view);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.search_more_list_head_layout, null);
        totalNumView = headView.findViewById(R.id.num_view);
        listView = (ListView) findViewById(R.id.search_list_view);
        finishView = (TextView) findViewById(R.id.finish_view);
        adapter = new SearchMoreListAdapter(this);
        //假数据
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
        adapter.setOnClick(new SearchMoreListAdapter.OnClick() {
            @Override
            public void joinGroupClick(final Group group, int posi) {
                isExistGroup = group.getIsExistGroup();
                groupMode = group.getGroupMode();
                position = posi;
                if (isExistGroup == 0) {
                    if (groupMode == 2) {
                        //申请加入群组
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "本群为私密群，需申请同意后才能加入，现在要申请入群吗？", "取消", "申请进群");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                            }

                            @Override
                            public void onRightClick() {
                                //申请
                                groupPresenter.joinGroup(userId, group.getGroupId(), groupMode);
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    } else {
                        //直接加入群组
                        groupPresenter.joinGroup(userId, group.getGroupId(), groupMode);
                    }
                } else if (isExistGroup == 1) {
                    //发起群聊
                    RongIMutils.startGroupChat(SearchMoreListActivity.this, group.getGroupId() + "", group.getGroupName());

                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object object = adapter.getItem(position);
                if (object instanceof OrganizationInfo) {
                    //跳到圈子详情
                    OrganizationInfo organizationInfo = (OrganizationInfo) object;
                } else if (object instanceof ActivityInfo) {
                    ActivityInfo ActivityInfo = (ActivityInfo) object;
                    //跳到活动详情
                } else if (object instanceof BaseUser) {
                    //跳到用户详情
                    BaseUser baseUser = (BaseUser) object;
                    if (baseUser.getUserId() != userId) {
                        //跳到用户详情
                        startActivity(new Intent(SearchMoreListActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    }
                } else if (object instanceof Group) {
                    Group group = (Group) object;
                }
            }
        });

        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(SearchMoreListActivity.this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                pageNum = 1;
                presenter.getSearchDataList(userId, content, type, pageSize, pageNum);
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (!loadingView.isOnLoadFinish()) {
                    pageNum++;
                    presenter.getSearchDataList(userId, content, type, pageSize, pageNum);
                }
            }
        });
        emptyView.setVisibility(View.GONE);

    }


    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_search_more_list);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    public void initTotalView(int total) {

        switch (type) {
            case 1:
                totalNumView.setText("共" + total + "个圈子");
                break;
            case 2:
                totalNumView.setText("共" + total + "个群组");
                break;
            case 3:
                totalNumView.setText("共" + total + "个用户");
                break;
            case 4:
                totalNumView.setText("共" + total + "个活动");
                break;
        }

    }


    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_GETSEARCHDATALIST)) {
            if (object instanceof GetSearchDataListResponse) {
                GetSearchDataListResponse getSearchDataListResponse = (GetSearchDataListResponse) object;
                List list = null;
                switch (type) {
                    case 1:
                        list = getSearchDataListResponse.getOrganList();
                        break;
                    case 2:
                        list = getSearchDataListResponse.getGroupList();
                        break;
                    case 3:
                        list = getSearchDataListResponse.getUserList();
                        break;
                    case 4:
                        list = getSearchDataListResponse.getActiveList();
                        break;
                    default:
                        break;

                }

                if (list != null) {
                    if (pageNum == 1) {
                        refreshLayout.finishRefreshing();
                        adapter.setList(list, getSearchDataListResponse.getDefaultImg());
                        initTotalView(getSearchDataListResponse.getTotal());
                    } else {
                        refreshLayout.finishLoadmore();
                        adapter.addList(list);
                    }

                    adapter.notifyDataSetChanged();
                    if (getSearchDataListResponse.getTotal() <= pageNum * pageSize) {
                        //没有更多数据时  下拉刷新不可用
                        refreshLayout.setEnableLoadmore(false);
                        finishView.setVisibility(View.VISIBLE);
                    } else {
                        //有更多数据时  下拉刷新才可用
                        refreshLayout.setEnableLoadmore(true);
                        finishView.setVisibility(View.GONE);
                    }

                    if (getSearchDataListResponse.getTotal() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);

                    }
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_JOIN_GROUP)) {
            if (object instanceof JoinGroupResponse) {
                JoinGroupResponse baseResponse = (JoinGroupResponse) object;
                if (baseResponse.getCode() == 200) {
                    Utils.showToastShortTime(baseResponse.getMsg());
                    if (groupMode == 1) {
                        Group group = (Group) adapter.getItem(position + 1);
                        group.setIsExistGroup(1);
                        adapter.notifyDataSetChanged();
                    } else {
                        Group group = (Group) adapter.getItem(position + 1);
                        group.setIsExistGroup(2);
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
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
}
