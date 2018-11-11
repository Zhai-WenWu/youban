//package cn.bjhdltcdn.p2plive.ui.dialog;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Rect;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.annotation.IdRes;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.httpresponse.GetHobbyListResponse;
//import cn.bjhdltcdn.p2plive.httpresponse.GetJoinOrganListResponse;
//import cn.bjhdltcdn.p2plive.model.HobbyInfo;
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.ProductDetail;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
//import cn.bjhdltcdn.p2plive.ui.activity.BootUpActivity;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActiveJoinOrgainRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.ActivePublishHobbyRecyclerViewAdapter;
//import cn.bjhdltcdn.p2plive.ui.adapter.ShopCartRecycleAdapter;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
//
///**
// * Created by huwnehua on 2018/5/19.
// */
//public class ShoppingCartDialog extends DialogFragment{
//
//    private View rootView;
//    private RecyclerView goodsRecycleView;
//    private TextView empty_tv;
//    private ShopCartRecycleAdapter shopCartRecycleAdapter;
//    private List<ProductDetail> list;
//
//    public void setList(List<ProductDetail> list) {
//        this.list = list;
//        shopCartRecycleAdapter.setList(list);
//        shopCartRecycleAdapter.notifyDataSetChanged(); }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//
//        rootView = inflater.inflate(R.layout.dialog_shopping_cart_layout, null);
//        // 触摸内容区域外的需要关闭对话框
//        rootView.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                if (v instanceof ViewGroup) {
//                    View layoutView = ((ViewGroup) v).getChildAt(0);
//                    if (layoutView != null) {
//                        float y = event.getY();
//                        float x = event.getX();
//                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
//                        if (!rect.contains((int) x, (int) y)) {
//                            dismiss();
//                        }
//                        rect.setEmpty();
//                        rect = null;
//                    }
//                }
//                return false;
//            }
//        });
//        initView();
//
//        return rootView;
//    }
//
//    private void initView() {
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    dismiss();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        goodsRecycleView = (RecyclerView) rootView.findViewById(R.id.goods_recycler_view);
//        shopCartRecycleAdapter = new ShopCartRecycleAdapter(this.getActivity());
//        goodsRecycleView.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(App.getInstance(), LinearLayoutManager.VERTICAL, false);
//        goodsRecycleView.setLayoutManager(layoutManager);
//        goodsRecycleView.setAdapter(shopCartRecycleAdapter);
//
//        if(shopCartRecycleAdapter.getItemCount()>5){
//            goodsRecycleView.getLayoutParams().height=Utils.dp2px(300);
//        }
//
//
//
//    }
//
//    @Override
//    public void dismiss() {
//        try {
//            super.dismissAllowingStateLoss();
//        } catch (Exception e) {
//            e.printStackTrace();
//            super.dismiss();
//        }
//    }
//
//    public void show(FragmentManager manager) {
//        show(manager, "dialog");
//    }
//
//    @Override
//    public void show(FragmentManager manager, String tag) {
//        try {
//            super.show(manager, tag);
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//            // 解决java.lang.IllegalStateException: Can not perform this action问题
//            final android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        }
//    }
//
//
//
//    public interface SelectItemListener {
//        void confirmItemClick();
//    }
//}
