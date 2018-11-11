package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindProductDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.PostDetailImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.HomeBannerFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.ShopCartPopWindow;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

import static android.view.View.VISIBLE;

/**
 * Created by Hu_PC on 2017/11/9.
 * 商品详情页
 */

public class GoodsDetailActivity extends BaseActivity implements BaseView {
    private GetStoreListPresenter presenter;
    private TitleFragment titleFragment;
    private long userId, productId;
    private ProductDetail productDetail;
    private HomeBannerFragment homeBannerFragment;
    private TextView productNameTextView,salePriceTextView,productPriceTextView,productDiscountTextView,productDescTextView,tv_num,productRemaintotalTextView,tip_view,saledTotalTextView;
    private ViewStub viewStubBuyer;
    private int userType = 3;//(1店主,2店员,3普通用户4店员申请中)
    private ImageView plusImg,reduceImg;
    private ShopCartPopWindow shopCartPopWindow;
    private BaseUser sellerBaseUser;//卖家
    private List<ProductDetail> shopChartList;
    private int productNum;
    private BigDecimal totalPrice;
    private TextView shoppingCartTextView,shopChartStatusTextView;
    private StoreDetail storeDetail;
    private String floorPrice;//起送价格
    private TextView settlementTextView;
    private int isSchoolmate;//是否是校友(1否2是),
    private int isReportMax;//本月被举报次数是否超过20次, 0:正常1：超过
    private int isAuth;//开店申请状态(1等待审核  2 审核通过 3审核拒绝)
    private NestedScrollView nestedScrollView;
    private RecyclerView imageDetailRecycleView;
    private PostDetailImageRecycleAdapter postDetailImageRecycleAdapter;
    private String postage;//订单要加进去的邮费
    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        EventBus.getDefault().register(this);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        presenter = new GetStoreListPresenter(this);
        Intent intent=getIntent();
        storeDetail=intent.getParcelableExtra("storeDetail");
        productId = intent.getLongExtra(Constants.Fields.PRODUCT_ID, 0);
        if(storeDetail!=null){
            userType = intent.getIntExtra("userType", 0);
            sellerBaseUser = intent.getParcelableExtra("sellerBaseUser");
            shopChartList=intent.getParcelableArrayListExtra("shopChartList");
            productNum=intent.getIntExtra("productNum",0);
            totalPrice= (BigDecimal) intent.getSerializableExtra("totalPrice");
            isSchoolmate= intent.getIntExtra("isSchoolmate", 0);
            isReportMax= intent.getIntExtra("isReportMax", 0);
            isAuth= intent.getIntExtra("isAuth", 0);
            floorPrice=storeDetail.getStoreInfo().getFloorPrice();
            if(floorPrice==null||StringUtils.isEmpty(floorPrice)){
                floorPrice="0";
            }
            setTitle();
            initView();
            presenter.findProductDetail(userId, productId);
        }else{
            storeId=intent.getLongExtra(Constants.Fields.STORE_ID, 0);
            presenter.findStoreDetail(userId,storeId);
        }



    }

    private void initView() {
        nestedScrollView=findViewById(R.id.nested_scroll_view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("scrollY",scrollY+"");
                titleFragment.setAlpha(scrollY,600);
            }
        });
        homeBannerFragment = (HomeBannerFragment) getSupportFragmentManager().findFragmentById(R.id.banner_frame);
        productNameTextView=findViewById(R.id.goods_name_view);
        salePriceTextView=findViewById(R.id.sale_price_text_view);
        productPriceTextView=findViewById(R.id.product_price_text_view);
        productPriceTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
        productDiscountTextView=findViewById(R.id.product_discount_text_view);
        productDescTextView=findViewById(R.id.goods_describe_view);
        tv_num = findViewById(R.id.num_text_view);
        plusImg= findViewById(R.id.plus_img);
        reduceImg= findViewById(R.id.reduce_img);
        productRemaintotalTextView= findViewById(R.id.product_remain_total_text_view);
        tip_view= findViewById(R.id.tip_text);
        saledTotalTextView= findViewById(R.id.buyer_num_text_view);
        //商品详情ui
        imageDetailRecycleView = (RecyclerView) findViewById(R.id.recycler_image_detail);
        imageDetailRecycleView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        imageDetailRecycleView.setLayoutManager(linearLayoutManager);
        imageDetailRecycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10)));
        postDetailImageRecycleAdapter = new PostDetailImageRecycleAdapter(this);
        imageDetailRecycleView.setAdapter(postDetailImageRecycleAdapter);
        postDetailImageRecycleAdapter.setImageViewPageDialogClick(new PostDetailImageRecycleAdapter.ImageViewPageDialogClick() {
            @Override
            public void onClick(List<Image> mList, int currentItem) {
                ImageViewPageDialog.newInstance(mList, currentItem).show(getSupportFragmentManager());
            }
        });
    }

    public void initBuyerBottomLayout() {
        viewStubBuyer = findViewById(R.id.buyer_operation_layout);
        if (viewStubBuyer != null) {
            viewStubBuyer.inflate();
            final RelativeLayout shopCartLayout = findViewById(R.id.shopping_cart_layout);
            shoppingCartTextView=findViewById(R.id.shopping_cart_text_view);
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
                    if(!shoppingCartTextView.getText().equals("未选购商品")) {
                        //弹出购物车列表界面
                        if (shopCartPopWindow.isShowing()) {
                            shopCartPopWindow.dismiss();
                        } else {
                            shopCartPopWindow.showPopupWindow(shopCartLayout);
                        }
                    }
                }
            });
            TextView shopTextView = findViewById(R.id.shop_text_view);
            shopTextView.setVisibility(View.VISIBLE);
            shopTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("私信");
                    if(storeId>0){
                        Intent intent = new Intent(GoodsDetailActivity.this, ShopDetailActivity.class);
                        intent.putExtra(Constants.Fields.STORE_ID,storeId);
                        startActivity(intent);
                    }else
                    {
                        finish();
                    }
                }
            });
            TextView messageTextView = findViewById(R.id.message_text_view);
            messageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("私信");
                    if (sellerBaseUser != null) {
                        RongIMutils.startToConversation(GoodsDetailActivity.this, sellerBaseUser.getUserId() + "", sellerBaseUser.getNickName());

                    }
                }
            });
            TextView videoTextView = findViewById(R.id.call_text_view);
            videoTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("视频");
                    if (sellerBaseUser != null) {
                        Intent intent = new Intent(GoodsDetailActivity.this, VideoChatActivity.class);
                        intent.putExtra(Constants.Fields.BASEUSER, sellerBaseUser);
                        intent.putExtra(Constants.Fields.TYPE, 5);
                        intent.putExtra(Constants.Fields.ROOMNUMBER, Utils.getRoomNumber());
                        startActivity(intent);
                    }
                }
            });
            settlementTextView = findViewById(R.id.settlement_text_view);
            settlementTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Utils.showToastShortTime("结算");
                    Intent intent = new Intent(GoodsDetailActivity.this, WXPayEntryActivity.class);
                    intent.putExtra("storeDetail",storeDetail);
                    intent.putParcelableArrayListExtra("productDetailList", (ArrayList<? extends Parcelable>) shopCartPopWindow.getDateSource());
                    intent.putExtra("productNum",productNum);
                    intent.putExtra("totalPrice",totalPrice);
                    intent.putExtra("postage", postage);
                    intent.putExtra(Constants.Fields.STORE_ID, storeDetail.getStoreInfo().getStoreId());
                    intent.putExtra(Constants.Fields.TYPE, 2);
                    startActivity(intent);
                }
            });
        }

        shopCartPopWindow = new ShopCartPopWindow(GoodsDetailActivity.this);
        //设置购物车数据
        shopCartPopWindow.setTotal(productNum,totalPrice);
        shopCartPopWindow.setDataSource(shopChartList);

        //设置底部购物车数据
        shoppingCartTextView.setText(totalPrice+"("+productNum+")");
        BigDecimal floorPriceBig=new BigDecimal(floorPrice);
        if(totalPrice.compareTo(floorPriceBig)==1||totalPrice.compareTo(floorPriceBig)==0){//小于 时，返回 -1   等于 时，返回 0   大于 时，返回 1
            settlementTextView.setText("结算");
            settlementTextView.setEnabled(true);
        }else{
            settlementTextView.setText("还差¥"+floorPriceBig.subtract(totalPrice)+"起送");
            settlementTextView.setEnabled(false);
        }
        if(totalPrice.compareTo(new BigDecimal(0))==-1||totalPrice.compareTo(new BigDecimal(0))==0){
            shoppingCartTextView.setText("未选购商品");
            settlementTextView.setText("¥"+floorPrice+"起送");
            settlementTextView.setEnabled(false);
        }else{
            String cartStr="¥"+totalPrice+"("+productNum+")";
            SpannableStringBuilder style = new SpannableStringBuilder();
            style.append(cartStr);
            style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffb700)), 0, cartStr.indexOf("("), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            shoppingCartTextView.setText(style);
        }

        //设置商品的购物数量
        final ProductInfo productInfo=productDetail.getProductInfo();
        if(shopChartList!=null){
            for(int i=0;i<shopChartList.size();i++){
                ProductDetail productdetail=shopChartList.get(i);
                if(productdetail.getProductInfo().getProductId()==productInfo.getProductId()){
                    productInfo.setProductNum(productdetail.getProductInfo().getProductNum());
                }
            }
        }
        plusImg.setVisibility(VISIBLE);
        final int status=productInfo.getStatus();
        if(status==1){
            plusImg.setBackgroundResource(R.mipmap.goods_plus_unenable_icon);
        }else{
            plusImg.setBackgroundResource(R.mipmap.goods_plus_icon);
        }
        tv_num.setText(productInfo.getProductNum()+"");
        if(productInfo.getProductNum()>0){
            tv_num.setVisibility(VISIBLE);
            reduceImg.setVisibility(VISIBLE);
        }else{
            tv_num.setVisibility(View.INVISIBLE);
            reduceImg.setVisibility(View.INVISIBLE);
        }

        plusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status==1){
                    return;
                }else if(userType==2){
                    Utils.showToastShortTime("店员不能购买商品");
                    return;
                }else if(isSchoolmate==1){
                    Utils.showToastShortTime("仅校友可购买商品");
                    return;
                }else if(isReportMax==1){
                    Utils.showToastShortTime("该商铺暂停售卖");
                    return;
                }else if(isAuth==1||isAuth==3){
                    Utils.showToastShortTime("该店正在装修，请稍后再来！");
                    return;
                }else {
                    //加
                    String numStr = tv_num.getText().toString();
                    if (StringUtils.isEmpty(numStr)) {
                        tv_num.setText(1 + "");
                    } else {
                        int num = Integer.parseInt(numStr);
                        if (num < productInfo.getProductRemainTotal()) {
                            tv_num.setText((num + 1) + "");
                        } else {
                            Utils.showToastShortTime("当前商品已售清");
                            return;
                        }
                    }
                    productNum++;
                    reduceImg.setVisibility(VISIBLE);
                    tv_num.setVisibility(VISIBLE);
                    productInfo.setProductNum(Integer.parseInt(tv_num.getText().toString()));
                    totalPrice = totalPrice.add(productInfo.getSalePrice());
                    EventBus.getDefault().post(new UpdateShopChartListEvent(1, productDetail, productNum, totalPrice));
                    //跟新店铺详情中商品列表
                    EventBus.getDefault().post(new UpdateShopChartListEvent(4, productDetail, productNum, totalPrice));
                }
            }
        });
        reduceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //减
                String reduceStr=tv_num.getText().toString();
                int num= Integer.parseInt(reduceStr);
                if(num<=1){
                    reduceImg.setVisibility(View.INVISIBLE);
                    tv_num.setText("0");
                    tv_num.setVisibility(View.INVISIBLE);
                }else{
                    reduceImg.setVisibility(VISIBLE);
                    tv_num.setText((num-1)+"");
                }
                productNum--;
                productInfo.setProductNum(Integer.parseInt(tv_num.getText().toString()));
                totalPrice=totalPrice.subtract(productInfo.getSalePrice());
                EventBus.getDefault().post(new UpdateShopChartListEvent(1,productDetail,productNum,totalPrice));
                //跟新店铺详情中商品列表
                EventBus.getDefault().post(new UpdateShopChartListEvent(4,productDetail,productNum,totalPrice));

            }
        });
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
                }else
                {
                    shopChartStatusTextView.setText("");
                    shopChartStatusTextView.setVisibility(View.GONE);
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
                    shopChartStatusTextView.setText("(本校包邮）");
                }else{
                    shopChartStatusTextView.setText("(配送费:"+storeInfo.getPostage()+")");
                    postage=storeInfo.getPostage();
                }
            }
        }

    }

    public void setProductDetail(ProductDetail productDetail) {
        if (productDetail != null) {
            List<ProductImage> productImageList=productDetail.getImageList();
            homeBannerFragment.setImageListDataNoScroll(productDetail.getImageList());
            List<ProductImage> productDetailImageList=productDetail.getDetaiLimageList();
            List<Image> detailImageList = new ArrayList<Image>();
            if(productDetailImageList!=null){
                for (int i=0;i<productDetailImageList.size();i++){
                    ProductImage productImage=productDetailImageList.get(i);
                    Image image=new Image();
                    image.setImageUrl(productImage.getImageUrl());
                    image.setImageId(productImage.getImageId());
                    image.setThumbnailUrl(productImage.getThumbnailUrl());
                    detailImageList.add(image);
                }
                postDetailImageRecycleAdapter.setList(detailImageList);
            }
            ProductInfo productInfo=productDetail.getProductInfo();
            if(productInfo!=null)
            {
                productNameTextView.setText(productInfo.getProductName());
                SpannableStringBuilder style = new SpannableStringBuilder();
                if(!StringUtils.isEmpty(productInfo.getProductDesc())){
                    style.append("商品描述："+productInfo.getProductDesc());
                    style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_333333)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    productDescTextView.setText(style);
                    productDescTextView.setVisibility(VISIBLE);
                }else
                {
                    productDescTextView.setVisibility(View.GONE);
                }

                BigDecimal salePrice=productInfo.getSalePrice();
                BigDecimal productPrice=productInfo.getProductPrice();
                salePriceTextView.setText(salePrice+"");
                if(salePrice.equals(productPrice)){
                    productPriceTextView.setVisibility(View.GONE);
                    productDiscountTextView.setVisibility(View.GONE);
                }else{
                    productPriceTextView.setText("¥ "+productPrice);
                    productPriceTextView.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    productDiscountTextView.setText(productInfo.getProductDiscount()+"折");
                    productPriceTextView.setVisibility(VISIBLE);
                    productDiscountTextView.setVisibility(VISIBLE);
                }
                productRemaintotalTextView.setText("库存："+productInfo.getProductRemainTotal());
                saledTotalTextView.setText(productInfo.getSaledTotal()+"人付款");
                int status=productInfo.getStatus();
                if (userType!=1) {
                    initBuyerBottomLayout();
                    if(status==1){
                        //买家看到的下架商品
                       tip_view.setVisibility(VISIBLE);
                       RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tip_view.getLayoutParams();
                       layoutParams.setMargins(0,0,0,Utils.dp2px(49));
                       RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams)  nestedScrollView.getLayoutParams();
                       layoutParams1.setMargins(0,0,0,Utils.dp2px(49+25));
                    }else{
                        //买家看到的正常商品
                        tip_view.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tip_view.getLayoutParams();
                        layoutParams.setMargins(0,0,0,Utils.dp2px(0));
                        RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams)  nestedScrollView.getLayoutParams();
                        layoutParams1.setMargins(0,0,0,Utils.dp2px(49));
                    }
                }else{
                    if(status==1){
                        //卖家看到的下架商品
                        tip_view.setVisibility(VISIBLE);
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tip_view.getLayoutParams();
                        layoutParams.setMargins(0,0,0,Utils.dp2px(0));
                        RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams)  nestedScrollView.getLayoutParams();
                        layoutParams1.setMargins(0,0,0,Utils.dp2px(25));
                    }else{
                        //卖家看到的正常商品
                        tip_view.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) tip_view.getLayoutParams();
                        layoutParams.setMargins(0,0,0,Utils.dp2px(0));
                        RelativeLayout.LayoutParams layoutParams1= (RelativeLayout.LayoutParams)  nestedScrollView.getLayoutParams();
                        layoutParams1.setMargins(0,0,0,Utils.dp2px(0));
                    }
                }
            }

        }
    }


    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_goods_detail);
        titleFragment.setBackGround(R.color.color_00000000);
        titleFragment.setLeftImageViewVisible(View.VISIBLE);
        titleFragment.setRightImageViewVisible(View.VISIBLE);
        titleFragment.setLeftImageView(R.mipmap.shop_detail_back_icon,  new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                if(userType!=1){
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("shopChartList", (ArrayList<? extends Parcelable>) shopCartPopWindow.getDateSource());
                    intent.putExtra("productNum",productNum);
                    intent.putExtra("totalPrice",totalPrice);
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        });
        if(isAuth!=0){
            //从订单详情进入没有分享按钮
            titleFragment.setRightImageView(R.mipmap.shop_detail_share_icon, new TitleFragment.RightViewClick() {
                @Override
                public void onClick() {
                    if(storeDetail==null){
                        Utils.showToastShortTime("获取店铺信息失败");
                        return;
                    }else{
                        if(storeDetail.getStoreInfo()==null) {
                            Utils.showToastShortTime("获取店铺信息失败");
                            return;
                        }else{
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
                            ShareUtil.getInstance().showShare(GoodsDetailActivity.this, ShareUtil.STOREDETAIL, storeDetail.getStoreInfo().getStoreId(), storeDetail, "", "这家小店不错哦，快来转转！", storeDetail.getStoreInfo().getTitle(), imgUrl, false);

                        }
                    }

                }
            });
        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_FINDPRODUCTDETAIL)) {
            if (object instanceof FindProductDetailResponse) {
                FindProductDetailResponse findProductDetailResponse = (FindProductDetailResponse) object;
                if (findProductDetailResponse.getCode() == 200) {
                    productDetail = findProductDetailResponse.getProductDetail();
                        if (productDetail != null) {
                            setProductDetail(productDetail);
                        }
                } else {
                    Utils.showToastShortTime(findProductDetailResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        userType = storeDetail.getIsClert();
                        sellerBaseUser = storeDetail.getStoreInfo().getBaseUser();
                        shopChartList=new ArrayList<ProductDetail>();
                        totalPrice= new BigDecimal(0);
                        isSchoolmate= storeDetail.getIsSchoolmate();
                        isReportMax= storeDetail.getIsReportMax();
                        isAuth= storeDetail.getIsAuth();
                        floorPrice=storeDetail.getStoreInfo().getFloorPrice();
                        if(floorPrice==null||StringUtils.isEmpty(floorPrice)){
                            floorPrice="0";
                        }
                        setTitle();
                        initView();
                        presenter.findProductDetail(userId, productId);
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                    finish();
                }
            }
        }
    }

    //接收充值成功消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ClosePayActivityEvent event) {
        if (event == null) {
            return;
        }
        finish();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallback(final UpdateShopChartListEvent event) {
        if (event == null) {
            return;
        }
        if(event.getType()==1){
            //更新购物车列表
            shopCartPopWindow.addProductDetail(event.getProductDetail(),event.getTotal(),event.getTotalMoney());
            BigDecimal floorPriceBig=new BigDecimal(floorPrice);
            BigDecimal totalMoneyBig=event.getTotalMoney();
            if(totalMoneyBig.compareTo(floorPriceBig)==1||totalMoneyBig.compareTo(floorPriceBig)==0){//小于 时，返回 -1   等于 时，返回 0   大于 时，返回 1
                settlementTextView.setText("结算");
                settlementTextView.setEnabled(true);
            }else{
                settlementTextView.setText("还差¥"+floorPriceBig.subtract(totalMoneyBig)+"起送");
                settlementTextView.setEnabled(false);
            }
            if(event.getTotal()<=0){
                shoppingCartTextView.setText("未选购商品");
                settlementTextView.setText("¥"+floorPrice+"起送");
                settlementTextView.setEnabled(false);
            }else{
                String cartStr="¥"+event.getTotalMoney().stripTrailingZeros().toPlainString()+"("+event.getTotal()+")";
                SpannableStringBuilder style = new SpannableStringBuilder();
                style.append(cartStr);
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffb700)), 0, cartStr.indexOf("("), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                shoppingCartTextView.setText(style);
            }
        }else if(event.getType()==2){
             //更新商品
             ProductDetail productdetail=event.getProductDetail();
             int num=productdetail.getProductInfo().getProductNum();
             if(productdetail.getProductInfo().getProductId()==productdetail.getProductInfo().getProductId()){
                 productdetail.getProductInfo().setProductNum(num);
                 tv_num.setText(num+"");
                 if(num>0){
                     tv_num.setVisibility(VISIBLE);
                     reduceImg.setVisibility(VISIBLE);
                 }else{
                     tv_num.setVisibility(View.INVISIBLE);
                     reduceImg.setVisibility(View.INVISIBLE);
                 }

             }
            BigDecimal floorPriceBig=new BigDecimal(floorPrice);
            BigDecimal totalMoneyBig=event.getTotalMoney();
            if(totalMoneyBig.compareTo(floorPriceBig)==1||totalMoneyBig.compareTo(floorPriceBig)==0){//小于 时，返回 -1   等于 时，返回 0   大于 时，返回 1
                settlementTextView.setText("结算");
                settlementTextView.setEnabled(true);
            }else{
                settlementTextView.setText("还差¥"+floorPriceBig.subtract(totalMoneyBig)+"起送");
                settlementTextView.setEnabled(false);
            }
            if(event.getTotal()<=0){
                shoppingCartTextView.setText("未选购商品");
                settlementTextView.setText("¥"+floorPrice+"起送");
                settlementTextView.setEnabled(false);
            }else{
                String cartStr="¥"+event.getTotalMoney().stripTrailingZeros().toPlainString()+"("+event.getTotal()+")";
                SpannableStringBuilder style = new SpannableStringBuilder();
                style.append(cartStr);
                style.setSpan(new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffb700)), 0, cartStr.indexOf("("), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                shoppingCartTextView.setText(style);
            }
        }else if(event.getType()==3){
            //清空购物车
            shoppingCartTextView.setText("未选购商品");
            settlementTextView.setText("¥"+floorPrice+"起送");
            settlementTextView.setEnabled(false);
            productDetail.getProductInfo().setProductNum(0);
            tv_num.setText(0+"");
            tv_num.setVisibility(View.INVISIBLE);
            reduceImg.setVisibility(View.INVISIBLE);
        }
        productNum=event.getTotal();
        totalPrice=event.getTotalMoney();

    }

    @Override
    public void showLoading() {
            ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(userType!=1){
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("shopChartList", (ArrayList<? extends Parcelable>) shopCartPopWindow.getDateSource());
                intent.putExtra("productNum",productNum);
                intent.putExtra("totalPrice",totalPrice);
                setResult(Activity.RESULT_OK, intent);
            }
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
