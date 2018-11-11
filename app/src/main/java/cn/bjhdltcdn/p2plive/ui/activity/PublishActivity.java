package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.constants.HostConfig;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeIsCreateStoreResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SearchLabelByContentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.savePostResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
import cn.bjhdltcdn.p2plive.ui.adapter.PostPublishImageRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PublishLabelRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditSelectDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.dialog.DelectTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.PublishBottomLayoutFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.UriUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;

/**
 * Created by Hu_PC on 2017/11/9.
 * 发布页面
 */

public class PublishActivity extends BaseActivity implements BaseView {


    /**
     * 标题封装对象
     */
    private TitleFragment titleFragment;

    /**
     * 编辑内容输入框
     */
    private EditText editText;

    /**
     * 内容输入字数
     */
    private TextView numContentView;
    /**
     * 显示店铺开关
     */
    private CheckBox storeCheckBox;
    /**
     * 当前用户id
     */
    private long userId;

    /**
     * 标签模块
     */
    private RelativeLayout tagLayout;

    /**
     * 标签列表控件
     */
    private TagContainerLayout tagContainerLayout;

    /**
     * 选中的参赛标签列表
     */
    private RecyclerView selectRecyclerView;

    /**
     * 图片选择列表
     */
    private RecyclerView imgRecycleView;
    /**
     * 图片选择列表适配器
     */
    private PostPublishImageRecycleAdapter imgPublishImageRecycleAdapter;

    /**
     * 选择图片张数的控件
     */
    private TextView numImgView;

    /**
     * 视频控件
     */
    private RelativeLayout videoLayoutView;

    /**
     * 视频缩略图
     */
    private ImageView videoImageView;

    /**
     * 视频删除控件
     */
    private ImageView videoDeleteView;

    /**
     * 图片最多选择数量
     */
    private int maxSelectNum = 9;

    /**
     * 帖子列表类别标签
     */
    private long postTypeLabelId;

    /**
     * 视频文件路径
     */
    private String videoPath;

    /**
     * 视频文件格式
     */
    private String videoSuffixs[] = {"mp4", "avi", "mov", "mkv", "flv", "f4v", "rmvb"};

    /**
     * 本地选择视频
     */
    private final int VIDEO_PICKER_CODE = 2;

    /**
     * 打开录制视频页面
     */
    private final int VIDEO_CODE = 1;
    /**
     * 自定义标签
     */
    private final int LABEL_CODE = 3;
    /**
     * 视频缩略图的宽度
     */
    private int imgWidth;

    /**
     * 视频缩略图的高度
     */
    private int imgHeight;

    /**
     * 底部布局
     */
    private PublishBottomLayoutFragment mFragment;

    /**
     * 是否为招聘信息(0 是,1 不是)
     */
    private int isRecurit;
    /**
     * 是否可以发送图片(0 否,1 是)
     */
    private int isImg = 1;

    /**
     * 是否可以发送gif(0 否,1 是)
     */
    private int isGif = 1;

    /**
     * 是否可以发送视频(0 否,1 是)
     */
    private int isVideo = 1;

    /**
     * 多媒体选择类型
     * 0 文本 ；1 图片 ；2 视频 ； 3 gif
     */
    private int multimediaItemTypeMode;

    /**
     * 已选择的标签列表(没有默认空)
     */
    private ArrayList<PostLabelInfo> selectLabelInfoList;
    /**
     * 选择标签的适配器
     */
    private PublishLabelRecyclerViewAdapter publishLabelRecyclerViewAdapter;
    /**
     * 标签列表数据
     */
    private List<PostLabelInfo> labelInfoList;
    /**
     * 是否匿名 (1匿名,2不匿名)
     */
    private int isAnonymous = 2;

    int contentLimit;

    /**
     * 帖子id，表白id，同学帮帮忙id
     */
    private long parentId;

    /**
     * 跟拍视频
     * 1 帖子跟拍视频 ; 8 表白墙跟拍视频 ； 9 同学帮帮忙跟拍视频 ；
     */
    private int followingShotEnterType;
    private long toUserId;

    /**
     * 店铺Id(默认0),
     */
    private long storeId;
    private AssociationPresenter associationPresenter;
    private GetStoreListPresenter getStoreListPresenter;

    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    private AssociationPresenter getAssociationPresenter() {
        if (associationPresenter == null) {
            associationPresenter = new AssociationPresenter(this);
        }
        return associationPresenter;
    }

    private ImagePresenter imagePresenter;

    private ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish_layout);


        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        toUserId = getIntent().getLongExtra(Constants.Fields.TO_USER_ID, 0);

        isAnonymous = getIntent().getIntExtra(Constants.Fields.IS_ANONYMOUS, 2);

        // 跟拍跳转入口
        parentId = getIntent().getLongExtra(Constants.Fields.PARENT_ID, 0);
        followingShotEnterType = getIntent().getIntExtra(Constants.Fields.FOLLOWING_SHOT_ENTER_TYPE, 0);

        storeId = getIntent().getLongExtra(Constants.Fields.STORE_ID, 0);
        postTypeLabelId = getIntent().getLongExtra(Constants.Fields.POST_LABEL_ID, 0);

        setTitle();

        initView();

        addFragmentToActivity();

//        if (!App.getInstance().isLoctionSuccess()) {
//            //定位失败 弹出弹窗
//            ActiveEditTipDialog networkDialog = new ActiveEditTipDialog();
//            networkDialog.setItemText("哎呀，您的网络不给力，获取不到定位。请重新登录获取定位吧", "我知道了");
//            networkDialog.setItemClick(new ActiveEditTipDialog.ItemClick() {
//                @Override
//                public void itemClick() {
//                    finish();
//                }
//            });
//            networkDialog.show(getSupportFragmentManager());
//            return;
//        }


        getAssociationPresenter().searchLabelByContent(userId, "", 30, 1);
        if (storeId == 0) {
            getGetStoreListPresenter().judgeIsCreateStore(userId);
            selectLabelInfoList = new ArrayList<>(1);
        } else {
            isRecurit = 1;
            storeCheckBox.setVisibility(View.VISIBLE);
            storeCheckBox.setEnabled(false);
        }

    }


    private void setTitle() {

        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);

        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {

                String text = editText.getText().toString().trim();

                if (!StringUtils.isEmpty(text) || imgPublishImageRecycleAdapter.getItemCount() > 0 || !StringUtils.isEmpty(videoPath)) {
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("编辑未完成", "退出后编辑的内容将不被保存", "退出", "继续编辑");
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            //取消
                            onBackPressed();
                        }

                        @Override
                        public void onRightClick() {

                        }
                    });
                    dialog.show(getSupportFragmentManager());
                } else {
                    onBackPressed();
                }

            }
        });

        titleFragment.getRightView().setEnabled(false);
        titleFragment.setTitle("编辑内容");
        titleFragment.setRightViewTitle("发布");
        titleFragment.getRightView().setTextColor(getResources().getColor(R.color.color_999999));

        // 发布按钮
        titleFragment.setRightViewTitle(new TitleFragment.RightViewClick() {
            @Override
            public void onClick() {
                if (!Utils.isAllowClick()) {
                    return;
                }
                // 跟拍视频（视频必传）
                if (followingShotEnterType > 0) {
                    if (StringUtils.isEmpty(videoPath)) {
                        Utils.showToastShortTime("请上传跟拍视频");
                        return;
                    }

                }
                long upStoreId = 0;
                if (storeCheckBox.isChecked()) {
                    upStoreId = storeId;
                }
                if (!StringUtils.isEmpty(videoPath)) {

                    LinkedHashMap<String, Object> map = new LinkedHashMap<>(1);
                    map.put(Constants.Fields.VIDEO_PATH, videoPath);
                    map.put(Constants.Fields.IMG_WIDTH, imgWidth);
                    map.put(Constants.Fields.IMG_HEIGHT, imgHeight);
                    map.put(Constants.Fields.CONTENT_STR, editText.getText().toString());
                    map.put(Constants.Fields.IS_ANONYMOUS, isAnonymous);
                    map.put(Constants.Fields.TO_USER_ID, toUserId);
                    map.put(Constants.Fields.TITLE_STR, "");
                    map.put(Constants.Fields.PLAY_TITLE_STR, "");
                    map.put(Constants.Fields.POST_LABEL_LIST, selectLabelInfoList);
                    map.put(Constants.Fields.PARENT_ID, parentId);
                    map.put(Constants.Fields.CONTENT_LIMIT, contentLimit);
                    map.put(Constants.Fields.STORE_ID, upStoreId);
                    map.put(Constants.Fields.IS_RECURIT, isRecurit);
                    SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(PublishActivity.this, map);
                    Utils.showToastShortTime("视频上传中，上传完成后将自动发布。");
                    finish();


                } else

                {

                    titleFragment.getRightView().setEnabled(false);
                    getAssociationPresenter().savePost(userId, toUserId, editText.getText().toString().trim(), isAnonymous, 1, "", "", selectLabelInfoList, true, parentId, upStoreId,isRecurit);

                }

            }
        });


    }


    private void initView() {

        // 输入框
        editText = findViewById(R.id.content_edit_text);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() <= 500) {
                    numContentView.setText(s.length() + "/500");
                }

                checkRightTitleViewStatus();

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });


        // 输入框的字数限制
        numContentView = findViewById(R.id.tv_num_content);

        //店铺显示开关
        storeCheckBox = findViewById(R.id.chk_store);
        storeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && isAnonymous == 1) {
                    Utils.showToastShortTime("匿名发布不能显示店铺信息");
                    buttonView.setChecked(false);
                }
            }
        });
        // 标签展示列表
        tagLayout = findViewById(R.id.tag_layout);
        tagContainerLayout = findViewById(R.id.tag_container_view);
        // 被选中的参赛标签
        selectRecyclerView = findViewById(R.id.select_recycler_view);
        LinearLayoutManager selectLinearLayoutManager = new LinearLayoutManager(this);
        selectLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        selectRecyclerView.setLayoutManager(selectLinearLayoutManager);
        selectRecyclerView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(15), false));
        publishLabelRecyclerViewAdapter = new PublishLabelRecyclerViewAdapter();
        selectRecyclerView.setAdapter(publishLabelRecyclerViewAdapter);
        publishLabelRecyclerViewAdapter.setOnItemListener(new ItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (selectLabelInfoList.get(position) != null) {
                    selectLabelInfoList.remove(position);
                }
                selectLabelCheckedStatus();

            }
        });

        // 图片选中列表
        imgRecycleView = findViewById(R.id.recycle_view);
        imgRecycleView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        imgRecycleView.setLayoutManager(gridLayoutManager);
        imgRecycleView.setNestedScrollingEnabled(false);
        imgRecycleView.addItemDecoration(new GridLayoutSpacesItemDecoration(Utils.dp2px(10), 3, false));

        imgPublishImageRecycleAdapter = new PostPublishImageRecycleAdapter(null, this);
        imgPublishImageRecycleAdapter.setIsDelete(true);

        imgRecycleView.setAdapter(imgPublishImageRecycleAdapter);

        imgPublishImageRecycleAdapter.setOnDeleteItemClick(new PostPublishImageRecycleAdapter.OnDeleteItemClick() {

            @Override
            public void deleteItemClick(int position) {

                if (imgPublishImageRecycleAdapter != null && imgPublishImageRecycleAdapter.getList() != null && imgPublishImageRecycleAdapter.getList().size() > position) {
                    imgPublishImageRecycleAdapter.getList().remove(position);
                    maxSelectNum++;

                    imgPublishImageRecycleAdapter.notifyDataSetChanged();
                    numImgView.setText((9 - maxSelectNum) + "/9");

                    if (imgPublishImageRecycleAdapter.getItemCount() > 0) {
                        imgRecycleView.setVisibility(View.VISIBLE);
                        numImgView.setVisibility(View.VISIBLE);
                    } else {
                        imgRecycleView.setVisibility(View.GONE);
                        numImgView.setVisibility(View.GONE);
                    }

                    checkRightTitleViewStatus();

                }

            }
        });

        // 选择图片的张数
        numImgView = findViewById(R.id.tv_num_img);

        // 视频显示控件
        videoLayoutView = findViewById(R.id.video_layout_view);

        // 视频缩略图显示控件
        videoImageView = findViewById(R.id.video_image_view);

        // 删除按钮
        videoDeleteView = findViewById(R.id.delete_view);
        videoDeleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelectTipDialog dialog = new DelectTipDialog();
                dialog.setTitleStr("确定删除视频文件？");
                dialog.setItemClick(new DelectTipDialog.ItemClick() {
                    @Override
                    public void itemClick() {

                        videoPath = null;
                        videoImageView.setImageBitmap(null);
                        videoLayoutView.setVisibility(View.GONE);

                        checkRightTitleViewStatus();

                    }
                });
                dialog.show(getSupportFragmentManager());
            }
        });


    }


    // 加载底部布局
    private void addFragmentToActivity() {
        mFragment = (PublishBottomLayoutFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (mFragment == null) {
            mFragment = new PublishBottomLayoutFragment();
        }

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.content_frame);

        // 匿名按钮监听器
        mFragment.setOnCheckedChangeListener(new PublishBottomLayoutFragment.OnCheckedChangeListener() {

            @Override
            public void onCheckedChangedCallback(CompoundButton buttonView, boolean isChecked) {
                if (storeCheckBox.getVisibility() == View.VISIBLE) {
                    if (storeCheckBox.isChecked()) {
                        Utils.showToastShortTime("显示店铺信息不能匿名发布");
                        mFragment.setSwitchIsChecked(false);
                        return;
                    }

                } else {
                    if (storeId != 0) {
                        Utils.showToastShortTime("招聘信息不能匿名发布");
                        mFragment.setSwitchIsChecked(false);
                        return;
                    }
                }

                if (isChecked) {
                    isAnonymous = 1;
                } else {
                    isAnonymous = 2;
                }

            }
        });


        mFragment.setMultimediaItemOnClickListener(new PublishBottomLayoutFragment.MultimediaItemOnClickListener() {
            @Override
            public void onClickCallback(View view, int type) {

                switch (type) {
                    case 1:// 图片

                        if (isImg == 1) {

                            if (!StringUtils.isEmpty(videoPath)) {
                                Utils.showToastShortTime("图片、视频、GIF只能选择一种展示");
                                return;
                            }

                            if (imgPublishImageRecycleAdapter.getItemCount() > 0 && multimediaItemTypeMode != type) {
                                Utils.showToastShortTime("图片、视频、GIF只能选择一种展示");
                                return;
                            }

                            //调起选择图片
                            PictureSelector.create(PublishActivity.this)
                                    .openGallery(PictureMimeType.ofImage())
                                    .maxSelectNum(maxSelectNum)
                                    .minimumCompressSize(1024)// 小于100kb的图片不压缩
                                    .compress(true) // 是否压缩 true or false
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }

                        break;

                    case 2:// 视频

                        if (isVideo == 1) {

                            if (imgPublishImageRecycleAdapter.getItemCount() > 0) {
                                Utils.showToastShortTime("图片、视频、GIF只能选择一种展示");
                                return;
                            }

                            ActiveEditSelectDialog dialog = new ActiveEditSelectDialog();
                            dialog.setItemText("拍摄", "从手机相册选择");
                            dialog.setItemClick(new ActiveEditSelectDialog.ItemClick() {
                                @Override
                                public void itemClick(int position) {
                                    switch (position) {
                                        case 1:
                                            //拍摄
                                            Intent intent = new Intent(PublishActivity.this, PublishVideoActivity.class);
                                            intent.putExtra(Constants.Fields.TYPE, 1);
                                            startActivityForResult(intent, VIDEO_CODE);
                                            break;
                                        case 2:
                                            //从手机相册选择
                                            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                            pickIntent.setType("video/*");
                                            pickIntent.addCategory(Intent.CATEGORY_OPENABLE);
                                            startActivityForResult(pickIntent, VIDEO_PICKER_CODE);
                                            break;
                                        default:
                                    }
                                }
                            });
                            dialog.show(getSupportFragmentManager());

                        }
                        break;

                    case 3:// gif

                        if (isGif == 1) {

                            if (!StringUtils.isEmpty(videoPath)) {
                                Utils.showToastShortTime("图片、视频、GIF只能选择一种展示");
                                return;
                            }


                            if (imgPublishImageRecycleAdapter.getItemCount() > 0) {
                                Utils.showToastShortTime("图片、视频、GIF只能选择一种展示");
                                return;
                            }


                            //调起选择图片
                            PictureSelector.create(PublishActivity.this)
                                    .openGallery(PictureMimeType.ofImage())
                                    .maxSelectNum(1)
                                    .isGif(true)// 是否显示gif图片 true or false
                                    .compress(true) // 是否压缩 true or false
                                    .forResult(PictureConfig.CHOOSE_REQUEST);
                        }

                        break;
                    default:

                }

                multimediaItemTypeMode = type;

            }
        });


    }

    /**
     * 检查标题右边按钮是否可点击
     */
    private void checkRightTitleViewStatus() {
        String contentText = editText.getText().toString().trim();

        if ((!StringUtils.isEmpty(contentText) || imgPublishImageRecycleAdapter.getItemCount() > 0 || !StringUtils.isEmpty(videoPath))) {
            titleFragment.getRightView().setEnabled(true);
            titleFragment.setRightViewColor(R.color.color_ffb700);

        } else {
            titleFragment.setRightViewColor(R.color.color_999999);
            titleFragment.getRightView().setEnabled(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:// 图片选择结果回调

                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    ArrayList<Image> imageList = new ArrayList(1);
                    for (LocalMedia info : selectList) {
                        Image image = new Image();
                        image.setImageUrl(info.getPath());
                        image.setThumbnailUrl(info.getCompressPath());
                        imageList.add(image);
                        maxSelectNum--;
                    }

                    selectList.clear();
                    imgPublishImageRecycleAdapter.setList(imageList);
                    imgPublishImageRecycleAdapter.notifyDataSetChanged();

                    if (imgPublishImageRecycleAdapter.getItemCount() > 0) {
                        imgRecycleView.setVisibility(View.VISIBLE);
                        numImgView.setVisibility(View.VISIBLE);
                    } else {
                        imgRecycleView.setVisibility(View.GONE);
                        numImgView.setVisibility(View.GONE);
                    }

                    if (multimediaItemTypeMode == 3) {
                        numImgView.setVisibility(View.GONE);
                    }

                    numImgView.setText((9 - maxSelectNum) + "/9");

                    checkRightTitleViewStatus();

                    break;


                case VIDEO_CODE:
                case VIDEO_PICKER_CODE:


                    if (VIDEO_PICKER_CODE == requestCode) {
                        Uri selectedMediaUri = data.getData();
                        videoPath = UriUtils.getFileAbsolutePath(App.getInstance(), selectedMediaUri);
                    } else {
                        videoPath = data.getStringExtra(Constants.KEY.KEY_EXTRA);
                    }

                    Log.e("videoPath ==== ", videoPath);

                    if (StringUtils.isEmpty(videoPath)) {
                        Log.e("==文件路径是null== ", videoPath);
                        return;
                    }

                    File file = new File(videoPath);
                    Log.e("file.length() ==== ", file.length() + "");

                    if (file.length() <= 0) {
                        Utils.showToastShortTime("对不起，您选择的文件无效");
                        return;
                    }

                    // 视频类型
                    try {
                        String suffix = videoPath.substring(videoPath.lastIndexOf(".") + 1, videoPath.length());
                        Log.e("suffix ==== ", suffix);
                        if (StringUtils.isEmpty(suffix)) {
                            Utils.showToastShortTime("对不起，您选择的文件格式无效");
                            return;
                        }

                        // 判断文件是否是视频文件
                        boolean isVideo = false;
                        for (int i = 0; i < videoSuffixs.length; i++) {
                            if (videoSuffixs[i].equals(suffix)) {
                                isVideo = true;
                                break;
                            }
                        }


                        if (!isVideo) {
                            Utils.showToastShortTime("视频文件格式(" + suffix + ")不正确");
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Utils.showToastShortTime("对不起，系统解析文件错误");
                        return;
                    }

                    if (file.length() / (1024 * 1024) > 50) {
                        Utils.showToastShortTime("文件过大，请不要超过50M");
                        return;
                    }

                    if (!StringUtils.isEmpty(videoPath)) {

                        // 视频缩略图
                        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg).error(R.mipmap.error_bg);
                        Glide.with(App.getInstance()).asBitmap().load(videoPath).apply(requestOptions).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {

                                if (resource != null && videoImageView != null) {

                                    checkRightTitleViewStatus();

                                    videoLayoutView.setVisibility(View.VISIBLE);
                                    videoImageView.setImageBitmap(resource);

                                    int width = resource.getWidth();
                                    int height = resource.getHeight();

                                    Logger.d("width =原图1== " + width + " ; height =原图1== " + height);

                                    float ww = BitmapUtils.getBitmapWidth(App.getInstance());
                                    float hh = BitmapUtils.getBitmapHeight(App.getInstance());
                                    Logger.d("ww ==指定大小== " + ww + " ; hh ==指定大小== " + hh);

                                    //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
                                    double scale = 1;//scale=1表示不缩放
                                    if (width > height && width > ww) {//如果宽度大的话根据宽度固定大小缩放
                                        scale = width / ww;
                                    } else if (width < height && height > hh) {//如果高度高的话根据宽度固定大小缩放
                                        scale = height / hh;
                                    }

                                    Logger.d("scale ==缩放比== " + scale);

                                    if (scale < 1) {
                                        scale = 1;
                                    }

                                    width = (int) (width / scale);
                                    height = (int) (height / scale);

                                    Logger.d("width =scale=最终= " + width + " ; height =scale=最终= " + height);

                                    imgWidth = width;
                                    imgHeight = height;


                                }
                            }
                        });

                    } else {
                        Utils.showToastShortTime("没有选择视频文件!");
                    }

                    break;
                case LABEL_CODE:
                    ArrayList<PostLabelInfo> parcelableArrayListExtra = data.getParcelableArrayListExtra(Constants.Fields.POST_LABEL_LIST);
                    if (parcelableArrayListExtra != null) {
                        selectLabelInfoList = parcelableArrayListExtra;
                        selectLabelCheckedStatus();
                    }
                    break;

                default:

                    checkRightTitleViewStatus();

                    break;

            }

        }
    }

    /**
     * 标签选中状态修改
     */
    private void selectLabelCheckedStatus() {
        publishLabelRecyclerViewAdapter.setSelectPostLabelInfoList(selectLabelInfoList);
        for (PostLabelInfo postLabelInfo1 : labelInfoList) {
            postLabelInfo1.setIsSelect(0);
            for (PostLabelInfo postLabelInfo : selectLabelInfoList) {
                if (postLabelInfo.getPostLabelId() == postLabelInfo1.getPostLabelId()) {
                    postLabelInfo1.setIsSelect(1);
                }
            }
        }
        tagContainerLayout.removeAllViews();
        setLabDataLayout(labelInfoList);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mFragment.setSwitchIsVisibility(View.VISIBLE);
        // 视频跟拍
        if (followingShotEnterType > 0) {
            multimediaItemTypeMode = 2;

            isImg = 0;
            isVideo = 1;
            isGif = 0;
        }

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }
        if (InterfaceUrl.URL_JUDGEISCREATESTORE.equals(apiName)) {
            if (object instanceof JudgeIsCreateStoreResponse) {
                JudgeIsCreateStoreResponse response = (JudgeIsCreateStoreResponse) object;
                StoreDetail storeDetail = response.getStoreDetail();
                if (response.getCode() == 200 && storeDetail != null && storeDetail.getIsAuth() == 2) {
                    storeId = storeDetail.getStoreInfo().getStoreId();
                    storeCheckBox.setVisibility(View.VISIBLE);
                }
            }
        } else if (InterfaceUrl.URL_SEARCHLABELBYCONTENT.equals(apiName)) {
            if (object instanceof SearchLabelByContentResponse) {
                SearchLabelByContentResponse response = (SearchLabelByContentResponse) object;
                if (response.getCode() == 200) {
                    labelInfoList = response.getList();
                    for (PostLabelInfo postLabelInfo : labelInfoList) {
                        if (postLabelInfo.getPostLabelId() == postTypeLabelId) {
                            postLabelInfo.setIsSelect(1);
                            selectLabelInfoList.add(postLabelInfo);
                            publishLabelRecyclerViewAdapter.setSelectPostLabelInfoList(selectLabelInfoList);
                            break;
                        }
                    }
                    PostLabelInfo postLabelInfo = new PostLabelInfo();
                    postLabelInfo.setLabelName("自定义标签");
                    labelInfoList.add(postLabelInfo);
                    setLabDataLayout(labelInfoList);
                }
            }
        } else if (InterfaceUrl.URL_SAVEPOST.equals(apiName)) {

            if (object instanceof savePostResponse) {
                savePostResponse response = (savePostResponse) object;
                if (response.getCode() == 200) {

                    final List<Image> imgList = imgPublishImageRecycleAdapter.getList();

                    // 上传图片到多个帖子
                    if (response.getPostIdlist() != null) {

                        publishObjectEventPostInfo(response);

                        for (int i = 0; i < response.getPostIdlist().size(); i++) {
                            parentId = response.getPostIdlist().get(i);
                            if (imgList != null && imgList.size() > 0) {

                                for (int j = 0; j < imgList.size(); j++) {
                                    Image image = imgList.get(j);
                                    getImagePresenter().uploadImages(parentId, 5, userId, DateUtils.getFormatDataString(new java.util.Date(), DateUtils.DATE_FORMAT_2), image.getImageUrl(), 0, j + 1, imgList.size());
                                }

                            }
                        }

                    }

                    finish();

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }

        }

    }


    /**
     * 构造帖子对象
     *
     * @param response
     */
    public void publishObjectEventPostInfo(savePostResponse response) {
        // 构造新的对象
        PostInfo postInfo = new PostInfo();

        if (response.getPostIdlist() != null && response.getPostIdlist().size() > 0) {
            postInfo.setPostId(response.getPostIdlist().get(0));
        }

        BaseUser baseUser = GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
        if (isAnonymous == 1) {
            String userIcon = "";
            if (BuildConfig.LOG_DEBUG) {
                userIcon = HostConfig.TEST_HOST + "/uploadfile/default/icon_default.png";
            } else {
                userIcon = HostConfig.OFFICIAL_HOST + "/uploadfile/default/icon_default.png";
            }
            baseUser.setNickName("匿名发布");
            baseUser.setUserIcon(userIcon);
        }

        baseUser.setCity(App.getInstance().getCity());
        baseUser.setLocation(App.getInstance().getUserLocation() != null ? App.getInstance().getUserLocation().getAddr() : "");

        postInfo.setBaseUser(baseUser);

        postInfo.setTopicType(1);

        postInfo.setAddTime(TimeUtils.getFriendlyTimeSpanByNow(new Date()));

        List<Image> imgList = imgPublishImageRecycleAdapter.getList();
        postInfo.setImageList(imgList);

        postInfo.setIsAnonymous(isAnonymous);

        HomeInfo homeInfo = new HomeInfo();

        String text = editText.getText().toString();
        postInfo.setContent(text);
        postInfo.setPostLabelList(selectLabelInfoList);
        homeInfo.setType(1);
        homeInfo.setBaseUser(baseUser);
        homeInfo.setPostInfo(postInfo);

    }

    /**
     * 设置标签数据
     *
     * @param labelList 标签列表数据
     */
    private void setLabDataLayout(final List<PostLabelInfo> labelList) {
        // 布局显示
        tagLayout.setVisibility(View.VISIBLE);
        for (final PostLabelInfo postLabelInfo : labelList) {
            View itemView = View.inflate(this, R.layout.public_label_tab_item_layout, null);
            final CheckBox tabTextView = itemView.findViewById(R.id.tv_label);
            if (postLabelInfo.getPostLabelId() == 0) {
                tabTextView.setText("+ " + postLabelInfo.getLabelName());
            } else {
                tabTextView.setText("# " + postLabelInfo.getLabelName());
            }
            if (postLabelInfo.getIsSelect() == 1) {
                tabTextView.setChecked(true);
            } else {
                tabTextView.setChecked(false);
            }
            tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if ("+ 自定义标签".equals(tabTextView.getText().toString())) {
                        int selectItemCount = selectLabelInfoList.size();
                        if (selectItemCount >= 3) {
                            Utils.showToastShortTime("最多可选3个标签");
                        } else {
                            Intent intent = new Intent(PublishActivity.this, PostLabelSearchActivity.class);
                            intent.putParcelableArrayListExtra(Constants.Fields.POST_LABEL_LIST, selectLabelInfoList);
                            startActivityForResult(intent, LABEL_CODE);
                        }
                        if (postLabelInfo.getIsSelect() == 1) {
                            tabTextView.setChecked(true);
                        } else {
                            tabTextView.setChecked(false);
                        }
                    } else {
                        if (postLabelInfo.getIsSelect() == 1) {
                            Iterator<PostLabelInfo> postLabelInfoIterator = selectLabelInfoList.iterator();
                            while (postLabelInfoIterator.hasNext()) {
                                PostLabelInfo postLabelInfo1 = postLabelInfoIterator.next();
                                if (postLabelInfo1.getPostLabelId() == postLabelInfo.getPostLabelId()) {
                                    postLabelInfoIterator.remove();
                                    postLabelInfo.setIsSelect(0);
                                }
                            }

                        } else {
                            if (!selectLabelInfoList.contains(postLabelInfo)) {
                                selectLabelInfoList.add(postLabelInfo);
                                postLabelInfo.setIsSelect(1);
                            }
                        }

                        int selectItemCount = selectLabelInfoList.size();
                        if (selectItemCount > 3) {
                            Utils.showToastShortTime("最多可选3个标签");
                            tabTextView.setChecked(false);
                            if (selectLabelInfoList.contains(postLabelInfo)) {
                                selectLabelInfoList.remove(postLabelInfo);
                                postLabelInfo.setIsSelect(0);
                            }
                        }
                    }
                    publishLabelRecyclerViewAdapter.setSelectPostLabelInfoList(selectLabelInfoList);
                }
            });
            tagContainerLayout.addView(itemView);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            String text = editText.getText().toString().trim();

            if (!StringUtils.isEmpty(text) || imgPublishImageRecycleAdapter.getItemCount() > 0 || !StringUtils.isEmpty(videoPath)) {
                ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                dialog.setText("编辑未完成", "退出后编辑的内容将不被保存", "退出", "继续编辑");
                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                    @Override
                    public void onLeftClick() {
                        //取消
                        onBackPressed();
                    }

                    @Override
                    public void onRightClick() {

                    }
                });
                dialog.show(getSupportFragmentManager());
            } else {
                onBackPressed();
            }


        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
