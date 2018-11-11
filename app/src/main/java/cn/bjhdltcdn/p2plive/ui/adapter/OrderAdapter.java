package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
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
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.ui.activity.OrderDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by Zhai_PC on 2018/4/17.
 */

public class OrderAdapter extends BaseRecyclerAdapter {

    private final int TYPE_TRYOUT = 1, TYPE_SHOP = 2, TYPE_SCHOOL_SHOP = 3;
    private List<ProductOrder> mList = new ArrayList<>();
    private int type;
    private ViewHolder viewHolder;
    private Activity mActivity;
    private SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter;
    private int comeInType;//1:卖家
    private boolean isPayOrderrder;
    private boolean isSaleRecord;
    private boolean canOpenOrderDetailActivity = true;
    private final long userId;

    public void setPayOrderrder(boolean payOrderrder) {
        isPayOrderrder = payOrderrder;
    }

    public void setSaleRecord(boolean saleRecord) {
        isSaleRecord = saleRecord;
    }

    public void setCanOpenOrderDetailActivity(boolean canOpenOrderDetailActivity) {
        this.canOpenOrderDetailActivity = canOpenOrderDetailActivity;
    }

    public void setComeInType(int comeInType) {
        this.comeInType = comeInType;
    }

    public OrderAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
    }

    public void setList(List<ProductOrder> list) {
        if (list != null) {
            this.mList = list;
        } else {
            mList = new ArrayList<>();
        }

    }

    public List<ProductOrder> getmList() {
        return mList;
    }

    public void addList(List<ProductOrder> list) {
        mList.addAll(list);
    }

    @Override
    public int getItemViewType(int position) {

        ProductInfo productInfo = mList.get(position).getProductInfo();
        if (productInfo != null) {
            if (productInfo.getProductType() == 1) {
                type = TYPE_TRYOUT;
            }
//            else if (productInfo.getProductType() == 2) {
//                type = TYPE_SHOP;
//            }
            else {
                type = TYPE_SCHOOL_SHOP;
            }
        } else {
            List<ProductInfo> productInfoList = mList.get(position).getProductList();
            if (productInfoList != null && productInfoList.size() > 0) {
                productInfo = productInfoList.get(0);
                if (productInfo.getProductType() == 1) {
                    type = TYPE_TRYOUT;
                }
//                else if (productInfo.getProductType() == 2) {
//                    type = TYPE_SHOP;
//                }
                else {
                    type = TYPE_SCHOOL_SHOP;
                }
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
        final OrderAdapter.ViewHolder viewHolder = (OrderAdapter.ViewHolder) holder;

        if (getItemViewType(position) == TYPE_TRYOUT) {

            if (mList.get(position).getIsSendReport() == 2) {
                viewHolder.tv_goods_receipt.setVisibility(View.INVISIBLE);
            }

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
                viewHolder.tv_productprice.setText(mList.get(position).getProductInfo().getProductPrice() + "");
                viewHolder.tv_now_productprice.setText(mList.get(position).getPrice());
                viewHolder.tv_productprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            viewHolder.tv_goods_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReceiptClick.OnReceiptClick(mList.get(position).getOrderId(), position);
                }
            });

        } else if (getItemViewType(position) == TYPE_SHOP) {

            if (mList.get(position).getIsEval() == 1) {
                viewHolder.tv_shop_goods_receipt.setVisibility(View.INVISIBLE);
            }

            if (mList.get(position).getOrderStatus() == 1) {
                viewHolder.tv_shop_orderstatus.setText("已收货");
                viewHolder.tv_shop_goods_receipt.setText("立即评价");
            } else {
                viewHolder.tv_shop_orderstatus.setText("未收货");
                viewHolder.tv_shop_goods_receipt.setText("确认收货");
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
                    onReceiptClick.OnReceiptClick(mList.get(position).getOrderId(), position);
                }
            });
        } else if (getItemViewType(position) == TYPE_SCHOOL_SHOP) {

            if (position==0){
                viewHolder.data.setVisibility(View.VISIBLE);
                viewHolder.data.setText("今天");
                //只是控制第一条，因为第一条是肯定显示的
                //getAddTime跟今天时间对比，如果相等，显示“今天”
                //如果不相等，显示日期
            }else {//从第二条开始，比对此条的上一天
                //如果时间不同，显示布局，设置时间
                if (mList.get(position).getAddTime().substring(0,9)!=mList.get(position-1).getAddTime().substring(0,9)){
                    viewHolder.data.setVisibility(View.VISIBLE);
                    viewHolder.data.setText(mList.get(position).getAddTime());
                }else {
                    //本条跟上条时间相同，代表是同一天的条目，隐藏时间布局
                    viewHolder.data.setVisibility(View.GONE);

                }
            }

            if (mList == null || mList.size() <= 0) {
                return;
            }
            int orderStatus = mList.get(position).getOrderStatus();
            if (comeInType == 1) {//卖家
                if (mList.get(position).getIsReceipt() == 1) {
                    //已接单
                    viewHolder.tv_store_info.setVisibility(View.GONE);
                    viewHolder.tv_receipt.setVisibility(View.GONE);
                } else {
                    //未接单
                    viewHolder.tv_store_info.setVisibility(View.GONE);
                    if (mList.get(position).getDistributeMode() == 37) {
                        //自提的不展示接单按钮
                        viewHolder.tv_receipt.setVisibility(View.GONE);
                    } else {
                        //卖家配送
//                        int isReceipt = mList.get(position).getIsReceipt();
//                        if (isReceipt == 0) {
//                            if (orderStatus != 1 && orderStatus != 4 && orderStatus != 3)
//                                viewHolder.tv_receipt.setVisibility(View.VISIBLE);
//                        } else if (isReceipt == 1) {
//                            viewHolder.tv_receipt.setVisibility(View.GONE);
//                        }
                        if (orderStatus == 2) {
                            viewHolder.tv_receipt.setVisibility(View.VISIBLE);
                        } else {
                            viewHolder.tv_receipt.setVisibility(View.GONE);
                        }
                    }

                }

                if (orderStatus == 3) {//退款申请中
                    BaseUser receiptUser = mList.get(position).getReceiptUser();
                    if (receiptUser != null) {
                        if (receiptUser.getUserId() == userId) {
                            viewHolder.tv_refuse_refund.setVisibility(View.VISIBLE);
                            viewHolder.tv_refund.setVisibility(View.VISIBLE);
                            viewHolder.tv_receipt.setVisibility(View.GONE);
                        }
                    } else if (mList.get(position).getStoreInfo().getBaseUser().getUserId() == userId) {
                        viewHolder.tv_refuse_refund.setVisibility(View.VISIBLE);
                        viewHolder.tv_refund.setVisibility(View.VISIBLE);
                        viewHolder.tv_receipt.setVisibility(View.GONE);
                    } else {
                        viewHolder.tv_refuse_refund.setVisibility(View.GONE);
                        viewHolder.tv_refund.setVisibility(View.GONE);
                    }
                } else {
                    viewHolder.tv_refuse_refund.setVisibility(View.GONE);
                    viewHolder.tv_refund.setVisibility(View.GONE);
                }

                viewHolder.tv_refund.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefundClick.OnRefundClick(mList.get(position).getOrderId(), position);
                    }
                });

                if (orderStatus == 0) {//订单已完成卖家未确认收货
                    viewHolder.tv_school_goods_receipt.setVisibility(View.VISIBLE);
                    viewHolder.tv_receipt.setVisibility(View.GONE);
                    viewHolder.tv_school_goods_receipt.setText("确认送达");
                } else {
                    viewHolder.tv_school_goods_receipt.setVisibility(View.GONE);
                }

            } else {

                ProductOrder productOrder = mList.get(position);
                if ((orderStatus == 1 && productOrder.getBuyerSign() != 3) || orderStatus == 0) {//买家已完成没确认收货
                    viewHolder.tv_school_goods_receipt.setVisibility(View.VISIBLE);
                    viewHolder.tv_school_goods_receipt.setText("确认收货");
                } else if (productOrder.getIsEval() != 1 && orderStatus == 1 && productOrder.getBuyerSign() == 3) {//订单已完成买家已收货但没评价
                    viewHolder.tv_school_goods_receipt.setVisibility(View.VISIBLE);
                    viewHolder.tv_school_goods_receipt.setText("立即评价");
                } else {
                    viewHolder.tv_school_goods_receipt.setVisibility(View.GONE);
                }

                if (orderStatus == 3) {//退款申请中
                    viewHolder.tv_refuse_refund.setText("撤销退款");
                    viewHolder.tv_refuse_refund.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_refuse_refund.setVisibility(View.GONE);
                }

            }
            if (isPayOrderrder) {
                viewHolder.tv_school_orderstatus.setVisibility(View.INVISIBLE);
                viewHolder.tv_school_goods_receipt.setVisibility(View.GONE);
                viewHolder.tv_store_info.setVisibility(View.GONE);
            }
            if (isSaleRecord) {
                viewHolder.tv_time.setText(mList.get(position).getAddTime());
                viewHolder.tv_school_goods_receipt.setVisibility(View.GONE);
                viewHolder.tv_store_info.setVisibility(View.GONE);
            }

            switch (orderStatus) {
                case 0:
                    if (comeInType == 1) {//卖家
                        viewHolder.tv_school_orderstatus.setText("待发货");
                    } else {
                        viewHolder.tv_school_orderstatus.setText("待收货");
                    }

                    break;
                case 2:
                    if (comeInType == 1) {//卖家
                        viewHolder.tv_school_orderstatus.setText("未接单");
                    } else {
                        viewHolder.tv_school_orderstatus.setText("待收货");
                    }
                    break;
                case 1:
                    if (comeInType == 1) {//卖家
                        viewHolder.tv_school_orderstatus.setText("已完成");
                    } else {
                        viewHolder.tv_school_orderstatus.setText("已收货");
                    }
                    break;
                case 3:
                    if (comeInType == 1) {//卖家
                        viewHolder.tv_school_orderstatus.setText("退款中");
                    } else {
                        viewHolder.tv_school_orderstatus.setText("退款申请中");
                    }
                    break;
                case 4:
                    viewHolder.tv_school_orderstatus.setText("退款成功");
                    break;
            }

            StoreInfo storeInfo = mList.get(position).getStoreInfo();

            if (storeInfo != null) {
                viewHolder.tv_store_info.setText(storeInfo.getTitle());
            } else {
                viewHolder.tv_store_info.setVisibility(View.GONE);
            }

            if (mList.get(position).getPostAge() == null) {
                viewHolder.tv_school_postage.setText("配送费：¥0");
            } else {
                if (StringUtils.isEmpty(mList.get(position).getPostAge())) {
                    viewHolder.tv_school_postage.setText("配送费：¥0");
                } else {
                    viewHolder.tv_school_postage.setText("配送费：¥" + mList.get(position).getPostAge());
                }
            }

            schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(mActivity);
            viewHolder.rv_school_shop.setHasFixedSize(true);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
            viewHolder.rv_school_shop.setLayoutManager(linearlayoutManager);
            viewHolder.rv_school_shop.setAdapter(schoolShopOrderDetailAdapter);

            schoolShopOrderDetailAdapter.setmList(mList.get(position).getProductList());

            schoolShopOrderDetailAdapter.setOnItemListener(new ItemListener() {
                @Override
                public void onItemClick(View view, int mPosition) {
                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                    intent.putExtra(Constants.Fields.ORDER_ID, mList.get(position).getOrderId());
                    intent.putExtra(Constants.Fields.POSITION, position);
                    if (comeInType == 1) {
                        intent.putExtra(Constants.Fields.TYPE, 1);
                    }
                    if (canOpenOrderDetailActivity) {
                        mActivity.startActivity(intent);
                    }
                }
            });

            viewHolder.tv_school_goods_num.setText("共计" + mList.get(position).getTotalProductCount() + "件商品  合计：¥" + mList.get(position).getTotalPrice());

            viewHolder.tv_refuse_refund.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRefusingRefundClick.OnRefusingRefundClick(mList.get(position).getOrderId(), position);
                }
            });

            viewHolder.tv_school_goods_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCollectoodsClick.OnCollectGoodsClick(mList.get(position).getOrderId(), position);
                }
            });

            viewHolder.tv_receipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReceiptClick.OnReceiptClick(mList.get(position).getOrderId(), position);
                }
            });

            viewHolder.tv_store_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ShopDetailActivity.class);
                    intent.putExtra(Constants.Fields.STORE_ID, mList.get(position).getStoreInfo().getStoreId());
                    mActivity.startActivity(intent);
                }
            });
        }

    }

    public class ViewHolder extends BaseViewHolder {

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
        TextView tv_store_info;
        TextView tv_school_goods_num;
        TextView tv_refuse_refund;
        TextView tv_refund;
        RecyclerView rv_school_shop;
        TextView tv_time;
        TextView tv_receipt;
        TextView tv_school_postage;
        TextView data;

        public ViewHolder(View view) {
            super(view);
            tv_time = view.findViewById(R.id.tv_time);
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
            tv_store_info = view.findViewById(R.id.tv_store_info);
            tv_school_goods_num = view.findViewById(R.id.tv_school_goods_num);
            tv_refuse_refund = view.findViewById(R.id.tv_refuse_refund);
            tv_refund = view.findViewById(R.id.tv_refund);
            tv_receipt = view.findViewById(R.id.tv_receipt);
            tv_school_postage = view.findViewById(R.id.tv_school_postage);
            data = view.findViewById(R.id.data);

        }
    }

    OnReceiptClick onReceiptClick;
    OnCollectGoodsClick onCollectoodsClick;
    OnRefundClick onRefundClick;
    OnRefusingRefundClick onRefusingRefundClick;

    public void setOnCollectGoodsClick(OnCollectGoodsClick onCollectoodsClick) {//确认收货
        this.onCollectoodsClick = onCollectoodsClick;
    }

    public void setOnReceiptClick(OnReceiptClick onReceiptClick) {//接单
        this.onReceiptClick = onReceiptClick;
    }

    public void setOnRefundClick(OnRefundClick onRefundClick) {
        this.onRefundClick = onRefundClick;
    }

    public void setOnRefusingRefundClick(OnRefusingRefundClick onRefusingRefundClick) {
        this.onRefusingRefundClick = onRefusingRefundClick;
    }

    public interface OnReceiptClick {
        void OnReceiptClick(long orderId, int position);
    }

    public interface OnCollectGoodsClick {
        void OnCollectGoodsClick(long orderId, int position);
    }

    public interface OnRefundClick {
        void OnRefundClick(long orderId, int position);
    }

    public interface OnRefusingRefundClick {
        void OnRefusingRefundClick(long orderId, int position);
    }
}
