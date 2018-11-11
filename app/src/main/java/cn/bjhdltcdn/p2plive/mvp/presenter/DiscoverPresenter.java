package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.GetDiscoverListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPlayListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetRoomListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetSecondLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SavePlayInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetAllPlayListResponse;
import cn.bjhdltcdn.p2plive.model.SaveLaunchPlayResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.DiscoverContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHUDI on 2017/11/23.
 */

public class DiscoverPresenter implements DiscoverContract.Presenter {
    private BaseView mView;

    public DiscoverPresenter(BaseView mView) {
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
    public void getDiscoverList(long userId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETDISCOVERLIST, tag, map, new JsonCallback<GetDiscoverListResponse>(GetDiscoverListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetDiscoverListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETDISCOVERLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETDISCOVERLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mView.hideLoading();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getLabelList(int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETLABELLIST, tag, map, new JsonCallback<GetLabelListResponse>(GetLabelListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetLabelListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLABELLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETLABELLIST, response.getException());
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
    public void getPKList(long userId, long launchId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("launchId", launchId);
        map.put("pageSize", pageSize);
        map.put("pageNumber", pageNumber);
        String tag = mView.getClass().getSimpleName();
        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SGETPLAYLIST, tag, map, new JsonCallback<GetPlayListResponse>(GetPlayListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<GetPlayListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SGETPLAYLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SGETPLAYLIST, response.getException());
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
    public void getSavePlayInfo(long userId, long launchId, String playTitle, String videoUrl, String videoImageUrl) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("launchId", launchId);
        map.put("playTitle", playTitle);
        map.put("videoUrl", videoUrl);
        map.put("videoImageUrl", videoImageUrl);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVEPLAYINFO, tag, map, new JsonCallback<SavePlayInfoResponse>(SavePlayInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<SavePlayInfoResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEPLAYINFO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVEPLAYINFO, response.getException());
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
    public void saveLaunchPlay(long userId, String title, String description, String playTitle, String videoUrl, String videoImageUrl) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("title", title);
        map.put("description", description);
        map.put("playTitle", playTitle);
        map.put("videoUrl", videoUrl);
        map.put("videoImageUrl", videoImageUrl);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVELAUNCHPLAY, tag, map, new JsonCallback<SaveLaunchPlayResponse>(SaveLaunchPlayResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<SaveLaunchPlayResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVELAUNCHPLAY, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVELAUNCHPLAY, response.getException());
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
    public void getAllPlayList(long userId, int sort, int pageSize, int pageNumber, final boolean needShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETALLPLAYLIST, tag, map, new JsonCallback<GetAllPlayListResponse>(GetAllPlayListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && needShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetAllPlayListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETALLPLAYLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETALLPLAYLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    mView.hideLoading();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getSecondLabelList(long labelId) {
        final Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.LABEL_ID, labelId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETSECONDLABELLIST, tag, map, new JsonCallback<GetSecondLabelListResponse>(GetSecondLabelListResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_GETSECONDLABELLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETSECONDLABELLIST, response.getException());
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
