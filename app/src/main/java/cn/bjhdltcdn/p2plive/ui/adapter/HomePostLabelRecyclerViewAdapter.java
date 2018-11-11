package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.EvaluateCountInfo;
import cn.bjhdltcdn.p2plive.model.HobbyInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 首页帖子标签列表
 */

public class HomePostLabelRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<Object> list;
    private Activity mActivity;
    private RequestOptions options;
    private OnSelectClick onSelectClick;
    public HomePostLabelRecyclerViewAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        options = new RequestOptions()
                .fitCenter()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
    }

    public void setList(List<Object> list) {
        this.list = list;
        if (list != null) {
            this.list=list;
        }
    }


    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
    }

    public List<Object> getList() {
        return list;
    }




    public Object getItem(int position){
        return list == null ? null : list.get(position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_post_label_item_layout, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                Object o=list.get(position);
                if(o instanceof LabelInfo){
                    LabelInfo labelInfo= (LabelInfo) o;
                    ImageHolder holder1= (ImageHolder) holder;
                    holder1.labelTextView.setText(labelInfo.getLabelName());
                    if(labelInfo.isCheck()){
                        holder1.labelTextView.setTextColor(mActivity.getResources().getColor(R.color.color_ffb700));
                    }else{
                        holder1.labelTextView.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                    }
                }else if(o instanceof EvaluateCountInfo) {
                    EvaluateCountInfo evaluateCountInfo= (EvaluateCountInfo) o;
                    ImageHolder holder1= (ImageHolder) holder;
                    holder1.labelTextView.setText(evaluateCountInfo.getName()+"("+evaluateCountInfo.getTypeCount()+")");
                    if(evaluateCountInfo.isCheck()){
                        holder1.labelTextView.setTextColor(mActivity.getResources().getColor(R.color.color_ffb700));
                    }else{
                        holder1.labelTextView.setTextColor(mActivity.getResources().getColor(R.color.color_666666));
                    }
                }



            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        TextView labelTextView;

        public ImageHolder(View itemView) {
            super(itemView);
            labelTextView = (TextView) itemView.findViewById(R.id.tv_label);
        }

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