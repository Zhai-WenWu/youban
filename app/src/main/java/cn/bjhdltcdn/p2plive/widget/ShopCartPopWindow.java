package cn.bjhdltcdn.p2plive.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.event.UpdateShopChartListEvent;
import cn.bjhdltcdn.p2plive.model.ProductDetail;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.ui.adapter.GoodsOperationListAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopCartRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.dialog.ActiveLaunchSuccessTipDialog;
import cn.bjhdltcdn.p2plive.utils.PlatformInfoUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.ViewSizeChangeAnimation;

/**
 * Created by Hu_PC on 2018/5/17.
 */

public class ShopCartPopWindow extends PopupWindow {
    private AppCompatActivity context;
    private View conentView;
    private View backgroundView;
    private RecyclerView goodsRecycleView;
    private TextView empty_tv;
    private ShopCartRecycleAdapter shopCartRecycleAdapter;
    private List<ProductDetail> list=new ArrayList<ProductDetail>();
    int[] location = new int[2];
    private OnItemListener onItemListener;
    private AdapterView.OnItemClickListener onItemClickListener;
    private View outView;
    private RelativeLayout topLayout;
    private TextView emptyTextView;
    int statusBarHeight1;

    public interface OnItemListener {
        public void OnItemListener(int position, String typeSelect);
    }

    ;

    public void setOnItemMyListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public ShopCartPopWindow(AppCompatActivity context) {
        this.context = context;
        initView();
    }

    private void initView() {
//        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.popshow_operation_anim_up);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.dialog_shopping_cart_layout, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
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
        this.goodsRecycleView = (RecyclerView) conentView.findViewById(R.id.goods_recycler_view);
        outView=conentView.findViewById(R.id.out_layout);
        topLayout=conentView.findViewById(R.id.top_layout);
        emptyTextView=conentView.findViewById(R.id.tv_empty);
        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //设置适配器
        this.shopCartRecycleAdapter = new ShopCartRecycleAdapter(context);
        goodsRecycleView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setAutoMeasureEnabled(true);
        goodsRecycleView.setLayoutManager(layoutManager);
        goodsRecycleView.setAdapter(shopCartRecycleAdapter);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) goodsRecycleView.getLayoutParams();
        layoutParams1.width=PlatformInfoUtils.getWidthOrHeight(context)[0];
        shopCartRecycleAdapter.setOperationClick(new ShopCartRecycleAdapter.OperationClick() {
            @Override
            public void operationClick(final int total) {
                if(total<=0)
                {
                    dismiss();
                }else {
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) outView.getLayoutParams();
                    final int itemCount=shopCartRecycleAdapter.getItemCount();
                    if(itemCount<=7){
                        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) goodsRecycleView.getLayoutParams();
                        layoutParams1.height= LinearLayout.LayoutParams.WRAP_CONTENT;
                        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    }
                    layoutParams.height = PlatformInfoUtils.getWidthOrHeight(context)[1] - goodsRecycleView.getMeasuredHeight() - Utils.dp2px(45 + 49) - statusBarHeight1;
                    Animation animation = new ViewSizeChangeAnimation(outView, layoutParams.height,PlatformInfoUtils.getWidthOrHeight(context)[0]);
                    animation.setDuration(50);
                    outView.startAnimation(animation);
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

        emptyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActiveLaunchSuccessTipDialog dialog=new ActiveLaunchSuccessTipDialog();
                dialog.setText("","清空购物车？","取消","清空");
                dialog.setOnClickListener(new ActiveLaunchSuccessTipDialog.OnClickListener() {
                    @Override
                    public void onLeftClick() {

                    }

                    @Override
                    public void onRightClick() {
                        shopCartRecycleAdapter.setTotal(0);
                        shopCartRecycleAdapter.setTotalMoney(new BigDecimal(0));
                        shopCartRecycleAdapter.clearList();
                        EventBus.getDefault().post(new UpdateShopChartListEvent(3));
                        dismiss();
                    }
                });
                dialog.show(context.getSupportFragmentManager());
            }
        });

        /**
         * 获取状态栏高度——方法1
         * */
        statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = App.getInstance().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = App.getInstance().getResources().getDimensionPixelSize(resourceId);
        }
    }

    public void clearList(){
        shopCartRecycleAdapter.setTotal(0);
        shopCartRecycleAdapter.setTotalMoney(new BigDecimal(0));
        shopCartRecycleAdapter.clearList();
        EventBus.getDefault().post(new UpdateShopChartListEvent(3));
    }


    //设置数据
    public void setDataSource(List<ProductDetail> list) {
        this.list = list;
        this.shopCartRecycleAdapter.setList(list);
        this.shopCartRecycleAdapter.notifyDataSetChanged();
    }

    public void setTotal(int total,BigDecimal totalMoney){
        shopCartRecycleAdapter.setTotal(total);
        shopCartRecycleAdapter.setTotalMoney(totalMoney);
    }

    //购物车添加数据
    public void addProductDetail(ProductDetail productdetail,int total,BigDecimal totalMoney){
        if(productdetail!=null){
            boolean isNew=true;
            for (int i=0;i<list.size();i++){
                ProductDetail productDetail1=list.get(i);
                ProductInfo productInfo=productDetail1.getProductInfo();
                if(productdetail.getProductInfo().getProductId()==productDetail1.getProductInfo().getProductId()){
                    int productNum=productdetail.getProductInfo().getProductNum();
                    if(productNum>0) {
                        productInfo.setProductNum(productNum);
                        productDetail1.setProductInfo(productInfo);
                        isNew = false;
                        list.remove(i);
                        list.add(i, productDetail1);
                    }else{
                        isNew = false;
                        list.remove(i);
                    }
                }
            }
            if(isNew){
                list.add(0,productdetail);
            }
            this.shopCartRecycleAdapter.setList(list);
            shopCartRecycleAdapter.setTotal(total);
            shopCartRecycleAdapter.setTotalMoney(totalMoney);
            this.shopCartRecycleAdapter.notifyDataSetChanged();

            int itemCount=shopCartRecycleAdapter.getItemCount();
            if(itemCount>7){
                goodsRecycleView.getLayoutParams().height=7*Utils.dp2px(53);
            }
        }
    }

    //得到购物车列表
    public List<ProductDetail> getDateSource(){
        return list;
    }


    /**
     * 没有半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        v.getLocationOnScreen(location);//获取控件的位置坐标
////        //获取自身的长宽高
        conentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        outView.getLayoutParams().height=PlatformInfoUtils.getWidthOrHeight(context)[1]-goodsRecycleView.getMeasuredHeight()-Utils.dp2px(45+49)-statusBarHeight1;
        this.showAtLocation(v, Gravity.TOP, 0, 0);
    }


}
