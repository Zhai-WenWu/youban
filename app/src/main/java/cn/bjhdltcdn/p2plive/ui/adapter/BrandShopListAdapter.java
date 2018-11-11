package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.BrandShop;
import cn.bjhdltcdn.p2plive.ui.activity.BrandBusinessDetailActiviy;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Description:品牌商列表适配器
 * Data: 2018/9/18
 *
 * @author: zhudi
 */
public class BrandShopListAdapter extends BaseRecyclerAdapter {
    private List<BrandShop> list;

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void updateList(List<BrandShop> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_brandshop, viewGroup, false);
        return new BrandShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof BrandShopViewHolder) {
            BrandShopViewHolder brandShopViewHolder = (BrandShopViewHolder) holder;
            BrandShop brandShop = list.get(position);
            Utils.ImageViewDisplayByUrl(brandShop.getBrandShopIcon(), brandShopViewHolder.shopIcon);
            String brandShopName = brandShop.getBrandShopName();
            if (!TextUtils.isEmpty(brandShopName)) {
                brandShopViewHolder.nameView.setText(brandShopName);
            }
            brandShopViewHolder.saleNumView.setText("总销量：" + brandShop.getTotalSales());
            brandShopViewHolder.shopStarView.setNumStars(5);
            brandShopViewHolder.shopStarView.setStepSize(0.5f);
            brandShopViewHolder.shopStarView.setRating(brandShop.getEvalScore());
            brandShopViewHolder.productServiceView.setText("共" + brandShop.getProductCount() + "件商品 " + brandShop.getService() + "类服务");
            brandShopViewHolder.intoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(App.getInstance(), BrandBusinessDetailActiviy.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getInstance().startActivity(intent);
                }
            });
        }
    }

    class BrandShopViewHolder extends BaseViewHolder {

        private ImageView shopIcon, levelIcon;
        private TextView nameView, saleNumView, productServiceView, intoView;
        private RatingBar shopStarView;


        public BrandShopViewHolder(View itemView) {
            super(itemView);
            shopIcon = itemView.findViewById(R.id.iv_icon);
            levelIcon = itemView.findViewById(R.id.iv_level);
            nameView = itemView.findViewById(R.id.nick_name_view);
            saleNumView = itemView.findViewById(R.id.tv_num_sales);
            productServiceView = itemView.findViewById(R.id.tv_product_service);
            intoView = itemView.findViewById(R.id.tv_into);
            shopStarView = itemView.findViewById(R.id.ratbar_brandshop);
        }
    }
}
