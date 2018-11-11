package cn.bjhdltcdn.p2plive.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * 圈子分享
 */
public class OrganizationInfoFragment extends BaseFragment {


    private View rootView;
    private OrganizationInfo organizationInfo;

    public void setOrganizationInfo(OrganizationInfo organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_organization_info_layout, null);
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

        if (organizationInfo == null) {
            getActivity().finish();
            return;
        }

        // 圈子封面
        ImageView imageView = rootView.findViewById(R.id.orgain_img);
        Glide.with(this).load(organizationInfo.getOrganImg()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(imageView);

        // 圈子昵称
        TextView nickNameView = rootView.findViewById(R.id.orgain_name_text);
        nickNameView.setText(organizationInfo.getOrganName());

        // 成员数据
        TextView textView = rootView.findViewById(R.id.orgain_user_num_text);
        textView.setText("成员  " + organizationInfo.getMemberCount());

        // 帖子数据
        TextView postCountView = rootView.findViewById(R.id.orgain_post_num_text);
        postCountView.setText("帖子  " + organizationInfo.getPostCount());





    }





    @Override
    protected void onVisible(boolean isInit) {

    }
}
