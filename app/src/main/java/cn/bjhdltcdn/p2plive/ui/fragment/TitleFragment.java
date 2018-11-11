package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.utils.Utils;


/**
 * 标题fragment
 */

public class TitleFragment extends Fragment {

    private View rootView;
    private TextView leftView;
    private TextView titleView;
    private TextView rightView,rightTwoView;
    private TextView tv_num_pop;
    private View redView, lineView,rightPopView;
    private ImageView leftImageView,rightImageView;

    private View viewLine;
    private RelativeLayout rootLayout;
    private final boolean[] topShow = {false};
    private final boolean[] bottomShow = { true };

    public TextView getTitleView() {
        return titleView;
    }

    public TextView getRightView() {
        return rightView;
    }

    public TextView getLeftView() {
        return leftView;
    }

    public View getRootView() {
        return rootView;
    }

    public View getViewLine() {
        return viewLine;
    }

    public void setViewLine(View viewLine) {
        this.viewLine = viewLine;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_title, container, false);
        rootLayout= (RelativeLayout) rootView.findViewById(R.id.root_layout);
        leftImageView = (ImageView) rootView.findViewById(R.id.back_img_view);
        rightImageView = (ImageView) rootView.findViewById(R.id.more_img_view);
        leftView = (TextView) rootView.findViewById(R.id.left_text_view);
        tv_num_pop = (TextView) rootView.findViewById(R.id.tv_num_pop);
        titleView = (TextView) rootView.findViewById(R.id.tile_text_view);
        rightView = (TextView) rootView.findViewById(R.id.right_text_view);
        rightTwoView= (TextView) rootView.findViewById(R.id.right_two_text_view);
        lineView = rootView.findViewById(R.id.view_line);
        rightPopView= rootView.findViewById(R.id.right_view_num_pop);

        viewLine = rootView.findViewById(R.id.view_line);

        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /** 根据百分比改变颜色透明度 */
    public void changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        int backColor=Color.argb(alpha, red, green, blue);
        rootLayout.setBackgroundColor(backColor);
    }

    public void setBackGround(int color){
        rootLayout.setBackgroundColor(getResources().getColor(color));
        viewLine.setVisibility(View.GONE);
    }


    public void setLeftImageViewVisible(int Visible) {
        leftImageView.setVisibility(Visible);
    }

    public void setRightImageViewVisible(int Visible) {
        rightImageView.setVisibility(Visible);
    }

    public void setLeftImageView(int drawableId,final LeftViewClick click) {
        leftImageView.setImageResource(drawableId);
        leftImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onBackClick();
            }
        });
    }

    public void setRightImageView(int drawableId,final RightViewClick click) {
        rightImageView.setImageResource(drawableId);
        rightImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }

    public void setAlpha(int scrollY, float scrollDistance){
        if(scrollY>scrollDistance){
           return;
        }
        changeAlpha(getResources().getColor(R.color.color_ffffff),scrollY/scrollDistance);
        if(scrollY<=scrollDistance/2){
            if(topShow[0]){
                leftImageView.setImageResource(R.mipmap.shop_detail_back_icon);
                rightImageView.setImageResource(R.mipmap.shop_detail_share_icon);
                topShow[0] =false;
                bottomShow[0] =true;
            }
            leftImageView.setAlpha(1-scrollY/(scrollDistance/2));//(透明度从1到0)
            rightImageView.setAlpha(1-scrollY/(scrollDistance/2));
        }else{
            if(bottomShow[0]){
                leftImageView.setImageResource(R.mipmap.store_detail_back_icon);
                rightImageView.setImageResource(R.mipmap.store_detail_share_icon);
                bottomShow[0] =false;
                topShow[0] =true;
            }
            leftImageView.setAlpha((scrollY-(scrollDistance/2))/(scrollDistance/2));//(透明度从0到1)
            rightImageView.setAlpha((scrollY-(scrollDistance/2))/(scrollDistance/2));
        }
        if(scrollY>=scrollDistance){
            viewLine.setVisibility(View.VISIBLE);
        }else{
            viewLine.setVisibility(View.GONE);
        }
    }



    public void setPopNum(int number) {
        if (number > 0) {
            tv_num_pop.setVisibility(View.VISIBLE);
            String str;
            str = String.valueOf(number);
            if (number > 99) {
                str = "99+";
            }
            tv_num_pop.setText(str);
        } else {
            tv_num_pop.setVisibility(View.GONE);
        }
    }

    public void setRightPopViewShow(boolean isShow) {
        if(isShow){
            rightPopView.setVisibility(View.VISIBLE);
        }else{
            rightPopView.setVisibility(View.GONE);
        }
    }

    public void setLeftViewTitle(int drawableId, String s, final LeftViewClick click) {
        leftView.setText(s);
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onBackClick();
            }
        });
    }

    public void setLeftViewTitle(int drawableId, int s, final LeftViewClick click) {
        leftView.setText(s);
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onBackClick();
            }
        });

    }

    public void setLeftViewTitle(int drawableId,final LeftViewClick click) {
        leftView.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onBackClick();
            }
        });

    }

    public void setLeftViewTitle(String s, final LeftViewClick click) {
        leftView.setText(s);
        leftView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        leftView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onBackClick();
            }
        });
    }

    public void setRightViewTitle(final int drawableId, int id, final RightViewClick click) {
        rightView.setText(id);
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }

    public void setRightViewTitle(final int drawableId, String s, final RightViewClick click) {
        rightView.setText(s);
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }

    public void setRightTwoViewTitle(final int drawableId, final RightTwoViewClick click) {
        rightTwoView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        rightTwoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }

    public void setHeight(int height){
        rootView.getLayoutParams().height= Utils.dp2px(height);
    }


    public void setRightViewTitle(String s, final RightViewClick click) {
        rightView.setText(s);
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }

    public void setRightViewTitle(String s, int drawableId){
        rightView.setText(s);
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
    }
    public void setRightViewTitle(int drawableId){
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
    }

    public void setRightViewTitle(String s){
        rightView.setText(s);
    }
    public void setRightViewColor(int color){
        rightView.setTextColor(getResources().getColor(color));
    }


    public void setRightViewTitle(int drawableId, final RightViewClick click) {
        rightView.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }


    public void setRightViewTitle(final RightViewClick click){
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == null) {
                    return;
                }
                click.onClick();
            }
        });
    }


    public void setBackground(int ids) {
        rootView.setBackgroundResource(ids);
    }

    public void setBackground(Drawable background) {
        rootView.setBackgroundDrawable(background);
    }

    public void setTitle(int titleId) {
        titleView.setText(titleId);
//        titleView.setTypeface(App.getInstance().getTypeface());
    }


    public void setTitle(String title) {
        titleView.setText(title);
//        titleView.setTypeface(App.getInstance().getTypeface());
    }

    public void setTitle(int titleId, int color) {
        titleView.setText(titleId);
//        titleView.setTypeface(App.getInstance().getTypeface());
        titleView.setTextColor(getResources().getColor(color));
    }

    public void setTitle(String title, int color) {
        titleView.setText(title);
//        titleView.setTypeface(App.getInstance().getTypeface());
        titleView.setTextColor(getResources().getColor(color));
    }

    public void setLineViewVisibile(int visibile) {
        lineView.setVisibility(visibile);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    public interface LeftViewClick {
        void onBackClick();
    }

    public interface RightViewClick {
        void onClick();
    }

    public interface RightTwoViewClick {
        void onClick();
    }

}
