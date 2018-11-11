package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by zhaiww on 2018/5/10.
 */

public class SchoolShopOrderDetailAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private ViewHolder holder;
    private List<ProductInfo> mList;
    private ViewHolder viedHolder;

    public List<ProductInfo> getmList() {
        return mList;
    }

    public void setmList(List<ProductInfo> mList) {
        this.mList = mList;
    }

    public SchoolShopOrderDetailAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        holder = new SchoolShopOrderDetailAdapter.ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_shcool_shop_order_detail, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder) {
            viedHolder = (ViewHolder) holder;
            ProductInfo productInfo = mList.get(position);
            if (!StringUtils.isEmpty(productInfo.getProductImg())) {
                Utils.CornerImageViewDisplayByUrl(productInfo.getProductImg(), viedHolder.iv_school_goods_ima,9);
            } else {
                viedHolder.iv_school_goods_ima.setImageResource(R.drawable.order_no_product_icon);
            }
            viedHolder.tv_school_goods_name.setText(productInfo.getProductName());
            viedHolder.tv_school_goods_praice.setText("¥" + productInfo.getSalePrice());
            viedHolder.tv_school_goods_num.setText("×" + productInfo.getProductNum());
        }
    }

    class ViewHolder extends BaseViewHolder {
        private ImageView iv_school_goods_ima;
        private TextView tv_school_goods_name, tv_school_goods_praice, tv_school_goods_num;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_school_goods_ima = itemView.findViewById(R.id.iv_school_goods_ima);
            tv_school_goods_name = itemView.findViewById(R.id.tv_school_goods_name);
            tv_school_goods_praice = itemView.findViewById(R.id.tv_school_goods_praice);
            tv_school_goods_num = itemView.findViewById(R.id.tv_school_goods_num);
        }
    }

}
