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
import cn.bjhdltcdn.p2plive.httpresponse.GetMyShareListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostListByLabelIdResponse;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.ShareContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class SharePresenter implements ShareContract.Presenter {

    private BaseView mView;

    public SharePresenter(BaseView mView) {
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
    public void saveShareNumber(long userId, long parentId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVESHARENUMBER, tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SAVESHARENUMBER, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESHARENUMBER, response.body());
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
    public void saveShareAttention(long userId, long parentId, int type, String content) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.CONTENT, content);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SAVESHAREATTENTION, tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_SAVESHAREATTENTION, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SAVESHAREATTENTION, response.body());
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
    public void getMyShareList(long userId, long toUserId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETMYSHARELIST, tag, map, new JsonCallback<GetMyShareListResponse>(GetMyShareListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetMyShareListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETMYSHARELIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETMYSHARELIST, response.body());
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
    public void deleteMyShare(long userId, long shareId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.SHARE_ID, shareId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEMYSHARE, tag, map, new JsonCallback<NoParameterResponse>(NoParameterResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_DELETEMYSHARE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEMYSHARE, response.body());
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
    public void getPostListByLabelId(long userId, long postLabelId, int pageSize, int pageNumber) {
        {
            Map map = new LinkedHashMap(1);
            map.put(Constants.Fields.USER_ID, userId);
            map.put(Constants.Fields.POST_LABEL_ID, postLabelId);
            map.put(Constants.Fields.PAGE_SIZE, pageSize);
            map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
            String tag = mView.getClass().getSimpleName();

            try {
                ApiData.getInstance().postData(InterfaceUrl.URL_GETPOSTLISTBYLABELID, tag, map, new JsonCallback<GetPostListByLabelIdResponse>(GetPostListByLabelIdResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                        if (mView != null) {
                            mView.showLoading();
                        }
                    }

                    @Override
                    public void onSuccess(Response<GetPostListByLabelIdResponse> response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETPOSTLISTBYLABELID, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);

                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_GETPOSTLISTBYLABELID, response.body());
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
