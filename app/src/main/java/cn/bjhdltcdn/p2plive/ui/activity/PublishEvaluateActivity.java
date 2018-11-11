package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ImageUploaderEvent;
import cn.bjhdltcdn.p2plive.event.OrderEvalusteAndRefundEvent;
import cn.bjhdltcdn.p2plive.event.VideoEvaluateEvent;
import cn.bjhdltcdn.p2plive.httpresponse.SaveOrderEvaluateResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.ProductOrder;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.service.SingleTaskShortVideoUploaderIntentService;
import cn.bjhdltcdn.p2plive.ui.adapter.SchoolShopOrderDetailAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.ImageAndVideoFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * Created by zhaiww on 2018/5/14.
 * 发布商品评价
 */

public class PublishEvaluateActivity extends BaseActivity implements BaseView, View.OnClickListener {

    private ImageAndVideoFragment mFragment;
    private LinearLayout ll_star;
    private int starLevel;
    private TitleFragment titleFragment;
    private TextView tv_submission;
    private OrderPresenter orderPresenter;
    private ProductOrder productOrder;
    private ImageView iv_shop_img;
    private TextView tv_shop_name;
    private long userId;
    private EditText et_remarks;
    private TextView tv_num_content;
    public int commentType;
    private ImagePresenter imagePresenter;
    private RecyclerView rv_school_shop;
    private TextView tv_school_goods_num;
    private TextView tv_shop_dec;
    private TextView tv_satisfied;
    private TextView tv_school_postage;
    private PostCommentListPresenter postCommentListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_evaluate);
        EventBus.getDefault().register(this);
        setTitle();
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        ll_star = findViewById(R.id.ll_star);
        tv_submission = findViewById(R.id.tv_submission);
        iv_shop_img = findViewById(R.id.iv_shop_img);
        tv_shop_name = findViewById(R.id.tv_shop_name);
        tv_num_content = findViewById(R.id.tv_num_content);
        et_remarks = findViewById(R.id.et_remarks);
        rv_school_shop = findViewById(R.id.rv_school_shop);
        tv_school_goods_num = findViewById(R.id.tv_school_goods_num);
        tv_shop_dec = findViewById(R.id.tv_shop_dec);
        tv_satisfied = findViewById(R.id.tv_satisfied);
        tv_school_postage = findViewById(R.id.tv_school_postage);
        tv_submission.setOnClickListener(this);

        mFragment = (ImageAndVideoFragment) getSupportFragmentManager().findFragmentById(R.id.fl_photo);
        if (mFragment == null) {
            mFragment = new ImageAndVideoFragment();
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mFragment, R.id.fl_photo);

        et_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() <= 500) {
                    tv_num_content.setText(s.length() + "/500");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });

        for (int i = 0; i < ll_star.getChildCount(); i++) {
            ImageView imageView = (ImageView) ll_star.getChildAt(i);
            imageView.setOnClickListener(new onStarClick(i));
        }

    }

    private void initData() {
        productOrder = getIntent().getParcelableExtra(Constants.Fields.ORDERINFO);
        if (productOrder != null) {
            if (productOrder.getStoreInfo() != null) {
                Utils.ImageViewDisplayByUrl(productOrder.getStoreInfo().getStoreIcon(), iv_shop_img);
                tv_shop_name.setText(productOrder.getStoreInfo().getTitle());
                tv_shop_dec.setText(productOrder.getStoreInfo().getStoreDesc());
            }
        }

        tv_school_goods_num.setText("共计" + productOrder.getTotalProductCount() + "件商品   合计：¥" + productOrder.getTotalPrice());

        SchoolShopOrderDetailAdapter schoolShopOrderDetailAdapter = new SchoolShopOrderDetailAdapter(this);
        rv_school_shop.setHasFixedSize(true);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutSpaceItemDecoration linearLayoutSpaceItemDecoration = new LinearLayoutSpaceItemDecoration(Utils.dp2px(10));
        rv_school_shop.setLayoutManager(linearlayoutManager);
        rv_school_shop.addItemDecoration(linearLayoutSpaceItemDecoration);
        rv_school_shop.setAdapter(schoolShopOrderDetailAdapter);
        schoolShopOrderDetailAdapter.setmList(productOrder.getProductList());

        if (productOrder.getPostAge() == null) {
            tv_school_postage.setText("配送费：¥0");
        } else {
            if (StringUtils.isEmpty(productOrder.getPostAge())) {
                tv_school_postage.setText("配送费：¥0");
            } else {
                tv_school_postage.setText("配送费：¥" + productOrder.getPostAge());
            }
        }
    }

    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle(R.string.title_publishevaluate);
        titleFragment.setLeftViewTitle(R.mipmap.back_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_SAVEORDEREVALUATE)) {
            if (object instanceof SaveOrderEvaluateResponse) {
                SaveOrderEvaluateResponse saveOrderEvaluateResponse = (SaveOrderEvaluateResponse) object;
                Utils.showToastShortTime(saveOrderEvaluateResponse.getMsg());
                OrderEvalusteAndRefundEvent orderEvalusteAndRefundEvent = new OrderEvalusteAndRefundEvent();
                orderEvalusteAndRefundEvent.setIsEval(1);
                EventBus.getDefault().post(orderEvalusteAndRefundEvent);
                if (saveOrderEvaluateResponse.getCode() == 200) {
                    List<Image> newImageList = mFragment.getNewImageList();
                    for (int i = 0; i < newImageList.size(); i++) {
                        Image image = newImageList.get(i);
                        getImagePresenter().uploadImages(saveOrderEvaluateResponse.getComment().getCommentId(), 23, userId, TimeUtils.date2String(new Date()), image.getImageUrl(), image.getImageId(), i, newImageList.size());
                    }
                    finish();
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

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    public ImagePresenter getImagePresenter() {
        if (imagePresenter == null) {
            imagePresenter = new ImagePresenter(this);
        }
        return imagePresenter;
    }

    public PostCommentListPresenter getPostPresenter() {
        if (postCommentListPresenter == null) {
            postCommentListPresenter = new PostCommentListPresenter(this);
        }
        return postCommentListPresenter;


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submission:
                if (starLevel == 0) {
                    Utils.showToastShortTime("请给店家一个五星好评吧！");
                } else {
                    if (mFragment.getNewImageList().size() > 0) {
                        commentType = 1;
                    }

                    if (!StringUtils.isEmpty(mFragment.getVideoUrl())) {
                        commentType = 2;
                    }

                    if (mFragment.getNewImageList().size() > 0 && !StringUtils.isEmpty(mFragment.getVideoUrl())) {
                        commentType = 3;
                    }

                    if (!StringUtils.isEmpty(mFragment.getVideoUrl())) {
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>(1);
                        map.put(Constants.Fields.VIDEO_PATH, mFragment.getVideoUrl());
                        map.put(Constants.Fields.IMG_WIDTH, mFragment.getImgWidth());
                        map.put(Constants.Fields.IMG_HEIGHT, mFragment.getImgHeight());
                        map.put(Constants.Fields.CONTENT_STR, "");
                        map.put(Constants.Fields.IS_ANONYMOUS, 0);
                        map.put(Constants.Fields.LAUNCH_ID, productOrder.getOrderId());
                        map.put(Constants.Fields.TO_USER_ID, 0);
                        map.put(Constants.Fields.ENTER_TYPE_MODE, 8);
                        map.put(Constants.Fields.TYPE, 1);
                        SingleTaskShortVideoUploaderIntentService.startShortVideoUploader(PublishEvaluateActivity.this, map);
                        ProgressDialogUtils.getInstance().showProgressDialog(this);
//                        Utils.showToastShortTime("视频上传中，上传完成后将自动发布。");
                    } else {
                        getOrderPresenter().saveOrderEvaluate(userId, productOrder.getOrderId(), productOrder.getProductInfo().getProductId(), 0, userId, 0, 0, 1, starLevel, et_remarks.getText().toString(), commentType, "", "");
                    }
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(VideoEvaluateEvent event) {
        if (event == null) {
            return;
        }
        getOrderPresenter().saveOrderEvaluate(userId, productOrder.getOrderId(), productOrder.getProductInfo().getProductId(), 0, userId, 0, 0, 1, starLevel, et_remarks.getText().toString(), commentType, event.getVideoPath(), event.getVideoImagePath());
    }

    class onStarClick implements View.OnClickListener {
        int i;


        public onStarClick(int i) {
            this.i = i;
        }

        @Override
        public void onClick(View v) {
            starLevel = i + 1;

            switch (starLevel) {
                case 1:
                case 2:
                    tv_satisfied.setText("不满意");
                    break;
                case 3:
                case 4:
                case 5:
                    tv_satisfied.setText("满意");
                    break;
            }

            for (int j = 0; j < ll_star.getChildCount(); j++) {
                ImageView imageView = (ImageView) ll_star.getChildAt(j);
                imageView.setImageResource(R.drawable.goods_star_false);
            }
            for (int j = 0; j <= i; j++) {
                ImageView imageView = (ImageView) ll_star.getChildAt(j);
                imageView.setImageResource(R.drawable.goods_star_true);
            }
        }
    }
}
