package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.VideoGroupTransferHostingEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.TransferHostingAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.VideoGroupTransferhostingTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by ZHUDI on 2017/3/6.
 * 移交主持人
 */

public class TransferHostingActicity extends BaseActivity implements BaseView {
    private List<RoomBaseUser> userList;
    private long roomId;
    private TransferHostingAdapter transferHostingAdapter;
    private RoomBaseUser toBaseUser;
    private ChatRoomPresenter chatRoomPresenter;

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_transferhosting);
        setTitle();
        init();
    }

    private void init() {
        if (getIntent().hasExtra(Constants.Fields.ROOMNUMBER)) {
            roomId = getIntent().getLongExtra(Constants.Fields.ROOMNUMBER, 0);
        }
        if (getIntent().hasExtra(Constants.Fields.EXTRA)) {
            userList = getIntent().getParcelableArrayListExtra(Constants.Fields.EXTRA);
        }
        GridView gridView = findViewById(R.id.gridView);
        transferHostingAdapter = new TransferHostingAdapter(this);
        gridView.setAdapter(transferHostingAdapter);
        transferHostingAdapter.setData(userList);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toBaseUser = userList.get(position);
                VideoGroupTransferhostingTipDialog dialog = new VideoGroupTransferhostingTipDialog();
                dialog.show(getSupportFragmentManager());
            }
        });
    }

    /**
     * 移交主持人
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoGroupTransferHostingEvent event) {
        if (event == null) {
            return;
        }
        getChatRoomPresenter().transferHosting(roomId, 1, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), toBaseUser.getUserId());
}

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_TRANSFERHOSTING)) {
            if (object instanceof BaseResponse) {
                BaseResponse transferHostingResponse = (BaseResponse) object;
                int code = transferHostingResponse.getCode();
                if (code == 200) {
                    Utils.showToastShortTime("移交请求已发送");
                } else {
                    Utils.showToastShortTime(transferHostingResponse.getMsg());
                }
                finish();
            }
        }
    }

    private void setTitle() {
        ToolBarFragment fragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitleView("主持移交");
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (chatRoomPresenter != null) {
            chatRoomPresenter = null;
        }
        if (userList != null) {
            userList = null;
        }
    }
}
