package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ForwardStoreDetailEvent;
import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendMerchantListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyForShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.CreateShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationActivity;
import cn.bjhdltcdn.p2plive.ui.activity.RealNameCertificationAndShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.StoreCategoryActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopDetailTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.StoreTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.widget.ShopCategoryPopWindow;

/**
 *
 * 校园店列表界面
 */
public class StoreFragment extends BaseFragment implements BaseView{
    private AppCompatActivity mActivity;
    private View rootView;
    private GetRecommendListPresenter getRecommendListPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private Long userId;
    private HomeBannerFragment homeBannerFragment;
    private CustomViewPager mViewPager;
    private TabLayout mTabLayout;
    private StoreTabFragmentAdapter adapter;
    private RefreshLayout refreshLayout;
    private EditText searchEditText;
    private String searchContent="";//搜索内容
    private long distanceSort=29;
    private List<FirstLabelInfo> selectTypeList;
    private List<LabelInfo> distanceList;
    private List<FirstLabelInfo> typeList;
    private TextView storeTextView,categorySreenTextView;
    private int pageSize=10,pageNumber=1;
    private ShopCategoryPopWindow shopCategoryPopWindow;
    private View emptyView;
    private TextView empty_tv,empty_list_tv;
    private List<StoreDetail> recommendStoreInfoList;
    private int type;//0:bannner店铺1：推荐店铺
    private String blankStr;
    private boolean hasRecommendStoreList;//是否已经访问过推荐店铺的接口
    private int VerticalOffset;
    private int currentPosition;
    private boolean enableLoadMoreForShop, enableLoadMoreForGoods;
    private StoreRecycleFragment storeRecycleFragment;
    private StoreGoodsRecycleFragment storeGoodsRecycleFragment;
    private int isCreateStore;
    private long storeId;

    public GetStoreListPresenter getGetStoreListPresenter() {
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_store_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//        EventBus.getDefault().register(this);
        initView();
        setTitle();
    }

    @Override
    protected void onVisible(boolean isInit) {
       if(isInit){
           getGetStoreListPresenter().getRecommendMerchantList(type,userId);
           getGetStoreListPresenter().getStoreLabelList(userId);
           getGetStoreListPresenter().judgeCreateStoreAuth(userId);
       }
    }


    public void refreshList(long distanceSort,List<FirstLabelInfo> labelInfoList){
        this.distanceSort=distanceSort;
        this.selectTypeList=labelInfoList;
//        switch (type) {
//            case 0:
                if (storeRecycleFragment != null) {
                    storeRecycleFragment.refreshStoreList(searchContent,distanceSort,labelInfoList);
                }
//                break;
//            case 1:
                if (storeGoodsRecycleFragment != null) {
                    storeGoodsRecycleFragment.refreshStoreList(searchContent,distanceSort,labelInfoList);
                }
//                break;
//        }
    }



    private void initView() {
        mViewPager = (CustomViewPager) rootView.findViewById(R.id.shop_detail_view_page);
        mViewPager.setIsCanScroll(true);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        mTabLayout.getLayoutParams().width= (PlatformInfoUtils.getWidthOrHeight(mActivity)[0]/2)-Utils.dp2px(2.5f);
        adapter = new StoreTabFragmentAdapter(mActivity.getSupportFragmentManager());
        storeRecycleFragment = new StoreRecycleFragment();
        adapter.addFragment(storeRecycleFragment, "逛店铺");
        storeGoodsRecycleFragment = new StoreGoodsRecycleFragment();
        adapter.addFragment(storeGoodsRecycleFragment, "看商品");
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            View tabView = adapter.getTabView(i);
            tab.setCustomView(tabView);
        }
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
            textView.setTextColor(getResources().getColor(R.color.color_333333));

            mViewPager.setCurrentItem(tab.getPosition());
            type = tab.getPosition();
            switch (type) {
                case 0:
                    refreshLayout.setEnableLoadMore(enableLoadMoreForShop);
                    break;
                case 1:
                    refreshLayout.setEnableLoadMore(enableLoadMoreForGoods);
                    break;
            }

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            TextView textView = (TextView) tab.getCustomView().findViewById(R.id.text_view);
            textView.setTextColor(getResources().getColor(R.color.color_999999));

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
//                KLog.d("onTabReselected()");
        }
    });

        refreshLayout = (RefreshLayout)rootView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                switch (type) {
                    case 0:
                        if (storeRecycleFragment != null) {
                            storeRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                        }
                        break;
                    case 1:
                        if (storeGoodsRecycleFragment != null) {
                            storeGoodsRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                        }
                        break;
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                switch (type) {
                    case 0:
                        if (storeRecycleFragment != null) {
                            storeRecycleFragment.getStoreList(storeRecycleFragment.getPageNumber());
                        }
                        break;
                    case 1:
                        if (storeGoodsRecycleFragment != null) {
                            storeGoodsRecycleFragment.getStoreList(storeGoodsRecycleFragment.getPageNumber());
                        }
                        break;
                }
            }
        });
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹
        searchEditText=rootView.findViewById(R.id.search_edittext);
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    searchContent = searchEditText.getText().toString();
                    if (!StringUtils.isEmpty(searchContent)) {
//                        switch (type) {
//                            case 0:
                                if (storeRecycleFragment != null) {
                                    storeRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                                }
//                                break;
//                            case 1:
                                if (storeGoodsRecycleFragment != null) {
                                    storeGoodsRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                                }
//                                break;
//                        }
                    } else {
                        Utils.showToastShortTime("请输入搜索内容");
                    }
                    return true;
                }
                return false;
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchContent = searchEditText.getText().toString();
                if (StringUtils.isEmpty(searchContent)) {
//                    switch (type) {
//                        case 0:
                            if (storeRecycleFragment != null) {
                                storeRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                            }
//                            break;
//                        case 1:
                            if (storeGoodsRecycleFragment != null) {
                                storeGoodsRecycleFragment.refreshStoreList(searchContent,distanceSort,selectTypeList);
                            }
//                            break;
//                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //监听软键盘是否显示或隐藏
        searchEditText.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        searchEditText.getWindowVisibleDisplayFrame(r);
                        int screenHeight = searchEditText.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);
                        if (heightDifference > 200) {
                            //软键盘显示
                            if(shopCategoryPopWindow.isShowing()){
                                shopCategoryPopWindow.dismiss();
                            }
                        } else {
                            //软键盘隐藏

                        }
                    }

                });
        homeBannerFragment = (HomeBannerFragment) getChildFragmentManager().findFragmentById(R.id.banner_frame);
        shopCategoryPopWindow=new ShopCategoryPopWindow(this);
        categorySreenTextView=rootView.findViewById(R.id.screening_text_view);
        categorySreenTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shopCategoryPopWindow!=null&&typeList!=null&&distanceList!=null){
                    if(shopCategoryPopWindow.isShowing()){
                        shopCategoryPopWindow.dismiss();
                    }else {
                        shopCategoryPopWindow.showDataSource(distanceSort,selectTypeList);
                        shopCategoryPopWindow.showAtLocation(rootView);
                    }
                }
            }
        });
        storeTextView=rootView.findViewById(R.id.my_shop_text_view);
        storeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (isCreateStore){
                    case 1:
                        //用户没有选择开店标签
                        Intent intent1=new Intent(mActivity,StoreCategoryActivity.class);
                        intent1.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent1);
                        break;
                    case 2:
                        //实名 开店两个都没都没申请 或者都拒绝
                        Intent intent2=new Intent(mActivity,RealNameCertificationAndShopActivity.class);
                        intent2.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent2);
                        break;
                    case 3:
                        //跳转实名认证
                        Intent intent3=new Intent(mActivity,RealNameCertificationActivity.class);
                        intent3.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent3);
                        break;
                    case 4:
                        //跳转申请开店界面
                        Intent intent4=new Intent(mActivity, ApplyForShopActivity.class);
                        intent4.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent4);
                        break;
                    case 5:
                        //创建店铺
                        Intent intent5=new Intent(mActivity,CreateShopActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        //店铺详情
                        Intent intent6=new Intent(mActivity,ShopDetailActivity.class);
                        intent6.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent6);
                        break;
                }

            }
        });

    }

    private void setTitle() {
        AppBarLayout mAppBarLayout= (AppBarLayout) rootView.findViewById(R.id.app_bar);
        final Toolbar mToolbar= (Toolbar) rootView.findViewById(R.id.AppFragment_Toolbar);
        final View lineView=(View) rootView.findViewById(R.id.title_line);
        final boolean[] topShow = {false};
        final boolean[] bottomShow = { true };
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mToolbar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.color_ffffff),Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()));
                Log.e("verticalOffset",verticalOffset+"---"+appBarLayout.getTotalScrollRange()+"---"+Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange());

                if(Math.abs(verticalOffset)<appBarLayout.getTotalScrollRange()/2){
                    if(topShow[0]){
                        searchEditText.setBackground(getResources().getDrawable(R.drawable.shape_round_40_solid_66000000));
                        searchEditText.setHintTextColor(getResources().getColor(R.color.color_ffffff));
                        searchEditText.setTextColor(getResources().getColor(R.color.color_ffffff));
                        searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.store_search_white_icon),null,null,null);
                        topShow[0] =false;
                        bottomShow[0] =true;
                    }
//                    backImageView.setAlpha(1-(Math.abs(verticalOffset*1.0f)/(appBarLayout.getTotalScrollRange()/2)));//(透明度从1到0)
                }else{
                    if(bottomShow[0]){
                        searchEditText.setBackground(getResources().getDrawable(R.drawable.shape_round_20_solid_f7f7f7));
                        searchEditText.setHintTextColor(getResources().getColor(R.color.color_999999));
                        searchEditText.setTextColor(getResources().getColor(R.color.color_999999));

                        searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.mipmap.home_search_icon),null,null,null);

                        bottomShow[0] =false;
                        topShow[0] =true;
                    }
//                    backImageView.setAlpha(((Math.abs(verticalOffset*1.0f)-(appBarLayout.getTotalScrollRange()/2))/(appBarLayout.getTotalScrollRange()/2)));//(透明度从0到1)
                }
                if(Math.abs(verticalOffset*1.0f)/appBarLayout.getTotalScrollRange()==1){
                    lineView.setVisibility(View.VISIBLE);
                }else{
                    lineView.setVisibility(View.GONE);
                }
            }
        });
    }

    /** 根据百分比改变颜色透明度 */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            refreshLayout.finishRefresh(false);//传入false表示刷新失败
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST)) {
            if (object instanceof GetRecommendMerchantListResponse) {
                GetRecommendMerchantListResponse getRecommendMerchantListResponse = (GetRecommendMerchantListResponse) object;
                if (getRecommendMerchantListResponse.getCode() == 200) {
                    if(getRecommendMerchantListResponse.getType()==0){
                        List<StoreDetail> recommendList = getRecommendMerchantListResponse.getStoreList();
//                        if (recommendList != null && recommendList.size() > 0) {
                            homeBannerFragment.setHeaderStoreData(recommendList);
//                        }
                    }
                } else {
                    Utils.showToastShortTime(getRecommendMerchantListResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_GETSTORELABELLIST)) {
            if (object instanceof GetStoreLabelListResponse) {
                GetStoreLabelListResponse getStoreLabelListResponse = (GetStoreLabelListResponse) object;
                if (getStoreLabelListResponse.getCode() == 200) {
                    distanceList = getStoreLabelListResponse.getDistanceList();
                    typeList=getStoreLabelListResponse.getTypeList();
                    shopCategoryPopWindow.setDataSource(distanceList,typeList);
                } else {
                    Utils.showToastShortTime(getStoreLabelListResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_JUDGECREATESTOREAUTH)) {
            if (object instanceof JudgeCreateStoreAuthResponse) {
                JudgeCreateStoreAuthResponse judgeCreateStoreAuthResponse = (JudgeCreateStoreAuthResponse) object;
                if (judgeCreateStoreAuthResponse.getCode() == 200) {
                    //1 没有用户店铺标签，跳转创建店铺标签;
//                    2 没有实名认证和申请开店记录;
//                    或开店申请拒绝同时实名认证被拒绝,调用实名认证整合接口;
//                    3 未实名认证或被拒绝,申请开店审核中或通过,跳转实名认证
//                    4 实名认证审核中或通过，未申请开店或申请被拒绝，跳转申请开店;
//                    5 实名认证审核中或通过，开店申请通过或审核中，跳转创建店铺;
//                    6 已开店并且实名认证和开店申请都通过,调用店铺详情

                    isCreateStore=judgeCreateStoreAuthResponse.getIsCreateStore();
                    storeId=judgeCreateStoreAuthResponse.getStoreId();
                    if(storeId>0){
                        storeTextView.setText("我的店");
                    }else{
                        storeTextView.setText("申请开店");
                    }
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
                } else {
                    Utils.showToastShortTime(judgeCreateStoreAuthResponse.getMsg());
                }
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
                enableLoadMoreForShop = enable;
                break;
            case 1:
                enableLoadMoreForGoods = enable;
                break;
        }
        refreshLayout.setEnableLoadMore(enable);
    }



    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(mActivity);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }


}
