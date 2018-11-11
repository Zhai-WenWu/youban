package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
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
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.PostOperationFragmentDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by zhaiww on 2018/5/17.
 */

public class ApplyStaffActivity extends BaseActivity implements View.OnClickListener, BaseView {

    private ImageView iv_student_card;
    private ImageView iv_student_card_include_info;
    private PostOperationFragmentDialog dialog;
    private Intent intent;
    private RequestOptions options;
    private CircleImageView iv_user_icon;
    private TextView tv_user_name;
    private TextView tv_user_id;
    private GetStoreListPresenter getStoreListPresenter;
    private TextView tv_submission;
    private long userId;
    private EditText et_phone_number;
    private EditText et_addr;
    private EditText et_remarks;
    private List<Image> list = new ArrayList<>();
    private String phoneNum;
    private String addr;
    private String remarks;
    private String frontImgStr;
    private String reverseImgStr;
    private ImagePresenter imagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_staff);
        options = new RequestOptions().centerCrop().skipMemoryCache(true) // 不使用内存缓存
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        setTitle();
        initView();
        initData();
    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    private void initData() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        BaseUser baseUser = new GreenDaoUtils().getBaseUser(userId);
        Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), iv_user_icon);
        tv_user_name.setText(baseUser.getNickName());
        tv_user_id.setText("ID:" + baseUser.getUserId());
    }

    private void initView() {
        iv_student_card = findViewById(R.id.iv_student_card);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_user_id = findViewById(R.id.tv_user_id);
        tv_submission = findViewById(R.id.tv_submission);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_remarks = findViewById(R.id.et_remarks);
        et_addr = findViewById(R.id.et_addr);
        iv_student_card_include_info = findViewById(R.id.iv_student_card_include_info);
        iv_student_card.setOnClickListener(this);
        iv_student_card_include_info.setOnClickListener(this);
        tv_submission.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_student_card:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {

                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                intent = new Intent(ApplyStaffActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardFrontPhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.iv_student_card_include_info:

                dialog = new PostOperationFragmentDialog();
                dialog.setTextList("拍照", "取消");
                dialog.setItemClick(new PostOperationFragmentDialog.ViewItemClick() {
                    @Override
                    public void onClick(int type) {

                        switch (type) {
                            case 1:
                                intent = new Intent(ApplyStaffActivity.this, IDCardTakePhotoActivity.class);
                                intent.putExtra(Constants.Fields.IDCARDPHOTOURL, "/IDCardReversePhoto.jpg");
                                startActivityForResult(intent, 1);
                                break;
                        }

                    }
                });
                dialog.show(getSupportFragmentManager());

                break;
            case R.id.tv_submission:

                phoneNum = et_phone_number.getText().toString().trim();
                addr = et_addr.getText().toString().trim();
                remarks = et_remarks.getText().toString().trim();

                getImagePresenter().uploadImage(2, 19, userId, TimeUtils.date2String(new Date()), frontImgStr);//parentId无用
                getImagePresenter().uploadImage(2, 20, userId, TimeUtils.date2String(new Date()), reverseImgStr);
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
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(iv_student_card);
            } else if (idCardUrl.contains("IDCardReversePhoto")) {
                reverseImgStr = idCardUrl;
                reverseImgStr = reverseImgStr.substring(7, reverseImgStr.length());
                Glide.with(this).asBitmap().load(idCardUrl).apply(options).into(iv_student_card_include_info);
            }
        }
    }

    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("申请店员");
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_UPLOADIMAGE:
                if (object instanceof UploadImageResponse) {
                    UploadImageResponse uploadImageResponse = (UploadImageResponse) object;
                    if (uploadImageResponse.getCode() == 200) {
                        list.add(uploadImageResponse.getImage());
                        if (list.size() == 2) {
                            getStoreListPresenter().applyClert(userId, 0, phoneNum, addr, remarks, frontImgStr, reverseImgStr);
                            list.clear();
                        }
                    } else {
                        Utils.showToastShortTime(uploadImageResponse.getMsg());
                    }
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
