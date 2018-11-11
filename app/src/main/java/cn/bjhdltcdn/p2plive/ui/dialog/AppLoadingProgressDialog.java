package cn.bjhdltcdn.p2plive.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by xiawenquan on 17/11/28.
 */

public class AppLoadingProgressDialog extends ProgressDialog {

    private TextView tvProgressTitle;

    public AppLoadingProgressDialog(Context context) {
        super(context);
    }

    public AppLoadingProgressDialog(Context context, int theme) {
        super(context, theme);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void setProgressTitle(String progresstitle) {
        tvProgressTitle.setText(progresstitle);
    }

    private void init() {
        // 允许按返回键
        setCancelable(true);
        // 不允许点击加载框以外的区域取消来关闭
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.app_loading_layout);//loading的xml文件
        tvProgressTitle = findViewById(R.id.tv_load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);



    }

    @Override
    public void show() {//开启
        super.show();
    }

    @Override
    public void dismiss() {//关闭
        super.dismiss();
    }
}
