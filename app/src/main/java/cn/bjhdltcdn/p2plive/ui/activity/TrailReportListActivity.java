package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SharePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAttentionRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;

/**
 * Created by ZHAI on 2017/12/12.
 */

public class TrailReportListActivity extends BaseActivity implements BaseView {
    private long userId;
    private View emptyView;
    private TextView empty_tv;
    private TextView finishView;
    private TwinklingRefreshLayout refreshLayout;
    private PostCommentListPresenter postCommentListPresenter;
    private RecyclerView recycleView;
    private TitleFragment titleFragment;
    private int pageSize = 10, pageNum = 1;
    private HomeAttentionRecyclerViewAdapter postListAdapter;
    private LoadingView loadingView;
    private int currentPosition;
    private View sendView;
    private EditText editText;
    private UserPresenter userPresenter;
    private long myUserId;
    private TextView sendTextView;
    private Switch anonymousSwitch;
    private int anonymousSwitchIsChecked;
    private SharePresenter sharePresenter;

    private long mToUserId;
    private long mPostId;
    private int mPosition;
    private int mDeletePosition;
    private PkPresenter pkPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private OrderPresenter orderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        EventBus.getDefault().register(this);
        sharePresenter = new SharePresenter(this);
        chatRoomPresenter = new ChatRoomPresenter(this);
        postCommentListPresenter = new PostCommentListPresenter(this);

        orderPresenter = new OrderPresenter(this);

        setTitle();
        initView();

        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        orderPresenter.getTrailReportList(userId, pageSize, pageNum);
    }

    public PkPresenter getPkPresenter() {
        if (pkPresenter == null) {
            pkPresenter = new PkPresenter(this);
        }
        return pkPresenter;
    }

    private void initView() {
        emptyView = findViewById(R.id.empty_view);
        empty_tv = findViewById(R.id.empty_textView);
        finishView = findViewById(R.id.finish_view);
        editText = findViewById(R.id.reply_edit_input);
        sendTextView = findViewById(R.id.send_view);
        sendView = findViewById(R.id.send_comment_view);
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        postListAdapter = new HomeAttentionRecyclerViewAdapter(TrailReportListActivity.this);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        recycleView.setLayoutManager(linearlayoutManager);
        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycleView.setAdapter(postListAdapter);
        postListAdapter.setOnClick(new HomeAttentionRecyclerViewAdapter.OnClick() {
            @Override
            public void onPraise(long postId, int type, int position, int operationType) {
                currentPosition = position;
                if (operationType == 1) {
                    //帖子
                    postCommentListPresenter.postPraise(myUserId, postId, type);
                }
            }

            @Override
            public void onOrgain(long orgainId) {

            }

            @Override
            public void onDelete(long postId, int position) {
                //删除帖子
                mPostId = postId;
                mDeletePosition = position;
                postCommentListPresenter.deletePost(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), mPostId);
            }

            @Override
            public void onDeleteShare(long shareId, int position) {
                //删除分享
                mDeletePosition = position;
                sharePresenter.deleteMyShare(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), shareId);
            }

            @Override
            public void onJoinClick(int position, int type) {

            }

            @Override
            public void onApplyClerk(long storeId) {

            }
        });

        postListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeInfo homeInfo = postListAdapter.getItem(position);
                //1帖子详情页
                if (homeInfo.getPostInfo() != null) {
                    if (homeInfo.getPostInfo().getContentLimit()!=2){
                    Intent intent = new Intent(TrailReportListActivity.this, PostDetailActivity.class);
                    intent.putExtra(Constants.KEY.KEY_OBJECT, homeInfo.getPostInfo());
                    intent.putExtra(Constants.KEY.KEY_POSITION, position);
                    startActivity(intent);}
                }

            }
        });

        anonymousSwitch = findViewById(R.id.anonymous_view);
        anonymousSwitch.setVisibility(View.VISIBLE);
        anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    anonymousSwitchIsChecked = 1;
                }
            }
        });
        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editText.getText().toString();
                if ("".equals(comment)) {
                    Utils.showToastShortTime("评论不能为空");
                } else {//String content, int type, long toUserId, long fromUserId, long postId, long parentId,int anonymousType
                    postCommentListPresenter.postComment(comment, 1, mToUserId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), mPostId, 0, anonymousSwitchIsChecked);
                }
            }
        });

        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(getApplicationContext());
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
                pageNum = 1;
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                if (!loadingView.isOnLoadFinish()) {
                }
            }
        });
        emptyView.setVisibility(View.GONE);

    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle("试用报告");
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_GETTRAILREPORTLIST)) {
            if (object instanceof MyPostListResponse) {
                MyPostListResponse postListResponse = (MyPostListResponse) object;
                if (postListResponse.getCode() == 200) {
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                    List<PostInfo> postInfoList = postListResponse.getList();
                    ArrayList<HomeInfo> list = new ArrayList<>();

                    for (int i = 0; i < postInfoList.size(); i++) {
                        HomeInfo homeInfo = new HomeInfo();
                        homeInfo.setBaseUser(postInfoList.get(i).getBaseUser());
                        homeInfo.setPostInfo(postInfoList.get(i));
                        list.add(homeInfo);
                    }

                    if (!TextUtils.isEmpty(postListResponse.getBlankHint())) {
                        empty_tv.setText(postListResponse.getBlankHint());
                    }
                    if (list != null) {
                        if (pageNum == 1) {
                            refreshLayout.finishRefreshing();
                            postListAdapter.setList(list, "");
                        } else {
                            refreshLayout.finishLoadmore();
                            postListAdapter.addList(list);
                        }

                        postListAdapter.notifyDataSetChanged();
                        if (postListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                            finishView.setVisibility(View.GONE);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                            finishView.setVisibility(View.GONE);
                        }
                        if (postListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            finishView.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Utils.showToastShortTime(postListResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_POSTPRAISE)) {
            if (object instanceof PostPraiseResponse) {
                PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                if (postPraiseResponse.getCode() == 200) {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                    PostInfo postInfo = postListAdapter.getItem(currentPosition).getPostInfo();
                    if (postInfo != null) {
                        int praiseCount = postInfo.getPraiseCount();
                        if (postPraiseResponse.getIsPraise() == 1) {
                            //点赞
                            postInfo.setIsPraise(1);
                            postInfo.setPraiseCount(praiseCount + 1);
                            postListAdapter.notifyItemChanged(currentPosition);
                        } else if (postPraiseResponse.getIsPraise() == 2) {
                            //取消点赞
                            postInfo.setIsPraise(0);
                            postInfo.setPraiseCount(praiseCount - 1);
                            postListAdapter.notifyItemChanged(currentPosition);
                        }
                    }
                } else {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_DELETEPOST.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());
                if (response.getCode() == 200) {
                    // 删除帖子
                    postListAdapter.getList().remove(mDeletePosition);
                    postListAdapter.notifyDataSetChanged();
                    if (postListAdapter.getList().size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        finishView.setVisibility(View.GONE);
                    }

                }
            }
        } else if (InterfaceUrl.URL_DELETEMYSHARE.equals(apiName)) {
            if (object instanceof NoParameterResponse) {
                NoParameterResponse response = (NoParameterResponse) object;
                Utils.showToastShortTime(response.getMsg());
                if (response.getCode() == 200) {
                    //删除分享
                    postListAdapter.getList().remove(mDeletePosition);
                    postListAdapter.notifyDataSetChanged();
                    if (postListAdapter.getList().size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                        finishView.setVisibility(View.GONE);
                    }

                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdatePostListEvent event) {
        if (event == null) {
            return;
        }
        int position = event.getPosition();
        postListAdapter.notifyDataSetChanged();

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
