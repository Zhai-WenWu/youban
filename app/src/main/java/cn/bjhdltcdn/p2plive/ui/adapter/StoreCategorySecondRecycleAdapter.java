package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoreCategorySecondRecycleAdapter extends BaseRecyclerAdapter {
    private List<SecondLabelInfo> list;
    private List<SecondLabelInfo> selectList;
    private OnSelectClick onSelectClick;
    private StoreCategoryFirstRecycleAdapter storeCategoryFirstRecycleAdapter;
    private long firstLabelId;

    public StoreCategorySecondRecycleAdapter(StoreCategoryFirstRecycleAdapter storeCategoryFirstRecycleAdapter) {
        selectList=new ArrayList<SecondLabelInfo>();
        this.storeCategoryFirstRecycleAdapter=storeCategoryFirstRecycleAdapter;
    }

    public StoreCategorySecondRecycleAdapter() {
    }

    public void setOnSelectClick(OnSelectClick onSelectClick) {
        this.onSelectClick = onSelectClick;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setList(List<SecondLabelInfo> list,long firstLabelId) {
        this.list = list;
        this.firstLabelId=firstLabelId;
    }

    public void setList(List<SecondLabelInfo> list) {
        this.list = list;
    }

    public SecondLabelInfo getItem(int position) {
        // TODO Auto-generated method stub
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @return the mList
     */
    public List<SecondLabelInfo> getList() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.store_categoty_second_recycle_item_layout, null);
        return new ItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {

            if (holder instanceof ItemViewHolder) {
                final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final SecondLabelInfo secondLabelInfo = list.get(position);
                itemViewHolder.secondLabelName.setText("#"+secondLabelInfo.getSecondLabelName());
                if(storeCategoryFirstRecycleAdapter==null){
                    itemViewHolder.secondLabelName.setEnabled(false);
                    itemViewHolder.secondLabelName.setPadding(0,0,Utils.dp2px(9),0);
                    itemViewHolder.secondLabelName.setTextSize(10);
                }else{
                    itemViewHolder.secondLabelName.setPadding(0,0,Utils.dp2px(33),Utils.dp2px(23));
                    itemViewHolder.secondLabelName.setTextSize(12);
                }
                itemViewHolder.secondLabelName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(buttonView.isPressed()) {
                            if(storeCategoryFirstRecycleAdapter.select&&storeCategoryFirstRecycleAdapter.selectFirstLabelId!=firstLabelId){
                                Utils.showToastShortTime("只能选一种标签里面的服务类型");
                                return;
                            }else {
                                //只有人为点击的时候才做此功能
                                if (secondLabelInfo.getIsSelect() == 0) {
                                    if (getSelectList().size() >= 3) {
                                        Utils.showToastShortTime("最多选择3个");
                                        return;
                                    }
                                    secondLabelInfo.setIsSelect(1);
                                    itemViewHolder.secondLabelName.setTextColor(App.getInstance().getResources().getColor(R.color.color_ffb700));
                                } else {
                                    secondLabelInfo.setIsSelect(0);
                                    itemViewHolder.secondLabelName.setTextColor(App.getInstance().getResources().getColor(R.color.color_666666));
                                }
                                List<SecondLabelInfo> selectSecondLabelInfoList=getSelectList();
                                if (selectSecondLabelInfoList.size() > 0) {
                                    onSelectClick.onSelect(true,firstLabelId,selectSecondLabelInfoList);
                                } else {
                                    onSelectClick.onSelect(false,firstLabelId,selectSecondLabelInfoList);
                                }
                            }
                        }
                    }
                });

            }

        }

    }

    public List<SecondLabelInfo> getSelectList() {
        selectList.clear();
        for (int i=0;i<list.size();i++){
            SecondLabelInfo secondLabelInfo=list.get(i);
            if(secondLabelInfo.getIsSelect()==1){
                selectList.add(secondLabelInfo);
            }
        }
        return selectList;
    }

    class ItemViewHolder extends BaseViewHolder {
        CheckBox secondLabelName;
        public ItemViewHolder(View itemView) {
            super(itemView);
            secondLabelName = (CheckBox) itemView.findViewById(R.id.second_label_view);
        }
    }

    interface OnSelectClick{
        void onSelect(boolean isSelect,long firstLableId,List<SecondLabelInfo> selectList);
    }

}
