package cn.bjhdltcdn.p2plive.ui.adapter;

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
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.BlackUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

public class BlackListAdapter extends BaseRecyclerAdapter {
    private List<BlackUser> list;
    private DeleteBlackUserListener listener;

    public void setList(List<BlackUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<BlackUser> getList() {
        return list;
    }

    public void setDeleteListener(DeleteBlackUserListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    public void updateList(List<BlackUser> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        if (list != null && list.size() > position && position > -1) {
            list.remove(position);
            notifyDataSetChanged();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_black, viewGroup,false);
        return new BlackItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof BlackItemViewHolder) {
            final BlackItemViewHolder blackItemViewHolder = (BlackItemViewHolder) holder;

            Object object = getItem(position);
            if (object instanceof BlackUser) {
                BlackUser blackUser = (BlackUser) object;
                BaseUser baseUser = blackUser.getBaseUser();
                if (baseUser != null) {
                    // 头像
                    RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
                    Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(options).into(blackItemViewHolder.headView);
                    blackItemViewHolder.nameView.setText(baseUser.getNickName());

                }

                // 删除
                blackItemViewHolder.deleteViewRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.deleteItem(position);
                        }
                    }
                });

                if (position==list.size()-1){
                    blackItemViewHolder.tv_bottom_text.setVisibility(View.VISIBLE);
                }else {
                    blackItemViewHolder.tv_bottom_text.setVisibility(View.GONE);
                }

            }


        }
    }


    class BlackItemViewHolder extends BaseViewHolder {

        public ImageView headView;
        public TextView nameView;
        public TextView tv_bottom_text;
        public TextView deleteViewRight;

        public BlackItemViewHolder(View itemView) {
            super(itemView);
            headView = itemView.findViewById(R.id.user_image_view);
            nameView = itemView.findViewById(R.id.nick_name_view);
            deleteViewRight = itemView.findViewById(R.id.smMenuViewRight);
            tv_bottom_text = itemView.findViewById(R.id.tv_bottom_text);

        }
    }


    public interface DeleteBlackUserListener {
        void deleteItem(int position);
    }

}
