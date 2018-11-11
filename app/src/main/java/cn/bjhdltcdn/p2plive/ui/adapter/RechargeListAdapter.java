package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.UseTypeInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

public class RechargeListAdapter extends BaseRecyclerAdapter {
    private List<UseTypeInfo> list;

    public RechargeListAdapter() {
    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setList(List<UseTypeInfo> list) {
        this.list = list;
    }


    public UseTypeInfo getItem(int position) {
        // TODO Auto-generated method stub
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @return the mList
     */
    public List<UseTypeInfo> getList() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.recharge_list_item_layout, null);
        return new ItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {

            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                UseTypeInfo useTypeInfo = list.get(position);
                int amount = useTypeInfo.getAmount();
                int coinsNum = useTypeInfo.getCoinsNum();
                itemViewHolder.goldNumView.setText(String.format("%d金币", coinsNum+useTypeInfo.getOtherPresentation()));
                itemViewHolder.moneyNumView.setText(amount + "元");

            }

        }

    }

    class ItemViewHolder extends BaseViewHolder {
        TextView goldNumView, moneyNumView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            goldNumView = (TextView) itemView.findViewById(R.id.gold_num_view);
            moneyNumView = (TextView) itemView.findViewById(R.id.money_num_view);
        }
    }
}
