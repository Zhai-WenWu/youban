package cn.bjhdltcdn.p2plive.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by Hu_PC on 2018/1/25.
 */

public class ToastDialog extends Dialog {
    public Activity context;



    public ToastDialog(Activity context) {
        super(context);
        this.context = context;
    }

    public ToastDialog(Activity context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public ToastDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(context,R.layout.layout_share_success_tip,null);
        setContentView(view);

        setCanceledOnTouchOutside(false);

        Window win = getWindow();
        win.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        win.setGravity(Gravity.TOP);
        WindowManager.LayoutParams lp = win.getAttributes();
        win.setAttributes(lp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    dismiss();
            }
        },1500);

//       setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    context.finish();
//                    return true;
//                }
//                return false;
//            }
//        });

    }



}
