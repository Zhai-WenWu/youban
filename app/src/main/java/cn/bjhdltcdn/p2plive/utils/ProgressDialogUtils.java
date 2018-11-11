package cn.bjhdltcdn.p2plive.utils;

import android.app.Activity;
import android.content.DialogInterface;

import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.ui.activity.LoginActivity;
import cn.bjhdltcdn.p2plive.ui.activity.MainActivity;
import cn.bjhdltcdn.p2plive.ui.dialog.AppLoadingProgressDialog;

/**
 * 加载框的样式
 */

public class ProgressDialogUtils {

    private static ProgressDialogUtils instance;
    private AppLoadingProgressDialog progressDialog;

    public static  ProgressDialogUtils getInstance() {
        if (instance == null) {
            instance = new ProgressDialogUtils();
        }
        return instance;
    }


    /**
     * 显示加载框
     * @param activity 对应的页面
     */
    public void showProgressDialog(final Activity activity){

        synchronized (instance) {
            if ( ! (activity instanceof Activity)) {
                return;
            }

            if (activity == null || activity.isFinishing() ) {
                return;
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }


            if (progressDialog == null) {
                progressDialog = new AppLoadingProgressDialog(activity, R.style.progressDialog);
            }

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Logger.d("按返回按钮关闭对话框");
                    hideProgressDialog();
                    if (!(activity instanceof MainActivity) && !(activity instanceof LoginActivity)) {
                        activity.finish();
                    }

                }
            });

            if(!activity.isFinishing()) {
                progressDialog.show();
            }
        }



    }

    /**
     * 显示加载框
     * @param activity 对应的页面
     * @param listener 返回监听器
     */
    public void showProgressDialog(final Activity activity, final DialogInterface.OnCancelListener listener){

        synchronized (instance) {
            if (activity == null || activity.isFinishing()) {
                return;
            }

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

            progressDialog = new AppLoadingProgressDialog(activity, R.style.progressDialog);

            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (listener != null) {
                        listener.onCancel(dialog);
                    }

                    Logger.d("按返回按钮关闭对话框");
                    hideProgressDialog();
                    if (!(activity instanceof MainActivity) && !(activity instanceof LoginActivity)) {
                        activity.finish();
                    }
                }
            });

            progressDialog.show();
        }


    }

    /**
     * 隐藏加载框
     */
    public void hideProgressDialog(){

        synchronized (instance) {
            if (progressDialog == null) {
                return;
            }

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }



    }

    public void onDestroy(){
        if (progressDialog != null && progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.onDetachedFromWindow();
            progressDialog = null;
        }

        if (instance != null) {
            instance = null;
        }
    }


}
