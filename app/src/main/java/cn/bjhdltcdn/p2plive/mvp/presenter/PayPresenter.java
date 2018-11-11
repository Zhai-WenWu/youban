package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindActiveDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetServiceInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinActiveResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveAlipayOrderResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.FindActiveDetailContract;
import cn.bjhdltcdn.p2plive.mvp.contract.PayContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class PayPresenter implements PayContract.Presenter {

    private BaseView mView ;

    public PayPresenter(BaseView mView) {
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
    public void getServiceInfo() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSERVICEINFO,tag, null, new JsonCallback<GetServiceInfoResponse>(GetServiceInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetServiceInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSERVICEINFO,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSERVICEINFO,response.body());
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
    public void saveAlipayOrder(String serviceId, long userId, int reqType) {
        Map map = new LinkedHashMap(1);
        map.put("serviceId",serviceId);
        map.put("userId",userId);
        map.put("reqType",reqType);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEALIPAYORDER,tag, map, new JsonCallback<SaveAlipayOrderResponse>(SaveAlipayOrderResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SaveAlipayOrderResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEALIPAYORDER,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEALIPAYORDER,response.body());
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
    public void saveWeixinOrder(String serviceId, long userId, int reqType,String ip) {
        Map map = new LinkedHashMap(1);
        map.put("serviceId",serviceId);
        map.put("userId",userId);
        map.put("reqType",reqType);
        map.put("ip",ip);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEWEIXINORDER,tag, map, new JsonCallback<SaveAlipayOrderResponse>(SaveAlipayOrderResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SaveAlipayOrderResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEWEIXINORDER,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEWEIXINORDER,response.body());
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
