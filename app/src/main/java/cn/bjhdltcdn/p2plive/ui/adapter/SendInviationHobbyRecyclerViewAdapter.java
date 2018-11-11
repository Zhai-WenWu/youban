package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 发送邀请函选择兴趣标签列表
 */

public class SendInviationHobbyRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list;
    private Activity mActivity;
    private List<Long> selectList;
    private RequestOptions options;
    private OnSelectClick onSelectClick;

    public SendInviationHobbyRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        selectList=new ArrayList<Long>();
        DisplayMetrics displayMetrics = App.getInstance().getResources().getDisplayMetrics();
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<HobbyInfo> list) {
        this.list = list;

    }

    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_hobby_type_select_recyclerview_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        try {
            if (getItemCount() > 0) {
                if (holder instanceof ImageHolder) {
                    final ImageHolder imageHolder = (ImageHolder) holder;
                    CheckBox checkBox=imageHolder.checkBox;
                    final ImageView photoView=imageHolder.serviceImg;
                    final TextView serviceName=imageHolder.serviceNameView;
                    final HobbyInfo hobbyInfo=list.get(position);
                    if(hobbyInfo!=null){
                         Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(photoView);
                         serviceName.setText(hobbyInfo.getHobbyName());
                    }
                    if(hobbyInfo.getIsCheck()==1){
                        imageHolder.selectImg.setVisibility(View.VISIBLE);
                        serviceName.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                    }else{
                        imageHolder.selectImg.setVisibility(View.GONE);
                        serviceName.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                    }
                    checkBox.setChecked(false);
                  checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                      @Override
                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          if(buttonView.isPressed()){
                              if(isChecked){
                                  imageHolder.selectImg.setVisibility(View.VISIBLE);
                                  serviceName.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
                                  Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(photoView);
                                  imageHolder.rootLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_10_stroke_333333));
                                  hobbyInfo.setIsCheck(1);
                              }else{
                                  imageHolder.selectImg.setVisibility(View.GONE);
                                  serviceName.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
                                  Glide.with(mActivity).load(hobbyInfo.getHobbyGrayImg()).apply(options).into(photoView);
                                  imageHolder.rootLayout.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_10_stroke_999999));
                                  hobbyInfo.setIsCheck(0);
                              }
                          }
                      }
                  });



                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        RelativeLayout rootLayout;
        ImageView serviceImg,selectImg;
        TextView serviceNameView;
        CheckBox checkBox;

        public ImageHolder(View itemView) {
            super(itemView);
            rootLayout= (RelativeLayout) itemView.findViewById(R.id.root_layout);
            checkBox= (CheckBox) itemView.findViewById(R.id.service_type_view);
            serviceImg = (ImageView) itemView.findViewById(R.id.service_view);
            serviceNameView= (TextView) itemView.findViewById(R.id.service_name_view);
            selectImg= (ImageView) itemView.findViewById(R.id.img_select);
        }

    }

    public List<Long> getSelectList() {
        selectList.clear();
        if(list!=null){
            for (int i=0;i<list.size();i++){
                HobbyInfo hobbyInfo=list.get(i);
                if(hobbyInfo.getIsCheck()==1){
                    selectList.add(hobbyInfo.getHobbyId());
                }
            }
        }
        return selectList;
    }

    public interface OnSelectClick{
        void onSelect();
    }



}