package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

public class AllAssociationListAdapter extends BaseRecyclerAdapter {


    private List<HobbyInfo> list;
    private RequestOptions options;

    public AllAssociationListAdapter() {
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg);

    }

    public List<HobbyInfo> getList() {
        return list;
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;
        notifyDataSetChanged();
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

            HobbyInfo hobbyInfo = list.get(position);

            if (hobbyInfo.getHobbyId() == -1) {
                Glide.with(App.getInstance()).asBitmap().load(R.mipmap.school_item_icon).apply(options).into(itemViewHolder.imageView);
            }else if (hobbyInfo.getHobbyId() == -2) {
                Glide.with(App.getInstance()).asBitmap().load(R.mipmap.school_orgain_item_icon).apply(options).into(itemViewHolder.imageView);
            }else {
                // 图片
                Glide.with(App.getInstance()).asBitmap().load(hobbyInfo.getHobbyImg()).apply(options).into(itemViewHolder.imageView);
            }

            // 文字
            itemViewHolder.textView.setText(hobbyInfo.getHobbyName());

            // 描述
            itemViewHolder.textView2.setText(hobbyInfo.getHobbyDesc());

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(),R.layout.all_association_list_item_layout,null);

        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        public ImageView imageView;
        public TextView textView;
        public TextView textView2;

        public View itemView;

        public ItemViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            imageView = itemView.findViewById(R.id.image_view);
            textView = itemView.findViewById(R.id.text_view);
            textView2 = itemView.findViewById(R.id.text_view_2);

        }
    }
}
