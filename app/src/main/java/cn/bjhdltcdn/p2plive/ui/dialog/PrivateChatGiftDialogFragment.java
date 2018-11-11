package cn.bjhdltcdn.p2plive.ui.dialog;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.ClosePayActivityEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetLetterPropsListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.QueryUserBalanceResponse;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ReChargeListActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.PrivateChatGiftAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.PrivateChatGiftListFragmentAdapter;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;
import io.rong.imkit.GiftMessage;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by xiawenquan on 17/12/29.
 */

public class PrivateChatGiftDialogFragment extends DialogFragment implements BaseView {


    private View rootView;
    private TextView userBalanceView;

    private String targetId;
    private CustomViewPager viewPager;

    // 礼物页面显示8个
    private int pageSize = 8;
    private PrivateChatGiftListFragmentAdapter adapter;
    private Props selectProps;
    /**
     * 金币余额
     */
    private long userBalance;
    private boolean needShowLoading = true;

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
        EventBus.getDefault().register(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.private_message_gift_dialog_layouy, null);
        rootView.setAnimation(AnimationUtils.loadAnimation(App.getInstance(), R.anim.popup_enter));
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userBalanceView = (TextView) rootView.findViewById(R.id.user_balance_view);
        // 充值按钮
        rootView.findViewById(R.id.pay_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReChargeListActivity.class));
            }
        });

        viewPager = (CustomViewPager) rootView.findViewById(R.id.view_pager);

        initSendView();

        // 查询余额
        long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        getUserPresenter().queryUserBalance(userId);

        // 查询私信礼物列表
        getUserPresenter().getLetterPropsList();


    }


    /**
     * 发送礼物
     */
    private void initSendView() {

        rootView.findViewById(R.id.send_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 送礼物接口

                if (StringUtils.isEmpty(targetId)) {
                    Utils.showToastShortTime("参数错误");
                    return;
                }

                if (selectProps == null) {
                    Utils.showToastShortTime("请选择礼物");
                    return;
                }


                if (userBalance < selectProps.getPrice()) {
                    Utils.showToastShortTime("余额不足，请充值吧。");
                    startActivity(new Intent(getActivity(), ReChargeListActivity.class));
                    return;
                }

                long userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                getUserPresenter().letterPresentedGifts(userId, Long.parseLong(targetId), selectProps.getPropsId(), 1, 1, 0);

            }
        });

    }

    /**
     * 初始化礼物显示控件
     */
    private void initViewPage(int countByGiftType, List list) {

        if (countByGiftType > 0) {
            // 计算分页
            int pageCount = 0;
            if (countByGiftType % pageSize == 0) {
                pageCount = countByGiftType / pageSize;
            } else {
                pageCount = (countByGiftType / pageSize) + 1;
            }

            adapter = new PrivateChatGiftListFragmentAdapter(pageCount, list);
            viewPager.setOffscreenPageLimit(3);
            viewPager.setAdapter(adapter);
            adapter.setOnItemListener(new PrivateChatGiftListFragmentAdapter.GropVideoGiftAdapterItemListener() {
                @Override
                public void onItemClick(final Props props) {
                    if (props != null) {

                        selectProps = props;

                    }
                }
            });

            // 默认选第一个礼物
//            mProps = adapter.getList().get(0);

            // 分页圆点
            final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.item_layout_view);
            if (layout != null) {
                for (int i = 0; i < pageCount; i++) {
                    View childView = View.inflate(getContext(), R.layout.group_video_dot_layout, null);
                    ImageView imageView = (ImageView) childView.findViewById(R.id.dot_view);
                    imageView.setImageResource(R.drawable.shape_group_view_dot_nor_layout);

                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                    if (params == null) {
                        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                    params.rightMargin = Utils.dp2px(5);

                    layout.addView(childView);

                }

                // 默认第一页圆点亮
                View view = layout.getChildAt(0);
                ImageView imageView = (ImageView) view.findViewById(R.id.dot_view);
                imageView.setImageResource(R.drawable.shape_group_view_dot_light_layout);

                Drawable layoutDrawable = layout.getBackground();
                if (layoutDrawable != null) {
                    layoutDrawable.mutate().setAlpha((int) (255 * 0.7));
                }

            }


            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    if (layout != null) {
                        for (int i = 0; i < layout.getChildCount(); i++) {
                            View view = layout.getChildAt(i);
                            if (view instanceof LinearLayout) {
                                ImageView imageView = (ImageView) view.findViewById(R.id.dot_view);
                                if (i == position) {
                                    imageView.setImageResource(R.drawable.shape_group_view_dot_light_layout);
                                } else {
                                    imageView.setImageResource(R.drawable.shape_group_view_dot_nor_layout);
                                }
                            }
                        }
                    }


                    // 清除当前页选中的状态
                    if (adapter.getItemAdapterList() != null) {
                        PrivateChatGiftAdapter itemAdapterList = adapter.getItemAdapterList().get(position);
                        if (itemAdapterList != null) {
                            // 清除之前点击的item状态
                            if (itemAdapterList.getItemSelectPosition() > -1 && itemAdapterList.getItemSelectPosition() < itemAdapterList.getItemCount()) {
                                Props itemSelectProps = itemAdapterList.getList().get(itemAdapterList.getItemSelectPosition());
                                itemSelectProps.setSelected(false);
                                itemAdapterList.notifyItemChanged(itemAdapterList.getItemSelectPosition());

                            }
                        }
                        selectProps = null;
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

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
    public void updateView(String apiName, Object object) {
        if (!isAdded()) {
            return;
        }

        if (object instanceof Exception) {
            Exception exception = (Exception) object;
            Utils.showToastShortTime(exception.getMessage());
            return;
        }

        if (InterfaceUrl.URL_GETLETTERPROPSLIST.equals(apiName)) {
            if (object instanceof GetLetterPropsListResponse) {
                GetLetterPropsListResponse response = (GetLetterPropsListResponse) object;
                if (response.getCode() == 200) {
                    List<Props> list = response.getList();
                    if (list != null && list.size() > 0) {
                        initViewPage(list.size(), list);
                    }
                }
            }
        } else if (InterfaceUrl.URL_QUERYUSERBALANCE.equals(apiName)) {// 查询余额

            if (object instanceof QueryUserBalanceResponse) {
                QueryUserBalanceResponse response = (QueryUserBalanceResponse) object;
                if (response.getCode() == 200) {
                    userBalance = response.getUserBalance();
                    String userBalanceStr = userBalance + "金币";
                    userBalanceView.setText(userBalanceStr);
                }
            }

        } else if (InterfaceUrl.URL_LETTERPRESENTEDGIFTS.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200) {

                    // 查询余额
                    getUserPresenter().queryUserBalance(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));

                    if (selectProps == null) {
                        return;
                    }

                    //发送消息
                    GiftMessage giftMessage = new GiftMessage();
                    giftMessage.setGiftImgUrl(selectProps.getUrl());
                    giftMessage.setGiftNmae(selectProps.getName());
                    giftMessage.setGiftGold(selectProps.getPrice());
                    giftMessage.setGiftNum(0);

                    Logger.d("targetId == " + targetId);
                    Message message = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, giftMessage);

                    RongIMutils.sendMessage(message, "[礼物]", "", new RongIMutils.SendMessageCallBack() {
                        @Override
                        public void sendMessageCallBack(boolean isSuccess) {

                            Logger.d("isSuccess === " + isSuccess);
                        }
                    });
                }
            }
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(ClosePayActivityEvent evnet) {

        if (evnet == null) {
            return;
        }

        // 查询余额
        getUserPresenter().queryUserBalance(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));

    }

    @Override
    public void showLoading() {

        ProgressDialogUtils.getInstance().showProgressDialog(getActivity());

    }

    @Override
    public void hideLoading() {

        ProgressDialogUtils.getInstance().hideProgressDialog();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (userPresenter != null) {
            userPresenter.onDestroy();
        }
        userPresenter = null;


    }
}
