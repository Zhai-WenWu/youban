package cn.bjhdltcdn.p2plive.mvp.presenter;

import android.net.Uri;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.exception.ParameterException;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.mvp.contract.ImageContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by xiawenquan on 17/11/21.
 */

public class ImagePresenter implements ImageContract.Presenter {

    private BaseView mView;

    public ImagePresenter(BaseView mView) {
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
    public void uploadImages(long parentId, int type, long userId, String photoGraphTime, String fileUrl, long imageId, int orderNum, int total) {

        if (StringUtils.isEmpty(fileUrl) || fileUrl.startsWith("http")) {
            Logger.d("fileUrl ===is null === " + fileUrl);
            return;
        }

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PHOTOGRAPH_TIME, photoGraphTime);
        map.put(Constants.Fields.FILE, fileUrl);
        map.put(Constants.Fields.ORDERNUM, orderNum);
        map.put(Constants.Fields.IMAGE_ID, imageId);
        map.put(Constants.Fields.TOTAL, total);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().uploadImage(InterfaceUrl.URL_UPLOADIMAGE, tag, map, new JsonCallback<UploadImageResponse>(UploadImageResponse.class) {

            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, response.body());
                }
            }

            @Override
            public void onError(Response response) {
                super.onError(response);
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, response.getException());
                }
                Utils.showToastShortTime("您有一张图片上传失败");
            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void uploadImage(long parentId, final int type, long userId, String photoGraphTime, String file) {

        if (StringUtils.isEmpty(file) || file.startsWith("http")) {
            Logger.d("fileUrl ===is null === " + file);
            return;
        }

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PHOTOGRAPH_TIME, photoGraphTime);
        map.put(Constants.Fields.FILE, file);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().uploadImage(InterfaceUrl.URL_UPLOADIMAGE, tag, map, new JsonCallback<UploadImageResponse>(UploadImageResponse.class) {

            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, response.body());


                    // 上传头像
                    if (type == 1) {
                        if (response.body() instanceof UploadImageResponse) {
                            final UploadImageResponse uploadImageResponse = (UploadImageResponse) response.body();
                            if (uploadImageResponse.getImage() != null) {
                                if (!StringUtils.isEmpty(uploadImageResponse.getImage().getImageUrl())) {
                                    GreenDaoUtils.getInstance().getBaseUser(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), new GreenDaoUtils.ExecuteCallBack() {
                                        @Override
                                        public void callBack(Object object) {
                                            if (object instanceof BaseUser) {
                                                BaseUser baseUser = (BaseUser) object;
                                                UserInfo userInfo = new UserInfo(baseUser.getUserId() + "", baseUser.getNickName(), Uri.parse(uploadImageResponse.getImage().getImageUrl()));
                                                RongIM.getInstance().refreshUserInfoCache(userInfo);
                                            }

                                        }
                                    });
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Response response) {
                super.onError(response);
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, response.getException());
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });

    }

    @Override
    public void uploadImageSynchronizing(long parentId, int type, long userId, String photoGraphTime, String fileUrl, Class calzz) {

        if (mView != null) {
            mView.showLoading();
        }

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PHOTOGRAPH_TIME, photoGraphTime);
        map.put(Constants.Fields.FILE, fileUrl);
        String tag = mView.getClass().getSimpleName();

        String responseStr = ApiData.getInstance().uploadImageSynchronizing(InterfaceUrl.URL_UPLOADIMAGE, tag, map);
        try {
            Object object = JsonUtil.getObjectMapper().readValue(responseStr, calzz);
            if (mView != null) {
                mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, object);
            }

        } catch (IOException e) {
            e.printStackTrace();

            if (mView != null) {
                Response response = new Response();
                response.setException(new ParameterException("解析失败"));
                mView.updateView(InterfaceUrl.URL_UPLOADIMAGE, response.body());
            }

        } finally {
            if (mView != null) {
                mView.hideLoading();
            }
        }


    }
}
