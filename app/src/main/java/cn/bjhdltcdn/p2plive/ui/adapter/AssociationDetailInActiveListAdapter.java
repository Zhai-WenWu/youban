//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.ApiData;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.ActivityInfo;
//import cn.bjhdltcdn.p2plive.model.BaseUser;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// * Created by xiawenquan on 18/3/3.
// */
//
//public class AssociationDetailInActiveListAdapter extends BaseAdapter {
//
//    private List<HomeInfo> list;
//    private AppCompatActivity mActivity;
//    private RequestOptions options, userOptions;
//
//    public List<HomeInfo> getList() {
//        return list;
//    }
//
//    private ItemListener itemListener;
//
//    public void setItemListener(ItemListener itemListener) {
//        this.itemListener = itemListener;
//    }
//
//    private AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick;
//
//    public void setItemWidgetOnClick(AssociationDetailListAdapter.ItemWidgetOnClick itemWidgetOnClick) {
//        this.itemWidgetOnClick = itemWidgetOnClick;
//    }
//
//    public AssociationDetailInActiveListAdapter(AppCompatActivity mActivity) {
//        this.mActivity = mActivity;
//        options = new RequestOptions().centerCrop().transform(new GlideRoundTransform( 9)).placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg).diskCacheStrategy(DiskCacheStrategy.RESOURCE);
//
//        userOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_user_icon).error(R.mipmap.error_user_icon);
//    }
//
//    public void setList(List<HomeInfo> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    public void setListAll(List<HomeInfo> list) {
//
//        if (this.list == null) {
//            return;
//        }
//
//        if (list == null) {
//            return;
//        }
//
//        this.list.addAll(list);
//
//        notifyDataSetChanged();
//
//    }
//
//    public void addItem(HomeInfo homeInfo) {
//
//        if (homeInfo == null) {
//            return;
//        }
//
//
//        if (this.list == null) {
//            this.list = new ArrayList<>(1);
//        }
//        this.list.add(0, homeInfo);
//
//        notifyDataSetChanged();
//
//    }
//
//
//    public void deleteItem(int position) {
//
//        if (position < 0) {
//            return;
//        }
//
//        this.list.remove(position);
//
//        notifyDataSetChanged();
//
//    }
//
//
//    @Override
//    public int getCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return list == null || list.size() == 0 ? null : list.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        ActiveItemViewHolder activeItemViewHolder = null;
//        if (convertView == null) {
//            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.association_active_list_item_layout, null);
//            activeItemViewHolder = new ActiveItemViewHolder();
//            convertView.setTag(activeItemViewHolder);
//            bindActiveItemViewHolder(convertView, activeItemViewHolder);
//        } else {
//            activeItemViewHolder = (ActiveItemViewHolder) convertView.getTag();
//        }
//
//        final HomeInfo homeInfo = list.get(position);
//
//        bindActiveItemData(activeItemViewHolder, homeInfo, position);
//
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到活动详情页
//                Intent intent = new Intent(mActivity, ActiveDetailActivity.class);
//                intent.putExtra(Constants.KEY.KEY_ACTIVITY_ID, homeInfo.getActivityInfo().getActivityId());
//                intent.putExtra(Constants.Fields.POSITION, position);
//                mActivity.startActivity(intent);
//            }
//        });
//
//        return convertView;
//    }
//
//
//    private void bindActiveItemData(ActiveItemViewHolder activeHolder, final HomeInfo homeInfo, final int position) {
//
//        if (activeHolder == null || homeInfo == null) {
//            return;
//        }
//
//        final ActivityInfo activityInfo = homeInfo.getActivityInfo();
//        if (activityInfo == null) {
//            return;
//        }
//
//        activeHolder.lineView2.setVisibility(View.VISIBLE);
//        if (position == getCount() - 1) {
//            activeHolder.lineView2.setVisibility(View.INVISIBLE);
//        }
//
//        // 跳转到附近活动
//        // 跳转到附近活动
//        activeHolder.activetopView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mActivity != null) {
//
//                    Intent intent = new Intent(mActivity, ActiveListActivity.class);
//                    intent.putExtra(Constants.Fields.TO_USER_ID, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
//                    intent.putExtra(Constants.Fields.COME_IN_TYPE, 1);
//                    mActivity.startActivity(intent);
//
//
//                }
//            }
//        });
//
//        // 用户信息
//        final BaseUser baseUser = homeInfo.getBaseUser() != null ? homeInfo.getBaseUser() : activityInfo.getBaseUser();
//        if (baseUser != null) {
//            // 用户头像
//            Glide.with(mActivity).asBitmap().apply(userOptions).load(baseUser.getUserIcon()).into(activeHolder.userImg);
//            activeHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //跳到用户详情
//                        mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                    }
//                }
//            });
//
//            // 学校
//            activeHolder.schoolTextView.setText(baseUser.getSchoolName());
//
//            if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                //校友 并且不是自己
//                activeHolder.alumnusView.setVisibility(View.VISIBLE);
//            } else {
//                activeHolder.alumnusView.setVisibility(View.GONE);
//            }
//
//            // 用户昵称
//            activeHolder.nickNameView.setText(baseUser.getNickName());
//
//            int sex = baseUser.getSex();
//            if (sex == 1) {
//                //男性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//            } else if (sex == 2) {
//                //女性
//                activeHolder.userAgeView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//            }
//            activeHolder.userAgeView.setText(baseUser.getAge() + "岁");
//
//            // 城市
//            activeHolder.cityTextView.setText(baseUser.getCity());
//
//            // 位置
//            activeHolder.locationView.setText(baseUser.getDistance());
//
//        }
//
//        // 用户级别
//        Utils.getActiveLevel(activeHolder.userLevelView, activityInfo.getActiveLevel());
//
//        // 活动时间
//        activeHolder.timeTextView.setText(activityInfo.getActivityTime());
//
//        // 活动状态(1报名中,2报名结束)
//        activeHolder.activeStatusImgView.setVisibility(View.INVISIBLE);
//        int status = activityInfo.getStatus();
//        if (status == 2) {
//            activeHolder.activeStatusImgView.setVisibility(View.VISIBLE);
//            activeHolder.activeStatusImgView.setImageResource(R.mipmap.active_status_unenable_icon);
//        }
//
//        // 是否置顶(0取消,1置顶)
//        if (activityInfo.getIsTop() == 0) {
//            activeHolder.topView.setVisibility(View.INVISIBLE);
//        } else {
//            activeHolder.topView.setVisibility(View.VISIBLE);
//        }
//
//        // 活动的兴起标签
//        List<HobbyInfo> hobbyInfoList = activityInfo.getHobbyList();
//        if (hobbyInfoList != null) {
//            ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
//            hobbyRecyclerViewAdapter.setList(hobbyInfoList);
//            activeHolder.hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
//        }
//
//        final ActiveListOrganizationRecyclerViewAdapter activeListOrganizationRecyclerViewAdapter = new ActiveListOrganizationRecyclerViewAdapter(mActivity);
//        activeListOrganizationRecyclerViewAdapter.setList((activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 3) ? activityInfo.getOrganList().subList(0, 3) : activityInfo.getOrganList());
//        activeHolder.orgainzationRecycleView.setAdapter(activeListOrganizationRecyclerViewAdapter);
//        activeListOrganizationRecyclerViewAdapter.setOnItemListener(new ItemListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                //跳转到圈子详情
//                OrganizationInfo organizationInfo = activeListOrganizationRecyclerViewAdapter.getItem(position);
//                mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//            }
//        });
//
//        if (activityInfo.getOrganList() != null && activityInfo.getOrganList().size() > 2) {
//            activeHolder.moreImgView.setVisibility(View.VISIBLE);
//        } else {
//            activeHolder.moreImgView.setVisibility(View.INVISIBLE);
//        }
//
//        // 圈子更多标签
//        activeHolder.moreImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到圈子列表页面
//                Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //我的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 2);
//                } else {
//                    //他的圈子列表
//                    intent.putExtra(Constants.Fields.TYPE, 3);
//                }
//                intent.putExtra(Constants.Fields.Ta_USER_ID, baseUser.getUserId());
//                mActivity.startActivity(intent);
//
//            }
//        });
//
//        // 圈子更多标签
//        activeHolder.moreImgView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //跳转到圈子列表页面
//                Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//
//                BaseUser baseUser1 = baseUser;
//
//                if (baseUser1 == null) {
//                    baseUser1 = activityInfo.getBaseUser();
//                }
//
//                if (baseUser1 != null) {
//                    if (baseUser1.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                        //我的圈子列表
//                        intent.putExtra(Constants.Fields.TYPE, 2);
//                    } else {
//                        //他的圈子列表
//                        intent.putExtra(Constants.Fields.TYPE, 3);
//                    }
//
//                    intent.putExtra(Constants.Fields.Ta_USER_ID, baseUser1.getUserId());
//                    mActivity.startActivity(intent);
//                }
//
//
//            }
//        });
//
//        // 图片显示
//        activeHolder.activeImgView.setOnClickListener(null);
//        String imgUrl = null;
//        if (activityInfo.getImageList() != null && activityInfo.getImageList().size() > 0) {
//            imgUrl = activityInfo.getImageList().get(0).getThumbnailUrl();
//            activeHolder.activeImgView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mActivity != null && !mActivity.isFinishing()) {
//                        ImageViewPageDialog.newInstance(activityInfo.getImageList(), 0).show(mActivity.getSupportFragmentManager());
//                    }
//                }
//            });
//        }
//
//        // 显示默认图
//        if (StringUtils.isEmpty(imgUrl)) {
//            String host = ApiData.getInstance().getHost();
//            host = host.substring(0, host.lastIndexOf("/"));
//            imgUrl = host + "/uploadfile/system/activity/activity_img.png";
//        }
//
//        Glide.with(App.getInstance()).load(imgUrl).apply(options).into(activeHolder.activeImgView);
//
//        // 活动主题
//        activeHolder.activeContentView.setText(activityInfo.getTheme());
//
//        // 更多按钮
//        activeHolder.moreImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (itemWidgetOnClick != null) {
//                    itemWidgetOnClick.moreImg(homeInfo.getType(), position);
//                }
//            }
//        });
//
//    }
//
//
//    private void bindActiveItemViewHolder(View convertView, ActiveItemViewHolder viewHolder) {
//
//        viewHolder.convertView = convertView;
//        viewHolder.activeTopLayout = convertView.findViewById(R.id.active_top_layout);
//        viewHolder.activetopView = convertView.findViewById(R.id.active_top_view);
//        viewHolder.moreImg = convertView.findViewById(R.id.more_img);
//        viewHolder.userImg = convertView.findViewById(R.id.user_img);
//        viewHolder.schoolTextView = (TextView) convertView.findViewById(R.id.school_text_view);
//        viewHolder.nickNameView = convertView.findViewById(R.id.user_nickname_text);
//        viewHolder.userLevelView = convertView.findViewById(R.id.user_level_img);
//        viewHolder.alumnusView = convertView.findViewById(R.id.alumnus_img);
//        viewHolder.timeTextView = convertView.findViewById(R.id.time_text);
//        viewHolder.userAgeView = convertView.findViewById(R.id.user_age_text);
//        viewHolder.cityTextView = (TextView) convertView.findViewById(R.id.city_text);
//        viewHolder.locationView = convertView.findViewById(R.id.location_text);
//        viewHolder.activeStatusImgView = convertView.findViewById(R.id.active_status_img);
//        viewHolder.topView = convertView.findViewById(R.id.post_stick_view);
//        viewHolder.activeImgView = convertView.findViewById(R.id.active_img);
//        viewHolder.activeContentView = convertView.findViewById(R.id.active_name_tv);
//
//        viewHolder.hobbyRecycleView = (RecyclerView) convertView.findViewById(R.id.recycler_hobby);
//        viewHolder.hobbyRecycleView.setHasFixedSize(true);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 4);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        viewHolder.hobbyRecycleView.setLayoutManager(gridLayoutManager);
//        viewHolder.hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 4, false));
//
//        viewHolder.orgainzationRecycleView = convertView.findViewById(R.id.recycler_orgainzation);
//        viewHolder.orgainzationRecycleView.setHasFixedSize(true);
//
//        GridLayoutManager orgainzationlayout = new GridLayoutManager(App.getInstance(), 3);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//
//        viewHolder.orgainzationRecycleView.setLayoutManager(orgainzationlayout);
//        viewHolder.orgainzationRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));
//
//
//        viewHolder.moreImgView = convertView.findViewById(R.id.orgainzation_more_image);
//
//
//        viewHolder.lineView2 = convertView.findViewById(R.id.line_view_2);
//
//    }
//
//    static class ActiveItemViewHolder {
//        View convertView;
//        TextView activeTopLayout;
//        ImageView moreImg;
//        CircleImageView userImg;
//        TextView schoolTextView;
//        ImageView userLevelView;
//        ImageView alumnusView;
//        TextView nickNameView;
//        TextView timeTextView;
//        TextView userAgeView;
//        TextView locationView;
//        ImageView activeStatusImgView;
//        ImageView topView;
//        ImageView activeImgView;
//        TextView activeContentView;
//        RecyclerView hobbyRecycleView;
//        ImageView moreImgView;
//        TextView cityTextView;
//        TextView activetopView;
//        View lineView2;
//
//        public RecyclerView orgainzationRecycleView;
//
//    }
//
//
//}
