package cn.bjhdltcdn.p2plive.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateShopDetailEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateCreateStoreResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.ActivityLocationInfo;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.model.LocationInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.model.StoreImage;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
import cn.bjhdltcdn.p2plive.ui.adapter.CreateShopCategoryRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.CreateShopDeliveryModeRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ImageAndVideoFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;

/**
 * 创建店铺
 */
public class CreateShopActivity extends BaseActivity implements BaseView {
    private GetStoreListPresenter presenter;
    private DiscoverPresenter discoverPresenter;
    private ImagePresenter imagePresenter;
    private ImageView imageView;
    // 选择的图片路径
    private String selectFileUrl;
    private EditText titleEditView,storeDescEditView, floorPriceEditView,postageEditView;
    private TextView selectLocationTextView,locationTextView,contentNumView,createTextView;
    private CheckBox recruitClertCheckBox,ischeckCheckbox,freeShippingCheckbox;
    private RadioGroup distributionScopeRadioGroup;
    private RadioButton sameSchoolRadioButton,wholeCountryRadioButton;
    private boolean needShowLoading = true;
    private RecyclerView deliveryModeRecycleView;
    private CreateShopDeliveryModeRecyclerViewAdapter deliveryModeRecyclerViewAdapter;
    private LocationInfo locationInfo;
    private ImageAndVideoFragment imageAndVideoFragment;
    private long userId;
    private StoreInfo storeInfo;
    private StoreDetail storeDetail;
    private int comeInType;//0:从申请开店进入 1：从店铺详情进入
    private int distribution=0;//配送范围(0本校1全国),


    public GetStoreListPresenter getPresenter() {
        if (presenter == null) {
            presenter = new GetStoreListPresenter(this);
        }
        return presenter;
    }

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop_layout);
        userId=SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        Intent intent=getIntent();
        storeDetail=intent.getParcelableExtra("storeDetail");
        if(storeDetail!=null){
            storeInfo=storeDetail.getStoreInfo();
        }
        comeInType=intent.getIntExtra("comeInType",0);
        setTitle();
        initView();
        getDiscoverPresenter().getLabelList(8);
        getDiscoverPresenter().getLabelList(10);
        if(storeDetail!=null){
            getPresenter().findStoreDetail(userId, storeInfo.getStoreId());
        }
    }

    private void setTitle() {

        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        if(storeInfo!=null){
            titleFragment.setTitle(R.string.title_edit_shop);
        }else{
            titleFragment.setTitle(R.string.title_create_shop);
        }

        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }


    public void initView() {
        // 选择图片
        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(CreateShopActivity.this)
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


        // 圈子所属类别
        contentNumView = findViewById(R.id.content_num_view);
        // 名称
        titleEditView = findViewById(R.id.edit_view_shop_name);
        // 简介
        storeDescEditView = findViewById(R.id.edit_view_shop_describe);
        storeDescEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 500) {
                    contentNumView.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        deliveryModeRecycleView=findViewById(R.id.recycler_delivery_mode);
        deliveryModeRecyclerViewAdapter=new CreateShopDeliveryModeRecyclerViewAdapter(this);
        FlowLayoutManager flowLayoutManager1 = new FlowLayoutManager(this);
        flowLayoutManager1.setAutoMeasureEnabled(true);
        deliveryModeRecycleView.setLayoutManager(flowLayoutManager1);
        deliveryModeRecycleView.setAdapter(deliveryModeRecyclerViewAdapter);

        selectLocationTextView = findViewById(R.id.select_location_text_view);
        selectLocationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateShopActivity.this, ActiveSelectLocationActivity.class);
                intent.putExtra(Constants.Fields.TYPE,1);
                startActivityForResult(intent, 1);
            }
        });
        locationTextView= findViewById(R.id.location_text_view);

        floorPriceEditView= findViewById(R.id.charge_edit);
        floorPriceEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                floorPriceEditView.setSelection(s.toString().length());
                if (s.length() >=4) {
                    floorPriceEditView.setText(s.subSequence(0,3));
                    Utils.showToastShortTime("起送价格不得超过3位数");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        postageEditView= findViewById(R.id.postage_edit);
        postageEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                postageEditView.setSelection(s.toString().length());
                if (s.length() >=5) {
                    postageEditView.setText(s.subSequence(0,4));
                    Utils.showToastShortTime("邮费价格不得超过4位数");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        recruitClertCheckBox= findViewById(R.id.recruit_clert_checkbox);
        recruitClertCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isPressed()&&b){
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("", "招聘店员，经店主审核通过之后，代表店员将有权对该店订单进行接单处理，店主可到“店铺-查看店员”进行设置。是否招聘店员？", "取消", "确定");
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            //取消
                            recruitClertCheckBox.setChecked(false);
                        }

                        @Override
                        public void onRightClick() {
                        }
                    });
                    dialog.show(CreateShopActivity.this.getSupportFragmentManager());
                }
            }
        });
        ischeckCheckbox= findViewById(R.id.ischeck_checkbox);
        freeShippingCheckbox= findViewById(R.id.free_shipping_checkbox);
        distributionScopeRadioGroup= findViewById(R.id.distribution_scope_radio_group);
        sameSchoolRadioButton= findViewById(R.id.distribution_scope_same_school_radio);
        wholeCountryRadioButton= findViewById(R.id.distribution_scope_whole_country_radio);
        distributionScopeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId==sameSchoolRadioButton.getId()){
                    //配送范围 全校
                    distribution=0;
                }else if(checkedId==wholeCountryRadioButton.getId()){
                    //配送范围 全国
                    distribution=1;
                }
            }
        });
        createTextView = findViewById(R.id.create_btn_view);
        createTextView.setText("创建");
//        createTextView.setBackgroundResource(R.drawable.shape_round_10_solid_e6e6e6);
        createTextView.setBackgroundResource(R.drawable.shape_round_10_solid_ffee00);
        if(storeInfo!=null){
            createTextView.setText("保存");
        }else{
            createTextView.setText("创建");
        }
        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Utils.isAllowClick()){
                    //判断快速点击
                    return;
                }

                if(storeInfo==null){
                    storeInfo=new StoreInfo();
                }
                storeInfo.setUserId(userId);

                  if(StringUtils.isEmpty(selectFileUrl)){
                      Utils.showToastShortTime("请上传店铺头像");
                      return;
                  }

                String title=titleEditView.getText().toString();
                if (StringUtils.isEmpty(title)) {
                    Utils.showToastShortTime("请输入店铺名称");
                    return;
                }
                storeInfo.setTitle(title);


                String storeDesStr=storeDescEditView.getText().toString();
                if (StringUtils.isEmpty(storeDesStr)) {
                    Utils.showToastShortTime("请添加店铺描述");
                    return;
                }
                storeInfo.setStoreDesc(storeDesStr);

                if(locationInfo==null){
                    Utils.showToastShortTime("请选择店铺位置");
                    return;
                }
                storeInfo.setLocationInfo(locationInfo);

//                List<LabelInfo> categoryLabelInfoList=categoryRecyclerViewAdapter.getSelectList();
//                if(categoryLabelInfoList==null||categoryLabelInfoList.size()<=0){
//                    Utils.showToastShortTime("请选择店铺类别");
//                    return;
//                }
//                storeInfo.setLabelList(categoryLabelInfoList);

                List<LabelInfo> modeLabelInfoList=deliveryModeRecyclerViewAdapter.getSelectList();
                if(modeLabelInfoList==null||modeLabelInfoList.size()<=0){
                    Utils.showToastShortTime("请选择送货方式");
                    return;
                }
                storeInfo.setDistributeList(modeLabelInfoList);

                if(StringUtils.isEmpty(floorPriceEditView.getText().toString())){
                    storeInfo.setFloorPrice(0+"");
                }else{
                    storeInfo.setFloorPrice(floorPriceEditView.getText().toString());
                }

                if(recruitClertCheckBox.isChecked()){
                    storeInfo.setIsRecruitClert(1);
                }else{
                    storeInfo.setIsRecruitClert(0);
                }

                if(ischeckCheckbox.isChecked()){
                    storeInfo.setIsCheck(1);
                }else{
                    storeInfo.setIsCheck(0);
                }

                if(freeShippingCheckbox.isChecked()){
                    storeInfo.setIsSchoolPostage(0);
                }else{
                    storeInfo.setIsSchoolPostage(1);
                }

                if(StringUtils.isEmpty(postageEditView.getText().toString())){
                    storeInfo.setPostage(0+"");
                }else{
                    storeInfo.setPostage(postageEditView.getText().toString());
                }

                storeInfo.setDistribution(distribution);

//                if (imageAndVideoFragment.getNewImageList().size() < 1&&StringUtils.isEmpty(imageAndVideoFragment.getVideoUrlLocal())) {
//                    Utils.showToastShortTime("请添加宣传图片或视频 ");
//                    return;
//                }

                getPresenter().updateCreateStore(userId,storeInfo);


            }
        });
        if(storeDetail==null){
            initStoreInfo(storeInfo);
        }
    }

    public void initStoreInfo(StoreInfo storeInfo){
        imageAndVideoFragment = (ImageAndVideoFragment) getSupportFragmentManager().findFragmentById(R.id.video_and_imag_fragment);
        if (imageAndVideoFragment == null) {
            imageAndVideoFragment = new ImageAndVideoFragment();
        }
        imageAndVideoFragment.setMaxImageNum(3);
        if(storeInfo!=null){
            selectFileUrl=storeInfo.getStoreIcon();
            showImage();
            titleEditView.setText(storeInfo.getTitle());
            storeDescEditView.setText(storeInfo.getStoreDesc());
            int length=storeDescEditView.getText().toString().length();
            contentNumView.setText(length + "/500");
            locationInfo=storeInfo.getLocationInfo();
            if(locationInfo!=null&&!StringUtils.isEmpty(locationInfo.getAddr())){
                locationTextView.setVisibility(View.VISIBLE);
                locationTextView.setText(locationInfo.getAddr());
            }else
            {
                locationTextView.setVisibility(View.GONE);
            }

            if(!StringUtils.isEmpty(storeInfo.getFloorPrice()))
            {
                double floorPriceDouble=Double.parseDouble(storeInfo.getFloorPrice());
                BigDecimal floorPriceBigDecimal=new BigDecimal(floorPriceDouble+"");
                if(floorPriceDouble>0d){
                    floorPriceEditView.setText(floorPriceBigDecimal.stripTrailingZeros().toPlainString());
                }
            }


            int isRecruitClert=storeInfo.getIsRecruitClert();
            if(isRecruitClert==1){
                recruitClertCheckBox.setChecked(true);
            }else{
                recruitClertCheckBox.setChecked(false);
            }
            int isCheck=storeInfo.getIsCheck();
            if(isCheck==1){
                ischeckCheckbox.setChecked(true);
            }else{
                ischeckCheckbox.setChecked(false);
            }
            //邮费价格,
            if(!StringUtils.isEmpty(storeInfo.getPostage()))
            {
                double postageeDouble=Double.parseDouble(storeInfo.getPostage());
                BigDecimal postageeBigDecimal=new BigDecimal(postageeDouble+"");
                if(postageeDouble>0d){
                    postageEditView.setText(postageeBigDecimal.stripTrailingZeros().toPlainString());
                }
            }
            //本校包邮(0是,1否),
            int isSchoolPostage=storeInfo.getIsSchoolPostage();
            if(isSchoolPostage==1){
                freeShippingCheckbox.setChecked(false);
            }else{
                freeShippingCheckbox.setChecked(true);
            }
            //配送范围(0本校1全国),
            distribution=storeInfo.getDistribution();
            if(distribution==0){
               sameSchoolRadioButton.setChecked(true);
            }else{
               wholeCountryRadioButton.setChecked(true);
            }

            List<StoreImage> storeImageList=storeInfo.getStoreImageList();
            List<Image> imageList = new ArrayList<Image>();
            String videoUrl=null;
            String videoImageUrl="";
            long imageId=0;
            if(storeImageList!=null){
                for (int i=0;i<storeImageList.size();i++){
                    StoreImage storeImage=storeImageList.get(i);
                    if(storeImage.getImageType()==1){
                        //初始化图片
                        Image image=new Image();
                        image.setImageUrl(storeImage.getImageUrl());
                        image.setImageId(storeImage.getImageId());
                        image.setThumbnailUrl(storeImage.getThumbnailUrl());
                        image.setParentId(storeImage.getParentId());
                        imageList.add(image);
                    }else{
                        //初始化视频
                        imageId=storeImage.getImageId();
                        videoUrl=storeImage.getVideoUrl();
                        videoImageUrl=storeImage.getVideoImageUrl();
                    }

                }
                imageAndVideoFragment.setType(21);
                imageAndVideoFragment.setServerData(imageList,videoUrl,videoImageUrl,imageId);

            }
        }else{
            showImage();
            //配送范围(0本校1全国),
            sameSchoolRadioButton.setChecked(true);
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), imageAndVideoFragment, R.id.video_and_imag_fragment);
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
                            showImage();
                        }
                    }

                    break;
                case 1:
                    ActivityLocationInfo activityLocationInfo=data.getParcelableExtra("location");
                    if(activityLocationInfo!=null){
                        if(locationInfo==null){
                            locationInfo=new LocationInfo();
                        }
                        locationInfo.setLongitude(activityLocationInfo.getLongitude());
                        locationInfo.setLatitude(activityLocationInfo.getLatitude());
                        locationInfo.setAddr(activityLocationInfo.getAddr());
                        locationInfo.setCity(activityLocationInfo.getCity());
                        locationInfo.setDistrict(activityLocationInfo.getDistrict());
                        locationInfo.setProvince(activityLocationInfo.getProvince());
                    }
                    locationTextView.setText(locationInfo.getAddr());
                    locationTextView.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }


    private void showImage() {

        if (!StringUtils.isEmpty(selectFileUrl)) {
            // 隐藏掉文字
            TextView textViewTips2 = findViewById(R.id.text_view_tips2);
            textViewTips2.setText("店铺头像");
            textViewTips2.setVisibility(View.GONE);
        }

        // 绑定选择过的图片
        RequestOptions options = new RequestOptions();
        options.diskCacheStrategy(DiskCacheStrategy.ALL);
        options.placeholder(R.mipmap.add_image_icon);
        Glide.with(this).load(selectFileUrl).apply(options).into(imageView);

    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }

        if (InterfaceUrl.URL_UPDATECREATESTORE.equals(apiName)) {
            if (object instanceof UpdateCreateStoreResponse) {
                final UpdateCreateStoreResponse response = (UpdateCreateStoreResponse) object;

                if (response.getCode() == 200) {
                    //上传店铺头像
                    getImagePresenter().uploadImage(response.getStoreId(), 18, userId, TimeUtils.date2String(new Date()), selectFileUrl);
                    //上传店铺宣传图片
                    List<StoreImage> storeImageList=new ArrayList<StoreImage>();//本地更新店铺详情中用的图片和视频列表
                    if (imageAndVideoFragment.getNewImageList().size() > 0) {
                        for (int i = 0; i < imageAndVideoFragment.getNewImageList().size(); i++) {
                            Image image = imageAndVideoFragment.getNewImageList().get(i);
                            // "add"图片
                            if (image.getImageUrl().contains("add")) {
                                continue;
                            }
                            storeImageList.add(Utils.imageToStoreImage(image,1,"",""));
                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                            //网络图片
                            String imageUrl = null;
                            if (image.getImageId() > 0) {
                                imageUrl = null;
                            } else {
                                imageUrl = image.getImageUrl();
                            }
                            getImagePresenter().uploadImages(response.getStoreId(), 21, userId, TimeUtils.date2String(new Date()), imageUrl, image.getImageId(), i + 1, imageAndVideoFragment.getNewImageList().size() - 1);

                        }
                     }
                     if(!StringUtils.isEmpty(imageAndVideoFragment.getVideoUrl())){
                         //上传视频文件
                         LinkedHashMap<String, Object> map = new LinkedHashMap<>(1);
                         map.put(Constants.Fields.VIDEO_PATH, imageAndVideoFragment.getVideoUrl());
                         map.put(Constants.Fields.IMG_WIDTH, imageAndVideoFragment.getImgWidth());
                         map.put(Constants.Fields.IMG_HEIGHT, imageAndVideoFragment.getImgHeight());
                         map.put(Constants.Fields.CONTENT_STR, "");
                         map.put(Constants.Fields.IS_ANONYMOUS, 0);
                         map.put(Constants.Fields.LAUNCH_ID, response.getStoreId());
                         map.put(Constants.Fields.TO_USER_ID, 0);
                         map.put(Constants.Fields.ENTER_TYPE_MODE, 7);
                         map.put(Constants.Fields.TYPE, 1);
                         SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(CreateShopActivity.this, map);
                     }
                     if(!StringUtils.isEmpty(imageAndVideoFragment.getVideoUrlLocal())){
                         storeImageList.add(0,Utils.imageToStoreImage(new Image(),2,imageAndVideoFragment.getVideoUrlLocal(),""));
                     }
                    if(comeInType==0) {
                        //跳转到店铺详情界面
                        Intent intent = new Intent(CreateShopActivity.this, ShopDetailActivity.class);
                        intent.putExtra(Constants.Fields.STORE_ID, response.getStoreId());
                        intent.putExtra("isFirst", true);
                        startActivity(intent);
                    }else{
                         storeInfo.setStoreIcon(selectFileUrl);
                         storeInfo.setStoreImageList(storeImageList);
                         storeDetail.setStoreInfo(storeInfo);
                         EventBus.getDefault().post(new UpdateShopDetailEvent(storeDetail));
                    }
                    finish();

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (InterfaceUrl.URL_UPLOADIMAGE.equals(apiName)) {
            if (object instanceof UploadImageResponse) {
                final UploadImageResponse response = (UploadImageResponse) object;

                if (response.getCode() == 200) {

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        }else if (InterfaceUrl.URL_GETLABELLIST.equals(apiName)) {
            if (object instanceof GetLabelListResponse) {
                final GetLabelListResponse response = (GetLabelListResponse) object;
                if (response.getCode() == 200) {
                    List<LabelInfo> labelInfoList=response.getLabelList();
                    if(labelInfoList!=null&&labelInfoList.size()>0){
                        LabelInfo labelInfo=labelInfoList.get(0);
                        if(labelInfo.getLabelType()==10){
                            deliveryModeRecyclerViewAdapter.setList(labelInfoList);
                            if(storeInfo!=null) {
                                deliveryModeRecyclerViewAdapter.resetList(storeInfo.getDistributeList());
                            }
                            deliveryModeRecyclerViewAdapter.notifyDataSetChanged();

                        }
                    }

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        }if (apiName.equals(InterfaceUrl.URL_FINDSTOREDETAIL)) {
            if (object instanceof FindStoreDetailResponse) {
                FindStoreDetailResponse findStoreDetailResponse = (FindStoreDetailResponse) object;
                if (findStoreDetailResponse.getCode() == 200) {
                    storeDetail = findStoreDetailResponse.getStoreDetail();
                    if (storeDetail != null) {
                        initStoreInfo(storeDetail.getStoreInfo());
                    }
                } else {
                    Utils.showToastShortTime(findStoreDetailResponse.getMsg());
                    finish();
                }
            }
        }
    }

    @Override
    public void showLoading() {
        ProgressDialogUtils.getInstance().showProgressDialog(CreateShopActivity.this);

    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (imageView != null) {
            imageView = null;
        }

        if (selectFileUrl != null) {
            selectFileUrl = null;
        }

        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }

        if (imagePresenter != null) {
            imagePresenter.onDestroy();
            imagePresenter = null;
        }

    }


}
