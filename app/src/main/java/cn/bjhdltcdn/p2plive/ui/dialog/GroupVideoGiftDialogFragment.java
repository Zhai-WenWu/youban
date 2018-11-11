package cn.bjhdltcdn.p2plive.ui.dialog;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.UpdatePayResultEvent;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GiftNumListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.QueryUserBalanceResponse;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.GiftNumListItem;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ReChargeListActivity;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupVideoGiftAdapter;
import cn.bjhdltcdn.p2plive.ui.adapter.GroupVideoGiftListFragmentAdapter;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.CustomViewPager;

/**
 * 多人视频礼物框
 */

public class GroupVideoGiftDialogFragment extends DialogFragment implements BaseView {

    private View rootView;
    //礼物列表
    private CustomViewPager viewPager;
    private GroupVideoGiftListFragmentAdapter adapter;
    private UserPresenter userPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    // 麦上的人列表
    private List<RoomBaseUser> list;

    public List<RoomBaseUser> getList() {
        return list;
    }

    public void setList(List<RoomBaseUser> list) {
        this.list = list;
    }

    // 礼物页面显示8个
    private int pageSize = 8;
    // 选择接收礼物的人
    private BaseUser receiveBaseUser;
    // 选择礼物数量
    private int selectGiftCount = 1;
    // 房间id
    private long roomId;

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    // 礼物id
    private Props mProps;

    // 账户余额
    private int userBalance;
    private int yaokeBeanNum;//邀客豆
    // 显示余额控件
    private TextView ykBeanView, userBalanceView;
    // 选中赠送礼物的人显示控件
    private TextView sendUserView;
    // 选择赠送的礼物数量
    private TextView sendGiftCountView;

    private long currBaseUserId;

    private long voteId;

    /**
     * 1 普通赠送,2 赠送金币
     */
    private int presentedType = 1;

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.select_img_dialog);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.group_video_gift_dialog_layouy, null);
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

        userBalanceView = (TextView) rootView.findViewById(R.id.text_view);
        ykBeanView = (TextView) rootView.findViewById(R.id.text_view_yk_bean);
        // 充值按钮
        rootView.findViewById(R.id.pay_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ReChargeListActivity.class));
            }
        });

        initViewPage();
        initSendUserView();
        initSendGiftCountView();
        initSendView();

        currBaseUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);

        // 默认送给一号
        if (list != null && list.size() > 0) {
            receiveBaseUser = list.get(0);
            sendUserView.setText("送给1号");
        }
        getUserPresenter().queryUserBalance(currBaseUserId);

    }

    /**
     * 初始化礼物显示控件
     */
    private void initViewPage() {
        viewPager = rootView.findViewById(R.id.view_pager);
        Drawable viewPagerDrawable = viewPager.getBackground();
        if (viewPagerDrawable != null) {
            // mutate()作用：
            // 原来Resources缓存的并不是个Drawable，而是Drawable中的State（比如BitmapDrawable的BitmapState）。
            // 是这个State起了一个share的作用，导致不同的Drawable产生了关联。
            // 而mutate就是重新创建一个State以避免共享。所以当要修改alpha时，只要mutate()后再修改即可。
            viewPagerDrawable.mutate().setAlpha((int) (255 * 0.7));
        }
        GreenDaoUtils.getInstance().getPropsListByGiftType(new GreenDaoUtils.ExecuteCallBack() {
            @Override
            public void callBack(Object object) {
                List<Props> props = (List<Props>) object;
                long countByGiftType = props.size();
                if (countByGiftType > 0) {
                    // 计算分页
                    int pageCount = 0;
                    if (countByGiftType % pageSize == 0) {
                        pageCount = (int) (countByGiftType / pageSize);
                    } else {
                        pageCount = (int) ((countByGiftType / pageSize) + 1);
                    }

                    adapter = new GroupVideoGiftListFragmentAdapter(pageCount, props);
                    viewPager.setOffscreenPageLimit(3);
                    viewPager.setAdapter(adapter);
                    adapter.setOnItemListener(new GroupVideoGiftListFragmentAdapter.GropVideoGiftAdapterItemListener() {
                        @Override
                        public void onItemClick(final Props props) {
                            if (props != null) {

                                mProps = props;

                                if (props.getType() == 12) {// 多人礼物的金币红包

                                    selectGiftCount = 1;

                                    TextView sendGiftCountView = (TextView) rootView.findViewById(R.id.send_gift_count_view);
                                    sendGiftCountView.setText(selectGiftCount + "");


                                    // 禁用发送按钮
                                    rootView.findViewById(R.id.send_view).setEnabled(false);
                                    // 禁用选择礼物数量按钮
                                    sendGiftCountView.setEnabled(false);

                                    RewardRedEnvelopeTipDialog rewardRedEnvelopeTipDialog = new RewardRedEnvelopeTipDialog();
                                    rewardRedEnvelopeTipDialog.setProps(props);
                                    rewardRedEnvelopeTipDialog.setOnClickListener(new RewardRedEnvelopeTipDialog.OnClickListener() {

                                        @Override
                                        public void onClick(int gold) {//gold 手动输入的金额

                                            if (userBalance < gold) {
                                                Utils.showToastShortTime("对不起，当前余额不足!");
                                                return;
                                            }

                                            if (currBaseUserId != 0 && receiveBaseUser != null) {
                                                if (receiveBaseUser.getUserId() == currBaseUserId) {
                                                    Utils.showToastShortTime("不能赠送自己礼物");
                                                    return;
                                                } else {
                                                    // 送礼物接口
                                                    //presentedType 1 普通赠送,2 赠送金币
                                                    //goldNum 赠送金币数 presentedType为2时使用
                                                    mProps.setPrice(gold);
                                                    mProps.setSalePrice(gold);

                                                    presentedType = 2;
                                                    getChatRoomPresenter().presentedGifts(currBaseUserId, receiveBaseUser.getUserId(), props.getPropsId(), selectGiftCount, roomId, voteId, presentedType, gold);

                                                }

                                            }

                                        }
                                    });
                                    rewardRedEnvelopeTipDialog.show(getFragmentManager());


                                    return;
                                }

                                // 开启发送按钮
                                rootView.findViewById(R.id.send_view).setEnabled(true);
                                // 开启选择礼物数量按钮
                                sendGiftCountView.setEnabled(true);

                            }
                        }
                    });
                    // 默认选第一个礼物
                    mProps = adapter.getList().get(0);

                    // 分页圆点
                    final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.item_layout_view);
                    if (layout != null) {
                        for (int i = 0; i < pageCount; i++) {
                            View childView = View.inflate(getContext(), R.layout.group_video_dot_layout, null);
                            ImageView imageView = (ImageView) childView.findViewById(R.id.dot_view);
                            imageView.setImageResource(R.drawable.shape_dot_light_layout);

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
                                GroupVideoGiftAdapter itemAdapterList = adapter.getItemAdapterList().get(position);
                                if (itemAdapterList != null) {
                                    // 清除之前点击的item状态
                                    if (itemAdapterList.getItemSelectPosition() > -1 && itemAdapterList.getItemSelectPosition() < itemAdapterList.getItemCount()) {
                                        Props itemSelectProps = itemAdapterList.getList().get(itemAdapterList.getItemSelectPosition());
                                        itemSelectProps.setSelected(false);
                                        itemAdapterList.notifyItemChanged(itemAdapterList.getItemSelectPosition());
                                    }
                                }
                                mProps = null;
                            }

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {
                        }
                    });

                }
            }
        }, 2);
    }

    /**
     * 选择接收人
     */
    private void initSendUserView() {

        View layout2 = rootView.findViewById(R.id.item_layout_view_2);
        Drawable layout2Drawable = layout2.getBackground();
        if (layout2Drawable != null) {
            layout2Drawable.mutate().setAlpha((int) (255 * 0.7));
        }

        sendUserView = (TextView) rootView.findViewById(R.id.send_user_view);
        sendUserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (getList() == null || getList().size() == 0) {
                    Utils.showToastShortTime("麦上无人可选");
                    return;
                }

                final View receiveUserView = View.inflate(App.getInstance(), R.layout.gift_num_list_layout, null);

                final PopupWindow popupWindow = new PopupWindow(receiveUserView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(getActivity().getResources().getColor(R.color.color_040404)));
                popupWindow.setOutsideTouchable(false); // 设置是否允许在外点击使其消失

                // 绑定数据
                for (int i = 0; i < list.size(); i++) {

                    LinearLayout layout = (LinearLayout) receiveUserView.findViewById(R.id.linear_layout_view);
                    final View convertView = View.inflate(App.getInstance(), R.layout.gift_num_list_item_layout, null);
                    TextView textView = (TextView) convertView.findViewById(R.id.text_view);
                    TextView textViewNum = (TextView) convertView.findViewById(R.id.text_view_num);
                    textViewNum.setVisibility(View.GONE);
                    BaseUser baseUser = list.get(i);

                    SpannableStringBuilder builder = new SpannableStringBuilder((i + 1) + "号" + baseUser.getNickName());

                    // 位置号变色
                    ForegroundColorSpan ffda44Span = new ForegroundColorSpan(getResources().getColor(R.color.color_ffda44));
                    builder.setSpan(ffda44Span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    ForegroundColorSpan whiteSpan = new ForegroundColorSpan(getResources().getColor(R.color.white));
                    builder.setSpan(whiteSpan, 2, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    if (i == 0) {// 主持人
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.main_wheat, 0, 0, 0);

                    } else {//嘉宾
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.guests_wheat, 0, 0, 0);
                    }

                    textView.setCompoundDrawablePadding(Utils.dp2px(8));

                    textView.setText(builder);

                    // 点击事件
                    convertView.setOnClickListener(new ReceiveUserViewClickListener(popupWindow, baseUser, i + 1));

                    layout.addView(convertView);

                }


                int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                receiveUserView.measure(width, height);

                int measuredHeight = receiveUserView.getMeasuredHeight();
                int measuredWidth = receiveUserView.getMeasuredWidth();
                LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.item_layout_view);
                int[] location = new int[2];
                layout.getLocationOnScreen(location);

                popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, location[0] + Utils.dp2px(12), location[1] - measuredHeight + Utils.dp2px(5));
                location = null;

            }
        });
    }

    /**
     * 选择礼物数量
     */
    private void initSendGiftCountView() {

        sendGiftCountView = (TextView) rootView.findViewById(R.id.send_gift_count_view);
        sendGiftCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mProps != null && mProps.getType() == 12) {
                    return;
                }

                getChatRoomPresenter().giftNumList();
            }
        });
    }

    /**
     * 发送礼物
     */
    private void initSendView() {

        rootView.findViewById(R.id.send_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (receiveBaseUser == null) {
                    Utils.showToastShortTime("请选择麦上的人");
                    return;
                }

                if (mProps == null) {
                    Utils.showToastShortTime("请选择礼物");
                    return;
                }

                if (mProps.getType() == 12) {
                    return;
                }

                if (selectGiftCount < 1) {
                    Utils.showToastShortTime("请选择礼物数量");
                    return;
                }

                if (currBaseUserId != 0) {
                    if (receiveBaseUser.getUserId() == currBaseUserId) {
                        Utils.showToastShortTime("不能赠送自己礼物");
                        return;
                    }

                }


                if (userBalance < mProps.getPrice() * selectGiftCount) {
                    Utils.showToastShortTime("余额不足，请充值吧。");
                    startActivity(new Intent(getActivity(), ReChargeListActivity.class));
                    return;

                }

                // 送礼物接口
                //presentedType 1 普通赠送,2 赠送金币
                //goldNum 赠送金币数 presentedType为2时使用
                presentedType = 1;
                getChatRoomPresenter().presentedGifts(currBaseUserId, receiveBaseUser.getUserId(), mProps.getPropsId(), selectGiftCount, roomId, voteId, presentedType, 0);

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


    //接收充值成功消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UpdatePayResultEvent event) {
        if (event == null) {
            return;
        }
        getUserPresenter().queryUserBalance(currBaseUserId);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (adapter != null) {
            adapter.setReleaseResource();
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GIFTNUMLIST:
                if (object instanceof GiftNumListResponse) {
                    GiftNumListResponse response = (GiftNumListResponse) object;
                    if (response.getCode() == 200) {
                        List<GiftNumListItem> listItems = new ArrayList<>(1);
                        LinkedHashMap<String, String> map = response.getGiftNumMap();

                        if (map != null) {
                            Iterator<LinkedHashMap.Entry<String, String>> entries = map.entrySet().iterator();
                            while (entries.hasNext()) {
                                LinkedHashMap.Entry<String, String> entry = entries.next();
                                GiftNumListItem giftNumListItem = new GiftNumListItem(entry.getKey(), entry.getValue());
                                listItems.add(giftNumListItem);
                            }
                        }


                        if (listItems.size() > 0) {

                            final View giftNumView = View.inflate(App.getInstance(), R.layout.gift_num_list_layout, null);

                            final PopupWindow popupWindow = new PopupWindow(giftNumView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                            popupWindow.setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.color_040404)));
                            popupWindow.setOutsideTouchable(false); // 设置是否允许在外点击使其消失

                            // 绑定数据
                            for (int i = 0; i < listItems.size(); i++) {

                                LinearLayout layout = (LinearLayout) giftNumView.findViewById(R.id.linear_layout_view);
                                final View convertView = View.inflate(App.getInstance(), R.layout.gift_num_list_item_layout, null);
                                TextView textView = (TextView) convertView.findViewById(R.id.text_view);
                                TextView textViewNum = (TextView) convertView.findViewById(R.id.text_view_num);
                                textViewNum.setVisibility(View.VISIBLE);
                                GiftNumListItem item = listItems.get(i);
                                textViewNum.setText(item.getKey().toString());
                                textView.setText(item.getValue().toString());

//                            SpannableStringBuilder builder = new SpannableStringBuilder(item.getKey() + "    " + item.getValue());

                                // 位置号变色
//                            ForegroundColorSpan ffda44Span = new ForegroundColorSpan(getResources().getColor(R.color.color_ffda44));
//                            builder.setSpan(ffda44Span, 0, item.getKey().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                            ForegroundColorSpan whiteSpan = new ForegroundColorSpan(getResources().getColor(R.color.white));
//                            builder.setSpan(whiteSpan, item.getKey().length() + 4, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//                            textView.setText(builder);
                                // 点击事件
                                convertView.setOnClickListener(new GiftNumViewClickListener(item, popupWindow));

                                layout.addView(convertView);

                            }


                            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                            giftNumView.measure(width, height);

                            int measuredHeight = giftNumView.getMeasuredHeight();
                            int measuredWidth = giftNumView.getMeasuredWidth();

                            // 取横坐标
                            int[] location = new int[2];
                            sendGiftCountView.getLocationOnScreen(location);

                            // 取纵坐标
                            LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.item_layout_view);
                            int[] location2 = new int[2];
                            layout.getLocationOnScreen(location2);

                            popupWindow.showAtLocation(layout, Gravity.NO_GRAVITY, location[0], location2[1] - measuredHeight + Utils.dp2px(5));

                            location2 = null;
                            location = null;

                        }
                    }
                }
                break;
            case InterfaceUrl.URL_PRESENTEDGIFTS:
                if (object instanceof BaseResponse) {
                    BaseResponse response = (BaseResponse) object;
                    if (response.getCode() == 200) {
                        getUserPresenter().queryUserBalance(currBaseUserId);

//                        if (mProps != null) {
//                            mProps.setCount(selectGiftCount);
//                            //通知房间中的其他人刷新礼物页面
//                            RongIMkitUtils.sendGroupVideoMessage(((VideoGroupActivity) getActivity()).getRoomNumber(), 0x14, 0x04, "", receiveBaseUser, mProps.getPropsId(), selectGiftCount, mProps.getPrice());
//                        }
                    }
                }
                break;
            case InterfaceUrl.URL_QUERYUSERBALANCE:
                if (object instanceof QueryUserBalanceResponse) {
                    QueryUserBalanceResponse response = (QueryUserBalanceResponse) object;
                    if (response.getCode() == 200) {
                        userBalance = response.getUserBalance();
                        String userBalanceStr = userBalance + "金币";
                        SpannableStringBuilder builder = new SpannableStringBuilder(userBalanceStr);
                        ForegroundColorSpan colorFFDA44Span = new ForegroundColorSpan(getResources().getColor(R.color.color_ffda44));
                        builder.setSpan(colorFFDA44Span, 0, userBalanceStr.length() - 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        userBalanceView.setText(builder);
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    /**
     * 选择接收人点击事件
     */
    class ReceiveUserViewClickListener implements View.OnClickListener {
        private BaseUser baseUser;
        private int position;
        private PopupWindow popupWindow;

        public ReceiveUserViewClickListener(PopupWindow popupWindow, BaseUser baseUser, int position) {
            this.baseUser = baseUser;
            this.position = position;
            this.popupWindow = popupWindow;
        }

        @Override
        public void onClick(View v) {
            receiveBaseUser = baseUser;
            sendUserView.setText("送给" + position + "号");
            popupWindow.dismiss();
        }
    }

    /**
     * 选择礼物数量
     */
    class GiftNumViewClickListener implements View.OnClickListener {

        private GiftNumListItem item;
        private PopupWindow popupWindow;

        public GiftNumViewClickListener(GiftNumListItem item, PopupWindow popupWindow) {
            this.item = item;
            this.popupWindow = popupWindow;
        }

        @Override
        public void onClick(View v) {
            if (TextUtils.isDigitsOnly(item.getKey())) {
                selectGiftCount = Integer.parseInt(item.getKey());
                TextView sendGiftCountView = (TextView) rootView.findViewById(R.id.send_gift_count_view);
                sendGiftCountView.setText(String.valueOf(selectGiftCount));
            }
            popupWindow.dismiss();
        }
    }

}
