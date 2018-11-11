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
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 创建店铺类别标签列表
 */

public class CreateShopDeliveryModeRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<LabelInfo> list;
    private Activity mActivity;
    private List<LabelInfo> selectList;
    private RequestOptions options;
    private boolean enable=true;
    private OnSelectClick onSelectClick;
    public CreateShopDeliveryModeRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<LabelInfo>();
        selectList=new ArrayList<LabelInfo>();
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<LabelInfo> list) {
        if (list != null) {
            this.list=list;
        }
    }

    public void resetList(List<LabelInfo> selectlist){
        if(selectlist!=null){
            for (int i=0;i<selectlist.size();i++){
                LabelInfo selectlabelInfo=selectlist.get(i);
                for (int j=0;j<list.size();j++){
                    LabelInfo labelInfo=list.get(j);
                    if(selectlabelInfo.getLabelId()==labelInfo.getLabelId()){
                        labelInfo.setIsSelect(1);
                    }
                }
            }
        }
    }



    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
    }

    public List<LabelInfo> getList() {
        return list;
    }

    public void setEnable(boolean enable){
        this.enable=enable;
        notifyDataSetChanged();
    }



    public LabelInfo getItem(int position){
        return list == null ? null : list.get(position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.create_shop_category_item_layout, null));
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

//                if(enable){
//                    tabTextView.setEnabled(true);
                    if(labelInfo.getIsSelect()==1){
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_solid_ffee00));
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                    }else{
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff));
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_666666));

                    }
//                }else
//                {
//                    tabTextView.setEnabled(false);
//                    tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_999999_solid_e6e6e6));
//                }


                tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(buttonView.isPressed()&&enable) {
                            //只有人为点击的时候才做此功能
                               if (labelInfo.getIsSelect()==0) {
                                        labelInfo.setIsSelect(1);
                                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_solid_ffee00));
                                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));

                                } else {
                                    labelInfo.setIsSelect(0);
                                    tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff));
                                    tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_666666));

                                }
//                            onSelectClick.onSelect();
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

    public List<LabelInfo> getSelectList() {
        selectList.clear();
        for (int i=0;i<list.size();i++){
            LabelInfo labelInfo=list.get(i);
            if(labelInfo.getIsSelect()==1&&labelInfo.getLabelId()!=0){
                selectList.add(labelInfo);
            }
        }
        return selectList;
    }

    public interface OnSelectClick{
        void onSelect();
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