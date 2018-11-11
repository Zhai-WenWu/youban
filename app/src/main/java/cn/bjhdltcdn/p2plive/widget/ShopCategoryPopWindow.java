package cn.bjhdltcdn.p2plive.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.adapter.ShopCategoryRecycleAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.StoreCategoryRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.StoreFragment;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.utils.Solve7PopupWindow;
import cn.bjhdltcdn.p2plive.utils.Utils;

/**
 * Created by Hu_PC on 2018/5/17.
 */

public class ShopCategoryPopWindow extends PopupWindow {
    private StoreFragment context;
    private View conentView;
    private View backgroundView;
    private RecyclerView diatanceRecycleView,categoryRecycleView;
    private TextView empty_tv;
    private ShopCategoryRecycleAdapter shopDiatanceRecyclerViewAdapter;
    private StoreCategoryRecyclerViewAdapter storeCategoryRecyclerViewAdapter;
    private List<FirstLabelInfo> categoryList;
    private List<LabelInfo> distanceList;
    int[] location = new int[2];
    private View outView;
    private TextView resetTextView,okTextView;
    private long distanceSort=29;

    public interface OnItemListener {
        public void OnItemListener(int position, String typeSelect);
    }

//    public void setType(int type) {
//        this.type = type;
//    }

    public ShopCategoryPopWindow(StoreFragment context) {
        this.context = context;
        initView();
    }

    private void initView() {
//        this.anim_backgroundView = AnimationUtils.loadAnimation(context, R.anim.popshow_operation_anim_up);
        LayoutInflater inflater = (LayoutInflater) context.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.conentView = inflater.inflate(R.layout.pop_shop_category_layout, null);
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

        outView=conentView.findViewById(R.id.out_layout);
        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //设置适配器
        this.diatanceRecycleView = (RecyclerView) conentView.findViewById(R.id.distance_recycler_view);
        this.shopDiatanceRecyclerViewAdapter = new ShopCategoryRecycleAdapter(context.getActivity());
        diatanceRecycleView.setHasFixedSize(true);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(context.getActivity());
        flowLayoutManager.setAutoMeasureEnabled(true);
        diatanceRecycleView.setLayoutManager(flowLayoutManager);
        diatanceRecycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10)));
        diatanceRecycleView.setAdapter(shopDiatanceRecyclerViewAdapter);
        shopDiatanceRecyclerViewAdapter.setOperationClick(new ShopCategoryRecycleAdapter.OperationClick() {
            @Override
            public void operationClick(long labelId) {
                distanceSort=labelId;
            }
        });

        this.categoryRecycleView = (RecyclerView) conentView.findViewById(R.id.category_recycler_view);
        storeCategoryRecyclerViewAdapter=new StoreCategoryRecyclerViewAdapter(context.getActivity());
        categoryRecycleView.setHasFixedSize(true);
        FlowLayoutManager flowLayoutManager2= new FlowLayoutManager(context.getActivity());
        flowLayoutManager2.setAutoMeasureEnabled(true);
        categoryRecycleView.setLayoutManager(flowLayoutManager2);
        categoryRecycleView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(10)));
        categoryRecycleView.setAdapter(storeCategoryRecyclerViewAdapter);


        resetTextView=conentView.findViewById(R.id.tv_reset);
        resetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shopDiatanceRecyclerViewAdapter.reset();
                storeCategoryRecyclerViewAdapter.reset();
                distanceSort=0;
            }
        });
        okTextView=conentView.findViewById(R.id.tv_ok);
        okTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FirstLabelInfo> labelInfoList=new ArrayList<>();
                labelInfoList=storeCategoryRecyclerViewAdapter.getSelectList();
                context.refreshList(distanceSort,labelInfoList);
                dismiss();
            }
        });

    }

    //设置数据
    public void setDataSource(List<LabelInfo> distanceList,List<FirstLabelInfo> categoryList) {
        this.categoryList = categoryList;
        this.distanceList=distanceList;
        if(distanceList!=null&&distanceList.size()>0){
            shopDiatanceRecyclerViewAdapter.setList(distanceList);
            shopDiatanceRecyclerViewAdapter.notifyDataSetChanged();
        }
        if(categoryList!=null&&categoryList.size()>0){
            storeCategoryRecyclerViewAdapter.setList(categoryList);
            storeCategoryRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    public void showDataSource(long distanceSort,List<FirstLabelInfo> selectCategoryList){
        if(distanceList!=null&&distanceList.size()>0){
            shopDiatanceRecyclerViewAdapter.setDistanceSort(distanceSort);
        }
        if(categoryList!=null&&categoryList.size()>0){
            storeCategoryRecyclerViewAdapter.setSelectList(selectCategoryList);
        }
    }



    /**
     * 没有半透明背景  显示popupWindow
     *
     * @param
     */
    public void showPopupWindow(View v) {
        showAsDropDown(v);
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

    public void showAtLocation(View v){
        this.showAtLocation(v, Gravity.TOP, 0, 0);
    }



}
