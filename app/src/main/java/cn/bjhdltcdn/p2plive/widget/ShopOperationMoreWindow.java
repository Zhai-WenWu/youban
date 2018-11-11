package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopCategoryRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.AssociationMoreDialog;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;

/**
 * Created by Hu_PC on 2018/5/17.
 */

public class ShopOperationMoreWindow extends PopupWindow {
    private ShopDetailActivity context;
    private View conentView;
    private View backgroundView;
    int[] location = new int[2];
    private View outView;
    private ShopOperationMoreWindow.ItemClick itemClick;

    private String[] operationStr=new String[]{};
    private TextView operationOne;

    public void setItemClick(ShopOperationMoreWindow.ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public interface OnItemListener {
        public void OnItemListener(int position, String typeSelect);
    }

    public void setOperationStr(String[] operationStr) {
        this.operationStr = operationStr;
        for (int i=0;i<operationStr.length;i++){
            if(i==0){
                operationOne.setText(operationStr[i]);
                if(operationStr[i].equals("编辑")){
                    operationOne.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.store_edit_icon), null, null, null);
                }else if(operationStr[i].equals("举报")){
                    operationOne.setCompoundDrawablesWithIntrinsicBounds(App.getInstance().getResources().getDrawable(R.mipmap.store_report_icon), null, null, null);
                }
            }
        }
    }

    public ShopOperationMoreWindow(ShopDetailActivity context) {
        this.context = context;
        initView();
    }

    private void initView() {
//        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.popshow_operation_anim_up);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.dialog_association_more, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
//         设置SelectPicPopupWindow弹出窗体动画效果
//        this.setAnimationStyle(R.style.operation_popwindow_anim_style_up);
        outView=conentView.findViewById(R.id.out_layout);
        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        operationOne=conentView.findViewById(R.id.tv_association_info);
        // 圈子资料
        operationOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClick != null) {
                    itemClick.itemClick(operationOne.getText().toString());
                }
                dismiss();
            }
        });


    }

    /**
     * 没有半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        showAsDropDown(v);
    }


    public interface ItemClick {
        void itemClick(String text);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect rect = new Rect();
            anchor.getGlobalVisibleRect(rect);
            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
            setHeight(h);
        }
        super.showAsDropDown(anchor);
    }

}
