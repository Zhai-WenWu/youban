package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 商品支付配送方式列表
 */

public class GoodsPayDeliverModeRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<LabelInfo> list;
    private Activity mActivity;
    // 用于记录每个RadioButton的状态，并保证只可选一个
    HashMap<Integer, Boolean> isSelect = new HashMap<Integer, Boolean>();
    private long selectLabelId;
    public GoodsPayDeliverModeRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<LabelInfo>();
    }

    public void setList(List<LabelInfo> list) {
        this.list = list;
        if (list != null) {
            this.list=list;
        }
        for (int i=0;i<list.size();i++){
//            if(i==0){
//                isSelect.put(i, true);
//            }else{
            isSelect.put(i, false);
//            }

        }
        if (list != null&&list.size()==1) {
            isSelect.put(0, true);
        }
    }

    public List<LabelInfo> getList() {
        return list;
    }



    public LabelInfo getItem(int position){
        return list == null ? null : list.get(position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.goods_pay_delivery_mode_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final CheckBox tabTextView = imageHolder.tabTextView;
                final LabelInfo labelInfo=list.get(position);
                tabTextView.setText(labelInfo.getLabelName());

                tabTextView.setChecked(isSelect.get(position));
                if(isSelect.get(position)){
                    labelInfo.setIsSelect(1);
                    tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_solid_ffee00));
                    tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                }else{
                    labelInfo.setIsSelect(0);
                    tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff));
                    tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_507daf));
                }
                tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        //先把链表中的数据取代掉
                        isSelect.put(position, isChecked);
                        if(isChecked) {
                            for (int i = 0; i < getItemCount(); i++) {
                                //把其他的checkbox设置为false
                                if (i != position) {
                                    isSelect.put(i, false);
                                }
                            }
                            //通知适配器更改
                            notifyDataSetChanged();
                        }else{
                            if(buttonView.isPressed()){
                                //只有人为点击的时候才做次功能
                                for (int i = 0; i < getItemCount(); i++) {
                                    //把所有的checkbox设置为false
                                    isSelect.put(i, false);
                                }
                                //通知适配器更改
                                notifyDataSetChanged();
                            }
                        }
                    }
                });

            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        CheckBox tabTextView;
        ImageView tabImg;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = (CheckBox) itemView.findViewById(R.id.tv_label);
            tabImg = (ImageView) itemView.findViewById(R.id.label_img);
        }

    }

    public long getSelectServiceTypeId() {
        for (int i=0;i<list.size();i++){
            LabelInfo labelInfo=list.get(i);
            if(labelInfo.getIsSelect()==1){
                selectLabelId=labelInfo.getLabelId();
                break;
            }
        }
        return selectLabelId;
    }


    public void onDestroy(){
        if (mActivity != null) {
            mActivity = null;
        }

        if (list != null) {
            list.clear();
        }
        list = null;
    }

}