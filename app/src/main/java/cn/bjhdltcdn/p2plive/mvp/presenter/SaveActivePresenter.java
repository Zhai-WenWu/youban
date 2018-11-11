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
import cn.bjhdltcdn.p2plive.httpresponse.GetActiveTypeListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveActiveResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SendActivityInviationResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.SaveActiveContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class SaveActivePresenter implements SaveActiveContract.Presenter {

    private BaseView mView ;

    public SaveActivePresenter(BaseView mView) {
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
    public void saveActive(long userId,long organId, ActivityInfo activeInfo) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        map.put("organId",organId);
        map.put("activeInfo",activeInfo);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEACTIVE,tag, map, new JsonCallback<SaveActiveResponse>(SaveActiveResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SaveActiveResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEACTIVE,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEACTIVE,response.body());
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
    public void updateActive(long userId, ActivityInfo activeInfo) {
        Map map = new LinkedHashMap(1);
        map.put("userId",userId);
        map.put("activeInfo",activeInfo);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEACTIVE,tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<NoParameterResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEACTIVE,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEACTIVE,response.body());
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
    public void getActiveTypeList() {
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETACTIVETYPELIST,tag, null, new JsonCallback<GetActiveTypeListResponse>(GetActiveTypeListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetActiveTypeListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETACTIVETYPELIST,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETACTIVETYPELIST,response.body());
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
    public void deleteImage(long imageId,int type) {
        Map map = new LinkedHashMap(1);
        map.put("imageId",imageId);
        map.put(Constants.Fields.TYPE,type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEIMAGE,tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_DELETEIMAGE,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEIMAGE,response.body());
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
    public void sendActivityInvitation(long userId,Long activityId, List<Long> hobbyIds, int sexLimit, int sendNumber, int type, int method,List<Long> userIds,int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID,userId);
        map.put(Constants.Fields.ACTIVITY_ID,activityId);
        map.put(Constants.Fields.HOBBY_IDS,hobbyIds);
        map.put(Constants.Fields.SEX_LIMIT,sexLimit);
        map.put(Constants.Fields.SEND_NUMBER,sendNumber);
        map.put(Constants.Fields.TYPE,type);
        map.put(Constants.Fields.METHOD,method);
        map.put(Constants.Fields.USER_IDS,userIds);
        map.put(Constants.Fields.PAGE_SIZE,pageSize);
        map.put(Constants.Fields.PAGE_NUMBER,pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SENDACTIVITYINVITATION,tag, map, new JsonCallback<SendActivityInviationResponse>(SendActivityInviationResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<SendActivityInviationResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SENDACTIVITYINVITATION,response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SENDACTIVITYINVITATION,response.body());
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
