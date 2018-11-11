package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.net.SocketTimeoutException;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetTradAreaListResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.BusinessDistrictPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * 商圈页面
 */
public class BusinessDistrictActivity extends BaseActivity implements BaseView {

    private BusinessDistrictPresenter businessDistrictPresenter;
    private ToolBarFragment titleFragment;
    private RequestOptions options;

    /**
     * 试用跳转H5链接
     */
    private String trailUrl;

    /**
     * 闪购跳转H5链接
     */
    private String flashUrl;

    //试用跳转H5详情
    private String trailDetailUrl;
    //闪购跳转H5详情
    private String flashDetailUrl;

    public BusinessDistrictPresenter getBusinessDistrictPresenter() {
        if (businessDistrictPresenter == null) {
            businessDistrictPresenter = new BusinessDistrictPresenter(this);
        }
        return businessDistrictPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_district_layout);

        setTitle();

        options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.mipmap.error_bg);
        options.error(R.mipmap.error_bg);
        options.transform(new GlideRoundTransform(Utils.dp2px(2)));


        getBusinessDistrictPresenter().getTradAreaList(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));

    }


    private void setTitle() {

        titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("商圈");

    }

    @Override
    public void updateView(String apiName, Object object) {

        if (object instanceof Exception) {
            Exception e = (Exception) object;
            if (e instanceof SocketTimeoutException) {
                Utils.showToastShortTime("网络连接超时");
                return;
            }
            Utils.showToastShortTime(e.getMessage());
            return;
        }


        if (InterfaceUrl.URL_GETTRADAREALIST.equals(apiName)) {
            if (object instanceof GetTradAreaListResponse) {
                GetTradAreaListResponse response = (GetTradAreaListResponse) object;
                if (response.getCode() == 200) {

                    trailUrl = response.getTrailUrl();
                    flashUrl = response.getFlashUrl();

                    trailDetailUrl = response.getTrailDetailUrl();
                    flashDetailUrl = response.getFlashDetailUrl();


                    LinearLayout layout = findViewById(R.id.root_layout);

                    // 试用列表
                    if (response.getTrailList() != null && response.getTrailList().size() > 0) {

                        View itemView = View.inflate(App.getInstance(),R.layout.activity_business_district_list_item_layout,null);
                        layout.addView(itemView);

                        setItemData(itemView,response.getTrailList(),1);

                    }

                    // 闪购商品列表
                    if (response.getFlashList() != null && response.getFlashList().size() > 0) {

                        View itemView = View.inflate(App.getInstance(),R.layout.activity_business_district_list_item_layout,null);
                        layout.addView(itemView);

                        setItemData(itemView,response.getFlashList(),2);

                    }

                    // 店铺列表
                    if (response.getStoreList() != null && response.getStoreList().size() > 0) {

                        View itemView = View.inflate(App.getInstance(),R.layout.activity_business_district_list_item_layout,null);
                        layout.addView(itemView);

                        setItemData(itemView,response.getStoreList(),3);

                    }


                } else {

                    Utils.showToastShortTime(response.getMsg());

                }
            }
        }

    }


    /**
     * 设置数据
     * @param itemView UI
     * @param list 数据源
     * @param type 类型：1 ，试用，2 闪购，3 店铺
     */
    private void setItemData(View itemView, List list ,int type ){

        switch (type) {
            case 1:

                // 试用活动
                if ( list instanceof List) {
                    // 提示语
                    TextView tipTextView1 = itemView.findViewById(R.id.tip_text_view_1);
                    tipTextView1.setText("试用活动");

                    // 更多按钮
                    View tipTextView2 = itemView.findViewById(R.id.tip_text_view_2);
                    tipTextView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BusinessDistrictActivity.this, WXPayEntryActivity.class);
                            intent.putExtra(Constants.KEY.KEY_URL, trailUrl);
                            startActivity(intent);

                        }
                    });

                    itemView.findViewById(R.id.layout_view_1).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_2).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_3).setVisibility(View.INVISIBLE);

                    ProductInfo productInfo1 = null;
                    ProductInfo productInfo2 = null;
                    ProductInfo productInfo3 = null;


                    switch (list.size()) {
                        case 0:

                            break;
                        case 1:
                            productInfo1 = (ProductInfo) list.get(0);
                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);

                            break;

                        case 2:
                            productInfo1 = (ProductInfo) list.get(0);
                            productInfo2 = (ProductInfo) list.get(1);

                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);

                            break;


                        case 3:
                            productInfo1 = (ProductInfo) list.get(0);
                            productInfo2 = (ProductInfo) list.get(1);
                            productInfo3 = (ProductInfo) list.get(2);


                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_3).setVisibility(View.VISIBLE);

                            break;

                    }

                    ImageView imageView1 = itemView.findViewById(R.id.image_view_1);
                    ImageView imageView2 = itemView.findViewById(R.id.image_view_2);
                    ImageView imageView3 = itemView.findViewById(R.id.image_view_3);

                    if (productInfo1 != null) {
                        setImageView(productInfo1,imageView1,1);
                    }

                    if (productInfo2 != null) {
                        setImageView(productInfo2,imageView2,1);
                    }

                    if (productInfo3!= null) {
                        setImageView(productInfo3,imageView3,1);
                    }

                    TextView tipTextView3 = itemView.findViewById(R.id.tip_text_view_3);
                    if (productInfo1 != null) {
                        tipTextView3.setText(productInfo1.getProductName());
                    }

                    TextView tipTextView4 = itemView.findViewById(R.id.tip_text_view_4);
                    tipTextView4.setText("免费申请");

                    TextView tipTextView5 = itemView.findViewById(R.id.tip_text_view_5);
                    if (productInfo2 != null) {
                        tipTextView5.setText(productInfo2.getProductName());
                    }


                    TextView tipTextView6 = itemView.findViewById(R.id.tip_text_view_6);
                    tipTextView6.setText("免费申请");

                    TextView tipTextView7 = itemView.findViewById(R.id.tip_text_view_7);
                    if (productInfo3 != null) {
                        tipTextView7.setText(productInfo3.getProductName());
                    }

                    TextView tipTextView8 = itemView.findViewById(R.id.tip_text_view_8);
                    tipTextView8.setText("免费申请");

                }

                break;


            case 2:

                if ( list instanceof List) {
                    // 提示语
                    TextView tipTextView1 = itemView.findViewById(R.id.tip_text_view_1);
                    tipTextView1.setText("闪购活动");

                    // 更多按钮
                    View tipTextView2 = itemView.findViewById(R.id.tip_text_view_2);
                    tipTextView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(BusinessDistrictActivity.this, WXPayEntryActivity.class);
                            intent.putExtra(Constants.KEY.KEY_URL, flashUrl);
                            startActivity(intent);

                        }
                    });

                    itemView.findViewById(R.id.layout_view_1).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_2).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_3).setVisibility(View.INVISIBLE);

                    ProductInfo productInfo1 = null;
                    ProductInfo productInfo2 = null;
                    ProductInfo productInfo3 = null;


                    switch (list.size()) {
                        case 0:

                            break;
                        case 1:
                           productInfo1 = (ProductInfo) list.get(0);
                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);

                            break;

                        case 2:
                            productInfo1 = (ProductInfo) list.get(0);
                            productInfo2 = (ProductInfo) list.get(1);

                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);

                            break;


                        case 3:
                            productInfo1 = (ProductInfo) list.get(0);
                            productInfo2 = (ProductInfo) list.get(1);
                            productInfo3 = (ProductInfo) list.get(2);


                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_3).setVisibility(View.VISIBLE);

                            break;

                    }


                    ImageView imageView1 = itemView.findViewById(R.id.image_view_1);
                    ImageView imageView2 = itemView.findViewById(R.id.image_view_2);
                    ImageView imageView3 = itemView.findViewById(R.id.image_view_3);

                    if (productInfo1 != null) {
                        setImageView(productInfo1,imageView1,2);
                    }

                    if (productInfo2 != null) {
                        setImageView(productInfo2,imageView2,2);
                    }

                    if (productInfo3!= null) {
                        setImageView(productInfo3,imageView3,2);
                    }

                    TextView tipTextView3 = itemView.findViewById(R.id.tip_text_view_3);
                    if (productInfo1 != null) {
                        tipTextView3.setText(productInfo1.getProductName());
                    }


                    TextView tipTextView4 = itemView.findViewById(R.id.tip_text_view_4);
                    if (productInfo1 != null) {
                        tipTextView4.setText("活动价格：￥" + productInfo1.getSalePrice());
                    }


                    TextView tipTextView5 = itemView.findViewById(R.id.tip_text_view_5);
                    if (productInfo2 != null) {
                        tipTextView5.setText(productInfo2.getProductName());
                    }


                    TextView tipTextView6 = itemView.findViewById(R.id.tip_text_view_6);
                    if (productInfo2 != null) {
                        tipTextView6.setText("活动价格：￥" + productInfo2.getSalePrice());
                    }


                    TextView tipTextView7 = itemView.findViewById(R.id.tip_text_view_7);
                    if (productInfo3 != null) {
                        tipTextView7.setText(productInfo3.getProductName());
                    }


                    TextView tipTextView8 = itemView.findViewById(R.id.tip_text_view_8);
                    if (productInfo3 != null) {
                        tipTextView8.setText("活动价格：￥" + productInfo3.getSalePrice());
                    }
                }

                break;


            case 3:

                if ( list instanceof List) {
                    // 提示语
                    TextView tipTextView1 = itemView.findViewById(R.id.tip_text_view_1);
                    tipTextView1.setText("店铺");

                    // 更多按钮
                    View tipTextView2 = itemView.findViewById(R.id.tip_text_view_2);
                    tipTextView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            //TODO 店铺列表
                            Intent intent = new Intent(BusinessDistrictActivity.this, StoreActivity.class);
                            startActivity(intent);

                        }
                    });

                    itemView.findViewById(R.id.layout_view_1).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_2).setVisibility(View.INVISIBLE);
                    itemView.findViewById(R.id.layout_view_3).setVisibility(View.INVISIBLE);

                    StoreInfo storeInfo1 = null;
                    StoreInfo storeInfo2 = null;
                    StoreInfo storeInfo3 = null;

                    switch (list.size()) {
                        case 0:

                            break;
                        case 1:
                            storeInfo1 = (StoreInfo) list.get(0);
                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);

                            break;

                        case 2:
                            storeInfo1 = (StoreInfo) list.get(0);
                            storeInfo2 = (StoreInfo) list.get(1);

                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);

                            break;


                        case 3:
                            storeInfo1 = (StoreInfo) list.get(0);
                            storeInfo2 = (StoreInfo) list.get(1);
                            storeInfo3 = (StoreInfo) list.get(2);


                            itemView.findViewById(R.id.layout_view_1).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_2).setVisibility(View.VISIBLE);
                            itemView.findViewById(R.id.layout_view_3).setVisibility(View.VISIBLE);

                            break;

                    }


                    ImageView imageView1 = itemView.findViewById(R.id.image_view_1);
                    ImageView imageView2 = itemView.findViewById(R.id.image_view_2);
                    ImageView imageView3 = itemView.findViewById(R.id.image_view_3);

                    if (storeInfo1 != null) {
                        setImageView(storeInfo1,imageView1);
                    }

                    if (storeInfo2 != null) {
                        setImageView(storeInfo2,imageView2);
                    }

                    if (storeInfo3!= null) {
                        setImageView(storeInfo3,imageView3);
                    }

                    TextView tipTextView3 = itemView.findViewById(R.id.tip_text_view_3);
                    if (storeInfo1 != null) {
                        tipTextView3.setText(storeInfo1.getTitle());
                    }


                    TextView tipTextView4 = itemView.findViewById(R.id.tip_text_view_4);
                    if (storeInfo1 != null) {
                        BaseUser baseUser1 = storeInfo1.getBaseUser();
                        if (baseUser1 != null) {
                            tipTextView4.setText(baseUser1.getSchoolName());
                        }

                    }

                    TextView tipTextView5 = itemView.findViewById(R.id.tip_text_view_5);
                    if (storeInfo2 != null) {
                        tipTextView5.setText(storeInfo2.getTitle());
                    }


                    TextView tipTextView6 = itemView.findViewById(R.id.tip_text_view_6);
                    if (storeInfo2 != null) {
                        BaseUser baseUser2 = storeInfo2.getBaseUser();
                        if (baseUser2 != null) {
                            tipTextView6.setText(baseUser2.getSchoolName());
                        }
                    }


                    TextView tipTextView7 = itemView.findViewById(R.id.tip_text_view_7);
                    if (storeInfo3 != null) {
                        tipTextView7.setText(storeInfo3.getTitle());
                    }


                    TextView tipTextView8 = itemView.findViewById(R.id.tip_text_view_8);
                    if (storeInfo3 != null) {
                        BaseUser baseUser3 = storeInfo3.getBaseUser();
                        if (baseUser3 != null) {
                            tipTextView8.setText(baseUser3.getSchoolName());
                        }

                    }

                }

                break;

        }


    }

    private void setImageView(final ProductInfo productInfo, ImageView imageView, final int type){

        if (productInfo != null && imageView != null) {
            String imgUrl = productInfo.getProductImg();
            Glide.with(this).asBitmap().load(imgUrl).apply(options).into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BusinessDistrictActivity.this, WXPayEntryActivity.class);
                    intent.putExtra(Constants.KEY.KEY_URL, (type == 1 ? trailDetailUrl : flashDetailUrl) + "&productId=" + productInfo.getProductId() + "&sysToken=" + SafeSharePreferenceUtils.getString(Constants.KEY.KEY_SYS_TOKEN,""));
                    startActivity(intent);
                }
            });
        }


    }

    /**
     * 设置图片
     */
    private void setImageView(final StoreInfo storeInfo, ImageView imageView){
        String imgUrl3 = storeInfo.getStoreIcon();
        Glide.with(this).asBitmap().load(imgUrl3).apply(options).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BusinessDistrictActivity.this, ShopDetailActivity.class);
                intent.putExtra(Constants.Fields.STORE_ID,storeInfo.getStoreId());
                startActivity(intent);
            }
        });
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

        if (businessDistrictPresenter != null) {
            businessDistrictPresenter.onDestroy();
        }
        businessDistrictPresenter = null;


        if (options != null) {
            options = null;
        }

        if (titleFragment != null) {
            titleFragment = null;
        }

        if (trailUrl != null) {
            trailUrl = null;
        }

        if (trailDetailUrl != null) {
            trailDetailUrl = null;
        }

        if (flashUrl != null) {
            flashUrl = null;
        }

        if (flashDetailUrl != null) {
            flashDetailUrl = null;
        }


    }

}
