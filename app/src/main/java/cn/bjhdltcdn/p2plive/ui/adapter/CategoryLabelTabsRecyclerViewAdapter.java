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
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 检索线下活动标签列表
 */

public class CategoryLabelTabsRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list;
    private Activity mActivity;
    private List<Long> selectLabelInfoList;
    private List<HobbyInfo> selectList;
    private RequestOptions options;
    public CategoryLabelTabsRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        list = new ArrayList<HobbyInfo>();
        selectLabelInfoList=new ArrayList<Long>();
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


    public List<HobbyInfo> getList() {
        return list;
    }

    public void reset(){
        for (int i=0;i<list.size();i++){
            HobbyInfo hobbyInfo=list.get(i);
            if(hobbyInfo.getIsCheck()==1){
                hobbyInfo.setIsCheck(0);
            }
        }
        notifyDataSetChanged();
    }

    public void selectAll(){
        for (int i=0;i<list.size();i++){
            HobbyInfo hobbyInfo=list.get(i);
            hobbyInfo.setIsCheck(1);
        }
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
                if(hobbyInfo.getIsCheck()==1){
                    tabTextView.setChecked(true);
                    if(hobbyInfo.getHobbyId()!=0){
                        Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(imageHolder.tabImg);
                        imageHolder.tabImg.setVisibility(View.VISIBLE);
                    }else{
                        imageHolder.tabImg.setVisibility(View.GONE);
                    }

                }else{
                    tabTextView.setChecked(false);
                    if(hobbyInfo.getHobbyId()!=0){
                        Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(imageHolder.tabImg);
                        imageHolder.tabImg.setVisibility(View.VISIBLE);
                    }else{
                        imageHolder.tabImg.setVisibility(View.GONE);
                    }

                }


                tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(buttonView.isPressed()) {
                            //只有人为点击的时候才做此功能
                            if (isChecked) {
                                tabTextView.setChecked(true);
                                hobbyInfo.setIsCheck(1);
                                if (hobbyInfo.getHobbyId() == 0) {
                                    //选中不限   取消其他选项的选中状态
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getHobbyId() != 0) {
                                            list.get(i).setIsCheck(0);

                                        }
                                    }
                                    notifyDataSetChanged();
                                } else {
                                    //选中其他选项  取消不限选项的选中状态
                                    for (int i = 0; i < list.size(); i++) {
                                        if (list.get(i).getHobbyId() == 0 && list.get(i).getIsCheck() == 1) {
                                            list.get(i).setIsCheck(0);
//                                            notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                    notifyDataSetChanged();
                                }
                            } else {
                                if (hobbyInfo.getHobbyId() != 0){
                                    tabTextView.setChecked(false);
                                    hobbyInfo.setIsCheck(0);
                                    //其他选项都不选情况下   自动选择不限
                                    int count=0;
                                    HobbyInfo hobby = null;
                                    for (int i = 0; i < list.size(); i++) {
                                        if(list.get(i).getHobbyId()==0){
                                            hobby=list.get(i);
                                        }
                                        if (list.get(i).getHobbyId()!= 0 && list.get(i).getIsCheck() == 0) {
                                            count++;
                                        }
                                    }
                                    if(count==list.size()-1){
                                        hobby.setIsCheck(1);
//                                        notifyDataSetChanged();
                                    }
                                    notifyDataSetChanged();
                                }else{
                                    hobbyInfo.setIsCheck(1);
                                    notifyDataSetChanged();
                                }
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
            tabTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.selector_textbg_condition_radio));
        }

    }

    public List<Long> getSelectLabelInfoList() {
        selectLabelInfoList.clear();
        for (int i=0;i<list.size();i++){
            HobbyInfo hobbyInfo=list.get(i);
            if(hobbyInfo.getIsCheck()==1&&hobbyInfo.getHobbyId()!=0){
                selectLabelInfoList.add(hobbyInfo.getHobbyId());
            }
        }
        return selectLabelInfoList;
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

    public interface SelectListener{
        void select(List<Long> selectLabelInfoList);
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