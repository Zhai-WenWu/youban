package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * 视频接受请求通话UI
 */
public class CallRequestDialog extends DialogFragment {

    private View rootView;
    private static CallRequestDialog instance;
    private String roomNumber;
    private BaseUser baseUser;


    public synchronized static CallRequestDialog getInstance(String roomNumber, BaseUser baseUser) {
        if (instance == null) {
            instance = new CallRequestDialog();
        }

        if (instance.isVisible()) {
            instance.dismiss();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.Fields.EXTRA, baseUser);
        bundle.putString(Constants.Fields.ROOMNUMBER, roomNumber);
        instance.setArguments(bundle);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.call_request_dialog_layout, null);
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

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
                            dismiss();
                        }
                        rect.setEmpty();
                        rect = null;
                    }
                }
                return false;
            }
        });

        baseUser = getArguments().getParcelable(Constants.Fields.EXTRA);
        roomNumber = getArguments().getString(Constants.Fields.ROOMNUMBER, "");

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (baseUser == null) {
            dismiss();
            return;
        }

        try {
            //头像
            ImageView userImageView = (ImageView) rootView.findViewById(R.id.user_image_view);
            Utils.ImageViewDisplayByUrl(baseUser.getUserIcon(), userImageView);

            //昵称
            TextView nickNameView = (TextView) rootView.findViewById(R.id.user_name_view);
            nickNameView.setText(baseUser.getNickName());

            // 性别
            TextView userAgeView = (TextView) rootView.findViewById(R.id.user_age_view);
            String sexText = "男";
            if (baseUser.getSex() == 1) {
                sexText = "女";
            }

            if (userAgeView != null) {
                userAgeView.setText(sexText + " " + baseUser.getAge() + "岁 " + baseUser.getLocation());
            }

            Button cancelView = (Button) rootView.findViewById(R.id.cancel_view);
            cancelView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
