//package cn.bjhdltcdn.p2plive.handler;
//
//import android.app.Activity;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//
//import java.lang.ref.WeakReference;
//
//import cn.bjhdltcdn.p2plive.ui.activity.ActiveDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.BaseActivity;
//
///**
// * Created by huwenhua on 2016/10/13.
// */
//
//public class AdvertisementHandlerInActivity extends Handler {
//    /**
//     * 请求更新显示的View。
//     */
//    public static final int MSG_UPDATE_IMAGE  = 1;
//    /**
//     * 请求暂停轮播。
//     */
//    public static final int MSG_KEEP_SILENT   = 2;
//    /**
//     * 请求恢复轮播。
//     */
//    public static final int MSG_BREAK_SILENT  = 3;
//    /**
//     * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
//     * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
//     * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
//     */
//    public static final int MSG_PAGE_CHANGED  = 4;
//
//    //轮播间隔时间
//    public static final long MSG_DELAY = 5000;
//
//    //使用弱引用避免Handler泄露.这里的泛型参数可以不是Activity，也可以是Fragment等
//    private WeakReference<BaseActivity> weakReference;
//    private int SquareCurrentItem = 0;
//    private int FocusOnCurrentItem = 0;
//    ActiveDetailActivity activeDetailActivity = null;
//    AssociationDetailActivity associationDetailActivity=null;
//
//    public AdvertisementHandlerInActivity(WeakReference<BaseActivity> wk){
//        weakReference = wk;
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        Log.d("advertisementHandler", "receive message " + msg.what);
//        Activity baseActivity = weakReference.get();
//        if (baseActivity==null){
//            //Activity已经回收，无需再处理UI了
//            return ;
//        }
//
//        if(baseActivity instanceof ActiveDetailActivity){
//            activeDetailActivity=(ActiveDetailActivity)baseActivity;
//            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
//            if (activeDetailActivity.handler.hasMessages(MSG_UPDATE_IMAGE)){
//                activeDetailActivity.handler.removeMessages(MSG_UPDATE_IMAGE);
//            }
//            switch (msg.what) {
//                case MSG_UPDATE_IMAGE:
//                    SquareCurrentItem++;
//                    Log.d("advertisementHandler", "SquareCurrentItem=" + SquareCurrentItem);
//                    activeDetailActivity.viewPager.setCurrentItem(SquareCurrentItem);
//                    Log.d("advertisementHandler", "准备下次播放");
////                    //准备下次播放
////                    activ备下次播放
//                    activeDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_KEEP_SILENT:
//                    //只要不发送消息就暂停了
//                    break;
//                case MSG_BREAK_SILENT:
//                    activeDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_PAGE_CHANGED:
//                    //记录当前的页号，避免播放的时候页面显示不正确。
//                    SquareCurrentItem = msg.arg1;
//                    //准备下次播放
////                    activeDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                default:
//                    break;
//            }
//        }else if(baseActivity instanceof AssociationDetailActivity){
//            associationDetailActivity=(AssociationDetailActivity)baseActivity;
//            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
//            if (associationDetailActivity.handler.hasMessages(MSG_UPDATE_IMAGE)){
//                associationDetailActivity.handler.removeMessages(MSG_UPDATE_IMAGE);
//            }
//            switch (msg.what) {
//                case MSG_UPDATE_IMAGE:
//                    FocusOnCurrentItem++;
//                    Log.d("advertisementHandler", "FocusOnCurrentItem=" + FocusOnCurrentItem);
//                    associationDetailActivity.viewPager.setCurrentItem(FocusOnCurrentItem);
//                    Log.d("advertisementHandler", "准备下次播放"+associationDetailActivity.viewPager.getCurrentItem());
////                    //准备下次播放
//                    associationDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_KEEP_SILENT:
//                    //只要不发送消息就暂停了
//                    break;
//                case MSG_BREAK_SILENT:
//                    associationDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                case MSG_PAGE_CHANGED:
//                    //记录当前的页号，避免播放的时候页面显示不正确。
//                    FocusOnCurrentItem = msg.arg1;
//                    //准备下次播放
////                    associationDetailActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//    }
//
//    public void clearData(){
//        if (weakReference != null) {
//            weakReference.clear();
//            weakReference = null;
//        }
//
//        if (activeDetailActivity != null) {
//            activeDetailActivity = null;
//        }
//
//        if (associationDetailActivity != null) {
//            associationDetailActivity = null;
//        }
//
//    }
//
//}
