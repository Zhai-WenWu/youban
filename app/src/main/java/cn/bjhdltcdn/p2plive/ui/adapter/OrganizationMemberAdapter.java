package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OrganMember;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class OrganizationMemberAdapter extends BaseRecyclerAdapter {

    private List<OrganMember> list;
    private RequestOptions options;

    public OrganizationMemberAdapter(List<OrganMember> list) {
        this.list = list;
        options = new RequestOptions();
        options.placeholder(R.mipmap.error_user_icon).centerCrop();

    }

    public List<OrganMember> getList() {
        return list;
    }

    public void setList(List<OrganMember> list) {
        this.list = list;
    }

    public void addItemAll(List<OrganMember> list) {
        if (this.list == null) {
            return;
        }
        int positionStart = this.list.size() - 1;
        this.list.addAll(list);
        notifyItemRangeInserted(positionStart, getItemCount() - list.size());

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;


            OrganMember organMember = list.get(position);
            Glide.with(itemViewHolder.imageView).asBitmap().apply(options).load(organMember.getBaseUser().getUserIcon()).into(itemViewHolder.imageView);

            String text = null;
            switch (organMember.getUserRole()) {
                case 1:
                    text = "圈主";
                    itemViewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    itemViewHolder.textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
                    break;

                case 2:
                    text = "管理员";
                    itemViewHolder.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                    itemViewHolder.textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_666666));
                    break;

                default:
                    if (organMember.getBaseUser() != null) {
                        text = organMember.getBaseUser().getNickName();
                    }
                    break;

            }


            itemViewHolder.textView.setText(text);


        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.adapter_organization_member_liat_item_layout, null);
        return new ItemViewHolder(itemView);
    }

    static class ItemViewHolder extends BaseViewHolder {

        ImageView imageView;
        TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.circle_image_view);
            textView = itemView.findViewById(R.id.text_view);

        }
    }
}
