package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 店铺搜索标签列表
 */

public class StoreCategoryRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<FirstLabelInfo> list;
    private Activity mActivity;
    private List<FirstLabelInfo> selectList;
    private RequestOptions options;
    private OnSelectClick onSelectClick;

    public StoreCategoryRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<FirstLabelInfo>();
        selectList=new ArrayList<FirstLabelInfo>();
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<FirstLabelInfo> list) {
        this.list = list;
        if (list != null) {
            this.list=list;
        }
    }

    public void setSelectList(List<FirstLabelInfo> categoryList){
        if (this.list!=null&&categoryList != null) {
                for (int j=0;j<list.size();j++){
                    FirstLabelInfo label=list.get(j);
                    label.setIsCheck(0);
                    for (int i=0;i<categoryList.size();i++){
                        FirstLabelInfo selectLabel=categoryList.get(i);
                        if(selectLabel.getFirstLabelId()==label.getFirstLabelId()){
                            label.setIsCheck(1);
                            list.set(j,label);
                            break;
                        }else{
                            label.setIsCheck(0);
                        }
                    }
                }
        }else{
            for (int j=0;j<list.size();j++) {
                FirstLabelInfo label = list.get(j);
                label.setIsCheck(0);
            }
        }
        notifyDataSetChanged();
    }



    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
    }

    public List<FirstLabelInfo> getList() {
        return list;
    }

    public void reset(){
        if(list!=null)
        {
            for (int i=0;i<list.size();i++){
                list.get(i).setIsCheck(0);
            }
        }
        notifyDataSetChanged();
    }


    public FirstLabelInfo getItem(int position){
        return list == null ? null : list.get(position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.category_label_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final CheckBox tabTextView = imageHolder.tabTextView;
                final FirstLabelInfo labelInfo=list.get(position);
                tabTextView.setText(labelInfo.getFirstLabelName());
                Glide.with(mActivity).load(labelInfo.getImageUrl()).apply(options).into(imageHolder.tabImg);

                    if(labelInfo.getIsCheck()==1){
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_ec5e2a));
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_6_solid_64fbebe2));
                        imageHolder.delectImageView.setVisibility(View.VISIBLE);
                        tabTextView.setChecked(true);
                    }else{
                        tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_6_solid_fafafa));
                        imageHolder.delectImageView.setVisibility(View.GONE);
                        tabTextView.setChecked(false);
                    }

                tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(buttonView.isPressed()) {
                            //只有人为点击的时候才做此功能
                            if (isChecked) {
                                labelInfo.setIsCheck(1);
                                tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_ec5e2a));
                                tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_6_solid_64fbebe2));
                                imageHolder.delectImageView.setVisibility(View.VISIBLE);
                            } else {
                                labelInfo.setIsCheck(0);
                                tabTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                                tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_6_solid_fafafa));
                                imageHolder.delectImageView.setVisibility(View.GONE);

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
        ImageView delectImageView;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = (CheckBox) itemView.findViewById(R.id.tv_label);
            tabImg = (ImageView) itemView.findViewById(R.id.label_img);
            delectImageView= itemView.findViewById(R.id.delete_view);
        }

    }

    public List<FirstLabelInfo> getSelectList() {
        selectList.clear();
        for (int i=0;i<list.size();i++){
            FirstLabelInfo firstLabelInfo=list.get(i);
            if(firstLabelInfo.getIsCheck()==1&&firstLabelInfo.getFirstLabelId()!=0){
                selectList.add(firstLabelInfo);
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