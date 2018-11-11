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
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindPlayDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetMyPlayListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPlayCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PlayCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PlayPraiseResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.PkContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHAI on 2017/12/22.
 */

public class PkPresenter implements PkContract.Presenter {
    private BaseView mView;

    public PkPresenter(BaseView mView) {
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
    public void playPraise(long playId, long userId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PLAY_ID, playId);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_PLAYPRAISE, tag, map, new JsonCallback<PlayPraiseResponse>(PlayPraiseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<PlayPraiseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PLAYPRAISE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PLAYPRAISE, response.body());
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
    public void deletePlay(long playId, long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PLAY_ID, playId);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEPLAY, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_DELETEPLAY, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEPLAY, response.body());
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
    public void getMyPlayList(long userId, long toUserId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETMYPLAYLIST, tag, map, new JsonCallback<GetMyPlayListResponse>(GetMyPlayListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetMyPlayListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETMYPLAYLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETMYPLAYLIST, response.body());
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
    public void getPlayCommentList(long playId, int sort, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PLAY_ID, playId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETPLAYCOMMENTLIST, tag, map, new JsonCallback<GetPlayCommentListResponse>(GetPlayCommentListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetPlayCommentListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPLAYCOMMENTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPLAYCOMMENTLIST, response.body());
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
    public void playComment(long playId, String content, int type, long toUserId, long fromUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PLAY_ID, playId);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_PLAYCOMMENT, tag, map, new JsonCallback<PlayCommentResponse>(PlayCommentResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<PlayCommentResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PLAYCOMMENT, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_PLAYCOMMENT, response.getException());
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
    public void findPlayDetail(long userId, long playId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PLAY_ID, playId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDPLAYDETAIL, tag, map, new JsonCallback<FindPlayDetailResponse>(FindPlayDetailResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_FINDPLAYDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPLAYDETAIL, response.body());
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
