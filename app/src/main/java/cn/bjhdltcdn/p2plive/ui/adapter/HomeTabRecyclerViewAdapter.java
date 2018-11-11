package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.CountInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 首页推荐圈子列表
 */

public class HomeTabRecyclerViewAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private List<CountInfo> countInfoList;

    public HomeTabRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setCountInfoList(List<CountInfo> countInfoList) {
        this.countInfoList = countInfoList;
    }

    public List<CountInfo> getCountInfoList() {
        return countInfoList;
    }

    public CountInfo getItem(int position){
        return countInfoList == null ? null : countInfoList.get(position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        TabHolder holder = new TabHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_tab_recycle_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof TabHolder) {
                final TabHolder tabHolder = (TabHolder) holder;
                ImageView popImg = tabHolder.popImg;
                View tabIndicatorView = tabHolder.tabIndicatorView;
                TextView tabNameTextView = tabHolder.tabNameTextView;
                CountInfo countInfo=countInfoList.get(position);
                tabNameTextView.setText(countInfo.getContent());
                if(countInfo.getType()==0){
                    tabIndicatorView.setVisibility(View.VISIBLE);
                }else{
                    tabIndicatorView.setVisibility(View.INVISIBLE);
                }
                int updateCount=countInfo.getUpdateCount();
                if(updateCount>0){
                    popImg.setVisibility(View.VISIBLE);
                }else{
                    popImg.setVisibility(View.GONE);
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return countInfoList == null ? 0 : countInfoList.size();
    }

    class TabHolder extends BaseViewHolder {
        ImageView popImg;
        View tabIndicatorView;
        TextView tabNameTextView;

        public TabHolder(View itemView) {
            super(itemView);
            popImg = (ImageView) itemView.findViewById(R.id.tab_pop_view);
            tabIndicatorView= (View) itemView.findViewById(R.id.tab_indicator_view);
            tabNameTextView = (TextView) itemView.findViewById(R.id.tab_name_view);

        }

    }

    public void onDestroy(){
        if (mActivity != null) {
            mActivity = null;
        }
    }


}