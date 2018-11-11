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
import cn.bjhdltcdn.p2plive.httpresponse.FindCashAccountResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetOrganizationListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SaveCashAccountResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.ExchangeContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class ExchangePresenter implements ExchangeContract.Presenter {

    private BaseView mView;

    public ExchangePresenter(BaseView mView) {
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
    public void saveCashAccount(long userId, String account, String phoneNumber, String verificationCode) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ACCOUNT, account);
        map.put(Constants.Fields.PHONE_NUMBER, phoneNumber);
        map.put(Constants.Fields.VERIFICATION_CODE, verificationCode);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_SAVECASHACCOUNTR, tag, map, new JsonCallback<SaveCashAccountResponse>(SaveCashAccountResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (mView != null ) {
                    mView.showLoading();
                }
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SAVECASHACCOUNTR, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_SAVECASHACCOUNTR, response.getException());
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

    @Override
    public void findCashAccount(long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_FINDCASHACCOUNT, tag, map, new JsonCallback<FindCashAccountResponse>(FindCashAccountResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (mView != null ) {
                    mView.showLoading();
                }
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_FINDCASHACCOUNT, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_FINDCASHACCOUNT, response.getException());
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
