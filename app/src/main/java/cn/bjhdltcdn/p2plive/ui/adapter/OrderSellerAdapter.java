package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by Zhai_PC on 2018/4/17.
 */

public class OrderSellerAdapter extends BaseRecyclerAdapter {

    private final int TYPE_TRYOUT = 1, TYPE_SHOP = 2, TYPE_SCHOOL_SHOP = 3;
    private List<ProductOrder> mList = new ArrayList<>();
    private int type;
    private ViewHolder viewHolder;
    private Activity mActivity;
    private SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter;

    public OrderSellerAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }
    public void setList(List<ProductOrder> list) {
        this.mList = list;
    }

    public List<ProductOrder> getmList() {
        return mList;
    }

    public void addList(List<ProductOrder> list) {
        mList.addAll(list);
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position) != null) {
            if (mList.get(position).getProductInfo().getProductType() == 1) {
                type = TYPE_TRYOUT;
            } else if (mList.get(position).getProductInfo().getProductType() == 2) {
                type = TYPE_SHOP;
            } else if (mList.get(position).getProductInfo().getProductType() == 3) {
                type = TYPE_SCHOOL_SHOP;
            }
        }
        return type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        if (itemType == TYPE_TRYOUT) {
            viewHolder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_tryout_order_item, null));
        } else if (itemType == TYPE_SHOP) {
            viewHolder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_shop_order_item, null));
        } else if (itemType == TYPE_SCHOOL_SHOP) {
            viewHolder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_school_shop_order_item, null));
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final OrderSellerAdapter.ViewHolder viewHolder = (OrderSellerAdapter.ViewHolder) holder;

        if (getItemViewType(position) == TYPE_TRYOUT) {

            if (mList.get(position).getOrderStatus() == 1) {
                viewHolder.tv_orderstatus.setText("已收货");
                viewHolder.tv_goods_receipt.setText("发布试用报告");
            } else {
                viewHolder.tv_orderstatus.setText("未收货");
                viewHolder.tv_goods_receipt.setText("确认收货");
            }

            final ProductInfo productInfo = mList.get(position).getProductInfo();
            if (productInfo != null) {
                viewHolder.tv_productname.setText(productInfo.getProductName());
                Utils.CornerImageViewDisplayByUrl(productInfo.getProductImg(), viewHolder.iv_productimg,9);
                String productTotal = String.format(App.getInstance().getResources().getString(R.string.str_product_total), productInfo.getProductTotal());
                viewHolder.tv_limited.setText(productTotal);
                viewHolder.tv_productprice.setText(mList.get(position).getProductInfo().getProductPrice()+"");
                viewHolder.tv_now_productprice.setText(mList.get(position).getPrice());
                viewHolder.tv_productprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            viewHolder.tv_goods_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoodsitemClick.onGoodsReceiptClick(mList.get(position).getOrderId(), position);
                }
            });

        } else if (getItemViewType(position) == TYPE_SHOP) {
            if (mList.get(position).getOrderStatus() == 1) {
                viewHolder.tv_shop_orderstatus.setText("已收货");
                viewHolder.tv_shop_goods_receipt.setVisibility(View.GONE);
            } else {
                viewHolder.tv_shop_orderstatus.setText("未收货");
                viewHolder.tv_shop_goods_receipt.setVisibility(View.VISIBLE);
            }
            final ProductInfo productInfo = mList.get(position).getProductInfo();
            if (productInfo != null) {
                viewHolder.tv_shop_productname.setText(productInfo.getProductName());
                Utils.CornerImageViewDisplayByUrl(productInfo.getProductImg(), viewHolder.iv_shop_productimg,9);
                viewHolder.tv_shop_now_productprice.setText(mList.get(position).getPrice());
                viewHolder.tv_shop_num.setText("×" + mList.get(position).getProductNum());
                viewHolder.tv_list_shop_totle_praice.setText("¥" + mList.get(position).getTotalPrice());
                viewHolder.tv_shop_productprice.setText("¥" + productInfo.getProductPrice());
                viewHolder.tv_shop_productprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            viewHolder.tv_shop_goods_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoodsitemClick.onGoodsReceiptClick(mList.get(position).getOrderId(), position);
                }
            });
        } else if (getItemViewType(position) == TYPE_SCHOOL_SHOP) {
            if (mList.get(position).getOrderStatus() == 1) {
                viewHolder.tv_school_orderstatus.setText("已收货");
                viewHolder.tv_school_goods_receipt.setText("立即评价");
            } else {
                viewHolder.tv_school_orderstatus.setText("未收货");
                viewHolder.tv_school_goods_receipt.setText("确认收货");
            }
            schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(mActivity);
            viewHolder.rv_school_shop.setHasFixedSize(true);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
            LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
            viewHolder.rv_school_shop.setLayoutManager(linearlayoutManager);
            viewHolder.rv_school_shop.addItemDecoration(linearLayoutSpaceItemDecoration);
            viewHolder.rv_school_shop.setAdapter(schoolShopOrderDetailAdapter);

            schoolShopOrderDetailAdapter.setmList(mList.get(position).getProductList());

            viewHolder.tv_shop_goods_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoodsitemClick.onGoodsReceiptClick(mList.get(position).getOrderId(), position);
                }
            });
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoodsitemClick.onItemClick(position);
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_orderstatus;
        ImageView iv_productimg;
        TextView tv_productname;
        TextView tv_limited;
        TextView tv_productprice;
        TextView tv_goods_receipt;
        TextView tv_now_productprice;
        TextView tv_shop_orderstatus;
        ImageView iv_shop_productimg;
        TextView tv_shop_productname;
        TextView tv_shop_productprice;
        TextView tv_shop_num;
        TextView tv_school_goods_receipt;
        TextView tv_shop_praice;
        TextView tv_shop_now_productprice;
        TextView tv_list_shop_totle_praice;
        TextView tv_shop_goods_receipt;
        TextView tv_school_orderstatus;
        RecyclerView rv_school_shop;

        public ViewHolder(View view) {
            super(view);
            tv_orderstatus = view.findViewById(R.id.tv_orderstatus);
            iv_productimg = view.findViewById(R.id.iv_productimg);
            tv_productname = view.findViewById(R.id.tv_productname);
            tv_limited = view.findViewById(R.id.tv_limited);
            tv_productprice = view.findViewById(R.id.tv_productprice);
            tv_goods_receipt = view.findViewById(R.id.tv_goods_receipt);
            tv_now_productprice = view.findViewById(R.id.tv_now_productprice);

            //商店
            tv_shop_orderstatus = view.findViewById(R.id.tv_shop_orderstatus);
            iv_shop_productimg = view.findViewById(R.id.iv_shop_productimg);
            tv_shop_productname = view.findViewById(R.id.tv_shop_productname);
            tv_shop_productprice = view.findViewById(R.id.tv_shop_productprice);
            tv_shop_num = view.findViewById(R.id.tv_list_shop_num);
            tv_shop_praice = view.findViewById(R.id.tv_shop_praice);
            tv_shop_goods_receipt = view.findViewById(R.id.tv_shop_goods_receipt);
            tv_shop_now_productprice = view.findViewById(R.id.tv_shop_now_productprice);
            tv_list_shop_totle_praice = view.findViewById(R.id.tv_list_shop_totle_praice);

            //校园店
            rv_school_shop = view.findViewById(R.id.rv__school_shop);
            tv_school_goods_receipt = view.findViewById(R.id.tv_school_goods_receipt);
            tv_school_orderstatus = view.findViewById(R.id.tv_school_orderstatus);

        }
    }

    OnGoodsitemClick onGoodsitemClick;

    public void setOnGoodsItemClick(OnGoodsitemClick onGoodsitemClick) {
        this.onGoodsitemClick = onGoodsitemClick;
    }

    public interface OnGoodsitemClick {
        void onGoodsReceiptClick(long orderId, int position);

        void onItemClick(int position);
    }
}
