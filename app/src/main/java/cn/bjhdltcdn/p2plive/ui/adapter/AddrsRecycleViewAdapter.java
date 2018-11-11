package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class AddrsRecycleViewAdapter extends BaseRecyclerAdapter {
    private List<AddressInfo> addressInfoList;
    public OnEditClick onEditClick;
    HashMap<Integer, Boolean> isSelect = new HashMap<Integer, Boolean>();
    private AddressInfo selectAddress;
    public AddrsRecycleViewAdapter() {
    }

    public void setOnEditClick(OnEditClick onEditClick) {
        this.onEditClick = onEditClick;
    }

    public void setAddressInfoList(List<AddressInfo> addressInfoList) {
        this.addressInfoList = addressInfoList;
    }

//    public void addList(List<AddressInfo> list) {
//        if (addressInfoList == null) {
//            return;
//        }
//        for (int i=0;i<list.size();i++){
//            AddressInfo addressInfo=list.get(i);
//            if(addressInfo.getIsDefault()==1){
//                isSelect.put(i, true);
//            }else{
//                isSelect.put(i, false);
//            }
//        }
//        this.addressInfoList.addAll(addressInfoList.size(), list);
//
//        notifyDataSetChanged();
//    }


    @Override
    public int getItemCount() {
        return addressInfoList == null ? 0 : addressInfoList.size();
    }


    public AddressInfo getItem(int position) {
        if(addressInfoList!=null)
        {
            return addressInfoList.get(position);
        }
        return null;
    }

    public List<AddressInfo> getAddressInfoList() {
        return addressInfoList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new ItemViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.addrs_recycle_item_layout, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final AddressInfo addressInfo=addressInfoList.get(position);
                itemViewHolder.nameTextView.setText(addressInfo.getContactName());
                itemViewHolder.addrsTextView.setText(addressInfo.getAddress());
                itemViewHolder.phoneNumTextView.setText(addressInfo.getPhoneNumber());

                if(addressInfo.getIsDefault()==1){
                    itemViewHolder.checkBox.setChecked(true);
                    itemViewHolder.checkBox.setEnabled(false);
                }else{
                    itemViewHolder.checkBox.setChecked(false);
                    itemViewHolder.checkBox.setEnabled(true);
                }
                itemViewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(buttonView.isPressed()&&isChecked){
                            for (int i = 0; i < addressInfoList.size(); i++) {
                               //把其他的checkbox设置为false
                                if (i != position) {
                                    AddressInfo addressInfo=addressInfoList.get(i);
                                    addressInfo.setIsDefault(0);
                                }
                                addressInfo.setIsDefault(1);
                            }
                            onEditClick.onDefault(addressInfo,position);
                        }
                    }
                });
                itemViewHolder.editImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onEditClick.onEdit(position,0);
                    }
                });

//                itemViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        itemViewHolder.checkBox.performClick();
//                    }
//                });

//                if (groupUser.getUserRole() == 2) {
//                    itemViewHolder.checkBox.setChecked(true);
//                } else {
//                    itemViewHolder.checkBox.setChecked(false);
//                }
            }
        }
    }


    class ItemViewHolder extends BaseViewHolder {
        RelativeLayout rootView;
        TextView nameTextView,addrsTextView,phoneNumTextView;
        CheckBox checkBox;
        ImageView editImg;

        public ItemViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.rootView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            addrsTextView = itemView.findViewById(R.id.addrs_text_view);
            phoneNumTextView = itemView.findViewById(R.id.phonenum_text_view);
            checkBox = itemView.findViewById(R.id.rules_checkbox);
            editImg = itemView.findViewById(R.id.edit_img);
        }
    }

//    public List<Long> getManagerIdsList() {
//        if (managerIdsList == null) {
//            managerIdsList = new ArrayList<Long>();
//        }
//        managerIdsList.clear();
//        for (int i = 0; i < groupUserList.size(); i++) {
//            GroupUser groupUser = groupUserList.get(i);
//            if (groupUser.getUserRole() == 2) {
//                managerIdsList.add(groupUser.getBaseUser().getUserId());
//            }
//        }
//        return managerIdsList;
//    }
//
//    public List<GroupUser> getManagerList() {
//        if (managerList == null) {
//            managerList = new ArrayList<GroupUser>();
//        }
//        managerList.clear();
//        for (int i = 0; i < groupUserList.size(); i++) {
//            GroupUser groupUser = groupUserList.get(i);
//            if (groupUser.getUserRole() == 2) {
//                managerList.add(groupUser);
//            }
//        }
//        return managerList;
//    }

    public interface  OnEditClick{
        void onEdit(int position,long addressId);
        void onDefault(AddressInfo addressInfo,int position);
    }



}
