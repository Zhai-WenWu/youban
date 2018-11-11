package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by xiawenquan on 18/2/28.
 */

public class AssociationSelectListAdapter extends BaseRecyclerAdapter {

    private List<OrganizationInfo> list;

    private RequestOptions options;

    private BtnClickListener btnClickListener;

    //圈子是否参赛
    private boolean isMatch;
    //是否为搜索结果
    private boolean isSearch;

    public void setBtnClickListener(BtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
    }

    public List<OrganizationInfo> getList() {
        return list;
    }

    public void setList(List<OrganizationInfo> list, boolean isSearch) {
        this.isSearch = isSearch;
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

    public void onDestroy() {
        if (list != null) {
            list.clear();
        }
        list = null;

        if (btnClickListener != null) {
            btnClickListener = null;
        }

        if (options != null) {
            options = null;
        }
    }


    public AssociationSelectListAdapter(boolean isMatch) {
        this.isMatch = isMatch;
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

            //全匿名 自定义文字显示
            if (organizationInfo.getAtFirst()) {
                if (organizationInfo.getContentLimit() == 2) {
                    itemViewHolder.tv_assortment_title.setText("圈子权限-发布内容-圈子可见");
                } else {
                    itemViewHolder.tv_assortment_title.setText("圈子权限-发布内容-全部可见");
                }
                itemViewHolder.tv_assortment_title.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.tv_assortment_title.setVisibility(View.GONE);
            }

            if (position == 0) {
                itemViewHolder.tc_top_text.setVisibility(View.VISIBLE);
            } else {
                itemViewHolder.tc_top_text.setVisibility(View.GONE);
            }

            // 选择框 0 未选,1 已选
            itemViewHolder.radioButton.setChecked(organizationInfo.getIsSelect() == 1 ? true : false);

            // 圈子封面
            Glide.with(App.getInstance()).load(organizationInfo.getOrganImg()).apply(options).into(itemViewHolder.imageview);

            // 圈子昵称
            String organName = organizationInfo.getOrganName();
            if (!StringUtils.isEmpty(organName) && organName.length() > (isMatch ? 8 : 10)) {
                organName = organName.substring(0, (isMatch ? 8 : 10)) + "...";
            }
            itemViewHolder.nickeTextView.setText(organName);
            //参赛图标
            if (!isMatch || TextUtils.isEmpty(organizationInfo.getMatchUrl())) {
                itemViewHolder.matchImageView.setVisibility(View.GONE);
            } else {
                itemViewHolder.matchImageView.setVisibility(View.VISIBLE);
                Utils.ImageViewDisplayByUrl(organizationInfo.getMatchUrl(), itemViewHolder.matchImageView);
            }

            // 二级标签
            String secondHobbyName = organizationInfo.getSecondHobbyName();
            if (secondHobbyName == null) {
                itemViewHolder.secondHobbyNameView.setVisibility(View.GONE);
            } else {
                itemViewHolder.secondHobbyNameView.setText(secondHobbyName);
            }

            // 成员 | 帖子数量
            itemViewHolder.membertextView.setText("成员 " + organizationInfo.getMemberCount());
            itemViewHolder.postCountView.setText("帖子 " + organizationInfo.getPostCount());
            if (isSearch) {
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
            } else {
                itemViewHolder.btnView.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.association_select_list_item_layout, null);
        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        public CheckBox radioButton;
        public ImageView imageview;
        public TextView nickeTextView;
        public ImageView matchImageView;
        public TextView membertextView;
        public TextView postCountView;
        public TextView descTextview;
        public TextView tv_assortment_title;
        public TextView secondHobbyNameView;
        public TextView tc_top_text;
        public Button btnView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            tv_assortment_title = itemView.findViewById(R.id.tv_assortment_title);
            radioButton = itemView.findViewById(R.id.radio_view);
            imageview = itemView.findViewById(R.id.image_view);
            nickeTextView = itemView.findViewById(R.id.nicke_text_view);
            matchImageView = itemView.findViewById(R.id.iv_match);
            membertextView = itemView.findViewById(R.id.member_text_view);
            postCountView = itemView.findViewById(R.id.post_count_view);
            descTextview = itemView.findViewById(R.id.desc_text_view);
            btnView = itemView.findViewById(R.id.btn_view_1);
            tc_top_text = itemView.findViewById(R.id.tc_top_text);
            secondHobbyNameView = itemView.findViewById(R.id.secondhobbyname_text_view);

        }
    }

    public interface BtnClickListener {
        void onClick(OrganizationInfo organizationInfo, int position);
    }


}
