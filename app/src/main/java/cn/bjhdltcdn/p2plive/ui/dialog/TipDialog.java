package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by huwenhua on 2016/1/22.
 */
public class TipDialog extends DialogFragment {

    private View rootView;
    private TextView titleText,contentText;
    private Button okBtn;
    private Handler handler ;

    private String flag ;
    private boolean isExist ;
    private int position ;

    private String title,content,okStr;



    public void setTitle(String title) {
        if(!StringUtils.isEmpty(title)){
            this.title = title;
        }
    }

    public void setContent(String content) {
        if(!StringUtils.isEmpty(content)){
        this.content = content;
        }
    }

    public void setOkStr(String okStr) {
        if(!StringUtils.isEmpty(okStr)){
        this.okStr = okStr;
    }
    }



    public TipDialog(){}

    public void setString(String flag, boolean isExist, int position) {
        this.flag = flag;
        this.isExist = isExist ;
        this.position = position ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tip_dialog_layout, null);
        initView();
        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            onDismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    private void initView() {
        titleText = (TextView) rootView.findViewById(R.id.title_text_view);
        if(!StringUtils.isEmpty(title)){
            titleText.setText(title);
        }
        contentText = (TextView) rootView.findViewById(R.id.content_text_view);
        if(!StringUtils.isEmpty(content)){
            contentText.setText(content);
        }

        okBtn = (Button) rootView.findViewById(R.id.ok_view);
        if(!StringUtils.isEmpty(okStr)){
            okBtn.setText(okStr);
        }

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                getActivity().finish();
                EventBus.getDefault().post(new ClosePayActivityEvent());
            }
        });

    }

    private void onDismiss() {

        if (okBtn != null) {
            okBtn = null;
        }

        if (rootView != null) {
            rootView = null;
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        handler = null ;

        dismiss();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void dismiss() {
        try {
            super.dismissAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            super.dismiss();
        }
    }

    public void show(FragmentManager manager) {
        show(manager, "dialog");
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (Exception e) {
            e.printStackTrace();
            // 解决java.lang.IllegalStateException: Can not perform this action问题
            final FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        }
    }


}
