package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by xiawenquan on 17/12/29.
 */

public class PrivateChatGiftAdapter extends BaseRecyclerAdapter {

    private List<Props> list;

    private int width;
    private int height;
    /**
     * 选中item的下标
     */
    private int itemSelectPosition = -1;


    public PrivateChatGiftAdapter(List<Props> list) {
        // 确保初始化状态是未选中状态
        for (Props props : list) {
            props.setSelected(false);
        }
        this.list = list;
    }

    /**
     * 默认选择
     */
    public void setDefaultSelectedItem(int position) {
        if (this.list != null && this.list.size() > 0) {
            this.list.get(position).setSelected(true);
        }
    }

    public List<Props> getList() {
        return list;
    }

    public void setList(List<Props> list) {
        this.list = list;
    }


    public int getItemSelectPosition() {
        return itemSelectPosition;
    }

    public void setItemSelectPosition(int itemSelectPosition) {
        this.itemSelectPosition = itemSelectPosition;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.private_message_gift_list_item_layout, null);
        DisplayMetrics displayMetrics = App.getInstance().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels / 4;
        height = Utils.dp2px(178) / 2;

        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(width, height);
        }

        layoutParams.width = width;
        layoutParams.height = height;

        itemView.setLayoutParams(layoutParams);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                Props props = list.get(position);
                // 礼物图片
                Glide.with(App.getInstance()).load(props.getUrl()).apply(new RequestOptions().fitCenter().placeholder(R.drawable.props_default)).into(itemViewHolder.propsImageView);

                // 礼物名称
                itemViewHolder.propsNameView.setText(props.getName());

                // 礼物金币
                if (props.getPrice() >0) {// 豆和金红包需要隐藏
                    itemViewHolder.propsGlodView.setVisibility(View.VISIBLE);
                    itemViewHolder.propsGlodView.setText(props.getPrice() + "金币");
                } else {
                    itemViewHolder.propsGlodView.setVisibility(View.GONE);
                }

                // 是否被选中
                itemViewHolder.mItemView.setBackground(null);
                if (props.isSelected()) {
                    itemViewHolder.mItemView.setBackgroundResource(R.drawable.shape_round_0_stroke_ffda44_solid_ffffff);
                }

                // 标识
                itemViewHolder.propsImgeTypeView.setVisibility(View.INVISIBLE);
//                if (props.getIsSale() != null && props.getIsSale() == 1) {
//                    itemViewHolder.propsImgeTypeView.setImageResource(R.drawable.props_type_zhe);
//                    itemViewHolder.propsImgeTypeView.setVisibility(View.VISIBLE);
//                } else if (props.getIsNew() != null && props.getIsNew() == 1) {
//                    itemViewHolder.propsImgeTypeView.setImageResource(R.drawable.props_type_xin);
//                    itemViewHolder.propsImgeTypeView.setVisibility(View.VISIBLE);
//                }

            }
        }


    }

    class ItemViewHolder extends BaseViewHolder {
        LinearLayout itemLayout;
        ImageView propsImageView;
        TextView propsNameView;
        TextView propsGlodView;
        View mItemView;
        ImageView propsImgeTypeView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            itemLayout= (LinearLayout) itemView.findViewById(R.id.item_layout);
            propsImageView = (ImageView) itemView.findViewById(R.id.props_image_view);
            propsNameView = (TextView) itemView.findViewById(R.id.props_name_view);
            propsGlodView = (TextView) itemView.findViewById(R.id.props_glod_view);
            propsImgeTypeView = (ImageView) itemView.findViewById(R.id.props_imge_type_view);

        }
    }
}
