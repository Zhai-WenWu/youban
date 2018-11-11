//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.AppBarLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.view.inputmethod.EditorInfo;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.scwang.smartrefresh.layout.api.RefreshLayout;
//import com.scwang.smartrefresh.layout.constant.RefreshState;
//import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
//import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
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
//import cn.bjhdltcdn.p2plive.event.ForwardStoreDetailEvent;
//import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
//import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendMerchantListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetStoreLabelListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetStoreListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
//import cn.bjhdltcdn.p2plive.model.LabelInfo;
//import cn.bjhdltcdn.p2plive.model.StoreDetail;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.ApplyForShopActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.CreateShopActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationAndShopActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.StoreCategoryActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.ShopRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.ShopCategoryPopWindow;
//
///**
// *
// * 校园店列表界面
// */
//public class ShopFragment extends BaseFragment implements BaseView{
//    private AppCompatActivity mActivity;
//    private View rootView;
//    private GetRecommendListPresenter getRecommendListPresenter;
//    private GetStoreListPresenter getStoreListPresenter;
//    private Long userId;
//    private HomeBannerFragment homeBannerFragment;
//    private RecyclerView recycleView;
//    private ShopRecyclerViewAdapter recyclerAdapter;
//    private AppBarLayout appBarLayout;
//    private RefreshLayout refreshLayout;
//    private EditText searchEditText;
//    private String searchContent="";//搜索内容
//    private long distanceSort,typeSort;
//    private int merchantSort;
//    private List<LabelInfo> distanceList,typeList;
//    private int merchantSwitch;//商家开关(1开启,2关闭),
//    private RelativeLayout shopScreenLabelLayout;
//    private TextView locationScreenTextView,sellerScreenTextView,categorySreenTextView;
//    private int pageSize=10,pageNumber=1;
//    private ShopCategoryPopWindow shopCategoryPopWindow,shopLocationPopWindow;
//    private View emptyView;
////    private NestedScrollView nestedScrollView;
//    private TextView empty_tv,empty_list_tv;
//    private List<StoreDetail> recommendStoreInfoList;
//    private int type;//0:bannner店铺1：推荐店铺
//    private String blankStr;
//    private boolean hasRecommendStoreList;//是否已经访问过推荐店铺的接口
//    private int VerticalOffset;
//    private int currentPosition;
//
//    public GetStoreListPresenter getGetStoreListPresenter() {
//        if (getStoreListPresenter == null) {
//            getStoreListPresenter = new GetStoreListPresenter(this);
//        }
//        return getStoreListPresenter;
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mActivity = (AppCompatActivity) context;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if (rootView == null) {
//            rootView = inflater.inflate(R.layout.activity_shop_layout, null);
//        }
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        EventBus.getDefault().register(this);
//        initView();
//        setTitle();
//    }
//
//    @Override
//    protected void onVisible(boolean isInit) {
//       if(isInit){
//           getGetStoreListPresenter().getRecommendMerchantList(type,userId);
//           getGetStoreListPresenter().getStoreLabelList(userId);
//           getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//       }
//    }
//
//    public void refreshByLocation(LabelInfo labelInfo){
//        pageNumber=1;
//        locationScreenTextView.setText(labelInfo.getLabelName());
//        locationScreenTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.text_open_icon_333333),null);
//        distanceSort=labelInfo.getLabelId();
//        getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//    }
//
//    public void refreshByCategory(LabelInfo labelInfo){
//        pageNumber=1;
//        categorySreenTextView.setText(labelInfo.getLabelName());
//        categorySreenTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.text_open_icon_333333),null);
//        typeSort=labelInfo.getLabelId();
//        getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//    }
//
//
//    private void initView() {
//        emptyView=rootView.findViewById(R.id.empty_view);
////        nestedScrollView=findViewById(R.id.nest_scrollView);
//        empty_tv=rootView.findViewById(R.id.empty_textView);
//        empty_list_tv=rootView.findViewById(R.id.empty_list_text_view);
//        refreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                pageNumber=1;
//                getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//            }
//        });
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshlayout) {
//                getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//            }
//        });
//        refreshLayout.setEnableAutoLoadMore(false);
//        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
//        refreshLayout.setEnableLoadMore(false);
//        appBarLayout = rootView.findViewById(R.id.app_bar);
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                //监听AppBarLauout的滑动
//                Log.e("verticalOffset",verticalOffset+"");
//                if(verticalOffset==0){
//                    RefreshState refreshState = refreshLayout.getState();
//                    if (refreshState.isDragging) {
//                        if(shopLocationPopWindow.isShowing()){
//                            shopLocationPopWindow.dismiss();
//                        }
//                        if(shopCategoryPopWindow.isShowing()){
//                            shopCategoryPopWindow.dismiss();
//                        }
//                    }
//                }else if(Math.abs(VerticalOffset-verticalOffset)>5){
//                    if(shopLocationPopWindow.isShowing()){
//                        shopLocationPopWindow.dismiss();
//                    }
//                    if(shopCategoryPopWindow.isShowing()){
//                        shopCategoryPopWindow.dismiss();
//                    }
//                }
//                VerticalOffset=verticalOffset;
//            }
//        });
//        searchEditText=rootView.findViewById(R.id.search_edittext);
//        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    /*隐藏软键盘*/
//                    InputMethodManager imm = (InputMethodManager) v
//                            .getContext().getSystemService(
//                                    Context.INPUT_METHOD_SERVICE);
//                    if (imm.isActive()) {
//                        imm.hideSoftInputFromWindow(
//                                v.getApplicationWindowToken(), 0);
//                    }
//
//                    searchContent = searchEditText.getText().toString();
//                    if (!StringUtils.isEmpty(searchContent)) {
//                        getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//                    } else {
//                        Utils.showToastShortTime("请输入搜索内容");
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });
//        searchEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                searchContent = searchEditText.getText().toString();
//                if (StringUtils.isEmpty(searchContent)) {
//                    getGetStoreListPresenter().getStoreList(userId,searchContent,distanceSort,merchantSort,typeSort,pageSize,pageNumber);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
//        //监听软键盘是否显示或隐藏
//        searchEditText.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//                    @Override
//                    public void onGlobalLayout() {
//                        Rect r = new Rect();
//                        searchEditText.getWindowVisibleDisplayFrame(r);
//                        int screenHeight = searchEditText.getRootView()
//                                .getHeight();
//                        int heightDifference = screenHeight - (r.bottom);
//                        if (heightDifference > 200) {
//                            //软键盘显示
//                            if(shopLocationPopWindow.isShowing()){
//                                shopLocationPopWindow.dismiss();
//                            }
//                            if(shopCategoryPopWindow.isShowing()){
//                                shopCategoryPopWindow.dismiss();
//                            }
//                        } else {
//                            //软键盘隐藏
//
//                        }
//                    }
//
//                });
//        homeBannerFragment = (HomeBannerFragment) getChildFragmentManager().findFragmentById(R.id.banner_frame);
//        shopScreenLabelLayout=rootView.findViewById(R.id.shop_screen_view);
//        shopCategoryPopWindow=new ShopCategoryPopWindow(this);
//        shopLocationPopWindow=new ShopCategoryPopWindow(this);
//        locationScreenTextView=rootView.findViewById(R.id.location_screen_text);
//        locationScreenTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(shopCategoryPopWindow!=null&&shopCategoryPopWindow.isShowing()){
//                    shopCategoryPopWindow.dismiss();
//                }
//               if(shopLocationPopWindow!=null&&distanceList!=null){
//                   if(shopLocationPopWindow.isShowing()){
//                       shopLocationPopWindow.dismiss();
//                   }else{
//                       locationScreenTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.text_close_icon_333333),null);
//                       shopLocationPopWindow.setType(0);
//                       shopLocationPopWindow.setDataSource(distanceList,distanceSort);
//                       shopLocationPopWindow.showPopupWindow(locationScreenTextView);
//                   }
//
//               }
//            }
//        });
//        sellerScreenTextView=rootView.findViewById(R.id.seller_screen_text);
//        categorySreenTextView=rootView.findViewById(R.id.category_sreen_text);
//        categorySreenTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(shopLocationPopWindow!=null&&shopLocationPopWindow.isShowing()){
//                    shopLocationPopWindow.dismiss();
//                }
//                if(shopCategoryPopWindow!=null&&typeList!=null){
//                    if(shopCategoryPopWindow.isShowing()){
//                        shopCategoryPopWindow.dismiss();
//                    }else {
//                        categorySreenTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.text_close_icon_333333), null);
//                        shopCategoryPopWindow.setType(1);
//                        shopCategoryPopWindow.setDataSource(typeList,typeSort);
//                        shopCategoryPopWindow.showPopupWindow(categorySreenTextView);
//                    }
//                }
//            }
//        });
//        recyclerAdapter=new ShopRecyclerViewAdapter(mActivity);
//        recycleView=rootView.findViewById(R.id.shop_recycler_view);
//        recycleView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
////        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
//        recycleView.setLayoutManager(linearLayoutManager);
////        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
//        recycleView.setAdapter(recyclerAdapter);
//        recyclerAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到店铺详情页
//                currentPosition=position;
//                Intent intent=new Intent(mActivity,ShopDetailActivity.class);
//                intent.putExtra(Constants.Fields.STORE_ID,recyclerAdapter.getItem(position).getStoreInfo().getStoreId());
//                startActivity(intent);
//            }
//        });
//        shopLocationPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                locationScreenTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.text_open_icon_333333),null);
//
//            }
//        });
//        shopCategoryPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                categorySreenTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.text_open_icon_333333),null);
//
//            }
//        });
//    }
//
//    private void setTitle() {
//        TitleFragment fragment = (TitleFragment) getChildFragmentManager().findFragmentById(R.id.title_fragment);
//        fragment.setTitle(R.string.title_shop);
//        fragment.setRightViewTitle("我的店", new TitleFragment.RightViewClick() {
//            @Override
//            public void onClick() {
//                //跳转到申请开店界面
//                getGetStoreListPresenter().judgeCreateStoreAuth(userId);
//            }
//        });
//        fragment.setRightViewColor(R.color.color_ffb700);
//    }
//
//
//    @Override
//    public void updateView(String apiName, Object object) {
//        if (object instanceof Exception) {
//            Exception e = (Exception) object;
//            String text = e.getMessage();
//            Utils.showToastShortTime(text);
//            if (pageNumber == 1) {
//                refreshLayout.finishRefresh(0, false);//传入false表示刷新失败
//            }else {
//                refreshLayout.finishLoadMore(false);//传入false表示加载失败
//            }
//            return;
//        }
//        if (apiName.equals(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST)) {
//            if (object instanceof GetRecommendMerchantListResponse) {
//                GetRecommendMerchantListResponse getRecommendMerchantListResponse = (GetRecommendMerchantListResponse) object;
//                if (getRecommendMerchantListResponse.getCode() == 200) {
//                    if(getRecommendMerchantListResponse.getType()==0){
//                        List<StoreDetail> recommendList = getRecommendMerchantListResponse.getStoreList();
//                        if (recommendList != null && recommendList.size() > 0) {
//                            homeBannerFragment.setHeaderStoreData(recommendList);
//                        }
//                    }else if(getRecommendMerchantListResponse.getType()==1){
//                        recommendStoreInfoList = getRecommendMerchantListResponse.getStoreList();
//                        hasRecommendStoreList=true;
//                        if (recommendStoreInfoList != null && recommendStoreInfoList.size() > 0) {
//                                    emptyView.setVisibility(View.GONE);
////                                    nestedScrollView.setVisibility(View.GONE);
//                                    empty_list_tv.setVisibility(View.VISIBLE);
//                                    recyclerAdapter.setList(recommendStoreInfoList);
//                                    refreshLayout.setEnableLoadMore(false);
//                        }else{
//                                    empty_list_tv.setVisibility(View.GONE);
//                                    emptyView.setVisibility(View.VISIBLE);
////                                    nestedScrollView.setVisibility(View.VISIBLE);
//                                    empty_tv.setText(blankStr);
//                        }
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getRecommendMerchantListResponse.getMsg());
//                }
//            }
//        }else if (apiName.equals(InterfaceUrl.URL_GETSTORELABELLIST)) {
//            if (object instanceof GetStoreLabelListResponse) {
//                GetStoreLabelListResponse getStoreLabelListResponse = (GetStoreLabelListResponse) object;
//                if (getStoreLabelListResponse.getCode() == 200) {
//                    distanceList = getStoreLabelListResponse.getDistanceList();
//                    typeList=getStoreLabelListResponse.getTypeList();
//                    merchantSwitch=getStoreLabelListResponse.getMerchantSwitch();
//
//                    if(distanceList!=null&&distanceList.size()>0){
//                        locationScreenTextView.setText(distanceList.get(0).getLabelName());
//                        distanceSort=distanceList.get(0).getLabelId();
//                    }
//                    if(typeList!=null&&typeList.size()>0){
////                        categorySreenTextView.setText(typeList.get(0).getLabelName());
//                        categorySreenTextView.setText("全部");
//                        LabelInfo labelInfo=new LabelInfo();
//                        labelInfo.setLabelName("全部");
//                        labelInfo.setLabelId(0);
//                        typeList.add(0,labelInfo);
//                    }
//                    if((distanceList!=null&&distanceList.size()>0)||(typeList!=null&&typeList.size()>0)){
//                        shopScreenLabelLayout.setVisibility(View.VISIBLE);
//                    }
//                    if(merchantSwitch==1)
//                    {
//                        sellerScreenTextView.setVisibility(View.VISIBLE);
//                    }else{
//                        sellerScreenTextView.setVisibility(View.GONE);
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getStoreLabelListResponse.getMsg());
//                }
//            }
//        }else if (apiName.equals(InterfaceUrl.URL_GETSTORELIST)) {
//            if (object instanceof GetStoreListResponse) {
//                GetStoreListResponse getStoreListResponse = (GetStoreListResponse) object;
//                if (pageNumber == 1) {
//                    refreshLayout.finishRefresh();
//                } else {
//                    refreshLayout.finishLoadMore();
//                }
//                if (getStoreListResponse.getCode() == 200) {
//                    blankStr=getStoreListResponse.getBlankHint();
//                    List<StoreDetail> list=getStoreListResponse.getList();
//                    if (getStoreListResponse.getTotal() == 0) {
//                        if(pageNumber == 1){
//                            if (recommendStoreInfoList != null && recommendStoreInfoList.size() > 0) {
//                                emptyView.setVisibility(View.GONE);
////                                    nestedScrollView.setVisibility(View.GONE);
//                                empty_list_tv.setVisibility(View.VISIBLE);
//                                recyclerAdapter.setList(recommendStoreInfoList);
//                                refreshLayout.setEnableLoadMore(false);
//                            }else{
//                                if(!hasRecommendStoreList){
//                                    type=1;
//                                    getGetStoreListPresenter().getRecommendMerchantList(type,userId);
//                                }else{
//                                    empty_list_tv.setVisibility(View.GONE);
//                                    emptyView.setVisibility(View.VISIBLE);
////                                        nestedScrollView.setVisibility(View.VISIBLE);
//                                    empty_tv.setText(blankStr);
//                                }
//                            }
//                        }
//                    } else {
//                        if(pageNumber == 1){
//                            emptyView.setVisibility(View.GONE);
////                                nestedScrollView.setVisibility(View.GONE);
//                            empty_list_tv.setVisibility(View.GONE);
//                        }
//                    }
//                    if(list!=null&&list.size()>0) {
//                        if (pageNumber == 1) {
//                            recyclerAdapter.setList(list);
//                        } else {
//                            recyclerAdapter.addList(list);
//                        }
//                        if(getStoreListResponse.getTotal()<=pageNumber*pageSize){
//                            //没有更多数据时  下拉刷新不可用
//                            refreshLayout.setEnableLoadMore(false);
//                        }else
//                        {
//                            //有更多数据时  下拉刷新才可用
//                            refreshLayout.setEnableLoadMore(true);
//                            pageNumber++;
//                        }
//                    }
//
//                } else {
//                    Utils.showToastShortTime(getStoreListResponse.getMsg());
//                }
//            }
//        }else if (apiName.equals(InterfaceUrl.URL_JUDGECREATESTOREAUTH)) {
//            if (object instanceof JudgeCreateStoreAuthResponse) {
//                JudgeCreateStoreAuthResponse judgeCreateStoreAuthResponse = (JudgeCreateStoreAuthResponse) object;
//                if (judgeCreateStoreAuthResponse.getCode() == 200) {
//                    //1 没有用户店铺标签，跳转创建店铺标签;
////                    2 没有实名认证和申请开店记录;
////                    或开店申请拒绝同时实名认证被拒绝,调用实名认证整合接口;
////                    3 未实名认证或被拒绝,申请开店审核中或通过,跳转实名认证
////                    4 实名认证审核中或通过，未申请开店或申请被拒绝，跳转申请开店;
////                    5 实名认证审核中或通过，开店申请通过或审核中，跳转创建店铺;
////                    6 已开店并且实名认证和开店申请都通过,调用店铺详情
//
//                    int isCreateStore=judgeCreateStoreAuthResponse.getIsCreateStore();
//                    long storeId=judgeCreateStoreAuthResponse.getStoreId();
//                    switch (isCreateStore){
//                        case 1:
//                            //用户没有选择开店标签
//                            Intent intent1=new Intent(mActivity,StoreCategoryActivity.class);
//                            intent1.putExtra(Constants.Fields.STORE_ID,storeId);
//                            startActivity(intent1);
//                            break;
//                        case 2:
//                            //实名 开店两个都没都没申请 或者都拒绝
//                            Intent intent2=new Intent(mActivity,RealNameCertificationAndShopActivity.class);
//                            intent2.putExtra(Constants.Fields.STORE_ID,storeId);
//                            startActivity(intent2);
//                            break;
//                        case 3:
//                            //跳转实名认证
//                            Intent intent3=new Intent(mActivity,RealNameCertificationActivity.class);
//                            intent3.putExtra(Constants.Fields.STORE_ID,storeId);
//                            startActivity(intent3);
//                            break;
//                        case 4:
//                            //跳转申请开店界面
//                            Intent intent4=new Intent(mActivity, ApplyForShopActivity.class);
//                            intent4.putExtra(Constants.Fields.STORE_ID,storeId);
//                            startActivity(intent4);
//                            break;
//                        case 5:
//                            //创建店铺
//                            Intent intent5=new Intent(mActivity,CreateShopActivity.class);
//                            startActivity(intent5);
//                            break;
//                        case 6:
//                            //店铺详情
//                            Intent intent6=new Intent(mActivity,ShopDetailActivity.class);
//                            intent6.putExtra(Constants.Fields.STORE_ID,storeId);
//                            startActivity(intent6);
//                            break;
//                    }
//
//                } else {
//                    Utils.showToastShortTime(judgeCreateStoreAuthResponse.getMsg());
//                }
//            }
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(final UpdateShopDetailEvent event) {
//        if (event == null) {
//            return;
//        }
//       //本地更新店铺列表中的店铺信息
//        if(event.getType()!=1){
//            recyclerAdapter.getList().set(currentPosition,event.getStoreDetail());
//            recyclerAdapter.notifyItemChanged(currentPosition);
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void eventCallback(final ForwardStoreDetailEvent event) {
//        if (event == null) {
//            return;
//        }
//        currentPosition=event.getPosition();
//        Intent intent=new Intent(mActivity,ShopDetailActivity.class);
//        intent.putExtra(Constants.Fields.STORE_ID,recyclerAdapter.getItem(event.getPosition()).getStoreInfo().getStoreId());
//        startActivity(intent);
//    }
//
//    @Override
//    public void showLoading() {
//        ProgressDialogUtils.getInstance().showProgressDialog(mActivity);
//    }
//
//    @Override
//    public void hideLoading() {
//        ProgressDialogUtils.getInstance().hideProgressDialog();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//
//}
