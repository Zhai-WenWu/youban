package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PKLaunchPlay;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * PK分享
 */
public class PkSharedFragment extends BaseFragment {

    private View rootView;


    private PlayInfo playInfo;

    public void setObject(PlayInfo playInfo) {
        this.playInfo = playInfo;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_pk_shared_layout, null);
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

        initView();

    }

    private void initView(){

        if (playInfo != null) {

            final BaseUser baseUser = playInfo.getBaseUser();
            if (baseUser != null) {
                TextView tipTextView = rootView.findViewById(R.id.form_user_nickname_text);
                tipTextView.setText("@" + baseUser.getNickName());
                tipTextView.setVisibility(View.VISIBLE);
                tipTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            //跳到用户详情
                            startActivity(new Intent(getActivity(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                        }
                    }
                });
            }

            // pk标题
            TextView pkTitleTextView = rootView.findViewById(R.id.pk_title_text);
            if (playInfo.getLaunchPlay() != null) {
                pkTitleTextView.setText(playInfo.getLaunchPlay().getTitle());
            } else {
                pkTitleTextView.setText(PKLaunchPlay.getInstent().getLaunchPlay().getTitle());
            }

            // PK描述(个人用户编辑的内容)
            TextView pkContentTextView = rootView.findViewById(R.id.pk_content_text);
            if (playInfo != null) {
                pkContentTextView.setText(playInfo.getTitle());
            }

            // pk视频
            ImageView imgView = rootView.findViewById(R.id.img_bg);
            RequestOptions options = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg);
            Glide.with(App.getInstance()).load(playInfo.getVideoImageUrl()).apply(options).into(imgView);

        }

    }



    @Override
    protected void onVisible(boolean isInit) {

    }

}
