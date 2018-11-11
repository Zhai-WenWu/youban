package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAuthContentInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;

/**
 * 实名认证页面
 */
public class RealNameCertificationActivity extends BaseActivity implements BaseView, View.OnClickListener {
    private TextView tv_tip_1, tv_tip_2;
    private EditText edit_name_rela, edit_phonenumber, edit_id_card;
    private ImageView img_card_front, img_card_reverse;
    private CardView card_submit;
    private UserPresenter userPresenter;
    private String frontImgStr;//身份证正面
    private String reverseImgStr;//身份证反面
    private boolean isSelectFront = true;//选择身份证正面
    private List<Image> list = new ArrayList<>(1);
    private ImagePresenter imagePresenter;
    private Intent intent;
    private RequestOptions options;
    private PostOperationFragmentDialog dialog;
    private long storeId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_certification);
        storeId=getIntent().getLongExtra(Constants.Fields.STORE_ID,0);
        options = new RequestOptions().centerCrop().skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        userPresenter = new UserPresenter(this);
        imagePresenter = new ImagePresenter(this);
        setTitle();
        init();
        userPresenter.getAuthContentInfo();
    }

    private void init() {
        tv_tip_1 = findViewById(R.id.tv_tip_1);
        tv_tip_2 = findViewById(R.id.tv_tip_2);
        edit_name_rela = findViewById(R.id.edit_name_rela);
        edit_phonenumber = findViewById(R.id.edit_phonenumber);
        edit_id_card = findViewById(R.id.edit_id_card);
        img_card_front = findViewById(R.id.img_card_front);
        img_card_reverse = findViewById(R.id.img_card_reverse);
        card_submit = findViewById(R.id.card_submit);
        img_card_front.setOnClickListener(this);
        img_card_reverse.setOnClickListener(this);
        card_submit.setOnClickListener(this);
        //String str = "请填写以下内容并提交，未提交实名认证或认证不通过的用户将无法进行<font color='#000000'>提现，开启聊天频道等操作。</font>";
        //tv_tip_1.setText(Html.fromHtml(str));
        edit_name_rela.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString().trim())) {
                    card_submit.setCardBackgroundColor(getResources().getColor(R.color.color_ffee00));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETAUTHCONTENTINFO:
                if (object instanceof GetAuthContentInfoResponse) {
                    GetAuthContentInfoResponse getAuthContentInfoResponse = (GetAuthContentInfoResponse) object;
                    if (getAuthContentInfoResponse.getCode() == 200) {
                        String content1 = getAuthContentInfoResponse.getContent1();
                        if (!TextUtils.isEmpty(content1)) {
                            //tv_tip_1.setText(content1);
                            tv_tip_1.setText(content1);
                        }
                        String content2 = getAuthContentInfoResponse.getContent2();
                        if (!TextUtils.isEmpty(content2)) {
                            tv_tip_2.setText(content2);
                        }
                    }
                }
                break;
            case InterfaceUrl.URL_SAVEREALNAMEAUTH:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                    hideLoading();
                    if (baseResponse.getCode() == 200) {
                        //实名认证状态(0未认证,1等待审核,2已认证,3审核失败)
                        SafeSharePreferenceUtils.saveInt(Constants.Fields.AUTHSTATUS, 1);
                        if(storeId==0){
                            //未创建店铺
                            Intent intent=new Intent(RealNameCertificationActivity.this,CreateShopActivity.class);
                            startActivity(intent);
                        }else{
                            //已创建店铺
                            Intent intent=new Intent(RealNameCertificationActivity.this,ShopDetailActivity.class);
                            intent.putExtra(Constants.Fields.STORE_ID,storeId);
                            startActivity(intent);
                        }
                        finish();
                    }
                }
                break;

            case InterfaceUrl.URL_UPLOADIMAGE:
                if (object instanceof UploadImageResponse) {
                    UploadImageResponse uploadImageResponse = (UploadImageResponse) object;
                    if (uploadImageResponse.getCode() == 200) {

                        if (list != null) {
                            list.add(uploadImageResponse.getImage());

                            if (list.size() == 2) {
                                userPresenter.saveRealNameAuth(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), getNameStr(),
                                        getPhoneStr(), getCardIdStr(), list);
                                list.clear();
                            }

                        }

                    } else {
                        Utils.showToastShortTime(uploadImageResponse.getMsg());
                        hideLoading();
                    }

                    card_submit.setEnabled(true);

                }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_card_front:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                intent = new Intent(RealNameCertificationActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardFrontPhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.img_card_reverse:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                isSelectFront = false;
                                intent = new Intent(RealNameCertificationActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardReversePhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.card_submit:
                if (TextUtils.isEmpty(getNameStr())) {
                    Utils.showToastShortTime("名字不能为空");
                } else if (TextUtils.isEmpty(getPhoneStr())) {
                    Utils.showToastShortTime("手机号不能为空");
                } else if (!StringUtils.isFormatedPhoneNumber(getPhoneStr())) {
                    Utils.showToastShortTime("请输入正确的手机号");
                } else if (TextUtils.isEmpty(frontImgStr)) {
                    Utils.showToastShortTime("身份证号不能为空");
                } else if (!StringUtils.IDCardValidate(getCardIdStr())) {
                    Utils.showToastShortTime("请输入正确的的身份证号码");
                } else if (TextUtils.isEmpty(frontImgStr) || TextUtils.isEmpty(reverseImgStr)) {
                    Utils.showToastShortTime("请按要求上传完整身份证照片");
                } else {

                    card_submit.setEnabled(false);

                    if (list != null) {
                        list.clear();
                    }

                    showLoading();

                    //parentId无用
                    imagePresenter.uploadImage(2, 8, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TimeUtils.date2String(new Date()), frontImgStr);
                    imagePresenter.uploadImage(2, 9, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), TimeUtils.date2String(new Date()), reverseImgStr);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2) {
            String idCardUrl = data.getStringExtra("URL");
            if (idCardUrl.contains("IDCardFrontPhoto")) {
                frontImgStr = idCardUrl;
                frontImgStr = frontImgStr.substring(7, frontImgStr.length());
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(img_card_front);
            } else if (idCardUrl.contains("IDCardReversePhoto")) {
                reverseImgStr = idCardUrl;
                reverseImgStr = reverseImgStr.substring(7, reverseImgStr.length());
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(img_card_reverse);
            }
        }
    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("实名认证");
    }

    /**
     * 获取名字
     *
     * @return
     */
    private String getNameStr() {
        return edit_name_rela.getText().toString();
    }

    /**
     * 获取手机号
     *
     * @return
     */
    private String getPhoneStr() {
        return edit_phonenumber.getText().toString();
    }

    /**
     * 获取身份证号
     *
     * @return
     */
    private String getCardIdStr() {
        return edit_id_card.getText().toString();
    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }
}
