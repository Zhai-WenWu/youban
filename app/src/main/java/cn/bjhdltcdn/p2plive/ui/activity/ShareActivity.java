package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ShareSuccessEvent;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.SharePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ActivitySharedFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.OrganizationInfoFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.PkSharedFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.PostInfoSharedFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.RoomSharedFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.SayloveSharedFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * 分享页面
 */
public class ShareActivity extends BaseActivity implements BaseView {
    public SharePresenter sharePresenter;
    private Long userId;
    /**
     * 分享的类型(1帖子,2圈子,3活动,4房间,5PK挑战,6表白),
     */
    private int type;
    private long parentId;
    private String defaultImg;
    private OrganizationInfo organizationInfo;
    private SayLoveInfo sayLoveInfo;
    private ActivityInfo activityInfo;
    private PostInfo postInfo;
    private PlayInfo playInfo;
    private RoomInfo roomInfo;

    private EditText editView;
    private Fragment mFragment;
    private TextView countView;
    private ToolBarFragment titleFragment;

    public SharePresenter getSharePresenter() {
        if (sharePresenter == null) {
            sharePresenter = new SharePresenter(this);
        }
        return sharePresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_layout);

        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        Object object = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);

        type = getIntent().getIntExtra(Constants.KEY.KEY_TYPE, 0);

        parentId = getIntent().getLongExtra(Constants.KEY.KEY_PARENT_ID, 0);
        defaultImg=getIntent().getStringExtra(Constants.KEY.KEY_DEFAULT_IMG);

        setTitle();
        initView();

        mFragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            if (object instanceof OrganizationInfo) {
                organizationInfo = (OrganizationInfo) object;
                if (organizationInfo != null) {
                    mFragment = new OrganizationInfoFragment();
                    ((OrganizationInfoFragment) mFragment).setOrganizationInfo(organizationInfo);
                }
            } else if (object instanceof ActivityInfo) {
                activityInfo = (ActivityInfo) object;
                if (activityInfo != null) {
                    mFragment = new ActivitySharedFragment();
                    ((ActivitySharedFragment) mFragment).setObject(activityInfo);
                    ((ActivitySharedFragment)mFragment).setDefaultImg(defaultImg);
                }
            } else if (object instanceof SayLoveInfo) {
                sayLoveInfo = (SayLoveInfo) object;
                if (sayLoveInfo != null) {
                    mFragment = new SayloveSharedFragment();
                    ((SayloveSharedFragment) mFragment).setObject(sayLoveInfo);
                }
            } else if (object instanceof PostInfo) {
                postInfo = (PostInfo) object;
                if (postInfo != null) {
                    mFragment = new PostInfoSharedFragment();
                    ((PostInfoSharedFragment) mFragment).setObject(postInfo);
                }
            } else if (object instanceof PlayInfo) {
                playInfo = (PlayInfo) object;
                if (playInfo != null) {
                    mFragment = new PkSharedFragment();
                    ((PkSharedFragment)mFragment).setObject(playInfo);
                }
            }else if (object instanceof RoomInfo) {
                mFragment = new RoomSharedFragment();
                roomInfo= (RoomInfo) object;
                if (roomInfo != null) {
                    ((RoomSharedFragment)mFragment).setObject(roomInfo);
                }
            }
        }


        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);


    }

    private void setTitle() {
        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("分享转发");
        titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_ffb700));
        titleFragment.setRightView("发送", new ToolBarFragment.ViewOnclick() {
            @Override
            public void onClick() {
                getSharePresenter().saveShareAttention(userId, parentId, type, editView.getText().toString());
            }
        });
    }

    private void initView() {
        // 分享内容
        editView = findViewById(R.id.edit_view);
        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                countView.setText(s.toString().trim().length() + " / 200");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 计算器
        countView = findViewById(R.id.count_view);


    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }
            Utils.showToastShortTime(e.getMessage());
            return;
        }

        if (InterfaceUrl.URL_SAVESHAREATTENTION.equals(apiName)) {
            if (object instanceof NoParameterResponse) {

                NoParameterResponse response = (NoParameterResponse) object;

                if (response.getCode() == 200) {
                    Utils.showToastShortTime("分享成功");
                    EventBus.getDefault().post(new ShareSuccessEvent());
                    finish();

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }

            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (organizationInfo != null) {
            organizationInfo = null;
        }


    }
}
