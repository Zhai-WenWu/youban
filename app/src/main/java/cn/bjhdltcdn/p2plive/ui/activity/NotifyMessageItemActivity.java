package cn.bjhdltcdn.p2plive.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.db.GreenDaoUtils;
import cn.bjhdltcdn.p2plive.event.RongyunReceiveUnreadCountEvent;
import cn.bjhdltcdn.p2plive.ui.fragment.ToolBarFragment;

/**
 * 互动消息下的3个消息
 * 通知消息
 * 回复我的
 * 待处理申请
 */
public class NotifyMessageItemActivity extends AppCompatActivity {


    private View arrowImageView;
    private TextView textViewPop;

    private View arrowImageView3;
    private TextView textViewPop3;

    private View arrowImageView4;
    private TextView textViewPop4;

    private int itemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_message_item_layout);

        EventBus.getDefault().register(this);

        setTitle();

        initView();

    }


    private void setTitle() {
        ToolBarFragment titleFragment = (ToolBarFragment) getSupportFragmentManager().findFragmentById(R.id.title_layout);
        titleFragment.setTitleView("互动消息");
    }


    private void initView() {

        // 通知消息
        View layoutView2 = findViewById(R.id.layout_view_2);
        ImageView imageView = layoutView2.findViewById(R.id.image_view);
        layoutView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemType = 1;
                startActivity(new Intent(NotifyMessageItemActivity.this, LocalNotifyMessageListActivity.class)
                        .putExtra(Constants.KEY.KEY_OBJECT, "通知消息")
                        .putExtra(Constants.KEY.KEY_TYPE, itemType));

            }
        });

        // 图标
        imageView.setImageResource(R.mipmap.notify_icon);
        // 文字
        TextView textView = layoutView2.findViewById(R.id.nicke_text_view);
        textView.setText("通知消息");

        // 箭头
        arrowImageView = layoutView2.findViewById(R.id.arrow_right_view);
        // 红泡
        textViewPop = layoutView2.findViewById(R.id.name_pop_view);

        getUnreadCountByType(arrowImageView, textViewPop, 10002, 10003, 30002, 30003, 40001, 40002);

        // 回复我的
        View layoutView3 = findViewById(R.id.layout_view_3);
        layoutView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemType = 2;
                startActivity(new Intent(NotifyMessageItemActivity.this, LocalNotifyMessageListActivity.class)
                        .putExtra(Constants.KEY.KEY_OBJECT, "回复我的")
                        .putExtra(Constants.KEY.KEY_TYPE, itemType));

            }
        });
        ImageView imageView3 = layoutView3.findViewById(R.id.image_view);
        // 图标
        imageView3.setImageResource(R.mipmap.message_nor_icon);
        // 文字
        TextView textView3 = layoutView3.findViewById(R.id.nicke_text_view);
        textView3.setText("回复我的");

        // 箭头
        arrowImageView3 = layoutView3.findViewById(R.id.arrow_right_view);
        // 红泡
        textViewPop3 = layoutView3.findViewById(R.id.name_pop_view);

        getUnreadCountByType(arrowImageView3, textViewPop3,10005, 10006, 20001, 20002,70001,70002);

        // 待处理申请
        View layoutView4 = findViewById(R.id.layout_view_4);
        layoutView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemType = 3;
                startActivity(new Intent(NotifyMessageItemActivity.this, LocalNotifyMessageListActivity.class)
                        .putExtra(Constants.KEY.KEY_OBJECT, "待处理申请")
                        .putExtra(Constants.KEY.KEY_TYPE, itemType));
            }
        });
        ImageView imageView4 = layoutView4.findViewById(R.id.image_view);
        // 图标
        imageView4.setImageResource(R.mipmap.not_processed_msg_icon);
        // 文字
        TextView textView4 = layoutView4.findViewById(R.id.nicke_text_view);
        textView4.setText("待处理申请");

        // 箭头
        arrowImageView4 = layoutView4.findViewById(R.id.arrow_right_view);
        // 红泡
        textViewPop4 = layoutView4.findViewById(R.id.name_pop_view);

        getUnreadCountByType(arrowImageView4, textViewPop4, 10000, 30001);


    }


    /**
     * 获取未读数
     *
     * @param view1
     * @param view2
     * @param params
     */
    private void getUnreadCountByType(final View view1, final TextView view2, Object... params) {

        long unReadCount = GreenDaoUtils.getInstance().queryUnreadCountByQueryBuilder(params);
        if (unReadCount < 1) {
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
        } else {
            view1.setVisibility(View.GONE);
            view2.setText(String.valueOf(unReadCount));
            view2.setVisibility(View.VISIBLE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventCallBack(RongyunReceiveUnreadCountEvent event) {
        if (event == null) {
            return;
        }

        switch (itemType) {
            case 1:
                getUnreadCountByType(arrowImageView, textViewPop, 10002, 10003, 30002, 30003, 40001, 40002);
                break;

            case 2:
                getUnreadCountByType(arrowImageView3, textViewPop3, 10005, 10006, 20001, 20002,70001,70002);
                break;

            case 3:
                getUnreadCountByType(arrowImageView4, textViewPop4, 10000, 30001);
                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

        if (arrowImageView != null) {
            arrowImageView = null;
        }

        if (arrowImageView3 != null) {
            arrowImageView3 = null;
        }

        if (arrowImageView4 != null) {
            arrowImageView4= null;
        }

        if (textViewPop != null) {
            textViewPop = null;
        }

        if (textViewPop3 != null) {
            textViewPop3 = null;
        }

        if (textViewPop4 != null) {
            textViewPop4 = null;
        }

    }
}
