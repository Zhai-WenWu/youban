package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ExchangeRecord;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.TransactionRecord;
import cn.bjhdltcdn.p2plive.ui.activity.OrderDetailActivity;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;

/**
 * 明细列表
 */

public class WithdrawCashRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<TransactionRecord> list;
    private Activity mActivity;
    private RequestOptions options;

    public WithdrawCashRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<TransactionRecord>();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<TransactionRecord> list) {
        this.list = list;
    }

    public void addList(List<TransactionRecord> list){
        this.list.addAll(list);
    }


    public TransactionRecord getItem(int position){
        return list == null ? null : list.get(position);
    }

    public List<TransactionRecord> getList() {
        return list;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
            View convertView = null;
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.withdraw_cash_recycle_item_layout, null);
            return new ShopHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
                    if(holder instanceof ShopHolder){
                        final ShopHolder shopHolder = (ShopHolder) holder;
                        RelativeLayout.LayoutParams layoutParams= (RelativeLayout.LayoutParams) shopHolder.moneyTextView.getLayoutParams();
                        if(position==0){
                            layoutParams.setMargins(Utils.dp2px(10),Utils.dp2px(9),0,0);
                        }else{
                            layoutParams.setMargins(Utils.dp2px(10),0,0,0);
                        }
                        final TransactionRecord transactionRecord=list.get(position);
                        if(transactionRecord!=null){
                            //3--->转入(收到转账),4--->提现,5--->红包,6--->转出(转给他人)7---订单收入8--退款
                            String str="";
                            switch (transactionRecord.getType()){
                                case 3:
                                    str="收到"+transactionRecord.getPresenterNickName()+"转账"+transactionRecord.getIncome()+"元";
                                    break;
                                case 4:
                                    str="发起提现"+transactionRecord.getIncome()+"元";
                                    break;
                                case 5:
                                    str="红包收入"+transactionRecord.getIncome()+"元";
                                    break;
                                case 6:
                                    str="向"+transactionRecord.getPresenterNickName()+"支出店员工资"+transactionRecord.getIncome()+"元";
                                    break;
                                case 7:
                                    str="订单收入"+transactionRecord.getIncome()+"元";
                                    break;
                                case 8:
                                    str="退款金额"+transactionRecord.getIncome()+"元";
                                    break;
                            }
                            shopHolder.moneyTextView.setText(str);
                            shopHolder.timeTextView.setText(transactionRecord.getTime());
                        }
                        shopHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(transactionRecord!=null&&transactionRecord.getType()==7){
                                    Intent intent = new Intent(mActivity, OrderDetailActivity.class);
                                    intent.putExtra(Constants.Fields.ORDER_ID, transactionRecord.getToUserId());
                                    intent.putExtra(Constants.Fields.POSITION, position);
                                    intent.putExtra(Constants.Fields.TYPE, 1);
                                    mActivity.startActivity(intent);
                                }
                            }
                        });
                    }


        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    class ShopHolder extends BaseViewHolder {
        RelativeLayout rootLayout;
        TextView moneyTextView,timeTextView;

        public ShopHolder(View itemView) {
            super(itemView);
            rootLayout= (RelativeLayout) itemView.findViewById(R.id.root_layout);
            moneyTextView = (TextView) itemView.findViewById(R.id.money_text_view);
            timeTextView= (TextView) itemView.findViewById(R.id.time_text_view);
        }

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