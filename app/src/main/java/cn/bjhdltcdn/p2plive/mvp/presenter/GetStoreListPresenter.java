package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindClertApplyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindClertDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindExchangeInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindProductDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindStoreDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserApplyStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserDefaultAddressResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetExchangeListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetH5ReceiptAddressListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRecommendMerchantListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrderEvaluateListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrderListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetSalesRecordListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetShopLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetStoreProductListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeIsCreateStoreResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeUserReceiptResponse;
import cn.bjhdltcdn.p2plive.httpresponse.RefuseRefundResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveAlipayProductOrderResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveH5ReceiptAddressResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateCreateStoreResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateProductInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetClertListResponse;
import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.contract.GetStoreListContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * 店铺列表控制器
 */

public class GetStoreListPresenter implements GetStoreListContract.Presenter {

    private BaseView mView;

    public GetStoreListPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void cancelTag(String tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    @Override
    public void onDestroy() {

        if (mView != null) {
            mView = null;
        }
    }


    @Override
    public void findStoreKeywordList(long userId, String content, long distanceSort, List<Long> labelIds, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.DISTANCE_SORT, distanceSort);
        map.put(Constants.Fields.LABEL_IDS, labelIds);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDSTOREKEYWORDLIST, tag, map, new JsonCallback<GetStoreListResponse>(GetStoreListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSTOREKEYWORDLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSTOREKEYWORDLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findProductInfoKeywordList(long userId, String content, long distanceSort, List<Long> labelIds, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.DISTANCE_SORT, distanceSort);
        map.put(Constants.Fields.LABEL_IDS, labelIds);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDPRODUCTINFOKEYWORDLIST, tag, map, new JsonCallback<GetStoreListResponse>(GetStoreListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPRODUCTINFOKEYWORDLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPRODUCTINFOKEYWORDLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStoreList(long userId, String content, long distanceSort, int merchantSort, long labelId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.DISTANCE_SORT, distanceSort);
        map.put(Constants.Fields.MERCHANT_SORT, merchantSort);
        map.put(Constants.Fields.LABEL_ID, labelId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSTORELIST, tag, map, new JsonCallback<GetStoreListResponse>(GetStoreListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTORELIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTORELIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getRecommendMerchantList(int type, long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST, tag, map, new JsonCallback<GetRecommendMerchantListResponse>(GetRecommendMerchantListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETRECOMMENDMERCHANTLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStoreLabelList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSTORELABELLIST, tag, map, new JsonCallback<GetStoreLabelListResponse>(GetStoreLabelListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTORELABELLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTORELABELLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void judgeCreateStoreAuth(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JUDGECREATESTOREAUTH, tag, map, new JsonCallback<JudgeCreateStoreAuthResponse>(JudgeCreateStoreAuthResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGECREATESTOREAUTH, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGECREATESTOREAUTH, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void applyCreateStore(long userId, String cardFrontImg, String cardBackImg) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_APPLYCREATESTORE, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_APPLYCREATESTORE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_APPLYCREATESTORE, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCreateStore(long userId, StoreInfo storeInfo) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STOREINFO, storeInfo);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATECREATESTORE, tag, map, new JsonCallback<UpdateCreateStoreResponse>(UpdateCreateStoreResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECREATESTORE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATECREATESTORE, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStoreVideo(long userId, long storeId, String videoUrl, String videoImageUrl) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put("videoUrl", videoUrl);
        map.put("videoImageUrl", videoImageUrl);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATESTOREVIDEO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATESTOREVIDEO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATESTOREVIDEO, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProductInfo(long userId, long storeId, long productId, String productName, String productDesc, String productPrice, String productDiscount, String salePrice, int productRemainTotal, int status) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.PRODUCT_ID, productId);
        map.put(Constants.Fields.PRODUCT_NAME, productName);
        map.put(Constants.Fields.PRODUCT_DESC, productDesc);
        map.put(Constants.Fields.PRODUCT_PRICE, productPrice);
        map.put(Constants.Fields.PRODUCT_DISCOUNT, productDiscount);
        map.put(Constants.Fields.SALE_PRICE, salePrice);
        map.put(Constants.Fields.PRODUCT_REMAIN_TOTAL, productRemainTotal);
        map.put(Constants.Fields.STATUS, status);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEPRODUCTINFO, tag, map, new JsonCallback<UpdateProductInfoResponse>(UpdateProductInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEPRODUCTINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEPRODUCTINFO, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findStoreDetail(long userId, long storeId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDSTOREDETAIL, tag, map, new JsonCallback<FindStoreDetailResponse>(FindStoreDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSTOREDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDSTOREDETAIL, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getStoreProductList(long userId, long storeId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSTOREPRODUCTLIST, tag, map, new JsonCallback<GetStoreProductListResponse>(GetStoreProductListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTOREPRODUCTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSTOREPRODUCTLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findProductDetail(long userId, long productId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PRODUCT_ID, productId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDPRODUCTDETAIL, tag, map, new JsonCallback<FindProductDetailResponse>(FindProductDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPRODUCTDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPRODUCTDETAIL, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getOrderList(long userId, long storeId, long labelId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.LABEL_ID, labelId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETORDERLIST, tag, map, new JsonCallback<GetOrderListResponse>(GetOrderListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETORDERLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETORDERLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getOrderEvaluateList(long userId, long storeId, int type, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETORDEREVALUATELIST, tag, map, new JsonCallback<GetOrderEvaluateListResponse>(GetOrderEvaluateListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETORDEREVALUATELIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETORDEREVALUATELIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSalesRecordtList(long userId, long storeId, int type, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSALESRECORDTLIST, tag, map, new JsonCallback<GetSalesRecordListResponse>(GetSalesRecordListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSALESRECORDTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSALESRECORDTLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProductStatus(long userId, long storeId, long productId, int isHot, int isNew, int status) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.PRODUCT_ID, productId);
        map.put(Constants.Fields.IS_HOT, isHot);
        map.put(Constants.Fields.IS_NEW, isNew);
        map.put(Constants.Fields.STATUS, status);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEPRODUCTSTATUS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEPRODUCTSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEPRODUCTSTATUS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fireClerk(long userId, long toUserId, long storeId) {
        {
            {
                Map map = new LinkedHashMap(1);
                map.put(Constants.Fields.USER_ID, userId);
                map.put(Constants.Fields.TO_USER_ID, toUserId);
                map.put(Constants.Fields.STORE_ID, storeId);
                String tag;
                if (mView != null) {
                    tag = mView.getClass().getSimpleName();
                } else {
                    return;
                }
                try {
                    ApiData.getInstance().postData(InterfaceUrl.URL_FIRECLERK, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                            if (mView != null) {
                                mView.showLoading();
                            }
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_FIRECLERK, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_FIRECLERK, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if (mView != null) {
                                mView.hideLoading();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void findClertDetail(long userId, long toUserId, long storeId, int pageSize, int pageNumber, int type) {
        {
            {
                Map map = new LinkedHashMap(1);
                map.put(Constants.Fields.USER_ID, userId);
                map.put(Constants.Fields.TO_USER_ID, toUserId);
                map.put(Constants.Fields.STORE_ID, storeId);
                map.put(Constants.Fields.PAGE_SIZE, pageSize);
                map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
                map.put(Constants.Fields.TYPE, type);
                String tag;
                if (mView != null) {
                    tag = mView.getClass().getSimpleName();
                } else {
                    return;
                }
                try {
                    ApiData.getInstance().postData(InterfaceUrl.URL_FINDCLERTDETAIL, tag, map, new JsonCallback<FindClertDetailResponse>(FindClertDetailResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                            if (mView != null) {
                                mView.showLoading();
                            }
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_FINDCLERTDETAIL, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_FINDCLERTDETAIL, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            if (mView != null) {
                                mView.hideLoading();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void findUserDefaultAddress(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDUSERDEFAULTADDRESS, tag, map, new JsonCallback<FindUserDefaultAddressResponse>(FindUserDefaultAddressResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERDEFAULTADDRESS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERDEFAULTADDRESS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveH5ReceiptAddress(long userId, AddressInfo addressInfo) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ADDRESSINFO, addressInfo);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEH5RECEIPTADDRESS, tag, map, new JsonCallback<SaveH5ReceiptAddressResponse>(SaveH5ReceiptAddressResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEH5RECEIPTADDRESS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEH5RECEIPTADDRESS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateH5ReceiptAddress(long userId, AddressInfo addressInfo) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ADDRESSINFO, addressInfo);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEH5RECEIPTADDRESS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEH5RECEIPTADDRESS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEH5RECEIPTADDRESS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateH5ReceiptAddress(long userId, long addressId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ADDRESS_ID, addressId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELH5RECEIPTADDRESS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELH5RECEIPTADDRESS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELH5RECEIPTADDRESS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getH5ReceiptAddressList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETH5RECEIPTADDRESSLIST, tag, map, new JsonCallback<GetH5ReceiptAddressListResponse>(GetH5ReceiptAddressListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETH5RECEIPTADDRESSLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETH5RECEIPTADDRESSLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findExchangeInfo(long userId, int hintType) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.HINT_TYPE, hintType);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDEXCHANGEINFO, tag, map, new JsonCallback<FindExchangeInfoResponse>(FindExchangeInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDEXCHANGEINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDEXCHANGEINFO, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveExchangeInfo(long userId, int type, String exchangeAmount) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.EXCHANGE_AMOUNT, exchangeAmount);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEEXCHANGEINFO, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEEXCHANGEINFO, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEEXCHANGEINFO, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getExchangeList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETEXCHANGELIST, tag, map, new JsonCallback<GetExchangeListResponse>(GetExchangeListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETEXCHANGELIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETEXCHANGELIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAlipayProductOrder(long productId, int productNum, long userId, long addressId, int reqType, long distributeMode, long storeId, List<ProductInfo> productList,String postAge, String remark) {
        Map map = new LinkedHashMap(1);
        map.put("productId", productId);
        map.put("productNum", productNum);
        map.put(Constants.Fields.USER_ID, userId);
        map.put("addressId", addressId);
        map.put("reqType", reqType);
        map.put("distributeMode", distributeMode);
        map.put("storeId", storeId);
        map.put("productList", productList);
        if(StringUtils.isEmpty(postAge)){
            map.put("postAge", 0);
        }else{
            map.put("postAge", postAge);
        }
        map.put("remark", remark);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEALIPAYPRODUCTORDER, tag, map, new JsonCallback<SaveAlipayProductOrderResponse>(SaveAlipayProductOrderResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEALIPAYPRODUCTORDER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEALIPAYPRODUCTORDER, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveWeixinProductOrder(long productId, int productNum, long userId, long addressId, int reqType, String ip, int distributeMode, String remark) {
        Map map = new LinkedHashMap(1);
        map.put("productId", productId);
        map.put("productNum", productNum);
        map.put(Constants.Fields.USER_ID, userId);
        map.put("addressId", addressId);
        map.put("reqType", reqType);
        map.put("ip", ip);
        map.put("distributeMode", distributeMode);
        map.put("remark", remark);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEWEIXINPRODUCTORDER, tag, map, new JsonCallback<SaveAlipayProductOrderResponse>(SaveAlipayProductOrderResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEWEIXINPRODUCTORDER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEWEIXINPRODUCTORDER, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sellerReceipt(long userId, long orderId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ORDER_ID, orderId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SELLERRECEIPT, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SELLERRECEIPT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SELLERRECEIPT, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void confirmRefund(long userId, long orderId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ORDER_ID, orderId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CONFIRMREFUND, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CONFIRMREFUND, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CONFIRMREFUND, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveShopUserPayOff(long userId, long toUserId, String amount) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.AMOUNT, amount);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVESHOPUSERPAYOFF, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESHOPUSERPAYOFF, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESHOPUSERPAYOFF, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findUserApplyStatus(long userId, long storeId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDUSERAPPLYSTATUS, tag, map, new JsonCallback<FindUserApplyStatusResponse>(FindUserApplyStatusResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERAPPLYSTATUS, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDUSERAPPLYSTATUS, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void judgeUserReceipt(long userId, long orderId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ORDER_ID, orderId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JUDGEUSERRECEIPT, tag, map, new JsonCallback<JudgeUserReceiptResponse>(JudgeUserReceiptResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGEUSERRECEIPT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGEUSERRECEIPT, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refuseRefund(long orderId, int isClert) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.IS_CLERT, isClert);
        map.put(Constants.Fields.ORDER_ID, orderId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_REFUSEREFUND, tag, map, new JsonCallback<RefuseRefundResponse>(RefuseRefundResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REFUSEREFUND, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_REFUSEREFUND, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void judgeIsCreateStore(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JUDGEISCREATESTORE, tag, map, new JsonCallback<JudgeIsCreateStoreResponse>(JudgeIsCreateStoreResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGEISCREATESTORE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JUDGEISCREATESTORE, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void applyClert(long userId, long storeId, String phoneNumber, String addr, String selfDesc, String cardFrontImg, String cardBackImg) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.STORE_ID, storeId);
        map.put(Constants.Fields.PHONE_NUMBER, phoneNumber);
        map.put(Constants.Fields.ADDRESS, addr);
        map.put(Constants.Fields.SELFDESC, selfDesc);
        map.put(Constants.Fields.CARD_FRONT_IMG, cardFrontImg);
        map.put(Constants.Fields.CARD_BACK_IMG, cardBackImg);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_APPLYCLERT, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_APPLYCLERT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_APPLYCLERT, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void findClertApply(long userId, long applyId) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.APPLY_ID, applyId);
            String tag;
            if (mView != null) {
                tag = mView.getClass().getSimpleName();
            } else {
                return;
            }
            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_FINDCLERTAPPLY, tag, map, new JsonCallback<FindClertApplyResponse>(FindClertApplyResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                        if (mView != null) {
                            mView.showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_FINDCLERTAPPLY, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_FINDCLERTAPPLY, response.getException());
                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void authClertApply(long userId, long applyId, int status) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.APPLY_ID, applyId);
            map.put(Constants.Fields.STATUS, status);
            String tag;
            if (mView != null) {
                tag = mView.getClass().getSimpleName();
            } else {
                return;
            }
            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_AUTHCLERTAPPLY, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                        if (mView != null) {
                            mView.showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_AUTHCLERTAPPLY, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_AUTHCLERTAPPLY, response.getException());
                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void getClertList(long userId, long storeId, int pageSize, int pageNumber) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.STORE_ID, storeId);
            map.put(Constants.Fields.PAGE_SIZE, pageSize);
            map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
            String tag;
            if (mView != null) {
                tag = mView.getClass().getSimpleName();
            } else {
                return;
            }
            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_GETCLERTLIST, tag, map, new JsonCallback<GetClertListResponse>(GetClertListResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                        if (mView != null) {
                            mView.showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETCLERTLIST, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETCLERTLIST, response.getException());
                        }


                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        if (mView != null) {
                            mView.hideLoading();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void getShopLabelList(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSHOPLABELLIST, tag, map, new JsonCallback<GetShopLabelListResponse>(GetShopLabelListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSHOPLABELLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSHOPLABELLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUserStoreLabel(long userId, FirstLabelInfo firstLabelInfo) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.FIRST_LABELINFO, firstLabelInfo);
        String tag;
        if (mView != null) {
            tag = mView.getClass().getSimpleName();
        } else {
            return;
        }
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEUSERSTORELABEL, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEUSERSTORELABEL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEUSERSTORELABEL, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
