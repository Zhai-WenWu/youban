package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.AddressBook;
import cn.bjhdltcdn.p2plive.ui.fragment.MessageAddressBookFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 选择通讯录页面
 */
public class SelectAddressBookActivity extends BaseActivity {

    private MessageAddressBookFragment mFragment;
    /**
     * type 默认0 分享 1添加好友
     */
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address_book_layout);
        type = getIntent().getIntExtra(Constants.Fields.TYPE, 0);
        setTitle();

        initView();

    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if (type == 1) {
            titleFragment.setTitleView("选择好友");
            titleFragment.setRightView("完成", R.color.color_ffb700,new ToolBarFragment.ViewOnclick() {
                @Override
                public void onClick() {
                    if (mFragment.getSelctlist() != null && mFragment.getSelctlist().size() > 0) {
                        for (AddressBook addressBook : mFragment.getSelctlist()) {
                            RongIMutils.sendSharedMessage("邀请", 30005, addressBook, mFragment.getObject());
                        }
                        finish();
                    } else {
                        Utils.showToastShortTime("请选择邀请好友");
                    }
                }
            });
        } else {
            titleFragment.setTitleView("分享给");
        }
    }


    private void initView() {
        mFragment = (MessageAddressBookFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = new MessageAddressBookFragment();
        }

        mFragment.onVisible(true);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);
    }


}
