package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 活动详情圈子列表
 */

public class ActiveDetailOrgainRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<OrganizationInfo> list;
    private Activity mActivity;
    private RequestOptions options;

    public ActiveDetailOrgainRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        DisplayMetrics displayMetrics = App.getInstance().getResources().getDisplayMetrics();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<OrganizationInfo> list) {
        this.list = list;

    }



    public OrganizationInfo getItem(int position) {
        return list.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_hobby_type_select_recyclerview_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        try {
            if (getItemCount() > 0) {
                if (holder instanceof ImageHolder) {
                    final ImageHolder imageHolder = (ImageHolder) holder;
                    CheckBox checkBox=imageHolder.checkBox;
                    final ImageView photoView=imageHolder.serviceImg;
                    TextView serviceName=imageHolder.serviceNameView;
                    final OrganizationInfo organizationInfo=list.get(position);
                    if(organizationInfo!=null){
                         Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(options).into(photoView);
                         serviceName.setText(organizationInfo.getOrganName());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        ImageView serviceImg,selectImg;
        TextView serviceNameView;
        CheckBox checkBox;

        public ImageHolder(View itemView) {
            super(itemView);
            checkBox= (CheckBox) itemView.findViewById(R.id.service_type_view);
            checkBox.setVisibility(View.GONE);
            serviceImg = (ImageView) itemView.findViewById(R.id.service_view);
            serviceNameView= (TextView) itemView.findViewById(R.id.service_name_view);
            selectImg= (ImageView) itemView.findViewById(R.id.img_select);
        }

    }
}