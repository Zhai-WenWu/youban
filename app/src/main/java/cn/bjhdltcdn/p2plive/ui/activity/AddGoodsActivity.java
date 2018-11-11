package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.UpdateGoodsListEvent;
import cn.bjhdltcdn.p2plive.httpresponse.FindProductDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateProductInfoResponse;
import cn.bjhdltcdn.p2plive.model.Image;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductImage;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ImagePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.ui.fragment.ImageAlbumFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.timeutils.TimeUtils;

import static cn.bjhdltcdn.p2plive.utils.DateUtils.getFormatDataString;

/**
 * Created by Hu_PC on 2017/11/9.
 * 添加商品页
 */

public class AddGoodsActivity extends BaseActivity implements BaseView {

    private GetStoreListPresenter presenter;
    private ImagePresenter imagePresenter;
    private RelativeLayout addImgLayout;
    private long userId;
    private TitleFragment titleFragment;
    private ImageAlbumFragment imageAlbumFragment,imageAlbumDetailFragment;
    private EditText productNameEditView,productDescEditView,productPriceEditView,productDiscountEditView,salePriceEditView,productRemainTotalEditView;
    private TextView tv_num_tv;
    private Button okBtn;
    private ProductDetail productDetail;
    private int status;
    private long storeId;
    private String productName,productDesc,productPrice,productDiscount,salePrice,productRemainTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        Intent intent=getIntent();
        productDetail=intent.getParcelableExtra("productDetail");
        storeId=intent.getLongExtra("storeId",0);
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        presenter = new GetStoreListPresenter(this);
        imagePresenter = new ImagePresenter(this);
        initView();
        setTitle();
        if(productDetail!=null)
        {
            presenter.findProductDetail(userId, productDetail.getProductInfo().getProductId());
        }
    }

    private void initView() {
        productDescEditView = findViewById(R.id.goods_destribe_edit_view);
        tv_num_tv = (TextView) findViewById(R.id.tv_num_content);
        productDescEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 500) {
                    tv_num_tv.setText(s.length() + "/500");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productNameEditView=findViewById(R.id.goods_name_edit_view);
        productPriceEditView=findViewById(R.id.goods_price_edit_view);
        productDiscountEditView=findViewById(R.id.goods_discount_edit_view);
        salePriceEditView=findViewById(R.id.goods_discount_price_edit_view);
        productDiscountEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String priceStr=productPriceEditView.getText().toString();
                if(!StringUtils.isEmpty(priceStr))
                {
                    float priceFloat=Float.parseFloat(priceStr);
                    String discountStr=productDiscountEditView.getText().toString();
                    if(!StringUtils.isEmpty(discountStr)){
                        double discountDouble=Double.parseDouble(discountStr);
                        if(discountDouble>=10){
                            Utils.showToastShortTime("输入的折扣不能大于10");
                            productDiscountEditView.setText("");
                            salePriceEditView.setText("");
                        }else{
                            salePriceEditView.setText(new BigDecimal(String .format("%.2f",(priceFloat*discountDouble/10))).stripTrailingZeros().toPlainString());
                        }
                    }else{
                        salePriceEditView.setText("");
                    }

                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        productPriceEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String priceStr=productPriceEditView.getText().toString();
                if(!StringUtils.isEmpty(priceStr))
                {
                    float priceFloat=Float.parseFloat(priceStr);
                    String discountStr=productDiscountEditView.getText().toString();
                    if(!StringUtils.isEmpty(discountStr)){
                        Double discountDouble=Double.parseDouble(discountStr);
                        salePriceEditView.setText(new BigDecimal(String .format("%.2f",(priceFloat*discountDouble/10))).stripTrailingZeros().toPlainString());
                    }

                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        productRemainTotalEditView=findViewById(R.id.goods_stock_edit_view);
        okBtn=findViewById(R.id.ok_btn_view);
        if(productDetail==null){
            initGoods(productDetail);
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加商品
                if(getInputGoodsInfo(1)==0){
                    if(productDetail!=null){
                        if(productDetail.getProductInfo().getStatus()==1){
                            status=0;
                        }
                        presenter.updateProductInfo(userId,storeId,productDetail.getProductInfo().getProductId(),productName,productDesc,productPrice,productDiscount,salePrice,Integer.parseInt(productRemainTotal),status);
                    }else{
                        presenter.updateProductInfo(userId,storeId,0,productName,productDesc,productPrice,productDiscount,salePrice,Integer.parseInt(productRemainTotal),status);
                    }
                }
            }
        });
    }

    public void initGoods(ProductDetail productDetail){
        //初始化商品信息
        //商品图片
        imageAlbumFragment = (ImageAlbumFragment) getSupportFragmentManager().findFragmentById(R.id.imag_fragment);
        if (imageAlbumFragment == null) {
            imageAlbumFragment = new ImageAlbumFragment();
        }

        imageAlbumFragment.setOnImageNumChange(new ImageAlbumFragment.OnImageNumChange() {
            @Override
            public void OnImageNum(int num) {
            }
        });
        //商品详情图片
        imageAlbumDetailFragment= (ImageAlbumFragment) getSupportFragmentManager().findFragmentById(R.id.imag_detail_fragment);
        if (imageAlbumDetailFragment == null) {
            imageAlbumDetailFragment = new ImageAlbumFragment();
        }
        imageAlbumDetailFragment.setMaxImageCount(5);
        imageAlbumDetailFragment.setOnImageNumChange(new ImageAlbumFragment.OnImageNumChange() {
            @Override
            public void OnImageNum(int num) {
            }
        });
        if(productDetail!=null){
            ProductInfo productInfo=productDetail.getProductInfo();
            productNameEditView.setText(productInfo.getProductName());
            productDescEditView.setText(productInfo.getProductDesc());
            int length=productDescEditView.getText().toString().length();
            tv_num_tv.setText(length + "/500");
            productPriceEditView.setText(productInfo.getProductPrice()+"");
            productDiscountEditView.setText(productInfo.getProductDiscount());
            salePriceEditView.setText(productInfo.getSalePrice()+"");
            productRemainTotalEditView.setText(productInfo.getProductRemainTotal()+"");
            List<ProductImage> productImageList=productDetail.getImageList();
            List<Image> imageList = new ArrayList<Image>();
            if(productImageList!=null){
                for (int i=0;i<productImageList.size();i++){
                    ProductImage productImage=productImageList.get(i);
                    Image image=new Image();
                    image.setImageUrl(productImage.getImageUrl());
                    image.setImageId(productImage.getImageId());
                    image.setThumbnailUrl(productImage.getThumbnailUrl());
                    imageList.add(image);
                }
                imageAlbumFragment.setType(22);
                imageAlbumFragment.setImageList(imageList);
            }
            //商品详情图片
            List<ProductImage> productDetailImageList=productDetail.getDetaiLimageList();
            List<Image> detailImageList = new ArrayList<Image>();
            if(productDetailImageList!=null){
                for (int i=0;i<productDetailImageList.size();i++){
                    ProductImage productImage=productDetailImageList.get(i);
                    Image image=new Image();
                    image.setImageUrl(productImage.getImageUrl());
                    image.setImageId(productImage.getImageId());
                    image.setThumbnailUrl(productImage.getThumbnailUrl());
                    detailImageList.add(image);
                }
                imageAlbumDetailFragment.setType(24);
                imageAlbumDetailFragment.setImageList(detailImageList);
            }
            if(productInfo.getStatus()==1){
                //下架的商品再次上架
                okBtn.setText("商品上架");
            }else{
                okBtn.setText("保存");
            }
        }else{
            okBtn.setText("商品上架");
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), imageAlbumFragment, R.id.imag_fragment);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), imageAlbumDetailFragment, R.id.imag_detail_fragment);
    }


    private void setTitle() {
        titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        if(productDetail!=null) {
            titleFragment.setTitle(R.string.title_edit_goods);
        }else{
            titleFragment.setTitle(R.string.title_add_goods);
        }

        titleFragment.setLeftViewTitle(R.mipmap.title_back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                if(productDetail==null) {//只针对于添加商品
                    if (getInputGoodsInfo(2) == 2 || getInputGoodsInfo(2) == 0) {
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("添加商品未完成", "退出后编辑的内容将不被保存", "退出", "继续编辑");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                                finish();
                            }

                            @Override
                            public void onRightClick() {

                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    } else {
                        finish();
                    }
                }else {
                    finish();
                }
            }
        });
    }

    public int getInputGoodsInfo(int type){ //返回值1:都没填写 2：部分未填写 0：全部填写  type:1点击保存2：点击返回
        productName=productNameEditView.getText().toString();
        productDesc=productDescEditView.getText().toString();
        productPrice=productPriceEditView.getText().toString();
        salePrice=salePriceEditView.getText().toString();
        productDiscount=productDiscountEditView.getText().toString();
        if (StringUtils.isEmpty(productDiscount)) {
            salePrice=productPrice;
        }
        productRemainTotal=productRemainTotalEditView.getText().toString();
        if(type==2){
            if(StringUtils.isEmpty(productName)&&StringUtils.isEmpty(productDesc)&&StringUtils.isEmpty(productPrice)&&StringUtils.isEmpty(productRemainTotal)&&imageAlbumFragment.getNewImageList().size()<=1){
                return 1;
            }
            if (StringUtils.isEmpty(productName)||StringUtils.isEmpty(productDesc)||StringUtils.isEmpty(productPrice)||StringUtils.isEmpty(productRemainTotal)||imageAlbumFragment.getNewImageList().size() <=1) {
                return 2;
            }else{
                return 0;
            }
        }else {
            if (imageAlbumFragment.getNewImageList().size() <=1) {
                Utils.showToastShortTime("请添加商品图片");
                return 2;
            }

            if (StringUtils.isEmpty(productName)) {
                Utils.showToastShortTime("请添加商品名称");
                return 2;
            }

            if (StringUtils.isEmpty(productDesc)) {
                Utils.showToastShortTime("请添加商品描述");
                return 2;
            }

            if (StringUtils.isEmpty(productPrice)) {
                Utils.showToastShortTime("请输入商品价格");
                return 2;
            }else
            {
                Double productPriceDouble=Double.parseDouble(productPrice);
                if(productPriceDouble<=0){
                    Utils.showToastShortTime("商品价格不能为0元");
                    return 2;
                }
            }

//            if (!StringUtils.isEmpty(salePrice)) {
//                Double salePriceDouble=Double.parseDouble(salePrice);
//                if(salePriceDouble<=0){
//                    Utils.showToastShortTime("折扣价格不能为0");
//                    return 2;
//                }
//           }
           if (!StringUtils.isEmpty(productDiscount)) {
               Double productDiscountDouble=Double.parseDouble(productDiscount);
               if(productDiscountDouble<=0){
                   Utils.showToastShortTime("折扣不能为0");
                   return 2;
               }
           }
           if (StringUtils.isEmpty(productRemainTotal)) {
                Utils.showToastShortTime("请输入商品货存数量");
                return 2;
            }else{
               Double productRemainTotalDouble=Double.parseDouble(productRemainTotal);
               if(productRemainTotalDouble<=0){
                   Utils.showToastShortTime("商品货存数量不能为0");
                   return 2;
               }
           }
            return 0;
        }

    }


    @Override
    public void updateView(String apiName, Object object) {

        if (isFinishing()) {
            return;
        }
        if (object instanceof Exception) {
            Exception e = (Exception) object;
            String text = e.getMessage();
            Utils.showToastShortTime(text);
            return;
        }
        if (apiName.equals(InterfaceUrl.URL_UPDATEPRODUCTINFO)) {
            if (object instanceof UpdateProductInfoResponse) {
                final UpdateProductInfoResponse updateProductInfoResponse = (UpdateProductInfoResponse) object;
                int code = updateProductInfoResponse.getCode();
                if (code == 200) {
                    //上传商品图片
                    List<ProductImage> productImageList=new ArrayList<ProductImage>();//更新本地数据需要的图片列表
                    if (imageAlbumFragment.getNewImageList().size() > 1) {
                        for (int i = 0; i < imageAlbumFragment.getNewImageList().size(); i++) {
                            Image image = imageAlbumFragment.getNewImageList().get(i);
                            // "add"图片
                            if (image.getImageUrl().contains("add")) {
                                continue;
                            }
                            productImageList.add(Utils.imageToProductImage(image));
                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                            //网络图片
                            String imageUrl = null;
                            if (image.getImageId() > 0) {
                                imageUrl = null;
                            } else {
                                imageUrl = image.getImageUrl();
                            }
                            imagePresenter.uploadImages(updateProductInfoResponse.getProductId(), 22, userId, TimeUtils.date2String(new Date()), imageUrl, image.getImageId(), i + 1, imageAlbumFragment.getNewImageList().size() - 1);
                        }
                    }
                    //上传商品详情图片
                    if (imageAlbumDetailFragment.getNewImageList().size() > 1) {
                        for (int i = 0; i < imageAlbumDetailFragment.getNewImageList().size(); i++) {
                            Image image = imageAlbumDetailFragment.getNewImageList().get(i);
                            // "add"图片
                            if (image.getImageUrl().contains("add")) {
                                continue;
                            }
                            long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                            //网络图片
                            String imageUrl = null;
                            if (image.getImageId() > 0) {
                                imageUrl = null;
                            } else {
                                imageUrl = image.getImageUrl();
                            }
                            imagePresenter.uploadImages(updateProductInfoResponse.getProductId(), 24, userId, TimeUtils.date2String(new Date()), imageUrl, image.getImageId(), i + 1, imageAlbumDetailFragment.getNewImageList().size() - 1);
                        }
                    }
                    if(productDetail!=null){
                        EventBus.getDefault().post(new UpdateGoodsListEvent(1,productName,productDesc,new BigDecimal(productPrice),productDiscount,new BigDecimal(salePrice),productRemainTotal,productImageList));
                        Utils.showToastShortTime(updateProductInfoResponse.getMsg());
                        finish();
                    }else{
                        EventBus.getDefault().post(new UpdateGoodsListEvent(2,updateProductInfoResponse.getProductId(),productName,productDesc,productDiscount,productRemainTotal,new BigDecimal(productPrice),new BigDecimal(salePrice),productImageList));
                        ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                        dialog.setText("", "商品已上架成功，是否继续上传新的商品？", "下次再说", "继续上传");
                        dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                            @Override
                            public void onLeftClick() {
                                //取消
                                finish();
                            }

                            @Override
                            public void onRightClick() {
                                //清空界面
                                productNameEditView.setText("");
                                productDescEditView.setText("");
                                productPriceEditView.setText("");
                                productDiscountEditView.setText("");
                                salePriceEditView.setText("");
                                productRemainTotalEditView.setText("");
                                imageAlbumFragment.clearList();
                            }
                        });
                        dialog.show(getSupportFragmentManager());
                    }

                } else {
                    Utils.showToastShortTime(updateProductInfoResponse.getMsg());
                }
            }
        }else if (apiName.equals(InterfaceUrl.URL_FINDPRODUCTDETAIL)) {
            if (object instanceof FindProductDetailResponse) {
                FindProductDetailResponse findProductDetailResponse = (FindProductDetailResponse) object;
                if (findProductDetailResponse.getCode() == 200) {
                    productDetail = findProductDetailResponse.getProductDetail();
                    if (productDetail != null) {
                        initGoods(productDetail);
                    }
                } else {
                    Utils.showToastShortTime(findProductDetailResponse.getMsg());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(productDetail==null) {//只针对于添加商品
                if (getInputGoodsInfo(2) == 2 || getInputGoodsInfo(2) == 0) {
                    ActiveLaunchSuccessTipDialog dialog = new ActiveLaunchSuccessTipDialog();
                    dialog.setText("添加商品未完成", "退出后编辑的内容将不被保存", "退出", "继续编辑");
                    dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            //取消
                            finish();
                        }

                        @Override
                        public void onRightClick() {

                        }
                    });
                    dialog.show(getSupportFragmentManager());

                } else {
                    finish();
                }
            }else {
                finish();
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;

        if (imagePresenter != null) {
            imagePresenter.onDestroy();
        }
        imagePresenter = null;

        if (addImgLayout != null) {
            addImgLayout.removeAllViews();
        }
        addImgLayout = null;



        if (titleFragment != null) {
            titleFragment = null;
        }



    }


}
