package cn.bjhdltcdn.p2plive.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ShareNumUpdateEvent;
import cn.bjhdltcdn.p2plive.event.ShareSuccessEvent;
import cn.bjhdltcdn.p2plive.httpresponse.NoParameterResponse;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PlayInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.model.StoreDetail;
import cn.bjhdltcdn.p2plive.mvp.presenter.SharePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.SelectAddressBookActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShareActivity;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by huwenhua on 2015/11/24.
 */
public class ShareUtil implements BaseView {
//    public Activity context;
    public static ShareUtil shareUtil;
    private String URL_LOGO = "/uploadfile/logo.png";
    private String URL_STOREDETAIL="/h5-project/view/lottery/#/store-detail";
    private String URL_YOUBAN = "http://sj.qq.com/myapp/detail.htm?apkName=cn.bjhdltcdn.p2plive";//app应用市场
    private String URL_MATCH="";
    private String URL_AUTHORIZE_WX="";//授权微信url
    private String URL_AUTHORIZE_QQ="";//授权qq url
    private String URL_SHARE = "";
    public static final int POST = 1, ORGAIN = 2, ACTIVE = 3, VIDEO = 4, PK = 5, SAYLOVE = 6,CLASSMATEHELP=8, YOUBAN = 9, LOGO = 10,MATCH=11,AUTHORIZE=12,STOREDETAIL=13;
    public StringBuilder builder;
    public SharePresenter sharePresenter;
    private long userId;
    private long parentId;
    private int type;
//    private Object object;
    private String defaultImg;//活动默认图片
    private boolean isShareCustom;
//    Solve7PopupWindow popupWindow=null;

    private String WEI_XIN_AUTHORIZE_BASE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx46e8742b48593b33&";

    public static ShareUtil getInstance() {
        if (shareUtil == null) {
            shareUtil = new ShareUtil();
        }
        return shareUtil;
    }



    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }

    public void setURL_MATCH(String URL_MATCH) {
        this.URL_MATCH = URL_MATCH;
    }

    public void setURL_AUTHORIZE_WX(String URL_AUTHORIZE_WX) {
        this.URL_AUTHORIZE_WX = URL_AUTHORIZE_WX;
    }

    public void setURL_AUTHORIZE_QQ(String URL_AUTHORIZE_QQ) {
        this.URL_AUTHORIZE_QQ = URL_AUTHORIZE_QQ;
    }

    public SharePresenter getSharePresenter() {
        if (sharePresenter == null) {
            sharePresenter = new SharePresenter(this);
        }
        return sharePresenter;
    }

    public String getShareUrl(int type, long id, String uuid) {
        if (builder == null) {
            builder = new StringBuilder();
        } else {
            builder.setLength(0);
        }
        URL_SHARE = App.getInstance().getShareUrl();
        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        switch (type) {
            case SAYLOVE:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + SAYLOVE + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case POST:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + POST + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case ORGAIN:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + ORGAIN + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case ACTIVE:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + ACTIVE + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case VIDEO:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + VIDEO + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case PK:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + PK + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case CLASSMATEHELP:
                if (!StringUtils.isEmpty(URL_SHARE)) {
                    builder.append(URL_SHARE + "?id=" + id + "&type=" + CLASSMATEHELP + "&userId=" + userId);
                } else {
                    builder.append(URL_YOUBAN);
                }
                break;
            case STOREDETAIL:
                String shareurl = ApiData.getInstance().getHost();
                builder.append(shareurl.substring(0, shareurl.lastIndexOf("/")));
                builder.append(URL_STOREDETAIL + "?userId=" + userId + "&storeId=" + id);
                break;
            case YOUBAN:
                builder.append(URL_YOUBAN);
                break;
            case MATCH:
                builder.append(URL_MATCH);
                break;
            case LOGO:
                String shareUrl = ApiData.getInstance().getHost();
                builder.append(shareUrl.substring(0, shareUrl.lastIndexOf("/")));
                builder.append(URL_LOGO);
                break;
            default:
                break;
        }
        Log.v("=shareUrl=", builder.toString());
        return builder.toString();
    }

    /**
     * @param type     分享类型
     * @param id       对象id
     * @param uuid
     * @param title    分享标题
     * @param content  分享内容
     * @param imageUrl 分享图片路径
     */
    public void showShare(final Activity context, final int type, final long id, final Object object, String uuid, final String title, final String content, final String imageUrl, boolean isShareCustom) {
//        this.context=context;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        parentId = id;
        this.type = type;
//        this.object = object;
//        LinearLayout popView = (LinearLayout) context.getLayoutInflater().inflate(R.layout.layout_share_success_tip, null, false);
//        popupWindow = new Solve7PopupWindow(popView, LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.FILL_PARENT);//ViewGroup.LayoutParams.MATCH_PARENT, true
//        popupWindow.setFocusable(false);
//        popupWindow.setOutsideTouchable(false);
//        popupWindow.setBackgroundDrawable(App.getInstance().getCurrentActivity().getResources().getDrawable(R.mipmap.pay_radiobutton_unselect_icon));
//        popupWindow.setAnimationStyle(R.style.dialogWindowAnim);
        final OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        if (object instanceof SayLoveInfo) {
            SayLoveInfo sayLoveInfo = (SayLoveInfo) object;
            oks.setTitle(getTitle(sayLoveInfo.getContent()));
        } else if (object instanceof PostInfo) {
            PostInfo postInfo = (PostInfo) object;
            oks.setTitle(getTitle(postInfo.getContent()));
        }else if (object instanceof HelpInfo) {
            HelpInfo helpInfo = (HelpInfo) object;
            oks.setTitle(getTitle(helpInfo.getContent()));
        } else if (object instanceof OrganizationInfo) {
            OrganizationInfo organizationInfo = (OrganizationInfo) object;
            oks.setTitle(getTitle(organizationInfo.getDescription()));
        } else if (object instanceof PlayInfo) {
            PlayInfo playInfo = (PlayInfo) object;
            oks.setTitle(getTitle(playInfo.getTitle()));
        } else if (object instanceof ActivityInfo) {
//            ActivityInfo activityInfo = (ActivityInfo) object;
//            oks.setTitle(getTitle(activityInfo.getTheme()));
            oks.setTitle(getTitle(title));
        } else if (object instanceof RoomInfo) {
//            RoomInfo roomInfo = (RoomInfo) object;
//            oks.setTitle(getTitle(roomInfo.getDescription()));
            oks.setTitle(getTitle(title));
        }else{
            oks.setTitle(getTitle(title));
        }
        // text是分享文本，所有平台都需要这个字段
        if (!StringUtils.isEmpty(content)) {
            oks.setText(content);
        } else {
            switch (type) {
                case SAYLOVE:
                    oks.setText("友伴校园表白，我遇见你是最美丽的意外");
                    break;
                case POST:
                    oks.setText("分享你一个有趣的帖子");
                    break;
                case ORGAIN:
                    oks.setText("分享兴趣圈子“**社”，我们在友伴等你加入！");
                    break;
                case ACTIVE:
                    oks.setText("活动找人：“***”邀你一起来玩儿！");
                    break;
                case VIDEO:
                    oks.setText("多人聊天：“***”，邀你一起来参与聊天！");
                    break;
                case PK:
                    oks.setText("友伴PK挑战，等你来参加，秀出你的才艺！");
                    break;
                case CLASSMATEHELP:
                    oks.setText("分享你一个“同学帮帮忙”快来围观吧！");
                    break;
                case MATCH:
                    oks.setText("王者荣耀比赛：我组建了一支战队，快来加入我的战队一起赢奖金吧！");
                    break;
                case AUTHORIZE:
                    oks.setText("在友伴，你想做的任何事，都能找到人陪你一起做哦！");
                    break;
                case STOREDETAIL:
                    oks.setText("在友伴，你想做的任何事，都能找到人陪你一起做哦！");
                    break;
                default:
                    oks.setText("在友伴，你想做的任何事，都能找到人陪你一起做哦！");
                    break;
            }
        }
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        if (!StringUtils.isEmpty(imageUrl)) {
            oks.setImageUrl(imageUrl);//确保网络上存在此张图片
        } else {
            oks.setImageUrl(getShareUrl(LOGO, 0, ""));//确保网络上存在此张图片
        }
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("友伴");
        String url = "";
        if(type!=AUTHORIZE){
            url = getShareUrl(type, id, uuid);
            // titleUrl是标题的网络链接，QQ和QQ空间等使用
            oks.setTitleUrl(url);
            // url仅在微信（包括好友和朋友圈）中使用
            oks.setUrl(url);
            // siteUrl是分享此内容的网站地址，仅在QQ空间使用
            oks.setSiteUrl(url);
        }
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                if(platform.getName().equals(Wechat.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                    switch (type){
                        case POST:
                        case SAYLOVE:
                        case CLASSMATEHELP:

                            String weixinUrl = WEI_XIN_AUTHORIZE_BASE_URL + "redirect_uri=" + App.getInstance().getWeixinShareUrl() + "?str=" + userId + "_" + type + "_" + id + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
                            Logger.d("weixinUrl === " + weixinUrl);
                            paramsToShare.setUrl(weixinUrl);

                            break;

                        case AUTHORIZE:
                            paramsToShare.setUrl(URL_AUTHORIZE_WX);
                            break;
                    }

                }else if(platform.getName().equals(WechatMoments.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                    switch (type){
                        case POST:
                        case SAYLOVE:
                        case CLASSMATEHELP:
                            String weixinUrl = WEI_XIN_AUTHORIZE_BASE_URL + "redirect_uri=" + App.getInstance().getWeixinShareUrl() + "?str=" + userId + "_" + type + "_" + id + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
                            Logger.d("weixinUrl === " + weixinUrl);
                            paramsToShare.setUrl(weixinUrl);
                            break;

                        case AUTHORIZE:
                            paramsToShare.setUrl(URL_AUTHORIZE_WX);
                            break;
                    }

                }else if(platform.getName().equals(QQ.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                    switch (type){
                        case AUTHORIZE:
                            paramsToShare.setUrl(URL_AUTHORIZE_QQ);
                            break;

                        case POST:
                        case SAYLOVE:
                        case CLASSMATEHELP:
                            if (builder != null) {
                                String temp = builder.toString();
                                if (!StringUtils.isEmpty(temp)) {
                                    temp = temp.substring(temp.lastIndexOf("?"),temp.length());
                                }
                                String qqUrl = App.getInstance().getQqShareUrl() + temp;
                                Logger.d("qqUrl === " + qqUrl);
                                paramsToShare.setUrl(qqUrl);
                                paramsToShare.setTitleUrl(qqUrl);
                                paramsToShare.setSiteUrl(qqUrl);
                            }
                            break;

                    }

                }else if(platform.getName().equals(QZone.NAME)){
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);

                    switch (type){
                        case AUTHORIZE:
                            paramsToShare.setTitleUrl(URL_AUTHORIZE_QQ);
                            paramsToShare.setSiteUrl(URL_AUTHORIZE_QQ);
                            break;


                        case POST:
                        case SAYLOVE:
                        case CLASSMATEHELP:
                            if (builder != null) {

                                String temp = builder.toString();
                                if (!StringUtils.isEmpty(temp)) {
                                    temp = temp.substring(temp.lastIndexOf("?"),temp.length());
                                }
                                String qqUrl = App.getInstance().getQqShareUrl() + temp;
                                Logger.d("qqUrl === " + qqUrl);
                                paramsToShare.setTitleUrl(qqUrl);
                                paramsToShare.setSiteUrl(qqUrl);

                            }
                            break;
                    }

                }
            }
        });

        this.isShareCustom=isShareCustom;
        if (isShareCustom) {
            // ------高级自定义功能：在分享菜单中加入自定义的按钮--------
            // 构造一个图标  注：自定义的图标必须和他们的图标大小一致否则显示有问题
            Bitmap logo = BitmapFactory.decodeResource(App.getInstance().getResources(), R.mipmap.share_friends_icon);
            // 定义图标的标签
            String label = "首页关注";
            // 图标点击后会通过Toast提示消息
            View.OnClickListener listener = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(App.getInstance(), ShareActivity.class);
                    if (object instanceof SayLoveInfo) {
                        SayLoveInfo sayLoveInfo = (SayLoveInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, sayLoveInfo);
                    } else if (object instanceof PostInfo) {
                        PostInfo postInfo = (PostInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
                    } else if (object instanceof OrganizationInfo) {
                        OrganizationInfo organizationInfo = (OrganizationInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, organizationInfo);
                    } else if (object instanceof PlayInfo) {
                        PlayInfo playInfo = (PlayInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, playInfo);
                    } else if (object instanceof ActivityInfo) {
                        ActivityInfo activityInfo = (ActivityInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, activityInfo);
                    } else if (object instanceof RoomInfo) {
                        RoomInfo roomInfo = (RoomInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, roomInfo);
                    }
                    intent.putExtra(Constants.KEY.KEY_TYPE, type);
                    intent.putExtra(Constants.KEY.KEY_PARENT_ID, parentId);
                    intent.putExtra(Constants.KEY.KEY_DEFAULT_IMG, defaultImg);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getInstance().startActivity(intent);
                }
            };
            oks.setCustomerLogo(logo, label, listener);

            Bitmap logo2 = BitmapFactory.decodeResource(App.getInstance().getResources(), R.mipmap.share_friends_icon);
            // 定义图标的标签
            String label2 = "友伴好友";
            // 图标点击后会通过Toast提示消息
            View.OnClickListener listener2 = new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(App.getInstance(), SelectAddressBookActivity.class);
                    if (object instanceof SayLoveInfo) {
                        SayLoveInfo sayLoveInfo = (SayLoveInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, sayLoveInfo);
                    } else if (object instanceof PostInfo) {
                        PostInfo postInfo = (PostInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, postInfo);
                    } else if (object instanceof OrganizationInfo) {
                        OrganizationInfo organizationInfo = (OrganizationInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, organizationInfo);
                    } else if (object instanceof PlayInfo) {
                        PlayInfo playInfo = (PlayInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, playInfo);
                    } else if (object instanceof ActivityInfo) {
                        ActivityInfo activityInfo = (ActivityInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, activityInfo);
                    } else if (object instanceof RoomInfo) {
                        RoomInfo roomInfo = (RoomInfo) object;
                        intent.putExtra(Constants.KEY.KEY_OBJECT, roomInfo);
                    }
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App.getInstance().startActivity(intent);
                }

            };

            oks.setCustomerLogo(logo2,label2, listener2);
//             ------高级自定义功能：在分享菜单中加入自定义的按钮--------
            // 启动分享GUI
        }
        oks.show(App.getInstance());

    }

    @Override
    public void updateView(String apiName, Object object) {
        if(apiName.equals(InterfaceUrl.URL_SAVESHARENUMBER)) {
            if(object instanceof NoParameterResponse){
                NoParameterResponse baseResponse= (NoParameterResponse) object;
            }
        }
    }

    public String getTitle(String title){
        if (!StringUtils.isEmpty(title)) {
            return title;//确保网络上存在此张图片
        } else {
            return "来自：友伴";//确保网络上存在此张图片
        }
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ShareSuccessEvent event) {
        if (event == null) {
            return;
        }
//
//        popupWindow.showAtLocation(context.getCurrentFocus(), Gravity.TOP,0,0);
//        new Handler().postDelayed(new Runnable() {
//                    @Override
//             public void run() {
//                 popupWindow.dismiss();
//             }
//        },1500);
        if(type==CLASSMATEHELP){
            EventBus.getDefault().post(new ShareNumUpdateEvent(type));//本地更新分享次数
            getSharePresenter().saveShareNumber(userId, parentId, type);
        }
        if(isShareCustom){
            EventBus.getDefault().post(new ShareNumUpdateEvent(type));//本地更新分享次数
            getSharePresenter().saveShareNumber(userId, parentId, type);
        }
        EventBus.getDefault().unregister(this);
    }


//    public void shareToQq(Context context) {
//        ShareSDK.initSDK(context);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle(shareTitle);
//        sp.setTitleUrl(URL_VMEET); // 标题的超链接
//        sp.setText(shareContent);
//        sp.setImageUrl(getShareUrl(4, "", ""));
//
//        Platform qq = ShareSDK.getPlatform(QQ.NAME);
////        qzone. setPlatformActionListener (paListener); // 设置分享事件回调
//        // 执行图文分享
//        qq.share(sp);
//    }
//
//    public void shareToQZone(Context context) {
//        ShareSDK.initSDK(context);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setTitle(shareTitle);
//        sp.setTitleUrl(URL_VMEET); // 标题的超链接
//        sp.setText(shareContent);
//        sp.setImageUrl(getShareUrl(4, "", ""));
//        sp.setSite(context.getString(R.string.app_name));
//        sp.setSiteUrl(URL_VMEET);
//
//        Platform qzone = ShareSDK.getPlatform(QZone.NAME);
////        qzone. setPlatformActionListener (paListener); // 设置分享事件回调
//        // 执行图文分享
//        qzone.share(sp);
//    }
//
//    public void shareWechat(Context context) {
//        ShareSDK.initSDK(context);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        sp.setTitle(shareTitle);
//        sp.setText(shareContent);
//        sp.setImageUrl(getShareUrl(4, "", ""));
//        sp.setUrl(URL_VMEET);
//        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
////        qzone. setPlatformActionListener (paListener); // 设置分享事件回调
//        // 执行图文分享
//        wechat.share(sp);
//    }
//
//    public void shareWechatMoments(Context context) {
//        ShareSDK.initSDK(context);
//        Platform.ShareParams sp = new Platform.ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        sp.setTitle(shareTitle);
//        sp.setText(shareContent);
//        sp.setImageUrl(getShareUrl(4, "", ""));
//        sp.setUrl(URL_VMEET);
//        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
////        qzone. setPlatformActionListener (paListener); // 设置分享事件回调
//        // 执行图文分享
//        wechatMoments.share(sp);
//    }


}
