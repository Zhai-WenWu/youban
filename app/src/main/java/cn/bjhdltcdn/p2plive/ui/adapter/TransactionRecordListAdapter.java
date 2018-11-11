package cn.bjhdltcdn.p2plive.ui.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.TransactionRecord;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHAI on 2017/12/18.
 */

public class TransactionRecordListAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private List<TransactionRecord> list;

    public TransactionRecordListAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public void setList(List<TransactionRecord> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        //TransactionRecordListAdapter.ViewHolder holder = new TransactionRecordListAdapter.ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.transaction_record_recyclerview_layout, false));
        View view = LayoutInflater.from(App.getInstance()).inflate(R.layout.transaction_record_recyclerview_layout, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ViewHolder itemViewHolder = (ViewHolder) holder;
        TransactionRecord transactionRecord = list.get(position);
        String balance = String.format(mActivity.getResources().getString(R.string.str_num_transaction_balance), transactionRecord.getIncome());
        itemViewHolder.tv_in_out_num_2.setText(balance);
        switch (transactionRecord.getType()) {
            case 1:
                itemViewHolder.tv_title.setText("充值");
                itemViewHolder.tv_in_out_num.setText("收入: +");
                break;
            case 2:
                itemViewHolder.tv_title.setText("赠送他人");
                itemViewHolder.tv_in_out_num.setText("赠送他人: -");
                break;
            case 3:
                itemViewHolder.tv_title.setText("他人赠送");
                itemViewHolder.tv_in_out_num.setText("赠送他人: +");
                break;
        }
        itemViewHolder.tv_time.setText(transactionRecord.getTime());
        //String balanceStr = String.format(mActivity.getResources().getString(R.string.str_num_balance), transactionRecord.getTotalAmount());
        itemViewHolder.tv_total_num.setText(transactionRecord.getTotalAmount() + "金币");

    }

    class ViewHolder extends BaseViewHolder {
        TextView tv_title;
        TextView tv_time;
        TextView tv_in_out_num;
        TextView tv_total_num;
        TextView tv_in_out_num_2;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_in_out_num = itemView.findViewById(R.id.tv_in_out_num);
            tv_total_num = itemView.findViewById(R.id.tv_total_num);
            tv_in_out_num_2 = itemView.findViewById(R.id.tv_in_out_num_2);
        }
    }
}
