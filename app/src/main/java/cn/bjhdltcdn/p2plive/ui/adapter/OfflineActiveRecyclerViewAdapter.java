//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
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
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * 线下活动列表
// */
//
//public class OfflineActiveRecyclerViewAdapter extends BaseRecyclerAdapter {
//    private List<ActivityInfo> list;
//    private Activity mActivity;
//    private RequestOptions options, userOptions;
//    private String defaultImg;//默认图片
//    private int selectPosition = 1;
//    private boolean showHeadView;
//    private int TYPE_NORMAL = 1000;
//    private int TYPE_HEADER = 1001;
//
//    public OfflineActiveRecyclerViewAdapter(Activity mActivity) {
//        this.mActivity = mActivity;
//        list = new ArrayList<ActivityInfo>();
//        options = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        userOptions = new RequestOptions()
//                .centerCrop()
//                .placeholder(R.mipmap.error_user_icon)
//                .error(R.mipmap.error_user_icon);
//    }
//
//    public void setList(List<ActivityInfo> list, String defaultImg) {
//        this.list = list;
//        this.defaultImg = defaultImg;
//    }
//
//    public int getSelectPosition() {
//        return selectPosition;
//    }
//
//    public void addList(List<ActivityInfo> list) {
//        this.list.addAll(list);
//    }
//
//    @Override
//    public int getItemCount() {
//        int count = list == null ? 0 : list.size();
//        if (showHeadView) {
//            count++;
//        }
//        return count;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0 && showHeadView) {
//            return TYPE_HEADER;
//        } else {
//            return TYPE_NORMAL;
//        }
//    }
//
//    public void setShowHeadView(boolean showHeadView) {
//        this.showHeadView = showHeadView;
//    }
//
//    public ActivityInfo getItem(int position) {
//        if (showHeadView) {
//            position = position - 1;
//        }
//        return list == null ? null : list.get(position);
//    }
//
//    public void removeItem(int position) {
//        list.remove(position);
//        notifyDataSetChanged();
//    }
//
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        if (itemType == TYPE_HEADER) {
//            return new HeadViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.send_inviation_active_list_head_layout, null));
//        } else {
//            return new ActiveHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.home_attention_active_recycle_item_layout, null));
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        int type = getItemViewType(position);
//        if (type == TYPE_NORMAL) {
//            if (getItemCount() > 0) {
//                if (holder instanceof ActiveHolder) {
//                    final ActiveHolder activeHolder = (ActiveHolder) holder;
//                    ImageView activeImg = activeHolder.activeImg;
//                    ActivityInfo activityInfo;
//                    if (showHeadView) {
//                        activityInfo = list.get(position - 1);
//                    } else {
//                        activityInfo = list.get(position);
//                    }
//                    if (activityInfo != null) {
//                        final BaseUser baseUser = activityInfo.getBaseUser();
//                        if (baseUser != null) {
//                            activeHolder.userName_tv.setText(baseUser.getNickName());
//                            if (!StringUtils.isEmpty(baseUser.getSchoolName())) {
//                                //学校名称
//                                activeHolder.schoolNameTextView.setVisibility(View.VISIBLE);
//                                activeHolder.schoolNameTextView.setText(baseUser.getSchoolName());
//                            } else {
//                                activeHolder.schoolNameTextView.setVisibility(View.GONE);
//                            }
//                            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                                //校友 并且不是自己
//                                activeHolder.alumnus_tv.setVisibility(View.VISIBLE);
//                            } else {
//                                activeHolder.alumnus_tv.setVisibility(View.GONE);
//                            }
//
//                            Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(activeHolder.userImg);
//                            activeHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                                        //跳到用户详情
//                                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                                    }
//                                }
//                            });
//                            if (baseUser.getSex() == 1) {
//                                activeHolder.userSexImg.setImageResource(R.mipmap.boy_icon);
//                            } else {
//                                activeHolder.userSexImg.setImageResource(R.mipmap.girl_icon);
//                            }
//                            //用户距离
//                            String userDiatance = baseUser.getDistance();
//                            if (!StringUtils.isEmpty(userDiatance)) {
//                                activeHolder.user_location_tv.setText(userDiatance);
//                                activeHolder.user_location_tv.setVisibility(View.VISIBLE);
//                            } else {
//                                activeHolder.user_location_tv.setVisibility(View.GONE);
//                            }
//                            if (!StringUtils.isEmpty(baseUser.getCity())) {
//                                activeHolder.cityTextView.setText(baseUser.getCity());
//                                activeHolder.cityTextView.setVisibility(View.VISIBLE);
//                            } else {
//                                activeHolder.cityTextView.setVisibility(View.GONE);
//                            }
//                            if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                                //自己不展示位置
//                                activeHolder.user_location_tv.setVisibility(View.GONE);
//                                activeHolder.cityTextView.setVisibility(View.GONE);
//                            }
//
//                        }
//                        //发布活动时间
//                        activeHolder.publishTimeTextView.setText(activityInfo.getAddTime());
//                        //活动距离
//                        String activeDiatance = activityInfo.getDistance();
//                        if ((!StringUtils.isEmpty(activeDiatance)) && activityInfo.getType() == 1) {
//                            activeHolder.activeDistanceView.setText("活动距你：" + activeDiatance);
//                            activeHolder.activeDistanceView.setVisibility(View.VISIBLE);
//                        } else {
//                            activeHolder.activeDistanceView.setVisibility(View.GONE);
//                        }
//                        int status = activityInfo.getStatus();
//                        if (status == 1) {
//                            activeHolder.activeStateImg.setImageResource(R.mipmap.active_status_enable_icon);
//                        } else {
//                            activeHolder.activeStateImg.setImageResource(R.mipmap.active_status_unenable_icon);
//                        }
//                        if (activityInfo.getHobbyList() != null) {
//                            ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
//                            hobbyRecyclerViewAdapter.setList(activityInfo.getHobbyList());
//                            activeHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//                        }
//                        if (activityInfo.getOrganList() != null) {
//                            List<OrganizationInfo> organizationInfoList = new ArrayList<OrganizationInfo>();
//                            organizationInfoList.addAll(activityInfo.getOrganList());
//                            ;
//                            if (activityInfo.getOrganList().size() > 3) {
//                                organizationInfoList.remove(organizationInfoList.size() - 1);
//                                activeHolder.orgainzationMoreImageView.setVisibility(View.VISIBLE);
//                            } else {
//                                activeHolder.orgainzationMoreImageView.setVisibility(View.INVISIBLE);
//                            }
//                            final ActiveListOrganizationRecyclerViewAdapter activeListOrganizationRecyclerViewAdapter = new ActiveListOrganizationRecyclerViewAdapter(mActivity);
//                            activeListOrganizationRecyclerViewAdapter.setList(organizationInfoList);
//                            activeHolder.orgainzationRecycleView.setAdapter(activeListOrganizationRecyclerViewAdapter);
//                            activeListOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//                                @Override
//                                public void onItemClick(View view, int position) {
//                                    //跳转到圈子详情
//                                    OrganizationInfo organizationInfo = activeListOrganizationRecyclerViewAdapter.getItem(position);
//                                    mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                                }
//                            });
//                            activeHolder.orgainzationMoreImageView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    //跳转到圈子列表页面
//                                    Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//                                    if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                                        //我的圈子列表
//                                        intent.putExtra(Constants.Fields.TYPE, 2);
//                                    } else {
//                                        //他的圈子列表
//                                        intent.putExtra(Constants.Fields.TYPE, 3);
//                                    }
//                                    intent.putExtra(Constants.Fields.Ta_USER_ID, baseUser.getUserId());
//                                    mActivity.startActivity(intent);
//                                }
//                            });
//                        }
//                        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//                            String thumnailUrl=activityInfo.getImageList().get(0).getThumbnailUrl();
//                            if(!thumnailUrl.equals( activeHolder.activeImg.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                                activeHolder.activeImg.setTag(null);//需要清空tag，否则报错
//                                Utils.CornerImageViewDisplayByUrl(thumnailUrl,  activeHolder.activeImg);
//                                activeHolder.activeImg.setTag(thumnailUrl);
//                            }
//                        } else {
//                            if(!defaultImg.equals( activeHolder.activeImg.getTag())){//解决图片加载不闪烁的问题,可以在加载时候，对于已经加载过的item,  采用比对tag方式判断是否需要重新计算高度
//                                activeHolder.activeImg.setTag(null);//需要清空tag，否则报错
//                                Utils.CornerImageViewDisplayByUrl(defaultImg,  activeHolder.activeImg);
//                                activeHolder.activeImg.setTag(defaultImg);
//                            }
//                        }
//                        activeHolder.activeContentTextView.setText(activityInfo.getTheme());
//                        //活动时间
//                        activeHolder.activeTimeTextView.setText("活动时间：" + activityInfo.getActivityTime());
//                        ActivityLocationInfo activityLocationInfo = activityInfo.getLocationInfo();
//                        if (activityLocationInfo != null && activityInfo.getType() == 1) {
//                            activeHolder.activePlaceTextView.setText("活动地点：" + activityInfo.getLocationInfo().getAddr());
//                            activeHolder.activePlaceTextView.setVisibility(View.VISIBLE);
//                        } else {
//                            activeHolder.activePlaceTextView.setVisibility(View.INVISIBLE);
//                        }
//
//                        activeHolder.activeApplyNumTextView.setText(activityInfo.getJoinNumber() + "人参加");
//                        if (activityInfo.getActivityPrice() >= 0) {
//                            activeHolder.activeCharge_tv.setText(activityInfo.getActivityPrice() + "元/人");
//                            activeHolder.activeCharge_tv.setVisibility(View.VISIBLE);
//                        } else {
//                            activeHolder.activeCharge_tv.setVisibility(View.INVISIBLE);
//                        }
//
//
//                        if (activityInfo.isCheck()) {
//                            selectPosition = position;
//                            activeHolder.checkRelativeLayout.setVisibility(View.VISIBLE);
//                        } else {
//                            activeHolder.checkRelativeLayout.setVisibility(View.GONE);
//                        }
//
//                    }
//
//                    if (showHeadView) {
//                        if (position == 0 || position == 1) {
//                            activeHolder.divider.setVisibility(View.GONE);
//                        } else {
//                            activeHolder.divider.setVisibility(View.VISIBLE);
//                        }
//
//                    } else {
//                        activeHolder.divider.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//        }
//
//
//    }
//
//
//    class ActiveHolder extends BaseViewHolder {
//        View divider;
//        ImageView userImg, userSexImg, activeImg, activeStateImg, alumnus_tv, orgainzationMoreImageView;
//        TextView userName_tv, user_location_tv, cityTextView, activeContentTextView, activeTimeTextView, activePlaceTextView, activeApplyNumTextView, activeCharge_tv,
//                publishTimeTextView, activeDistanceView, schoolNameTextView;
//        RecyclerView hobbyRecycleView, orgainzationRecycleView;
//        RelativeLayout checkRelativeLayout;
//
//        public ActiveHolder(View itemView) {
//            super(itemView);
//            divider = itemView.findViewById(R.id.top_header_view);
//            userImg = (ImageView) itemView.findViewById(R.id.user_img);
//            userSexImg = (ImageView) itemView.findViewById(R.id.user_sex_img);
//            activeImg = (ImageView) itemView.findViewById(R.id.active_img);
//            activeStateImg = (ImageView) itemView.findViewById(R.id.active_status_img);
//            userName_tv = (TextView) itemView.findViewById(R.id.user_nickname_text);
//            schoolNameTextView = (TextView) itemView.findViewById(R.id.school_name_text_view);
//            alumnus_tv = (ImageView) itemView.findViewById(R.id.alumnus_text);
//            user_location_tv = (TextView) itemView.findViewById(R.id.location_text);
//            cityTextView = (TextView) itemView.findViewById(R.id.city_text);
//            activeContentTextView = (TextView) itemView.findViewById(R.id.active_name_tv);
//            activeTimeTextView = (TextView) itemView.findViewById(R.id.active_time_tv);
//            activePlaceTextView = (TextView) itemView.findViewById(R.id.active_place_tv);
//            activeCharge_tv = (TextView) itemView.findViewById(R.id.active_charge_tv);
//            activeApplyNumTextView = (TextView) itemView.findViewById(R.id.active_apply_num_tv);
//            publishTimeTextView = itemView.findViewById(R.id.publish_time_text);
//            activeDistanceView = itemView.findViewById(R.id.active_distance_tv);
//            hobbyRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_hobby);
//            hobbyRecycleView.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager.setAutoMeasureEnabled(true);
//            hobbyRecycleView.setLayoutManager(gridLayoutManager);
//            hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false));
//            orgainzationRecycleView = (RecyclerView) itemView.findViewById(R.id.recycler_orgainzation);
//            orgainzationRecycleView.setHasFixedSize(true);
//            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(App.getInstance(), 3);
//            gridLayoutManager2.setAutoMeasureEnabled(true);
//            orgainzationRecycleView.setLayoutManager(gridLayoutManager2);
//            orgainzationRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));
//            orgainzationMoreImageView = itemView.findViewById(R.id.orgainzation_more_image);
//            checkRelativeLayout = itemView.findViewById(R.id.active_check_layout);
//        }
//
//    }
//
//    class HeadViewHolder extends BaseViewHolder {
//        TextView tipTextView;
//
//        public HeadViewHolder(View itemView) {
//            super(itemView);
//            tipTextView = itemView.findViewById(R.id.tip_text_view);
//        }
//
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