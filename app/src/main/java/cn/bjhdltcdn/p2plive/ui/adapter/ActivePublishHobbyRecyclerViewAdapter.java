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
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 检索线下活动标签列表
 */

public class ActivePublishHobbyRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list;
    private Activity mActivity;
    private List<HobbyInfo> selectList;
    private RequestOptions options;
    private boolean enable=true;
    private OnSelectClick onSelectClick;
    public ActivePublishHobbyRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<HobbyInfo>();
        selectList=new ArrayList<HobbyInfo>();
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;
        if (list != null) {
            this.list=list;
        }
    }


    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
    }

    public List<HobbyInfo> getList() {
        return list;
    }

    public void setEnable(boolean enable){
        this.enable=enable;
        notifyDataSetChanged();
    }



    public HobbyInfo getItem(int position){
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
                final HobbyInfo hobbyInfo=list.get(position);
                tabTextView.setText(hobbyInfo.getHobbyName());

                if(enable){
                    tabTextView.setEnabled(true);
                    if(hobbyInfo.getIsCheck()==1){
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_solid_ffee00));
                        Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.tabImg);
                        imageHolder.tabImg.setVisibility(View.VISIBLE);

                    }else{
                        tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff));
                        Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(imageHolder.tabImg);
                        imageHolder.tabImg.setVisibility(View.VISIBLE);

                    }
                }else
                {
                    tabTextView.setEnabled(false);
                    tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_999999_solid_e6e6e6));
                    Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(imageHolder.tabImg);
                    imageHolder.tabImg.setVisibility(View.VISIBLE);
                }


                tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(buttonView.isPressed()&&enable) {
                            //只有人为点击的时候才做此功能
                            if (isChecked) {
                                hobbyInfo.setIsCheck(1);
                                tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_solid_ffee00));
                                Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.tabImg);
                            } else {
                                hobbyInfo.setIsCheck(0);
                                tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_20_stroke_d8d8d8_solid_ffffff));
                                Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(imageHolder.tabImg);

                            }
                            onSelectClick.onSelect();
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

    public List<HobbyInfo> getSelectList() {
        selectList.clear();
        for (int i=0;i<list.size();i++){
            HobbyInfo hobbyInfo=list.get(i);
            if(hobbyInfo.getIsCheck()==1&&hobbyInfo.getHobbyId()!=0){
                selectList.add(hobbyInfo);
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