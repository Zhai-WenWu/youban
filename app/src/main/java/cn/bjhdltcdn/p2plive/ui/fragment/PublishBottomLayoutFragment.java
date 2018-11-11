package cn.bjhdltcdn.p2plive.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;

/**
 * Created by xiawenquan on 18/3/1.
 * 发布底部按钮
 */

public class PublishBottomLayoutFragment extends BaseFragment {

    private View rootView;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private Switch mSwitch;

    public void setOnCheckedChangeListener(OnCheckedChangeListener mOnCheckedChangeListener) {
        this.mOnCheckedChangeListener = mOnCheckedChangeListener;
    }

    private MultimediaItemOnClickListener multimediaItemOnClickListener;

    public void setMultimediaItemOnClickListener(MultimediaItemOnClickListener multimediaItemOnClickListener) {
        this.multimediaItemOnClickListener = multimediaItemOnClickListener;
    }

    @Override
    protected void onVisible(boolean isInit) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.publish_bottom_layout, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 匿名按钮
        mSwitch = rootView.findViewById(R.id.anonymous_view);
        mSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChangedCallback(mSwitch, mSwitch.isChecked());
                }
            }
        });
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener.onCheckedChangedCallback(mSwitch, mSwitch.isChecked());
                }
            }
        });

        // 图片选择按钮
        rootView.findViewById(R.id.left_img_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multimediaItemOnClickListener != null) {
                    multimediaItemOnClickListener.onClickCallback(v, 1);
                }
            }
        });

        // 视频选择按钮
        rootView.findViewById(R.id.middle_img_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multimediaItemOnClickListener != null) {
                    multimediaItemOnClickListener.onClickCallback(v, 2);
                }
            }
        });


        // gif选择按钮
        rootView.findViewById(R.id.right_img_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (multimediaItemOnClickListener != null) {
                    multimediaItemOnClickListener.onClickCallback(v, 3);
                }
            }
        });

    }

    public Switch getSwitch() {
        return mSwitch;
    }

    /**
     * 设置匿名是否显示
     *
     * @param visibility
     */
    public void setSwitchIsVisibility(int visibility) {
        if (mSwitch != null) {
            mSwitch.setVisibility(visibility);
        }
    }

    /**
     * 设置匿名是否被选中
     *
     * @param checked
     */
    public void setSwitchIsChecked(boolean checked) {
        if (mSwitch != null) {
            mSwitch.setChecked(checked);
        }
    }

    public interface MultimediaItemOnClickListener {

        /**
         * 点击回调接口
         *
         * @param view 点击当前的view
         * @param type 1 图片 ；2 视频 ； 3 gif
         */
        void onClickCallback(View view, int type);

    }

    public interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChangedCallback(CompoundButton buttonView, boolean isChecked);
    }

}
