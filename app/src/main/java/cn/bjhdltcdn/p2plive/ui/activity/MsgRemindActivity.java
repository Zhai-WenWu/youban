package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by ZHUDI on 2017/12/8.
 * 消息提醒
 */

public class MsgRemindActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    private ToggleButton togglebtn_voice, togglebtn_msg_private, togglebtn_msg_group,
            togglebtn_msg_notify, togglebtn_msg_comment, togglebtn_msg_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_remind);
        setTitle();
        init();
    }

    private void init() {
        togglebtn_voice = findViewById(R.id.togglebtn_voice);
        togglebtn_msg_private = findViewById(R.id.togglebtn_msg_private);
        togglebtn_msg_group = findViewById(R.id.togglebtn_msg_group);
        togglebtn_msg_notify = findViewById(R.id.togglebtn_msg_notify);
        togglebtn_msg_comment = findViewById(R.id.togglebtn_msg_comment);
        togglebtn_msg_join = findViewById(R.id.togglebtn_msg_join);
        togglebtn_voice.setOnCheckedChangeListener(this);
        togglebtn_msg_private.setOnCheckedChangeListener(this);
        togglebtn_msg_group.setOnCheckedChangeListener(this);
        togglebtn_msg_notify.setOnCheckedChangeListener(this);
        togglebtn_msg_comment.setOnCheckedChangeListener(this);
        togglebtn_msg_join.setOnCheckedChangeListener(this);
        togglebtn_voice.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_VOICE, true));
        togglebtn_msg_private.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_PRIVATE, true));
        togglebtn_msg_group.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_GROUP, true));
        togglebtn_msg_notify.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_NOTIFY, true));
        togglebtn_msg_comment.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_COMMENT, true));
        togglebtn_msg_join.setChecked(SafeSharePreferenceUtils.getBoolean(Constants.Fields.MSG_JOIN, true));
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.togglebtn_voice:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_VOICE, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_VOICE, false);
                }
                break;
            case R.id.togglebtn_msg_private:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_PRIVATE, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_PRIVATE, false);
                }
                break;
            case R.id.togglebtn_msg_group:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_GROUP, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_GROUP, false);
                }
                break;
            case R.id.togglebtn_msg_notify:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_NOTIFY, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_NOTIFY, false);
                }
                break;
            case R.id.togglebtn_msg_comment:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_COMMENT, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_COMMENT, false);
                }
                break;
            case R.id.togglebtn_msg_join:
                if (b) {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_JOIN, true);
                } else {
                    SafeSharePreferenceUtils.saveBoolean(Constants.Fields.MSG_JOIN, false);
                }
                break;
        }
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("消息提醒");
    }
}
