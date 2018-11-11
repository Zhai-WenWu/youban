package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.GoodsPaySuccessEvent;
import cn.bjhdltcdn.p2plive.event.ShopPlaceOrderEvent;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.LocationInfo;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopDetailCategoryRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopDetailTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.PostDetailCommentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.EvaluateRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.GoodsRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeBannerFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.OrderRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.KeyBoardUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.ShopCartPopWindow;
import cn.bjhdltcdn.p2plive.widget.ShopOperationMoreWindow;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * 店铺详情界面
 */
public class ShopDetailActivity extends BaseActivity implements BaseView {
    private GetRecommendListPresenter getRecommendListPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private CommonPresenter commonPresenter;
    private Long userId;
    private long storeId;
    private HomeBannerFragment homeBannerFragment;
    private RefreshLayout refreshLayout;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private ShopDetailTabFragmentAdapter adapter;
    private ImageView shopImg, userImg, desOpenImg;
    private TextView titleTextView, salesVolumeTextView, distanceTextView, locationTextView, shopDesTextView, addrsTextView, schoolNameTextView, nickNameTextView,distributionTextView;
    private LinearLayout evaluateStarLayout;
    private RecyclerView recycleView;
    private ShopDetailCategoryRecyclerViewAdapter recyclerAdapter;
    private TextView addGoodsTextView, shoppingCartTextView, settlementTextView,shopChartStatusTextView;
    private StoreDetail storeDetail;
    public BaseUser baseUser;
    private GoodsRecycleFragment goodsRecycleFragment;
    private EvaluateRecycleFragment evaluateRecycleFragment;
    private OrderRecycleFragment orderRecycleFragment;
    private int type;//0:商品 1：评价 2：订单
    private int goodsPageNum, evaluatePageNum, orderPageNum;
    private int userType = 3;//(1店主,2店员,3普通用户4店员申请中)
    public ShopCartPopWindow shopCartPopWindow;
    private List<ProductDetail> shopChartList;
    private ViewStub viewStubBuyer, viewStubSeller;
    private View bottomView;
    private boolean textOpen;
    private boolean isFirst,showIsAuthTip;
    private int reportType;//15-->举报店铺
    private long pullBackUserId;//被举报人的id,
    private View shopScreenView;
    private TextView recruitsTextView;
    private TextView recruitsSeeTextView;
    private Intent intent;
    private String floorPrice;//起送价格
    private int productNum;
    private BigDecimal totalPrice = new BigDecimal(0);
    private ShopOperationMoreWindow shopOperationMorePopWindow;
//    private TitleFragment titleFragment;
    private ToolBarFragment toolBarFragment;
    private boolean enableLoadMoreForGoods, enableLoadMoreForEvaluate, enableLoadMoreForOrder;
    private boolean showlocalImageAndVideo;
    private EditText editText;
    private TextView sendTextView;
    private View sendView, orderNewView;
    private int isPublish;
    private long postId;
    private int isCanBuy=2;//是否可购买商品(1否2是),
    private String postage;//订单要加进去的邮费
    private Toolbar mToolbar;
    private ImageView moreImageView;

    public GetRecommendListPresenter getGetRecommendListPresenter() {
        if (getRecommendListPresenter == null) {
            getRecommendListPresenter = new GetRecommendListPresenter(this);
        }
        return getRecommendListPresenter;
    }

    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }

    public List<ProductDetail> getShopChartList() {
        if (shopCartPopWindow != null) {
            return shopCartPopWindow.getDateSource();
        } else {
            return null;
        }

    }

    public int getProductNum() {
        return productNum;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public StoreDetail getStoreDetail() {
        return storeDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail_layoutt);
        EventBus.getDefault().register(this);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        storeId = getIntent().getLongExtra(Constants.Fields.STORE_ID, 0);
        isFirst = getIntent().getBooleanExtra("isFirst", false);
        setTitle();
        getView();
        getGetStoreListPresenter().findStoreDetail(userId, storeId);
    }

    /** 根据百分比改变颜色透明度 */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    public void getView() {
        sendView = findViewById(R.id.send_comment_view);
        editText = sendView.findViewById(R.id.reply_edit_input);
        sendTextView = sendView.findViewById(R.id.send_view);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() > 0) {
                    sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
                    sendTextView.setTextColor(getResources().getColor(R.color.color_333333));
                } else {
                    sendTextView.setBackgroundResource(R.drawable.shape_round_10_solid_fff69f);
                    sendTextView.setTextColor(getResources().getColor(R.color.color_999999));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sendTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (evaluateRecycleFragment != null && !StringUtils.isEmpty(editText.getText().toString())) {
                    evaluateRecycleFragment.comment(editText.getText().toString());
                    sendView.setVisibility(View.GONE);
                    editText.setText("");
                    KeyBoardUtils.closeKeybord(editText, App.getInstance());
                }
            }
        });
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getGetStoreListPresenter().findStoreDetail(userId, storeId);
                switch (type) {
                    case 0:
                        goodsPageNum = 1;
                        if (goodsRecycleFragment != null) {
                            goodsRecycleFragment.getStoreProductList(goodsPageNum);
                        }
                        break;
                    case 1:
                        evaluatePageNum = 1;
                        if (evaluateRecycleFragment != null) {
                            evaluateRecycleFragment.getOrderEvaluateList(evaluatePageNum);
                        }
                        break;
                    case 2:
                        orderPageNum = 1;
                        if (orderRecycleFragment != null) {
                            orderRecycleFragment.getOrderList(orderPageNum);
                        }
                        break;
                }
                if (orderNewView != null && orderNewView.getVisibility() == View.VISIBLE) {
                    orderNewView.setVisibility(View.GONE);
//                    SafeSharePreferenceUtils.clearDataByKey(ShopDetailActivity.this,userId+"/"+storeId);
                    GreenDaoUtils.getInstance().deletePlaceOrderMessageById(userId,storeId);
                }
            }
        });
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                switch (type) {
                    case 0:
                        if (goodsRecycleFragment != null) {
                            goodsRecycleFragment.getStoreProductList(goodsRecycleFragment.getPageNumber());
                        }
                        break;
                    case 1:
                        if (evaluateRecycleFragment != null) {
                            evaluateRecycleFragment.getOrderEvaluateList(evaluateRecycleFragment.getPageNumber());
                        }
                        break;
                    case 2:
                        if (orderRecycleFragment != null) {
                            orderRecycleFragment.getOrderList(orderRecycleFragment.getPageNumber());
                        }
                        break;
                }

            }
        });
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        homeBannerFragment = (HomeBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner_frame);
        mViewPager = (CustomViewPager) findViewById(R.id.shop_detail_view_page);
        mViewPager.setIsCanScroll(true);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        adapter = new ShopDetailTabFragmentAdapter(getSupportFragmentManager());

        //店铺信息
        shopImg = (ImageView) findViewById(R.id.shop_img);
        titleTextView = (TextView) findViewById(R.id.shop_name_text);
        salesVolumeTextView = (TextView) findViewById(R.id.shop_sales_volume_text_view);
        distanceTextView = (TextView) findViewById(R.id.shop_distance_text_view);
        locationTextView = (TextView) findViewById(R.id.shop_location_text_view);
        addrsTextView = (TextView) findViewById(R.id.shop_addr_text_view);
        shopDesTextView = (TextView) findViewById(R.id.shop_des_text_view);
        desOpenImg = (ImageView) findViewById(R.id.open_img);
        schoolNameTextView = (TextView) findViewById(R.id.user_school_text_view);
        evaluateStarLayout = (LinearLayout) findViewById(R.id.evaluate_star_layout);
        recycleView = (RecyclerView) findViewById(R.id.recycler_category);
        shopScreenView = (View) findViewById(R.id.shop_screen_view);
        recruitsTextView = shopScreenView.findViewById(R.id.recruits_text_view);
        recruitsSeeTextView = shopScreenView.findViewById(R.id.recruits_see_text_view);
        distributionTextView= shopScreenView.findViewById(R.id.distribution_text_view);

        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
//        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(ShopDetailActivity.this);
//        flowLayoutManager.setMargin(Utils.dp2px(5));
        recycleView.setLayoutManager(linearLayoutManager);
        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(5), false));
        recyclerAdapter = new ShopDetailCategoryRecyclerViewAdapter(ShopDetailActivity.this);
        recycleView.setAdapter(recyclerAdapter);
        userImg = (ImageView) findViewById(R.id.img_icon);
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到用户详情
                if (baseUser!=null&&baseUser.getUserId() != userId) {
                    //跳到用户详情
                    startActivity(new Intent(ShopDetailActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                }
            }
        });
        nickNameTextView = (TextView) findViewById(R.id.name_text_view);
//        setListenerToRootView();

    }

    public int getSendViewVisible() {
        return sendView.getVisibility();
    }

    public void setSendViewVisible(int visible) {
        sendView.setVisibility(visible);
        editText.setText("");
        KeyBoardUtils.closeKeybord(editText, App.getInstance());
    }

    private void setListenerToRootView() {
        final View rootView = getWindow().getDecorView().findViewById(R.id.root_view);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                LogUtils.d(TAG, "[onGlobalLayout] .. in ..");
                boolean mKeyboardUp = isKeyboardShown(rootView);
                if (mKeyboardUp) {
//                    LogUtils.d(TAG, "键盘弹出..");
                } else {
//                    LogUtils.d(TAG, "键盘收起..");
                    if (sendView.getVisibility() == View.VISIBLE) {
                        sendView.setVisibility(View.GONE);
                        editText.setText("");
                    }
                }
            }
        });
    }

    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    public void comment(String toUserNickName) {
        if (sendView != null) {
            sendView.setVisibility(View.VISIBLE);
            if (!StringUtils.isEmpty(toUserNickName)) {
                editText.setHint("回复：" + toUserNickName);
            } else {
                editText.setHint("说点什么吧....");
            }

        }

        if (editText != null) {
            KeyBoardUtils.openKeybord(editText, App.getInstance());
        }
    }

    private void setTitle() {
//        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
//        titleFragment.setTitle(R.string.title_shop_detail);
//        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
//            @Override
//            public void onBackClick() {
//                finish();
//            }
//        });
//        titleFragment.setRightTwoViewTitle(R.mipmap.share_icon, new TitleFragment.RightTwoViewClick() {
//            @Override
//            public void onClick() {
//                if (shopOperationMorePopWindow!=null&&shopOperationMorePopWindow.isShowing()) {
//                    shopOperationMorePopWindow.dismiss();
//                }
//                if (getSendViewVisible() == View.VISIBLE) {
//                    setSendViewVisible(View.GONE);
//                }
//                //分享弹窗
//                String imgUrl = "";
//                List<StoreImage> storeImageList=storeDetail.getStoreInfo().getStoreImageList();
//                if(storeImageList!=null&&storeImageList.size()>0){
//                    StoreImage storeImage=storeImageList.get(0);
//                    if (storeImage.getImageType() == 2) {
//                        imgUrl = storeImage.getVideoImageUrl();
//                    } else {
//                        imgUrl = storeImage.getThumbnailUrl();
//                    }
//                }
//                ShareUtil.getInstance().showShare(ShopDetailActivity.this, ShareUtil.STOREDETAIL, storeId, storeDetail, "", "这家小店不错哦，快来转转！", storeDetail.getStoreInfo().getTitle(), imgUrl, false);
//
//            }
//        });
//        titleFragment.setRightViewTitle(R.mipmap.store_detail_more_icon, new TitleFragment.RightViewClick() {
//            @Override
//            public void onClick() {
//                if (getSendViewVisible() == View.VISIBLE) {
//                    setSendViewVisible(View.GONE);
//                }
////                PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
////                postDetailCommentDialog.setItemStr(str);
////                postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
////                    @Override
////                    public void itemClick(int position, String content) {
////                        if (content.equals("编辑")) {//
////                        } else if (content.equals("举报")) {
////                        }
////                    }
////                });
////                postDetailCommentDialog.show(getSupportFragmentManager());
//                String[] str = new String[0];
//                if (userType == 1) {
//                    //卖家
//                    str = new String[]{"编辑"};
//                } else{
//                    //买家
//                    str = new String[]{"举报"};
//                }
//                initOperationMorePopWindow(str);
//            }
//        });

//        toolBarFragment= (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.toolbar_fragment);
//        toolBarFragment.setTitleView("店铺详情");
//        toolBarFragment.setBackGround(R.color.color_00000000);
//        toolBarFragment.setLeftView("",R.drawable);

        //        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        AppBarLayout mAppBarLayout= (AppBarLayout) findViewById(R.id.app_bar);
        mToolbar= (Toolbar) findViewById(R.id.AppFragment_Toolbar);
        final ImageView backImageView=(ImageView) findViewById(R.id.back_img_view);
        final ImageView shareImageView=(ImageView) findViewById(R.id.share_img_view);
        moreImageView=(ImageView) findViewById(R.id.more_img_view);
        final View lineView=(View) findViewById(R.id.title_line);
        final boolean[] topShow = {false};
        final boolean[] bottomShow = { true };
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mToolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.color_ffffff),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
//                toolBarFragment.changeAlpha(getResources().getColor(R.color.colorPrimary),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange());
                Log.e("verticalOffset",verticalOffset+"---"+appBarLayout.getTotalScrollRange()+"---"+Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange());

                if(Math.abs(verticalOffset)<appBarLayout.getTotalScrollRange()/2){
                    if(topShow[0]){
                        backImageView.setImageResource(R.mipmap.shop_detail_back_icon);
                        shareImageView.setImageResource(R.mipmap.shop_detail_share_icon);
                        moreImageView.setImageResource(R.mipmap.shop_detail_more_icon);
                        topShow[0] =false;
                        bottomShow[0] =true;
                    }
                    backImageView.setAlpha(1-(Math.abs(verticalOffset*1.0f)/(appBarLayout.getTotalScrollRange()/2)));//(透明度从1到0)
                    shareImageView.setAlpha(1-(Math.abs(verticalOffset*1.0f)/(appBarLayout.getTotalScrollRange()/2)));
                    moreImageView.setAlpha(1-(Math.abs(verticalOffset*1.0f)/(appBarLayout.getTotalScrollRange()/2)));
                }else{
                    if(bottomShow[0]){
                        backImageView.setImageResource(R.mipmap.store_detail_back_icon);
                        shareImageView.setImageResource(R.mipmap.store_detail_share_icon);
                        moreImageView.setImageResource(R.mipmap.store_detail_more_icon);
                        bottomShow[0] =false;
                        topShow[0] =true;
                    }
                    backImageView.setAlpha(((Math.abs(verticalOffset*1.0f)-(appBarLayout.getTotalScrollRange()/2))/(appBarLayout.getTotalScrollRange()/2)));//(透明度从0到1)
                    shareImageView.setAlpha(((Math.abs(verticalOffset*1.0f)-(appBarLayout.getTotalScrollRange()/2))/(appBarLayout.getTotalScrollRange()/2)));
                    moreImageView.setAlpha(((Math.abs(verticalOffset*1.0f)-(appBarLayout.getTotalScrollRange()/2))/(appBarLayout.getTotalScrollRange()/2)));
                }
                if(Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()==1){
                    lineView.setVisibility(View.VISIBLE);
                }else{
                    lineView.setVisibility(View.GONE);
                }
            }
        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shopOperationMorePopWindow!=null&&shopOperationMorePopWindow.isShowing()) {
                    shopOperationMorePopWindow.dismiss();
                }
                if (getSendViewVisible() == View.VISIBLE) {
                    setSendViewVisible(View.GONE);
                }
                //分享弹窗
                String imgUrl = "";
                List<StoreImage> storeImageList=storeDetail.getStoreInfo().getStoreImageList();
                if(storeImageList!=null&&storeImageList.size()>0){
                    StoreImage storeImage=storeImageList.get(0);
                    if (storeImage.getImageType() == 2) {
                        imgUrl = storeImage.getVideoImageUrl();
                    } else {
                        imgUrl = storeImage.getThumbnailUrl();
                    }
                }
                ShareUtil.getInstance().showShare(ShopDetailActivity.this, ShareUtil.STOREDETAIL, storeId, storeDetail, "", "这家小店不错哦，快来转转！", storeDetail.getStoreInfo().getTitle(), imgUrl, false);

            }
        });
        moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSendViewVisible() == View.VISIBLE) {
                    setSendViewVisible(View.GONE);
                }
                String[] str = new String[0];
                if (userType == 1) {
                    //卖家
                    str = new String[]{"编辑"};
                } else{
                    //买家
                    str = new String[]{"举报"};
                }
                initOperationMorePopWindow(str);
            }
        });
    }

    public void initOperationMorePopWindow(String[] str) {
        if (shopOperationMorePopWindow == null) {
            shopOperationMorePopWindow = new ShopOperationMoreWindow(this);
        }
        if (shopOperationMorePopWindow.isShowing()) {
            shopOperationMorePopWindow.dismiss();
        } else {
            shopOperationMorePopWindow.setOperationStr(str);
            shopOperationMorePopWindow.setItemClick(new ShopOperationMoreWindow.ItemClick() {
                @Override
                public void itemClick(String operationStr) {
                    if (operationStr.equals("编辑")) {
                        //编辑
                        Intent intent = new Intent(ShopDetailActivity.this, CreateShopActivity.class);
                        if (storeDetail != null) {
                            intent.putExtra("storeDetail", storeDetail);
                        }
                        intent.putExtra("comeInType", 1);
                        startActivity(intent);
                    } else if (operationStr.equals("举报")) {
                        //举报
                        reportType = 15;
                        getCommonPresenter().getReportType(2);
                    }
                }
            });
            shopOperationMorePopWindow.showPopupWindow(moreImageView);
        }
    }


    public void initStoreDetail(final StoreDetail storeDetail) {
        this.storeDetail = storeDetail;
        StoreInfo storeInfo = storeDetail.getStoreInfo();
        floorPrice = storeInfo.getFloorPrice();
        if(floorPrice==null||StringUtils.isEmpty(floorPrice)){
            floorPrice=0+"";
        }
        if (!showlocalImageAndVideo) {
            Utils.CornerImageViewDisplayByUrl(storeInfo.getStoreIcon(), shopImg,9);
        }
        titleTextView.setText(storeInfo.getTitle());
        salesVolumeTextView.setText("销售量" + storeDetail.getSalesVolume());
        String distance = storeInfo.getDistance();
        if (!StringUtils.isEmpty(distance)) {
            distanceTextView.setText(distance);
            distanceTextView.setPadding(Utils.dp2px(10), 0, 0, 0);
        } else {
            distanceTextView.setText(distance);
            distanceTextView.setPadding(0, 0, 0, 0);
        }
        if(storeInfo.getDistribution()==1){
            distributionTextView.setText("全国配送");
        }else{
            distributionTextView.setText("同校配送");
        }

        //描述
        String shopDes = storeInfo.getStoreDesc();
        shopDesTextView.setText(shopDes);
        shopDesTextView.post(new Runnable() {
            @Override
            public void run() {
                //获取textView的行数
                int txtPart = shopDesTextView.getLineCount();
                if (txtPart > 2) {
                    //大于三行做的操作
                    shopDesTextView.setMaxLines(2);
                    desOpenImg.setVisibility(View.VISIBLE);
                    //显示查看更多
                }
            }
        });
        desOpenImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textOpen) {
                    shopDesTextView.setMaxLines(100);
                    desOpenImg.setImageResource(R.mipmap.text_close_icon_666666);
                    textOpen = true;
                } else {
                    shopDesTextView.setMaxLines(2);
                    desOpenImg.setImageResource(R.mipmap.text_open_icon_666666);
                    textOpen = false;
                }
            }
        });
        LocationInfo locationInfo = storeInfo.getLocationInfo();
        if (locationInfo != null) {
            if (!StringUtils.isEmpty(distance)) {
                locationTextView.setText(storeInfo.getLocationInfo().getDistrict());
            } else {
                locationTextView.setText(storeInfo.getLocationInfo().getCity());
            }
            addrsTextView.setText("地址：" + storeInfo.getLocationInfo().getAddr());
        } else {
            locationTextView.setText("");
            addrsTextView.setText("");
        }
        schoolNameTextView.setText(storeInfo.getSchoolName());
        setEvaluateShowLayout(storeDetail.getEvaluateScore(), evaluateStarLayout);
        FirstLabelInfo firstLabelInfo=storeInfo.getFirstLabelInfo();
        if(firstLabelInfo!=null){
            List<SecondLabelInfo> secondLabelInfoList=firstLabelInfo.getList();
            if(secondLabelInfoList!=null&&secondLabelInfoList.size()>0)
            {
                recyclerAdapter.setList(secondLabelInfoList);
                recyclerAdapter.notifyDataSetChanged();
            }
        }

        final int isClert = storeDetail.getIsClert();//当前是否是店员(1店主,2店员,3普通用户,4店员申请中),
        userType = isClert;
        baseUser = storeInfo.getBaseUser();
        if (baseUser != null) {
            pullBackUserId = storeInfo.getStoreId();
            Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon())
                    .apply(new RequestOptions().centerCrop().error(R.mipmap.error_user_icon)).into(userImg);
            nickNameTextView.setText(baseUser.getNickName());
            if (userType == 1) {
                initSellerBottomLayout();
            } else {
                initBuyerBottomLayout();
            }
        }
        initTabLayout(userType, isCanBuy, storeDetail.getIsReportMax(), storeDetail.getIsAuth());
        if (orderNewView != null) {
            //取出本地存储的显示订单小红点的userId
//            if (SafeSharePreferenceUtils.getLong(userId+"/"+storeId, 0) == 1) {
//                orderNewView.setVisibility(View.VISIBLE);
//            }
            if(GreenDaoUtils.getInstance().getPlaceOrderMessage(userId,storeId)!=null){
                orderNewView.setVisibility(View.VISIBLE);
            }
        }
        int isRecruitClert = storeInfo.getIsRecruitClert();//是否招聘店员(0不招聘,1招聘),
        isPublish = storeInfo.getIsPublish();
        postId = storeInfo.getPostId();
        if (isRecruitClert == 0) {
            recruitsTextView.setVisibility(View.GONE);
        } else {
            recruitsTextView.setVisibility(View.VISIBLE);
            if (isClert == 1) {
                recruitsTextView.setText("招聘店员");
                recruitsTextView.setEnabled(true);
                recruitsTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
            }else if (isClert == 2) {
                recruitsTextView.setText("已为店员");
                recruitsTextView.setEnabled(false);
                recruitsTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
            } else if (isClert == 3) {
                recruitsTextView.setText("申请店员");
                recruitsTextView.setEnabled(true);
                recruitsTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
            } else if (isClert == 4) {
                //店员申请中
                recruitsTextView.setText("审核中");
                recruitsTextView.setEnabled(false);
                recruitsTextView.setBackground(getResources().getDrawable(R.drawable.shape_round_10_solid_e6e6e6));
            }
        }
        int clertNum = storeDetail.getClertNum();//店员数量(不包含店主),
        if (clertNum > 0) {
            if (isClert == 1) {
                recruitsSeeTextView.setVisibility(View.VISIBLE);
            }else{
                recruitsSeeTextView.setVisibility(View.GONE);
            }
        } else {
            recruitsSeeTextView.setVisibility(View.GONE);
        }
        recruitsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClert == 1) {
                    //开店申请状态(1等待审核  2 审核通过 3审核拒绝)
                    if(storeDetail.getIsAuth()!=2){
                        Utils.showToastShortTime("您的资料还未通过审核，暂时不能招聘店员");
                        return;
                    }
//                    if (isPublish == 1) {
//                        //跳到帖子详情
//                        Intent intent = new Intent(ShopDetailActivity.this, PostDetailActivity.class);
//                        PostInfo postInfo = new PostInfo();
//                        postInfo.setPostId(postId);
//                        if (postInfo.getContentLimit() != 2) {
//                            intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
//                            intent.putExtra(Constants.Fields.COME_IN_TYPE, 0);
//                            startActivity(intent);
//                        }
//                    } else {
                        //跳到一键发布界面
                        Intent intent = new Intent(ShopDetailActivity.this, PublishActivity.class);
                        intent.putExtra(Constants.Fields.TYPE, 1);
                        intent.putExtra(Constants.Fields.STORE_ID, storeId);
                        startActivity(intent);
//                    }

                } else {
                    //开店申请状态(1等待审核  2 审核通过 3审核拒绝)
                    if(storeDetail.getIsAuth()!=2){
                        Utils.showToastShortTime("店铺还未通过审核，暂时不能申请店员");
                        return;
                    }
                    if (isPublish == 1) {
//                        if(storeDetail.getIsSchoolmate()==1){
//                            //是否是校友(1否2是),
//                            Utils.showToastShortTime("非本校用户不得应聘店员");
//                        }else {
                            if (isClert == 2) {
                                Utils.showToastShortTime("您已成为该店店员");
                            } else if (isClert == 3) {
                                //跳到店员申请界面
                                intent = new Intent(ShopDetailActivity.this, ApplyClerkActivity.class);
                                intent.putExtra(Constants.Fields.POST_ID, postId);
                                intent.putExtra(Constants.Fields.STORE_ID, storeId);
                                startActivity(intent);
                            } else if (isClert == 4) {
                                //店员申请中
                                Utils.showToastShortTime("您的店员申请正在审核中，请耐心等待");
                            }
//                        }
                    } else {
                        Utils.showToastShortTime("店家还没有发布招聘信息哦！");
                    }
                }
            }
        });
        recruitsSeeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ShopDetailActivity.this, ClerkListActiviity.class);
                intent.putExtra(Constants.Fields.TYPE, userType);
                intent.putExtra("isClert", isClert);
                intent.putExtra(Constants.Fields.STORE_ID, storeId);
                startActivity(intent);
            }
        });

        if (isFirst) {
            ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
            dialog.setText("", "开店成功！您的资料已提交审核，通过后，您的小店将自动开启，是否现在添加商品？", "稍后再说", "马上就去");
            dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                @Override
                public void onLeftClick() {
                    //取消
                }

                @Override
                public void onRightClick() {
                    Intent intent = new Intent(ShopDetailActivity.this, AddGoodsActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show(ShopDetailActivity.this.getSupportFragmentManager());
            isFirst=false;
        }else{
            if(!showIsAuthTip&&storeDetail.getIsAuth()==1&&userType==1){
                Utils.showToastLongTime("您的资料正在审核中，暂时不能出售商品，审核通过后，您的小店将自动开启");
                showIsAuthTip=true;
            }
        }
    }

    public void initTabLayout(final int userType, int isSchoolmate, int isReportMax, int isAuth) {
        if (mTabLayout.getTabCount() <= 0) {
            if(userType==0){
                return;
            }
            if (userType == 1 || userType == 2) {
                goodsRecycleFragment = new GoodsRecycleFragment();
                adapter.addFragment(goodsRecycleFragment, "商品");

                evaluateRecycleFragment = new EvaluateRecycleFragment();
                evaluateRecycleFragment.setStoreId(storeId,storeDetail.getStoreInfo().getUserId());
                adapter.addFragment(evaluateRecycleFragment, "评价");

                orderRecycleFragment = new OrderRecycleFragment();
                orderRecycleFragment.setStoreId(storeId);
                adapter.addFragment(orderRecycleFragment, "订单");
                mViewPager.setOffscreenPageLimit(3);
            } else if (userType == 3 || userType == 4) {
                goodsRecycleFragment = new GoodsRecycleFragment();
                adapter.addFragment(goodsRecycleFragment, "商品");

                evaluateRecycleFragment = new EvaluateRecycleFragment();
                evaluateRecycleFragment.setStoreId(storeId,storeDetail.getStoreInfo().getUserId());
                adapter.addFragment(evaluateRecycleFragment, "评价");
                mViewPager.setOffscreenPageLimit(2);
            }
            goodsRecycleFragment.setStoreId(storeId);
            goodsRecycleFragment.setUserType(userType);
            goodsRecycleFragment.setIsSchoolmate(isSchoolmate);
            goodsRecycleFragment.setIsReportMax(isReportMax);
            goodsRecycleFragment.setIsAuth(isAuth);

            mViewPager.setAdapter(adapter);
            mTabLayout.setupWithViewPager(mViewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = mTabLayout.getTabAt(i);
                View tabView = adapter.getTabView(i);
                tab.setCustomView(tabView);
                if (i == 2) {
                    orderNewView = tabView.findViewById(R.id.tab_home_pop_view);
                }
            }
            mViewPager.setCurrentItem(0);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
            mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                    textView.setTextColor(getResources().getColor(R.color.color_333333));

                    View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                    tabIndicatorView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));

                    mViewPager.setCurrentItem(tab.getPosition());
                    type = tab.getPosition();

//                    if (userType == 3 || userType == 4) {
//                        if (type == 1) {
//                            if (viewStubBuyer != null) {
//                                viewStubBuyer.setVisibility(View.GONE);
//                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) refreshLayout.getLayout().getLayoutParams();
//                                layoutParams.setMargins(0, 0, 0, 0);
//                            }
//                        } else {
//                            if (viewStubBuyer != null) {
//                                viewStubBuyer.setVisibility(View.VISIBLE);
//                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) refreshLayout.getLayout().getLayoutParams();
//                                layoutParams.setMargins(0, 0, 0, Utils.dp2px(49));
//                            }
//                        }
//                    }

                    switch (type) {
                        case 0:
                            refreshLayout.setEnableLoadMore(enableLoadMoreForGoods);
                            break;
                        case 1:
                            refreshLayout.setEnableLoadMore(enableLoadMoreForEvaluate);
                            break;
                        case 2:
                            refreshLayout.setEnableLoadMore(enableLoadMoreForOrder);
                            break;
                    }
                    if (getSendViewVisible() == View.VISIBLE) {
                        setSendViewVisible(View.GONE);
                    }
                    if (orderNewView != null && orderNewView.getVisibility() == View.VISIBLE) {
                        orderNewView.setVisibility(View.GONE);
//                        SafeSharePreferenceUtils.clearDataByKey(ShopDetailActivity.this,userId+"/"+storeId);
                        GreenDaoUtils.getInstance().deletePlaceOrderMessageById(userId,storeId);
                    }

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
                    textView.setTextColor(getResources().getColor(R.color.color_999999));

                    View tabIndicatorView = tab.getCustomView().findViewById(R.id.tab_indicator_view);
                    tabIndicatorView.setBackgroundColor(getResources().getColor(R.color.transparent));

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
//                KLog.d("onTabReselected()");
                }
            });
        }
    }


    public void initSellerBottomLayout() {
        if (viewStubSeller == null) {
            viewStubSeller = findViewById(R.id.seller_operation_layout);
            viewStubSeller.inflate();
            addGoodsTextView = findViewById(R.id.add_goods_text_view);
            addGoodsTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShopDetailActivity.this, AddGoodsActivity.class);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                }
            });
            TextView salesRecordTextView = findViewById(R.id.sales_record_text_view);
            salesRecordTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ShopDetailActivity.this, SalesRecordListActivity.class);
                    intent.putExtra("storeId", storeId);
                    startActivity(intent);
                }
            });
        }
    }

    public void initBuyerBottomLayout() {
        if (viewStubBuyer == null) {
            viewStubBuyer = findViewById(R.id.buyer_operation_layout);
            viewStubBuyer.inflate();
            final RelativeLayout shopCartLayout = findViewById(R.id.shopping_cart_layout);
            shoppingCartTextView = findViewById(R.id.shopping_cart_text_view);
            shopChartStatusTextView= findViewById(R.id.shopping_cart_status_text_view);
            //判断购物车状态显示的文案
            List<LabelInfo> distributeList = storeDetail.getStoreInfo().getDistributeList();
            if(distributeList!=null&&distributeList.size()>0){
                if(distributeList.size()==1)
                {
                    LabelInfo labelInfo=distributeList.get(0);
                    if(labelInfo.getLabelId()==37){
                        //37 支持自提
                        shopChartStatusTextView.setText("(不支持配送)");
                        Utils.showToastLongTime("已超出配送范围，只支持自提");
                    }else if(labelInfo.getLabelId()==36){
                        //36 支持配送
                        showShopChartText(false);
                    }
                }else{
                    //支持配送 支持自提
                    showShopChartText(true);
                }
            }
            shopCartLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //弹出购物车列表界面
                    if (!shoppingCartTextView.getText().equals("未选购商品")) {
                        if (shopCartPopWindow.isShowing()) {
                            shopCartPopWindow.dismiss();
                        } else {
                            shopCartPopWindow.showPopupWindow(shopCartLayout);
                        }
                    }
                }
            });
            TextView shopTextView = findViewById(R.id.shop_text_view);
            TextView messageTextView = findViewById(R.id.message_text_view);
            messageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("私信");
                    if (baseUser != null) {
                        RongIMutils.startToConversation(ShopDetailActivity.this, baseUser.getUserId() + "", baseUser.getNickName());
                    }
                }
            });
            TextView videoTextView = findViewById(R.id.call_text_view);
            videoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("视频");
                    if (baseUser != null) {
                        Intent intent = new Intent(ShopDetailActivity.this, VideoChatActivity.class);
                        intent.putExtra(Constants.Fields.BASEUSER, baseUser);
                        intent.putExtra(Constants.Fields.TYPE, 5);
                        intent.putExtra(Constants.Fields.ROOMNUMBER, Utils.getRoomNumber());
                        startActivity(intent);
                    }
                }
            });
            settlementTextView = findViewById(R.id.settlement_text_view);
            settlementTextView.setText("¥" + floorPrice + "起送");
            settlementTextView.setEnabled(false);

            settlementTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("结算");
                    Intent intent = new Intent(ShopDetailActivity.this, WXPayEntryActivity.class);
                    intent.putExtra("storeDetail", storeDetail);
                    intent.putParcelableArrayListExtra("productDetailList", (ArrayList<? extends Parcelable>) shopCartPopWindow.getDateSource());
                    intent.putExtra("productNum", productNum);
                    intent.putExtra("totalPrice", totalPrice);
                    intent.putExtra("postage", postage);
                    intent.putExtra(Constants.Fields.STORE_ID, storeId);
                    intent.putExtra(Constants.Fields.TYPE, 2);
                    startActivity(intent);

                }
            });
        }

        shopCartPopWindow = new ShopCartPopWindow(ShopDetailActivity.this);
    }

    /**
     * 展示购物车提示文案的显示
     * @param selfLifting 是否支持自提
     */
    public void showShopChartText(boolean selfLifting){
        //配送范围(0本校1全国),
        StoreInfo storeInfo=storeDetail.getStoreInfo();
        if(storeInfo.getDistribution()==0){
            //是否是校友(1否2是),
            if(storeDetail.getIsSchoolmate()==1) {
                if(selfLifting){
                    shopChartStatusTextView.setText("(不支持配送)");
                    Utils.showToastLongTime("已超出配送范围，只支持自提");
                }else
                {
                    shopChartStatusTextView.setText("");
                    shopChartStatusTextView.setVisibility(View.GONE);
                    Utils.showToastLongTime("非本校人员不得进行购买");
                    isCanBuy=1;
                }

            }else if(storeDetail.getIsSchoolmate()==2){
                //本校包邮(0是,1否),
                if(storeInfo.getIsSchoolPostage()==0){
                    shopChartStatusTextView.setText("(本校包邮)");
                }else{
                    shopChartStatusTextView.setText("(配送费:"+storeInfo.getPostage()+")");
                    postage=storeInfo.getPostage();
                }
            }
        }else if(storeInfo.getDistribution()==1){
            //是否是校友(1否2是),
            if(storeDetail.getIsSchoolmate()==1) {
                shopChartStatusTextView.setText("(配送费:"+storeInfo.getPostage()+")");
                postage=storeInfo.getPostage();
            }else if(storeDetail.getIsSchoolmate()==2){
                //本校包邮(0是,1否),
                if(storeInfo.getIsSchoolPostage()==0){
                    shopChartStatusTextView.setText("(本校包邮)");
                }else{
                    shopChartStatusTextView.setText("(配送费:"+storeInfo.getPostage()+")");
                    postage=storeInfo.getPostage();
                }
            }
        }

    }


    public boolean getShoppingCartData(){
        if(shoppingCartTextView!=null){
            if (!shoppingCartTextView.getText().equals("未选购商品")) {
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }



    /**
     * 设置星级
     */
    private void setEvaluateShowLayout(double averageScore, LinearLayout evaluateStarLayout) {
        //显示评价星数
        if (averageScore >= 0) {

            evaluateStarLayout.removeAllViews();

            if (averageScore == 0) {
                //0星
                for (int i = 0; i < (5 - Math.round(averageScore)); i++) {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
                return;
            }

            if (averageScore > 0 && averageScore < 1) {
                //大于0 小于1 星
                if (Math.round(averageScore) > averageScore) {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.star_half_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                } else {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            } else {
                //大于等于1星
                // 新增完整的星
                for (int i = 0; i < (int) averageScore; i++) {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.rating_select_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }

                // 四舍五入，则新增半个星
                if (Math.round(averageScore) > averageScore) {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.star_half_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }

            // 如果还没有满5个星，则补加未选中的星
            if (evaluateStarLayout.getChildCount() < 5) {
                for (int i = 0; i < (5 - Math.round(averageScore)); i++) {
                    ImageView starImage = new ImageView(this);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }

        } else {
//            evaluateStarLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            refreshLayout.finishRefresh(false);//传入false表示刷新失败
            showlocalImageAndVideo = false;
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        storeId = storeDetail.getStoreInfo().getStoreId();
                        initStoreDetail(storeDetail);
                        if (!showlocalImageAndVideo) {
                            //获取网络视频和图片
                            List<StoreImage> storeImageList = storeDetail.getStoreInfo().getStoreImageList();
//                            if (storeImageList != null && storeImageList.size() > 0) {
                                homeBannerFragment.setStoreDetailHeadData(storeImageList);
//                            }
                        } else {
                            //获取本地视频和图片
                            showlocalImageAndVideo = false;
                        }
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                    finish();
                }
            }
        } else if (InterfaceUrl.URL_GETREPORTTYPE.equals(apiName)) {
            if (object instanceof GetReportTypeResponse) {
                GetReportTypeResponse response = (GetReportTypeResponse) object;
                final List<ReportType> reportTypeList = response.getReprotTypeList();
                if (response.getCode() == 200 && reportTypeList != null && reportTypeList.size() > 0) {
                    PostDetailCommentDialog postDetailCommentDialog = new PostDetailCommentDialog();
                    String[] str = new String[reportTypeList.size()];
                    for (int i = 0; i < reportTypeList.size(); i++) {
                        str[i] = reportTypeList.get(i).getReportName();
                    }
                    postDetailCommentDialog.setItemStr(str);
                    postDetailCommentDialog.setItemClick(new PostDetailCommentDialog.ItemClick() {
                        @Override
                        public void itemClick(int position, String content) {
                            if (reportType == 15) {
                                //举报店铺
                                commonPresenter.reportOperation(storeId, reportType, userId, pullBackUserId, reportTypeList.get(position).getReportTypeId());
                            }
                        }
                    });
                    postDetailCommentDialog.show(getSupportFragmentManager());
                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());
            }
        }
    }

    public void finishRefresh(boolean finishRefresh) {
        if (finishRefresh) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishRefresh(false);
        }
    }

    public void finishLoadMore(boolean finishLoadMore) {
        if (finishLoadMore) {
            refreshLayout.finishLoadMore();
        } else {
            refreshLayout.finishLoadMore(false);

        }
    }

    public void setEnableLoadMore(boolean enable, int type) {
        switch (type) {
            case 0:
                enableLoadMoreForGoods = enable;
                break;
            case 1:
                enableLoadMoreForEvaluate = enable;
                break;
            case 2:
                enableLoadMoreForOrder = enable;
                break;
        }
        refreshLayout.setEnableLoadMore(enable);
    }


    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(ShopDetailActivity.this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();

    }

    //接收编辑店铺消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateShopDetailEvent event) {
        if (event == null) {
            return;
        }
        //访问服务器店铺详情的数据 不包括店铺头像以及宣传图片和视频
        getGetStoreListPresenter().findStoreDetail(userId, storeId);
        if(event.getType()!=1){
            showlocalImageAndVideo = true;
            //本地跟新店铺头像以及宣传图片和视频
            List<StoreImage> storeImageList = event.getStoreDetail().getStoreInfo().getStoreImageList();
            if (storeImageList != null && storeImageList.size() > 0) {
                homeBannerFragment.setStoreDetailHeadData(storeImageList);
            }
            Utils.CornerImageViewDisplayByUrl(event.getStoreDetail().getStoreInfo().getStoreIcon(), shopImg,9);
        }
    }

    //接收买家下单成功消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ShopPlaceOrderEvent event) {
        if (event == null) {
            return;
        }
        orderNewView.setVisibility(View.VISIBLE);
    }

    //接收充值成功消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(GoodsPaySuccessEvent event) {
        if (event == null) {
            return;
        }
        shoppingCartTextView.setText("未选购商品");
        settlementTextView.setText("¥" + floorPrice + "起送");
        settlementTextView.setEnabled(false);
        productNum = 0;
        totalPrice = new BigDecimal(0);
        shopCartPopWindow.setDataSource(new ArrayList<ProductDetail>());
        shopCartPopWindow.setTotal(0, totalPrice);
        if(shopCartPopWindow!=null&&shopCartPopWindow.isShowing()){
            shopCartPopWindow.dismiss();
        }
        goodsRecycleFragment.emptyList();
        //更新商品列表库存
        goodsRecycleFragment.getStoreProductList(1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateShopChartListEvent event) {
        if (event == null) {
            return;
        }
        if (event.getType() == 1) {
            //更新购物车列表
            shopCartPopWindow.addProductDetail(event.getProductDetail(), event.getTotal(), event.getTotalMoney());
            BigDecimal floorPriceBig = new BigDecimal(floorPrice);
            BigDecimal totalMoneyFloat = event.getTotalMoney();
            if (totalMoneyFloat.compareTo(floorPriceBig) == 1 || totalMoneyFloat.compareTo(floorPriceBig) == 0) {//小于 时，返回 -1   等于 时，返回 0   大于 时，返回 1
                settlementTextView.setText("结算");
                settlementTextView.setEnabled(true);
            } else {
                settlementTextView.setText("还差¥" + floorPriceBig.subtract(totalMoneyFloat) + "起送");
                settlementTextView.setEnabled(false);
            }
            if (event.getTotal() <= 0) {
                shoppingCartTextView.setText("未选购商品");
                settlementTextView.setText("¥" + floorPrice + "起送");
                settlementTextView.setEnabled(false);
            } else {
                String cartStr = "¥" + event.getTotalMoney().stripTrailingZeros().toPlainString() + "(" + event.getTotal() + ")";
                SpannableStringBuilder style = new SpannableStringBuilder();
                style.append(cartStr);
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffb700)), 0, cartStr.indexOf("("), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                shoppingCartTextView.setText(style);
            }
        } else if (event.getType() == 2) {
            //更新商品列表
            BigDecimal floorPriceBig = new BigDecimal(floorPrice);
            BigDecimal totalMoneyFloat = event.getTotalMoney();
            if (totalMoneyFloat.compareTo(floorPriceBig) == 1 || totalMoneyFloat.compareTo(floorPriceBig) == 0) {//小于 时，返回 -1   等于 时，返回 0   大于 时，返回 1
                settlementTextView.setText("结算");
                settlementTextView.setEnabled(true);
            } else {
                settlementTextView.setText("还差¥" + floorPriceBig.subtract(totalMoneyFloat) + "起送");
                settlementTextView.setEnabled(false);
            }
            if (event.getTotal() <= 0) {
                shoppingCartTextView.setText("未选购商品");
                settlementTextView.setText("¥" + floorPrice + "起送");
                settlementTextView.setEnabled(false);
            } else {
                String cartStr = "¥" + event.getTotalMoney().stripTrailingZeros().toPlainString() + "(" + event.getTotal() + ")";
                SpannableStringBuilder style = new SpannableStringBuilder();
                style.append(cartStr);
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffb700)), 0, cartStr.indexOf("("), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                shoppingCartTextView.setText(style);
            }
        } else if (event.getType() == 3) {
            //清空购物车
            shoppingCartTextView.setText("未选购商品");
            settlementTextView.setText("¥" + floorPrice + "起送");
            settlementTextView.setEnabled(false);
        }
        productNum = event.getTotal();
        totalPrice = event.getTotalMoney();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    //更新购物车列表
                    if (data != null) {
                        shopCartPopWindow.setTotal(data.getIntExtra("productNum", 0), (BigDecimal) data.getSerializableExtra("totalPrice"));
                        List<ProductDetail> list = data.getParcelableArrayListExtra("shopChartList");
                        shopCartPopWindow.setDataSource(list);
                    }
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
