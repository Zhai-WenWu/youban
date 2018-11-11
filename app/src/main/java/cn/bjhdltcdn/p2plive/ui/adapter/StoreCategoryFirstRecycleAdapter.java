package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.annotation.IntRange;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.SecondLabelInfo;
import cn.bjhdltcdn.p2plive.model.UseTypeInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import de.hdodenhof.circleimageview.CircleImageView;

public class StoreCategoryFirstRecycleAdapter extends BaseRecyclerAdapter {
    private List<FirstLabelInfo> list;
    private FirstLabelInfo firstLabelInfo;
    private AppCompatActivity activity;
    public StoreCategoryFirstRecycleAdapter(AppCompatActivity activity) {
        this.activity=activity;
    }
    public boolean select;
    public long selectFirstLabelId;
    public int selectPosition;
    public List<SecondLabelInfo> selectSecondLabelInfoList;

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    public void setList(List<FirstLabelInfo> list) {
        this.list = list;
    }


    public FirstLabelInfo getItem(int position) {
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
    public List<FirstLabelInfo> getList() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.store_categoty_first_recycle_item_layout, null);
        return new ItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {

            if (holder instanceof ItemViewHolder) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                final FirstLabelInfo firstLabelInfo = list.get(position);
                itemViewHolder.firstLabelName.setText(firstLabelInfo.getFirstLabelName());
                Utils.ImageViewDisplayByUrl(firstLabelInfo.getImageUrl(),itemViewHolder.firstLabelImageView);
                StoreCategorySecondRecycleAdapter storeCategorySecondRecycleAdapter=new StoreCategorySecondRecycleAdapter(this);
                storeCategorySecondRecycleAdapter.setOnSelectClick(new StoreCategorySecondRecycleAdapter.OnSelectClick() {
                    @Override
                    public void onSelect(boolean isSelect,long firstLabelId,List<SecondLabelInfo> selectList) {
                            select=isSelect;
                            selectFirstLabelId=firstLabelId;
                            selectPosition=position;
                            selectSecondLabelInfoList=selectList;
                    }
                });
                storeCategorySecondRecycleAdapter.setList(firstLabelInfo.getList(),firstLabelInfo.getFirstLabelId());
                itemViewHolder.secondLabelRecycleView.setAdapter(storeCategorySecondRecycleAdapter);
                if(position==list.size()-1){
                    itemViewHolder.lineView.setVisibility(View.GONE);
                }else{
                    itemViewHolder.lineView.setVisibility(View.VISIBLE);
                }

            }

        }

    }

    public FirstLabelInfo getFirstLabelInfo() {
        if(firstLabelInfo==null){
            firstLabelInfo=new FirstLabelInfo();
        }
        firstLabelInfo=list.get(selectPosition);
        firstLabelInfo.setList(selectSecondLabelInfoList);
        return firstLabelInfo;
    }

    class ItemViewHolder extends BaseViewHolder {
        TextView firstLabelName;
        CircleImageView firstLabelImageView;
        RecyclerView secondLabelRecycleView;
        View lineView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            firstLabelName = (TextView) itemView.findViewById(R.id.first_label_view);
            firstLabelImageView = (CircleImageView) itemView.findViewById(R.id.first_label_image_view);
            secondLabelRecycleView = (RecyclerView) itemView.findViewById(R.id.label_second_recycle_view);
            ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(activity)
                    //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                    .setChildGravity(Gravity.TOP)
                    //whether RecyclerView can scroll. TRUE by default
                    .setScrollingEnabled(true)
                    //set maximum views count in a particular row
//                    .setMaxViewsInRow(2)
                    //set gravity resolver where you can determine gravity for item in position.
                    //This method have priority over previous one
                    .setGravityResolver(new IChildGravityResolver() {
                        @Override
                        public int getItemGravity(int position) {
                            return Gravity.LEFT;
                        }
                    })
                    //you are able to break row due to your conditions. Row breaker should return true for that views
//                    .setRowBreaker(new IRowBreaker() {
//                        @Override
//                        public boolean isItemBreakRow(@IntRange(from = 0) int position) {
//                            return position == 6 || position == 11 || position == 2;
//                        }
//                    })
                    //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                    //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT )
                    // whether strategy is applied to last row. FALSE by default
                    .withLastRow(true)
                    .build();
            secondLabelRecycleView.setLayoutManager(chipsLayoutManager);
            lineView=(View) itemView.findViewById(R.id.divider_line);
        }
    }
}
