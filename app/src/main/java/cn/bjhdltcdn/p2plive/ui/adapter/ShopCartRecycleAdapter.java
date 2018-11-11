package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.ui.fragment.GoodsRecycleFragment;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by huwenhua on 2018/4/17.
 */

public class ShopCartRecycleAdapter extends BaseRecyclerAdapter {


    private List<ProductDetail> mList = new ArrayList<>();
    private Activity activity;
    private int total;
    private BigDecimal totalMoney=new BigDecimal(0);


    public ShopCartRecycleAdapter(Activity activity) {
        this.activity=activity;
    }

    public void setList(List<ProductDetail> list) {
        this.mList = list;
    }

    public List<ProductDetail> getmList() {
        return mList;
    }

    public void addList(List<ProductDetail> list) {
        mList.addAll(list);
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

    public void clearList(){
        if(mList!=null)
        {
            mList.clear();
        }
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        BaseViewHolder viewHolder = null;
         viewHolder = new BuyerViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_shop_cart_recycle_item, null));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
            final ShopCartRecycleAdapter.BuyerViewHolder viewHolder = (ShopCartRecycleAdapter.BuyerViewHolder) holder;
            final ProductDetail productDetail= mList.get(position);
            if(productDetail!=null){
                final ProductInfo productInfo=productDetail.getProductInfo();
                viewHolder.tv_goods_name.setText(productInfo.getProductName());
                final int productRemainTotal=productInfo.getProductRemainTotal();
                final BigDecimal salePrice=productInfo.getSalePrice();
                BigDecimal productPrice=productInfo.getProductPrice();
                viewHolder.tv_sale_price.setText("¥"+salePrice);
                if(salePrice.equals(productPrice)){
                    viewHolder.tv_product_price.setVisibility(View.GONE);
                    viewHolder.product_price_text.setVisibility(View.GONE);
                }else{
                    viewHolder.tv_product_price .setText(productPrice+"");
                    viewHolder.tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
                    viewHolder.tv_product_price.setVisibility(View.VISIBLE);
                    viewHolder.product_price_text.setVisibility(View.VISIBLE);
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
                        //加
                        String numStr=viewHolder.tv_num.getText().toString();
                        if(StringUtils.isEmpty(numStr)){
                            viewHolder.tv_num.setText(1+"");
                        }else{
                            int num= Integer.parseInt(numStr);
                            if(num<productRemainTotal){
                                viewHolder.tv_num.setText((num+1)+"");
                            }else{
                                return;
                            }
                        }
                        total++;
                        productInfo.setProductNum(Integer.parseInt(viewHolder.tv_num.getText().toString()));
                        totalMoney=totalMoney.add(salePrice);
                        viewHolder.reduceImg.setVisibility(View.VISIBLE);
                        viewHolder.tv_num.setVisibility(View.VISIBLE);
                        EventBus.getDefault().post(new UpdateShopChartListEvent(2,productDetail,total,totalMoney));
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
                        totalMoney=totalMoney.subtract(salePrice);
                        productInfo.setProductNum(Integer.parseInt(viewHolder.tv_num.getText().toString()));
                        EventBus.getDefault().post(new UpdateShopChartListEvent(2,productDetail,total,totalMoney));
                        if(Integer.parseInt(viewHolder.tv_num.getText().toString())<=0){
                            mList.remove(position);
                            notifyDataSetChanged();
                            operationClick.operationClick(total);
                        }
                    }
                });

                if(position!=0){
                    viewHolder.line.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.line.setVisibility(View.INVISIBLE);
                }

            }
    }


    public class BuyerViewHolder extends BaseViewHolder {
        ImageView plusImg,reduceImg;
        TextView tv_goods_name,tv_sale_price,product_price_text,tv_product_price,tv_num;
        View line;

        public BuyerViewHolder(View view) {
            super(view);
            line= view.findViewById(R.id.line_view);
            tv_goods_name = view.findViewById(R.id.goods_name_textview);
            tv_sale_price = view.findViewById(R.id.sale_price_text_view);
            product_price_text= view.findViewById(R.id.product_price_text);
            tv_product_price = view.findViewById(R.id.product_price_text_view);
            tv_product_price.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG); //中划线
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
        void operationClick(int total);
    }
}
