package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.PublishObjectEvent;
import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
import cn.bjhdltcdn.p2plive.handler.AdvertisementHandler;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCommonLabelInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetDiscoverListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendOrganListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.model.RecommendInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyClerkActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ChatRoomActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostListActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAdvertisementTabAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomePostLabelRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeRecommendAssoiationRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeRecommendPostListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PostLabelInfoRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Hu_PC on 2017/11/8.
 * 首页推荐
 */

public class HomeRecommendFragment extends BaseFragment implements BaseView {
    private View rootView;
    private AppCompatActivity mActivity;
    private GetRecommendListPresenter getRecommendListPresenter;
    private PostCommentListPresenter postCommentListPresenter;
    private DiscoverPresenter discoverPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private UserPresenter userPresenter;
    private RecyclerView labelRecycleView;
    private HomePostLabelRecyclerViewAdapter homePostLabelRecyclerViewAdapter;
    private RecyclerView postLabelRecycleView;
    private PostLabelInfoRecycleAdapter postLabelInfoRecycleAdapter;
    private List<LabelInfo> labelInfoList;
    private int selectLabelPosition;
    public CustomViewPager viewPager;
    private TwinklingRefreshLayout refreshLayout;
    private View headOneView, headTwoView, headThreeView;
    private View hearOneViewContainer, hearTwoViewContainer;
    public AdvertisementHandler handler = new AdvertisementHandler(new WeakReference<BaseFragment>(this));
    private int previousPosition = 0;
    //    private HomeRecommendAssoiationRecyclerViewAdapter homeRecommendAssoiationRecyclerViewAdapter;
//    private RecyclerView associationRecycleView;
//    private TextView nextOrgain_tv;
    private ListView listView;
    private HomeRecommendPostListAdapter homeRecommendPostListAdapter;
    private int pageSize = 10, pageNum = 1;
    private int orgainPageSize = 10, orgainPageNum = 1;
    private TextView finishView;
    private LoadingView loadingView;
    private View emptyView;
    private Solve7PopupWindow popupWindow;
    private TextView sortByTimeTextView, sortByHeatTextView, sortByDistanceTextView;
    private RelativeLayout popView;
    private int sort = 1;//排序(1最新发布,2离我最近,3最热表白),
    private TextView sortTextView;
    private RequestOptions options;
    private int currentPosition, currentOraginPosition;
    private RelativeLayout roomLayout, roomUserLayout, roomAnonymousLayout, roomAnonymousUserLayout, orgainLayout, sortLayout;
    private long userId;
    private int joinType;
    private TextView empty_tv;
    private View sendView;
    private EditText editText;
    private TextView sendTextView;
    private Switch anonymousSwitch;
    private int anonymousSwitchIsChecked;
    private long mToUserId;
    private long mPostId;
    private int mPosition;
    private boolean showPopWindow;
    private boolean needShowLoading = true;
    private int forwadChatType;

    public GetRecommendListPresenter getGetRecommendListPresenter() {
        if (getRecommendListPresenter == null) {
            getRecommendListPresenter = new GetRecommendListPresenter(this);
        }
        return getRecommendListPresenter;
    }

    public PostCommentListPresenter getPostCommentListPresenter() {
        if (postCommentListPresenter == null) {
            postCommentListPresenter = new PostCommentListPresenter(this);
        }
        return postCommentListPresenter;
    }


    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home_recommend, null);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getGetRecommendListPresenter().getHomeBannerList(userId, 1);
//        getGetRecommendListPresenter().getRecommendOrganList(userId, orgainPageSize, orgainPageNum, false);
//        getChatRoomPresenter().getRoomList(userId, 5, 1);
        getDiscoverPresenter().getDiscoverList(userId, 5, 1, false);
        getGetRecommendListPresenter().getCommonLabelInfo(userId);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        if (labelInfoList == null || labelInfoList.size() < 1) {
            getDiscoverPresenter().getLabelList(3);
        } else {
            pageNum = 1;
            getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, true);
        }
    }

    @Override
    protected void onVisible(boolean isInit) {
        Logger.d("isInit === " + isInit);
//        if (isInit) {
//            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//            if (labelInfoList == null || labelInfoList.size() < 1) {
//                getDiscoverPresenter().getLabelList(3);
//            } else {
//                pageNum = 1;
//                getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, true);
//            }
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            //不可见时 popwindow 关闭
//            if (popupWindow != null && popupWindow.isShowing()) {
//                popupWindow.dismiss();
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                showPopWindow = false;
//            }
        }
    }

    private void initView() {
        //推荐帖子列表
        refreshLayout = (TwinklingRefreshLayout) rootView.findViewById(R.id.refresh_layout_view);
        listView = (ListView) rootView.findViewById(R.id.home_list_view);
        finishView = (TextView) rootView.findViewById(R.id.finish_view);
        sendView = rootView.findViewById(R.id.send_comment_view);
        editText = rootView.findViewById(R.id.reply_edit_input);
        sendTextView = rootView.findViewById(R.id.send_view);
        anonymousSwitch = rootView.findViewById(R.id.anonymous_view);
        anonymousSwitch.setVisibility(View.VISIBLE);
        anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    anonymousSwitchIsChecked = 1;
                }
            }
        });
        homeRecommendPostListAdapter = new HomeRecommendPostListAdapter(mActivity, 2);
        homeRecommendPostListAdapter.setUserPresenter(getUserPresenter());
        homeRecommendPostListAdapter.setOnClick(new HomeRecommendPostListAdapter.OnClick() {
            @Override
            public void onPraise(long postId, int type, int position) {
                //点赞/取消点赞
                PostInfo postInfo = homeRecommendPostListAdapter.getList().get(position);
                if (postInfo.getContentLimit() != 2) {
                    currentPosition = position;
                    getPostCommentListPresenter().postPraise(userId, postId, type);
                }
            }

            @Override
            public void onOrgain(long orgainId) {
                //跳到圈子详情

            }

            @Override
            public void onComment(long toUserId, long postId, int position) {
                PostInfo postInfo = homeRecommendPostListAdapter.getList().get(position);
                if (postInfo.getContentLimit() != 2) {
                    mToUserId = toUserId;
                    mPostId = postId;
                    mPosition = position;

                    if (sendView != null) {
                        sendView.setVisibility(View.VISIBLE);
                    }

                    if (editText != null) {
                        editText.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KeyBoardUtils.openKeybord(editText, App.getInstance());
                            }
                        }, 500);

                    }
                }
            }

            @Override
            public void delete(long postId, int position) {

            }

            @Override
            public void onApplyClerk(long storeId) {
                getStoreListPresenter().findStoreDetail(userId, storeId);
            }
        });

        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = editText.getText().toString();
                if ("".equals(comment)) {
                    Utils.showToastShortTime("评论不能为空");
                } else {//String content, int type, long toUserId, long fromUserId, long postId, long parentId,int anonymousType
                    getPostCommentListPresenter().postComment(comment, 1, mToUserId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), mPostId, 0, anonymousSwitchIsChecked);
                }
            }
        });

        //广告轮播图
        hearOneViewContainer = mActivity.getLayoutInflater().inflate(R.layout.home_ad_header_layout, null);
        headOneView = hearOneViewContainer.findViewById(R.id.layout_advertisement);
        headOneView.setBackgroundColor(getResources().getColor(R.color.color_ffffff));
        listView.addHeaderView(hearOneViewContainer);


        //除广告外的头部
        hearTwoViewContainer = mActivity.getLayoutInflater().inflate(R.layout.home_recommend_organ_header_layout, null);
        headTwoView = hearTwoViewContainer.findViewById(R.id.layout_recommend_organ);
        roomAnonymousLayout = hearTwoViewContainer.findViewById(R.id.room_anonymous_layout);
        roomAnonymousLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, ChatRoomActivity.class);
                intent.putExtra(Constants.Fields.TYPE, forwadChatType);
                mActivity.startActivity(intent);
            }
        });
        roomAnonymousUserLayout = hearTwoViewContainer.findViewById(R.id.room_anonymous_user_layout);
//        nextOrgain_tv = hearTwoViewContainer.findViewById(R.id.next_orgain_view);
//        nextOrgain_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orgainPageNum++;
//                getGetRecommendListPresenter().getRecommendOrganList(userId, orgainPageSize, orgainPageNum, true);
//            }
//        });
//        sortTextView = hearTwoViewContainer.findViewById(R.id.sort_tv);
//        orgainLayout = hearTwoViewContainer.findViewById(R.id.oragin_layout);
        sortLayout = hearTwoViewContainer.findViewById(R.id.sort_layout);

        labelRecycleView = hearTwoViewContainer.findViewById(R.id.recycler_label);
        homePostLabelRecyclerViewAdapter = new HomePostLabelRecyclerViewAdapter(this.getActivity());
        labelRecycleView.setHasFixedSize(true);
//        labelRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(0),3,false));

        homePostLabelRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectLabelPosition = position;
                LabelInfo labelInfo = (LabelInfo) homePostLabelRecyclerViewAdapter.getItem(position);
                labelInfo.setCheck(true);
                for (int i = 0; i < homePostLabelRecyclerViewAdapter.getList().size(); i++) {
                    LabelInfo labelInfo1 = (LabelInfo) homePostLabelRecyclerViewAdapter.getList().get(i);
                    if (labelInfo1.getLabelId() != labelInfo.getLabelId()) {
                        labelInfo1.setCheck(false);
                    }
                }
                homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                pageNum = 1;
                getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfo.getLabelId(), pageSize, pageNum, true);
            }
        });
        //热门标签
        postLabelRecycleView = hearTwoViewContainer.findViewById(R.id.recycler_hot_label);
        postLabelInfoRecycleAdapter = new PostLabelInfoRecycleAdapter(10, 1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        postLabelRecycleView.setLayoutManager(linearLayoutManager);
        postLabelRecycleView.setAdapter(postLabelInfoRecycleAdapter);
        postLabelInfoRecycleAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, PostListActivity.class);
                intent.putExtra(Constants.Fields.POST_LABEL_ID, postLabelInfoRecycleAdapter.getItem(position).getPostLabelId());
                startActivity(intent);
            }
        });

        emptyView = hearTwoViewContainer.findViewById(R.id.empty_view);
        empty_tv = hearTwoViewContainer.findViewById(R.id.empty_textView);
//        initPopWindow();
//        sortTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //弹出排序筛选框
//                if (popupWindow == null) {
//                    return;
//                }
//
//                if (!showPopWindow) {
//                    popupWindow.showAsDropDown(sortTextView);
//                    Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_open_icon);
//                    sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                    if (sort == 1) {
//                        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                        sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                    } else if (sort == 2) {
//                        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                    } else if (sort == 3) {
//                        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                        sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                    }
//                    showPopWindow = true;
//                } else {
//                    popupWindow.dismiss();
//                    Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                    sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                    showPopWindow = false;
//                }
//            }
//        });

        listView.addHeaderView(hearTwoViewContainer);
//        homeRecommendAssoiationRecyclerViewAdapter = new HomeRecommendAssoiationRecyclerViewAdapter(mActivity);
//        associationRecycleView = (RecyclerView) hearTwoViewContainer.findViewById(R.id.recommend_association_recycler_view);
//        associationRecycleView.setHasFixedSize(true);
//        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(15), false);
//        associationRecycleView.setLayoutManager(linearlayoutManager);
//        associationRecycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        associationRecycleView.setAdapter(homeRecommendAssoiationRecyclerViewAdapter);
//        homeRecommendAssoiationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到圈子详情页
//            }
//        });
//        homeRecommendAssoiationRecyclerViewAdapter.setOnClick(new HomeRecommendAssoiationRecyclerViewAdapter.OnClick() {
//            @Override
//            public void onJoinClick(int position, final int type) {
//                final long organId = homeRecommendAssoiationRecyclerViewAdapter.getItem(position).getOrganId();
//                joinType = type;
//                currentOraginPosition = position;
//                if (type == 2) {
//                    //申请加入圈子
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
//                        }
//                    });
//                    dialog.show(mActivity.getSupportFragmentManager());
//                } else {
//                    //直接加入圈子
//                }
//
//            }
//        });

        listView.setAdapter(homeRecommendPostListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //帖子详情
                PostInfo postInfo = homeRecommendPostListAdapter.getList().get(position - 2);
                if (postInfo.getContentLimit() != 2) {
                    Intent intent = new Intent(mActivity, PostDetailActivity.class);
                    intent.putExtra(Constants.KEY.KEY_OBJECT, homeRecommendPostListAdapter.getItem(position - 2));
                    intent.putExtra(Constants.KEY.KEY_POSITION, position - 2);
                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 0);
                    startActivity(intent);
                }
            }
        });

        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        loadingView = new LoadingView(mActivity);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
        //默认进来  下拉刷新不可用
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                if (labelInfoList == null || labelInfoList.size() < 1) {
                    getDiscoverPresenter().getLabelList(3);
                } else {
                    pageNum = 1;
                    getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, true);
                }
//                getChatRoomPresenter().getRoomList(userId, 5, 1);
                getDiscoverPresenter().getDiscoverList(userId, 5, 1, false);

            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                if (!loadingView.isOnLoadFinish()) {
//                    pageNum++;
                    getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, true);
                }
            }

            @Override
            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
                super.onPullingDown(refreshLayout, fraction);
//                if(popupWindow!=null&&popupWindow.isShowing()){
//                    popupWindow.dismiss();
//                    Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                    sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                    showPopWindow=false;
//                }
            }
        });

//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // 触摸按下时的操作
//                        if(popupWindow!=null&&popupWindow.isShowing()){
//                            popupWindow.dismiss();
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                            showPopWindow=false;
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        // 触摸移动时的操作
//                        if(popupWindow!=null&&popupWindow.isShowing()){
//                            popupWindow.dismiss();
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                            showPopWindow=false;
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        // 触摸抬起时的操作
//                        break;
//                }
//                return false;
//            }
//        });

        final ImageView tabTopImageView = rootView.findViewById(R.id.tab_top);
        tabTopImageView.setVisibility(View.GONE);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                homeRecommendPostListAdapter.setRefreshItem(false);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 1) {
                    tabTopImageView.setVisibility(View.VISIBLE);
                } else {
                    tabTopImageView.setVisibility(View.GONE);
                }
            }
        });
        tabTopImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelection(0);
//                listView.setSelectionAfterHeaderView();
//                listView.smoothScrollToPosition(0);
            }
        });
        emptyView.setVisibility(View.GONE);

    }

//    private void initPopWindow() {
//        popView = (RelativeLayout) getLayoutInflater().inflate(R.layout.screening_order_popwindow_layout, null, false);
//        popView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (v instanceof ViewGroup) {
//                    View layoutView = ((ViewGroup) v).getChildAt(0);
//                    if (v != null) {
//                        float y = event.getY();
//                        float x = event.getX();
//                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
//                        if (!rect.contains((int) x, (int) y)) {
//                            popupWindow.dismiss();
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                            showPopWindow = false;
//                        }
//                        rect.setEmpty();
//                        rect = null;
//                    }
//                }
//                return false;
//            }
//        });
//        sortByTimeTextView = popView.findViewById(R.id.order_by_time_tv);
//        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//        sortByHeatTextView = popView.findViewById(R.id.order_by_heat_tv);
//        sortByDistanceTextView = popView.findViewById(R.id.order_by_diatance_tv);
//        sortByTimeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                sortTextView.setText(sortByTimeTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 1;
//                pageNum = 1;
//                getGetRecommendListPresenter().getPostList(userId, 0, sort,0, pageSize, pageNum, false);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        sortByHeatTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                sortTextView.setText(sortByHeatTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 3;
//                pageNum = 1;
//                getGetRecommendListPresenter().getPostList(userId, 0, sort,0, pageSize, pageNum, false);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        sortByDistanceTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                sortTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                sortTextView.setText(sortByDistanceTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 2;
//                pageNum = 1;
//                getGetRecommendListPresenter().getPostList(userId, 0, sort, 0,pageSize, pageNum, false);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//
//    }


    public void setHeaderViewData(List<RecommendInfo> list) {
        try {
            options = new RequestOptions()
                    .centerCrop()
//                    .transform(new GlideRoundTransform(9))
                    .placeholder(R.mipmap.error_bg)
                    .error(R.mipmap.error_bg);
            List<RecommendInfo> arrayList = null;
            final LinearLayout llPointGroup;
            if (arrayList == null) {
                arrayList = new ArrayList<RecommendInfo>();
            }
            arrayList.clear();
            if (list != null && list.size() > 0) {
                arrayList.addAll(list);
            } else {
                return;
            }
            List<View> viewList = new ArrayList<View>();
            viewPager = (CustomViewPager) headOneView.findViewById(R.id.advertisement_head_view_page);
            llPointGroup = (LinearLayout) headOneView.findViewById(R.id.ll_point_group);
            llPointGroup.removeAllViews();
            View advertisementView;
            String recommendImgUrl = "";
            for (int i = 0; i < list.size(); i++) {
                advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
                ImageView advertisementImage = (ImageView) advertisementView.findViewById(R.id.advertisement_image);
                final RecommendInfo recommendInfo = arrayList.get(i);
                recommendImgUrl = recommendInfo.getImgUrl();
                Glide.with(App.getInstance()).asBitmap().load(recommendImgUrl).apply(options).into(advertisementImage);
                viewList.add(advertisementView);
                advertisementView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String gotoUrl = recommendInfo.getGotoUrl();
                        if (!TextUtils.isEmpty(gotoUrl)) {
                            Intent intent = new Intent(getContext(), WXPayEntryActivity.class);
                            intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
                            intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
                            startActivity(intent);
                        }
                    }
                });
                if (list.size() == 1) {
                    break;
                }
                //每循环一次需要向LinearLayout中添加一个点的view对象
                View v = new View(App.getInstance());
                v.setBackgroundResource(R.drawable.point_bg);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(14, 14);
                params.leftMargin = 9;
                params.rightMargin = 9;
                v.setLayoutParams(params);
                v.setEnabled(false);
                llPointGroup.addView(v);
            }

            HomeAdvertisementTabAdapter homeAdvertisementTabAdapter = new HomeAdvertisementTabAdapter(viewList);
            viewPager.setAdapter(homeAdvertisementTabAdapter);
            viewPager.setIsCanScroll(false);
            viewPager.setCurrentItem(0);
            if (list.size() > 1) {
                llPointGroup.getChildAt(previousPosition).setEnabled(true);
                //开始轮播效果
                handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
            }

            final List<RecommendInfo> finalArrayList = arrayList;
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int arg1) {
                    int position = arg1 % finalArrayList.size();
                    // 把当前选中的点给切换了, 还有描述信息也切换
                    llPointGroup.getChildAt(previousPosition).setEnabled(false);
                    llPointGroup.getChildAt(position).setEnabled(true);
                    // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                    previousPosition = position;
                    handler.sendMessage(Message.obtain(handler, AdvertisementHandler.MSG_PAGE_CHANGED, arg1, 0));
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                //覆写该方法实现轮播效果的暂停和恢复
                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // 当页面的状态改变时将调用此方法
                    //arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
                    Log.d("onPageScrollStateChange", arg0 + "");
                    switch (arg0) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            // 正在拖动页面时执行此处
                            handler.sendEmptyMessage(AdvertisementHandler.MSG_KEEP_SILENT);
                            break;
                        case ViewPager.SCROLL_STATE_IDLE:
                            // 未拖动页面时执行此处
                            handler.sendEmptyMessageDelayed(AdvertisementHandler.MSG_UPDATE_IMAGE, AdvertisementHandler.MSG_DELAY);
                            break;
                        default:
                            break;
                    }
                }

            });


            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headOneView.getLayoutParams();
            params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 37 / 75;
            headOneView.setLayoutParams(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (refreshLayout != null) {
                if (pageNum == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETPOSTLIST)) {
            if (object instanceof GetPostListResponse) {
                if (pageNum == 1) {
                    refreshLayout.finishRefreshing();
                } else {
                    refreshLayout.finishLoadmore();
                }
                GetPostListResponse getPostListResponse = (GetPostListResponse) object;
                if (getPostListResponse.getCode() == 200) {
                    List<PostInfo> postInfoList = getPostListResponse.getPostList();
                    if (postInfoList != null) {
                        if (pageNum == 1) {
                            homeRecommendPostListAdapter.setList(postInfoList);
                        } else {
                            homeRecommendPostListAdapter.setListAll(postInfoList);
                        }
                        homeRecommendPostListAdapter.notifyDataSetChanged();
                        if (getPostListResponse.getTotal() <= pageNum * pageSize) {
                            //没有更多数据时  下拉刷新不可用
                            refreshLayout.setEnableLoadmore(false);
                            finishView.setVisibility(View.VISIBLE);
                        } else {
                            //有更多数据时  下拉刷新才可用
                            refreshLayout.setEnableLoadmore(true);
                            finishView.setVisibility(View.GONE);
                            pageNum++;
                        }
                        if (getPostListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            finishView.setVisibility(View.GONE);
                            empty_tv.setText(getPostListResponse.getBlankHint());
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                    }
                } else if (getPostListResponse.getCode() == 201) {
                    homeRecommendPostListAdapter.setList(null);
                    homeRecommendPostListAdapter.notifyDataSetChanged();
                    emptyView.setVisibility(View.VISIBLE);
                    finishView.setVisibility(View.GONE);
                    empty_tv.setText(getPostListResponse.getMsg());
                    refreshLayout.setEnableLoadmore(false);
                } else {
                    Utils.showToastShortTime(getPostListResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETHOMEBANNERLIST)) {
            if (object instanceof GetHomeBannerListResponse) {
                GetHomeBannerListResponse getHomeBannerListResponse = (GetHomeBannerListResponse) object;
                if (getHomeBannerListResponse.getCode() == 200) {
                    List<RecommendInfo> recommendInfoList = getHomeBannerListResponse.getList();
                    if (recommendInfoList != null && recommendInfoList.size() > 0) {
                        hearOneViewContainer.setVisibility(View.VISIBLE);
                        setHeaderViewData(recommendInfoList);
                    } else {
                        hearOneViewContainer.setVisibility(View.GONE);
                    }
                } else {
                    Utils.showToastShortTime(getHomeBannerListResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETRECOMMENDORGANLIST)) {
            if (object instanceof GetRecommendOrganListResponse) {
                GetRecommendOrganListResponse getRecommendOrganListResponse = (GetRecommendOrganListResponse) object;
//                if (getRecommendOrganListResponse.getCode() == 200) {
//                    List<OrganizationInfo> organizationInfolist = getRecommendOrganListResponse.getRecommendList();
//                    if (organizationInfolist != null && organizationInfolist.size() > 0) {
//                        orgainLayout.setVisibility(View.VISIBLE);
//                        homeRecommendAssoiationRecyclerViewAdapter.setList(organizationInfolist);
//                        homeRecommendAssoiationRecyclerViewAdapter.notifyDataSetChanged();
//                    } else {
//                        orgainLayout.setVisibility(View.GONE);
//                    }
//                    int total = getRecommendOrganListResponse.getTotal();
//                    if (total > orgainPageSize) {
//                        nextOrgain_tv.setVisibility(View.VISIBLE);
//                    } else {
//                        nextOrgain_tv.setVisibility(View.INVISIBLE);
//                    }
//                    if (total == orgainPageNum * orgainPageSize) {
//                        orgainPageNum = 0;
//                    }
//                } else {
//                    Utils.showToastShortTime(getRecommendOrganListResponse.getMsg());
//                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_GETDISCOVERLIST)) {
            if (object instanceof GetDiscoverListResponse) {
                GetDiscoverListResponse getDiscoverListResponse = (GetDiscoverListResponse) object;
                if (getDiscoverListResponse.getCode() == 200) {
                    forwadChatType = 0;
                    List<ChatInfo> chatInfoList = getDiscoverListResponse.getChatRoomList();
                    List<RoomInfo> roomList = getDiscoverListResponse.getRoomList();
                    List<BaseUser> baseUserList = new ArrayList<BaseUser>();
                    if (chatInfoList != null && chatInfoList.size() > 0) {
                        for (int i = 0; i < chatInfoList.size(); i++) {
                            baseUserList.add(chatInfoList.get(i).getBaseUser());
                        }
                    } else {
                        forwadChatType = 1;
                    }
                    if (baseUserList.size() < 5) {
                        if (roomList != null && roomList.size() > 0) {
                            for (int i = 0; i < roomList.size(); i++) {
                                if (baseUserList.size() < 5) {
                                    baseUserList.add(roomList.get(i).getBaseUser());
                                } else {
                                    break;
                                }
                            }
                        }
                    }
                    roomAnonymousUserLayout.removeAllViews();
                    if (baseUserList != null && baseUserList.size() > 0) {
                        roomAnonymousLayout.setVisibility(View.VISIBLE);
                        int friendLyoutWidth = 0;
                        for (int i = baseUserList.size() - 1; i >= 0; i--) {
                            if (i == baseUserList.size() - 1) {
                                CircleImageView friendImage = new CircleImageView(mActivity);
                                friendImage.setBorderColor(mActivity.getResources().getColor(R.color.white));
                                friendImage.setBorderWidth(Utils.dp2px(0.5f));
                                Glide.with(App.getInstance()).load(baseUserList.get(i).getUserIcon()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon)).into(friendImage);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dp2px(26), Utils.dp2px(26));
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                friendImage.setLayoutParams(params);
                                roomAnonymousUserLayout.addView(friendImage);
                                friendLyoutWidth = Utils.dp2px(26);
                            } else {
                                CircleImageView friendImage = new CircleImageView(mActivity);
                                friendImage.setBorderColor(mActivity.getResources().getColor(R.color.white));
                                friendImage.setBorderWidth(Utils.dp2px(0.5f));
                                Glide.with(App.getInstance()).load(baseUserList.get(i).getUserIcon()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon)).into(friendImage);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(Utils.dp2px(26), Utils.dp2px(26));
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                params.setMargins(0, 0, Utils.dp2px(20 * (baseUserList.size() - 1 - i)), 0);
                                friendImage.setLayoutParams(params);
                                roomAnonymousUserLayout.addView(friendImage);
                                friendLyoutWidth += Utils.dp2px(20);
                            }
                        }
                        roomAnonymousUserLayout.getLayoutParams().width = friendLyoutWidth;
                    } else {
                        roomAnonymousLayout.setVisibility(View.GONE);
                    }
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_POSTPRAISE)) {
            if (object instanceof PostPraiseResponse) {
                PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
                if (postPraiseResponse.getCode() == 200) {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                    int praiseCount = homeRecommendPostListAdapter.getItem(currentPosition).getPraiseCount();
                    if (postPraiseResponse.getIsPraise() == 1) {
                        //点赞
                        homeRecommendPostListAdapter.getItem(currentPosition).setIsPraise(1);
                        homeRecommendPostListAdapter.getItem(currentPosition).setPraiseCount(praiseCount + 1);
                        homeRecommendPostListAdapter.setRefreshItem(true);
                        homeRecommendPostListAdapter.notifyDataSetChanged();
                    } else if (postPraiseResponse.getIsPraise() == 2) {
                        //取消点赞
                        homeRecommendPostListAdapter.getItem(currentPosition).setIsPraise(0);
                        homeRecommendPostListAdapter.getItem(currentPosition).setPraiseCount(praiseCount - 1);
                        homeRecommendPostListAdapter.setRefreshItem(true);
                        homeRecommendPostListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Utils.showToastShortTime(postPraiseResponse.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_JOINORGANIZATION.equals(apiName)) {

//            if (object instanceof BaseResponse) {
//                BaseResponse response = (BaseResponse) object;
//                if (response.getCode() == 200) {
//                    Utils.showToastShortTime(response.getMsg());
//                    if (joinType == 1) {
//                        //加入成功
//                        homeRecommendAssoiationRecyclerViewAdapter.getList().get(currentOraginPosition).setUserRole(3);
//                    } else {
//                        //申请成功
//                        homeRecommendAssoiationRecyclerViewAdapter.getList().get(currentOraginPosition).setUserRole(4);
//
//                    }
//                    homeRecommendAssoiationRecyclerViewAdapter.notifyItemChanged(currentOraginPosition);
//                } else {
//                    Utils.showToastShortTime(response.getMsg());
//                }
//            }
        } else if (InterfaceUrl.URL_POSTCOMMENT.equals(apiName)) {
            if (object instanceof PostCommentResponse) {
                PostCommentResponse response = (PostCommentResponse) object;
                Utils.showToastShortTime(response.getMsg());
                if (response.getCode() == 200) {
                    // 更新评论数
                    PostInfo postInfo = (PostInfo) homeRecommendPostListAdapter.getList().get(mPosition);
                    postInfo.setCommentCount(postInfo.getCommentCount() + 1);
                    homeRecommendPostListAdapter.notifyDataSetChanged();
                }
                if (sendView != null) {
                    sendView.setVisibility(View.GONE);
                    KeyBoardUtils.closeKeybord(editText, getActivity());
                    editText.setText("");
                }
            }
        } else if (InterfaceUrl.URL_GETLABELLIST.equals(apiName)) {
            if (object instanceof GetLabelListResponse) {
                GetLabelListResponse getLabelListResponse = (GetLabelListResponse) object;
                if (getLabelListResponse.getCode() == 200) {
                    labelInfoList = getLabelListResponse.getLabelList();
                    if (labelInfoList != null && labelInfoList.size() > 0) {
                        labelInfoList.get(0).setCheck(true);
                        List<Object> list = new ArrayList<Object>();
                        list.addAll(labelInfoList);
                        homePostLabelRecyclerViewAdapter.setList(list);
                        homePostLabelRecyclerViewAdapter.notifyDataSetChanged();
                        if (homePostLabelRecyclerViewAdapter.getItemCount() > 0) {
                            GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), labelInfoList.size());
                            labelRecycleView.setLayoutManager(layoutManager);
                            labelRecycleView.setAdapter(homePostLabelRecyclerViewAdapter);
                            labelRecycleView.setVisibility(View.VISIBLE);
                            pageNum = 1;
                            getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, true);
                        } else {
                            labelRecycleView.setVisibility(View.GONE);
                        }
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
                                Intent intent = new Intent(mActivity, ApplyClerkActivity.class);
                                intent.putExtra(Constants.Fields.STORE_ID, storeInfo.getStoreId());
                                mActivity.startActivity(intent);
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
        } else if (InterfaceUrl.URL_GETCOMMONLABELINFO.equals(apiName)) {
            if (object instanceof GetCommonLabelInfoResponse) {
                GetCommonLabelInfoResponse getCommonLabelInfoResponse = (GetCommonLabelInfoResponse) object;
                if (getCommonLabelInfoResponse.getCode() == 200) {
                    List<PostLabelInfo> list = getCommonLabelInfoResponse.getList();
                    if (list != null && list.size() > 0) {
                        postLabelInfoRecycleAdapter.setList(list);
                        postLabelInfoRecycleAdapter.notifyDataSetChanged();
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
        if (event.getComeInType() == 0) {
            int position = event.getPosition();
            //1:更新评论2：更新点赞3：删除表白4:发布表白
            switch (event.getType()) {
                case 1:
                    homeRecommendPostListAdapter.updateCommentNum(position, event.getCommentNum());
                    break;
                case 2:
                    homeRecommendPostListAdapter.updateIsPraise(position, event.getIsPraise());
                    break;
                case 3:
                    homeRecommendPostListAdapter.deleteItem(position);
                    break;
                case 4:
                    if (labelInfoList == null || labelInfoList.size() < 1) {
                        getDiscoverPresenter().getLabelList(3);
                    } else {
                        pageNum = 1;
                        getGetRecommendListPresenter().getPostList(userId, 0, sort, labelInfoList.get(selectLabelPosition).getLabelId(), pageSize, pageNum, false);
                    }
                    refreshLayout.finishRefreshing();
                    break;
                default:
                    break;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final PublishObjectEvent event) {
        if (event == null) {
            return;
        }

        if (event.getType() == 1) {
            if (event.getObject() instanceof HomeInfo) {
                HomeInfo homeInfo = (HomeInfo) event.getObject();

                if (homePostLabelRecyclerViewAdapter.getItemCount() > selectLabelPosition) {
                    LabelInfo labelInfo = (LabelInfo) homePostLabelRecyclerViewAdapter.getItem(selectLabelPosition);

                    // 看比赛
                    if (labelInfo.getLabelId() == 20 && event.getLabelId() > 0) {
                        homeRecommendPostListAdapter.addPostInfo(homeInfo.getPostInfo());
                    } else if (labelInfo.getLabelId() == 17) {//按时间
                        homeRecommendPostListAdapter.addPostInfo(homeInfo.getPostInfo());
                    }

                }
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (getRecommendListPresenter != null) {
            getRecommendListPresenter.onDestroy();
        }

        if (postCommentListPresenter != null) {
            postCommentListPresenter.onDestroy();
        }


        if (handler != null) {
            handler.clearData();
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (viewPager != null) {
            viewPager.removeAllViews();
            viewPager = null;
        }

        EventBus.getDefault().unregister(this);
    }


}
