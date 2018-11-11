package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OccupationInfo;
import cn.bjhdltcdn.p2plive.model.SchoolInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by ZHUDI on 2017/9/22.
 */

public class SchoolListAdapter extends BaseRecyclerAdapter {
    private List<SchoolInfo> list_school;


    public void setDate(List<SchoolInfo> list_school) {
        this.list_school = list_school;
        notifyDataSetChanged();
    }

    public List<SchoolInfo> getList_school() {
        return list_school;
    }

    @Override
    public int getItemCount() {
        return list_school != null ? list_school.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_school, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                SchoolInfo schoolInfo = list_school.get(position);
                if (!TextUtils.isEmpty(schoolInfo.getSchoolName())) {
                    itemViewHolder.tv_school.setText(schoolInfo.getSchoolName());
                }
            }
        }

    }

    public void addDate(List<SchoolInfo> schoolList) {
        list_school.addAll(schoolList);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends BaseViewHolder {

        TextView tv_school;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_school = itemView.findViewById(R.id.tv_school);
        }
    }

}
