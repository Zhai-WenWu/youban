package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;

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
import cn.bjhdltcdn.p2plive.event.UpdateGoodsListEvent;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreProductListResponse;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.AddGoodsActivity;
import cn.bjhdltcdn.p2plive.ui.activity.GoodsDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.GoodsRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomOperationPopWindow;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by Hu_PC on 2017/11/8.
 * 商品列表fragment
 */

public class GoodsRecycleFragment extends BaseFragment implements BaseView {
    private View rootView;
    private ShopDetailActivity mActivity;
    private RefreshLayout refreshLayout;
    private GetStoreListPresenter getStoreListPresenter;
    private RecyclerView recycleView;
    private GoodsRecycleAdapter goodsRecycleAdapter;
    private View emptyView;
    private int currenPosition;
    private long userId;
    private long storeId;
    private int pageSize=10,pageNumber=1;
    public CustomOperationPopWindow customOperationPopWindow;
    private int userType=3;//(1店主,2店员,3普通用户4店员申请中)
    public List<String> operationList;
    private int isHot,isNew,status;
    private int isSchoolmate;//是否是校友(1否2是),
    private int isReportMax;//本月被举报次数是否超过20次, 0:正常1：超过
    private int isAuth;//开店申请状态(1等待审核  2 审核通过 3审核拒绝)


    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
        if(userId==0){
            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        }
        getStoreListPresenter().getStoreProductList(userId,storeId,pageSize,pageNumber);
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setIsSchoolmate(int isSchoolmate) {
        this.isSchoolmate = isSchoolmate;
    }

    public void setIsReportMax(int isReportMax) {
        this.isReportMax = isReportMax;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (ShopDetailActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_goods_recycle, null);
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
        EventBus.getDefault().register(this);
        initView();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    @Override
    protected void onVisible(boolean isInit) {
        if(isInit){
            userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        }else{
            //每次都刷新
//            getStoreProductList(1);
        }
    }

    public void getStoreProductList(int pageNumber){
        this.pageNumber=pageNumber;
        getStoreListPresenter().getStoreProductList(userId,storeId,pageSize,pageNumber);
    }

    private void initView() {
//        refreshLayout = (RefreshLayout) rootView.findViewById(R.id.refreshLayout);
//        refreshLayout.setEnableRefresh(false);
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                getStoreProductList(pageNumber);
//            }
//        });
//        refreshLayout.setEnableLoadMore(false);
//        refreshLayout.setEnableAutoLoadMore(false);
//        refreshLayout.setEnableOverScrollBounce(false);//是否启用越界回弹

        recycleView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        emptyView = rootView.findViewById(R.id.empty_view);
        recycleView.setHasFixedSize(true);
        goodsRecycleAdapter = new GoodsRecycleAdapter(this);
        goodsRecycleAdapter.setIsSchoolmate(isSchoolmate);
        goodsRecycleAdapter.setIsReportMax(isReportMax);
        goodsRecycleAdapter.setIsAuth(isAuth);
        recycleView.setHasFixedSize(true);
        LinearLayoutManager itemLinearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        recycleView.setLayoutManager(itemLinearlayoutManager);
        recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(0),true));
        recycleView.setAdapter(goodsRecycleAdapter);
        goodsRecycleAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                //跳转到商品详情页
                currenPosition = position;
                Intent intent=new Intent(getActivity(), GoodsDetailActivity.class);
                intent.putExtra(Constants.Fields.PRODUCT_ID, goodsRecycleAdapter.getItem(position).getProductInfo().getProductId());
                intent.putExtra("userType", userType);
                intent.putExtra("sellerBaseUser", mActivity.baseUser);
                intent.putParcelableArrayListExtra("shopChartList", (ArrayList<? extends Parcelable>) mActivity.getShopChartList());
                intent.putExtra("productNum",mActivity.getProductNum());
                intent.putExtra("totalPrice",mActivity.getTotalPrice());
                intent.putExtra("storeDetail",mActivity.getStoreDetail());
                intent.putExtra("isSchoolmate",isSchoolmate);
                intent.putExtra("isReportMax",isReportMax);
                intent.putExtra("isAuth",isAuth);
                mActivity.startActivityForResult(intent,1);
            }
        });

        goodsRecycleAdapter.setOperationClick(new GoodsRecycleAdapter.OperationClick() {
            @Override
            public void operationClick(long productId, int position) {
                //管理商品
                currenPosition=position;
            }
        });
        emptyView.setVisibility(View.GONE);

        goodsRecycleAdapter.setType(userType);

        if(userType==1){
            initPopWindow();
        }
    }





    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            if (pageNumber == 1) {
                if(mActivity!=null){
                    mActivity.finishRefresh(false);
//                    refreshLayout.finishRefresh(false);
                }
            } else {
                if(mActivity!=null) {
                    mActivity.finishLoadMore(false);
//                    refreshLayout.finishLoadMore(false);
                }
            }
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_GETSTOREPRODUCTLIST)) {
            if (object instanceof GetStoreProductListResponse) {
                GetStoreProductListResponse getStoreProductListResponse = (GetStoreProductListResponse) object;
                if (pageNumber == 1) {
                    if(mActivity!=null){
                        mActivity.finishRefresh(true);
//                        refreshLayout.finishRefresh();
                    }
                } else {
                    if(mActivity!=null) {
                        mActivity.finishLoadMore(true);
//                        refreshLayout.finishLoadMore();
                    }
                }
                if (getStoreProductListResponse.getCode() == 200) {
                    List<ProductDetail> list = getStoreProductListResponse.getList();
                    if(list!=null){
                        if(pageNumber==1){
                            goodsRecycleAdapter.setList(list);
                            //如果购物车有数据 要清空购物车
                            if(mActivity.getShoppingCartData()){
                                mActivity.shopCartPopWindow.clearList();
                            }
                        }else{
                            goodsRecycleAdapter.addList(list);
                        }
                    }
                    if(getStoreProductListResponse.getTotal()<=pageNumber*pageSize){
                        //没有更多数据时  下拉刷新不可用
                        mActivity.setEnableLoadMore(false,0);
//                        refreshLayout.setEnableLoadMore(false);
                    }else
                    {
                        //有更多数据时  下拉刷新才可用
                        mActivity.setEnableLoadMore(true,0);
//                        refreshLayout.setEnableLoadMore(true);
                        pageNumber++;
                    }

                    if (getStoreProductListResponse.getTotal() == 0) {
                            emptyView.setVisibility(View.VISIBLE);
                            ((TextView)emptyView.findViewById(R.id.empty_textView)).setText(getStoreProductListResponse.getBlankHint());
                        } else {
                            emptyView.setVisibility(View.GONE);
                        }
                } else {
                    Utils.showToastShortTime(getStoreProductListResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_UPDATEPRODUCTSTATUS)) {
            if (object instanceof BaseResponse) {
                BaseResponse baseResponse = (BaseResponse) object;
                if (baseResponse.getCode() == 200) {
                   if(status==1)
                   {
                       ProductDetail productDetail=goodsRecycleAdapter.getItem(currenPosition);
                       productDetail.getProductInfo().setIsHot(0);
                       productDetail.getProductInfo().setIsNew(0);
                       productDetail.getProductInfo().setStatus(1);
                       goodsRecycleAdapter.notifyItemChanged(currenPosition);
                   }else{
                       ProductDetail productDetail=goodsRecycleAdapter.getItem(currenPosition);
                       productDetail.getProductInfo().setIsHot(isHot);
                       productDetail.getProductInfo().setIsNew(isNew);
                       goodsRecycleAdapter.notifyItemChanged(currenPosition);
                   }
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        }
    }

    private void initPopWindow() {
        operationList=new ArrayList<String>();
        operationList.add("编辑商品");
        operationList.add("店长热推");
        operationList.add("新品上架");
        operationList.add("商品下架");
        customOperationPopWindow = new CustomOperationPopWindow(mActivity);
        customOperationPopWindow.setDataSource(operationList,-1,0);

        customOperationPopWindow.setOnItemMyListener(new CustomOperationPopWindow.OnItemListener() {
            @Override
            public void OnItemListener(int position, String typeSelect) {
                //此处实现列表点击所要进行的操作
                final ProductDetail productDetail=goodsRecycleAdapter.getItem(currenPosition);
                switch (position) {
                    case 0:
                        //编辑商品
                        Intent intent = new Intent(mActivity, AddGoodsActivity.class);
                        intent.putExtra("productDetail",productDetail);
                        intent.putExtra("storeId",storeId);
                        startActivity(intent);
                        break;
                    case 1:
                        if (typeSelect.equals("店长热推")) {
                            //店长热推
                            isHot=1;
                        } else if (typeSelect.equals("取消热推")) {
                            //店长热推
                            isHot=0;
                        }
                        getStoreListPresenter().updateProductStatus(userId, storeId,productDetail.getProductInfo().getProductId(), isHot, 0, 0);
                        break;
                    case 2:
                        if (typeSelect.equals("新品上架")) {
                            //新品上架
                            isNew=1;
                        } else if (typeSelect.equals("取消新品推荐")) {
                            //店长热推
                            isNew=0;
                        }
                        getStoreListPresenter().updateProductStatus(userId,storeId,productDetail.getProductInfo().getProductId(),0,isNew,0);

                        break;
                    case 3:
                        //商品下架
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "下架后用户将不能购买此商品", "取消", "确定");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                            }

                            @Override
                            public void onRightClick() {
                                status=1;
                                getStoreListPresenter().updateProductStatus(userId,storeId,productDetail.getProductInfo().getProductId(),0,0,status);
                            }
                        });
                        dialog.show(mActivity.getSupportFragmentManager());
                        break;
                }
            }
        });
        customOperationPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                params.alpha=1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateShopChartListEvent event) {
        if (event == null) {
            return;
        }
        if(event.getType()==2){
            //更新商品列表
            goodsRecycleAdapter.refreshList(event.getProductDetail());
            goodsRecycleAdapter.setTotal(event.getTotal());
            goodsRecycleAdapter.setTotalMoney(event.getTotalMoney());
        }else if(event.getType()==3){
            //清空购物车
            goodsRecycleAdapter.emptyNumList();
            goodsRecycleAdapter.setTotal(0);
            goodsRecycleAdapter.setTotalMoney(new BigDecimal(0));
        }else if(event.getType()==4){
            //从商品详情更新商品列表
            goodsRecycleAdapter.refreshList(event.getProductDetail());
            goodsRecycleAdapter.setTotal(event.getTotal());
            goodsRecycleAdapter.setTotalMoney(event.getTotalMoney());
        }

    }

    public void emptyList(){
        //更新商品列表
        goodsRecycleAdapter.emptyNumList();
        goodsRecycleAdapter.setTotal(0);
        goodsRecycleAdapter.setTotalMoney(new BigDecimal(0));
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateGoodsListEvent event) {
        if (event == null) {
            return;
        }
        if(event.getType()==1){
            //编辑商品
            ProductDetail productDetail=goodsRecycleAdapter.getItem(currenPosition);
            productDetail.setImageList(event.getProductImageList());
            ProductInfo productInfo=productDetail.getProductInfo();
            productInfo.setProductName(event.getProductName());
            productInfo.setProductDesc(event.getProductDesc());
            productInfo.setProductPrice(event.getProductPrice());
            productInfo.setProductDiscount(event.getProductDiscount());
            productInfo.setSalePrice(event.getSalePrice());
            productInfo.setProductRemainTotal(Integer.parseInt(event.getProductRemainTotal()));
            productInfo.setStatus(0);
            goodsRecycleAdapter.notifyItemChanged(currenPosition);
        }else if(event.getType()==2){
            //添加新商品
            ProductDetail productDetail=new ProductDetail();
            productDetail.setImageList(event.getProductImageList());
            ProductInfo productInfo=new ProductInfo();
            productInfo.setProductId(event.getProductId());
            productInfo.setProductName(event.getProductName());
            productInfo.setProductDesc(event.getProductDesc());
            productInfo.setProductPrice(event.getProductPrice());
            productInfo.setProductDiscount(event.getProductDiscount());
            productInfo.setSalePrice(event.getSalePrice());
            productInfo.setProductRemainTotal(Integer.parseInt(event.getProductRemainTotal()));
            productDetail.setProductInfo(productInfo);
            goodsRecycleAdapter.getmList().add(0,productDetail);
            goodsRecycleAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
