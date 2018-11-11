package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindRoomDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyShareListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostListByLabelIdResponse;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetNearHotPostListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PkPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SharePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
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

public class PostListActivity extends BaseActivity implements BaseView {
    private long userId;
    private View emptyView;
    private TextView empty_tv;
    private TextView finishView;
    private TwinklingRefreshLayout refreshLayout;
    private GetNearHotPostListPresenter presenter;
    private PostCommentListPresenter postCommentListPresenter;
    private GetStoreListPresenter getStoreListPresenter;
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
    private int userType;
    private int mDeletePosition;
    private PkPresenter pkPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private long postLableId;
    public GetMyShareListResponse getMyShareListResponse;
    private TextView tile_bellow_text;

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        EventBus.getDefault().register(this);
        userType = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        sharePresenter = new SharePresenter(this);
        chatRoomPresenter = new ChatRoomPresenter(this);
        postCommentListPresenter = new PostCommentListPresenter(this);
        presenter = new GetNearHotPostListPresenter(this);
        setTitle();
        initView();

        userId = getIntent().getLongExtra(Constants.Fields.Ta_USER_ID, 0);
        myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        if (userType == 1) {
            userId = myUserId;
            titleFragment.setTitle(R.string.title_group_mypost);
            sharePresenter.getMyShareList(myUserId, userId, pageSize, pageNum, true);
        } else if (userType == 4) {
            titleFragment.setTitle(R.string.title_group_tapost);
            sharePresenter.getMyShareList(myUserId, userId, pageSize, pageNum, true);
        } else {
            postLableId = getIntent().getLongExtra(Constants.Fields.POST_LABEL_ID, 0);
            RelativeLayout home_post_title = findViewById(R.id.home_post_title);
            home_post_title.setVisibility(View.VISIBLE);
            TextView tile_text_view = home_post_title.findViewById(R.id.tile_text_view);
            tile_bellow_text = home_post_title.findViewById(R.id.tile_bellow_text);
            home_post_title.findViewById(R.id.left_text_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            findViewById(R.id.title_right_text).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PostListActivity.this, PublishActivity.class);
                    intent.putExtra(Constants.Fields.POST_LABEL_ID, postLableId);
                    startActivity(intent);
                }
            });

            if (postLableId == 1) {
                tile_text_view.setText("#表白");
            } else if (postLableId == 2) {
                tile_text_view.setText("#帮帮忙");
            } else if (postLableId == 3) {
                tile_text_view.setText("#比赛");
            }
            sharePresenter.getPostListByLabelId(myUserId, postLableId, pageSize, pageNum);
        }

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
        postListAdapter = new HomeAttentionRecyclerViewAdapter(PostListActivity.this);
        postListAdapter.setUserType(userType);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        recycleView.setLayoutManager(linearlayoutManager);
        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycleView.setAdapter(postListAdapter);
        postListAdapter.setOneToOneCharOnClick(new HomeAttentionRecyclerViewAdapter.OneToOneCharOnClick() {
            @Override
            public void onClick(long toUserId) {
                getUserPresenter().getAnonymityUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toUserId);
            }
        });
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
                getStoreListPresenter().findStoreDetail(myUserId, storeId);
            }
        });

        postListAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                HomeInfo homeInfo = postListAdapter.getItem(position);
                switch (homeInfo.getType()) {
                    case 1:
                    case 11:
                        //1帖子详情页
                        if (homeInfo.getPostInfo() != null) {
                            if (homeInfo.getPostInfo().getContentLimit() == 2) {
                                return;
                            }
                            Intent intent = new Intent(PostListActivity.this, PostDetailActivity.class);
                            intent.putExtra(Constants.KEY.KEY_OBJECT, homeInfo.getPostInfo());
                            intent.putExtra(Constants.KEY.KEY_POSITION, position);
                            startActivity(intent);
                        }
                        break;
                    case 2:
                    case 12:
                        //2圈子
                        if (homeInfo.getOrganizationInfo() != null) {
                        }
                        break;
                    case 3:
                    case 13:
                        //3活动详情页
                        break;
                    case 4:
                    case 14:
                        //4进入房间
                        RoomInfo roomInfo = homeInfo.getRoomInfo();
                        if (roomInfo != null) {
                            if (roomInfo.getPasswordType() == 0) {
                                //不加密
                                chatRoomPresenter.updateUserStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId(), 0, roomInfo.getPasswordType(),
                                        roomInfo.getPassword());
                            } else if (roomInfo.getPasswordType() == 1) {
                                //加密
                                chatRoomPresenter.findRoomDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), roomInfo.getRoomId());
                            }
                        }
                        break;
                    case 5:
                    case 15:
                        //5进入pk播放视频界面
                        if (homeInfo.getPlayInfo() != null) {
                            getPkPresenter().findPlayDetail(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), homeInfo.getPlayInfo().getPlayId());
                        }
                        break;
                    case 8:
                    case 18:
                        //8进入表白详情
                        break;
                    default:
                        break;
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
                if (userType != 1 && userType != 4) {
                    sharePresenter.getPostListByLabelId(myUserId, postLableId, pageSize, pageNum);
                } else {
                    sharePresenter.getMyShareList(myUserId, userId, pageSize, pageNum, false);
                }
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                pageNum++;
                if (!loadingView.isOnLoadFinish()) {
                    if (userType != 1 && userType != 4) {
                        sharePresenter.getPostListByLabelId(myUserId, postLableId, pageSize, pageNum);
                    } else {
                        sharePresenter.getMyShareList(myUserId, userId, pageSize, pageNum, false);
                    }
                }
            }
        });
        emptyView.setVisibility(View.GONE);

    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_GETMYSHARELIST) || apiName.equals(InterfaceUrl.URL_GETPOSTLISTBYLABELID)) {
            if (object instanceof GetMyShareListResponse || object instanceof GetPostListByLabelIdResponse) {

                if (apiName.equals(InterfaceUrl.URL_GETPOSTLISTBYLABELID)) {

                    GetPostListByLabelIdResponse getPostListByLabelIdResponse = (GetPostListByLabelIdResponse) object;

                    tile_bellow_text.setText("标签有" + getPostListByLabelIdResponse.getTotal() + "条发布");

                    if (getMyShareListResponse == null) {
                        getMyShareListResponse = new GetMyShareListResponse();
                    }

                    ArrayList<HomeInfo> homeInfoList = new ArrayList<>();

                    for (int i = 0; i < getPostListByLabelIdResponse.getList().size(); i++) {
                        HomeInfo homeInfo = new HomeInfo();
                        homeInfo.setType(1);
                        homeInfo.setPostInfo(getPostListByLabelIdResponse.getList().get(i));
                        homeInfo.setBaseUser(getPostListByLabelIdResponse.getList().get(i).getBaseUser());
                        homeInfoList.add(homeInfo);
                    }

                    getMyShareListResponse.setCode(200);
                    getMyShareListResponse.setList(homeInfoList);
                    getMyShareListResponse.setTotal(getPostListByLabelIdResponse.getTotal());
                    getMyShareListResponse.setBlankHint(getPostListByLabelIdResponse.getBlankHint());

                } else {
                    getMyShareListResponse = (GetMyShareListResponse) object;
                }

                if (getMyShareListResponse.getCode() == 200) {
                    refreshLayout.finishRefreshing();
                    refreshLayout.finishLoadmore();
                    ArrayList<HomeInfo> postInfoList = getMyShareListResponse.getList();
                    //帖子
                    if (!TextUtils.isEmpty(getMyShareListResponse.getBlankHint())) {
                        empty_tv.setText(getMyShareListResponse.getBlankHint());
                    }
                    if (postInfoList != null) {
                        if (pageNum == 1) {
                            refreshLayout.finishRefreshing();
                            postListAdapter.setList(postInfoList, getMyShareListResponse.getDefaultImg());
                        } else {
                            refreshLayout.finishLoadmore();
                            postListAdapter.addList(postInfoList);
                        }

                        postListAdapter.notifyDataSetChanged();
                        if (getMyShareListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                            finishView.setVisibility(View.GONE);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                            finishView.setVisibility(View.GONE);
                        }
                        if (getMyShareListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            finishView.setVisibility(View.GONE);
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                } else {
                    Utils.showToastShortTime(getMyShareListResponse.getMsg());
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
        } else if (InterfaceUrl.URL_UPDATEUSERSTATUS.equals(apiName)) {
            //进入房间
            if (object instanceof UpdateUserStatusResponse) {
                UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                if (response.getCode() == 200) {
                    Intent intent = new Intent(PostListActivity.this, GroupVideoActivity.class);
                    intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                    startActivity(intent);
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_FINDROOMDETAIL.equals(apiName)) {
            if (object instanceof FindRoomDetailResponse) {
                FindRoomDetailResponse findRoomDetailResponse = (FindRoomDetailResponse) object;
                if (findRoomDetailResponse.getCode() == 200) {
                    RoomInfo roomInfo = findRoomDetailResponse.getRoomInfo();
                    if (roomInfo.getStatus() == 0) {
                        Intent intent = new Intent(PostListActivity.this, OpenRoomSettingPassWordActivity.class);
                        intent.putExtra(Constants.Fields.TYPE, 2);
                        intent.putExtra(Constants.Fields.ROOMINFO, roomInfo);
                        startActivity(intent);
                    } else {
                        Utils.showToastShortTime("该聊天频道已关闭");
                    }
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    StoreDetail storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
                        StoreInfo storeInfo = storeDetail.getStoreInfo();
                        int isPublish = storeInfo.getIsPublish();
                        if (isPublish == 1) {
                            if (isClert == 1) {
                                //店长自己不跳转
                            } else if (isClert == 2) {
                                Utils.showToastShortTime("您已成为该店店员");
                            } else if (isClert == 3) {
                                //跳到店员申请界面
                                Intent intent = new Intent(PostListActivity.this, ApplyClerkActivity.class);
                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                                startActivity(intent);
                            } else if (isClert == 4) {
                                //店员申请中
                                Utils.showToastShortTime("店员申请中");
                            }
                        } else {
                            Utils.showToastShortTime("该店已关闭招聘信息");
                        }
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdatePostListEvent event) {
        if (event == null) {
            return;
        }

        if (event.getType() == 5) { // 删除帖子

            if (event.getPosition() > -1 && postListAdapter.getItemCount() > event.getPosition()) {
                postListAdapter.getList().remove(event.getPosition());
                postListAdapter.notifyItemRemoved(event.getPosition());
                postListAdapter.notifyItemRangeChanged(event.getPosition(), postListAdapter.getItemCount() - event.getPosition());
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
