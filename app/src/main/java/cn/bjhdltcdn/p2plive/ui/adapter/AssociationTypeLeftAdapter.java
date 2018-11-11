package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class AssociationTypeLeftAdapter extends BaseRecyclerAdapter {

    private List<HobbyInfo> list ;

    public AssociationTypeLeftAdapter(List<HobbyInfo> list) {
        this.list = list;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;

            String imgUrl = null;
            int resId = 0;
            if (list.get(position).isLocalSelectItem()) {
                imgUrl = list.get(position).getHobbyImg();
            }else {
                imgUrl = list.get(position).getHobbyGrayImg();
            }

            Glide.with(itemViewHolder.imageView).asBitmap().load(imgUrl).into(itemViewHolder.imageView);


        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(),R.layout.adapter_association_type_list_item_layout,null);
        ItemViewHolder holder = new ItemViewHolder(itemView);

        return holder;
    }


    static class ItemViewHolder extends BaseViewHolder{

        private ImageView imageView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }
}
