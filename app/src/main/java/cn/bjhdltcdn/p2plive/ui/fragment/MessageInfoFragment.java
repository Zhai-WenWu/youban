package cn.bjhdltcdn.p2plive.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.OnPageSelectedEvent;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.event.UserAgeEvent;
import cn.bjhdltcdn.p2plive.ui.activity.NotifyMessageItemActivity;
import cn.bjhdltcdn.p2plive.utils.ActivityUtils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * Created by Hu_PC on 2017/11/8.
 * 消息列表
 */

public class MessageInfoFragment extends BaseFragment {
    private View rootView;
    private AppCompatActivity mActivity;

    private Fragment mFragment;

    private ImageView imageView;
    private TextView namePopView;
    private RelativeLayout layout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_message_info, null);
        }
        //因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 通知item
        layout = rootView.findViewById(R.id.layout_view);
        imageView = rootView.findViewById(R.id.arrow_right_view);
        namePopView = rootView.findViewById(R.id.name_pop_view);

    }

    @Override
    protected void onVisible(boolean isInit) {


        Logger.d("isInit === " + isInit);

        if (!isInit && mFragment == null) {
            initView();
        }

    }


    private void initView() {


        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotifyMessageItemActivity.class));
            }
        });

        EventBus.getDefault().post(new RongyunReceiveUnreadCountEvent());

        mFragment = getChildFragmentManager().findFragmentById(R.id.conversation_list_layout);
        if (mFragment == null) {
            mFragment = new ChatConversationListFragment();
        }
        if (rootView != null) {
            final Fragment finalMFragment = mFragment;
            rootView.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Uri uri = Uri.parse("rong://" + App.getInstance().getApplicationInfo().packageName).buildUpon()
                            .appendPath("conversationlist")
                            .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                            .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
                            .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                            .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                            .build();

                    if (finalMFragment == null) {
                        return;
                    }

                    if (uri == null) {
                        return;
                    }

                    ((ConversationListFragment) finalMFragment).setUri(uri);

                }
            }, 500);
        }

        ActivityUtils.addFragmentToActivity(getChildFragmentManager(), mFragment, R.id.conversation_list_layout);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(RongyunReceiveUnreadCountEvent event) {

        if (layout == null) {
            layout = rootView.findViewById(R.id.layout_view);
        }

        if (imageView == null) {
            imageView = rootView.findViewById(R.id.arrow_right_view);
        }

        if (namePopView == null) {
            namePopView = rootView.findViewById(R.id.name_pop_view);
        }

        long unReadCount = GreenDaoUtils.getInstance().queryPushdbModelUnreadAllCountByQueryBuilder();
        Logger.d("unReadCount === " + unReadCount);

        if (unReadCount < 1) {
            imageView.setVisibility(View.VISIBLE);
            namePopView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.GONE);
            namePopView.setText(String.valueOf(unReadCount));
            namePopView.setVisibility(View.VISIBLE);
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void callBackEvent(OnPageSelectedEvent evnet) {

        if (evnet == null) {
            return;
        }

        if (evnet.getTabSelectIndex() == 3) {
            onVisible(false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
