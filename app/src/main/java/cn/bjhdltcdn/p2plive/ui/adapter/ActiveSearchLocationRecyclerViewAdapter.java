//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.app.Activity;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.baidu.mapapi.search.core.PoiInfo;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//
///**
// * 附近圈子列表
// */
//
//public class ActiveSearchLocationRecyclerViewAdapter extends BaseRecyclerAdapter {
//    private List<PoiInfo> list;
//    private Activity mActivity;
//    private int selectPosition=-1;
//
//    public ActiveSearchLocationRecyclerViewAdapter(Activity mActivity) {
//        this.mActivity = mActivity;
//    }
//
//    public void setList(List<PoiInfo> list) {
//        this.list = list;
//    }
//
//    public void addList(List<PoiInfo> list){
//        this.list.addAll(list);
//    }
//
//
//    public PoiInfo getItem(int position){
//        return list == null ? null : list.get(position);
//    }
//
//    public void setSelectItem(int position){
//        selectPosition=position;
//        notifyDataSetChanged();
//    }
//
//
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        ActiveHolder baseViewHolder = new ActiveHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.active_location_recycle_item_layout, null));
//        return baseViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        if (getItemCount() > 0) {
//            if(holder instanceof ActiveHolder){
//                final ActiveHolder activeHolder = (ActiveHolder) holder;
//                PoiInfo poiInfo=list.get(position);
//                activeHolder.addrs_tv.setText(poiInfo.name);
//                activeHolder.location_tv.setText(poiInfo.address);
//                if(selectPosition==position){
//                    activeHolder.selectImg.setVisibility(View.VISIBLE);
//                }else{
//                    activeHolder.selectImg.setVisibility(View.INVISIBLE);
//                }
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//
//    class ActiveHolder extends BaseViewHolder {
//        TextView addrs_tv,location_tv;
//        ImageView selectImg;
//
//        public ActiveHolder(View itemView) {
//            super(itemView);
//            addrs_tv = (TextView) itemView.findViewById(R.id.location_addr_text);
//            location_tv= (TextView) itemView.findViewById(R.id.location_text);
//            selectImg= (ImageView) itemView.findViewById(R.id.select_img);
//        }
//
//    }
//
//    public void onDestroy(){
//        if (mActivity != null) {
//            mActivity = null;
//        }
//
//        if (list != null) {
//            list.clear();
//        }
//        list = null;
//    }
//
//}