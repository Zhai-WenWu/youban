package cn.bjhdltcdn.p2plive.ui.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by xiawenquan on 16/7/28.
 */
public class MyPropsListAdapter extends BaseRecyclerAdapter {
    private List<MyProps> mList;
    private int totalGoldNum;
    /**
     * 显示类型
     * 1 收到礼物
     * 2 送出礼物
     * 3 房间内本场收获显示
     */
    private int type;

    public MyPropsListAdapter(int type) {
        this.type = type;
    }

    /**
     * 第一页更新数据
     *
     * @param list
     */
    public void addData(List<MyProps> list) {
        if (list == null) {
            return;
        }

        this.mList = list;
        notifyDataSetChanged();

    }

    /**
     * 分页更新数据
     *
     * @param list
     */
    public void update(List<MyProps> list) {
        if (list == null) {
            return;
        }

        if (this.mList == null) {
            this.mList = new ArrayList<>();
        }

        this.mList.addAll(list);

        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new PropsHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_my_props, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof PropsHolder) {
                PropsHolder propsHolder = (PropsHolder) holder;
                MyProps myProps = mList.get(position);

                if (myProps.getIsExchange() == 1) {
                    propsHolder.tv_already.setVisibility(View.VISIBLE);
                } else {
                    propsHolder.tv_already.setVisibility(View.GONE);
                }
                //道具图片
                String propsUrl = myProps.getPropUrl();
                Utils.ImageViewDisplayByUrl(propsUrl, propsHolder.img_gift);
                // 礼物道具数量
                propsHolder.tv_num_gift.setText("x" + (myProps.getGiftMultiple() < 1 ? 1 : myProps.getGiftMultiple()));
                if (type == 3) {
                    propsHolder.tv_num_gold.setText(myProps.getTotalGold() + "金币");
                } else {
                    propsHolder.tv_num_gold.setText(myProps.getGoldNum() + "金币");
                }


//                // 金币
//                int goldNum = myProps.getGoldNum();
//                int salePrice = myProps.getSalePrice();
//
//                if (myProps.getIsSale() == 1) {
//                    propsHolder.gift_type_view.setImageResource(R.drawable.props_type_zhe);
//                    propsHolder.gift_type_view.setVisibility(View.VISIBLE);
//                }
//                if (myProps.getIsNew() == 1) {
//                    propsHolder.gift_type_view.setImageResource(R.drawable.props_type_xin);
//                    propsHolder.gift_type_view.setVisibility(View.VISIBLE);
//                }
//
//                if (myProps.getIsSale() == 0 && myProps.getIsNew() == 0) {
//                    propsHolder.gift_type_view.setVisibility(View.INVISIBLE);
//                }

                //时间
                String time = myProps.getTime();
                propsHolder.tv_time.setText(DateUtils.showYYYYMMddHHmm(time));

                // 人名称
                String temp = "赠送";
                if (type == 2) { // 送出
                    temp = "送给";
                    SpannableStringBuilder style2 = new SpannableStringBuilder(temp + myProps.getReceiveNickname());
                    style2.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 2, 2 + (myProps.getReceiveNickname() + "").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    propsHolder.tv_nickname_sender.setText(style2);
                } else {
                    String sendNickname = myProps.getSendNickname();
                    if (TextUtils.isEmpty(sendNickname)) {
                        if (myProps.getBaseUser() != null) {
                            sendNickname = myProps.getBaseUser().getNickName();
                        }
                    }
                    SpannableStringBuilder style1 = new SpannableStringBuilder(sendNickname + temp);
                    style1.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), 0, sendNickname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    propsHolder.tv_nickname_sender.setText(style1);
                }
            }
        }
    }


    class PropsHolder extends BaseViewHolder {
        ImageView img_gift;
        TextView tv_num_gift, tv_num_gold, tv_time, tv_nickname_sender, tv_already;

        public PropsHolder(View itemView) {
            super(itemView);
            tv_num_gift = itemView.findViewById(R.id.tv_num_gift);
            tv_num_gold = itemView.findViewById(R.id.tv_num_gold);
            img_gift = itemView.findViewById(R.id.img_gift);
            tv_already = itemView.findViewById(R.id.tv_already);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_nickname_sender = itemView.findViewById(R.id.tv_nickname_sender);
        }
    }


}
