package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ForwardStoreDetailEvent;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.LocationInfo;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.ui.activity.GoodsDetailActivity;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * 商圈店铺列表
 */

public class ShopRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<StoreDetail> list;
    private Activity mActivity;
    private RequestOptions options;
    private OnClick onClick;
    private View convertView = null;
    private int screenWidth;

    public ShopRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<StoreDetail>();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        screenWidth= PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
    }

    public void setList(List<StoreDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<StoreDetail> list){
        this.list.addAll(list);
        notifyItemRangeInserted(this.list.size()-list.size(),list.size());
    }


    public StoreDetail getItem(int position){
        return list == null ? null : list.get(position);
    }


    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public List<StoreDetail> getList() {
        return list;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            return new ShopHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.shop_recycle_item_layout, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
                    if(holder instanceof ShopHolder){
                        final ShopHolder shopHolder = (ShopHolder) holder;
                        StoreDetail storeDetail=list.get(position);
                        final StoreInfo storeInfo=storeDetail.getStoreInfo();
                        if(storeDetail!=null&&storeInfo!=null){
                            Utils.ImageViewDisplayByUrl(storeInfo.getStoreIcon(),shopHolder.shopImg);
                            shopHolder.titleTextView.setText(storeInfo.getTitle());
                            String distance=storeInfo.getDistance();
                            if(!StringUtils.isEmpty(distance)){
                                shopHolder.distanceTextView.setText(distance);
                                shopHolder.distanceTextView.setPadding(Utils.dp2px(7),0,0,0);
                            }else{
                                shopHolder.distanceTextView.setText(distance);
                                shopHolder.distanceTextView.setPadding(0,0,0,0);
                            }
                            LocationInfo locationInfo=storeInfo.getLocationInfo();
                            if(locationInfo!=null){
                                if(!StringUtils.isEmpty(distance)){
                                    shopHolder.locationTextView.setText(storeInfo.getLocationInfo().getDistrict());
                                }else{
                                    shopHolder.locationTextView.setText(storeInfo.getLocationInfo().getCity());
                                }
                            }
                            shopHolder.schoolNameTextView.setText(storeInfo.getSchoolName());

                            shopHolder.goodsOneImageView.setVisibility(View.INVISIBLE);
                            shopHolder.goodsTwoImageView.setVisibility(View.INVISIBLE);
                            shopHolder.goodsThreeImageView.setVisibility(View.INVISIBLE);
                            final List<ProductInfo> productInfoList=storeDetail.getProductList();
                            ProductInfo productInfo;
                            if(productInfoList!=null&&productInfoList.size()>0){
                                for (int i=0;i<productInfoList.size();i++){
                                    productInfo=productInfoList.get(i);
                                    String productImg=productInfo.getProductImg();
                                    switch (i){
                                        case 0:
                                            Utils.ImageViewDisplayByUrl(productImg,shopHolder.goodsOneImageView);
                                            shopHolder.goodsOneImageView.setVisibility(View.VISIBLE);
                                            break;
                                        case 1:
                                            Utils.ImageViewDisplayByUrl(productImg,shopHolder.goodsTwoImageView);
                                            shopHolder.goodsTwoImageView.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:
                                            Utils.ImageViewDisplayByUrl(productImg,shopHolder.goodsThreeImageView);
                                            shopHolder.goodsThreeImageView.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }

                            }else{
                                Utils.ImageViewDisplayByUrl(R.mipmap.store_goods_no_img_icon,shopHolder.goodsOneImageView);
                                shopHolder.goodsOneImageView.setVisibility(View.VISIBLE);
                            }
                            shopHolder.goodsOneImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //跳转到商品详情
                                    if(productInfoList!=null&&productInfoList.size()>0&&productInfoList.get(0)!=null){
                                        Intent intent=new Intent(mActivity, GoodsDetailActivity.class);
                                        intent.putExtra(Constants.Fields.PRODUCT_ID, productInfoList.get(0).getProductId());
                                        intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                                        mActivity.startActivity(intent);
                                    }

                                }
                            });
                            shopHolder.goodsTwoImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //跳转到商品详情
                                    if(productInfoList!=null&&productInfoList.size()>0&&productInfoList.get(1)!=null){
                                        Intent intent=new Intent(mActivity, GoodsDetailActivity.class);
                                        intent.putExtra(Constants.Fields.PRODUCT_ID, productInfoList.get(1).getProductId());
                                        intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                                        mActivity.startActivity(intent);
                                    }

                                }
                            });
                            shopHolder.goodsThreeImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //跳转到商品详情
                                    if(productInfoList!=null&&productInfoList.size()>0&&productInfoList.get(2)!=null){
                                        Intent intent=new Intent(mActivity, GoodsDetailActivity.class);
                                        intent.putExtra(Constants.Fields.PRODUCT_ID, productInfoList.get(2).getProductId());
                                        intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                                        mActivity.startActivity(intent);
                                    }
                                }
                            });

                            ShopCategoryRecyclerViewAdapter recyclerAdapter= new ShopCategoryRecyclerViewAdapter(mActivity);
                            FirstLabelInfo firstLabelInfo=storeInfo.getFirstLabelInfo();
                            if(firstLabelInfo!=null){
                               recyclerAdapter.setFirstLabelInfo(firstLabelInfo);
                            }
                            shopHolder.recycleView.setAdapter(recyclerAdapter);
                            recyclerAdapter.setOnItemListener(new ItemListener() {
                                @Override
                                public void onItemClick(View view, int pos) {
//                                    shopHolder.titleTextView.performClick();
                                    EventBus.getDefault().post(new ForwardStoreDetailEvent(position));
                                }
                            });

                            if(storeInfo.getBaseUser().getIsAuth()==0){
                                shopHolder.isAuthImageView.setVisibility(View.VISIBLE);
                            }else{
                                shopHolder.isAuthImageView.setVisibility(View.GONE);
                            }

                        }

                        if(position==0){
                            shopHolder.headerView.setVisibility(View.VISIBLE);
                        }else{
                            shopHolder.headerView.setVisibility(View.GONE);
                        }
                    }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ShopHolder extends BaseViewHolder {
        View headerView;
        ImageView shopImg,isAuthImageView;
        TextView titleTextView,distanceTextView,locationTextView,schoolNameTextView,inTextView;
        RecyclerView recycleView;
        LinearLayout goodsLayout;
        ImageView goodsOneImageView,goodsTwoImageView,goodsThreeImageView;

        public ShopHolder(View itemView) {
            super(itemView);
            headerView= itemView.findViewById(R.id.top_header_view);
            shopImg = (ImageView) itemView.findViewById(R.id.shop_img);
            titleTextView = (TextView) itemView.findViewById(R.id.shop_name_text);
            distanceTextView= (TextView) itemView.findViewById(R.id.shop_distance_text_view);
            locationTextView= (TextView) itemView.findViewById(R.id.shop_location_text_view);
            schoolNameTextView= (TextView) itemView.findViewById(R.id.user_school_text_view);
            inTextView= (TextView) itemView.findViewById(R.id.in_text_view);
            goodsLayout= (LinearLayout) itemView.findViewById(R.id.goods_layout);
            goodsLayout.getLayoutParams().height=(screenWidth-Utils.dp2px(40+18))/3;
            goodsOneImageView= (ImageView) itemView.findViewById(R.id.goods_one_img);
            goodsTwoImageView= (ImageView) itemView.findViewById(R.id.goods_two_img);
            goodsThreeImageView= (ImageView) itemView.findViewById(R.id.goods_three_img);
            recycleView = (RecyclerView) itemView.findViewById(R.id.recycler_category);
            recycleView.setHasFixedSize(true);
//            FlowLayoutManager flowLayoutManager = new FlowLayoutManager(mActivity);
//            flowLayoutManager.setMargin(Utils.dp2px(5));
//            recycleView.setLayoutManager(flowLayoutManager);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.HORIZONTAL, false);
//            recycleView.setLayoutManager(linearLayoutManager);
            ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mActivity)
                    .setChildGravity(Gravity.TOP)
                    .setScrollingEnabled(true)
                    .setGravityResolver(new IChildGravityResolver() {
                        @Override
                        public int getItemGravity(int position) {
                            return Gravity.LEFT;
                        }
                    })
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT )
                    .withLastRow(true)
                    .build();
            recycleView.setLayoutManager(chipsLayoutManager);
            recycleView.setHasFixedSize(true);
            recycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(8),false));

            isAuthImageView= (ImageView) itemView.findViewById(R.id.sm_img);
        }

    }

    /**
     * 设置星级
     */
    private void setEvaluateShowLayout(double averageScore,LinearLayout evaluateStarLayout) {
        //显示评价星数
        if (averageScore >= 0) {

            evaluateStarLayout.removeAllViews();

            if(averageScore==0){
                //0星
                for (int i = 0; i < (5 - Math.round(averageScore)); i++) {
                    ImageView starImage = new ImageView(mActivity);
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

            if(averageScore>0&&averageScore<1){
                //大于0 小于1 星
                if (Math.round(averageScore) > averageScore) {
                    ImageView starImage = new ImageView(mActivity);
                    starImage.setImageResource(R.drawable.star_half_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }else{
                    ImageView starImage = new ImageView(mActivity);
                    starImage.setImageResource(R.drawable.rating_unselect_icon);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 7, 0);
                    params.height = Utils.dp2px(10);//设置图片的高度
                    params.width = Utils.dp2px(10); //设置图片的宽度
                    starImage.setLayoutParams(params);
                    evaluateStarLayout.addView(starImage);
                }
            }else{
               //大于等于1星
                // 新增完整的星
                for (int i = 0; i < (int) averageScore; i++) {
                    ImageView starImage = new ImageView(mActivity);
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
                    ImageView starImage = new ImageView(mActivity);
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
                    ImageView starImage = new ImageView(mActivity);
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

    public interface OnClick{
        void onJoinClick(int position, int type);
    }

    public void onDestroy(){
        if (mActivity != null) {
            mActivity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }

}