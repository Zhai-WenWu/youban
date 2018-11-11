//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
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
//import cn.bjhdltcdn.p2plive.event.PublishObjectEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateSayloveListEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetSayLoveListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.SayLovePraiseResponse;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetCommentListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetSayLoveListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.SayLoveListAdapter;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * 表白列表
// */
//public class SayLoveListActivity extends BaseActivity implements BaseView {
//    private GetSayLoveListPresenter getSayLoveListPresenter;
//    private GetCommentListPresenter getCommentListPresenter;
//    private UserPresenter userPresenter;
//    private TextView orderTextView;
//    private RelativeLayout sortLayout;
//    private ImageView publishTextView;
//    private ListView listView;
//    private SayLoveListAdapter sayLoveListAdapter;
//    private View emptyView;
//    private int pageSize = 10, pageNum = 1;
//    private TextView finishView, sendView;
//    private LoadingView loadingView;
//    // 下拉刷新
//    private TwinklingRefreshLayout refreshLayout;
//    private Solve7PopupWindow popupWindow;
//    private TextView sortByTimeTextView, sortByHeatTextView, sortByDistanceTextView;
//    private RelativeLayout popView;
//    private int sort = 1;//排序(1最新发布,2离我最近,3最热表白4.本校发布),
//    private RelativeLayout headerLayout;
//    private View headView, sendCommentView;
//    private View hearViewContainer;
//    private EditText replyEditInput;
//    private int currentPosition;
//    private int mCommentCount;
//    private long userId;
//    private TextView empty_tv;
//    private int comeInType;//1:表白列表2：我的表白
//    private TitleFragment titleFragment;
//    private boolean showPopWindow;
//    private boolean needShowLoading = true;
//    private long myUserId;
//
//    public UserPresenter getUserPresenter() {
//        if (userPresenter == null) {
//            userPresenter = new UserPresenter(this);
//        }
//        return userPresenter;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_saylove_list);
//        comeInType = getIntent().getIntExtra("comeInType", 0);
//        myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        userId = getIntent().getLongExtra(Constants.Fields.USER_ID, 0);
//        initView();
//        getSayLoveListPresenter = new GetSayLoveListPresenter(this);
//        getCommentListPresenter = new GetCommentListPresenter(this);
//        if (comeInType == 1) {
//            getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//        } else if (comeInType == 2) {
//            if (userId == 0) {
//                userId = myUserId;
//            }
//            getSayLoveListPresenter.getMySayLoveList(myUserId, userId, pageSize, pageNum, true);
//        }
//
//    }
//
//    public void initView() {
//        EventBus.getDefault().register(this);
//        emptyView = findViewById(R.id.empty_view);
//        empty_tv = findViewById(R.id.empty_textView);
//        replyEditInput = findViewById(R.id.reply_edit_input);
//        replyEditInput = findViewById(R.id.reply_edit_input);
//        sendCommentView = findViewById(R.id.send_comment_view);
//
//
//        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refresh_layout_view);
//        listView = (ListView) findViewById(R.id.saylove_list_view);
//        sendView = (TextView) findViewById(R.id.send_view);
//        finishView = findViewById(R.id.finish_view);
//        sayLoveListAdapter = new SayLoveListAdapter(this);
//        sayLoveListAdapter.setUserPresenter(getUserPresenter());
//        sayLoveListAdapter.setType(comeInType);
//        if (comeInType == 1) {
//            hearViewContainer = getLayoutInflater().inflate(R.layout.saylove_list_header_layout, null);
//            headView = hearViewContainer.findViewById(R.id.header_layout);
//            ImageView backImageView = hearViewContainer.findViewById(R.id.back_img);
//            backImageView.setVisibility(View.VISIBLE);
//            backImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//            sortLayout = hearViewContainer.findViewById(R.id.sort_layout);
//            sortLayout.setVisibility(View.VISIBLE);
//            orderTextView = hearViewContainer.findViewById(R.id.order_text_view);
//            initPopWindow();
//            orderTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //弹出筛选顺序框
//                    if (!showPopWindow) {
//                        popupWindow.showAsDropDown(orderTextView);
//                        Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_open_icon);
//                        orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                        if (sort == 1) {
//                            sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                            sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                            sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        } else if (sort == 4) {
//                            sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                            sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                            sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                        } else if (sort == 3) {
//                            sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                            sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                            sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                        }
//                        showPopWindow = true;
//                    } else {
//                        popupWindow.dismiss();
//                        Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                        orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                        showPopWindow = false;
//                    }
//
//                }
//            });
//            listView.addHeaderView(hearViewContainer);
//            setTitle();
//            titleFragment.setHeight(0);
//        } else {
//            setTitle();
//
//            ImageView backImageView = findViewById(R.id.back_img);
//            if (backImageView != null) {
//                backImageView.setVisibility(View.GONE);
//            }
//
//            titleFragment.setHeight(44);
//        }
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (comeInType == 1) {
//                    if (position > 0) {
//                        //跳转到表白详情页
//                        Intent intent = new Intent(SayLoveListActivity.this, SayLoveDetailActivity.class);
//                        intent.putExtra("sayloveId", sayLoveListAdapter.getItem(position - 1).getSayLoveId());
//                        intent.putExtra("position", position - 1);
//                        startActivity(intent);
//                    }
//                } else {
//                    //跳转到表白详情页
//                    Intent intent = new Intent(SayLoveListActivity.this, SayLoveDetailActivity.class);
//                    intent.putExtra("sayloveId", sayLoveListAdapter.getItem(position).getSayLoveId());
//                    intent.putExtra("position", position);
//                    startActivity(intent);
//                }
//
//            }
//        });
//        sayLoveListAdapter.setOnClick(new SayLoveListAdapter.OnClick() {
//
//            private String commentSrring;
//
//            @Override
//            public void onPraise(long sayloveId, int type, int position) {
//                //点赞/取消点赞
//                currentPosition = position;
//                getCommentListPresenter.sayLovePraise(sayloveId, type, myUserId);
//            }
//
//            @Override
//            public void onComment(final long toUserId, final long sayloveId, int commentCount, int position) {
//                if (sendCommentView != null) {
//                    sendCommentView.setVisibility(View.VISIBLE);
//                    KeyBoardUtils.openKeybord(replyEditInput, SayLoveListActivity.this);
//                }
//                currentPosition = position;
//                mCommentCount = commentCount;
//                sendView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        commentSrring = replyEditInput.getText().toString();
//                        if ("".equals(commentSrring)) {
//                            Utils.showToastShortTime("评论内容不能为空");
//                        } else {
//                            getCommentListPresenter.sayLoveComment(sayloveId, commentSrring, 1, toUserId, userId, 0);
//
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void delete(long sayLoveId, int position) {
//                currentPosition = position;
//                getSayLoveListPresenter.deleteSayLove(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), sayLoveId);
//            }
//        });
//        listView.setAdapter(sayLoveListAdapter);
//
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        loadingView = new LoadingView(SayLoveListActivity.this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(listView);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//        refreshLayout.setEnableLoadmore(false);
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                if (comeInType == 1) {
//                    getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//                } else {
//                    getSayLoveListPresenter.getMySayLoveList(myUserId, userId, pageSize, pageNum, false);
//                }
//                refreshLayout.finishRefreshing();
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                if (!loadingView.isOnLoadFinish()) {
//                    if (comeInType == 1) {
//                        getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//                    } else {
//                        getSayLoveListPresenter.getMySayLoveList(myUserId, userId, pageSize, pageNum, false);
//                    }
//                }
//            }
//
//            @Override
//            public void onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction) {
//                super.onPullingDown(refreshLayout, fraction);
//                if (popupWindow != null && popupWindow.isShowing()) {
//                    popupWindow.dismiss();
//                    Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                    orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                    showPopWindow = false;
//                }
//            }
//        });
//
//        listView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        // 触摸按下时的操作
//                        if (popupWindow != null && popupWindow.isShowing()) {
//                            popupWindow.dismiss();
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                            showPopWindow = false;
//                        }
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        // 触摸移动时的操作
//                        if (popupWindow != null && popupWindow.isShowing()) {
//                            popupWindow.dismiss();
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                            showPopWindow = false;
//                        }
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        // 触摸抬起时的操作
//                        break;
//                }
//                return false;
//            }
//        });
//
//        final ImageView tabTopImageView = findViewById(R.id.tab_top);
//        tabTopImageView.setVisibility(View.GONE);
//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                sayLoveListAdapter.setRefreshItem(false);
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem > 0 && comeInType == 1) {
//                    tabTopImageView.setVisibility(View.VISIBLE);
//                } else {
//                    tabTopImageView.setVisibility(View.GONE);
//                }
//            }
//        });
//        tabTopImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listView.setSelection(0);
////                listView.smoothScrollToPosition(0);
//            }
//        });
//
//
//        emptyView.setVisibility(View.GONE);
//
//
//        publishTextView = findViewById(R.id.publish_image_view);
//        if (comeInType == 1) {
//            publishTextView.setVisibility(View.VISIBLE);
//        } else {
//            publishTextView.setVisibility(View.GONE);
//        }
//        publishTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳到发布表白页面
//                Intent intent = new Intent(SayLoveListActivity.this, PublishActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 3);
//                startActivity(intent);
//
//            }
//        });
//    }
//
//    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        if (userId == myUserId) {
//            titleFragment.setTitle(R.string.title_saylove);
//        } else {
//            titleFragment.setTitle("Ta发布的表白");
//        }
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
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
//                            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                            orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
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
//        sortByHeatTextView.setText("本校发布");
//        sortByDistanceTextView = popView.findViewById(R.id.order_by_diatance_tv);
//        sortByDistanceTextView.setText("最热表白");
//        sortByTimeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                orderTextView.setText(sortByTimeTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 1;
//                pageNum = 1;
//                getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        sortByHeatTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                orderTextView.setText(sortByHeatTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 4;
//                pageNum = 1;
//                getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        sortByDistanceTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                orderTextView.setText(sortByDistanceTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 3;
//                pageNum = 1;
//                getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, true);
//                refreshLayout.finishRefreshing();
//            }
//        });
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            String text = e.getMessage();
//            Utils.showToastShortTime(text);
//            if (refreshLayout != null) {
//                if (pageNum == 1) {
//                    refreshLayout.finishRefreshing();
//                } else {
//                    refreshLayout.finishLoadmore();
//                }
//            }
//            return;
//        }
//        if ((apiName.equals(InterfaceUrl.URL_GETSAYLOVELIST) || apiName.equals(InterfaceUrl.URL_GETMYSAYLOVELIST))) {
//            if (pageNum == 1) {
//                refreshLayout.finishRefreshing();
//            } else {
//                refreshLayout.finishLoadmore();
//            }
//            if (object instanceof GetSayLoveListResponse) {
//                GetSayLoveListResponse getSayLoveListResponse = (GetSayLoveListResponse) object;
//                if (getSayLoveListResponse.getCode() == 200) {
//                    List<SayLoveInfo> sayLoveInfoList = getSayLoveListResponse.getList();
//                    if (sayLoveInfoList != null) {
//                        if (pageNum == 1) {
//                            sayLoveListAdapter.setList(sayLoveInfoList);
//                        } else {
//                            sayLoveListAdapter.addList(sayLoveInfoList);
//                        }
//
//                        sayLoveListAdapter.notifyDataSetChanged();
//                        if (getSayLoveListResponse.getTotal() <= pageNum * pageSize) {
//                            //没有更多数据时  下拉刷新不可用
//                            refreshLayout.setEnableLoadmore(false);
//                            finishView.setVisibility(View.VISIBLE);
//                        } else {
//                            //有更多数据时  下拉刷新才可用
//                            refreshLayout.setEnableLoadmore(true);
//                            finishView.setVisibility(View.GONE);
//                            pageNum++;
//                        }
//
//                        if (getSayLoveListResponse.getTotal() == 0) {
//                            emptyView.setVisibility(View.VISIBLE);
//                            finishView.setVisibility(View.GONE);
//                            empty_tv.setText(getSayLoveListResponse.getBlankHint());
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                        }
//                    }
//                } else {
//                    Utils.showToastShortTime(getSayLoveListResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVEPRAISE)) {
//            if (object instanceof SayLovePraiseResponse) {
//                SayLovePraiseResponse sayLovePraiseResponse = (SayLovePraiseResponse) object;
//                if (sayLovePraiseResponse.getCode() == 200) {
//                    Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
//                    int praiseCount = sayLoveListAdapter.getItem(currentPosition).getPraiseCount();
//                    if (sayLovePraiseResponse.getIsPraise() == 1) {
//                        //点赞
//                        sayLoveListAdapter.getItem(currentPosition).setIsPraise(1);
//                        sayLoveListAdapter.getItem(currentPosition).setPraiseCount(praiseCount + 1);
//                        sayLoveListAdapter.setRefreshItem(true);
//                        sayLoveListAdapter.notifyDataSetChanged();
//                    } else if (sayLovePraiseResponse.getIsPraise() == 2) {
//                        //取消点赞
//                        sayLoveListAdapter.getItem(currentPosition).setIsPraise(0);
//                        sayLoveListAdapter.getItem(currentPosition).setPraiseCount(praiseCount - 1);
//                        sayLoveListAdapter.setRefreshItem(true);
//                        sayLoveListAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    Utils.showToastShortTime(sayLovePraiseResponse.getMsg());
//                }
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_DELETESAYLOVE)) {
//            if (object instanceof BaseResponse) {
//                BaseResponse baseResponse = (BaseResponse) object;
//                if (baseResponse.getCode() == 200) {
//                    sayLoveListAdapter.getList().remove(currentPosition);
//                    sayLoveListAdapter.notifyDataSetChanged();
//                    if (sayLoveListAdapter.getList().size() == 0) {
//                        emptyView.setVisibility(View.VISIBLE);
//                        finishView.setVisibility(View.GONE);
//                    }
//                }
//                Utils.showToastShortTime(baseResponse.getMsg());
//            }
//        } else if (apiName.equals(InterfaceUrl.URL_SAYLOVECOMMENT)) {
//            if (object instanceof SayLoveCommentResponse) {
//                SayLoveCommentResponse sayLoveCommentResponse = (SayLoveCommentResponse) object;
//                int code = sayLoveCommentResponse.getCode();
//                if (code == 200) {
//                    sayLoveListAdapter.getList().get(currentPosition).setCommentCount(mCommentCount + 1);
//                    sayLoveListAdapter.notifyDataSetChanged();
//                }
//                if (sendCommentView != null) {
//                    sendCommentView.setVisibility(View.GONE);
//                    KeyBoardUtils.closeKeybord(replyEditInput, this);
//                    replyEditInput.setText("");
//                }
//                Utils.showToastShortTime(sayLoveCommentResponse.getMsg());
//
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
//    public void onEventMainThread(UpdateSayloveListEvent event) {
//        if (event == null) {
//            return;
//        }
//        int position = event.getPosition();
//        //1:更新评论2：更新点赞3：删除表白4:发布表白
//        switch (event.getType()) {
//            case 1:
//                sayLoveListAdapter.updateCommentNum(position, event.getCommentNum());
//                break;
//            case 2:
//                sayLoveListAdapter.updateIsPraise(position, event.getIsPraise());
//                break;
//            case 3:
//                sayLoveListAdapter.deleteItem(position);
//                if (sayLoveListAdapter.getList().size() == 0) {
//                    finishView.setVisibility(View.GONE);
//                    emptyView.setVisibility(View.VISIBLE);
//                }
//                break;
//            case 4:
//                pageNum = 1;
//                getSayLoveListPresenter.getSayLoveList(myUserId, sort, pageSize, pageNum, false);
//                refreshLayout.finishRefreshing();
//                break;
//            default:
//                break;
//        }
//
//
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(final PublishObjectEvent event) {
//        if (event == null) {
//            return;
//        }
//
//        if (event.getType() == 8) {
//            if (event.getObject() instanceof HomeInfo) {
//                HomeInfo homeInfo = (HomeInfo) event.getObject();
//
//                if (sayLoveListAdapter != null) {
//                    sayLoveListAdapter.addSayLoveInfo(homeInfo.getSayLoveInfo());
//                }
//            }
//        }
//
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
