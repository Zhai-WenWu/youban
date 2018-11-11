package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.ui.adapter.GoodsOperationListAdapter;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by Hu_PC on 2018/5/17.
 */

public class CustomOperationPopWindow extends PopupWindow {
    private Context context;
    private View conentView;
    private RelativeLayout rootLayout;
    private View backgroundView;
    private Animation anim_backgroundView;
    private ListView listView;
    private GoodsOperationListAdapter selectAdapter;
    ImageView arrow_up, arrow_down;
    List<String> typeSelectlist = new ArrayList<>();
    int[] location = new int[2];
    private OnItemListener onItemListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private int unEnablePosition=-1;
    private int productStatus;//商品状态

    public interface OnItemListener {
        public void OnItemListener(int position, String typeSelect);
    }

    public void setOnItemMyListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public CustomOperationPopWindow(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
//        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.popshow_operation_anim_up);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.goods_operation_popwindow_layout, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(Utils.dp2px(145));
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
//         设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
        this.rootLayout= (RelativeLayout) conentView.findViewById(R.id.root_layout);
        this.listView = (ListView) conentView.findViewById(R.id.lv_list);
        this.arrow_up = (ImageView) conentView.findViewById(R.id.arrow_up);
        this.arrow_down = (ImageView) conentView.findViewById(R.id.arrow_down);

        //设置适配器
        this.selectAdapter = new GoodsOperationListAdapter();
        this.listView.setAdapter(selectAdapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(productStatus==1){
                    //下架商品  只能编辑
                    if(position==0){
                        if (isShowing()) {
                            dismiss();
                        }
                        onItemListener.OnItemListener(position, selectAdapter.getItem(position));
                    }
                }else {
                    if (unEnablePosition != position) {
                        if (isShowing()) {
                            dismiss();
                        }
                        onItemListener.OnItemListener(position, selectAdapter.getItem(position));
                    }
                }

            }
        });
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (backgroundView != null) {
                    backgroundView.setVisibility(View.GONE);
                }
            }
        });
    }

    //设置数据
    public void setDataSource(List<String> typeSelectlist,int unEnablePosition,int productStatus) {
        this.typeSelectlist = typeSelectlist;
        this.unEnablePosition=unEnablePosition;
        this.productStatus=productStatus;
        this.selectAdapter.setUnEnablePosition(unEnablePosition);
        this.selectAdapter.setProductStatus(productStatus);
        this.selectAdapter.setList(typeSelectlist);
        this.selectAdapter.notifyDataSetChanged();
    }


    /**
     * 没有半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        v.getLocationOnScreen(location);  //获取控件的位置坐标
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        if (location[1] > PlatformInfoUtils.getWidthOrHeight(context)[1] / 2 ) {  //MainApplication.SCREEN_H 为屏幕的高度，方法可以自己写
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
//            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight());
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0])-(Utils.dp2px(145)-v.getMeasuredWidth()), location[1] -Utils.dp2px(51*4+3)-Utils.dp2px(10));
            this.conentView.setBackground(App.getInstance().getDrawable(R.drawable.goods_operation_pop_top_bg));
//            this.rootLayout.setBackgroundResource(R.drawable.goods_operation_pop_top_bg);
        } else {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);
            this.showAsDropDown(v, -(Utils.dp2px(145)-v.getMeasuredWidth()), 0);
            this.conentView.setBackground(App.getInstance().getDrawable(R.drawable.goods_operation_pop_botom_bg));
//            this.rootLayout.setBackgroundResource(R.drawable.goods_operation_pop_botom_bg);
        }
    }

    /**
     * 携带半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location);  //获取控件的位置坐标
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //对view执行动画
//        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > PlatformInfoUtils.getWidthOrHeight(context)[1] / 2 + 100) {  //若是控件的y轴位置大于屏幕高度的一半，向上弹出
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
            arrow_up.setVisibility(View.GONE);
            arrow_down.setVisibility(View.VISIBLE);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight());  //显示指定控件的上方
        } else {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);  //反之向下弹出
            arrow_up.setVisibility(View.VISIBLE);
            arrow_down.setVisibility(View.GONE);
            this.showAsDropDown(v, 0, 0);    //显示指定控件的下方
        }
    }
    /**
     * 显示popupWindow  根据特殊要求高度显示位置
     *
     * @param
     */
    public void showPopupWindow(View v, View backgroundView,int hight) {
        this.backgroundView = backgroundView;
        v.getLocationOnScreen(location);
        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        backgroundView.setVisibility(View.VISIBLE);
        //对view执行动画
//        backgroundView.startAnimation(anim_backgroundView);
        if (location[1] > PlatformInfoUtils.getWidthOrHeight(context)[1]/ 2 + 100) {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
            arrow_up.setVisibility(View.GONE);
            arrow_down.setVisibility(View.VISIBLE);
            this.showAtLocation(v, Gravity.NO_GRAVITY, (location[0]), location[1] - conentView.getMeasuredHeight()-hight);
        } else {
            this.setAnimationStyle(R.style.operation_popwindow_anim_style_down);
            arrow_up.setVisibility(View.VISIBLE);
            arrow_down.setVisibility(View.GONE);
            this.showAsDropDown(v, 0, 0);
        }
    }
}
