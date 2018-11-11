package cn.bjhdltcdn.p2plive.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
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
import cn.bjhdltcdn.p2plive.ui.adapter.ShopRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.ShopCategoryPopWindow;

/**
 *
 * 校园店列表界面
 */
public class StoreRecycleFragment extends BaseFragment implements BaseView{
    private AppCompatActivity mActivity;
    private StoreFragment storeFragment;
    private View rootView;
    private GetRecommendListPresenter getRecommendListPresenter;
    private GetStoreListPresenter getStoreListPresenter;
    private Long userId;
    private RecyclerView recycleView;
    private ShopRecyclerViewAdapter recyclerAdapter;
    private String searchContent="";//搜索内容
    private long distanceSort=29;
    private List<FirstLabelInfo> categoryLableList;
    private List<Long> categoryLableIdList;
    private int pageSize=10,pageNumber=1;
    private View emptyView;
    private TextView empty_tv,empty_list_tv;
    private List<StoreDetail> recommendStoreInfoList;
    private int type=1;//0:bannner店铺1：推荐店铺
    private String blankStr;
    private boolean hasRecommendStoreList;//是否已经访问过推荐店铺的接口
    private int currentPosition;

    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
        storeFragment= (StoreFragment) mActivity.getSupportFragmentManager().getFragments().get(1);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_store_recycle_layout, null);
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
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onVisible(boolean isInit) {
       if(isInit){
           userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
           getGetStoreListPresenter().getRecommendMerchantList(type,userId);
           getGetStoreListPresenter().findStoreKeywordList(userId,searchContent,distanceSort,categoryLableIdList,pageSize,pageNumber);
       }
    }

    public void refreshStoreList(String searchContent,long distanceSort,List<FirstLabelInfo> categoryLableList){
        pageNumber=1;
        this.distanceSort=distanceSort;
        this.categoryLableList=categoryLableList;
        if(categoryLableIdList==null){
            categoryLableIdList=new ArrayList<Long>();
        }else{
            categoryLableIdList.clear();
        }
        if(categoryLableList!=null&&categoryLableList.size()>0){
            for (int i=0;i<categoryLableList.size();i++){
                categoryLableIdList.add(categoryLableList.get(i).getFirstLabelId());
            }
        }
        getGetStoreListPresenter().findStoreKeywordList(userId,searchContent,distanceSort,categoryLableIdList,pageSize,pageNumber);
    }

    public void getStoreList(int pageNumber){
        this.pageNumber=pageNumber;
        getGetStoreListPresenter().findStoreKeywordList(userId,searchContent,distanceSort,categoryLableIdList,pageSize,pageNumber);
    }

    public int getPageNumber() {
        return pageNumber;
    }

    private void initView() {
        emptyView=rootView.findViewById(R.id.empty_view);
        empty_tv=rootView.findViewById(R.id.empty_textView);
        empty_list_tv=rootView.findViewById(R.id.empty_list_text_view);
        recyclerAdapter=new ShopRecyclerViewAdapter(mActivity);
        recycleView=rootView.findViewById(R.id.shop_recycler_view);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(11));
        recycleView.setLayoutManager(linearLayoutManager);
//        recycleView.addItemDecoration(linearLayoutSpaceItemDecoration);
        recycleView.setAdapter(recyclerAdapter);
        recyclerAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到店铺详情页
                currentPosition=position;
                Intent intent=new Intent(mActivity,ShopDetailActivity.class);
                intent.putExtra(Constants.Fields.STORE_ID,recyclerAdapter.getItem(position).getStoreInfo().getStoreId());
                startActivity(intent);
            }
        });
    }



    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (pageNumber == 1) {
                if(storeFragment!=null){
                    storeFragment.finishRefresh(false);
                }
            } else {
                if(storeFragment!=null) {
                    storeFragment.finishLoadMore(false);
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST)) {
            if (object instanceof GetRecommendMerchantListResponse) {
                GetRecommendMerchantListResponse getRecommendMerchantListResponse = (GetRecommendMerchantListResponse) object;
                if (getRecommendMerchantListResponse.getCode() == 200) {
                    if(getRecommendMerchantListResponse.getType()==1){
                        recommendStoreInfoList = getRecommendMerchantListResponse.getStoreList();
                        hasRecommendStoreList=true;
                        if (recommendStoreInfoList != null && recommendStoreInfoList.size() > 0) {
                                    emptyView.setVisibility(View.GONE);
                                    empty_list_tv.setVisibility(View.VISIBLE);
                                    recyclerAdapter.setList(recommendStoreInfoList);
                                    storeFragment.setEnableLoadMore(false,0);
                        }else{
                                    empty_list_tv.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                    empty_tv.setText(blankStr);
                        }
                    }

                } else {
                    Utils.showToastShortTime(getRecommendMerchantListResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_FINDSTOREKEYWORDLIST)) {
            if (object instanceof GetStoreListResponse) {
                GetStoreListResponse getStoreListResponse = (GetStoreListResponse) object;
                if (pageNumber == 1) {
                    if(storeFragment!=null){
                        storeFragment.finishRefresh(true);
                    }
                } else {
                    if(storeFragment!=null) {
                        storeFragment.finishLoadMore(true);
                    }
                }
                if (getStoreListResponse.getCode() == 200) {
                    blankStr=getStoreListResponse.getBlankHint();
                    List<StoreDetail> list=getStoreListResponse.getList();
                    if (getStoreListResponse.getTotal() == 0) {
                        if(pageNumber == 1){
                            if (recommendStoreInfoList != null && recommendStoreInfoList.size() > 0) {
                                emptyView.setVisibility(View.GONE);
                                empty_list_tv.setVisibility(View.VISIBLE);
                                recyclerAdapter.setList(recommendStoreInfoList);
                                storeFragment.setEnableLoadMore(false,0);
                            }else{
                                if(!hasRecommendStoreList){
                                    type=1;
                                    getGetStoreListPresenter().getRecommendMerchantList(type,userId);
                                }else{
                                    empty_list_tv.setVisibility(View.GONE);
                                    emptyView.setVisibility(View.VISIBLE);
                                    empty_tv.setText(blankStr);
                                }
                            }
                        }
                    } else {
                        if(pageNumber == 1){
                            emptyView.setVisibility(View.GONE);
                            empty_list_tv.setVisibility(View.GONE);
                        }
                    }
                    if(list!=null&&list.size()>0) {
                        if (pageNumber == 1) {
                            recyclerAdapter.setList(list);
                        } else {
                            recyclerAdapter.addList(list);
                        }
                        if(getStoreListResponse.getTotal()<=pageNumber*pageSize){
                            //没有更多数据时  下拉刷新不可用
                            storeFragment.setEnableLoadMore(false,0);
                        }else
                        {
                            //有更多数据时  下拉刷新才可用
                            storeFragment.setEnableLoadMore(true,0);
                            pageNumber++;
                        }
                    }

                } else {
                    Utils.showToastShortTime(getStoreListResponse.getMsg());
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateShopDetailEvent event) {
        if (event == null) {
            return;
        }
       //本地更新店铺列表中的店铺信息
        if(event.getType()!=1){
            recyclerAdapter.getList().set(currentPosition,event.getStoreDetail());
            recyclerAdapter.notifyItemChanged(currentPosition);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final ForwardStoreDetailEvent event) {
        if (event == null) {
            return;
        }
        currentPosition=event.getPosition();
        Intent intent=new Intent(mActivity,ShopDetailActivity.class);
        intent.putExtra(Constants.Fields.STORE_ID,recyclerAdapter.getItem(event.getPosition()).getStoreInfo().getStoreId());
        startActivity(intent);
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
        EventBus.getDefault().unregister(this);
    }


}
