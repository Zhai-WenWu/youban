package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
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
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductInfoDetail;
import cn.bjhdltcdn.p2plive.ui.activity.BusinessDistrictActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.fragment.GoodsRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.StoreGoodsRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by huwenhua on 2018/4/17.
 */

public class StoreGoodsRecycleAdapter extends BaseRecyclerAdapter {
    private List<ProductInfoDetail> mList = new ArrayList<>();
    private StoreGoodsRecycleFragment activity;
    private BigDecimal totalMoney=new BigDecimal(0);
    private boolean refresh;//整体刷新的时候是否刷新图片
    private int screenWidth;

    public StoreGoodsRecycleAdapter(StoreGoodsRecycleFragment activity) {
        this.activity=activity;
        screenWidth= PlatformInfoUtils.getWidthOrHeight(activity.getActivity())[0];
    }

    public void setList(List<ProductInfoDetail> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<ProductInfoDetail> getmList() {
        return mList;
    }

    public void addList(List<ProductInfoDetail> list) {
        mList.addAll(list);
        notifyItemRangeInserted(mList.size()-list.size(),list.size());
    }


    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public ProductInfoDetail getItem(int position){
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
                  ProductInfoDetail productInfoDetail = mList.get(i);
                  ProductInfo productInfo=productInfoDetail.getProductInfo();
                  if (productInfo.getProductId() == productdetail.getProductInfo().getProductId()) {
                      productInfo.setProductNum(productdetail.getProductInfo().getProductNum());
                      mList.remove(i);
                      mList.add(i, productInfoDetail);
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
                ProductInfoDetail productInfoDetail = mList.get(i);
                ProductInfo productInfo=productInfoDetail.getProductInfo();
                productInfo.setProductNum(0);
            }
//            notifyDataSetChanged();
            notifyItemRangeChanged(0,mList.size(),true);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        BaseViewHolder viewHolder = viewHolder = new SellerViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_store_goods_recycle_item, null));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position,@NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
            final StoreGoodsRecycleAdapter.SellerViewHolder viewHolder = (StoreGoodsRecycleAdapter.SellerViewHolder) holder;
            if(mList==null||mList.size()<=0){
                return;
            }
            final ProductInfoDetail productInfoDetail = mList.get(position);
            ProductInfo productInfo=productInfoDetail.getProductInfo();
            if(productInfo!=null) {
                String imageStr = productInfo.getProductImg();
                if (!StringUtils.isEmpty(imageStr)) {
                        Utils.ImageViewDisplayByUrl(imageStr, viewHolder.goodsImg);
                } else {
                        RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.goods_no_image_big);
                        Glide.with(App.getInstance()).asBitmap().load("").apply(options).into(viewHolder.goodsImg);
                }
                final int isHot = productInfo.getIsHot();
                final int isNew = productInfo.getIsNew();
                final int status = productInfo.getStatus();
                viewHolder.hotImg.setVisibility(View.GONE);
                if (isHot == 1) {
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_hot_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                }
                if (isNew == 1) {
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_new_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                }
                if (status == 1) {
                    //已下架商品
                    viewHolder.hotImg.setImageResource(R.mipmap.goods_unenable_icon);
                    viewHolder.hotImg.setVisibility(View.VISIBLE);
                }
                viewHolder.tv_goods_name.setText(productInfo.getProductName());
                viewHolder.tv_product_remain_total.setText(productInfo.getSaledTotal()+"人付款");
                BigDecimal salePrice = productInfo.getSalePrice();
                BigDecimal productPrice = productInfo.getProductPrice();
                viewHolder.tv_sale_price.setText(salePrice + "");
                if (salePrice.equals(productPrice)) {
                    viewHolder.tv_product_price.setVisibility(View.GONE);
                    viewHolder.tv_product_discount.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_product_price.setText("¥" + productPrice);
                    viewHolder.tv_product_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                    viewHolder.tv_product_discount.setText(productInfo.getProductDiscount() + "折");
                    viewHolder.tv_product_price.setVisibility(View.VISIBLE);
                    viewHolder.tv_product_discount.setVisibility(View.VISIBLE);
                }

                viewHolder.ti_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转店铺详情
                        Intent intent = new Intent(activity.getActivity(), ShopDetailActivity.class);
                        intent.putExtra(Constants.Fields.STORE_ID,productInfoDetail.getStoreId());
                        activity.getActivity().startActivity(intent);
                    }
                });

                viewHolder.tv_store_title.setText(productInfoDetail.getTitle());

//                if (position == 0) {
//                    viewHolder.rootLayout.setPadding(0, Utils.dp2px(13), 0, 0);
//                } else {
//                    viewHolder.rootLayout.setPadding(0, Utils.dp2px(5), 0, 0);
//                }
            }

    }

    public class SellerViewHolder extends BaseViewHolder {
        RelativeLayout rootLayout;
        ImageView goodsImg,hotImg;
        TextView tv_goods_name,tv_product_remain_total,tv_sale_price,product_price_text,tv_product_price,tv_product_discount,tv_store_title,ti_in;

        public SellerViewHolder(View view) {
            super(view);
            rootLayout= view.findViewById(R.id.root_layout);
            goodsImg = view.findViewById(R.id.goods_img);
            goodsImg.getLayoutParams().height=(screenWidth-Utils.dp2px(32))/2;
            hotImg = view.findViewById(R.id.hot_img);
            tv_goods_name = view.findViewById(R.id.goods_name_textview);
            tv_product_remain_total = view.findViewById(R.id.product_remain_total_text_view);
            tv_sale_price = view.findViewById(R.id.sale_price_text_view);
            tv_product_price = view.findViewById(R.id.product_price_text_view);
            product_price_text = view.findViewById(R.id.product_price_text);
            tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
            tv_product_discount = view.findViewById(R.id.product_discount_text_view);
            tv_store_title= view.findViewById(R.id.store_text_view);
            ti_in= view.findViewById(R.id.in_text_view);

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
