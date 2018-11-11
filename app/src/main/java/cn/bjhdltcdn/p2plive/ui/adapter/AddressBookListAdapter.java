package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiawenquan on 17/12/1.
 */

public class AddressBookListAdapter extends BaseRecyclerAdapter {

    private List<AddressBook> list;
    private RequestOptions options;
    private UserIconClickListener userIconClickListener;
    /**
     * 1 群详情点击加号添加好友到群
     */
    private int type;

    public void setUserIconClickListener(UserIconClickListener userIconClickListener) {
        this.userIconClickListener = userIconClickListener;
    }

    public AddressBookListAdapter() {
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
    }

    public AddressBookListAdapter(int type) {
        this.type = type;
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon);
    }

    public List<AddressBook> getList() {
        return list;
    }

    public void setList(List<AddressBook> list) {
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
            final AddressBook addressBook = list.get(position);
            if (type == 1 && position != 0) {
                itemViewHolder.checkBox.setVisibility(View.VISIBLE);
                itemViewHolder.checkBox.setChecked(addressBook.getIsSelect() == 1);
            }
            if (position == 0) {
                options.placeholder(R.mipmap.group_item_icon);
                itemViewHolder.arrowRightView.setVisibility(View.VISIBLE);
            } else if (position == 1 && addressBook.getNickName().equals("匿名好友")) {
                options.placeholder(R.drawable.nm_item_icon);
                itemViewHolder.arrowRightView.setVisibility(View.VISIBLE);
            } else {
                options.placeholder(R.mipmap.error_group_icon);
            }

            // 头像
            Glide.with(App.getInstance()).asBitmap().load(addressBook.getUserIcon()).apply(options).into(itemViewHolder.circleImageView);

            // 昵称
            itemViewHolder.textView.setText(addressBook.getNickName());
            itemViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (userIconClickListener != null) {
                        userIconClickListener.onClick(addressBook);
                    }
                }
            });

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {

        View itemView = View.inflate(App.getInstance(), R.layout.address_book_list_adapter_layout, null);

        return new ItemViewHolder(itemView);
    }


    static class ItemViewHolder extends BaseViewHolder {

        CircleImageView circleImageView;
        TextView textView;
        ImageView arrowRightView;
        CheckBox checkBox;

        public ItemViewHolder(View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circle_image_view);
            textView = itemView.findViewById(R.id.nicke_text_view);
            arrowRightView = itemView.findViewById(R.id.arrow_right_view);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }

    public interface UserIconClickListener {
        void onClick(AddressBook addressBook);
    }
}
