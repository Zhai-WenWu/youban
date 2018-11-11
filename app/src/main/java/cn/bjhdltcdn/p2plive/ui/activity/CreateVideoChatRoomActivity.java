package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateRoomStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.RoomLabelTabsRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.OpenGroupVideoLockDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * 创建视频聊天房
 * Created by ZHUDI on 2017/11/23.
 */

public class CreateVideoChatRoomActivity extends BaseActivity implements BaseView {
    private DiscoverPresenter discoverPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private ImagePresenter imagePresenter;
    private EditText edit_titel;
    private ImageView img_cover;
    private RecyclerView recycler_label;
    private TextView tv_open;
    private String selectFileUrl;
    private int passwordType = 0;//加密类型(0--->不加密,1--->密码),
    private RoomLabelTabsRecyclerViewAdapter roomLabelTabsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        discoverPresenter = new DiscoverPresenter(this);
        chatRoomPresenter = new ChatRoomPresenter(this);
        imagePresenter = new ImagePresenter(this);
        setContentView(R.layout.activity_create_room_video);
        setTitle();
        init();
        discoverPresenter.getLabelList(1);
    }

    private void init() {
        edit_titel = findViewById(R.id.edit_title);
        img_cover = findViewById(R.id.img_cover);
        recycler_label = findViewById(R.id.recycler_label);
        tv_open = findViewById(R.id.tv_open);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(this);
        flowLayoutManager.setAutoMeasureEnabled(true);
        recycler_label.setLayoutManager(flowLayoutManager);
        recycler_label.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(20)));
        roomLabelTabsRecyclerViewAdapter = new RoomLabelTabsRecyclerViewAdapter(1);
        roomLabelTabsRecyclerViewAdapter.setOnClickListener(new RoomLabelTabsRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                // 第一次查询
                int selectItemCount = roomLabelTabsRecyclerViewAdapter.getSelectItemCount();
                if (selectItemCount > 3) {
                    LabelInfo keywordInfo = roomLabelTabsRecyclerViewAdapter.getList().get(position);
                    keywordInfo.setIsSelect(0);
                    roomLabelTabsRecyclerViewAdapter.notifyItemChanged(position);
                    if (selectItemCount > 3) {
                        Utils.showToastShortTime("最多可选3个标签");
                    }
                }
            }
        });

        edit_titel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(selectFileUrl) && !TextUtils.isEmpty(charSequence)) {
                    tv_open.setBackgroundResource(R.color.color_ffee00);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        img_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureSelector.create(CreateVideoChatRoomActivity.this)
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
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isAllowClick()) {
                    if (TextUtils.isEmpty(getTitleEditText())) {
                        Utils.showToastShortTime("房间主题不能为空");
                    } else if (TextUtils.isEmpty(selectFileUrl)) {
                        Utils.showToastShortTime("房间封面不能为空");
                    } else {
                        final OpenGroupVideoLockDialog dialog = new OpenGroupVideoLockDialog();
                        dialog.setItemClickListener(new OpenGroupVideoLockDialog.ItemClickListener() {
                            @Override
                            public void clickItem(int type) {
                                passwordType = type;
                                if (passwordType == 0) { // 未设置密码
                                    chatRoomPresenter.updateRoomStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), 0, getTitleEditText(),
                                            0, passwordType, null, roomLabelTabsRecyclerViewAdapter.getSelectLabelInfoList(), null);

                                } else if (passwordType == 1) { // 设置密码
                                    Intent intent = new Intent(CreateVideoChatRoomActivity.this, OpenRoomSettingPassWordActivity.class);
                                    intent.putExtra(Constants.Fields.TITLE, getTitleEditText());
                                    intent.putExtra(Constants.Fields.IMAGEPATH, selectFileUrl);
                                    intent.putExtra(Constants.Fields.TYPE, 1);
                                    intent.putExtra(Constants.Fields.EXTRA, (Serializable) roomLabelTabsRecyclerViewAdapter.getSelectLabelInfoList());
                                    startActivity(intent);
                                }

                                dialog.dismiss();
                            }
                        });
                        dialog.show(getSupportFragmentManager(), "dialog");

                    }
                }
            }
        });

    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETLABELLIST:
                if (object instanceof GetLabelListResponse) {
                    GetLabelListResponse getLabelListResponse = (GetLabelListResponse) object;
                    if (getLabelListResponse.getCode() == 200) {
                        roomLabelTabsRecyclerViewAdapter.setList(getLabelListResponse.getLabelList());
                        recycler_label.setAdapter(roomLabelTabsRecyclerViewAdapter);
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATEROOMSTATUS:
                if (object instanceof UpdateRoomStatusResponse) {
                    UpdateRoomStatusResponse response = (UpdateRoomStatusResponse) object;
                    if (response.getCode() == 200) {
                        Intent intent = new Intent(this, GroupVideoActivity.class);
                        if (roomLabelTabsRecyclerViewAdapter.getSelectLabelStrs().size() > 0) {
                            response.getRoomInfo().setLabelList(roomLabelTabsRecyclerViewAdapter.getSelectLabelStrs());
                        }
                        intent.putExtra(Constants.Fields.ROOMINFO, response.getRoomInfo());
                        imagePresenter.uploadImage(response.getRoomInfo().getRoomId(), 7, response.getRoomInfo().getToBaseUser().getUserId(), TimeUtils.date2String(new Date()), selectFileUrl);
                        startActivity(intent);
                    } else {
                        Utils.showToastShortTime(response.getMsg());
                        if (response.getCode() == 203) {//未实名认证
                            startActivity(new Intent(this, RealNameCertificationActivity.class));
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_UPLOADIMAGE:
                if (object instanceof UploadImageResponse) {
                    UploadImageResponse response = (UploadImageResponse) object;
                    if (response.getCode() == 200) {
                        Constants.Fields.ROOM_BG_URL = response.getImage().getImageUrl();
                    }
                    finish();
                }
                break;
            default:
        }
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_open_room, R.color.color_ffffff);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_write_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setBackground(R.color.color_00000000);
    }


    /**
     * 获取标题
     *
     * @return
     */
    private String getTitleEditText() {
        return edit_titel.getText().toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                            RequestOptions options = new RequestOptions().centerCrop();
                            Glide.with(this).asBitmap().load(selectFileUrl).apply(options).into(img_cover);
                        }
                    }
                    if (!TextUtils.isEmpty(selectFileUrl) && !TextUtils.isEmpty(getTitleEditText())) {
                        tv_open.setBackgroundResource(R.color.color_ffee00);
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
