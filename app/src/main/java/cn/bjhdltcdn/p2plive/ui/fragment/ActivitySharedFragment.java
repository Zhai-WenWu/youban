package cn.bjhdltcdn.p2plive.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.ActiveListHobbyRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PostImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

/**
 *
 */
public class ActivitySharedFragment extends BaseFragment {

    private View rootView;
    private AppCompatActivity mActivity;
    private RequestOptions options, userOptions;
    private int screenWidth;
    private ActivityInfo activityInfo;
    private String defaultImg;

    public void setObject(ActivityInfo activityInfo) {
        this.activityInfo = activityInfo;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity= (AppCompatActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_active_shared_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_bg)
                .error(R.mipmap.error_bg);
        userOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.error_user_icon)
                .error(R.mipmap.error_user_icon);
        screenWidth = PlatformInfoUtils.getWidthOrHeight(mActivity)[0];
        initView();
    }

    private void initView() {

        if (activityInfo != null) {
            TextView userName_tv = (TextView) rootView.findViewById(R.id.form_user_nickname_text);
            ImageView activeImg= (ImageView) rootView.findViewById(R.id.active_img);
            ImageView activeStateImg= (ImageView) rootView.findViewById(R.id.active_status_img);
            TextView activeContentTextView = (TextView) rootView.findViewById(R.id.active_name_tv);
            TextView activeTimeTextView = (TextView) rootView.findViewById(R.id.active_time_tv);
            TextView activePlaceTextView = (TextView) rootView.findViewById(R.id.active_place_tv);
            TextView activeCharge_tv = (TextView) rootView.findViewById(R.id.active_charge_tv);
            TextView activeApplyNumTextView = (TextView) rootView.findViewById(R.id.active_apply_num_tv);
            TextView activeDistanceView=rootView.findViewById(R.id.active_distance_tv);
            RecyclerView hobbyRecycleView= (RecyclerView) rootView.findViewById(R.id.recycler_hobby);
            hobbyRecycleView.setHasFixedSize(true);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(App.getInstance(), 3);
            gridLayoutManager.setAutoMeasureEnabled(true);
            hobbyRecycleView.setLayoutManager(gridLayoutManager);
            hobbyRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(12), 3, false));
            if(activityInfo!=null){
                final BaseUser baseUser=activityInfo.getBaseUser();
                if(baseUser!=null){
                    userName_tv.setText("@"+baseUser.getNickName());
                    userName_tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(baseUser.getUserId()!= SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID,0)){
                                //跳到用户详情
                                mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT,baseUser));
                            }
                        }
                    });

                }
                //活动距离
                String activeDiatance=activityInfo.getDistance();
                if((!StringUtils.isEmpty(activeDiatance))&&activityInfo.getType()==1){
                    activeDistanceView.setText("活动距你："+activeDiatance);
                    activeDistanceView.setVisibility(View.VISIBLE);
                }else{
                    activeDistanceView.setVisibility(View.GONE);
                }
                int status=activityInfo.getStatus();
                if(status==1){
                    activeStateImg.setImageResource(R.mipmap.active_status_enable_icon);
                }else{
                    activeStateImg.setImageResource(R.mipmap.active_status_unenable_icon);
                }
                if(activityInfo.getHobbyList()!=null){
                    ActiveListHobbyRecyclerViewAdapter hobbyRecyclerViewAdapter = new ActiveListHobbyRecyclerViewAdapter(mActivity);
                    hobbyRecyclerViewAdapter.setList(activityInfo.getHobbyList());
                    hobbyRecycleView.setAdapter(hobbyRecyclerViewAdapter);
                }
                if(activityInfo.getImageList()!=null&&activityInfo.getImageList().size()>0){
                    Glide.with(App.getInstance()).load(activityInfo.getImageList().get(0).getThumbnailUrl()).apply(options).into(activeImg);
                }else{
                    Glide.with(App.getInstance()).load(defaultImg).apply(options).into(activeImg);
                }
                activeContentTextView.setText(activityInfo.getTheme());
                //活动时间
                activeTimeTextView.setText("活动时间："+activityInfo.getActivityTime());
                ActivityLocationInfo activityLocationInfo=activityInfo.getLocationInfo();
                if(activityLocationInfo!=null)
                {
                    activePlaceTextView.setText("活动地点："+activityInfo.getLocationInfo().getAddr());
                    activePlaceTextView.setVisibility(View.VISIBLE);
                }else{
                    activePlaceTextView.setVisibility(View.INVISIBLE);
                }

                activeApplyNumTextView.setText(activityInfo.getJoinNumber()+"人参加");
                if(activityInfo.getActivityPrice()>=0){
                    activeCharge_tv.setText(activityInfo.getActivityPrice()+"元/人");
                    activeCharge_tv.setVisibility(View.VISIBLE);
                }else{
                    activeCharge_tv.setVisibility(View.INVISIBLE);
                }
            }


        }

    }

    @Override
    protected void onVisible(boolean isInit) {

    }


}
