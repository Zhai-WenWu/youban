package cn.bjhdltcdn.p2plive.ui.dialog;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by huwenhua on 2016/7/7.
 */
public class RewardRedEnvelopeTipDialog extends DialogFragment {

    private View rootView;
    private ImageView imageView;
    private TextView titleText, tip1Text, tip2Text, tip3Text;
    private EditText moneyEditView;
    private Button sendMoneyView;
    private OnClickListener onClickListener;
    private Props props;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public RewardRedEnvelopeTipDialog() {
    }

    public void setProps(Props props) {
        this.props = props;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.reward_red_envelope_tip_dialog_layout, null);
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
        ImageView closeImage = (ImageView) rootView.findViewById(R.id.reward_tip_close_view);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismiss();
            }
        });
        imageView = (ImageView) rootView.findViewById(R.id.reward_image_view);
        titleText = (TextView) rootView.findViewById(R.id.reward_name_view);
        tip1Text = (TextView) rootView.findViewById(R.id.reward_jine_view);
        tip2Text = (TextView) rootView.findViewById(R.id.money_edit_view);
        tip3Text = (TextView) rootView.findViewById(R.id.reward_jinbi_view);
        moneyEditView = (EditText) rootView.findViewById(R.id.money_edit_view);
        sendMoneyView = (Button) rootView.findViewById(R.id.send_money_view);
        if (props != null) {
            titleText.setText(props.getName());
            if (props.getPropsId() == 77) {
                tip1Text.setText("数量:");
                tip2Text.setHint("填写数量");
                tip3Text.setText("邀客豆");
            }
            Utils.ImageViewDisplayByUrl(props.getUrl(), imageView);

        }
        sendMoneyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String goldNum = moneyEditView.getText().toString();
                    if (onClickListener != null && !TextUtils.isEmpty(goldNum)) {
                        int num = Integer.parseInt(goldNum);
                        if (num > 0) {
                            onClickListener.onClick(Integer.parseInt(goldNum));
                            onDismiss();
                        } else {
                            if (props.getPropsId() == 77) {
                                Utils.showToastShortTime("亲，数量太少啦！");
                            } else {
                                Utils.showToastShortTime("亲，金额太少啦！");
                            }
                        }

                    } else {
                        Utils.showToastShortTime("亲，不能为空哦！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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

    public interface OnClickListener {
        void onClick(int goldNum);
    }

}
