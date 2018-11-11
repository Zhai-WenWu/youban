package cn.bjhdltcdn.p2plive.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.constants.HostConfig;
import cn.bjhdltcdn.p2plive.event.UpdateRefreshTImeEvent;
import cn.bjhdltcdn.p2plive.exception.NetworkAvailableException;
import cn.bjhdltcdn.p2plive.exception.ParameterException;
import cn.bjhdltcdn.p2plive.model.PlatformInfo;
import cn.bjhdltcdn.p2plive.utils.BitmapUtils;
import cn.bjhdltcdn.p2plive.utils.DesUtil;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.NetUtils;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;

/**
 * Created by xiawenquan on 17/11/6.
 */

public class ApiData {

    private static ApiData instance;


    public static ApiData getInstance() {
        if (instance == null) {
            instance = new ApiData();
        }
        return instance;
    }

    /**
     * 获取主机地址
     *
     * @return
     */
    public String getHost() {
        if (BuildConfig.LOG_DEBUG) {
            return HostConfig.TEST_HOST + "/youban";
        } else {
            return HostConfig.OFFICIAL_HOST + "/youban";
        }
    }

    /**
     * post 异步请求
     *
     * @param url      接口路径
     * @param tag      tag标记
     * @param object   参数对象（map 或者 object ）
     * @param callback 回调接口
     * @throws Exception
     */
    public void postData(String url, String tag, Object object, Callback callback) {


        if (!NetUtils.isConnected(App.getInstance())) {

            Response response = new Response();
            response.setException(new NetworkAvailableException("网络不可用"));
            callback.onError(response);

            return;

        }

        Map map = null;
        String jsonTepm = null;
        if (object == null) {
            map = new LinkedHashMap(1);
        } else if (object instanceof Map) {
            map = (Map) object;

            StringBuilder sb = new StringBuilder();
            for (Object key : map.keySet()) {
                sb.append("key=" + key + " value=" + map.get(key) + " ; ");
            }
        } else {
            //类全名
            String fullName = object.getClass().getName();
            if (StringUtils.isEmpty(fullName)) {
                map = new LinkedHashMap(1);
            } else {
                try {
                    //得到对象
                    Class c = Class.forName(fullName);
                    //得到声明的参数
                    if (c != null && c.getDeclaredFields() != null && c.getDeclaredFields().length > 0) {
                        jsonTepm = JsonUtil.getObjectMapper().writeValueAsString(object);
                        map = JsonUtil.getObjectMapper().readValue(jsonTepm, Map.class);
                    } else {
                        map = new LinkedHashMap(1);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    map = new LinkedHashMap(1);
                }
            }
        }


        post(url, tag, map, callback);


    }

    /**
     * post 提交网络任务
     *
     * @param url      接口路径
     * @param tag      网络任务标记
     * @param map      请求数据
     * @param callback 网络层请求回调
     */
    private void post(String url, String tag, Map map, final Callback callback) {

        String sysToken = "";
        if (!InterfaceUrl.URL_GETVERIFICATIONCODE.equals(url)) {
            Object object = map.get(Constants.Fields.USER_ID);
            if (object == null) {
                // 添加用户id
                map.put(Constants.Fields.USER_ID, SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
            }

            // 添加token
            sysToken = SafeSharePreferenceUtils.getString(Constants.KEY.KEY_SYS_TOKEN, "");
            map.put(Constants.KEY.KEY_SYS_TOKEN, sysToken);
        }

        // 1.请求体的原始数据
        String requestBody = null;
        try {
            requestBody = JsonUtil.getObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            if (callback != null) {
                Response response = new Response();
                response.setException(new ParameterException("参数错误"));
                callback.onError(response);
            }
            return;
        }

        if (StringUtils.isEmpty(requestBody)) {
            if (callback != null) {
                Response response = new Response();
                response.setException(new ParameterException("参数错误"));
                callback.onError(response);
            }
            return;
        }

        // 请求原始数据
        Logger.d("请求原始数据");
        Logger.json(requestBody);

        try {
            String sha1Text = null;
            // 支付、强制升级 不需要加密
            if (!(InterfaceUrl.URL_SAVEWEIXINORDER.equals(url) || InterfaceUrl.URL_SAVEALIPAYORDER.equals(url)
//                    || InterfaceUrl.URL_GETUPGRADEVERSION.equals(url)
            )) {
                // 1.des简单加密
                String jsonData = DesUtil.encrypt(requestBody, DesUtil.KEY);

                map.clear();
                map.put("data", jsonData);
                //{"data":"bnrwgetbfkjsno"}
                requestBody = JsonUtil.getObjectMapper().writeValueAsString(map);
                // 请求des加密数据
                Logger.json(requestBody);

                // 2.SHA1验证
                String temp = DesUtil.MD5(requestBody);
                // 注册、登录、获取验证码 不需要验证 HSA1
                if (!InterfaceUrl.URL_REGISTER.equals(url) || !InterfaceUrl.URL_GETVERIFICATIONCODE.equals(url)
                        || !InterfaceUrl.URL_LOGINBYPHONE.equals(url) || !InterfaceUrl.URL_LOGINBYTHIRDPARTY.equals(url)) {
                    temp = temp + sysToken;
                }
                sha1Text = DesUtil.SHA1(temp);

            }

            map.clear();
            // 添加平台信息
            PlatformInfo platformInfo = PlatformInfoUtils.getClientDataJSONObj(App.getInstance());
            map.put(Constants.KEY.KEY_APP_PLATFORM_INFO, platformInfo);
            String platformInfoStr = JsonUtil.getObjectMapper().writeValueAsString(map);


            if (StringUtils.isEmpty(requestBody)) {
                Logger.d("requestBody is null ");
                return;
            }

            // 提交联网任务
            OkGo.post(getHost() + url + ".do")
                    .tag(!StringUtils.isEmpty(tag) ? tag : "OkGo")
                    // 消息头
                    .headers(Constants.KEY.KEY_SHA1, sha1Text)
                    //防止app调用H5接口，没有此消息头
                    .headers(Constants.KEY.KEY_USER_CERTIFICATE, platformInfo.getUserId() + "")
                    .headers(Constants.KEY.KEY_APP_PLATFORM_INFO, platformInfoStr)
                    // 请求体
                    .upJson(requestBody).execute(new AbsCallback<Object>() {
                @Override
                public void onStart(Request request) {
                    if (callback != null) {
                        callback.onStart(request);
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (callback != null) {
                        callback.onSuccess(response);
                    }
                }

                @Override
                public void onCacheSuccess(Response response) {
                    if (callback != null) {
                        callback.onCacheSuccess(response);
                    }
                }

                @Override
                public void onError(Response response) {
                    if (callback != null) {

                        if (response.getException() instanceof Exception) {
                            Exception e = (Exception) response.getException();
                            String text = e.getMessage();
                            if (e instanceof InterruptedIOException || e instanceof SocketException) {
                                response.setException(new NetworkAvailableException("网络连接异常"));
                                Utils.showToastShortTime("网络连接异常");
                            } else if (e instanceof JsonProcessingException) {
                                response.setException(new NetworkAvailableException("解析数据异常"));
                                Utils.showToastShortTime("解析数据异常");
                            }
                            e.printStackTrace();
                        }
                        int code = response.code();
                        if (code == 404 || code >= 500) {
                            Utils.showToastShortTime("网络正在开小差~~~");
                            EventBus.getDefault().post(new UpdateRefreshTImeEvent());
                            response.setException(new NetworkAvailableException("网络正在开小差~~~"));
                            callback.onError(response);
                        } else {
                            callback.onError(response);
                        }
                    }
                }

                @Override
                public void onFinish() {
                    if (callback != null) {
                        callback.onFinish();
                    }
                }

                @Override
                public void uploadProgress(Progress progress) {
                    if (callback != null) {
                        callback.uploadProgress(progress);
                    }
                }

                @Override
                public void downloadProgress(Progress progress) {
                    if (callback != null) {
                        callback.downloadProgress(progress);
                    }
                }

                @Override
                public Object convertResponse(okhttp3.Response response) throws Throwable {
                    if (callback != null) {
                        return callback.convertResponse(response);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                Response response = new Response();
                response.setException(new ParameterException("参数错误"));
                callback.onError(response);
            }
            return;
        }

    }


    /**
     * 上传图片
     *
     * @param url      接口地址
     * @param tag      接口名称
     * @param map      请求参数集合
     * @param callback 网络层请求回调
     */
    public void uploadImage(String url, String tag, Map map, final Callback callback) {

        PostRequest request = OkGo.post(getHost() + url + ".do");
        request.tag(!StringUtils.isEmpty(tag) ? tag : "OkGo");

        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        request.isMultipart(true);

        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            if ("file".equals(entry.getKey())) {
                if (entry.getValue() != null && !"".equals(entry.getValue()) && !"null".equals(entry.getValue())) {
                    if (entry.getValue() instanceof String) {
                        String fileUrl = String.valueOf(entry.getValue());
                        Logger.d("fileUrl ==== " + fileUrl);
                        if (!StringUtils.isEmpty(fileUrl)) {
                            File file = new File(fileUrl);
                            if (file != null && file.exists()) {
                                if (fileUrl.endsWith(".gif")) {
                                    request.params(entry.getKey(), file);

                                } else {

                                    File scaleFile = BitmapUtils.doParseToFile(fileUrl);
                                    if (scaleFile != null && scaleFile.exists()) {
                                        request.params(entry.getKey(), scaleFile);
                                    } else {
                                        Logger.d("压缩的图片失败");
                                        request.params(entry.getKey(), "");
                                    }
                                }
                            }
                        }
                    }

                } else {
                    request.params(entry.getKey(), "");
                }
                continue;
            }
            request.params(entry.getKey(), entry.getValue() + "");
            // 请求原始数据
            Logger.d(entry.getKey() + "=====" + entry.getValue());

        }

        request.execute(callback);


    }


    /**
     * (同步)上传图片
     *
     * @param url 接口地址
     * @param tag 接口名称
     * @param map 请求参数集合
     */
    public String uploadImageSynchronizing(String url, String tag, Map map) {

        PostRequest request = OkGo.post(getHost() + url + ".do");
        request.tag(!StringUtils.isEmpty(tag) ? tag : "OkGo");
        request.isMultipart(true);
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            if ("file".equals(entry.getKey())) {
                request.params(entry.getKey(), new File(entry.getValue() + ""));
                continue;
            }
            request.params(entry.getKey(), entry.getValue() + "");

        }

        try {

            okhttp3.Response response = request.execute();
            String responseStr = response.body().string();
            response.close();

            return responseStr;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 保存gif图片接口
     *
     * @param url      图片路径（http://）
     * @param callback
     */
    public void saveGifFile(String url, Callback callback) {
        if (callback != null) {
            OkGo.get(url).execute(callback);
        }

    }


}
