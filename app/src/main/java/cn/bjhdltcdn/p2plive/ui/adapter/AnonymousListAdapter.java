package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.AnonymousUser;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

public class AnonymousListAdapter extends BaseRecyclerAdapter {
    private List<AnonymousUser> list;
    private DeleteAnonymousUserListener listener;

    public void setList(List<AnonymousUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public List<AnonymousUser> getList() {
        return list;
    }

    public void setDeleteListener(DeleteAnonymousUserListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public AnonymousUser getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    public void updateList(List<AnonymousUser> list) {
        if (list == null) {
            return;
        }

        if (this.list == null) {
            this.list = new ArrayList<>(1);
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
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_anonymous, viewGroup,false);
        return new AnonymousItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof AnonymousItemViewHolder) {
            final AnonymousItemViewHolder anonymousItemViewHolder = (AnonymousItemViewHolder) holder;

            AnonymousUser anonymousUser = getItem(position);
            ChatBaseUser chatBaseUser = anonymousUser.getChatBaseUser();
            if (chatBaseUser != null) {
                // 头像
                RequestOptions options = new RequestOptions().centerCrop().error(R.drawable.nm_item_icon);
                Glide.with(App.getInstance()).asBitmap().load(chatBaseUser.getUserIcon()).apply(options).into(anonymousItemViewHolder.headView);
                anonymousItemViewHolder.nameView.setText(chatBaseUser.getNickName());

            }

            // 删除
            anonymousItemViewHolder.deleteViewRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (anonymousItemViewHolder.swipeHorizontalMenuLayout.isMenuOpen()) {
                        anonymousItemViewHolder.swipeHorizontalMenuLayout.smoothCloseMenu();
                    }

                    if (listener != null) {
                        listener.deleteItem(position);
                    }
                }
            });

        }
    }


    class AnonymousItemViewHolder extends BaseViewHolder {

        public ImageView headView;
        public TextView nameView;
        public View deleteViewRight;
        public SwipeHorizontalMenuLayout swipeHorizontalMenuLayout;

        public AnonymousItemViewHolder(View itemView) {
            super(itemView);
            headView = itemView.findViewById(R.id.iv_icon);
            nameView = itemView.findViewById(R.id.tv_nickname);
            deleteViewRight = itemView.findViewById(R.id.smMenuViewRight);
            swipeHorizontalMenuLayout = itemView.findViewById(R.id.root_layout);

        }
    }


    public interface DeleteAnonymousUserListener {
        void deleteItem(int position);
    }

}
