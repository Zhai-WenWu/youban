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
import cn.bjhdltcdn.p2plive.httpresponse.JoinActiveResponse;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.FindActiveDetailContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class FindActiveDetailPresenter implements FindActiveDetailContract.Presenter {

    private BaseView mView;

    public FindActiveDetailPresenter(BaseView mView) {
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
    public void findActiveDetail(long userId, long activityId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("activityId", activityId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDACTIVEDETAIL, tag, map, new JsonCallback<FindActiveDetailResponse>(FindActiveDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<FindActiveDetailResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDACTIVEDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDACTIVEDETAIL, response.body());
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
    public void joinActive(long userId, long activityId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("activityId", activityId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JOINACTIVE, tag, map, new JsonCallback<JoinActiveResponse>(JoinActiveResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<JoinActiveResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JOINACTIVE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JOINACTIVE, response.body());
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
    public void signOutActive(long userId, long activityId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("activityId", activityId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SIGNOUTACTIVE, tag, map, new JsonCallback<JoinActiveResponse>(JoinActiveResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<JoinActiveResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SIGNOUTACTIVE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SIGNOUTACTIVE, response.body());
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
    public void deleteActive(long userId, long activityId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("activityId", activityId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEACTIVE, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<BaseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEACTIVE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEACTIVE, response.body());
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
