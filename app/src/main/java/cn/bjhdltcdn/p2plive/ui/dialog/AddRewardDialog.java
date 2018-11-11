package cn.bjhdltcdn.p2plive.ui.dialog;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.ui.activity.ReChargeListActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.PropsGridAdapter;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * Created by huwenhua on 2016/7/6.
 */
public class AddRewardDialog extends DialogFragment {

    private View rootView;
    private TextView propsView, rechargeView, goldNumView;
    private List<View> viewList = new ArrayList<>();
    private CustomViewPager viewPager;
    private PropsTabAdapter propsTabAdapter;
    private PropsGridAdapter propsGridAdapter;
    private GridView propsGridView;
    private List<Props> propsList;
    private OnClickListener onClickListener;
    private int goldNum;//剩余金币

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 加载道具数据
     */
    public void setPropsList() {

        if (propsList == null) {
            propsList = new ArrayList<>(1);
        } else {
            propsList.clear();
        }

        if (propsGridAdapter == null) {
            propsGridAdapter = new PropsGridAdapter();
        }
        GreenDaoUtils.getInstance().getPropsListByGiftType(new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                propsList = (List<Props>) object;
                if (propsGridAdapter == null) {
                    propsGridAdapter = new PropsGridAdapter();
                }

                if (propsList != null && !propsList.isEmpty()) {
                    propsGridAdapter.setList(propsList);
                    propsGridAdapter.notifyDataSetChanged();
                }
            }
        }, 1);


    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
        if (goldNumView != null) {
            goldNumView.setText("剩余" + goldNum + "金币");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_reward_layout, null);
        rootView.setAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_enter));
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        setPropsList();
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(App.getInstance(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        // 触摸内容区域外的需要关闭对话框
        rootView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v instanceof ViewGroup) {
                    View layoutView = ((ViewGroup) v).getChildAt(0);
                    if (layoutView != null) {
                        float y = event.getY();
                        float x = event.getX();
                        Rect rect = new Rect(layoutView.getLeft(), layoutView.getTop(), layoutView.getRight(), layoutView.getBottom());
                        if (!rect.contains((int) x, (int) y)) {
                            onDismiss();
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

    private void initView() throws Exception {

        if (propsList == null) {
            throw new Exception("在你调用show()之前，必须优先调用setPropsList()");
        }

        rechargeView = (TextView) rootView.findViewById(R.id.tab_recharge_name_view);
        propsView = (TextView) rootView.findViewById(R.id.tab_props_name_view);
        viewPager = (CustomViewPager) rootView.findViewById(R.id.props_view_page);
        View propsLayout = LayoutInflater.from(App.getInstance()).inflate(R.layout.publish_props_layout, null);
        viewList.add(propsLayout);
        propsTabAdapter = new PropsTabAdapter(viewList);
        viewPager.setAdapter(propsTabAdapter);
        viewPager.setCurrentItem(2);
        viewPager.setIsCanScroll(true);
        goldNumView = (TextView) rootView.findViewById(R.id.my_dian_view);
        propsGridView = (GridView) propsLayout.findViewById(R.id.props_grid_view);
        propsGridView.setAdapter(propsGridAdapter);
        goldNumView.setText("剩余" + goldNum + "金币");
        rechargeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到充值界面
                startActivity(new Intent(getActivity(), ReChargeListActivity.class));
                onDismiss();
            }
        });


        propsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                propsView.setTextColor(App.getInstance().getResources().getColor(R.color.color_ffb921));
            }
        });


        propsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Props props = propsGridAdapter.getItem(position);
                //聊天
//                if (props.getType() == 9) {
//                    //弹出发红包dialog
//                    showRedEnvelopeDialog(props, 1);
//                } else {
                if (yesOrNoBuy(props)) {
                    onClickListener.onClick(props, goldNum);
//                        onDismiss();
                }
//                }

            }

        });
    }

    /**
     * 检测当前金币数是否可以购买道具
     *
     * @param props
     * @return
     */
    private boolean yesOrNoBuy(Props props) {
        if (props == null) {
            return false;
        }

        int payGoldNum = 0;
        if (props.getIsSale() == 1) {
            payGoldNum = props.getSalePrice() > 0 ? props.getSalePrice() : 0;
        } else {
            payGoldNum = props.getPrice() > 0 ? props.getPrice() : 0;
        }

        if (goldNum >= payGoldNum) {
            goldNum = goldNum - payGoldNum;
            return true;
        } else {
            Utils.showToastShortTime("账户余额不足，去增加金币吧");
            startActivity(new Intent(getActivity(), ReChargeListActivity.class));
            return false;
        }
    }

    /**
     * 检测当前金币数是否可以购买道具
     *
     * @param payGoldNum
     * @return
     */
    private boolean yesOrNoBuyByNum(int payGoldNum) {
        if (goldNum >= payGoldNum) {
            goldNum = goldNum - payGoldNum;
            return true;
        } else {
            Toast.makeText(App.getInstance(), "账户余额不足，去增加金币吧", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(), ReChargeListActivity.class));
            return false;
        }
    }

    private void showRedEnvelopeDialog(final Props props, final int type) {
        RewardRedEnvelopeTipDialog rewardRedEnvelopeTipDialog = new RewardRedEnvelopeTipDialog();
        rewardRedEnvelopeTipDialog.setProps(props);
        rewardRedEnvelopeTipDialog.setOnClickListener(new RewardRedEnvelopeTipDialog.OnClickListener() {

            @Override
            public void onClick(int gold) {
                if (yesOrNoBuyByNum(gold)) {
                    onClickListener.onClick(props, gold);
                    onDismiss();
                }
            }
        });
        rewardRedEnvelopeTipDialog.show(getFragmentManager());
    }

    public void onDismiss() {
        if (rootView == null) {
            return;
        }

        if (rootView.getAnimation() != null) {
            rootView.getAnimation().cancel();
        }

        Animation animation = AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_out);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rootView.startAnimation(animation);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

        if (rootView != null) {
            if (rootView.getAnimation() != null) {
                rootView.getAnimation().cancel();
                rootView.getAnimation().reset();
            }
        }

        rootView = null;
    }

    public interface OnClickListener {
        /**
         * @param props
         * @param goldNum
         */
        void onClick(Props props, int goldNum);
    }

}
