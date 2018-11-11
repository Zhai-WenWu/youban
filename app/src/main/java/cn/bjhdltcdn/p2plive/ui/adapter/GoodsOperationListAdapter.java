package cn.bjhdltcdn.p2plive.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;

public class GoodsOperationListAdapter extends BaseAdapter {
    private List<String> list;
    private int unEnablePosition=-1;
    private int productStatus;//
    public GoodsOperationListAdapter() {
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list != null ? list.size() : 0;
    }

    @Override
    public String getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setUnEnablePosition(int position){
        unEnablePosition=position;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.goods_operation_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.operationName = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.lineView = convertView.findViewById(R.id.divider_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.operationName.setText(list.get(position));
        if(productStatus==1){
            if(position!=0){
                viewHolder.operationName.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
            }else{
                viewHolder.operationName.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
            }
        }else {
            if (unEnablePosition == position) {
                viewHolder.operationName.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
            } else {
                viewHolder.operationName.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));

            }
        }
        if (position == 0) {
            viewHolder.lineView.setVisibility(View.GONE);
        } else {
            viewHolder.lineView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView operationName;
        View lineView;
    }

}
