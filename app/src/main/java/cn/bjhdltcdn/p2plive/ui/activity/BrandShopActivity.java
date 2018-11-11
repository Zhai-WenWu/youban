package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.handler.AdvertisementHandler;
import cn.bjhdltcdn.p2plive.httpresponse.GetBrandShopInfoListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHomeBannerListResponse;
import cn.bjhdltcdn.p2plive.model.RecommendInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.BrandShopPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetRecommendListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.BrandShopListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.HomeAdvertisementTabAdapter;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import cn.bjhdltcdn.p2plive.widget.LoadingView;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Description:品牌商
 * Data: 2018/9/13
 *
 * @author: zhudi
 */
public class BrandShopActivity extends BaseActivity implements BaseView {
    private CustomViewPager viewPager;
    private Handler handler = new Handler();
    //    private AdvertisementHandler handler = new AdvertisementHandler(new WeakReference<Activity>(BrandShopActivity.this));
    private int previousPosition = 0;
    private ImageView shopIcon;
    private TextView contentView;
    private RecyclerView labelRecyclerView, shopRecyclerView;
    private BrandShopPresenter brandShopPresenter;
    private GetRecommendListPresenter getRecommendListPresenter;
    private BrandShopListAdapter brandShopListAdapter;
    private int pageSize = 20, pageNumber = 1;

    public BrandShopPresenter getBrandShopPresenter() {
        if (brandShopPresenter == null) {
            brandShopPresenter = new BrandShopPresenter(this);
        }
        return brandShopPresenter;
    }

    public GetRecommendListPresenter getGetRecommendListPresenter() {
        if (getRecommendListPresenter == null) {
            getRecommendListPresenter = new GetRecommendListPresenter(this);
        }
        return getRecommendListPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brandshop);
        init();
        getRecommendListPresenter.getHomeBannerList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 5);
        requestData();
    }

    private void init() {
        shopIcon = findViewById(R.id.iv_icon);
        contentView = findViewById(R.id.tv_content);
        labelRecyclerView = findViewById(R.id.rv_label);
        shopRecyclerView = findViewById(R.id.rv_shop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        shopRecyclerView.setLayoutManager(linearLayoutManager);
        brandShopListAdapter = new BrandShopListAdapter();
        shopRecyclerView.setAdapter(brandShopListAdapter);
        // 刷新框架
        TwinklingRefreshLayout refreshLayout = findViewById(R.id.refresh_layout_view);
        // 头部加载样式
        ProgressLayout header = new ProgressLayout(App.getInstance());
        refreshLayout.setHeaderView(header);
        // 底部加载样式
        LoadingView loadingView = new LoadingView(this);
        refreshLayout.setBottomView(loadingView);
        refreshLayout.setFloatRefresh(false);//支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。
        refreshLayout.setEnableOverScroll(false);//是否允许越界回弹。
        refreshLayout.setHeaderHeight(50);
        refreshLayout.setMaxHeadHeight(120);
        refreshLayout.setTargetView(shopRecyclerView);//设置滚动事件的作用对象。
        refreshLayout.setEnableRefresh(true);//灵活的设置是否禁用上下拉。
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setAutoLoadMore(false);//是否在底部越界的时候自动切换到加载更多模式

        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishRefreshing();
                pageNumber = 1;
                requestData();
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                refreshLayout.finishLoadmore();
                requestData();
            }
        });
    }

    private void requestData() {
        getBrandShopPresenter().getBrandShopInfoList(pageNumber, pageSize);
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETBRANDSHOPINFOLIST:
                if (object instanceof GetBrandShopInfoListResponse) {
                    GetBrandShopInfoListResponse getBrandShopInfoListResponse = (GetBrandShopInfoListResponse) object;
                    if (getBrandShopInfoListResponse.getCode() == 200) {
                        brandShopListAdapter.updateList(getBrandShopInfoListResponse.getList());
                    }
                }
                break;
            case InterfaceUrl.URL_GETHOMEBANNERLIST:
                if (object instanceof GetHomeBannerListResponse) {
                    GetHomeBannerListResponse getHomeBannerListResponse = (GetHomeBannerListResponse) object;
                    if (getHomeBannerListResponse.getCode() == 200) {
                        setHeaderViewData(getHomeBannerListResponse.getList());
                        Utils.ImageViewDisplayByUrl(getHomeBannerListResponse.getLogoUrl(), shopIcon);
                    }
                }
                break;
            default:
        }
    }

    public void setHeaderViewData(List<RecommendInfo> list) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                    .transform(new GlideRoundTransform(9))
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        List<RecommendInfo> arrayList = null;
        final LinearLayout llPointGroup;
        if (arrayList == null) {
            arrayList = new ArrayList<>(1);
        }
        arrayList.clear();
        if (list != null && list.size() > 0) {
            arrayList.addAll(list);
        } else {
            return;
        }
        List<View> viewList = new ArrayList<View>();
        viewPager = findViewById(R.id.advertisement_head_view_page);
        llPointGroup = findViewById(R.id.ll_point_group);
        llPointGroup.removeAllViews();
        View advertisementView;
        String recommendImgUrl;
        for (int i = 0; i < list.size(); i++) {
            advertisementView = LayoutInflater.from(App.getInstance()).inflate(R.layout.advertisement_item_layout, null);
            ImageView advertisementImage = advertisementView.findViewById(R.id.advertisement_image);
            final RecommendInfo recommendInfo = arrayList.get(i);
            recommendImgUrl = recommendInfo.getImgUrl();
            Glide.with(App.getInstance()).asBitmap().load(recommendImgUrl).apply(options).into(advertisementImage);
            viewList.add(advertisementView);
            advertisementView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String gotoUrl = recommendInfo.getGotoUrl();
                    if (!TextUtils.isEmpty(gotoUrl)) {
                        Intent intent = new Intent(BrandShopActivity.this, WXPayEntryActivity.class);
                        intent.putExtra(Constants.Action.ACTION_EXTRA, 6);
//                        intent.putExtra(Constants.KEY.KEY_URL, gotoUrl + "?userId=" + userId);
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

//设置广告高度
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) headOneView.getLayoutParams();
//        params.height = PlatformInfoUtils.getWidthOrHeight(mActivity)[0] * 37 / 75;
//        headOneView.setLayoutParams(params);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
