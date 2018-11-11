//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.OrganMember;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * Created by xiawenquan on 17/11/29.
// */
//
//public class OrganizationListMemberAdapter extends BaseRecyclerAdapter {
//
//    private List<OrganMember> list;
//    private RequestOptions options;
//
//    private int userRole;//所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0,
//    private long myUserId;
//
//    private ViewOnClick viewOnClick;
//
//    private UserViewOnClick userViewOnClick;
//
//    public void setUserViewOnClick(UserViewOnClick userViewOnClick) {
//        this.userViewOnClick = userViewOnClick;
//    }
//
//    private OutAndGagViewOnClick outAndGagViewOnClick;
//
//    public void setOutAndGagViewOnClick(OutAndGagViewOnClick outAndGagViewOnClick) {
//        this.outAndGagViewOnClick = outAndGagViewOnClick;
//    }
//
//    public void setViewOnClick(ViewOnClick viewOnClick) {
//        this.viewOnClick = viewOnClick;
//    }
//
//    public OrganizationListMemberAdapter(List<OrganMember> list, int userRole) {
//        this.userRole = userRole;
//        this.list = list;
//        options = new RequestOptions();
//        options.placeholder(R.mipmap.error_user_icon).centerCrop();
//        myUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//    }
//
//    public List<OrganMember> getList() {
//        return list;
//    }
//
//    public void setList(List<OrganMember> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    public void addList(List<OrganMember> list) {
//        if (this.list == null) {
//            return;
//        }
//
//        if (list == null) {
//            return;
//        }
//
//        this.list.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//
//        if (holder instanceof ItemViewHolder) {
//
//            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//
//
//            final OrganMember organMember = list.get(position);
//            itemViewHolder.topTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//            String userRoleText = "圈友";
//            //用户角色(1-->圈主,2-->管理员,3-->普通成员)
//            switch (organMember.getUserRole()) {
//
//                case 1://1-->圈主
//
//                    userRoleText = "圈主";
//                    itemViewHolder.topTextView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
//
//                    break;
//
//                case 2://2-->管理员
//
//                    userRoleText = "管理员";
//                    itemViewHolder.deleteView.setVisibility(View.VISIBLE);
//                    break;
//
//
//                case 3://3-->普通成员
//                    userRoleText = "圈友";
//                    itemViewHolder.deleteView.setVisibility(View.VISIBLE);
//                    break;
//
//            }
//
//            itemViewHolder.topTextView.setText(userRoleText);
//
//            // 头像
//            Glide.with(App.getInstance()).asBitmap().apply(options).load(organMember.getBaseUser().getUserIcon()).into(itemViewHolder.circleImageView);
//            itemViewHolder.circleImageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (userViewOnClick != null) {
//                        userViewOnClick.onClick(organMember.getBaseUser());
//                    }
//                }
//            });
//            // 昵称
//            String nickName = organMember.getBaseUser().getNickName();
//            if (nickName.length() > 10) {
//                nickName = nickName.substring(0, 10) + "...";
//            }
//            itemViewHolder.nickNameView.setText(nickName);
//
//            // 用户级别
//            Utils.getActiveLevel(itemViewHolder.activeLevelView, organMember.getActiveLevel());
//
//            if (organMember.getBaseUser() != null) {
//
//                //是否是校友(1否2是)
//                int isSchoolmate = organMember.getBaseUser().getIsSchoolmate();
//                if (isSchoolmate == 2) {
//                    itemViewHolder.alumnusView.setVisibility(View.VISIBLE);
//                } else {
//                    itemViewHolder.alumnusView.setVisibility(View.GONE);
//                }
//
//                // 距离
//                String distance = organMember.getBaseUser().getDistance();
//                itemViewHolder.distanceTextView.setText(distance);
//                itemViewHolder.distanceTextView.setVisibility(View.VISIBLE);
//
//                if (organMember.getBaseUser().getUserId() == myUserId) {
//                    itemViewHolder.distanceTextView.setVisibility(View.INVISIBLE);
//                }
//
//
//
//            }
//
//
//            // 性别 1 男；2女 | 年龄
//            itemViewHolder.userAgeText.setText(organMember.getBaseUser().getAge() + "岁");
//            int sex = organMember.getBaseUser().getSex();
//            if (sex == 1) {
//                //男性
//                itemViewHolder.userAgeText.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                itemViewHolder.userAgeText.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            String city = organMember.getBaseUser().getCity();
//            if (!TextUtils.isEmpty(city)) {
//                if (city.length() > 5) {
//                    city = city.substring(0, 5) + "...";
//                }
//                // 位置
//                itemViewHolder.locationtextView.setText(city);
//            }
//
//            // 删除按钮
//
//            itemViewHolder.deleteView.setVisibility(View.VISIBLE);
//            if (organMember.getUserRole() == 1) {// 圈主
//                itemViewHolder.deleteView.setOnClickListener(null);
//                itemViewHolder.deleteView.setVisibility(View.GONE);
//                itemViewHolder.topTextView.setBackgroundResource(R.color.color_ffffff);
//            } else if (organMember.getUserRole() == 2) {
//                itemViewHolder.deleteView.setText("删除");
//                itemViewHolder.deleteView.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                itemViewHolder.deleteView.setBackgroundResource(R.drawable.shape_round_10_stroke_666666_solid_ffffff);
//
//                itemViewHolder.topTextView.setBackgroundResource(R.color.color_e6e6e6);
//
//            } else if (organMember.getUserRole() == 3) {
//                itemViewHolder.deleteView.setText("设为管理员");
//                itemViewHolder.deleteView.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
//                itemViewHolder.deleteView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                itemViewHolder.topTextView.setBackgroundResource(R.color.color_e6e6e6);
//            }
//
//
//            //是否禁言(1解除禁言,2禁言)
//            if (organMember.getIsGag() == 1) {
//                itemViewHolder.gagView.setText("禁言");
//                itemViewHolder.gagView.setTextColor(App.getInstance().getResources().getColor(R.color.color_ff8d8d));
//                itemViewHolder.gagView.setBackgroundResource(R.drawable.shape_round_10_stroke_ff8d8d_solid_ffffff);
//            } else {
//                itemViewHolder.gagView.setText("解除禁言");
//                itemViewHolder.gagView.setTextColor(App.getInstance().getResources().getColor(R.color.color_8ed6ff));
//                itemViewHolder.gagView.setBackgroundResource(R.drawable.shape_round_8_stroke_8ed6ff);
//            }
//
//            // 隐藏分割线
//            itemViewHolder.topTextView.setVisibility(View.VISIBLE);
//            if (getItemCount() > 1 && position > 0) {
//                OrganMember organMember0 = list.get(position - 1);
//                if (organMember.getUserRole() == organMember0.getUserRole()) {
//                    itemViewHolder.topTextView.setVisibility(View.GONE);
//                }
//            }
//
//            itemViewHolder.deleteView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (viewOnClick != null) {
//                        viewOnClick.onClick(position);
//                    }
//                }
//            });
//
//            itemViewHolder.deleteView.setVisibility(View.VISIBLE);
//            itemViewHolder.outView.setVisibility(View.VISIBLE);
//            itemViewHolder.gagView.setVisibility(View.VISIBLE);
//
//            //当前用户所在圈子中所属角色(1--->圈主,2--->管理员,3--->普通成员,4--->申请中)用户不在圈子返回0
//            if (userRole == 1 && organMember.getUserRole() == 1) {// 当前用户是圈主并且看到的item数据也是圈主
//                itemViewHolder.deleteView.setVisibility(View.GONE);
//                itemViewHolder.outView.setVisibility(View.GONE);
//                itemViewHolder.gagView.setVisibility(View.GONE);
//            } else if (userRole == 2 && (organMember.getUserRole() == 1 || organMember.getUserRole() == 2)) {// 当前用户是管理员并且看到的item数据也是圈主，管理员
//                itemViewHolder.deleteView.setVisibility(View.GONE);
//                itemViewHolder.outView.setVisibility(View.GONE);
//                itemViewHolder.gagView.setVisibility(View.GONE);
//            } else if (userRole == 0 || userRole == 3 || userRole == 4) {// 当前用户不在圈子或者是普通成员或者是申请中
//                itemViewHolder.deleteView.setVisibility(View.GONE);
//                itemViewHolder.outView.setVisibility(View.GONE);
//                itemViewHolder.gagView.setVisibility(View.GONE);
//            }
//
//            // 踢出圈子
//            itemViewHolder.outView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (outAndGagViewOnClick != null) {
//                        outAndGagViewOnClick.onClick(position, 0, "踢出");
//                    }
//                }
//            });
//
//            // 踢出圈子
//            itemViewHolder.gagView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (outAndGagViewOnClick != null) {
//                        //是否禁言(1解除禁言,2禁言)
//                        outAndGagViewOnClick.onClick(position, organMember.getIsGag() == 1 ? 2 : 1, organMember.getIsGag() == 1 ? "禁言" : "解除禁言");
//                    }
//                }
//            });
//
//
//        }
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//
//        View itemView = View.inflate(App.getInstance(), R.layout.organization_list_item_member_adapter_layout, null);
//
//        return new ItemViewHolder(itemView);
//
//    }
//
//
//    static class ItemViewHolder extends BaseViewHolder {
//
//        public TextView topTextView;
//        public ImageView circleImageView;
//        public TextView nickNameView;
//        public ImageView activeLevelView;
//        public TextView userAgeText;
//        public TextView locationtextView;
//        public TextView distanceTextView;
//        public TextView outView;
//        public TextView gagView;
//        public TextView deleteView;
//        public ImageView alumnusView;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//
//            topTextView = itemView.findViewById(R.id.top_text_view);
//            circleImageView = itemView.findViewById(R.id.circle_image_view);
//            nickNameView = itemView.findViewById(R.id.nicke_text_view);
//            activeLevelView = itemView.findViewById(R.id.active_level_view);
//            userAgeText = itemView.findViewById(R.id.user_age_text);
//            locationtextView = itemView.findViewById(R.id.location_text_view);
//            distanceTextView = itemView.findViewById(R.id.distance_text_view);
//            outView = itemView.findViewById(R.id.out_view);
//            gagView = itemView.findViewById(R.id.gag_view);
//            deleteView = itemView.findViewById(R.id.delete_view);
//            alumnusView = itemView.findViewById(R.id.alumnus_text);
//
//        }
//    }
//
//    public interface ViewOnClick {
//        void onClick(int position);
//    }
//
//    public interface UserViewOnClick {
//        void onClick(BaseUser baseUser);
//    }
//
//    /**
//     * 踢出和禁言/解禁 接口
//     */
//    public interface OutAndGagViewOnClick {
//        /**
//         * @param position
//         * @param itemType 0 踢出; 1 解除禁言; 2 禁言
//         */
//        void onClick(int position, int itemType, String title);
//    }
//
//}
