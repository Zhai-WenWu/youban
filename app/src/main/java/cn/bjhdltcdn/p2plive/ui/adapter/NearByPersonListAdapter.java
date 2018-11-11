//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.content.Intent;
//import android.content.res.TypedArray;
//import android.graphics.drawable.Drawable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
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
//import cn.bjhdltcdn.p2plive.model.Image;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
//import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
//import cn.bjhdltcdn.p2plive.ui.dialog.ImageViewPageDialog;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
//import cn.bjhdltcdn.p2plive.utils.ShareUtil;
//import cn.bjhdltcdn.p2plive.utils.StringUtils;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
//public class NearByPersonListAdapter  extends BaseRecyclerAdapter {
//    private List<BaseUser> list;
//    private AppCompatActivity mActivity;
//    private RequestOptions options, userOptions;
//    private OnClick onClick;
//
//    public NearByPersonListAdapter(AppCompatActivity mActivity) {
//        this.mActivity = mActivity;
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
//
//    public void setOnClick(OnClick onClick) {
//        this.onClick = onClick;
//    }
//
//
//
//    public void setList(List<BaseUser> list) {
//        this.list = list;
//    }
//
//    public void setListAll(List<BaseUser> list) {
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
//    }
//    /**
//     * @return the mList
//     */
//    public List<BaseUser> getList() {
//        return list;
//    }
//
//    public BaseUser getItem(int position){
//        return list == null ? null : list.get(position);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        BaseViewHolder baseViewHolder = null;
//        baseViewHolder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.nearby_person_list_item_layout, null));
//        return baseViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        if (getItemCount() > 0) {
//            if(holder instanceof ViewHolder){
//
//                final ViewHolder viewHolder = (ViewHolder) holder;
//                final BaseUser baseUser = list.get(position);
//                if (baseUser == null) {
//                    return;
//                }
//                viewHolder.userNickNameTextView.setText(baseUser.getNickName());
//                Glide.with(App.getInstance()).load(baseUser.getUserIcon()).apply(userOptions).into(viewHolder.userImg);
//                viewHolder.userImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            //跳到用户详情
//                            mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
//                        }
//                    }
//                });
//
//                viewHolder.userAgeTextView.setText(baseUser.getAge() + "岁");
//                if (baseUser.getIsSchoolmate() == 2 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //校友 并且不是自己
//                    viewHolder.alumnus_tv.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.alumnus_tv.setVisibility(View.GONE);
//                }
//
//                int sex = baseUser.getSex();
//                if (sex == 1) {
//                    //男性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.boy_icon), null, null, null);
//                } else if (sex == 2) {
//                    //女性
//                    viewHolder.userAgeTextView.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.girl_icon), null, null, null);
//                }
//
//                if ((!StringUtils.isEmpty(baseUser.getDistance()))) {
//                    viewHolder.locationTextView.setText(baseUser.getDistance());
//                    viewHolder.locationTextView.setVisibility(View.VISIBLE);
//                } else {
//                    viewHolder.locationTextView.setVisibility(View.GONE);
//                }
//                if((!StringUtils.isEmpty(baseUser.getCity()))){
//                    viewHolder.cityTextView.setText(baseUser.getCity());
//                    viewHolder.cityTextView.setVisibility(View.VISIBLE);
//                }else{
//                    viewHolder.cityTextView.setVisibility(View.GONE);
//                }
//                if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                    //自己不展示位置
//                    viewHolder.locationTextView.setVisibility(View.GONE);
//                    viewHolder.cityTextView.setVisibility(View.GONE);
//                }
//                viewHolder.sendInvitationTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //发邀请函
//                        onClick.onSendInvitaion(baseUser.getUserId(),baseUser.getUserId());
//                    }
//                });
//                final int isAttention=baseUser.getIsAttention();
//                if(isAttention==0){
//                    viewHolder.attentionTextView.setText("关注");
//                    viewHolder.attentionTextView.setTextColor(mActivity.getResources().getColor(R.color.color_333333));
//                    viewHolder.attentionTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_8_solid_ffee00));
//                }else{
//                    viewHolder.attentionTextView.setText("已关注");
//                    viewHolder.attentionTextView.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
//                    viewHolder.attentionTextView.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_round_8_stroke_999999_solid_ffffff));
//                }
//
//                viewHolder.attentionTextView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //关注
//                        if(isAttention==0) {
//                            onClick.onAttention(position,1,0, baseUser.getUserId());
//                        }
//                    }
//                });
//
//                if (position == 0) {
//                    viewHolder.divider.setVisibility(View.GONE);
//                } else {
//                    viewHolder.divider.setVisibility(View.GONE);
//                }
//
//            }
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    class ViewHolder extends BaseViewHolder {
//        ImageView userImg, alumnus_tv;
//        TextView userNickNameTextView,userAgeTextView,  cityTextView,locationTextView,sendInvitationTextView,attentionTextView;
//        View divider;
//
//        public ViewHolder(View convertView) {
//            super(convertView);
//            userNickNameTextView = (TextView) convertView.findViewById(R.id.user_nickname_text);
//            alumnus_tv = (ImageView) convertView.findViewById(R.id.alumnus_text);
//            userAgeTextView = (TextView) convertView.findViewById(R.id.user_age_text);
//            locationTextView = (TextView) convertView.findViewById(R.id.location_text);
//            cityTextView= (TextView) convertView.findViewById(R.id.city_text);
//            userImg = (ImageView) convertView.findViewById(R.id.user_img);
//            divider = convertView.findViewById(R.id.divider_line);
//            sendInvitationTextView= (TextView) convertView.findViewById(R.id.send_invitation_text_view);
//            attentionTextView= (TextView) convertView.findViewById(R.id.attention_text_view);
//        }
//
//    }
//
//    public interface OnClick {
//        void onAttention(int position,int type,long formUserId, long toUserId);
//
//        void onSendInvitaion(long userId,long toUserId);
//
//    }
//
//
//}
