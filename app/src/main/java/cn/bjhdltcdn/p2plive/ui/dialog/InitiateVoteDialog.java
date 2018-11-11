package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;


/**
 * Created by huwenhua on 2016/10/26
 */
public class InitiateVoteDialog extends DialogFragment {

    private View rootView;
    private TextView initiateTextView,contentTextView;
    private ImageView closeView;
    private OnClickListener onClickListener;




    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public InitiateVoteDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_vote_pk, null);
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
        contentTextView= (TextView) rootView.findViewById(R.id.pk_tip_text_view);
        SpannableStringBuilder fansTextStyle = new SpannableStringBuilder();
        fansTextStyle.append("PK结束后才能查看结果，\n观战免费投票，或礼物\n100金币=1票");
        fansTextStyle.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 218, 68)), 24, 27, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fansTextStyle.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 77, 169)), 30, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        contentTextView.setText(fansTextStyle);
        closeView= (ImageView) rootView.findViewById(R.id.close_img_view);
        initiateTextView = (TextView) rootView.findViewById(R.id.open_pk_text_view);
        initiateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onOkClick();
                dismiss();
            }
        });
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void onDismiss() {


        if (rootView != null) {
            rootView = null;
        }


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

    public interface OnClickListener{
        void onOkClick();
    }

}
