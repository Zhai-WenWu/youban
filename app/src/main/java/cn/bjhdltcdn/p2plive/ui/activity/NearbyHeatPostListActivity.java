//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.Switch;
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
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.event.UpdatePostListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.httpresponse.GetNearHotPostListResponse;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetNearHotPostListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.HomeRecommendPostListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 附近热帖列表
// */
//public class NearbyHeatPostListActivity extends BaseActivity implements BaseView {
//    private GetNearHotPostListPresenter presenter;
//
//    private PostCommentListPresenter postCommentListPresenter;
//    private TitleFragment titleFragment;
//    private TwinklingRefreshLayout refreshLayout;
//    private ListView listView;
//    private HomeRecommendPostListAdapter homeRecommendPostListAdapter;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView;
//    private LoadingView loadingView;
//    private View emptyView;
//    private Solve7PopupWindow popupWindow;
//    private TextView sortByTimeTextView, sortByDistanceTextView;
//    private RelativeLayout popView;
//    private int sort = 1;//排序(1最新发布,2离我最近,3最热表白),
//    private Long userId;
//    private int currentPosition;
//    private TextView empty_tv;
//    private View sendView;
//    private EditText editText;
//    private long mToUserId;
//    private long mPostId;
//    private int mPosition;
//    private TextView sendTextView;
//    private Switch anonymousSwitch;
//    private int anonymousSwitchIsChecked;
//    private boolean showPopWindow;
//    private boolean needShowLoading = true;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_nearby_heat_post_list);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        initView();
//        EventBus.getDefault().register(this);
//        setTitle();
//        presenter = new GetNearHotPostListPresenter(this);
//        postCommentListPresenter = new PostCommentListPresenter(this);
//        presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//    }
//
//    public void initView() {
//        emptyView = findViewById(R.id.empty_view);
//        empty_tv = findViewById(R.id.empty_textView);
//        sendView = findViewById(R.id.send_comment_view);
//        editText = findViewById(R.id.reply_edit_input);
//        sendTextView = findViewById(R.id.send_view);
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        listView = (ListView) findViewById(R.id.home_list_view);
//        finishView = (TextView) findViewById(R.id.finish_view);
//        anonymousSwitch = findViewById(R.id.anonymous_view);
//        anonymousSwitch.setVisibility(View.VISIBLE);
//        anonymousSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    anonymousSwitchIsChecked = 1;
//                }
//            }
//        });
//        homeRecommendPostListAdapter = new HomeRecommendPostListAdapter(this, 3);
//        homeRecommendPostListAdapter.setOnClick(new HomeRecommendPostListAdapter.OnClick() {
//            @Override
//            public void onPraise(long postId, int type, int position) {
//                //点赞/取消点赞
//                currentPosition = position;
//                postCommentListPresenter.postPraise(userId, postId, type);
//            }
//
//            @Override
//            public void onOrgain(long orgainId) {
//                //跳到圈子详情
//
//            }
//
//            @Override
//            public void onComment(long toUserId, long postId, int position) {
//                mToUserId = toUserId;
//                mPostId = postId;
//                mPosition = position;
//
//                if (sendView != null) {
//                    sendView.setVisibility(View.VISIBLE);
//                }
//
//                if (editText != null) {
//                    editText.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            KeyBoardUtils.openKeybord(editText, App.getInstance());
//                        }
//                    }, 500);
//
//                }
//            }
//
//            @Override
//            public void delete(long postId, int position) {
//
//            }
//
//            @Override
//            public void onApplyClerk(long storeId) {
//
//            }
//        });
//
//        sendTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String comment = editText.getText().toString();
//                if ("".equals(comment)) {
//                    Utils.showToastShortTime("评论不能为空");
//                } else {//String content, int type, long toUserId, long fromUserId, long postId, long parentId,int anonymousType
//                    postCommentListPresenter.postComment(comment, 1, mToUserId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), mPostId, 0, anonymousSwitchIsChecked);
//                }
//            }
//        });
//
//        listView.setAdapter(homeRecommendPostListAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (homeRecommendPostListAdapter.getItem(position).getContentLimit() != 2) {
//                    Intent intent = new Intent(NearbyHeatPostListActivity.this, PostDetailActivity.class);
//                    intent.putExtra(Constants.KEY.KEY_OBJECT, homeRecommendPostListAdapter.getItem(position));
//                    intent.putExtra(Constants.KEY.KEY_POSITION, position);
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 2);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        loadingView = new LoadingView(NearbyHeatPostListActivity.this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    pageNum++;
//                    presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//
//                }
//            }
//        });
//        emptyView.setVisibility(View.GONE);
//
//        initPopWindow();
//    }
//
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
//                            titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon);
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
//        sortByTimeTextView.setText("帖子时间");
//        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//        final TextView sortByHeatTextView = popView.findViewById(R.id.order_by_heat_tv);
//        sortByHeatTextView.setVisibility(View.GONE);
//        sortByDistanceTextView = popView.findViewById(R.id.order_by_diatance_tv);
//        sortByDistanceTextView.setText("作者距离");
//        sortByTimeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                titleFragment.setRightViewTitle(sortByTimeTextView.getText().toString(), R.mipmap.home_recommend_sort_close_icon);
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 1;
//                pageNum = 1;
//                presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//        });
////        sortByHeatTextView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                titleFragment.setRightViewTitle(sortByHeatTextView.getText().toString(), R.mipmap.home_recommend_sort_close_icon);
////                popupWindow.dismiss();
////                sort = 3;
////                pageNum = 1;
////                presenter.getNearHotPostList(userId, sort,pageSize,pageNum);
////                refreshLayout.finishRefreshing();
////            }
////        });
//        sortByDistanceTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                titleFragment.setRightViewTitle(sortByDistanceTextView.getText().toString(), R.mipmap.home_recommend_sort_close_icon);
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 2;
//                pageNum = 1;
//                presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_nearby_heat_post);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon, "帖子时间", new TitleFragment.RightViewClick() {
//
//            @Override
//            public void onClick() {
//                //弹出筛选顺序框
//                if (!showPopWindow) {
//                    popupWindow.showAsDropDown(titleFragment.getRightView());
//                    titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_open_icon);
//                    if (sort == 1) {
//                        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                        sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                    } else if (sort == 2) {
//                        sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                    }
//                    showPopWindow = true;
//                } else {
//                    popupWindow.dismiss();
//                    titleFragment.setRightViewTitle(R.mipmap.home_recommend_sort_close_icon);
//                    showPopWindow = false;
//                }
//            }
//        });
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (apiName.equals(InterfaceUrl.URL_GETNEARHOTPOSTLIST)) {
//            if (object instanceof GetNearHotPostListResponse) {
//                GetNearHotPostListResponse getNearHotPostListResponse = (GetNearHotPostListResponse) object;
//                List<PostInfo> postInfoList = getNearHotPostListResponse.getList();
//                if (postInfoList != null) {
//                    if (pageNum == 1) {
//                        refreshLayout.finishRefreshing();
//                        homeRecommendPostListAdapter.setList(postInfoList);
//                    } else {
//                        refreshLayout.finishLoadmore();
//                        homeRecommendPostListAdapter.setListAll(postInfoList);
//                    }
//
//                    homeRecommendPostListAdapter.notifyDataSetChanged();
//                    if (getNearHotPostListResponse.getTotal() <= pageNum * pageSize) {
//                        //没有更多数据时  下拉刷新不可用
//                        refreshLayout.setEnableLoadmore(false);
//                        finishView.setVisibility(View.VISIBLE);
//                    } else {
//                        //有更多数据时  下拉刷新才可用
//                        refreshLayout.setEnableLoadmore(true);
//                        finishView.setVisibility(View.GONE);
//                    }
//
//                    if (getNearHotPostListResponse.getTotal() == 0) {
//                        emptyView.setVisibility(View.VISIBLE);
//                        finishView.setVisibility(View.GONE);
//                        empty_tv.setText(getNearHotPostListResponse.getBlankHint());
//                    } else {
//                        emptyView.setVisibility(View.GONE);
//                    }
//
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_POSTPRAISE)) {
//            if (object instanceof PostPraiseResponse) {
//                PostPraiseResponse postPraiseResponse = (PostPraiseResponse) object;
//                if (postPraiseResponse.getCode() == 200) {
//                    Utils.showToastShortTime(postPraiseResponse.getMsg());
//                    int praiseCount = homeRecommendPostListAdapter.getItem(currentPosition).getPraiseCount();
//                    if (postPraiseResponse.getIsPraise() == 1) {
//                        //点赞
//                        homeRecommendPostListAdapter.getItem(currentPosition).setIsPraise(1);
//                        homeRecommendPostListAdapter.getItem(currentPosition).setPraiseCount(praiseCount + 1);
//                        homeRecommendPostListAdapter.notifyDataSetChanged();
//                    } else if (postPraiseResponse.getIsPraise() == 2) {
//                        //取消点赞
//                        homeRecommendPostListAdapter.getItem(currentPosition).setIsPraise(0);
//                        homeRecommendPostListAdapter.getItem(currentPosition).setPraiseCount(praiseCount - 1);
//                        homeRecommendPostListAdapter.notifyDataSetChanged();
//                    }
//
//                } else {
//                    Utils.showToastShortTime(postPraiseResponse.getMsg());
//                }
//            }
//        } else if (InterfaceUrl.URL_POSTCOMMENT.equals(apiName)) {
//            if (object instanceof PostCommentResponse) {
//                PostCommentResponse response = (PostCommentResponse) object;
//                Utils.showToastShortTime(response.getMsg());
//                if (response.getCode() == 200) {
//                    // 更新评论数
//                    PostInfo postInfo = (PostInfo) homeRecommendPostListAdapter.getList().get(mPosition);
//                    postInfo.setCommentCount(postInfo.getCommentCount() + 1);
//                    homeRecommendPostListAdapter.notifyDataSetChanged();
//                }
//                if (sendView != null) {
//                    sendView.setVisibility(View.GONE);
//                    KeyBoardUtils.closeKeybord(editText, this);
//                    editText.setText("");
//                }
//            }
//        }
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(this);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventMainThread(UpdatePostListEvent event) {
//        if (event == null) {
//            return;
//        }
//        if (event.getComeInType() == 2) {
//            int position = event.getPosition();
//            //1:更新评论2：更新点赞3：删除表白4:发布表白
//            switch (event.getType()) {
//                case 1:
//                    homeRecommendPostListAdapter.updateCommentNum(position, event.getCommentNum());
//                    break;
//                case 2:
//                    homeRecommendPostListAdapter.updateIsPraise(position, event.getIsPraise());
//                    break;
//                case 3:
//                    homeRecommendPostListAdapter.deleteItem(position);
//                    break;
//                case 4:
//                    pageNum = 1;
//                    presenter.getNearHotPostList(userId, sort, pageSize, pageNum);
//                    refreshLayout.finishRefreshing();
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
