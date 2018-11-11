package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.ApplyRefundResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindOrderDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyOrderListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRefundReasonListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.MyPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveOrderEvaluateResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.OrderContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHAI on 2018/2/27.
 */

public class OrderPresenter implements OrderContract.Presenter {
    private BaseView mView;

    public OrderPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void cancelTag(String tag) {

    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void getMyOrderList(long userId, long labelId, int pageSize, int pageNumber) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.LABELID, labelId);
            map.put(Constants.Fields.PAGE_SIZE, pageSize);
            map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_GETMYORDERLIST, tag, map, new JsonCallback<GetMyOrderListResponse>(GetMyOrderListResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_GETMYORDERLIST, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETMYORDERLIST, response.getException());
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
    public void updateOrderStatus(long userId, long orderId, String receiptCode, int evalScore, int isClert) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.ORDER_ID, orderId);
            map.put(Constants.Fields.RECEIPT_CODE, receiptCode);
            map.put(Constants.Fields.EVALSCORE, evalScore);
            map.put(Constants.Fields.IS_CLERT, isClert);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEORDERSTATUS, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_UPDATEORDERSTATUS, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_UPDATEORDERSTATUS, response.getException());
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
    public void findOrderDetail(long userId, long orderId) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.ORDER_ID, orderId);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_FINDORDERDETAIL, tag, map, new JsonCallback<FindOrderDetailResponse>(FindOrderDetailResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_FINDORDERDETAIL, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_FINDORDERDETAIL, response.getException());
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
    public void getTrailReportList(long userId, int pageSize, int pageNumber) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_GETTRAILREPORTLIST, tag, map, new JsonCallback<MyPostListResponse>(MyPostListResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_GETTRAILREPORTLIST, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETTRAILREPORTLIST, response.getException());
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
    public void applyRefund(long userId, long orderId, long reasonId, String remark) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.ORDER_ID, orderId);
            map.put(Constants.Fields.REASONI_D, reasonId);
            map.put(Constants.Fields.REMARK, remark);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_APPLYREFUND, tag, map, new JsonCallback<ApplyRefundResponse>(ApplyRefundResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_APPLYREFUND, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_APPLYREFUND, response.getException());
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
    public void saveOrderEvaluate(long userId, long orderId, long productId, long toUserId, long fromUserId, long parentId, long commentParentId, int contentType, int evalScore, String content, int commentType, String videoUrl, String videoImageUrl) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.TO_USER_ID, toUserId);
            map.put(Constants.Fields.FROM_USER_ID, fromUserId);
            map.put(Constants.Fields.ORDER_ID, orderId);
            map.put(Constants.Fields.PRODUCT_ID, productId);
            map.put(Constants.Fields.PARENT_ID, parentId);
            map.put(Constants.Fields.COMMENT_PARENT_ID, commentParentId);
            map.put(Constants.Fields.EVALSCORE, evalScore);
            map.put(Constants.Fields.CONTENT, content);
            map.put(Constants.Fields.CONTENT_TYPE, contentType);
            map.put(Constants.Fields.COMMENT_TYPE, commentType);
            map.put(Constants.Fields.VIDEO_URL, videoUrl);
            map.put(Constants.Fields.VIDEO_IMAGE_URL, videoImageUrl);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_SAVEORDEREVALUATE, tag, map, new JsonCallback<SaveOrderEvaluateResponse>(SaveOrderEvaluateResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_SAVEORDEREVALUATE, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_SAVEORDEREVALUATE, response.getException());
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
    public void getRefundReasonList(long userId) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_GETREFUNDREASONLIST, tag, map, new JsonCallback<GetRefundReasonListResponse>(GetRefundReasonListResponse.class) {

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
                            mView.updateView(InterfaceUrl.URL_GETREFUNDREASONLIST, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETREFUNDREASONLIST, response.getException());
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
