package cn.bjhdltcdn.p2plive.ui.fragment;

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
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.PostImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;

/**
 *
 */
public class RoomSharedFragment extends BaseFragment {

    private View rootView;
    private AppCompatActivity mActivity;
    private RequestOptions options;
    private RoomInfo roomInfo;

    public void setObject(RoomInfo roomInfo) {
        this.roomInfo = roomInfo;
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
            rootView = inflater.inflate(R.layout.fragment_room_shared_layout, null);
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
        initView();
    }

    private void initView() {

        if (roomInfo != null) {
            ImageView roomImageView = (ImageView) rootView.findViewById(R.id.room_image_view);
            TextView userNameView = (TextView) rootView.findViewById(R.id.form_user_nickname_text);
            RelativeLayout tagLayout = (RelativeLayout) rootView.findViewById(R.id.tag_layout);
            TextView tagListView = (TextView) rootView.findViewById(R.id.tag_text_view);
            TextView roomNameView = (TextView) rootView.findViewById(R.id.room_name_view);

            roomNameView.setText(roomInfo.getRoomName());
            Glide.with(App.getInstance()).load(roomInfo.getBackgroundUrl()).apply(options).into(roomImageView);
            //房间标签
            List<String> keywordInfoList = roomInfo.getLabelList();
            if (keywordInfoList != null && keywordInfoList.size() > 0) {
                tagLayout.setVisibility(View.VISIBLE);
                StringBuffer labelString = new StringBuffer();
                for (int i = 0; i < keywordInfoList.size(); i++) {
                    String info = keywordInfoList.get(i);
                    labelString.append(info + "    ");
                }
                tagListView.setText(labelString.toString());
            } else {
                tagLayout.setVisibility(View.GONE);
            }
            final BaseUser baseUser = roomInfo.getBaseUser();
            if (baseUser != null) {
                userNameView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //跳到用户详情
                            mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                        }
                    }
                });
                userNameView.setText("@" + baseUser.getNickName());

            }
        }

    }

    @Override
    protected void onVisible(boolean isInit) {

    }


}
