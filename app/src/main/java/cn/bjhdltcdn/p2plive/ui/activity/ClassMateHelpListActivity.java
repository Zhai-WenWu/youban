//package cn.bjhdltcdn.p2plive.ui.activity;
//
//import android.content.Intent;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutCompat;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
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
//import cn.bjhdltcdn.p2plive.event.ClassMateHelpEvent;
//import cn.bjhdltcdn.p2plive.event.PublishObjectEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetClassmateHelpListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.HelpPraiseResponse;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.adapter.ClassMateHelpListAdapter;
//import cn.bjhdltcdn.p2plive.ui.dialog.AskDialog;
//import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.LoadingView;
//
///**
// * Created by ZHAI on 2018/2/22.
// */
//
//public class ClassMateHelpListActivity extends BaseActivity implements BaseView {
//
//    private ListView ll_schoolmate_help;
//    private View emptyView;
//    private ClassMateHelpListAdapter classMateHelpListAdapter;
//    private int HOME_COMEIN = 1;
//    private int MY_COMEIN = 2;
//    private int type;//1,首页。2，我的
//    private TitleFragment titleFragment;
//    private Solve7PopupWindow popupWindow;
//    private boolean showPopWindow;
//    private int sort = 2;
//    private RelativeLayout popView;
//    private TextView orderTextView;
//    private RelativeLayout sortLayout;
//    private TextView sortByTimeTextView;
//    private TextView sortByHeatTextView;
//    private TextView sortByDistanceTextView;
//    private TextView sortBySchoolMateTextView;
//    private ImageView publish_image_view;
//    private ClassMateHelpPresenter classMateHelpPresenter;
//    private long userId;
//    private long toUserId;
//    private int pageSize = 20, pageNum = 1;
//    private TwinklingRefreshLayout refreshLayout;
//    private TextView empty_tv;
//    private int praisePosition;
//    private int deletePosition;
//    private ImageView iv_scroll_top;
//    private View footerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_schoolmate_help_list);
//        EventBus.getDefault().register(this);
//        setTile();
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        ll_schoolmate_help = findViewById(R.id.ll_schoolmate_help);
//        iv_scroll_top = findViewById(R.id.iv_scroll_top);
//        empty_tv = findViewById(R.id.empty_textView);
//        emptyView = findViewById(R.id.empty_view);
//        publish_image_view = findViewById(R.id.publish_image_view);
//        refreshLayout = findViewById(R.id.refresh_layout_view);
//        classMateHelpListAdapter = new ClassMateHelpListAdapter(ClassMateHelpListActivity.this);
//        classMateHelpListAdapter.setType(type);
//        ll_schoolmate_help.setAdapter(classMateHelpListAdapter);
//        if (type == HOME_COMEIN) {
//            View hearViewContainer = getLayoutInflater().inflate(R.layout.schoolmate_help_list_header_layout, null);
//            ll_schoolmate_help.addHeaderView(hearViewContainer, null, false);
//            sortLayout = hearViewContainer.findViewById(R.id.sort_layout);
//            orderTextView = hearViewContainer.findViewById(R.id.order_text_view);
//        }
//        ll_schoolmate_help.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//                classMateHelpListAdapter.setPraiceRefresh(true);
//            }
//
//            @Override
//            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                if (i != 0 && type != MY_COMEIN) {
//                    iv_scroll_top.setVisibility(View.VISIBLE);
//                } else {
//                    iv_scroll_top.setVisibility(View.GONE);
//                }
//            }
//        });
//
//        iv_scroll_top.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ll_schoolmate_help.setSelection(0);
////                ll_schoolmate_help.smoothScrollToPosition(0);
//            }
//        });
//
//        classMateHelpListAdapter.setOnBottomClick(new ClassMateHelpListAdapter.OnBottomClick() {
//            @Override
//            public void onDelete(final int position, final long helpId) {
//                new AskDialog.Builder()
//                        .content("确定删除帮帮忙吗？")
//                        .leftBtnText("取消")
//                        .rightBtnText("确定")
//                        .rightClickListener(new AskDialog.OnRightClickListener() {
//                            @Override
//                            public void onClick() {
//                                deletePosition = position;
//                                getClassMateHelpPresenter().deleteHelp(userId, helpId);
//                            }
//                        })
//                        .build()
//                        .show(getSupportFragmentManager());
//            }
//
//            @Override
//            public void onPraise(int position, long helpId, int isPraise) {
//                praisePosition = position;
//                getClassMateHelpPresenter().helpPraise(userId, helpId, isPraise == 1 ? 2 : 1);
//            }
//
//
//            @Override
//            public void onShare(int position, long helpId, String imageUrl) {
//
//            }
//        });
//
//        ll_schoolmate_help.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(ClassMateHelpListActivity.this, ClassMateHelpDetailActivity.class);
//                intent.putExtra(Constants.Fields.COME_IN_TYPE, type);
//                intent.putExtra(Constants.Fields.TYPE, Constants.Constant.HOME_TYPE_HELPINFO);
//                if (type == HOME_COMEIN) {
//
//                    if ((i - 1) > -1 && (i - 1) < ll_schoolmate_help.getAdapter().getCount()) {
//                        intent.putExtra(Constants.Fields.POSITION, i - 1);
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, classMateHelpListAdapter.getmList().get(i - 1).getHelpId());
//                        intent.putExtra(Constants.KEY.KEY_OBJECT, classMateHelpListAdapter.getmList().get(i - 1));
//                        startActivity(intent);
//                    }
//
//                } else {
//
//                    if (i > -1 && i < ll_schoolmate_help.getAdapter().getCount()) {
//                        intent.putExtra(Constants.Fields.POSITION, i);
//                        intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, classMateHelpListAdapter.getmList().get(i).getHelpId());
//                        intent.putExtra(Constants.KEY.KEY_OBJECT, classMateHelpListAdapter.getmList().get(i));
//                        startActivity(intent);
//                    }
//
//                }
//
//            }
//        });
//
//        ProgressLayout header = new ProgressLayout(App.getInstance());
//        refreshLayout.setHeaderView(header);
//        // 底部加载样式
//        LoadingView loadingView = new LoadingView(this);
//        refreshLayout.setBottomView(loadingView);
//        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
//        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
//        refreshLayout.setHeaderHeight(50);
//        refreshLayout.setMaxHeadHeight(120);
//        refreshLayout.setTargetView(ll_schoolmate_help);//设置滚动事件的作用对象。
//        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
//        refreshLayout.setEnableLoadmore(true);
//        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式
//
//        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
//            @Override
//            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
//                pageNum = 1;
//                if (type == HOME_COMEIN) {
//                    getClassMateHelpPresenter().getClassmateHelpList(userId, sort, pageSize, pageNum, false);
//                } else if (type == MY_COMEIN) {
//                    getClassMateHelpPresenter().getPublishHelpList(userId, toUserId, pageSize, pageNum, false);
//                }
//            }
//
//            @Override
//            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
//                refreshLayout.finishLoadmore();
//                pageNum += 1;
//                if (type == HOME_COMEIN) {
//                    getClassMateHelpPresenter().getClassmateHelpList(userId, sort, pageSize, pageNum, false);
//                } else if (type == MY_COMEIN) {
//                    getClassMateHelpPresenter().getPublishHelpList(userId, toUserId, pageSize, pageNum, false);
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
//        publish_image_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ClassMateHelpListActivity.this, PublishActivity.class);
//                intent.putExtra(Constants.Fields.TYPE, 4);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void initData() {
//        if (type == HOME_COMEIN) {
//            getClassMateHelpPresenter().getClassmateHelpList(userId, 2, pageSize, pageNum, true);
//            ImageView backImageView = findViewById(R.id.back_img);
//            backImageView.setVisibility(View.VISIBLE);
//            backImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });
//            sortLayout.setVisibility(View.VISIBLE);
//            initPopWindow();
//            orderTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //弹出筛选顺序框
//                    showPopview();
//                }
//
//
//            });
//            titleFragment.setHeight(0);
//        } else if (type == MY_COMEIN) {
//            getClassMateHelpPresenter().getPublishHelpList(userId, toUserId, pageSize, pageNum, true);
//            titleFragment.setHeight(44);
//            publish_image_view.setVisibility(View.GONE);
//        }
//    }
//
//    private void showPopview() {
//        if (!showPopWindow) {
//            popupWindow.showAsDropDown(orderTextView);
//            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_open_icon);
//            orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//            if (sort == 1) {
//                sortBySchoolMateTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//            } else if (sort == 2) {
//                sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortBySchoolMateTextView.setTextColor(getResources().getColor(R.color.color_999999));
//            } else if (sort == 3) {
//                sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                sortBySchoolMateTextView.setTextColor(getResources().getColor(R.color.color_999999));
//            } else if (sort == 4) {
//                sortByTimeTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortByHeatTextView.setTextColor(getResources().getColor(R.color.color_333333));
//                sortByDistanceTextView.setTextColor(getResources().getColor(R.color.color_999999));
//                sortBySchoolMateTextView.setTextColor(getResources().getColor(R.color.color_999999));
//            }
//            showPopWindow = true;
//        } else {
//            popupWindow.dismiss();
//            Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//            orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//            showPopWindow = false;
//        }
//    }
//
//    private void initPopWindow() {
//        popView = (RelativeLayout) getLayoutInflater().inflate(R.layout.school_help_popwindow_layout, null, false);
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
//        //sortByHeatTextView.setText("我附近的");
//        sortByDistanceTextView = popView.findViewById(R.id.order_by_diatance_tv);
//        sortBySchoolMateTextView = popView.findViewById(R.id.order_by_school_tv);
//        //sortByDistanceTextView.setText("最热表白");
//        sortByTimeTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                orderTextView.setText(sortByTimeTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 2;
//                pageNum = 1;
//                getClassMateHelpPresenter().getClassmateHelpList(userId, 2, pageSize, pageNum, false);
//                refreshLayout.startRefresh();
//            }
//        });
//        sortBySchoolMateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Drawable drawable = getResources().getDrawable(R.mipmap.home_recommend_sort_close_icon);
//                orderTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
//                orderTextView.setText(sortBySchoolMateTextView.getText().toString());
//                popupWindow.dismiss();
//                showPopWindow = false;
//                sort = 1;
//                pageNum = 1;
//                getClassMateHelpPresenter().getClassmateHelpList(userId, 1, pageSize, pageNum, false);
//                refreshLayout.startRefresh();
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
//                getClassMateHelpPresenter().getClassmateHelpList(userId, 4, pageSize, pageNum, false);
//                refreshLayout.startRefresh();
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
//                getClassMateHelpPresenter().getClassmateHelpList(userId, 3, pageSize, pageNum, false);
//                refreshLayout.startRefresh();
//            }
//        });
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//
//    }
//
//    public ClassMateHelpPresenter getClassMateHelpPresenter() {
//        if (classMateHelpPresenter == null) {
//            classMateHelpPresenter = new ClassMateHelpPresenter(this);
//        }
//        return classMateHelpPresenter;
//    }
//
//    private void setTile() {
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        if (type == MY_COMEIN) {
//            toUserId = userId;
//            titleFragment.setTitle(R.string.title_my_schoolmate_help);
//        } else {
//            toUserId = getIntent().getIntExtra(Constants.Fields.TO_USER_ID, 0);
//            titleFragment.setTitle(R.string.title_ta_schoolmate_help);
//        }
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
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
//            if (pageNum == 1) {
//                refreshLayout.finishRefreshing();
//            } else {
//                refreshLayout.finishLoadmore();
//            }
//            return;
//        }
//
//        switch (apiName) {
//            case InterfaceUrl.URL_GETCLASSMATEHELPLIST:
//            case InterfaceUrl.URL_GETPUBLISHHELPLIST:
//                if (object instanceof GetClassmateHelpListResponse) {
//
//                    GetClassmateHelpListResponse getClassmateHelpListResponse = (GetClassmateHelpListResponse) object;
//                    if (pageNum == 1) {
//                        refreshLayout.finishRefreshing();
//                    } else {
//                        refreshLayout.finishLoadmore();
//                    }
//                    if (getClassmateHelpListResponse.getCode() == 200) {
//                        List<HelpInfo> getClassmateHelpListResponseList = getClassmateHelpListResponse.getList();
//                        if (pageNum == 1) {
//                            classMateHelpListAdapter.setData(getClassmateHelpListResponseList);
//                        } else {
//                            classMateHelpListAdapter.addData(getClassmateHelpListResponseList);
//                        }
//
//                        classMateHelpListAdapter.notifyDataSetChanged();
//
//                        if (getClassmateHelpListResponse.getTotal() <= pageNum * pageSize) {
//                            //没有更多数据时  下拉刷新不可用
//                            refreshLayout.setEnableLoadmore(false);
//                            //finishView.setVisibility(View.VISIBLE);
//                        } else {
//                            //有更多数据时  下拉刷新才可用
//                            refreshLayout.setEnableLoadmore(true);
//                            //finishView.setVisibility(View.GONE);
//                        }
//
//                        if (getClassmateHelpListResponse.getTotal() == 0 && pageNum == 1) {
//                            int childCount = ll_schoolmate_help.getChildCount();
//                            if (childCount == 1) {
//                                footerView = LayoutInflater.from(this).inflate(R.layout.item_empty_classmate_help_layout, null);
//                                TextView empty_textView = footerView.findViewById(R.id.empty_textView);
//                                empty_textView.setText(getClassmateHelpListResponse.getBlankHint());
//                                ll_schoolmate_help.addFooterView(footerView, null, false);
//                            }
//                            if (type == MY_COMEIN) {
//                                if (ll_schoolmate_help.getFooterViewsCount() > 0) {
//                                    ll_schoolmate_help.removeFooterView(footerView);
//                                }
//                                empty_tv.setText(getClassmateHelpListResponse.getBlankHint());
//                                emptyView.setVisibility(View.VISIBLE);
//                            }
//                            //finishView.setVisibility(View.GONE);
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                            if (ll_schoolmate_help.getFooterViewsCount() > 0) {
//                                ll_schoolmate_help.removeFooterView(footerView);
//                            }
//                        }
//                    } else if (getClassmateHelpListResponse.getCode() == 201) {
//                        refreshLayout.finishRefreshing();
//                        refreshLayout.finishLoadmore();
//                        classMateHelpListAdapter.getmList().clear();
//                        classMateHelpListAdapter.notifyDataSetChanged();
//
//                        if (ll_schoolmate_help.getChildCount() == 1) {
//                            footerView = LayoutInflater.from(this).inflate(R.layout.item_empty_classmate_help_layout, null);
//                            TextView empty_textView = footerView.findViewById(R.id.empty_textView);
//                            empty_textView.setText(getClassmateHelpListResponse.getBlankHint());
//                            ll_schoolmate_help.addFooterView(footerView, null, false);
//                        }
//                    }
//                }
//                break;
//            case InterfaceUrl.URL_HELPPRAISE:
//                if (object instanceof HelpPraiseResponse) {
//                    HelpPraiseResponse helpPraiseResponse = (HelpPraiseResponse) object;
//                    if (helpPraiseResponse.getCode() == 200) {
//                        HelpInfo helpInfo = classMateHelpListAdapter.getmList().get(praisePosition);
//                        helpInfo.setIsPraise(helpPraiseResponse.getIsPraise() == 1 ? 1 : 0);
//                        helpInfo.setPraiseCount(helpInfo.getPraiseCount() + (helpPraiseResponse.getIsPraise() == 1 ? 1 : -1));
//                        classMateHelpListAdapter.setPraiceRefresh(false);
//                        classMateHelpListAdapter.notifyDataSetChanged();
//                        //classMateHelpListAdapter.updateSingleRow(ll_schoolmate_help, classMateHelpListAdapter.getmList().get(praisePosition).getHelpId());
//
//                    }
//                    Utils.showToastShortTime(helpPraiseResponse.getMsg());
//                }
//                break;
//            case InterfaceUrl.URL_DELETEHELP:
//                if (object instanceof BaseResponse) {
//                    BaseResponse baseResponse = (BaseResponse) object;
//                    if (baseResponse.getCode() == 200) {
//                        classMateHelpListAdapter.getmList().remove(deletePosition);
//                        classMateHelpListAdapter.setPraiceRefresh(true);
//                        classMateHelpListAdapter.notifyDataSetChanged();
//                    }
//                }
//                break;
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
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(ClassMateHelpEvent event) {
//        if (event == null) {
//            return;
//        }
//        int position = event.getPosition();
//        final HelpInfo helpInfo = classMateHelpListAdapter.getmList().get(position);
//        if (helpInfo != null) {
//            if (event.getType() == 2) {// 更新点赞
//                helpInfo.setIsPraise(event.getIsPraise());
//                helpInfo.setPraiseCount(helpInfo.getPraiseCount() + (event.getIsPraise() == 1 ? 1 : -1));
//            } else if (event.getType() == 5) { // 删除帖子
//                classMateHelpListAdapter.getmList().remove(position);
//            } else if (event.getType() == 1) {
//                refreshLayout.startRefresh();
//                initData();
//                ProgressDialogUtils.getInstance().hideProgressDialog();
//            }
//            classMateHelpListAdapter.setPraiceRefresh(true);
//            classMateHelpListAdapter.notifyDataSetChanged();
//        }
//
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(final PublishObjectEvent event) {
//        if (event == null) {
//            return;
//        }
//
//        if (event.getType() == 9) {
//            if (event.getObject() instanceof HomeInfo) {
//                HomeInfo homeInfo = (HomeInfo) event.getObject();
//
//                if (classMateHelpListAdapter != null) {
//                    classMateHelpListAdapter.setPraiceRefresh(true);
//                    classMateHelpListAdapter.addHelpInfo(homeInfo.getHelpInfo());
//                }
//            }
//        }
//
//    }
//
//
//}
