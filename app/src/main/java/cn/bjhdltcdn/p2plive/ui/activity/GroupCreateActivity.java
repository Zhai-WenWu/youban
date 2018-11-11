package cn.bjhdltcdn.p2plive.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.GroupOperationEvent;
import cn.bjhdltcdn.p2plive.event.UpdateActiveDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.CreateGroupResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GroupPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 创建群页面
 */
public class GroupCreateActivity extends BaseActivity implements BaseView {

    private CircleImageView imageView;
    private String selectFileUrl;

    private EditText editView;

    /**
     * 群组类型(默认为0--->普通群,1--->圈子群,2--->活动临时讨论群)
     */
    private int type;

    /**
     * 成员进群方式(1直接入群,2申请进群)
     */
    private int groupMode = 1;

    /**
     * 群组关联Id(type=0传值0,type=1圈子Id,type=2活动Id)
     */
    private long relationId;

    private GroupPresenter groupPresenter;
    private ImagePresenter imagePresenter;


    private OrganizationInfo organizationInfo;

    private ActivityInfo activityInfo;
    private Button nextView;
    private boolean needShowLoading = true;

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    public GroupPresenter getGroupPresenter() {
        if (groupPresenter == null) {
            groupPresenter = new GroupPresenter(this);
        }
        return groupPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create_layout);


        Object object = getIntent().getParcelableExtra(Constants.KEY.KEY_OBJECT);

        if (object instanceof OrganizationInfo) {
            organizationInfo = (OrganizationInfo) object;
        } else if (object instanceof ActivityInfo) {
            activityInfo = (ActivityInfo) object;
        }

        if (organizationInfo == null && activityInfo == null) {
            type = 0;

        } else if (organizationInfo != null) {
            relationId = organizationInfo.getOrganId();
            type = 1;
        } else if (activityInfo != null) {
            relationId = activityInfo.getActivityId();
            type = 2;
        }

        setTitle();
        initView();


    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        if (organizationInfo != null) {
            titleFragment.setTitleView("创建圈子聊天群");
        } else {
            titleFragment.setTitleView("创建聊天群");
        }
    }

    private void initView() {

        // 群头像
        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(GroupCreateActivity.this)
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
            }
        });

        // 群昵称
        editView = findViewById(R.id.edit_view);
        editView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                checkStatus();

            }
        });


        // 进群方式
        RadioButton radioButton1 = findViewById(R.id.radio_btn_1);
        RadioButton radioButton2 = findViewById(R.id.radio_btn_2);

        radioButton1.setChecked(true);


        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    groupMode = 1;
                }

                Logger.d("isChecked ==== " + isChecked + " === groupMode== " + groupMode);

            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    groupMode = 2;
                }

                Logger.d("isChecked ==== " + isChecked + " === groupMode== " + groupMode);

            }
        });

        // 创建按钮
        nextView = findViewById(R.id.btn_view);
        nextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 群昵称
                String nickName = editView.getText().toString().trim();
                if (StringUtils.isEmpty(nickName)) {

                    Utils.showToastShortTime("群昵称不能为空");
                    return;
                }

                if (StringUtils.isEmpty(selectFileUrl)) {
                    Utils.showToastShortTime("群头像不能为空");
                    return;
                }

                // 如果是圈子群，默认使用圈子封面
                String groupImg = null;
                if (type == 1 && !StringUtils.isEmpty(organizationInfo.getOrganImg())) {
                    if (organizationInfo.getOrganImg().startsWith("http://")) {
                        groupImg = organizationInfo.getOrganImg();
                    }else {
                        selectFileUrl = organizationInfo.getOrganImg();
                    }
                }

                getGroupPresenter().createGroup(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), relationId, nickName, groupMode, type,groupImg);

            }
        });


        // 圈子进来的
        if (organizationInfo != null) {
            selectFileUrl = organizationInfo.getOrganImg();
            Glide.with(this).load(selectFileUrl).apply(new RequestOptions().placeholder(R.mipmap.error_group_icon)).into(imageView);
            editView.setText(organizationInfo.getOrganName() + "圈子聊天群");
        }


        checkStatus();


    }

    private void checkStatus() {
        String nickName = editView.getText().toString().trim();
        if (!StringUtils.isEmpty(nickName) && !StringUtils.isEmpty(selectFileUrl)) {
            nextView.setEnabled(true);
            nextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
        } else {
            nextView.setEnabled(false);
            nextView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
        }
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
                            options.centerCrop().placeholder(R.mipmap.error_group_icon);
                            Glide.with(this).asBitmap().load(selectFileUrl).apply(options).into(imageView);

                            checkStatus();

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

        if (InterfaceUrl.URL_CREATEGROUP.equals(apiName)) {
            if (object instanceof CreateGroupResponse) {

                CreateGroupResponse response = (CreateGroupResponse) object;

                if (response.getCode() == 200) {

                    if (response.getGroup() != null) {
                        // TODO 启动群聊页面

                        getImagePresenter().uploadImage(response.getGroup().getGroupId(), 11, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TimeUtils.date2String(new Date()), selectFileUrl);

                        RongIMutils.startGroupChat(GroupCreateActivity.this, response.getGroup().getGroupId() + "", response.getGroup().getGroupName());

                        if (activityInfo != null) {
                            //刷新活动详情界面
                            EventBus.getDefault().post(new UpdateActiveDetailEvent());
                        }

                        if (organizationInfo != null) {
                            // 创建群事件
                            EventBus.getDefault().post(new GroupOperationEvent(4, response.getGroup().getGroupId()));
                        }

                        finish();
                    }

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

        if (groupPresenter != null) {
            groupPresenter.onDestroy();
            groupPresenter = null;
        }

        if (organizationInfo != null) {
            organizationInfo = null;
        }


    }
}
