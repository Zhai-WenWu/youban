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
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.model.NearOrganInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//
///**
// * 附近圈子列表
// */
//
//public class NearbyOrganRecyclerViewAdapter extends BaseRecyclerAdapter {
//    private List<NearOrganInfo> list;
//    private Activity mActivity;
//    private RequestOptions options;
//    private OnClick onClick;
//
//    public NearbyOrganRecyclerViewAdapter(Activity mActivity) {
//        this.mActivity = mActivity;
//        list = new ArrayList<NearOrganInfo>();
//        options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//    }
//
//    public void setList(List<NearOrganInfo> list) {
//        this.list = list;
//    }
//
//    public void addList(List<NearOrganInfo> list) {
//        this.list.addAll(list);
//    }
//
//
//    public NearOrganInfo getItem(int position) {
//        return list == null ? null : list.get(position);
//    }
//
//    public void setOnClick(OnClick onClick) {
//        this.onClick = onClick;
//    }
//
//    public List<NearOrganInfo> getList() {
//        return list;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        BaseViewHolder baseViewHolder = null;
//        baseViewHolder = new ActiveHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.nearby_organ_recycle_item_layout, null));
//        return baseViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        if (getItemCount() > 0) {
//            if (holder instanceof ActiveHolder) {
//
//                final ActiveHolder activeHolder = (ActiveHolder) holder;
//                ImageView organImg = activeHolder.organImg;
//                TextView organNameTextView = activeHolder.organNameTextView;
//                NearOrganInfo nearOrganInfo = list.get(position);
//                OrganizationInfo organizationInfo = nearOrganInfo.getOrganizationInfo();
//                Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(options).into(organImg);
//                organNameTextView.setText(organizationInfo.getOrganName());
//                activeHolder.memberNumTextView.setText("成员  " + organizationInfo.getMemberCount());
//                activeHolder.postNumTextView.setText("帖子  " + organizationInfo.getPostCount());
//                activeHolder.joinNumTextView.setText("你附近有" + nearOrganInfo.getNearPerson() + "人参加了这个圈子");
//                int userRole = organizationInfo.getUserRole();
//                final int joinLimit = organizationInfo.getJoinLimit();
//                if (userRole == 0) {
//                    //用户不在圈子
//                    activeHolder.joinTextView.setVisibility(View.VISIBLE);
//                    activeHolder.joinTextView.setEnabled(true);
//                    if (joinLimit == 1) {
//                        activeHolder.joinTextView.setText("加入");
//                    } else {
//                        activeHolder.joinTextView.setText("加入");
//                    }
//                    activeHolder.joinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
//                    activeHolder.joinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_solid_ffee00));
//                } else if (userRole == 4) {
//                    activeHolder.joinTextView.setText("申请中");
//                    activeHolder.joinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                    activeHolder.joinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
//                    activeHolder.joinTextView.setEnabled(false);
//                } else {
//                    //用户在圈子
//                    activeHolder.joinTextView.setText("已加入");
//                    activeHolder.joinTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                    activeHolder.joinTextView.setBackground(App.getInstance().getResources().getDrawable(R.drawable.shape_round_10_stroke_d8d8d8));
//                    activeHolder.joinTextView.setEnabled(false);
//                }
//                activeHolder.joinTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        onClick.onJoinClick(position, joinLimit);
//
//                    }
//                });
//
//                if (position == 0) {
//                    activeHolder.headerView.setVisibility(View.VISIBLE);
//                } else {
//                    activeHolder.headerView.setVisibility(View.GONE);
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
//        View headerView;
//        ImageView organImg;
//        TextView organNameTextView, memberNumTextView, postNumTextView, joinNumTextView, joinTextView;
//
//        public ActiveHolder(View itemView) {
//            super(itemView);
//            headerView = itemView.findViewById(R.id.top_header_view);
//            organImg = (ImageView) itemView.findViewById(R.id.organ_img);
//            organNameTextView = (TextView) itemView.findViewById(R.id.organ_name_text);
//            memberNumTextView = (TextView) itemView.findViewById(R.id.organ_user_num_text);
//            postNumTextView = (TextView) itemView.findViewById(R.id.organ_post_num_text);
//            joinNumTextView = (TextView) itemView.findViewById(R.id.nearby_user_num_text);
//            joinTextView = (TextView) itemView.findViewById(R.id.orgain_join_text);
//        }
//
//    }
//
//    public interface OnClick {
//        void onJoinClick(int position, int type);
//    }
//
//    public void onDestroy() {
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