package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrganizationListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetReportTypeResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.CommonContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by xiawenquan on 17/11/27.
 */

public class CommonPresenter implements CommonContract.Presenter {

    private BaseView mView;

    public CommonPresenter(BaseView mView) {
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
    public void getReportType(int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_GETREPORTTYPE, tag, map, new JsonCallback<GetReportTypeResponse>(GetReportTypeResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETREPORTTYPE, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETREPORTTYPE, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void reportOperation(long id, int type, long fromUserId, long toUserId, long reportTypeId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ID, id);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.REPORT_TYPE_ID, reportTypeId);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_REPORTOPERATION, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_REPORTOPERATION, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_REPORTOPERATION, response.getException());
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
    }
}
