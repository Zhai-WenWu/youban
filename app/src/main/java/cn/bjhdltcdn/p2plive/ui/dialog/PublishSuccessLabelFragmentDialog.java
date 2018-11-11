package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.httpresponse.GetSecondLabelListResponse;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.PublishLabelTabsRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.RoomLabelTabsRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.GridLayoutSpacesItemDecoration;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;

/**
 * 发布成功后弹框提示
 */
public class PublishSuccessLabelFragmentDialog extends DialogFragment {

    private View rootView;

    private List<LabelInfo> labelInfoList;
    private PublishLabelTabsRecyclerViewAdapter labelAdapter;
    private RecyclerView recyclerView;

    public void setLabelInfoList(List<LabelInfo> labelInfoList) {
        this.labelInfoList = labelInfoList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.color_cc333333_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.publish_success_label_fragment_dialog_layout, null);
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
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = rootView.findViewById(R.id.recycler_label);
        labelAdapter = new PublishLabelTabsRecyclerViewAdapter();
        labelAdapter.setList(labelInfoList);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(getActivity());
//        flowLayoutManager.setAutoMeasureEnabled(true);
        flowLayoutManager.setMargin(Utils.dp2px(5));
        recyclerView.setLayoutManager(flowLayoutManager);
        recyclerView.setAdapter(labelAdapter);


        // 关闭
        rootView.findViewById(R.id.tip_text_view_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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


    @Override
    public void onDetach() {
        super.onDetach();

    }
}
