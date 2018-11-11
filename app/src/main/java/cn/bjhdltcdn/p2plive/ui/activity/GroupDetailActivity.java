package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.UpdateManagersEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.GroupMemberListAdapter;
import cn.bjhdltcdn.p2plive.model.GroupUser;
import cn.bjhdltcdn.p2plive.model.ReportType;
import cn.bjhdltcdn.p2plive.mvp.presenter.CommonPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.SelectorReportContentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.rong.imlib.model.Conversation;

import static cn.bjhdltcdn.p2plive.utils.DateUtils.getFormatDataString;

/**
 * 群详情
 */
public class GroupDetailActivity extends BaseActivity implements BaseView {

    private String mTargetId;

    private RecyclerView recyclerView;

    private CircleImageView iv_group_icon;

    private GroupMemberListAdapter adapter;

    private TextView moreView;
    private TextView groupNickView;
    private TextView exitView;
    private CommonPresenter commonPresenter;

    private Group group;

    private int userRole = 3;

    private GroupPresenter groupPresenter;

    private Switch mSwitchView;
    private Switch mSwitchView2;

    /**
     * 是否是群组或者管理员手动删除
     */
    private boolean isInitiativeDelete;

    private int pageSize = 20;
    private int pageNumber = 1;
    /**
     * 群免打扰设置
     */
    private boolean isDisturbMode = false;

    /**
     * 群昵称
     */
    private String groupNickName;
    private String selectFileUrl;
    private ImagePresenter imagepresenter;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private TextView tv_report;
    private View line_view_2;

    public GroupPresenter getGroupPresenter() {
        if (groupPresenter == null) {
            groupPresenter = new GroupPresenter(this);
        }
        return groupPresenter;
    }

    public CommonPresenter getCommonPresenter() {
        if (commonPresenter == null) {
            commonPresenter = new CommonPresenter(this);
        }
        return commonPresenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_layout);

        EventBus.getDefault().register(this);

        mTargetId = getIntent().getStringExtra(Constants.KEY.KEY_OBJECT);

        setTitle();

        initView();

        getGroupPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0, pageSize, pageNumber);

    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("群详情");
    }


    private void initView() {
        imagepresenter = new ImagePresenter(this);
        // 群成员列表
        recyclerView = findViewById(R.id.recycler_view);
        iv_group_icon = findViewById(R.id.iv_group_icon);
        GridLayoutManager layoutManager = new GridLayoutManager(App.getInstance(), 5);
        layoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new GroupMemberListAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                GroupUser groupUser = adapter.getList().get(position);
                if (groupUser != null) {// 跳转到用户详情页面
                    if (groupUser.getUserRole() > 0 && groupUser.getUserId() > 0) {
                        if (groupUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                            startActivity(new Intent(GroupDetailActivity.this, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, groupUser.getBaseUser()));
                        }
                    } else if (groupUser.getUserRole() == adapter.getADD_ICON_ROLE()) {
                        Intent intent =  new Intent(GroupDetailActivity.this, SelectAddressBookActivity.class);
                        intent.putExtra(Constants.KEY.KEY_OBJECT, group);
                        intent.putExtra(Constants.Fields.TYPE, 1);
                        startActivity(intent);
                    } else if (groupUser.getUserRole() == adapter.getDEL_ICON_ROLE()) {
                        Intent intent = new Intent(GroupDetailActivity.this, AddGroupAdministratorsActivity.class);
                        intent.putExtra(Constants.Fields.GROUP_ID, group.getGroupId());
                        intent.putExtra(Constants.Fields.TYPE, 2);
                        startActivity(intent);
                    }

                }
            }
        });

//        // 删除事件
//        adapter.setViewClickListener(new GroupMemberListAdapter.ViewClickListener() {
//            @Override
//            public void onClick(int position) {
//
//                GroupUser groupUser = adapter.getList().get(position);
//                if (groupUser != null && groupUser.getBaseUser() != null) {
//
//                    isInitiativeDelete = true;
//
//                    List<Long> outUserIds = new ArrayList<Long>(1);
//                    long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
//                    outUserIds.add(groupUser.getBaseUser().getUserId());
//                    long groupId = group == null ? (TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0) : group.getGroupId();
//                    getGroupPresenter().signOutGroup(groupId, userId, outUserIds);
//
//                    adapter.getList().remove(position);
//                    adapter.notifyDataSetChanged();
//
//                }
//
//            }
//        });

        // 更多按钮
        moreView = findViewById(R.id.tv_more);
        moreView.setVisibility(View.GONE);
        moreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupDetailActivity.this, GroupMemberListActivity.class).putExtra(Constants.KEY.KEY_OBJECT, group)
                        .putExtra(Constants.KEY.KEY_EXTRA_PARAM, userRole));
            }
        });

        // 群昵称
        findViewById(R.id.layout_view_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userRole == 1) {
                    Intent intent = new Intent(GroupDetailActivity.this, ChangeTextActivity.class);
                    intent.putExtra(Constants.KEY.KEY_OBJECT, groupNickName);
                    intent.putExtra(Constants.KEY.KEY_TYPE, 1);
                    startActivity(intent);
                } else {
                    Utils.showToastShortTime("只有群主可以修改群名称");
                }


            }
        });

        groupNickView = findViewById(R.id.group_nick_view);


        // 进群方式
        radioButton1 = findViewById(R.id.radio_btn_1);
        radioButton2 = findViewById(R.id.radio_btn_2);

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDisturbMode = false;

                boolean isChecked = radioButton1.isChecked();
                // 成员进群方式(1直接入群,2申请进群),
                int groupMode = isChecked ? 1 : 2;

                Logger.d("radioButton1  ==groupMode== " + groupMode + "  ==isChecked== " + isChecked);

                getGroupPresenter().updateGroup(group.getGroupId(), "", groupMode, -1, -1);

            }
        });

        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDisturbMode = false;

                boolean isChecked = radioButton2.isChecked();
                // 成员进群方式(1直接入群,2申请进群),
                int groupMode = isChecked ? 2 : 1;

                Logger.d("radioButton2  ==groupMode== " + groupMode + "  ==isChecked== " + isChecked);

                getGroupPresenter().updateGroup(group.getGroupId(), "", groupMode, -1, -1);

            }
        });

        // 群分享
        findViewById(R.id.layout_view_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupDetailActivity.this, SelectAddressBookActivity.class).putExtra(Constants.KEY.KEY_OBJECT, group));
            }
        });

        // 免打扰
        mSwitchView = findViewById(R.id.img_no_disturb);
        mSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDisturbMode = true;

                boolean isChecked = mSwitchView.isChecked();
                // 消息免打扰模式(默认为0--->关闭,1--->开启)
                int isDisturbModeType = isChecked ? 1 : 0;

                Logger.d("mSwitchView  ==isDisturbMode== " + isDisturbMode + "  ===isDisturbModeType === " + isDisturbModeType + "  ==isChecked== " + isChecked);


                getGroupPresenter().updateGroup(group.getGroupId(), "", -1, isDisturbModeType, -1);

            }
        });

        // 允许别人看到我加入本群群
        mSwitchView2 = findViewById(R.id.img_no_disturb2);
        mSwitchView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isDisturbMode = false;

                boolean isChecked = mSwitchView2.isChecked();
                // 是否公开圈子(默认为0--->公开,1--->隐藏)
                int isPublic = isChecked ? 0 : 1;

                Logger.d("mSwitchView2  ==isPublic== " + isPublic + "  ==isChecked== " + isChecked);

                getGroupPresenter().updateGroup(group.getGroupId(), "", -1, -1, isPublic);


            }
        });
        tv_report = findViewById(R.id.tv_report);
        line_view_2 = findViewById(R.id.line_view_2);
        tv_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectorReportContentDialog selectorReportContentDialog = new SelectorReportContentDialog();
                selectorReportContentDialog.setOnItemListener(new SelectorReportContentDialog.OnItemListener() {
                    @Override
                    public void reportItemClick(Object object) {

                        if (object instanceof ReportType) {
                            ReportType reportTypeObj = (ReportType) object;
                            getCommonPresenter().reportOperation(TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0
                                    , 4
                                    , SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)
                                    , 0
                                    , reportTypeObj.getReportTypeId());
                        }

                    }
                });
                selectorReportContentDialog.show(getSupportFragmentManager());
            }
        });

        //群头像
        iv_group_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userRole == 1) {
                    PictureSelector.create(GroupDetailActivity.this)
                            .openGallery(PictureMimeType.ofImage())
                            .maxSelectNum(1)// 最大图片选择数量 int
                            .minSelectNum(1)// 最小选择数量 int
                            .imageSpanCount(4)// 每行显示个数 int
                            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                            .previewImage(true)// 是否可预览图片 true or false
                            .isCamera(true)// 是否显示拍照按钮 true or false
                            .enableCrop(true)// 是否裁剪 true or false
                            .compress(true)// 是否压缩 true or false
                            .glideOverride(Utils.dp2px(110), Utils.dp2px(110))// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .isGif(true)// 是否显示gif图片 true or false
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                            .showCropFrame(false)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                            .openClickSound(true)// 是否开启点击声音 true or false
                            .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            .cropWH(Utils.dp2px(110), Utils.dp2px(110))// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                            .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                            .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                } else {
                    Utils.showToastShortTime("只有群主可以修改群头像");
                }
            }

        });

        // 退出群
        exitView = findViewById(R.id.tv_exit);
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (group == null) {
                    return;
                }

                if (group.getIsExistGroup() == 0) {
                    return;
                }

                switch (userRole) {
                    case 1://解散该群

                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "是否要解散该群？", "取消", "解散");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                            }

                            @Override
                            public void onRightClick() {


                                long groupId = group == null ? (TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0) : group.getGroupId();
                                getGroupPresenter().disbandGroup(groupId, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));

                            }
                        });
                        dialog.show(getSupportFragmentManager());


                        break;

                    case 2://退出该群
                    case 3:

                        dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "退群后，您将不能接收本群消息，确定退出？", "取消", "退出");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                            }

                            @Override
                            public void onRightClick() {

                                List<Long> outUserIds = new ArrayList<Long>(1);
                                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                                outUserIds.add(userId);

                                long groupId = group == null ? (TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0) : group.getGroupId();

                                getGroupPresenter().signOutGroup(groupId, userId, outUserIds);

                            }
                        });
                        dialog.show(getSupportFragmentManager());

                        break;
                }

            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(GroupOperationEvent evnet) {

        if (evnet == null || evnet.getOperationType() < 5) {
            return;
        }

        switch (evnet.getOperationType()) {
            case 5:

                if (!StringUtils.isEmpty(evnet.getNickName())) {
                    groupNickName = evnet.getNickName();
                    getGroupPresenter().updateGroup(group.getGroupId(), groupNickName, -1, -1, -1);
                }

                break;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateManagersThread(UpdateManagersEvent event) {
        if (event == null) {
            return;
        }
        getGroupPresenter().getGroupUserList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TextUtils.isDigitsOnly(mTargetId) ? Long.valueOf(mTargetId) : 0, pageSize, pageNumber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                    if (selectList != null || selectList.size() > 0) {
                        LocalMedia localMedia = selectList.get(0);
                        if (localMedia.isCompressed()) {
                            selectFileUrl = localMedia.getCompressPath();

                            // 绑定选择过的图片
                            RequestOptions options = new RequestOptions();
                            options.centerCrop();
                            Glide.with(this).asBitmap().load(selectFileUrl).apply(options).into(iv_group_icon);

                            if (!StringUtils.isEmpty(selectFileUrl)) {
                                long groupId = group.getGroupId();
                                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                                String time = getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2);
                                imagepresenter.uploadImage(groupId, 11, userId, time, selectFileUrl);
                            }

                        }
                    }

                    break;
            }
        }
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

        if (InterfaceUrl.URL_REPORTOPERATION.equals(apiName)) {

            if (object instanceof BaseResponse) {

                BaseResponse response = (BaseResponse) object;

                Utils.showToastShortTime(response.getMsg());
            }

        } else if (InterfaceUrl.URL_GETGROUPUSERLIST.equals(apiName)) {

            if (object instanceof GetGroupUserListResponse) {

                GetGroupUserListResponse response = (GetGroupUserListResponse) object;

                if (response.getCode() == 200) {

                    // 更多按钮
                    moreView.setTag(response.getGroupUserList());

                    // 群信息
                    group = response.getGroup();
                    if (group != null) {

                        //群组状态(默认为0--->开启,1--->解散)
                        if (group.getStatus() == 1) {
                            Utils.showToastShortTime("群组已解散");
                            finish();
                            return;
                        }

                        //当前用户是否在群中(0不在,1在)
                        if (group.getIsExistGroup() == 0) {
                            Utils.showToastShortTime("您不在群里");
                            finish();
                            return;
                        }

                        groupNickView.setTag(group);
                        groupNickView.setText(group.getGroupName());
                        groupNickView.setEnabled(true);
                        groupNickName = group.getGroupName();

                        String groupImg = group.getGroupImg();
                        Glide.with(this).load(groupImg).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(iv_group_icon);

                        if (group.getBaseUser() != null) {
                            if (group.getBaseUser().getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {

                                findViewById(R.id.forward_view).setVisibility(View.INVISIBLE);
                                // 头像右箭头
                            } else {
                                findViewById(R.id.img_right_icon).setVisibility(View.VISIBLE);
                            }
                        }

                        //成员进群方式(1直接入群,2申请进群)
                        int groupMode = group.getGroupMode();
                        switch (groupMode) {
                            case 1:
                                radioButton1.setChecked(true);
                                radioButton2.setChecked(false);
                                break;

                            case 2:
                                radioButton1.setChecked(false);
                                radioButton2.setChecked(true);
                                break;

                        }

                        // 是否公开圈子(默认为0--->公开,1--->隐藏)
                        int isPublic = group.getIsPublic();
                        mSwitchView2.setChecked(isPublic == 0);

                    } else {

                        Utils.showToastShortTime("获取群详情信息失败");
                        finish();
                        return;

                    }

                    exitView.setVisibility(View.VISIBLE);
                    GroupUser groupUser = response.getGroupUser();
                    if (groupUser != null) {
                        // 所属角色(1--->群主,2--->管理员,3--->普通成员)
                        userRole = groupUser.getUserRole();
                        if (userRole == 1 || userRole == 2) {
                            if (response.getGroupUserList().size() > 13) {
                                adapter.setList(response.getGroupUserList().subList(0, 13));
                                moreView.setVisibility(View.VISIBLE);
                            } else {
                                adapter.setList(response.getGroupUserList());
                            }
                        } else {
                            if (response.getGroupUserList().size() > 15) {
                                adapter.setList(response.getGroupUserList().subList(0, 15));
                                moreView.setVisibility(View.VISIBLE);
                            } else {
                                adapter.setList(response.getGroupUserList());
                            }
                        }

                        switch (userRole) {
                            case 1:
                                exitView.setText("解散该群");
                                adapter.setDeleteItem();

                                findViewById(R.id.line_view_5).setVisibility(View.VISIBLE);
                                findViewById(R.id.tv_no_disturb2_view).setVisibility(View.VISIBLE);
                                findViewById(R.id.img_no_disturb2).setVisibility(View.VISIBLE);

                                findViewById(R.id.line_view_6).setVisibility(View.VISIBLE);
                                findViewById(R.id.layout_view_6).setVisibility(View.VISIBLE);

                                // 举报
                                tv_report.setVisibility(View.GONE);
                                line_view_2.setVisibility(View.GONE);

                                View line_view_administrator = findViewById(R.id.line_view_administrator);
                                TextView tv_group_administrator = findViewById(R.id.tv_group_administrator);
                                line_view_administrator.setVisibility(View.VISIBLE);
                                tv_group_administrator.setVisibility(View.VISIBLE);
                                tv_group_administrator.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(GroupDetailActivity.this, AddGroupAdministratorsActivity.class);
                                        intent.putExtra(Constants.Fields.GROUP_ID, group.getGroupId());
                                        intent.putExtra(Constants.Fields.TYPE, 1);
                                        startActivity(intent);
                                    }
                                });

                                break;

                            case 2:

                                exitView.setText("退出该群");
                                adapter.setDeleteItem();


                                findViewById(R.id.line_view_5).setVisibility(View.VISIBLE);
                                findViewById(R.id.tv_no_disturb2_view).setVisibility(View.VISIBLE);

                                // 举报
                                tv_report.setVisibility(View.GONE);
                                line_view_2.setVisibility(View.GONE);

                                break;

                            case 3:
                                tv_report.setVisibility(View.VISIBLE);
                                exitView.setText("退出该群");

                                findViewById(R.id.line_view_5).setVisibility(View.VISIBLE);
                                findViewById(R.id.tv_no_disturb2_view).setVisibility(View.VISIBLE);

                                break;

                            default:

                                exitView.setVisibility(View.GONE);

                                findViewById(R.id.line_view_5).setVisibility(View.GONE);
                                findViewById(R.id.tv_no_disturb2_view).setVisibility(View.GONE);

                                findViewById(R.id.line_view_6).setVisibility(View.GONE);
                                findViewById(R.id.layout_view_6).setVisibility(View.GONE);

                                break;
                        }

                        // 消息免打扰模式(默认为0--->关闭,1--->开启),
                        int isDisturbMode = groupUser.getIsDisturbMode();
                        mSwitchView.setChecked(isDisturbMode != 0);

                    }

                } else {
                    Utils.showToastShortTime(response.getMsg());
                    finish();
                }
            }

        } else if (InterfaceUrl.URL_SIGNOUTGROUP.equals(apiName)) {
            if (object instanceof BaseResponse) {

                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());

                if (!isInitiativeDelete) {
                    EventBus.getDefault().post(new GroupOperationEvent(2, group.getGroupId()));
                    finish();
                }


            }

        } else if (InterfaceUrl.URL_DISBANDGROUP.equals(apiName)) {
            if (object instanceof BaseResponse) {

                BaseResponse response = (BaseResponse) object;
                Utils.showToastShortTime(response.getMsg());

                EventBus.getDefault().post(new GroupOperationEvent(1, group.getGroupId()));

                finish();

            }
        } else if (InterfaceUrl.URL_UPDATEGROUP.equals(apiName)) {
            if (object instanceof BaseResponse) {

                BaseResponse response = (BaseResponse) object;

                if (response.getCode() == 200) {

                    if (!StringUtils.isEmpty(groupNickName)) {
                        groupNickView.setText(groupNickName);
                        EventBus.getDefault().post(new GroupOperationEvent(3, groupNickName));
                    }

                    if (isDisturbMode) {
                        // 更新融云状态
                        RongIMutils.setConversationNotificationStatus(Conversation.ConversationType.GROUP
                                , group.getGroupId() + ""
                                , mSwitchView.isChecked() ? Conversation.ConversationNotificationStatus.DO_NOT_DISTURB : Conversation.ConversationNotificationStatus.NOTIFY);

                        isDisturbMode = false;

                    }

                }

            }
            groupNickName = null;
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

        EventBus.getDefault().unregister(this);
    }
}
