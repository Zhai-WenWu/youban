package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.UpdateSelectPersonNumListEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class SendInviationSelectPersonListAdapter extends BaseRecyclerAdapter {
    private List<BaseUser> baseUserList = new ArrayList<>();
    private List<Long> selectIdsList,beforeSelectIdsList;
    private int selectNum;
    private int maxSelectNum = 0;
    private boolean showTip = true;
    RequestOptions options;

    public SendInviationSelectPersonListAdapter() {
        options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon).error(R.mipmap.error_user_icon);
    }

    public void addList(List<BaseUser> baseUserList) {
        if (baseUserList == null) {
            return;
        }

        this.baseUserList.addAll(baseUserList);
        notifyDataSetChanged();
    }

    public void setBaseUserList(List<BaseUser> baseUserList) {
        this.baseUserList = baseUserList;
    }

    public List<BaseUser> getBaseUserList() {
        return baseUserList;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;

    }

    public void setSelectNum(int selectNum) {
        this.selectNum = selectNum;
    }



    public void setBeforeSelectIdsList(List<Long> beforeSelectIdsList) {
        if(beforeSelectIdsList!=null&&beforeSelectIdsList.size()>0){
            this.beforeSelectIdsList = beforeSelectIdsList;
            for (int i=0;i<baseUserList.size();i++){
                for (int j=0;j<beforeSelectIdsList.size();j++){
                    BaseUser baseUser=baseUserList.get(i);
                    if(beforeSelectIdsList.get(j)==baseUser.getUserId()){
                        baseUser.setCheck(true);
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return baseUserList == null ? 0 : baseUserList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new ItemViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.send_inviation_select_person_list_item_layout, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final BaseUser baseUser = baseUserList.get(position);
                Glide.with(App.getInstance()).asBitmap().load(baseUser.getUserIcon()).apply(options).into(itemViewHolder.memberUserImg);
                Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), itemViewHolder.memberUserImg);
                itemViewHolder.memberName.setText(baseUser.getNickName());
                itemViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {// 设置checkBox点击事件
                    @Override
                    public void onClick(View v) {
                        // 判断checkBox的勾选状态
                        if (itemViewHolder.checkBox.isChecked()) {
                            if (selectNum >= (10 - maxSelectNum)) {
//                                if (showTip) {
                                    Utils.showToastShortTime("您最多可以给10个人发送邀请函！");
//                                    showTip = false;
//                                }
                            } else {
                                // 手动设置每个checkBox的勾选状态
                                baseUserList.get(position).setCheck(true);
                                selectNum++;
                                Log.e("selectNum++",selectNum+"");
                                //发送消息
                                EventBus.getDefault().post(new UpdateSelectPersonNumListEvent(selectNum));
                            }
                        } else {
                            // 手动设置每个checkBox的勾选状态
                            baseUserList.get(position).setCheck(false);
                            selectNum--;
                            Log.e("selectNum--",selectNum+"");
                            EventBus.getDefault().post(new UpdateSelectPersonNumListEvent(selectNum));
                        }
                        notifyDataSetChanged();
                    }
                });
                itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemViewHolder.checkBox.performClick();
                    }
                });
                if (baseUserList.get(position).isCheck()) {
                    itemViewHolder.checkBox.setChecked(true);
                } else {
                    itemViewHolder.checkBox.setChecked(false);
                }
                if(position==0){
                    itemViewHolder.divider.setVisibility(View.VISIBLE);
                }else{
                    itemViewHolder.divider.setVisibility(View.GONE);
                }
            }
        }
    }


    class ItemViewHolder extends BaseViewHolder {
        RelativeLayout rootView;
        View divider,dividerBottom;
        TextView memberName;
        CheckBox checkBox;
        ImageView memberUserImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            divider=itemView.findViewById(R.id.divider_line);
            dividerBottom=itemView.findViewById(R.id.divider_bottom);
            checkBox = itemView.findViewById(R.id.checkbox_service);
            memberUserImg = itemView.findViewById(R.id.member_user_img_view);
            memberName = itemView.findViewById(R.id.member_name_view);
        }
    }

    public List<Long> getSelectIdsList() {
        if (selectIdsList == null) {
            selectIdsList = new ArrayList<Long>();
        }
        selectIdsList.clear();
        for (int i = 0; i < baseUserList.size(); i++) {
            BaseUser baseUser = baseUserList.get(i);
            if (baseUser.isCheck()) {
                selectIdsList.add(baseUser.getUserId());
            }
        }
        return selectIdsList;
    }

}
