//package cn.bjhdltcdn.p2plive.ui.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Switch;
//import android.widget.Toast;
//
//import org.greenrobot.eventbus.EventBus;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.event.CreateAssociationEvent;
//import cn.bjhdltcdn.p2plive.ui.activity.CreateAssociationActivity;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//
///**
// * Created by zhaiww on 2018/6/11.
// */
//
//public class CreateAssociationFragmentStup0 extends Fragment {
//
//    private View rootView;
//    private Switch switch_no_name;
//    private Switch switch_custom;
//    private CheckBox checkbox_no_name_can_publish;
//    private Switch switch_creat_no_name_hoom;
//    private RadioGroup radiogroup_sex;
//    private RadioGroup radiogroup_content;
//    private RadioButton radio_btn_no_limit;
//    private RadioButton radio_btn_boy;
//    private RadioButton radio_btn_girl;
//    private RadioButton radio_all;
//    private RadioButton radio_btn_circle;
//    private Button stup0_next_btn_view;
//    private boolean noNameSwitch;
//    private boolean customSwitch;
//    private CreateAssociationActivity activity;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        rootView = inflater.inflate(R.layout.activity_create_circle, container, false);
//
//        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
//        ViewGroup parent = (ViewGroup) rootView.getParent();
//        if (parent != null) {
//            parent.removeView(rootView);
//        }
//
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        activity = (CreateAssociationActivity) getActivity();
//        initView();
//    }
//
//    private void initView() {
//        switch_no_name = rootView.findViewById(R.id.switch_no_name);
//        switch_custom = rootView.findViewById(R.id.switch_custom);
//        checkbox_no_name_can_publish = rootView.findViewById(R.id.checkbox_no_name_can_publish);
//        switch_creat_no_name_hoom = rootView.findViewById(R.id.switch_creat_no_name_hoom);
//        radiogroup_sex = rootView.findViewById(R.id.radiogroup_sex);
//        radiogroup_content = rootView.findViewById(R.id.radiogroup_content);
//        radio_btn_no_limit = rootView.findViewById(R.id.radio_btn_no_limit);
//        radio_btn_boy = rootView.findViewById(R.id.radio_btn_boy);
//        radio_btn_girl = rootView.findViewById(R.id.radio_btn_girl);
//        radio_all = rootView.findViewById(R.id.radio_all);
//        radio_btn_circle = rootView.findViewById(R.id.radio_btn_circle);
//        stup0_next_btn_view = rootView.findViewById(R.id.stup0_next_btn_view);
//        stup0_next_btn_view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (noNameSwitch || customSwitch) {
//                    EventBus.getDefault().post(new CreateAssociationEvent(1));
//                }
//            }
//        });
//
//        checkbox_no_name_can_publish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    activity.setAnonymousLimit(1);
//                } else {
//                    activity.setAnonymousLimit(2);
//                }
//            }
//        });
//
//        switch_creat_no_name_hoom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    activity.setIsCreateChatInfo(1);
//                    Utils.showToastLongTime("聊天室开放时间21:00-24:00，邀约好友一起来尬聊吧！");
//                } else {
//                    activity.setIsCreateChatInfo(0);
//                }
//            }
//        });
//
//        switch_no_name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                noNameSwitch = isChecked;
//                switch_custom.setChecked(!isChecked);
//                if (isChecked){
//                    activity.setType(3);
//                }
//                if (noNameSwitch || customSwitch) {
//                    stup0_next_btn_view.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                } else {
//                    stup0_next_btn_view.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                }
//            }
//        });
//        switch_custom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                customSwitch = isChecked;
//                switch_no_name.setChecked(!isChecked);
//                if (isChecked){
//                    activity.setType(0);
//                }
//                if (noNameSwitch || customSwitch) {
//                    stup0_next_btn_view.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
//                } else {
//                    stup0_next_btn_view.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
//                }
//            }
//        });
//
//        radiogroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == radio_btn_no_limit.getId()) {
//                    activity.setSexLimit(1);
//                } else if (checkedId == radio_btn_boy.getId()) {
//                    activity.setSexLimit(2);
//                } else if (checkedId == radio_btn_girl.getId()) {
//                    activity.setSexLimit(3);
//                }
//            }
//        });
//
//        radiogroup_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                if (checkedId == radio_all.getId()) {
//                    activity.setContentLimit(1);
//                } else if (checkedId == radio_btn_circle.getId()) {
//                    activity.setContentLimit(2);
//                }
//            }
//        });
//    }
//}
