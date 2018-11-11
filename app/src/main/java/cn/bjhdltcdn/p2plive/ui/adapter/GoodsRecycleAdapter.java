package cn.bjhdltcdn.p2plive.ui.adapter;

import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.ui.fragment.GoodsRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by huwenhua on 2018/4/17.
 */

public class GoodsRecycleAdapter extends BaseRecyclerAdapter {

    private final int TYPE_SELLER = 1, TYPE_BUYER = 3,TYPE_CLERT=2,TYPE_CLERT_APPLY=4;
    private List<ProductDetail> mList = new ArrayList<>();
    private int type;
    private int total;
    public TextView operationTextView;
    private GoodsRecycleFragment activity;
    private BigDecimal totalMoney=new BigDecimal(0);
    private int isSchoolmate;//是否是校友(1否2是),
    private int isReportMax;//本月被举报次数是否超过20次, 0:正常1：超过
    private int isAuth;//开店申请状态(1等待审核  2 审核通过 3审核拒绝)
    private boolean refresh;//整体刷新的时候是否刷新图片

    public GoodsRecycleAdapter(GoodsRecycleFragment activity) {
        this.activity=activity;
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

    public void setList(List<ProductDetail> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<ProductDetail> getmList() {
        return mList;
    }

    public void addList(List<ProductDetail> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size()-list.size(),list.size());
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public ProductDetail getItem(int position){
        if(mList!=null)
        {
            return mList.get(position);
        }else{
            return null;
        }
    }

    public void refreshList(ProductDetail productdetail){
      if(mList!=null){
          if(productdetail!=null) {
              int pos=0;
              for (int i = 0; i < mList.size(); i++) {
                  ProductDetail productDetail1 = mList.get(i);
                  ProductInfo productInfo = productDetail1.getProductInfo();
                  if (productdetail.getProductInfo().getProductId() == productDetail1.getProductInfo().getProductId()) {
                      productInfo.setProductNum(productdetail.getProductInfo().getProductNum());
                      mList.remove(i);
                      mList.add(i, productDetail1);
                      pos=i;
                  }
              }
//              notifyItemChanged(pos);
              notifyItemChanged(pos,true);
          }
      }
    }



    public void emptyNumList(){
        if(mList!=null)
        {
            for(int i=0;i<mList.size();i++){
                ProductDetail productDetail=mList.get(i);
                productDetail.getProductInfo().setProductNum(0);
            }
//            notifyDataSetChanged();
            notifyItemRangeChanged(0,mList.size(),true);
        }
    }

    //更新商品列表库存数量
//    public void updateProductRemainTotalList(ProductOrder productOrder){
//        List<ProductInfo> productList=productOrder.getProductList();
//        if(mList!=null)
//        {
//            for(int i=0;i<productList.size();i++){
//                ProductInfo productInfo=productList.get(i);
//                for(int j=0;j<mList.size();j++){
//                    ProductDetail productDetail=mList.get(j);
//                    ProductInfo productInfo1=productDetail.getProductInfo();
//                    if(productInfo.getProductId()==productInfo1.getProductId())
//                    {
//                        productInfo1.setProductRemainTotal(productInfo1.getProductRemainTotal()-productInfo.getProductNum());
//                    }
//                    productDetail.setProductInfo(productInfo1);
//                    if(productInfo1.getProductRemainTotal()<=0){
//                        mList.remove(j);
//                    }else
//                    {
//                        mList.set(j,productDetail);
//                    }
//                }
//            }
//            notifyDataSetChanged();
//        }
//    }




    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemViewType(int position) {
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        BaseViewHolder viewHolder = null;
        if (itemType == TYPE_BUYER||itemType == TYPE_CLERT||itemType ==TYPE_CLERT_APPLY) {
            viewHolder = new BuyerViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_goods_buyer_recycle_item, null));
        } else if (itemType == TYPE_SELLER) {
            viewHolder = new SellerViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_goods_recycle_item, null));
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position,@NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (getItemViewType(position) == TYPE_SELLER) {
            final GoodsRecycleAdapter.SellerViewHolder viewHolder = (GoodsRecycleAdapter.SellerViewHolder) holder;
            ProductDetail productDetail= mList.get(position);
            if(productDetail!=null){
                final ProductInfo productInfo=productDetail.getProductInfo();
                if(productInfo==null){
                    return;
                }
                List<ProductImage> productImageList=productDetail.getImageList();
                if (payloads == null||payloads.isEmpty()) {
                    if(productImageList!=null&&productImageList.size()>0){
//                    if(!productImageList.get(0).getThumbnailUrl().equals(viewHolder.goodsImg.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                        viewHolder.goodsImg.setTag(null);//需要清空tag，否则报错
                        Utils.CornerImageViewDisplayByUrl(productImageList.get(0).getThumbnailUrl(),viewHolder.goodsImg,9);
//                        viewHolder.goodsImg.setTag(productImageList.get(0).getThumbnailUrl());
//                    }
                    }else{
                        RequestOptions options = new RequestOptions().centerCrop().transform(new GlideRoundTransform(9)).error(R.mipmap.goods_no_image);
                        Glide.with(App.getInstance()).asBitmap().load("").apply(options).into(viewHolder.goodsImg);
                    }
                }


                  final int isHot=productInfo.getIsHot();
                  final int isNew=productInfo.getIsNew();
                  final int status=productInfo.getStatus();
                  viewHolder.hotImg.setVisibility(View.GONE);
                  if(isHot==1){
                      viewHolder.hotImg.setImageResource(R.mipmap.goods_hot_icon);
                      viewHolder.hotImg.setVisibility(View.VISIBLE);
                  }
                  if(isNew==1){
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_new_icon);
                      viewHolder.hotImg.setVisibility(View.VISIBLE);
                  }
                  if(status==1){
                      //已下架商品
                      viewHolder.hotImg.setImageResource(R.mipmap.goods_unenable_icon);
                      viewHolder.hotImg.setVisibility(View.VISIBLE);
                  }
                viewHolder.tv_goods_name.setText(productInfo.getProductName());
                viewHolder.tv_product_remain_total.setText("库存："+productInfo.getProductRemainTotal());
                BigDecimal salePrice=productInfo.getSalePrice();
                BigDecimal productPrice=productInfo.getProductPrice();
                viewHolder.tv_sale_price.setText(salePrice+"");
                if(salePrice.equals(productPrice)){
                    viewHolder.tv_product_price.setVisibility(View.GONE);
                    viewHolder.tv_product_discount.setVisibility(View.GONE);
//                    viewHolder.product_price_text.setVisibility(View.GONE);
                }else{
                    viewHolder.tv_product_price .setText("¥ "+productPrice);
                    viewHolder.tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    viewHolder.tv_product_discount.setText(productInfo.getProductDiscount()+"折");
                    viewHolder.tv_product_price.setVisibility(View.VISIBLE);
                    viewHolder.tv_product_discount.setVisibility(View.VISIBLE);
//                    viewHolder.product_price_text.setVisibility(View.VISIBLE);
                }


//            operationTextView=LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_goods_recycle_item, null).findViewById(R.id.operation_text_view);
            viewHolder.tv_operation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int unEnablePosition=-1;
                        if(status!=1){
                            if(isHot==1){
                                activity.operationList.remove(1);
                                activity.operationList.add(1,"取消热推");
                                unEnablePosition=2;
                            }else{
                                activity.operationList.remove(1);
                                activity.operationList.add(1,"店长热推");
                            }
                            if(isNew==1){
                                activity.operationList.remove(2);
                                activity.operationList.add(2,"取消新品推荐");
                                unEnablePosition=1;
                            }else{
                                activity.operationList.remove(2);
                                activity.operationList.add(2,"新品上架");
                            }
                        }
                        activity.customOperationPopWindow.setDataSource(activity.operationList,unEnablePosition,status);
                        //弹出管理弹框
                        activity.customOperationPopWindow.showPopupWindow(viewHolder.tv_operation);
                        WindowManager.LayoutParams params=activity.getActivity().getWindow().getAttributes();
                        params.alpha=0.7f;
                        activity.getActivity().getWindow().setAttributes(params);
                        operationClick.operationClick(productInfo.getProductId(),position);
                    }
                });
            }

            if(position==0){
                viewHolder.rootLayout.setPadding(0,Utils.dp2px(13),0,0);
            }else{
                viewHolder.rootLayout.setPadding(0,Utils.dp2px(5),0,0);
            }

        } else{
            final GoodsRecycleAdapter.BuyerViewHolder viewHolder = (GoodsRecycleAdapter.BuyerViewHolder) holder;
            final ProductDetail productDetail= mList.get(position);
            if(productDetail!=null){
                final ProductInfo productInfo=productDetail.getProductInfo();
                List<ProductImage> productImageList=productDetail.getImageList();
                if (payloads == null||payloads.isEmpty()) {
                    if (productImageList != null && productImageList.size() > 0) {
//                    if(!productImageList.get(0).getThumbnailUrl().equals(viewHolder.goodsImg.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                        viewHolder.goodsImg.setTag(null);//需要清空tag，否则报错
                        Utils.CornerImageViewDisplayByUrl(productImageList.get(0).getThumbnailUrl(), viewHolder.goodsImg,9);
//                        viewHolder.goodsImg.setTag(productImageList.get(0).getThumbnailUrl());
//                    }
                    } else {
                        RequestOptions options = new RequestOptions().centerCrop().transform(new GlideRoundTransform(9)).error(R.mipmap.goods_no_image);
                        Glide.with(App.getInstance()).asBitmap().load("").apply(options).into(viewHolder.goodsImg);
                    }
                }
                int isHot=productInfo.getIsHot();
                int isNew=productInfo.getIsNew();
                final int status=productInfo.getStatus();
                viewHolder.hotImg.setVisibility(View.GONE);
                if(isHot==1){
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_hot_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                }
                if(isNew==1){
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_new_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                }
                if(status==1){
                    //已下架商品
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_unenable_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                    viewHolder.plusImg.setBackgroundResource(R.mipmap.goods_plus_unenable_icon);
                }else{
                    viewHolder.plusImg.setBackgroundResource(R.mipmap.goods_plus_icon);
                }
                viewHolder.tv_goods_name.setText(productInfo.getProductName());
                final int productRemainTotal=productInfo.getProductRemainTotal();
                viewHolder.tv_product_remain_total.setText("库存："+productRemainTotal);
                final BigDecimal salePrice=productInfo.getSalePrice();
                BigDecimal productPrice=productInfo.getProductPrice();
                viewHolder.tv_sale_price.setText(salePrice+"");
                if(salePrice.equals(productPrice)){
                    viewHolder.tv_product_price.setVisibility(View.GONE);
                    viewHolder.tv_product_discount.setVisibility(View.GONE);
//                    viewHolder.product_price_text.setVisibility(View.GONE);
                }else{
                    viewHolder.tv_product_price .setText("¥ "+productPrice);
                    viewHolder.tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    viewHolder.tv_product_discount.setText(productInfo.getProductDiscount()+"折");
                    viewHolder.tv_product_price.setVisibility(View.VISIBLE);
                    viewHolder.tv_product_discount.setVisibility(View.VISIBLE);
//                    viewHolder.product_price_text.setVisibility(View.VISIBLE);
                }

                viewHolder.tv_num.setText(productInfo.getProductNum()+"");
                if(productInfo.getProductNum()>0){
                    viewHolder.tv_num.setVisibility(View.VISIBLE);
                    viewHolder.reduceImg.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.tv_num.setVisibility(View.INVISIBLE);
                    viewHolder.reduceImg.setVisibility(View.INVISIBLE);
                }

            viewHolder.plusImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(status==1){
                        return;
                    }else if(type==TYPE_CLERT){
                        Utils.showToastShortTime("店员不能购买商品");
                        return;
                    }else if(isSchoolmate==1){
                        Utils.showToastShortTime("非本校人员不得进行购买");
                        return;
                    }else if(isReportMax==1){
                        Utils.showToastShortTime("该商铺暂停售卖");
                        return;
                    }else if(isAuth==1||isAuth==3){
                        Utils.showToastShortTime("该店正在装修，请稍后再来！");
                        return;
                    }else{
                        //加
                        String numStr=viewHolder.tv_num.getText().toString();
                        if(StringUtils.isEmpty(numStr)){
                            viewHolder.tv_num.setText(1+"");
                        }else{
                            int num= Integer.parseInt(numStr);
                            if(num<productRemainTotal){
                                viewHolder.tv_num.setText((num+1)+"");
                            }else{
                                Utils.showToastShortTime("当前商品已售清");
                                return;
                            }
                        }
                        total++;
                        viewHolder.reduceImg.setVisibility(View.VISIBLE);
                        viewHolder.tv_num.setVisibility(View.VISIBLE);
                        productInfo.setProductNum(Integer.parseInt(viewHolder.tv_num.getText().toString()));
                        totalMoney=totalMoney.add(salePrice);
                        EventBus.getDefault().post(new UpdateShopChartListEvent(1,productDetail,total,totalMoney));
                    }

                }
            });
            viewHolder.reduceImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //减
                    String reduceStr=viewHolder.tv_num.getText().toString();
                    int num= Integer.parseInt(reduceStr);
                    if(num<=1){
                        viewHolder.reduceImg.setVisibility(View.INVISIBLE);
                        viewHolder.tv_num.setText("0");
                        viewHolder.tv_num.setVisibility(View.INVISIBLE);
                    }else{
                        viewHolder.reduceImg.setVisibility(View.VISIBLE);
                        viewHolder.tv_num.setText((num-1)+"");
                    }
                    total--;
                    productInfo.setProductNum(Integer.parseInt(viewHolder.tv_num.getText().toString()));
                    totalMoney=totalMoney.subtract(salePrice);
                    EventBus.getDefault().post(new UpdateShopChartListEvent(1,productDetail,total,totalMoney));

                }
            });

            }

            if(position==0){
                viewHolder.rootLayout.setPadding(0,Utils.dp2px(13),0,0);
            }else{
                viewHolder.rootLayout.setPadding(0,Utils.dp2px(5),0,0);
            }
        }

    }

    public class SellerViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout;
        ImageView goodsImg,hotImg;
        TextView tv_goods_name,tv_product_remain_total,tv_sale_price,product_price_text,tv_product_price,tv_product_discount,tv_operation;

        public SellerViewHolder(View view) {
            super(view);
            rootLayout= view.findViewById(R.id.root_layout);
            goodsImg = view.findViewById(R.id.goods_img);
            hotImg = view.findViewById(R.id.hot_img);
            tv_goods_name = view.findViewById(R.id.goods_name_textview);
            tv_product_remain_total = view.findViewById(R.id.product_remain_total_text_view);
            tv_sale_price = view.findViewById(R.id.sale_price_text_view);
            tv_product_price = view.findViewById(R.id.product_price_text_view);
            product_price_text = view.findViewById(R.id.product_price_text);
            tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            tv_product_discount = view.findViewById(R.id.product_discount_text_view);
            tv_operation = view.findViewById(R.id.operation_text_view);

        }
    }

    public class BuyerViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout;
        ImageView goodsImg,hotImg,plusImg,reduceImg;
        TextView tv_goods_name,tv_product_remain_total,tv_sale_price,product_price_text,tv_product_price,tv_product_discount,tv_num;

        public BuyerViewHolder(View view) {
            super(view);
            rootLayout= view.findViewById(R.id.root_layout);
            goodsImg = view.findViewById(R.id.goods_img);
            hotImg = view.findViewById(R.id.hot_img);
            tv_goods_name = view.findViewById(R.id.goods_name_textview);
            tv_product_remain_total = view.findViewById(R.id.product_remain_total_text_view);
            tv_sale_price = view.findViewById(R.id.sale_price_text_view);
            tv_product_price = view.findViewById(R.id.product_price_text_view);
            product_price_text = view.findViewById(R.id.product_price_text);
            tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            tv_product_discount = view.findViewById(R.id.product_discount_text_view);
            tv_num = view.findViewById(R.id.num_text_view);
            plusImg= view.findViewById(R.id.plus_img);
            reduceImg= view.findViewById(R.id.reduce_img);

        }
    }

    OperationClick operationClick;

    public void setOperationClick(OperationClick operationClick) {
        this.operationClick = operationClick;
    }

    public interface OperationClick {
        void operationClick(long productId, int position);
    }
}
