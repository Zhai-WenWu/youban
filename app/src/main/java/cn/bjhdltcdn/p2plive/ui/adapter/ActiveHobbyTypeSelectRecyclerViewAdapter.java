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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 老板发单的服务列表
 */

public class ActiveHobbyTypeSelectRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<HobbyInfo> list;
    private Activity mActivity;
    private List<HobbyInfo> selectList;
    private RequestOptions options;
    private OnSelectClick onSelectClick;

    public ActiveHobbyTypeSelectRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        selectList=new ArrayList<HobbyInfo>();
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
                    TextView serviceName=imageHolder.serviceNameView;
                    final HobbyInfo hobbyInfo=list.get(position);
                    if(hobbyInfo!=null){
                         Glide.with(mActivity).load(hobbyInfo.getHobbyImg()).apply(options).into(photoView);
                         serviceName.setText(hobbyInfo.getHobbyName());
                    }
                  checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                      @Override
                      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                          if(isChecked){
                              imageHolder.selectImg.setVisibility(View.VISIBLE);
                              hobbyInfo.setIsCheck(1);
                          }else{
                              imageHolder.selectImg.setVisibility(View.GONE);
                              hobbyInfo.setIsCheck(0);
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
        ImageView serviceImg,selectImg;
        TextView serviceNameView;
        CheckBox checkBox;

        public ImageHolder(View itemView) {
            super(itemView);
            checkBox= (CheckBox) itemView.findViewById(R.id.service_type_view);
            serviceImg = (ImageView) itemView.findViewById(R.id.service_view);
            serviceNameView= (TextView) itemView.findViewById(R.id.service_name_view);
            selectImg= (ImageView) itemView.findViewById(R.id.img_select);
        }

    }

    public List<HobbyInfo> getSelectList() {
        selectList.clear();
        for (int i=0;i<list.size();i++){
            HobbyInfo hobbyInfo=list.get(i);
            if(hobbyInfo.getIsCheck()==1){
                selectList.add(hobbyInfo);
            }
        }
        return selectList;
    }

    public interface OnSelectClick{
        void onSelect(int position);
    }



}