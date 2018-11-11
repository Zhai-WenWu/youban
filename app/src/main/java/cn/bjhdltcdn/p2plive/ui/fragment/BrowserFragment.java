package cn.bjhdltcdn.p2plive.ui.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.ui.activity.MoreActivity;
import cn.bjhdltcdn.p2plive.ui.activity.PostDetailActivity;
import cn.bjhdltcdn.p2plive.ui.activity.TrailReportListActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.utils.FileUtils;
import cn.bjhdltcdn.p2plive.utils.ShareUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.alipay.ZfbPayUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.utils.wxpay.WxPayUtils;

/**
 * Created by Hu_PC on 2017/11/8.
 * 支付界面
 */

public class BrowserFragment extends BaseFragment {
    private View rootView;
    private AppCompatActivity mActivity;
    private WebView webView;
    private int flag;
    private String signUpGameGroupUrl;
    private String mTitle;
    /***
     * H5选择本地相册相关的问题
     */
    private ValueCallback<Uri[]> mFilePathCallback;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;
    private long orderId;

    /**
     * 是否需要关闭当前页面
     */
    private boolean isNeedFinished;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_browser, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        webView = mActivity.findViewById(R.id.agreementWebview);
        flag = mActivity.getIntent().getIntExtra(Constants.Action.ACTION_BROWSE, -1);

        String title = "";
        String urlFileName = "";
        switch (flag) {
            case 1: // 用户协议
                urlFileName = "UserAgreement.html";
                title = "用户协议";
                break;

            case 2: // 圈子管理规范
                urlFileName = "SocietyProtocol.html";
                title = "圈子管理规范";
                break;
            case 3: // 帮助
                //urlFileName = "SocietyProtocol.html";
                title = "帮助";
                break;
            case 4: // 申请开店规章制度
                urlFileName = "ApplyForShopRules.html";
                title = "规章制度";
                break;
        }

        webView.clearHistory();

        WebSettings setting = webView.getSettings();
        //设置 缓存模式
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: 根据cache-control决定是否从网络上取数据
         * LOAD_CACHE_NORMAL: API level 17中已经废弃, 从API level 11开始作用同LOAD_DEFAULT模式
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置与Js交互的权限
        setting.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置可以访问文件
        setting.setAllowFileAccess(true);
        //设置支持缩放
        setting.setBuiltInZoomControls(false);
        //开启DOM
        setting.setDomStorageEnabled(true);
        // js调用android接口
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webView.addJavascriptInterface(new BrowserFragment.AndroidtoJs(), "AndroidtoJs");

        setTitle(title);


        String url = mActivity.getIntent().getStringExtra(Constants.KEY.KEY_URL);

        if (url == null) {
            url = "";
        } else {
            signUpGameGroupUrl = url;//记录报名入口html
        }

        Logger.d("flag =========" + flag);
        Logger.d("url =========" + url);


        // 设置setWebChromeClient对象
        switch (flag) {
            case 1:
            case 2:
            case 4:
                webView.loadUrl("file:///android_asset/" + urlFileName);
//                webView.setWebChromeClient(new WebChromeClient() {
//                    @Override
//                    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
////                        b.setTitle("Alert");
//                        b.setTitle("");
//                        b.setMessage(message);
//                        b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirm();
//                            }
//                        });
//                        b.setCancelable(false);
//                        b.create().show();
//                        return true;
////                        return super.onJsAlert(view, url, message, result);
//                    }
//                });
//                webView.setWebViewClient(new WebViewClient() {
//
//                    @Override
//                    public void onPageFinished(WebView view, String url) {
//                        super.onPageFinished(view, url);
//                        JavaJs(1,"code");
//
//                    }
//                });
                break;
            case 3:
                webView.loadUrl(url);
                break;

            default:

                // webView 的辅助类
                WebChromeClient webChromeClient = new WebChromeClient() {
                    @Override
                    public void onReceivedTitle(WebView view, String title) {
                        super.onReceivedTitle(view, title);

                        Logger.d("title === " + title);

                        setTitle(title);
                    }

                    @Override
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {

                        Logger.d("filePathCallback === " + filePathCallback + " ===>>fileChooserParams===>> " + fileChooserParams);

                        mFilePathCallback = filePathCallback;
                        openImageChooserActivity();
                        return true;
                    }

                };

                webView.setWebChromeClient(webChromeClient);

                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (mActivity.isFinishing()) {
                            return true;
                        }

                        view.loadUrl(url);
                        Logger.d("url =========" + url);

                        return true;
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);

                        Logger.d("url =========" + url);

                        if (isNeedFinished) {
                            isNeedFinished = false;
                            webView.clearHistory();
                        }

                    }
                });
                webView.loadUrl(url);
                break;
        }

    }

    private void setTitle(final String title) {
        mTitle = title;
        TitleFragment fragment = (TitleFragment) getChildFragmentManager().findFragmentById(R.id.title_fragment);
        fragment.setTitle(TextUtils.isEmpty(title) ? "" : title);
        fragment.setLeftViewTitle(R.mipmap.back_icon, "", new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                //game/entrance.html

                // H5需要关闭当前页面
                if (isNeedFinished) {
                    isNeedFinished = false;

                    mActivity.finish();

                    return;
                }

                if (!StringUtils.isEmpty(signUpGameGroupUrl) && (mTitle.equals("我的战队") || mTitle.equals("战队列表"))) {
                    webView.loadUrl(signUpGameGroupUrl);
                    webView.clearHistory();
                    Logger.d("url =========" + signUpGameGroupUrl);
                } else if (webView.canGoBack() && !title.equals("报名入口") && !title.equals("赛事公告") && !mTitle.equals("赛事详情公告"))

                {//不是报名入口，不是赛事公告
                    webView.goBack();
                } else {
                    mActivity.finish();
                }
            }
        });
    }

    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK) && !StringUtils.isEmpty(signUpGameGroupUrl) && (mTitle.equals("我的战队") || mTitle.equals("战队列表"))) {
            webView.loadUrl(signUpGameGroupUrl);
            webView.clearHistory();
            Logger.d("url =========" + signUpGameGroupUrl);
            return true;
        } else if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack() && !mTitle.equals("报名入口") && !mTitle.equals("赛事公告") && !mTitle.equals("赛事详情公告")) {//不是报名入口，不是赛事公告
            webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        mActivity.finish();//结束退出程序
        return false;
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void jumpDetail(int type, String id) {// id字段js页面传递的就是字符串

            Logger.d("JS调用了Android的jumpDetail()方法 id ====>>>> " + id + "  type ====>>>>  " + type);

            if (StringUtils.isEmpty(id) || "0".equals(id)) {
                return;

            }

            switch (type) {
                //1 用户详情   2 圈子详情   3 帖子详情  4 表白详情   5 帮帮忙  6 活动详情 7 更多页面 8 ； 9 清除浏览历史记录
                case 1:// 用户详情
                    if (Long.valueOf(id) != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                        //跳到用户详情
                        BaseUser baseUser = new BaseUser();
                        baseUser.setUserId(Long.valueOf(id));
                        startActivity(new Intent(App.getInstance(), UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));
                    }
                    break;

                case 2:// 圈子详情
                    break;

                case 3:// 帖子详情
                    PostInfo postInfo = new PostInfo();
                    postInfo.setPostId(Long.valueOf(id));
                    startActivity(new Intent(App.getInstance(), PostDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, postInfo));
                    break;

                case 4:// 表白详情
                    break;

                case 5:// 帮帮忙详情
                    break;

                case 6:// 活动详情
                    break;
                case 7:// 我的更多功能
                    startActivity(new Intent(App.getInstance(), MoreActivity.class));
                    break;
                case 8://试用
                    startActivity(new Intent(App.getInstance(), TrailReportListActivity.class));
                    break;

                case 9:

                    isNeedFinished = true;

                    break;

                default:

            }
        }

        @JavascriptInterface
        public void clickShare(String titleStr, String imageUrl, String webUrl, String contentStr) {
            Logger.d("shareWebUrl =========" + webUrl);
            ShareUtil.getInstance().setURL_MATCH(webUrl);
            ShareUtil.getInstance().showShare(mActivity, ShareUtil.MATCH, 0, 0, "", titleStr, contentStr, imageUrl, false);
        }

        @JavascriptInterface
        public void clickShare(String titleStr, String imageUrl, String WXwebUrl, String QQwebUrl, String contentStr) {
            Logger.d("WXwebUrl =========" + WXwebUrl);
            Logger.d("QQwebUrl =========" + QQwebUrl);
            ShareUtil.getInstance().setURL_AUTHORIZE_WX(WXwebUrl);
            ShareUtil.getInstance().setURL_AUTHORIZE_QQ(QQwebUrl);
            ShareUtil.getInstance().showShare(mActivity, ShareUtil.AUTHORIZE, 0, 0, "", titleStr, contentStr, imageUrl, false);
        }

        @JavascriptInterface // 1 微信；2 支付 ；signature 签名数据
        public void payJump(int type, String signature,long orderid) {

            Logger.d("type =========" + type + "  ====== signature ==== " + signature);
            orderId=orderid;
            if (type == 2) {
                //调用第三方支付宝支付sdk
                ZfbPayUtils.getInstance().setType(1);
                ZfbPayUtils.getInstance().pay(mActivity, signature);
            } else if (type == 1) {
                if (isWXAppInstalledAndSupported()) {
                    //调用第三方微信支付sdk
                    WxPayUtils.getInstance().pay(mActivity, signature);
                } else {
                    Utils.showToastShortTime("请检查是否安装微信客户端");
                }
            } else {
                Utils.showToastShortTime("请选择支付方式");
            }
        }

    }

    //android中调用js中的方法 type 1 微信；2 支付 ; 通知的code码
    public void JavaJs(int type, String code) {
        Logger.d("type === " + type + " ===code==== " + code);

//        webView.evaluateJavascript("androidNotifyJsPayResult('"+type+ "'，'"+msg+"')", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String value) {
//
//            }
//        });
        webView.loadUrl("javascript:androidNotifyJsPayResult('" + type + " ','"+ code + " ','" + orderId + "')");

//       webView.loadUrl("javascript:message2()");

    }




    /**
     * 开启本地相册
     */
    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "图片选择"), FILE_CHOOSER_RESULT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mFilePathCallback) {
                return;
            }

            if (mFilePathCallback != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mFilePathCallback == null) {
            return;
        }

        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {

                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }

                String dataString = intent.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }

            }
        }

        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(results);
        }

    }


    //检测手机是否安装微信客户端
    private boolean isWXAppInstalledAndSupported() {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(mActivity, null);
        msgApi.registerApp(Constants.KEY.KEY_WXPAY);

        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled()
                && msgApi.isWXAppSupportAPI();

        return sIsWXAppInstalledAndSupported;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            // 将webview的页面设置问空页面即可
            webView.loadUrl("about:blank");
            // 清除缓存
            webView.clearCache(true);

            webView.clearHistory();

            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();

            webView = null;

        }

        if (mActivity != null) {
            mActivity = null;
        }

        if (mFilePathCallback != null) {
            mFilePathCallback = null;
        }

        if (rootView != null) {
            rootView = null;
        }

        // 防止clearCache()方法不生效，清楚网页缓存
        FileUtils.clearWebViewCache(App.getInstance());


        ZfbPayUtils.getInstance().onDestroy();


    }


    @Override
    protected void onVisible(boolean isInit) {

    }
}
