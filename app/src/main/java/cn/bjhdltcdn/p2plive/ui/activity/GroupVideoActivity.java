package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.callback.RongYunUnreadCountCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.InitiateVoteEvent;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.event.VideoCMDTextMessageEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupGetUserInfoEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupManageEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupMicApplyEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupRoomCloseEvent;
import cn.bjhdltcdn.p2plive.event.VideoGroupTransferHostingEvent;
import cn.bjhdltcdn.p2plive.httpresponse.AttentionStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnWheatApplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnWheatListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOnlineUserListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOwnerInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.InitiateVoteResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateUserStatusResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.model.VoteInfo;
import cn.bjhdltcdn.p2plive.model.Wheat;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupVideoListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.VideoGroupMsgAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.VideoGroupUserListAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.GroupVideoUserDetailDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.InitiateVoteDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.SelectVoteUserDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.VideoGroupCloseRoomTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.VideoGroupManageDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.VideoGroupMicApplyDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.GroupVideoMessageListDialogFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CircleProgressView;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoCanvas;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by ZHUDI on 2017/11/23.
 */

public class GroupVideoActivity extends BaseEngineEventHandlerActivity implements BaseView, View.OnClickListener {

    private RecyclerView recycler_group, recycler_user, recycler_msg;
    private VideoGroupMsgAdapter msgAdapter;
    private VideoGroupUserListAdapter userAdapter;
    private GroupVideoListAdapter videoAdapter;
    private TextView tv_num;
    private TextView attention_view;
    private int attentionStatus;
    private ImageView img_small;//视频的放大和缩小
    private ImageView img_manage;//视频管理
    private View sendLayoutView;// 输入框
    private EditText editText;
    private RoomBaseUser currBaseUser;//当前登录用户
    private RoomBaseUser otherUser;//主持人操作的普通观众
    private RoomBaseUser ownerUser;//房主
    private View view_img_mic;
    private RelativeLayout rela_video_no;//首麦关闭摄像头显示画面
    private int micType;//1申请上麦,2上麦,3剔除下麦,4拒绝上麦5主动下麦
    private VideoGroupMicApplyDialog dialog_Apply;//用户申请列表弹出
    private boolean isCloseClick = false;//退出房间按钮是否点击
    private boolean isFirstJoin = true;//是否刚进入房间
    private boolean isDisableMsg = false;//是否被禁言
    private boolean isRemoveOut = false;//是否被移出房间
    private boolean isBigView = false;//是否放到全屏视图
    private boolean isLocalMicClose = false;//本地话筒是否关闭
    private boolean isLocalVideoClose = false;//本地摄像头是否关闭
    private boolean isOffline = false;//是否有其他麦上用户掉线
    private SurfaceView firstlView = null;
    private FrameLayout frame_first_mic;//主持人的视图
    private long bigViewUserId;//全屏视图时的用户id
    private boolean videoClick = false;//点击列表视图放大
    private boolean isTransferHosting;//是否移交过主持人
    private int transfer;//移交主持人请求状态  2同意 3拒绝
    private Handler handler;
    private Runnable runnable;
    private long lastTime = 0;
    /**
     * 声网美颜
     */
//    private AgoraYuvEnhancer yuvEnhancer = null;

    /**
     * 点击麦上的item下标
     */
    private int clickItemPosition = -1;


    /**
     * 1--->主持,2--->场控(管理员),3--->观众,4----房主
     */
    private int userRole;

    private ImageView voteUnenabeImageView, voteEnableImageView;
    private CircleProgressView circleProgressView;
    private Handler timerHandler;
    private Runnable voteTimeRunnable;
    private int maxProgerss = 180, progerss = 180;
    private int voteTime = 180;
    private VoteInfo voteInfo;
    private long voteId;
    private int openVote = 1;//1发起 2关闭,
    private int sort = 1;//1:麦上顺序 2：投票顺序
    private AnimationDrawable animationDrawable;//投票动画
    private BaseUser toBaseUser;//被投票的用户  用于显示投票的公屏消息
    private String imagePath;//分享的的房间的本地图片
    private long roomNumber;
    private String roomBg;
    private RoomInfo roomInfo;

    private ChatRoomPresenter chatRoomPresenter;
    private UserPresenter userPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.fragment_video_group);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        chatRoomPresenter = new ChatRoomPresenter(this);
        userPresenter = new UserPresenter(this);
        initData(getIntent());
    }


    private void initData(Intent intent) {


        if (intent.hasExtra(Constants.Fields.IMAGEPATH)) {
            roomBg = intent.getStringExtra(Constants.Fields.IMAGEPATH);
        }

        //进入房间
        if (intent.hasExtra(Constants.Fields.ROOMINFO)) {
            roomInfo = intent.getParcelableExtra(Constants.Fields.ROOMINFO);
            roomNumber = roomInfo.getRoomId();
            currBaseUser = roomInfo.getToBaseUser();
            if (currBaseUser != null) {
                userRole = currBaseUser.getUserRole();
                int isGag = currBaseUser.getIsGag();
                if (isGag == 1) {
                    isDisableMsg = false;
                } else if (isGag == 2) {
                    isDisableMsg = true;
                }
            }
        }

        //保证取消上次视频，防止其他视频接入时失败
        App.getInstance().getRtcEngine().leaveChannel();
        Utils.wakeUpAndUnlock(App.getInstance());

        if (currBaseUser == null) {
            GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), new GreenDaoUtils.ExecuteCallBack() {
                @Override
                public void callBack(Object object) {
                    BaseUser baseUser = (RoomBaseUser) object;
                    currBaseUser.setUserId(baseUser.getUserId());
                    currBaseUser.setNickName(baseUser.getNickName());
                    currBaseUser.setUserIcon(baseUser.getUserIcon());
                }
            });
        }


        setupRtcEngine();

        init();
        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, true);
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }

        chatRoomPresenter.getOwnerInfo(roomInfo == null ? 0 : roomInfo.getRoomId());
        startRequestOnlineUserList();
        chatRoomPresenter.getOnWheatList(roomNumber);

        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //获取View可见区域的bottom
                Rect rect = new Rect();
                getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
                if (bottom != 0 && oldBottom != 0 && bottom - rect.bottom <= 200) {
                    //键盘消失
                    if (sendLayoutView != null && sendLayoutView.getVisibility() == View.VISIBLE && editText.isFocused() && editText.getText().length() == 0) {
                        sendLayoutView.setVisibility(View.GONE);
                    }
                }
            }
        });

        // 获取未读数
        getUnreadCount();


    }

    private void init() {
        circleProgressView = findViewById(R.id.circleProgressbar);
        voteUnenabeImageView = findViewById(R.id.vote_unenable_image_view);
        voteEnableImageView = findViewById(R.id.vote_enable_image_view);

        attention_view = findViewById(R.id.attention_view);
        tv_num = findViewById(R.id.tv_num);
        findViewById(R.id.img_finish).setOnClickListener(this);

        img_small = findViewById(R.id.img_small);
        img_small.setOnClickListener(this);

        findViewById(R.id.img_msg_room).setOnClickListener(this);
        findViewById(R.id.img_msg_private).setOnClickListener(this);
        findViewById(R.id.img_mic).setOnClickListener(this);
        findViewById(R.id.img_share).setOnClickListener(this);
        img_manage = findViewById(R.id.img_manage);
        img_manage.setOnClickListener(this);
        view_img_mic = findViewById(R.id.view_img_mic);
        frame_first_mic = findViewById(R.id.frame_first_mic);
        rela_video_no = findViewById(R.id.rela_video_no);
        //多人视频列表
        recycler_group = findViewById(R.id.recycler_group);
        recycler_group.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 3);
        layoutManager.setAutoMeasureEnabled(true);
        recycler_group.setLayoutManager(layoutManager);
        recycler_group.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(1), 3, false));

        videoAdapter = new GroupVideoListAdapter();
        recycler_group.setAdapter(videoAdapter);

        // 下麦
        videoAdapter.setOnItemLongClickListener(new GroupVideoListAdapter.onItemLongClick() {
            @Override
            public void onItemLongClick(View view, int position) {
                if (videoAdapter.getList_video() != null) {
                    if (userRole == 1) {
                        VideoGroupMicApplyDialog dialog_Apply = new VideoGroupMicApplyDialog();
                        dialog_Apply.setType(3);
                        dialog_Apply.setToBaseUser(videoAdapter.getList_video().get(position).getUser());
                        dialog_Apply.show(getSupportFragmentManager());
                    }
                }
            }
        });

        // 放大
        videoAdapter.setOnItemClickListener(new GroupVideoListAdapter.onItemClick() {
            @Override
            public void onItemClick(View view, int position) {
                if (videoAdapter.getList_video().get(position).getUser() != null) {
                    isBigView = true;
                    videoClick = true;
                    bigViewUserId = videoAdapter.getList_video().get(position).getUser().getUserId();
                    if (videoAdapter.getList_video().get(position).getUser().getType() == 2) {
                        Utils.ImageViewDisplayByUrl(videoAdapter.getList_video().get(position).getUser().getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                        ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(videoAdapter.getList_video().get(position).getUser().getNickName());
                        rela_video_no.setVisibility(View.VISIBLE);
                    } else {
                        rela_video_no.setVisibility(View.GONE);
                    }
                    firstVideoDisplay(bigViewUserId);
                    videoDisplay();
                    img_small.setImageResource(R.drawable.video_group_view_small);
                    clickItemPosition = (position - 1);

                }
            }
        });

        //用户列表
        recycler_user = findViewById(R.id.recycler_user);
        recycler_user.setHasFixedSize(true);
        LinearLayoutManager hlayoutManager = new LinearLayoutManager(App.getInstance());
        hlayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycler_user.setLayoutManager(hlayoutManager);
        recycler_user.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10), false));
        userAdapter = new VideoGroupUserListAdapter();
        recycler_user.setAdapter(userAdapter);
        userAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {// 查看用户基本信息入口
                if (userAdapter.getItemCount() > 0) {
                    BaseUser baseUser = userAdapter.getList().get(position);
                    if (baseUser == null) {
                        return;
                    }

                    if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        return;
                    }

                    if (!isFinishing()) {
                        GroupVideoUserDetailDialog userDetailDialogFragment = new GroupVideoUserDetailDialog();
                        userDetailDialogFragment.setDate(baseUser, roomNumber);
                        userDetailDialogFragment.setUserRole(userRole);
                        userDetailDialogFragment.show(getSupportFragmentManager());
                    }
                }
            }
        });

        //公屏消息
        recycler_msg = findViewById(R.id.recycler_msg);
        recycler_msg.setHasFixedSize(true);
        LinearLayoutManager vlayoutManager = new LinearLayoutManager(App.getInstance());
        vlayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        vlayoutManager.setAutoMeasureEnabled(true);
        recycler_msg.setLayoutManager(vlayoutManager);
        recycler_msg.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(5), true));
        msgAdapter = new VideoGroupMsgAdapter();
        recycler_msg.setAdapter(msgAdapter);

        sendLayoutView = findViewById(R.id.send_layout_view);
        editText = sendLayoutView.findViewById(R.id.reply_edit_input);
        Button sendView = sendLayoutView.findViewById(R.id.send_view);
        sendView.setEnabled(true);
        sendView.setOnClickListener(this);
    }

    /**
     * 每10s请求一次在线用户列表
     */
    private void startRequestOnlineUserList() {
        runnable = new Runnable() {
            @Override
            public void run() {
                chatRoomPresenter.getOnlineUserList(roomNumber);
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_finish:// 关闭按钮
                if (userRole == 1) {//主持人
                    VideoGroupCloseRoomTipDialog dialog = new VideoGroupCloseRoomTipDialog();
                    dialog.show(getSupportFragmentManager());
                } else {
                    if (videoAdapter.getMicUserIdList().size() > 0 && videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {//嘉宾（除主持人外的主播）
                        isCloseClick = true;
                        micType = 3;//接口回调switch进入需要
                        chatRoomPresenter.upOrDownWheat(currBaseUser.getUserId(), currBaseUser.getUserId(), roomNumber, 3, 0);//关闭房间前先下麦
                    } else {//普通观众
                        chatRoomPresenter.updateUserStatus(currBaseUser.getUserId(), roomNumber, 1, 0, "");//1为主动退出房间
                    }
                }
                break;
            case R.id.img_small:
                if (System.currentTimeMillis() - lastTime > 2000) {
                    lastTime = System.currentTimeMillis();
                    isBigView = !isBigView;
                    if (isBigView) {
                        img_small.setImageResource(R.drawable.video_group_view_small);
                        bigViewUserId = videoAdapter.getMicUserIdList().get(0);
                    } else {
                        img_small.setImageResource(R.drawable.video_group_view_big);
                    }
                    firstVideoDisplay(videoAdapter.getMicUserIdList().get(0));
                    videoDisplay();

                    // 缩小的时候需要刷新
                    if (!isBigView) {
                        // 更新麦上的人刷新视频
                        if (clickItemPosition > -1) {
                            if (videoAdapter.getList_video() != null) {
                                if (videoAdapter.getList_video().get(0).getUser().getType() == 2) {
                                    Utils.ImageViewDisplayByUrl(videoAdapter.getList_video().get(0).getUser().getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                                    ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(videoAdapter.getList_video().get(0).getUser().getNickName());
                                    rela_video_no.setVisibility(View.VISIBLE);
                                } else {
                                    rela_video_no.setVisibility(View.GONE);
                                }

                                Wheat wheat = videoAdapter.getList_video().get(clickItemPosition + 1);
                                if (wheat != null) {
                                    // 更新别人画面
                                    if (wheat.getUser() != null && wheat.getUser().getUserId() != currBaseUser.getUserId()) {
                                        videoAdapter.notifyItemChanged(clickItemPosition);
                                    }
                                }
                            }
                        }

                        if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {
                            // 更新自己画面
                            videoAdapter.setLocalVideoClose(isLocalVideoClose);
                            videoAdapter.setLocalMicClose(isLocalMicClose);
                            videoAdapter.updateCurrUserView();
                        }

                    }

                    if (videoClick) {
                        videoClick = false;
                    }

                    clickItemPosition = -1;

                }
                break;
            case R.id.img_msg_room:
                if (isDisableMsg) {
                    Utils.showToastShortTime("您已被管理员禁言!");
                } else {
                    sendLayoutView.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.img_msg_private:// 私信入口

                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        if (conversations == null || conversations.isEmpty()) {
                            Utils.showToastShortTime("暂无私信");
                            return;
                        }

                        GroupVideoMessageListDialogFragment messageListDialogFragment = new GroupVideoMessageListDialogFragment();
                        messageListDialogFragment.show(getSupportFragmentManager());
                        RongIMutils.setIsCanOnUserPortraitClick(false);

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        // 发生错误强制吊起私信对话框
                        GroupVideoMessageListDialogFragment messageListDialogFragment = new GroupVideoMessageListDialogFragment();
                        messageListDialogFragment.show(getSupportFragmentManager());
                        RongIMutils.setIsCanOnUserPortraitClick(false);

                    }
                });


                break;
            case R.id.img_mic: // 上麦入口
                if (userRole == 1) {//主持
                    view_img_mic.setVisibility(View.GONE);
                    chatRoomPresenter.getOnWheatApplyList(roomNumber);
                } else {//观众
                    VideoGroupMicApplyDialog dialog_Apply = new VideoGroupMicApplyDialog();
                    if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {//在麦上
                        dialog_Apply.setType(5);
                    } else {
                        dialog_Apply.setType(1);
                    }
                    dialog_Apply.show(getSupportFragmentManager());
                }
                break;
            case R.id.img_share:// 分享入口
                String imgUrl = roomInfo.getBackgroundUrl();
                if (TextUtils.isEmpty(imgUrl)) {
                    imgUrl = Constants.Fields.ROOM_BG_URL;
                    roomInfo.setBackgroundUrl(imgUrl);
                }
                ShareUtil.getInstance().showShare(GroupVideoActivity.this, ShareUtil.VIDEO, roomNumber, roomInfo, null, "",
                        "多人聊天：“" + roomInfo.getRoomName() + "”邀你一起来参与聊天", imgUrl, true);
//                        mPresenter.shareRoom(DBUtils.getInstance().getBaseUser().getUserId(), roomInfo.getRoomId());
                break;
            case R.id.img_manage:// 管理入口
                VideoGroupManageDialog dialog = new VideoGroupManageDialog();
                dialog.setLocalMicClose(isLocalMicClose);
                dialog.setLocalVideoClose(isLocalVideoClose);
                dialog.setUserRole(userRole);
                dialog.setOpenVote(openVote);
                dialog.setOnWheatUserNum(videoAdapter.getMicUserOutMyselfList().size());
                dialog.show(getSupportFragmentManager());
                break;
            case R.id.send_view:// 消息入口
                if (editText != null) {
                    String text = editText.getText().toString();
                    if (StringUtils.isEmpty(text)) {
                        Toast.makeText(App.getInstance(), "内容不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        sendLayoutView.setVisibility(View.GONE);
                        Utils.hideSystemSoftInputKeyboard(editText);
                        editText.setText("");
                        RongIMutils.sendGroupVideoMessage(roomNumber, 60001, text, 0);
                    }
                }
                break;

        }
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETONWHEATLIST:
                if (object instanceof GetOnWheatListResponse) {
                    GetOnWheatListResponse getOnWheatListResponse = (GetOnWheatListResponse) object;
                    if (getOnWheatListResponse.getCode() == 200) {
                        List<Wheat> list_group = getOnWheatListResponse.getOnWheatList();
                        if (isFirstJoin) {//语音连麦在麦上，进程被杀掉，再次进入保持关闭摄像头状态
                            if (list_group != null) {
                                for (Wheat wheat : list_group) {
                                    int isUser = wheat.getIsUser();
                                    if (isUser == 1) {
                                        RoomBaseUser user = wheat.getUser();
                                        if (user.getUserId() == currBaseUser.getUserId()) {
                                            if (user.getType() == 2) {
                                                isLocalVideoClose = true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        videoAdapter.setLocalVideoClose(isLocalVideoClose);
                        videoAdapter.setLocalMicClose(isLocalMicClose);
                        videoAdapter.setData(list_group);
                        if (list_group != null) {
                            if (videoAdapter.getMicUserOutMyselfList() != null && videoAdapter.getMicUserOutMyselfList().size() > 0) {
                                //麦上有人的时候
                                if (timerHandler == null) {
                                    initVoteView();
                                }

                            } else {
                                //麦上没人的时候
                                circleProgressView.setVisibility(View.GONE);
                                voteUnenabeImageView.setVisibility(View.GONE);
                                voteEnableImageView.setVisibility(View.GONE);
                                if (timerHandler != null && voteTimeRunnable != null) {
                                    timerHandler.removeCallbacks(voteTimeRunnable);
                                    timerHandler = null;
                                }
                            }
                        }
                        if (userRole != 1) {//非主持人
                            if (micType == 2) {//普通观众上麦成功，并刷新本地布局成功后，通知房间内的人刷新页面
                                img_manage.setVisibility(View.VISIBLE);
                                RongIMutils.sendGroupVideoMessage(roomNumber, 60006, null, 0);
                                micType = 0;//防止自己上下麦后，mictype不变，麦序变化时向全频道发送广播

                            } else if (micType == 3) {//下麦成功
                                if (currBaseUser.getUserId() == bigViewUserId) {//自己在麦上且为全屏视图
                                    isBigView = false;
                                }
                                img_manage.setVisibility(View.GONE);
                                App.getInstance().getRtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE, null);
                                RongIMutils.sendGroupVideoMessage(roomNumber, 60006, null, 0);
                                micType = 0;//防止自己上下麦后，mictype不变，麦序变化时向全频道发送广播
                                if (isRemoveOut) {
                                    Utils.showToastShortTime("您被管理员踢出房间");
                                    finish();
                                }
                            } else if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {//后台强杀进程再进后，管理按钮显示
                                img_manage.setVisibility(View.VISIBLE);
                            }

                        } else {
                            img_manage.setVisibility(View.VISIBLE);
                        }

                        if (!isBigView) {
                            if (isFirstJoin || isTransferHosting || videoClick) {
                                int isUser = list_group.get(0).getIsUser();
                                RoomBaseUser user = list_group.get(0).getUser();
                                if (isUser == 1) {
                                    firstVideoDisplay(user.getUserId());
                                    if (user.getType() == 2) {
                                        Utils.ImageViewDisplayByUrl(user.getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                                        ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(user.getNickName());
                                        rela_video_no.setVisibility(View.VISIBLE);
                                    } else {
                                        rela_video_no.setVisibility(View.GONE);
                                    }
                                }
                                if (isTransferHosting) {
                                    isTransferHosting = false;
                                }
                            }
                            videoDisplay();
                        }
                    }
                }

                break;
            case InterfaceUrl.URL_GETONLINEUSERLIST:
                if (object instanceof GetOnlineUserListResponse) {
                    GetOnlineUserListResponse getOnlineUserListResponse = (GetOnlineUserListResponse) object;
                    int code = getOnlineUserListResponse.getCode();
                    if (code == 200) {
                        List<RoomBaseUser> list = getOnlineUserListResponse.getRoomUserList();
                        userAdapter.setData(list);
                        tv_num.setText("在线" + getOnlineUserListResponse.getTotal() + "人");
                    }
                }

                break;
            case InterfaceUrl.URL_UPDATEUSERSTATUS://关闭或被移出房间
                if (object instanceof UpdateUserStatusResponse) {
                    UpdateUserStatusResponse response = (UpdateUserStatusResponse) object;
                    if (response.getCode() == 200) {
                        App.getInstance().getRtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
                        finish();
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEROOMSTATUS://主持人关闭房间
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() != 200) {
                        Utils.showToastShortTime(response.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_GETONWHEATPAALYLIST:
                view_img_mic.setVisibility(View.GONE);
                if (object instanceof GetOnWheatApplyListResponse) {
                    GetOnWheatApplyListResponse getOnWheatApplyListResponse = (GetOnWheatApplyListResponse) object;
                    List<RoomBaseUser> list = getOnWheatApplyListResponse.getOnWheatList();
                    dialog_Apply = new VideoGroupMicApplyDialog();
                    dialog_Apply.setType(2);
                    dialog_Apply.setList(list);
                    dialog_Apply.show(getSupportFragmentManager());
                }
                break;
            case InterfaceUrl.URL_UPORDOWNWHEAT:
                if (object instanceof BaseResponse) {
                    BaseResponse upOrDownWheatResponse = (BaseResponse) object;
                    int code = upOrDownWheatResponse.getCode();
                    if (code == 200) {
                        switch (micType) {
                            case 1://观众申请上麦
                                Utils.showToastShortTime("申请已发送，请等待处理");
                                break;
                            case 2://上麦--主持同意,通知请求者
                                dialog_Apply.setDate(otherUser);
                                break;
                            case 3://下麦--主持移除或者主动下麦/掉线
                                if (userRole == 1) {//主持移除
                                    if (isOffline) {
                                        RongIMutils.sendGroupVideoMessage(roomNumber, 60006, null, 0);
                                        chatRoomPresenter.getOnWheatList(roomNumber);
                                    }
                                } else {//主动
                                    if (isCloseClick) {//关闭房间
                                        RongIMutils.sendGroupVideoMessage(roomNumber, 60006, null, 0);
                                        chatRoomPresenter.updateUserStatus(currBaseUser.getUserId(), roomNumber, 1, 0, "");//1为主动退出房间
                                    } else {//下麦
                                        chatRoomPresenter.getOnWheatList(roomNumber);
                                    }
                                }
                                break;
                            case 4://拒绝--主持
                                dialog_Apply.setDate(otherUser);
                                break;

                        }
                    } else {
                        Utils.showToastShortTime(upOrDownWheatResponse.getMsg());
                    }
                }
                break;
//            case InterfaceUrl.URL_SHAREROOM:
//                if (object instanceof BaseResponse) {
//                    BaseResponse shareRoomResponse = (BaseResponse) object;
//                    int code = shareRoomResponse.getCode();
//                    if (code == 200) {
//                        Utils.showToastShortTime("分享成功");
//                    } else {
//                        Utils.showToastShortTime("分享失败");
//                    }
//                }
//                break;
            case InterfaceUrl.URL_TRANSFERHOSTING:
                if (object instanceof BaseResponse) {
                    BaseResponse transferHostingResponse = (BaseResponse) object;
                    int code = transferHostingResponse.getCode();
                    if (code == 200) {
                        if (transfer == 2) {//同意
                            userRole = 1;
                            Utils.showToastShortTime("您已成为房间主持人");
                            if (isLocalVideoClose) {
                                rela_video_no.setVisibility(View.VISIBLE);
                                Utils.ImageViewDisplayByUrl(currBaseUser.getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                                ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(currBaseUser.getNickName());
                            }
                        } else if (transfer == 3) {//拒绝
                            Utils.showToastShortTime("您已拒绝移交主持人申请");
                        }
                    } else {
                        Utils.showToastShortTime(transferHostingResponse.getMsg());
                    }
                }
                break;
            case InterfaceUrl.URL_USERVOTING:
                if (object instanceof BaseResponse) {
                    BaseResponse userVotingResponse = (BaseResponse) object;
                    int code = userVotingResponse.getCode();
                    if (code == 200) {
                        Toast toast = new Toast(this);
                        toast.setGravity(Gravity.TOP, 0, PlatformInfoUtils.getWidthOrHeight(this)[1] / 2);
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(LayoutInflater.from(this).inflate(R.layout.toast_vote_success_layout, null));
                        toast.show();
                    } else {
                        Utils.showToastShortTime("投票失败");
                    }
                    circleProgressView.setVisibility(View.VISIBLE);
                    voteUnenabeImageView.setVisibility(View.VISIBLE);
                    voteEnableImageView.setVisibility(View.GONE);
                    animationDrawable.stop();
                    progerss = voteTime;
                    circleProgressView.setProgress(0);
                    circleProgressView.setmTxtHint2(String.format("%2d:%02d", progerss / 60, progerss % 60));
                    circleProgressView.setEnabled(false);
                    timerHandler.postDelayed(voteTimeRunnable, 1000);

                }
                break;
            case InterfaceUrl.URL_INITIATEVOTE:
                if (object instanceof InitiateVoteResponse) {
                    InitiateVoteResponse initiateVoteResponse = (InitiateVoteResponse) object;
                    int code = initiateVoteResponse.getCode();
                    if (code == 200) {

                    }
                }
                break;
            case InterfaceUrl.URL_GETCURRENTVOTEDATA:
//                if (object instanceof GetCurrentVoteDataResponse) {
//                    GetCurrentVoteDataResponse getCurrentVoteDataResponse = (GetCurrentVoteDataResponse) object;
//                    int code = getCurrentVoteDataResponse.getCode();
//                    if (sort == 1) {
//                        if (code == 200) {
//                            //PK投票
//                            if (getCurrentVoteDataResponse.getVoteRecordList() == null || getCurrentVoteDataResponse.getVoteRecordList().size() <= 0) {
//                                return;
//                            }
//                            SelectVoteUserDialog selectVoteUserDialog = new SelectVoteUserDialog();
//                            selectVoteUserDialog.setType(2);
//                            selectVoteUserDialog.setVoteRecordList(getCurrentVoteDataResponse.getVoteRecordList());
//                            selectVoteUserDialog.setOnClickListener(new SelectVoteUserDialog.onClickListener() {
//                                @Override
//                                public void onClick(BaseUser baseUser) {
//                                    toBaseUser = baseUser;
//                                    videoAdapter.userVoting(roomInfo.getRoomId(), DBUtils.getInstance().getBaseUser().getUserId(), baseUser.getUserId(), voteInfo == null ? 0 : voteInfo.getVoteId());
//
//                                }
//                            });
//                            selectVoteUserDialog.show(getChildFragmentManager());
//                        }
//                    } else if (sort == 2) {
//                        if (code == 200) {
//                            //获取本轮投票数据成功
//                            VotePKResultDialog votePKResultDialog = new VotePKResultDialog();
//                            votePKResultDialog.setVoteRecordList(getCurrentVoteDataResponse.getVoteRecordList());
//                            votePKResultDialog.show(getChildFragmentManager());
//                        }
//                    }
//                }
                break;
            case InterfaceUrl.URL_GETOWNERINFO:
                if (object instanceof GetOwnerInfoResponse) {
                    GetOwnerInfoResponse getOwnerInfoResponse = (GetOwnerInfoResponse) object;
                    int code = getOwnerInfoResponse.getCode();
                    if (code == 200) {
                        ownerUser = getOwnerInfoResponse.getBaseUser();
                        Utils.ImageViewDisplayByUrl(ownerUser.getUserIcon(), (CircleImageView) findViewById(R.id.img_icon_owner));
                        if (ownerUser.getUserId() == currBaseUser.getUserId()) {//自己为房主不能关注自己
                            attention_view.setVisibility(View.GONE);
                        } else {
                            attention_view.setVisibility(View.VISIBLE);
                            userPresenter.attentionStatus(currBaseUser.getUserId(), ownerUser.getUserId());
                        }
                        voteTime = getOwnerInfoResponse.getVoteTimer();
                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONSTATUS:
                if (object instanceof AttentionStatusResponse) {
                    AttentionStatusResponse response = (AttentionStatusResponse) object;
                    if (response.getCode() == 200) {
                        this.attentionStatus = response.getAttentionStatus();
                        switch (attentionStatus) {
                            case 1://双向关注
                            case 2://当前用户单向关注对方  不能发起视频
                                attention_view.setText("已关注");
                                attention_view.setTextColor(getResources().getColor(R.color.white));
                                attention_view.setBackground(getResources().getDrawable(R.drawable.shape_round_60_solid_30020202));
                                attention_view.setEnabled(false);
                                break;
                            case 3: // 双向不关注 视频和聊天都不可用
                            case 4: // 对方关注我，我不关注对方
                                attention_view.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        userPresenter.attentionOperation(1, currBaseUser.getUserId(), ownerUser.getUserId());
                                    }
                                });
                                break;
                        }

                    }
                }
                break;
            case InterfaceUrl.URL_ATTENTIONOPERATION:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {//关注类型(1-->关注;2-->取消关注)
                        attention_view.setText("已关注");
                        attention_view.setTextColor(getResources().getColor(R.color.white));
                        attention_view.setBackground(getResources().getDrawable(R.drawable.shape_round_60_solid_30020202));
                        attention_view.setEnabled(false);
                    }
                }
                break;


            case InterfaceUrl.URL_CONTROLUSERCAMERA:
                if (object instanceof BaseResponse) {
                    BaseResponse controlUserCameraResponse = (BaseResponse) object;
                    if (controlUserCameraResponse.getCode() == 200) {
                        isLocalVideoClose = !isLocalVideoClose;
                        App.getInstance().getRtcEngine().muteLocalVideoStream(isLocalVideoClose);
                        getRemoteVideoStatus((int) currBaseUser.getUserId(), isLocalVideoClose);
                        if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {
                            // 更新自己画面
                            videoAdapter.setLocalVideoClose(isLocalVideoClose);
                            videoAdapter.updateCurrUserView();
                        }
                    }
                }
                break;
        }
    }

    /**
     * 第一麦显示
     */
    public void firstVideoDisplay(long userId) {
        if (isFirstJoin || (videoClick && bigViewUserId == currBaseUser.getUserId()) || isTransferHosting) {
            App.getInstance().getRtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            if (firstlView == null) {
                firstlView = App.getInstance().getRtcEngine().CreateRendererView(App.getInstance());
            }
        }

        if (frame_first_mic.getChildCount() <= 0) {
            frame_first_mic.addView(firstlView);
        }

        if (userId == currBaseUser.getUserId()) {
            // 防止大屏幕是同一个人的时候，放大和缩小出现黑屏现象
            Object tagValue = frame_first_mic.getTag();
            if (tagValue == null || (long) tagValue != userId) {
                App.getInstance().getRtcEngine().setVideoProfile(IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_360P_8, true);
                App.getInstance().getRtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER, null);
                if (isFirstJoin && userRole == 1) {//主持人杀掉进入摄像头关闭
                    App.getInstance().getRtcEngine().muteLocalVideoStream(isLocalVideoClose);
                }
                App.getInstance().getRtcEngine().setupLocalVideo(new VideoCanvas(firstlView));
            }

        } else {
            if (!isBigView) {//如果最大化的时候不需要改变当前用户身份
                App.getInstance().getRtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE, null);
            } else {
                if (videoAdapter.getMicUserIdList().get(0).longValue() != userId) { //放大的非主持人
                    App.getInstance().getRtcEngine().setRemoteVideoStreamType(videoAdapter.getMicUserIdList().get(0).intValue(), io.agora.rtc.Constants.VIDEO_STREAM_LOW);
                }
            }
            App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(firstlView, VideoCanvas.RENDER_MODE_HIDDEN, (int) userId));
            //设置大流，非当前用户的任一主播切换到大图时高清显示
            App.getInstance().getRtcEngine().setRemoteVideoStreamType((int) userId, io.agora.rtc.Constants.VIDEO_STREAM_HIGH);
        }

        if (isFirstJoin) {
            setupChannel();
            //第一次进入房间设置大流，主持人图像高清显示
            isFirstJoin = false;
        }

        frame_first_mic.setTag(userId);

    }

    /**
     * 获取对方视频开关状态改变
     */
    public void getRemoteVideoStatus(int uid, final boolean enabled) {
        Logger.d("获取对方视频开关状态改变===" + uid + enabled);
        final long userId = Long.valueOf(String.valueOf(uid));
        if (videoAdapter.getMicUserMap().containsKey(userId)) {
            final int index = videoAdapter.getMicUserMap().get(userId);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isBigView) {
                        if (bigViewUserId == userId) {//当放大视图的用户关闭开启摄像头
                            if (enabled) {//暂停视频流发送
                                if (videoAdapter.getList_video() != null) {
                                    videoAdapter.getList_video().get(index).getUser().setType(2);
                                    Utils.ImageViewDisplayByUrl(videoAdapter.getList_video().get(index).getUser().getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                                    ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(videoAdapter.getList_video().get(index).getUser().getNickName());
                                }
                                rela_video_no.setVisibility(View.VISIBLE);
                            } else {
                                videoAdapter.getList_video().get(index).getUser().setType(1);
                                rela_video_no.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        if (index == 0) {//主持人
                            if (enabled) {//暂停视频流发送
                                if (videoAdapter.getList_video() != null) {
                                    videoAdapter.getList_video().get(index).getUser().setType(2);
                                    Utils.ImageViewDisplayByUrl(videoAdapter.getList_video().get(index).getUser().getUserIcon(), (CircleImageView) rela_video_no.findViewById(R.id.img_icon));
                                    ((TextView) rela_video_no.findViewById(R.id.tv_nickname)).setText(videoAdapter.getList_video().get(index).getUser().getNickName());
                                }
                                rela_video_no.setVisibility(View.VISIBLE);
                            } else {
                                videoAdapter.getList_video().get(index).getUser().setType(1);
                                rela_video_no.setVisibility(View.GONE);
                            }
                        } else {
                            if (videoAdapter.getList_video() != null && videoAdapter.getList_video().get(index).getUser() != null) {
                                if (enabled) {
                                    videoAdapter.getList_video().get(index).getUser().setType(2);
                                } else {
                                    videoAdapter.getList_video().get(index).getUser().setType(1);
                                }
                                videoAdapter.notifyItemChanged(index - 1);
                            }
                        }
                    }
                }
            });

        }
    }

    /**
     * 投票
     */
    public void initVoteView() {
        maxProgerss = voteTime;
        progerss = voteTime;
        animationDrawable = (AnimationDrawable) voteEnableImageView.getDrawable();
        circleProgressView.setVisibility(View.VISIBLE);
        voteUnenabeImageView.setVisibility(View.VISIBLE);
        //只有多人娱乐房 才可以投票
        timerHandler = new Handler();
        circleProgressView.setMaxProgress(maxProgerss);
        circleProgressView.setProgress(0);
        circleProgressView.setmTxtHint2(String.format("%2d:%02d", progerss / 60, progerss % 60));
        circleProgressView.setEnabled(false);
        if (voteTimeRunnable == null) {
            voteTimeRunnable = new Runnable() {
                @Override
                public void run() {
                    progerss = progerss - 1;
                    circleProgressView.setmTxtHint2(String.format("%2d:%02d", progerss / 60, progerss % 60));
                    circleProgressView.setProgress(maxProgerss - progerss);
                    if (progerss > 0) {
                        timerHandler.postDelayed(this, 1000);
                    } else {
                        circleProgressView.setVisibility(View.GONE);
                        voteUnenabeImageView.setVisibility(View.GONE);
                        voteEnableImageView.setVisibility(View.VISIBLE);
                        animationDrawable.start();
                    }
                }
            };
        }
        timerHandler.postDelayed(voteTimeRunnable, 1000);
        voteEnableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voteInfo == null) {
                    //常驻
                    if (videoAdapter.getMicUserOutMyselfList() == null || videoAdapter.getMicUserOutMyselfList().size() <= 0) {
                        return;
                    }
                    SelectVoteUserDialog selectVoteUserDialog = new SelectVoteUserDialog();
                    selectVoteUserDialog.setType(1);
                    selectVoteUserDialog.setUserList(videoAdapter.getMicUserList());
                    selectVoteUserDialog.setOnClickListener(new SelectVoteUserDialog.onClickListener() {
                        @Override
                        public void onClick(BaseUser baseUser) {
                            toBaseUser = baseUser;
                            chatRoomPresenter.userVoting(roomInfo.getRoomId(), currBaseUser.getUserId(), baseUser.getUserId(), voteInfo == null ? 0 : voteInfo.getVoteId());

                        }
                    });
                    selectVoteUserDialog.show(getSupportFragmentManager());
                } else {
                    //PK
                    sort = 1;
                    chatRoomPresenter.getCurrentVoteData(0, roomInfo.getRoomId(), voteId, sort);
                }

            }
        });
    }

    /**
     * 主持人发起/关闭 投票
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(InitiateVoteEvent event) {
        if (event == null) {
            return;
        }
        voteId = event.getVoteId();
        if (event.getType() == 1) {
            //发起投票
            if (voteInfo == null) {
                voteInfo = new VoteInfo();
            }
            voteInfo.setVoteId(event.getVoteId());
            openVote = 2;
        } else if (event.getType() == 2) {
            //关闭投票 才可以查看PK投票结果
            voteInfo = null;
            sort = 2;
            chatRoomPresenter.getCurrentVoteData(0, roomInfo.getRoomId(), voteId, sort);
            openVote = 1;
        }
    }

    /**
     * 聊天室广播
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoCMDTextMessageEvent event) {
        if (event == null) {
            return;
        }
        final VideoCMDTextMessageModel message = event.getTextMessageModel();
        if (message == null) {
            return;
        }

        if (msgAdapter == null) {
            return;
        }
//        case 60001://聊天消息
//        case 60006://刷新麦序
//        case 60007://关闭房间
//        case 60008://移交主持人刷新
//        case 60009://禁言
//        case 60010://送礼物
//        case 60011://投票
//        case 60012://关注
//        case 60013://温馨提示
//        case 60014://进入房间
        switch (message.getMessageType()) {
            case 60001://公屏消息
            case 60009://禁言
            case 60011://投票
            case 60012://关注
            case 60013://温馨提示
            case 60014://进入房间
                recycler_msg.setVisibility(View.VISIBLE);
                msgAdapter.addItemData(message);
                recycler_msg.getLayoutManager().scrollToPosition(msgAdapter.getItemCount() - 1);
                break;
            case 60008://移交主持人
                isTransferHosting = true;
            case 60006://麦上人数变化
                BaseUser messageUser = message.getBaseUser();
                if (messageUser != null && messageUser.getUserId() == bigViewUserId) {//麦序变化时，全屏视图用户在麦序上（全屏视图用户下麦）
                    isBigView = false;
                }
                chatRoomPresenter.getOnWheatList(roomNumber);
                break;
            case 60007://主持人关闭房间
                App.getInstance().getRtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
                Intent intent = new Intent(this, ShowCurrentRoomGoldStatisticDialogActivity.class);
                intent.putExtra(Constants.Fields.ROOMNUMBER, roomInfo.getRoomId());
                String imgUrl = roomInfo.getBackgroundUrl();
                if (TextUtils.isEmpty(imgUrl)) {
                    imgUrl = Constants.Fields.ROOM_BG_URL;
                }
                intent.putExtra(Constants.Fields.IMAGEPATH, imgUrl);
                intent.putExtra(Constants.Fields.ROOMINFO, roomInfo);
                startActivity(intent);
                finish();
                break;


        }
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
        final int type = event.getType();
        final BaseUser baseUser = event.getBaseUser();
        switch (type) {
            case 1://收到移交请求
                final Dialog dialog = new Dialog(this, R.style.select_img_dialog);
                dialog.setContentView(R.layout.dialog_video_group_transferhosting);
                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                tv_title.setText(baseUser.getNickName() + "要将主持移交给你");

                Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
                Button btn_ok = (Button) dialog.findViewById(R.id.btn_ok);
                btn_cancel.setText("拒绝");
                btn_ok.setText("接受");
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transfer = 3;
                        chatRoomPresenter.transferHosting(roomNumber, 3, currBaseUser.getUserId(), baseUser.getUserId());
                        dialog.dismiss();
                    }
                });

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transfer = 2;
                        chatRoomPresenter.transferHosting(roomNumber, 2, currBaseUser.getUserId(), baseUser.getUserId());
                        dialog.dismiss();
                    }
                });
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
                    }
                });
                dialog.show();
                break;
            case 2://收到同意
                Utils.showToastShortTime("主持人移交成功");
                userRole = 3;
                isTransferHosting = true;
                isBigView = false;
                chatRoomPresenter.getOnWheatList(roomNumber);
                break;
            case 3://收到拒绝
                Utils.showToastShortTime(baseUser.getNickName() + "拒绝成为主持人");
                break;
        }

    }

    /**
     * 房间内被移除
     * 管理按钮点击弹出
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoGroupManageEvent event) {
        if (event == null) {
            return;
        }
        switch (event.getType()) {
            case 1://翻转摄像头
                App.getInstance().getRtcEngine().switchCamera();
                break;
            case 2://关闭话筒
                isLocalMicClose = !isLocalMicClose;
                if (isLocalMicClose) {
                    Utils.showToastShortTime("关闭话筒成功");
                } else {
                    Utils.showToastShortTime("打开话筒成功");
                }
                App.getInstance().getRtcEngine().muteLocalAudioStream(isLocalMicClose);
                if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {
                    // 更新自己画面
                    videoAdapter.setLocalMicClose(isLocalMicClose);
                    videoAdapter.updateCurrUserView();
                }
                break;
            case 3://关闭摄像头
                if (isLocalVideoClose) {
                    chatRoomPresenter.controlUserCamera(currBaseUser.getUserId(), roomInfo.getRoomId(), 1);
                } else {
                    chatRoomPresenter.controlUserCamera(currBaseUser.getUserId(), roomInfo.getRoomId(), 2);
                }
                break;
            case 4://主持移交
                Intent intent = new Intent(this, TransferHostingActicity.class);
                intent.putExtra(Constants.Fields.ROOMNUMBER, roomNumber);
                intent.putParcelableArrayListExtra(Constants.Fields.EXTRA, videoAdapter.getMicUserOutMyselfList());
                startActivity(intent);
                break;
            case 5://打开或者关闭PK
                if (openVote == 1) {
                    //主持人发起投票接口
                    InitiateVoteDialog initiateVoteDialog = new InitiateVoteDialog();
                    initiateVoteDialog.setOnClickListener(new InitiateVoteDialog.OnClickListener() {
                        @Override
                        public void onOkClick() {
                            chatRoomPresenter.initiateVote(roomInfo.getRoomId(), videoAdapter.getMicUserIdList().get(0), openVote, voteInfo == null ? 0 : voteInfo.getVoteId());
                        }
                    });
                    initiateVoteDialog.show(getSupportFragmentManager());
                } else if (openVote == 2) {
                    chatRoomPresenter.initiateVote(roomInfo.getRoomId(), videoAdapter.getMicUserIdList().get(0), openVote, voteInfo == null ? 0 : voteInfo.getVoteId());
                }
                break;
            case 60004://踢出房间
                if (videoAdapter.getMicUserIdList().contains(currBaseUser.getUserId())) {
                    isRemoveOut = true;
                    micType = 3;//接口回调switch进入需要
                    chatRoomPresenter.upOrDownWheat(currBaseUser.getUserId(), currBaseUser.getUserId(), roomNumber, 3, 0);
                } else {
                    Utils.showToastShortTime("您被管理员踢出房间");
                    finish();
                }
                break;
            case 60009://禁言
                int flag = event.getFlag();
                if (flag == 0x01) {
                    isDisableMsg = false;
                } else if (flag == 0x02) {
                    isDisableMsg = true;
                }
                break;
        }
    }

    /**
     * 主持人关闭房间或者系统提示关闭房间
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoGroupRoomCloseEvent event) {
        if (event == null) {
            return;
        }
        if (TextUtils.isEmpty(event.getMsg())) {//主持人关闭
            int status = 2;
            chatRoomPresenter.updateRoomStatus(currBaseUser.getUserId(), roomNumber, "", status, 0, "", null, null);
        } else if (event.getRoomId() == roomNumber) {//系统
            if (event.getFlag() == 1) {
                finish();
            } else if (event.getFlag() == 0) {
                Utils.showToastShortTime(event.getMsg());
            } else if (event.getFlag() == 2) {
                chatRoomPresenter.getOnWheatList(roomNumber);
            }
        }
    }

    /**
     * 点击发言区获取用户信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoGroupGetUserInfoEvent event) {
        if (event == null) {
            return;
        }
        BaseUser baseUser = event.getBaseUser();
        if (baseUser == null) {
            return;
        }

        if (!isFinishing()) {
            GroupVideoUserDetailDialog userDetailDialogFragment = new GroupVideoUserDetailDialog();
            userDetailDialogFragment.setDate(baseUser, roomNumber);
            userDetailDialogFragment.setUserRole(userRole);
            userDetailDialogFragment.show(getSupportFragmentManager());
        }
    }

    /**
     * 连麦的申请和同意
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoGroupMicApplyEvent event) {
        micType = event.getType();
        int videoType = event.getVideoType();
        if (userRole == 1) {
            if (micType == 1) {//主持人收到请求连麦
                view_img_mic.setVisibility(View.VISIBLE);
                return;
            }
            RoomBaseUser roomBaseUser = new RoomBaseUser();
            roomBaseUser.setUserId(event.getBaseUser().getUserId());
            roomBaseUser.setNickName(event.getBaseUser().getNickName());
            roomBaseUser.setUserIcon(event.getBaseUser().getUserIcon());
            otherUser = roomBaseUser;
        } else {
            if (micType == 1) {
                if (videoType == 1) {
                    isLocalVideoClose = false;
                } else if (videoType == 2) {
                    isLocalVideoClose = true;
                }
            } else if (micType == 2) {//观众上麦，修改身份
                Utils.showToastShortTime("申请已通过，开始连麦");
                chatRoomPresenter.getOnWheatList(roomNumber);
                return;
            } else if (micType == 3) {//嘉宾被移除下麦，修改身份
                chatRoomPresenter.getOnWheatList(roomNumber);
                return;
            }
        }
        if (micType == 5) {//主动下麦
            micType = 3;
        }

        chatRoomPresenter.upOrDownWheat(currBaseUser.getUserId(), event.getBaseUser().getUserId(), roomNumber, micType, videoType);
    }

    /**
     * 接收融云未读消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RongyunReceiveUnreadCountEvent event) {


        if (event == null) {
            return;
        }

        getUnreadCount();


    }

    private void getUnreadCount() {
        try {
            RongIMutils.getUnreadCount(new RongYunUnreadCountCallback() {
                @Override
                public void onSuccess(int count) {
                    TextView popView = findViewById(R.id.img_msg_private_pop_view);
                    if (count > 0) {
                        popView.setText(count > 99 ? "99+" : count + "");
                        popView.setVisibility(View.VISIBLE);
                    } else {
                        popView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置主持人高清
     */
    public void setFirstHighDisplay() {
        App.getInstance().getRtcEngine().setRemoteVideoStreamType(videoAdapter.getMicUserIdList().get(0).intValue(), io.agora.rtc.Constants.VIDEO_STREAM_HIGH);
    }

    /**
     * 视频布局显示
     */
    private void videoDisplay() {
        DisplayMetrics outMetrics = App.getInstance().getResources().getDisplayMetrics();
        frame_first_mic.getLayoutParams().width = outMetrics.widthPixels;
        if (isBigView) {
            recycler_group.setVisibility(View.GONE);
            frame_first_mic.getLayoutParams().height = outMetrics.heightPixels;
            recycler_group.removeAllViews();
        } else {
            if (videoAdapter.getItemCount() == 0) {
                img_small.setVisibility(View.GONE);
                frame_first_mic.getLayoutParams().height = outMetrics.heightPixels;
                recycler_group.setVisibility(View.GONE);
                recycler_group.removeAllViews();
            } else if (videoAdapter.getItemCount() == 3) {
                img_small.setVisibility(View.VISIBLE);
                recycler_group.setVisibility(View.VISIBLE);
                frame_first_mic.getLayoutParams().height = outMetrics.heightPixels - Utils.dp2px(126);
            } else if (videoAdapter.getItemCount() == 6) {
                img_small.setVisibility(View.VISIBLE);
                recycler_group.setVisibility(View.VISIBLE);
                frame_first_mic.getLayoutParams().height = outMetrics.heightPixels - Utils.dp2px(126 * 2);
            }
        }
    }

    /**
     * 初始化声网视频引擎
     */
    private void setupRtcEngine() {
        App.getInstance().setRtcEngine();
        App.getInstance().setEngineEventHandlerActivity(this);
        App.getInstance().getRtcEngine().enableVideo();
        //开启双流模式
        App.getInstance().getRtcEngine().enableDualStreamMode(true);
        //设置视频流畅优先
        App.getInstance().getRtcEngine().setVideoQualityParameters(true);
    }

    /**
     * 加入视频聊天室
     * String channelKey,
     * String channelName,
     * String optionalInfo,
     * int optionalUid
     */
    private void setupChannel() {
        App.getInstance().getRtcEngine().joinChannel(Constants.KEY.AGORA_APP_KEY,
                String.valueOf(roomNumber),
                "", (int) currBaseUser.getUserId());
    }

    @Override
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onJoinChannelSuccess(channel, uid, elapsed);
        Logger.d("onJoinChannelSuccess======" + channel + "=======" + uid + "======" + elapsed);
        RongIMutils.joinChatRoom(GroupVideoActivity.this,String.valueOf(roomNumber), true);
        setFirstHighDisplay();

    }

    @Override
    public void onUserJoined(final int uid, int elapsed) {
        super.onUserJoined(uid, elapsed);
        Logger.d("onUserJoined() === " + uid + " ===elapsed === " + elapsed);

    }

    @Override
    public void onUserMuteVideo(int uid, boolean muted) {
        Logger.d("onUserMuteVideo() === " + uid + " ===muted === " + muted);
        getRemoteVideoStatus(uid, muted);
    }

    @Override
    public void onError(int err) {
        Logger.e("err === " + err);
    }

    /**
     * 连接丢失回调
     * 该回调方法表示 SDK 和服务器失去了网络连接,并且尝试自动重连一段时间(默认 10 秒)后仍 未连上。
     * 该回调触发后,SDK 仍然会尝试重连,重连成功后会触发 onRejoinChannelSuccess 回调。
     */
    @Override
    public void onConnectionLost() {
        super.onConnectionLost();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showToastShortTime("网络不给力视频连接中断");
            }
        });

    }

    @Override
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
        super.onRejoinChannelSuccess(channel, uid, elapsed);
        Logger.d("重新加入频道回调");
    }

    /**
     * 拦截
     */
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SafeSharePreferenceUtils.saveBoolean(Constants.Fields.VIDEO_STATUS, false);
        App.getInstance().getRtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER, null);
        App.getInstance().getRtcEngine().leaveChannel();
        App.getInstance().setEngineEventHandlerActivity(null);
        App.getInstance().getRtcEngine().setChannelProfile(io.agora.rtc.Constants.CHANNEL_PROFILE_COMMUNICATION);
        App.getInstance().getRtcEngine().setVideoProfile(IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_360P_8, false);
        RongIMutils.quitChatRoom(String.valueOf(roomNumber));
        EventBus.getDefault().unregister(this);
        if (currBaseUser != null) {
            currBaseUser = null;
        }
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        if (runnable != null) {
            runnable = null;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
