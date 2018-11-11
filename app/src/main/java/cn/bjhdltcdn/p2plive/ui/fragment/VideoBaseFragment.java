package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.VideoChatActivity;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * Created by xiawenquan on 16/6/30.
 */
public abstract class VideoBaseFragment extends BaseFragment implements BaseView {

    private VideoChatActivity activity;
    private UserPresenter userPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (VideoChatActivity) context;
    }

    public VideoChatActivity getVideoChatActivity() {
        return activity;
    }

    public void setActivity(VideoChatActivity activity) {
        this.activity = activity;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    private ChatRoomPresenter chatRoomPresenter;

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity != null && activity.getBaseUser() != null) {
            long baseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
            getUserPresenter().attentionStatus(baseUserId, activity.getBaseUser().getUserId());
//            presenter.queryUserBalance(baseUserId);

        }

    }


    @Override
    public void updateView(String type, Object object) {
        updateUIView(type, object);
    }

    /**
     * 视频呼叫消息列表显示
     *
     * @param callType
     * @param type
     */
    public void oneToOneVideo(int callType, int type) {
        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getChatRoomPresenter().oneToOneVideo(userId, userId, getVideoChatActivity().getBaseUser().getUserId(),
                callType, type, getVideoChatActivity().getRoomNumber());
    }

    //    /***
//     * 接口回调UI控件
//     * @param id
//     * @return
//     */
    public abstract View getUIView(int id);
//
//    public abstract void getUIPresenter(VideoContract.Presenter presenter);
//
//    public abstract void updateUIAttentionStatus(int status);

    /**
     * 更新UI入口
     *
     * @param type
     * @param object
     */
    public abstract void updateUIView(String type, Object object);

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
