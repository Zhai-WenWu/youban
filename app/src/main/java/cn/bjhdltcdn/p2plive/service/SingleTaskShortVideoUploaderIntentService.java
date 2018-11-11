package cn.bjhdltcdn.p2plive.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.upyun.library.common.Params;
import com.upyun.library.common.UploadEngine;
import com.upyun.library.listener.UpCompleteListener;
import com.upyun.library.listener.UpProgressListener;
import com.upyun.library.utils.UpYunUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.BuildConfig;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.FindListEvent;
import cn.bjhdltcdn.p2plive.event.UpdateActiveListEvent;
import cn.bjhdltcdn.p2plive.event.VideoCommentFinishEvent;
import cn.bjhdltcdn.p2plive.event.VideoEvaluateEvent;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SavePlayInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.savePostResponse;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PostLabelInfo;
import cn.bjhdltcdn.p2plive.model.SaveLaunchPlayResponse;
import cn.bjhdltcdn.p2plive.model.UpyunModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ClassMateHelpPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.OrderPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.PostCommentListPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.SaveSayLovePresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveEditTipDialog;
import cn.bjhdltcdn.p2plive.utils.DateUtils;
import cn.bjhdltcdn.p2plive.utils.NetUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import okhttp3.Response;

/**
 * 短视频单任务服务
 */
public class SingleTaskShortVideoUploaderIntentService extends IntentService implements BaseView {
    private AssociationPresenter associationPresenter;
    /**
     * 1 活动 ；2 表白墙 ； 3 同学帮帮忙 ；4 选择圈子 ; 5 pk视频 ；6评论 ; 8 订单评价
     */
    private int enterTypeMode;
    private ClassMateHelpPresenter classMateHelpPresenter;
    // 跟拍需要用
    private long parentId;


    /**
     * 标签列表
     */
    private List<PostLabelInfo> labelList;

    /**
     * 店铺id
     */
    private long storeId;
    /**
     * 是否为招聘信息
     */
    private int isRecurit;
    private OrderPresenter orderPresenter;


    public AssociationPresenter getAssociationPresenter() {
        if (associationPresenter == null) {
            associationPresenter = new AssociationPresenter(this);
        }
        return associationPresenter;
    }

    public ClassMateHelpPresenter getClassMateHelpPresenter() {
        if (classMateHelpPresenter == null) {
            classMateHelpPresenter = new ClassMateHelpPresenter(this);
        }
        return classMateHelpPresenter;
    }

    private SaveSayLovePresenter presenter;

    public SaveSayLovePresenter getPresenter() {
        if (presenter == null) {
            presenter = new SaveSayLovePresenter(this);
        }
        return presenter;
    }

    private DiscoverPresenter discoverPresenter;

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    private PostCommentListPresenter postPresenter;

    private PostCommentListPresenter getPostPresenter() {

        if (postPresenter == null) {
            postPresenter = new PostCommentListPresenter(this);
        }
        return postPresenter;
    }

    private GetStoreListPresenter getStoreListPresenter;

    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    public OrderPresenter getOrderPresenter() {
        if (orderPresenter == null) {
            orderPresenter = new OrderPresenter(this);
        }
        return orderPresenter;
    }

    private AppCompatActivity mActivity;
    private long userId;
    private List<Integer> organIdList;
    private int isAnonymous = 2;// 是否匿名(1不匿名,2匿名),
    private String contentStr;

    private String videoPath;//视频路径
    private int imgWidth;
    private int imgHeight;
    private String uploadUrl;
    private String videoImagePath;// 视频封面
    //enterTypeMode=5 0：发起pk 1:参与pk enterTypeMode=6 模块id（帖子id，表白id，pkId，帮帮忙id）
    private long launchId = -1;
    // enterTypeMode=6 被评论的用id
    private long toUserId;
    //enterTypeMode=6 type(1帖子，2表白，3pk，4帮帮忙)
    private int type;
    private String titleStr;
    private String playTitleStr;

    /**
     * 选择的圈子列表
     */
    private List<OrganizationInfo> organList;


    public SingleTaskShortVideoUploaderIntentService() {
        super("SingleTaskShortVideoUploaderIntentService");

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_LOW);

            NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            manager.createNotificationChannel(channel);

            Notification notification = new Notification.Builder(this, "id").build();

            startForeground(1, notification);
        }


        Log.e("ShortVideoIntentService", "onCreate");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("ShortVideoIntentService", "onStart");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("ShortVideoIntentService", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 启动短视频上传业务
     *
     * @param context 上下文
     * @param map     参数对象
     */
    public static void startShortVideoUploader(Context context, LinkedHashMap<String, Object> map) {
        Intent intent = new Intent(context, SingleTaskShortVideoUploaderIntentService.class);
        intent.setAction(Constants.KEY.KEY_START_SINGLE_TASK_SHORT_VIDEO_UPLOAD);
        intent.putExtra(Constants.KEY.KEY_EXTRA_PARAM, map);
        if (Build.VERSION.SDK_INT >= 26) {
            App.getInstance().startForegroundService(intent);
        } else {
            // Pre-O behavior.
            App.getInstance().startService(intent);
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Constants.KEY.KEY_START_SINGLE_TASK_SHORT_VIDEO_UPLOAD.equals(action)) {
                Object object = intent.getSerializableExtra(Constants.KEY.KEY_EXTRA_PARAM);
                if (object instanceof Map) {
                    handleShortVideoUploaderService((Map<String, Object>) object);
                }

            }
        }
    }


    /**
     * 处理上传短视频业务
     *
     * @param map
     */
    private void handleShortVideoUploaderService(Map<String, Object> map) {

        Logger.d("开始上传视频任务..............................");

        if (map == null || map.isEmpty()) {
            Logger.d("参数为空..............................");
            return;
        }

        if (!NetUtils.isConnected(App.getInstance())) {
            Utils.showToastShortTime("网络不可用");
            return;
        }


        if (map.containsKey(Constants.Fields.VIDEO_PATH)) {
            Object object = map.get(Constants.Fields.VIDEO_PATH);
            if (object instanceof String) {
                videoPath = (String) object;
            }

        }

        if (map.containsKey(Constants.Fields.IMG_WIDTH)) {
            Object object = map.get(Constants.Fields.IMG_WIDTH);
            if (object instanceof Integer) {
                imgWidth = (Integer) object;
            }
        }

        if (map.containsKey(Constants.Fields.IMG_HEIGHT)) {
            Object object = map.get(Constants.Fields.IMG_HEIGHT);
            if (object instanceof Integer) {
                imgHeight = (Integer) object;
            }
        }


        if (map.containsKey(Constants.Fields.CONTENT_STR)) {
            Object object = map.get(Constants.Fields.CONTENT_STR);
            if (object instanceof String) {
                contentStr = (String) object;
            }

        }
        if (map.containsKey(Constants.Fields.IS_ANONYMOUS)) {
            Object object = map.get(Constants.Fields.IS_ANONYMOUS);
            if (object instanceof Integer) {
                isAnonymous = (Integer) object;
            }

        }

        if (map.containsKey(Constants.Fields.LAUNCH_ID)) {
            Object object = map.get(Constants.Fields.LAUNCH_ID);
            if (object instanceof Long) {
                launchId = (Long) object;
            }
        }

        if (map.containsKey(Constants.Fields.ENTER_TYPE_MODE)) {
            Object object = map.get(Constants.Fields.ENTER_TYPE_MODE);
            if (object instanceof Integer) {
                enterTypeMode = (int) object;
            }
        }

        if (map.containsKey(Constants.Fields.TO_USER_ID)) {
            Object object = map.get(Constants.Fields.TO_USER_ID);
            if (object instanceof Long) {
                toUserId = (Long) object;
            }
        }

        if (map.containsKey(Constants.Fields.POST_LABEL_LIST)) {
            Object object = map.get(Constants.Fields.POST_LABEL_LIST);
            if (object instanceof List) {
                labelList = (List<PostLabelInfo>) object;
            }
        }

        if (map.containsKey(Constants.Fields.PARENT_ID)) {
            Object object = map.get(Constants.Fields.PARENT_ID);
            if (object instanceof Integer) {
                parentId = (Long) object;
            } else if (object instanceof Long) {
                parentId = (Long) object;
            }

        }

        if (map.containsKey(Constants.Fields.ORGAN_LIST)) {
            Object object = map.get(Constants.Fields.ORGAN_LIST);
            if (object instanceof List) {
                organList = (List<OrganizationInfo>) object;
            }
        }


        if (map.containsKey(Constants.Fields.STORE_ID)) {
            Object object = map.get(Constants.Fields.STORE_ID);
            if (object instanceof Long) {
                storeId = (Long) object;
            }
        }
        if (map.containsKey(Constants.Fields.IS_RECURIT)) {
            Object object = map.get(Constants.Fields.IS_RECURIT);
            if (object instanceof Long) {
                isRecurit = (Integer) object;
            }
        }
        if (!StringUtils.isEmpty(videoPath)) {
            //表单上传
            final Map<String, Object> paramsMap = new HashMap<>();
            //上传空间
            paramsMap.put(Params.BUCKET, "video-hdlt-com");
            //保存路径，任选其中一个
            String saveUrl = null;


            switch (enterTypeMode) {
                case 1:


                    break;

                case 2:
                    //表白

                case 3:

                    break;

                case 4:

                    //帖子
                    organIdList = (List<Integer>) map.get(Constants.Fields.ORGAN_ID_LIST);
                    saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/post/" : "hdlt_youban_official/post/") + userId + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";

                    break;

                case 5:
                    titleStr = (String) map.get(Constants.Fields.TITLE_STR);
                    playTitleStr = (String) map.get(Constants.Fields.PLAY_TITLE_STR);
                    if (launchId >= 0) {
                        //pk
                        saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/pk/" : "hdlt_youban_official/pk/") + userId + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";
                    }
                    break;

                case 6://评论
                    toUserId = (long) map.get(Constants.Fields.TO_USER_ID);
                    type = (int) map.get(Constants.Fields.TYPE);
                    saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/comment/" : "hdlt_youban_official/comment/") + userId + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";
                    break;
                case 7://店铺
                    saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/store/" : "hdlt_youban_official/store/") + userId + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";
                    break;
                case 8://评价
                    saveUrl = "/uploads/" + (BuildConfig.LOG_DEBUG ? "hdlt_youban_test/store/" : "hdlt_youban_official/store/") + userId + "/" + DateUtils.getFormatDataString(new Date(), DateUtils.DATE_FORMAT_1) + "/" + System.currentTimeMillis() + ".mp4";
                    break;
                default:
            }


            paramsMap.put(Params.SAVE_KEY, saveUrl);

            //上传结果回调
            final String finalSaveUrl = saveUrl;
            UpCompleteListener completeListener = new UpCompleteListener() {
                @Override
                public void onComplete(boolean isSuccess, String result) {

                    try {
                        if (!StringUtils.isEmpty(result)) {
                            if (result.startsWith("{") && result.endsWith("}")) {
                                final UpyunModel upyunModel = RongIMutils.getMapper().readValue(result, UpyunModel.class);
//                                            KLog.d("upyunModel === " + upyunModel);
                                if (upyunModel != null) {
                                    if (upyunModel.getCode() == 200) {
                                        videoPath = Constants.KEY.UPAIYUN_HOST + upyunModel.getUrl();
                                        final String save_as = finalSaveUrl.replace(".mp4", ".png");
                                        uploadUrl = upyunModel.getUrl();
                                        getPresenter().upYunSnapshot(uploadUrl, save_as, "00:00:00", imgWidth, imgHeight);
                                    }
                                }
                            } else {
                                final String save_as = finalSaveUrl.replace(".mp4", ".png");
                                getPresenter().upYunSnapshot(uploadUrl, save_as, "00:00:01", imgWidth, imgHeight);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            //进度条回调
            UpProgressListener progressListener = new UpProgressListener() {
                @Override
                public void onRequestProgress(final long bytesWrite, final long contentLength) {
                    Logger.d((100 * bytesWrite) / contentLength + "%");
                }

            };

            UploadEngine.getInstance().formUpload(new File(videoPath), paramsMap, Constants.KEY.OPERATOR_NAME, UpYunUtils.md5(Constants.KEY.OPERATOR_PWD), completeListener, progressListener);


            Logger.d("结束上传任务..............................");
        }
    }


    @Override
    public void updateView(String apiName, Object object) {


        if (object instanceof Exception) {
            Exception e = (Exception) object;
            Utils.showToastShortTime(e.getMessage());
            return;
        }


        if (InterfaceUrl.URL_SAVEPOST.equals(apiName)) {
            if (object instanceof savePostResponse) {
                savePostResponse response = (savePostResponse) object;
                if (response.getCode() == 200) {
//                    Utils.showToastShortTime("您的视频帖子发布成功！");
                    EventBus.getDefault().post(new UpdateActiveListEvent(1));

                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAVELAUNCHPLAY)) {//发起pk
            if (object instanceof SaveLaunchPlayResponse) {
                SaveLaunchPlayResponse launchPlayResponse = (SaveLaunchPlayResponse) object;
                int code = launchPlayResponse.getCode();
                if (code == 200) {
                    ActiveEditTipDialog dialog = new ActiveEditTipDialog();
                    dialog.setTitle("视频上传完成!");
                    dialog.setItemText("您的PK挑战发起成功，快去邀请朋友们来为你点赞打call吧！", "我知道了");
                    AppCompatActivity activity = (AppCompatActivity) ((App) getApplicationContext()).getCurrentActivity();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    dialog.show(fragmentManager);
                    //更新发现列表
                    EventBus.getDefault().post(new FindListEvent(1));
                } else {
                    Utils.showToastShortTime(launchPlayResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_SAVEPLAYINFO)) {//参与pk
            if (object instanceof SavePlayInfoResponse) {
                SavePlayInfoResponse baseResponse = (SavePlayInfoResponse) object;
                int code = baseResponse.getCode();
                if (code == 200) {
                    ActiveEditTipDialog dialog = new ActiveEditTipDialog();
                    dialog.setTitle("视频上传完成!");
                    dialog.setItemText("您的PK挑战参赛成功，快去邀请朋友们来为你点赞打call吧！", "我知道了");
                    AppCompatActivity activity = (AppCompatActivity) ((App) getApplicationContext()).getCurrentActivity();
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    dialog.show(fragmentManager);
                } else {
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
            }
        } else if (apiName.equals(InterfaceUrl.URL_UPYUN_SNAPSHOT)) {
            if (object instanceof Response) {
                Response response = (Response) object;
                try {
                    String responseText = response.body().string();
                    if (!StringUtils.isEmpty(responseText)) {

                        final Map map = RongIMutils.getMapper().readValue(responseText, Map.class);
                        if (map != null && !map.isEmpty()) {

                            Object statusCode = map.get("status_code");
//                            KLog.d("statusCode === " + statusCode);

                            if (statusCode instanceof Integer) {
                                int code = ((Integer) statusCode).intValue();
                                if (code == 200) {
                                    final Object saveAs = map.get("save_as");
//                                    KLog.d("saveAs === " + saveAs);
                                    if (saveAs instanceof String) {
                                        videoImagePath = Constants.KEY.UPAIYUN_HOST + saveAs;


                                        switch (enterTypeMode) {
                                            case 4:
                                                // 发布帖子视频
                                                getAssociationPresenter().savePost(userId, toUserId, contentStr, isAnonymous, 2, videoPath, videoImagePath, labelList, false, parentId, storeId, isRecurit);
                                                break;
                                            case 6:
                                                getPostPresenter().commentUploadImage(userId, launchId, type, contentStr, 1,
                                                        isAnonymous, toUserId, userId, 0, 0, 2, videoPath,
                                                        videoImagePath, null, null);
                                            case 7:
                                                //上传店铺视频接口
                                                getGetStoreListPresenter().updateStoreVideo(userId, launchId, videoPath, videoImagePath);
                                                break;
                                            case 8:
                                                //上传评价视频接口
                                                EventBus.getDefault().post(new VideoEvaluateEvent(videoPath, videoImagePath));
                                                break;
                                            default:
                                        }

                                    }
                                } else if (code == 40100002) {// 当前手机时间错误

                                    Utils.showToastShortTime("当前手机时间错误");

                                } else {
                                    Utils.showToastShortTime((String) map.get("msg"));
                                }
                            } else {
                                Utils.showToastShortTime(responseText);
                            }
                        }

                    } else {
                        Utils.showToastShortTime("上传视频失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (apiName.equals(InterfaceUrl.URL_COMMENTUPLOADIMAGE)) {
            if (object instanceof PostCommentResponse) {
                PostCommentResponse postCommentResponse = (PostCommentResponse) object;
                Utils.showToastShortTime(postCommentResponse.getMsg());
                if (postCommentResponse.getCode() == 200) {
                    EventBus.getDefault().post(new VideoCommentFinishEvent(1));
                }
            }
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
