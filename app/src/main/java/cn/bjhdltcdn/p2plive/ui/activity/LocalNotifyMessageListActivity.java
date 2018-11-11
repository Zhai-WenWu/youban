package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.fragment.NotifyApplyListItemFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.LocalNotifyMessageListFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;

/**
 * 本地消息列表
 */
public class LocalNotifyMessageListActivity extends BaseActivity {

    private Fragment mFragment;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_notify_message_list_layout);

        String title = getIntent().getStringExtra(Constants.KEY.KEY_OBJECT);
        type = getIntent().getIntExtra(Constants.KEY.KEY_TYPE, 0);

        setTitle(title);

        initView();


    }


    private void setTitle(String title) {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView(title);
        titleFragment.getLeftView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                finish();
            }
        });
    }

    private void initView() {


        switch (type) {
            case 1:
            case 2:

                // 本地消息列表
                mFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (mFragment == null || !(mFragment instanceof LocalNotifyMessageListFragment)) {
                    mFragment = LocalNotifyMessageListFragment.newInstance(type);
                }

                break;

            case 3:// 网络消息列表
                mFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (mFragment == null || !(mFragment instanceof NotifyApplyListItemFragment)) {
                    mFragment = NotifyApplyListItemFragment.newInstance(type);
                }

                break;
            default:
        }


        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);

    }


}
