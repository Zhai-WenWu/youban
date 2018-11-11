//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.event.AssociationBlackListEvent;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.OrganMember;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import de.hdodenhof.circleimageview.CircleImageView;
//
//
///**
// * 圈子黑名单
// */
//public class AssociationBlackListAdapter extends BaseRecyclerAdapter {
//    private List<OrganMember> mList;
//    /**
//     * 显示类型
//     * 1 禁言的用户
//     * 2 踢出的用户
//     */
//    private int type;
//    private final int TYPE_ITEM = 1, TYPE_HEADER = 2;
//
//    public AssociationBlackListAdapter(int type) {
//        this.type = type;
//    }
//
//    public List<OrganMember> getmList() {
//        return mList;
//    }
//
//    /**
//     * 第一页更新数据
//     *
//     * @param list
//     */
//    public void setData(List<OrganMember> list) {
//        if (list == null || list.size() == 0) {
//            return;
//        }
//        OrganMember organMember = new OrganMember();
//        organMember.setMemberId(-1);
//        list.add(0,organMember);
//        this.mList = list;
//        notifyDataSetChanged();
//
//    }
//
//    /**
//     * 分页更新数据
//     *
//     * @param list
//     */
//    public void addData(List<OrganMember> list) {
//        if (list == null) {
//            return;
//        }
//
//        if (this.mList == null) {
//            this.mList = new ArrayList<>();
//        }
//
//        this.mList.addAll(list);
//
//        notifyDataSetChanged();
//
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (mList.get(position).getMemberId() <= 0) {
//            return TYPE_HEADER;
//        } else {
//            return TYPE_ITEM;
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mList != null ? mList.size() : 0;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        if (itemType == TYPE_HEADER) {
//            return new BlackHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.header_list_association_black_list, viewGroup, false));
//        } else {
//            return new BlackHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_association_black_list, viewGroup, false));
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        if (getItemCount() > 0) {
//            if (holder instanceof BlackHolder) {
//                BlackHolder blackHolder = (BlackHolder) holder;
//                final OrganMember organMember = mList.get(position);
//                if (getItemViewType(position) == TYPE_HEADER) {
//                    if (type == 1) {
//                        blackHolder.tv_header.setText("以下是被禁言的用户，不能在圈子发布帖子和评论。\n" + "点击“解除禁言”，Ta将可以在圈子内发布帖子和评论。");
//                    } else {
//                        blackHolder.tv_header.setText("以下是被踢出圈子的用户，不能重新加入本圈子。\n" + "点击“解除限制”，Ta将可以重新申请加入圈子。");
//                    }
//                } else {
//                    BaseUser baseUser = organMember.getBaseUser();
//                    if (baseUser != null) {
//                        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), blackHolder.img_icon);
//                        blackHolder.tv_nickname.setText(baseUser.getNickName());
//                        blackHolder.tv_age.setText(baseUser.getAge() + "岁");
//                        // 性别 1 男；2女
//                        int sex = baseUser.getSex();
//                        if (sex == 1) {
//                            //男性
//                            blackHolder.tv_age.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//                        } else if (sex == 2) {
//                            //女性
//                            blackHolder.tv_age.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//                        }
//                        blackHolder.tv_location.setText(baseUser.getCity());
//                        blackHolder.tv_distance.setText(baseUser.getDistance());
//                    }
//                    if (type == 1) {
//                        blackHolder.tv_relieve.setText("解除禁言");
//                    } else {
//                        blackHolder.tv_relieve.setText("解除限制");
//                    }
//                    blackHolder.tv_relieve.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            EventBus.getDefault().post(new AssociationBlackListEvent(type, organMember,position));
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//    class BlackHolder extends BaseViewHolder {
//        TextView tv_header;
//        CircleImageView img_icon;
//        ImageView img_level;
//        TextView tv_nickname, tv_age, tv_location, tv_distance, tv_relieve;
//
//        public BlackHolder(View itemView) {
//            super(itemView);
//            //头布局
//            tv_header = itemView.findViewById(R.id.tv_header);
//            //item布局
//            img_icon = itemView.findViewById(R.id.img_icon);
//            img_level = itemView.findViewById(R.id.img_level);
//            tv_nickname = itemView.findViewById(R.id.tv_nickname);
//            tv_age = itemView.findViewById(R.id.tv_age);
//            tv_location = itemView.findViewById(R.id.tv_location);
//            tv_distance = itemView.findViewById(R.id.tv_distance);
//            tv_relieve = itemView.findViewById(R.id.tv_relieve);
//        }
//    }
//
//}
