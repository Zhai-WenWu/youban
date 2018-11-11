package cn.bjhdltcdn.p2plive.ui.dialog;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupVideoTabFragmentAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.RoomReceiveGiftGaveMeFragment;
import cn.bjhdltcdn.p2plive.ui.fragment.RoomReceiveGiftListFragment;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;


/**
 * 房间内麦上嘉宾查看礼物情况
 */

public class RoomReceiveGiftListDialog extends DialogFragment {

    private View rootView;

    private CustomViewPager mViewPager;
    private GroupVideoTabFragmentAdapter adapter;

    private RoomReceiveGiftListFragment roomReceiveGiftListFragment;
    private RoomReceiveGiftGaveMeFragment receiveGiftGaveMeFragment;

    private TextView tabTextView1;
    private TextView tabTextView2;

    private View tabIndicatorColorView1;
    private View tabIndicatorColorView2;

    private long roomId;


    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.room_receive_gift_list_dialog_lauoyt, null);

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
                            rect.setEmpty();
                            rect = null;
                            dismiss();
                            return false;
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (CustomViewPager) rootView.findViewById(R.id.pager_view);
        if (mViewPager == null) {
            new NullPointerException();
        }
        mViewPager.setIsCanScroll(true);

        adapter = new GroupVideoTabFragmentAdapter(getChildFragmentManager());

        roomReceiveGiftListFragment = new RoomReceiveGiftListFragment();
        adapter.addFragment(roomReceiveGiftListFragment, "");

        receiveGiftGaveMeFragment = new RoomReceiveGiftGaveMeFragment();
        adapter.addFragment(receiveGiftGaveMeFragment, "");

        // 保存内存有2个页面
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(adapter);

        mViewPager.setCurrentItem(0);

        tabTextView1 = (TextView) rootView.findViewById(R.id.tab_text_view_1);
        tabTextView2 = (TextView) rootView.findViewById(R.id.tab_text_view_2);
        tabIndicatorColorView1 = rootView.findViewById(R.id.tab_indicator_color_view_1);
        tabIndicatorColorView2 = rootView.findViewById(R.id.tab_indicator_color_view_2);
        // 本场为我送礼
        rootView.findViewById(R.id.tab_view_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
                setSelectTabStatus(0);

            }
        });

        // 累计为我送礼
        rootView.findViewById(R.id.tab_view_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
                setSelectTabStatus(1);

            }
        });


    }

    /**
     * 选择tab状态
     *
     * @param index
     */
    private void setSelectTabStatus(int index) {

        switch (index) {
            case 0:

                tabTextView1.setTextColor(getResources().getColor(R.color.color_ffda44));
                tabTextView2.setTextColor(getResources().getColor(R.color.color_333333));

                tabIndicatorColorView1.setVisibility(View.VISIBLE);
                tabIndicatorColorView2.setVisibility(View.INVISIBLE);

                break;

            case 1:

                tabTextView1.setTextColor(getResources().getColor(R.color.color_333333));
                tabTextView2.setTextColor(getResources().getColor(R.color.color_ffda44));

                tabIndicatorColorView1.setVisibility(View.INVISIBLE);
                tabIndicatorColorView2.setVisibility(View.VISIBLE);


                break;

        }
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
    public void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.clearData();
        }
        adapter = null;


        if (mViewPager != null) {
            mViewPager.removeAllViews();
            mViewPager.destroyDrawingCache();
        }
        mViewPager = null;

        if (tabTextView1 != null) {
            tabTextView1 = null;
        }

        if (tabTextView2 != null) {
            tabTextView2 = null;
        }

        if (tabIndicatorColorView1 != null) {
            tabIndicatorColorView1 = null;
        }

        if (tabIndicatorColorView2 != null) {
            tabIndicatorColorView2 = null;
        }

        if (roomReceiveGiftListFragment != null) {
            roomReceiveGiftListFragment = null;
        }

        if (receiveGiftGaveMeFragment != null) {
            receiveGiftGaveMeFragment = null;
        }

        if (rootView != null) {
            ((ViewGroup) rootView).removeAllViews();
            rootView.destroyDrawingCache();
        }
        rootView = null;

    }
}
