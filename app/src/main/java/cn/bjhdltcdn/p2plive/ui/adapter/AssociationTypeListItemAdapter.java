package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * Created by xiawenquan on 17/11/28.
 */

public class AssociationTypeListItemAdapter extends BaseRecyclerAdapter {

    private List<OrganizationInfo> list;

    private RequestOptions options;

    private BtnClickListener btnClickListener;

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public List<OrganizationInfo> getList() {
        return list;
    }

    public void setList(List<OrganizationInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addList(List<OrganizationInfo> list) {
        if (list == null || this.list == null) {
            return;
        }

        this.list.addAll(list);
        notifyDataSetChanged();

    }


    public AssociationTypeListItemAdapter() {
        options = new RequestOptions().placeholder(R.mipmap.error_bg).centerCrop();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {

            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            final OrganizationInfo organizationInfo = list.get(position);

            // 圈子封面
            Glide.with(App.getInstance()).load(organizationInfo.getOrganImg()).apply(options).into(itemViewHolder.imageview);

            // 圈子昵称
            itemViewHolder.nickeTextView.setText(organizationInfo.getOrganName());

            // 二级标签
            String secondHobbyName = organizationInfo.getSecondHobbyName();
            if (secondHobbyName == null) {
                itemViewHolder.secondhobbyname_text_view.setVisibility(View.GONE);
            } else {
                itemViewHolder.secondhobbyname_text_view.setText(secondHobbyName);
            }

            // 成员 | 帖子数量
            itemViewHolder.membertextView.setText("成员 " + organizationInfo.getMemberCount());
            itemViewHolder.postCountView.setText("帖子 " + organizationInfo.getPostCount());

            // 加入按钮
            itemViewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (btnClickListener != null) {
                        btnClickListener.onClick(organizationInfo, position);
                    }

                }
            });

            // 圈子申请状态
            itemViewHolder.btnView.setText("加入");
            itemViewHolder.btnView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
            itemViewHolder.btnView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
            itemViewHolder.btnView.setVisibility(View.VISIBLE);
            if (organizationInfo.getUserRole() == 4) {
                itemViewHolder.btnView.setText("申请中");
                itemViewHolder.btnView.setOnClickListener(null);
                itemViewHolder.btnView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                itemViewHolder.btnView.setBackgroundResource(R.drawable.shape_round_5_stroke_d8d8d8_solid_ffffff);
                itemViewHolder.btnView.setOnClickListener(null);
            } else if (organizationInfo.getUserRole() == 1 || organizationInfo.getUserRole() == 2 || organizationInfo.getUserRole() == 3) {
                itemViewHolder.btnView.setText("已加入");
                itemViewHolder.btnView.setBackgroundResource(R.drawable.shape_round_10_stroke_d8d8d8);
                itemViewHolder.btnView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
                itemViewHolder.btnView.setOnClickListener(null);
            }


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.association_type_list_item_adapter_layout, null);
        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        public ImageView imageview;
        public TextView nickeTextView;
        public TextView membertextView;
        public TextView postCountView;
        public TextView descTextview;
        public TextView secondhobbyname_text_view;
        public Button btnView;

        public ItemViewHolder(View itemView) {
            super(itemView);


            imageview = itemView.findViewById(R.id.image_view);
            nickeTextView = itemView.findViewById(R.id.nicke_text_view);
            membertextView = itemView.findViewById(R.id.member_text_view);
            postCountView = itemView.findViewById(R.id.post_count_view);
            descTextview = itemView.findViewById(R.id.desc_text_view);
            btnView = itemView.findViewById(R.id.btn_view_1);
            secondhobbyname_text_view = itemView.findViewById(R.id.secondhobbyname_text_view);

        }
    }

    public interface BtnClickListener {
        void onClick(OrganizationInfo organizationInfo, int position);
    }

}
