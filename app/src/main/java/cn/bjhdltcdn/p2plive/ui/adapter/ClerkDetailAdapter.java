package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ClertDetail;
import cn.bjhdltcdn.p2plive.model.ClertInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.ui.activity.ClerkDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by zhaiww on 2018/5/22.
 */

public class ClerkDetailAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private int type;
    private int ITEM_CLERT_INFO = 1, ITEM_TAB = 2, ITEM_PRODUCT_ORDER = 3;
    private List<ProductOrder> mList = new ArrayList<>();
    private ViewHolder holder;

    public ClerkDetailAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        holder = new ClerkDetailAdapter.ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_product_order, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            ProductOrder productOrder = mList.get(position);
            viewHolder.tv_order_time.setText(productOrder.getUpdateTime());
            SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(mActivity);
            viewHolder.rv__school_shop.setHasFixedSize(true);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
            LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
            viewHolder.rv__school_shop.setLayoutManager(linearlayoutManager);
            viewHolder.rv__school_shop.addItemDecoration(linearLayoutSpaceItemDecoration);
            viewHolder.rv__school_shop.setAdapter(schoolShopOrderDetailAdapter);
            schoolShopOrderDetailAdapter.setmList(productOrder.getProductList());
            schoolShopOrderDetailAdapter.notifyDataSetChanged();
            viewHolder.tv_school_goods_num.setText("共计" + productOrder.getTotalProductCount() + "件商品   合计：¥" + productOrder.getTotalPrice());
            if (productOrder.getPostAge() == null) {
                viewHolder.tv_school_postage.setText("配送费：¥0");
            } else {
                if (StringUtils.isEmpty(productOrder.getPostAge())) {
                    viewHolder.tv_school_postage.setText("配送费：¥0");
                } else {
                    viewHolder.tv_school_postage.setText("配送费：¥" + productOrder.getPostAge());
                }
            }
        }
    }

    public void setmList(List<ProductOrder> list) {
        mList = list;
    }

    public List<ProductOrder> getmList() {
        return mList;
    }

    public void addmList(List<ProductOrder> list) {
        mList.addAll(list);
    }

    public class ViewHolder extends BaseViewHolder {
        private TextView tv_order_time;
        private TextView tv_school_goods_num;
        private TextView tv_school_postage;
        private RecyclerView rv__school_shop;

        public ViewHolder(View view) {
            super(view);
            tv_order_time = view.findViewById(R.id.tv_order_time);
            tv_school_goods_num = view.findViewById(R.id.tv_school_goods_num);
            rv__school_shop = view.findViewById(R.id.rv__school_shop);
            tv_school_postage = view.findViewById(R.id.tv_school_postage);
        }
    }
}
