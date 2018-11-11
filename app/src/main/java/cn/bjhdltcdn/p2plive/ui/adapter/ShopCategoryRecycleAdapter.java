package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by huwenhua on 2018/4/17.
 */

public class ShopCategoryRecycleAdapter extends BaseRecyclerAdapter {


    private List<LabelInfo> mList = new ArrayList<>();
    private Activity activity;

    public ShopCategoryRecycleAdapter(Activity activity) {
        this.activity=activity;
    }

    public void setList(List<LabelInfo> list) {
        this.mList = list;
    }

    public void setDistanceSort(long labelId){
        if(mList!=null)
        {
            for (int i=0;i<mList.size();i++){
                if(mList.get(i).getLabelId()==labelId){
                    mList.get(i).setCheck(true);
                }else{
                    mList.get(i).setCheck(false);
                }
            }
        }
        notifyDataSetChanged();
    }


    public List<LabelInfo> getmList() {
        return mList;
    }

    public void addList(List<LabelInfo> list) {
        mList.addAll(list);
    }

    public LabelInfo getItem(int position){
        if(mList!=null)
        {
            return mList.get(position);
        }else{
            return null;
        }
    }

    public void clearList(){
        if(mList!=null)
        {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public void reset(){
        if(mList!=null)
        {
            for (int i=0;i<mList.size();i++){
                mList.get(i).setCheck(false);
            }
        }
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        BaseViewHolder viewHolder = null;
         viewHolder = new BuyerViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_shop_category_recycle_item, null));
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final ShopCategoryRecycleAdapter.BuyerViewHolder viewHolder = (ShopCategoryRecycleAdapter.BuyerViewHolder) holder;
        final LabelInfo labelInfo=mList.get(position);
        if(labelInfo!=null){
            viewHolder.labelTextView.setText(labelInfo.getLabelName());
            if(labelInfo.isCheck())
            {
                viewHolder.labelTextView.setTextColor(activity.getResources().getColor(R.color.color_ec5e2a));
                viewHolder.labelTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_round_6_solid_64fbebe2));
                viewHolder.delectImageView.setVisibility(View.VISIBLE);
                viewHolder.labelTextView.setChecked(true);
            }else{
                viewHolder.labelTextView.setTextColor(activity.getResources().getColor(R.color.color_333333));
                viewHolder.labelTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_round_6_solid_fafafa));
                viewHolder.delectImageView.setVisibility(View.GONE);
                viewHolder.labelTextView.setChecked(false);

            }

            viewHolder.labelTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(buttonView.isPressed()) {
                        //只有人为点击的时候才做此功能
                        if (isChecked) {
                            labelInfo.setCheck(true);
                            operationClick.operationClick(labelInfo.getLabelId());
                            for (int i = 0; i < mList.size(); i++) {
                                if (mList.get(i).getLabelId() != labelInfo.getLabelId()) {
                                    mList.get(i).setCheck(false);
                                }
                            }
                            notifyDataSetChanged();
                        } else {
                            labelInfo.setCheck(false);
                            viewHolder.labelTextView.setTextColor(activity.getResources().getColor(R.color.color_333333));
                            viewHolder.labelTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_round_6_solid_fafafa));
                            viewHolder.delectImageView.setVisibility(View.GONE);
                        }

                    }else{
                        if (!isChecked) {
                            labelInfo.setCheck(false);
                            viewHolder.labelTextView.setTextColor(activity.getResources().getColor(R.color.color_333333));
                            viewHolder.labelTextView.setBackground(activity.getResources().getDrawable(R.drawable.shape_round_6_solid_fafafa));
                            viewHolder.delectImageView.setVisibility(View.GONE);
                        }
                    }
                }
            });
        }

    }


    public class BuyerViewHolder extends BaseViewHolder {
        CheckBox labelTextView;
        ImageView delectImageView;
        public BuyerViewHolder(View view) {
            super(view);
            labelTextView = view.findViewById(R.id.category_textview);
            delectImageView= view.findViewById(R.id.delete_view);

        }
    }

    OperationClick operationClick;

    public void setOperationClick(OperationClick operationClick) {
        this.operationClick = operationClick;
    }

    public interface OperationClick {
        void operationClick(long labelId);
    }
}
