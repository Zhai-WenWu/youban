package cn.bjhdltcdn.p2plive.ui.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * ToolBar 标题控件
 */
public class ToolBarFragment extends Fragment {

    private View rootView;
    private TextView leftView;
    private TextView titleView;
    private TextView rightView;
    private ImageView rightImageView;
    private Toolbar toolbar;

    public View getRootView() {
        return rootView;
    }

    public TextView getLeftView() {
        return leftView;
    }

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getRightView() {
        return rightView;
    }

    public ImageView getRightImageView() {
        return rightImageView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_tool_bar_layout, container, false);

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        leftView = rootView.findViewById(R.id.left_text_view);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        titleView = rootView.findViewById(R.id.title_text_view);
        rightView = rootView.findViewById(R.id.right_text_view);
        rightImageView = rootView.findViewById(R.id.right_image_view);
//        toolbar= rootView.findViewById(R.id.tool_bar);
    }

    /** 根据百分比改变颜色透明度 */
    public void changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        int backColor=Color.argb(alpha, red, green, blue);
//        toolbar.setBackgroundColor(backColor);
    }

    public void setBackGround(int color){
//        toolbar.setBackgroundColor(color);
    }



    public void setLeftView(String leftTitle, final ViewOnclick viewOnclick) {
        if (!StringUtils.isEmail(leftTitle)) {
            if (leftView != null) {
                leftView.setText(leftTitle);
            }
        }

        if (leftView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setLeftView(int leftTitle, final ViewOnclick viewOnclick) {
        if (leftTitle > 0) {
            if (leftView != null) {
                leftView.setText(getResources().getString(leftTitle));
            }
        }

        if (leftView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setLeftView(final ViewOnclick viewOnclick) {
        if (leftView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setLeftViewVisibility(int visibility) {
        if (leftView != null) {
            leftView.setVisibility(visibility);
        }

    }

    public void setLeftView(int leftTitle, int drawableRes, final ViewOnclick viewOnclick) {
        if (leftTitle > 0) {
            if (leftView != null) {
                leftView.setText(getResources().getString(leftTitle));
            }

            if (drawableRes > 0) {
                leftView.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0);
            }
        }

        if (leftView != null) {
            leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }


    public void setTitleView(String title) {
        if (titleView != null) {

            if (!StringUtils.isEmail(title)) {
                if (titleView != null) {
                    titleView.setText(title);
                }
            }
        }
    }

    public void setRightViewIsVisibility(int visibility) {
        if (rightView != null) {
            rightView.setVisibility(visibility);
        }
    }


    public void setRightView(String rightTitle, final ViewOnclick viewOnclick) {


        if (rightView != null) {

            if (!StringUtils.isEmail(rightTitle)) {
                if (rightView != null) {

                    rightView.setVisibility(View.VISIBLE);
                    rightImageView.setVisibility(View.GONE);

                    rightView.setText(rightTitle);
                }
            }

            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setRightView(String rightTitle, int colorId, final ViewOnclick viewOnclick) {


        if (rightView != null) {

            if (!StringUtils.isEmail(rightTitle)) {
                if (rightView != null) {

                    rightView.setVisibility(View.VISIBLE);
                    rightImageView.setVisibility(View.GONE);

                    rightView.setText(rightTitle);
                }
            }

            if (colorId > 0) {
                rightView.setTextColor(getResources().getColor(colorId));
            }

            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setRightView(int rightTitle, int colorId, final ViewOnclick viewOnclick) {


        if (rightView != null) {

            if (rightTitle > 0) {

                rightView.setVisibility(View.VISIBLE);
                rightImageView.setVisibility(View.GONE);

                if (rightView != null) {
                    rightView.setText(getResources().getString(rightTitle));
                }
            }

            if (colorId > 0) {
                rightView.setTextColor(getResources().getColor(colorId));
            }

            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    public void setRightView(int drawableRes, final ViewOnclick viewOnclick) {


        if (rightView != null) {

            if (drawableRes > 0) {

                rightView.setVisibility(View.GONE);
                rightImageView.setVisibility(View.VISIBLE);

                if (rightImageView != null) {
                    rightImageView.setImageResource(drawableRes);
                }
            }

            rightImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewOnclick != null) {
                        viewOnclick.onClick();
                    }
                }
            });
        }

    }

    /**
     * 两个按钮显示
     *
     * @param leftDrawableRes
     * @param righrDrawableRes
     * @param leftViewOnclick
     * @param rightViewOnclick
     */
    public void setDoubleRightView(int leftDrawableRes, int righrDrawableRes, final ViewOnclick leftViewOnclick, final ViewOnclick rightViewOnclick) {


        if (rightView != null) {

            if (leftDrawableRes > 0) {

                rightView.setVisibility(View.VISIBLE);

                if (rightView != null) {
                    rightView.setBackgroundResource(leftDrawableRes);
                }
            }
            if (righrDrawableRes > 0) {

                rightImageView.setVisibility(View.VISIBLE);

                if (rightImageView != null) {
                    rightImageView.setImageResource(righrDrawableRes);
                }
            }

            rightView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leftViewOnclick != null) {
                        leftViewOnclick.onClick();
                    }
                }
            });
            rightImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightViewOnclick != null) {
                        rightViewOnclick.onClick();
                    }
                }
            });
        }

    }


    public interface ViewOnclick {
        void onClick();
    }


}

