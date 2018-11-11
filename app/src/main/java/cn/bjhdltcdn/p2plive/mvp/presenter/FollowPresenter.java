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
import cn.bjhdltcdn.p2plive.httpresponse.FindFollowDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetItemFollowListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.FollowContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by ZHAI on 2018/3/26.
 */

public class FollowPresenter implements FollowContract.Presenter {
    private BaseView mView;

    public FollowPresenter(BaseView mView) {
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
    public void findFollowDetail(long userId, long parentId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDFOLLOWDETAIL, tag, map, new JsonCallback<FindFollowDetailResponse>(FindFollowDetailResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<FindFollowDetailResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDFOLLOWDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDFOLLOWDETAIL, response.getException());
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
    public void getItemFollowList(long userId,long toUserId, int sort, long parentId, int type, int pageSize, int pageNumber, final boolean needShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETITEMFOLLOWLIST, tag, map, new JsonCallback<GetItemFollowListResponse>(GetItemFollowListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null&&needShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetItemFollowListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETITEMFOLLOWLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETITEMFOLLOWLIST, response.getException());
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
    public void getAllFollowList(long userId, int sort, int pageSize, int pageNumber, final boolean needShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETALLFOLLOWLIST, tag, map, new JsonCallback<GetItemFollowListResponse>(GetItemFollowListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && needShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response<GetItemFollowListResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETALLFOLLOWLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETALLFOLLOWLIST, response.getException());
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
