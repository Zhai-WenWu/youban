package cn.bjhdltcdn.p2plive.ui.activity;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCurrentGoldStatisticResponse;
import cn.bjhdltcdn.p2plive.model.MyProps;
import cn.bjhdltcdn.p2plive.model.RoomInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.UserPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 显示本次直播房间的最终数据页面
 */
public class ShowCurrentRoomGoldStatisticDialogActivity extends BaseActivity implements BaseView {
    private long roomId;// 房间id
    /**
     * 选中的关注按钮
     */
    private TextView mAttentionView;
    private long currentUserId;
    private ChatRoomPresenter chatRoomPresenter;

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    private UserPresenter userPresenter;

    public UserPresenter getUserPresenter() {
        if (userPresenter == null) {
            userPresenter = new UserPresenter(this);
        }
        return userPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_current_room_gold_statistic_dialog_layout);
        currentUserId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
        roomId = getIntent().getLongExtra(Constants.Fields.ROOMNUMBER, 0);
        initView();
        getChatRoomPresenter().getCurrentGoldStatistic(roomId, currentUserId);
    }

    private void initView() {
        findViewById(R.id.finish_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // 背景
        ImageView bgImageView = (ImageView) findViewById(R.id.bg_img);
        RoomInfo roomInfo = getIntent().getParcelableExtra(Constants.Fields.ROOMINFO);
        String roomBg = getIntent().getStringExtra(Constants.Fields.IMAGEPATH);
        if (TextUtils.isEmpty(roomBg) && roomInfo != null) {
            roomBg = roomInfo.getBackgroundUrl();
        }
//        Glide.with(getCurrentActivity()).load(roomBg)
//                .crossFade(1000)
//                .bitmapTransform(new BlurTransformation(getCurrentActivity(), 20, 1))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
//                .into(bgImageView);
        Utils.ImageViewDisplayByUrl(roomBg, bgImageView);

    }


    /**
     * 投票获得支持列表
     *
     * @param response
     */
    private void initCurrentVotelistData(GetCurrentGoldStatisticResponse response) {
        if (response.getCurrentVotelist() != null && response.getCurrentVotelist().size() > 0) {
            LinearLayout layoutView = findViewById(R.id.layout_view_3);

            int position = 0;
            for (MyProps myProps : response.getCurrentVotelist()) {
                View itemView = View.inflate(App.getInstance(), R.layout.show_current_room_gold_statistic_list_item_layout, null);

                if (myProps.getBaseUser() != null) {
                    // 位置
                    position++;
                    TextView textView = (TextView) itemView.findViewById(R.id.position_view);
                    textView.setText(String.valueOf(position));

                    // 头像
                    CircleImageView circleImageView = (CircleImageView) itemView.findViewById(R.id.user_image_view);
                    Utils.ImageViewDisplayByUrl(myProps.getBaseUser().getUserIcon(), circleImageView);

                    // 昵称
                    TextView nickNameView = (TextView) itemView.findViewById(R.id.nick_name_view);
                    nickNameView.setText(myProps.getBaseUser().getNickName());

                    // 收获金币
                    SpannableStringBuilder stringBuilder = new SpannableStringBuilder(String.valueOf(myProps.getTotalGold()));
                    stringBuilder.append("支持");
                    stringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_ffda44)), 0, stringBuilder.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBuilder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), stringBuilder.length() - 2, stringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    TextView sendGoldcountView = (TextView) itemView.findViewById(R.id.send_goldcount_view);
                    sendGoldcountView.setText(stringBuilder);

                    // 关注状态
                    TextView attentionView = (TextView) itemView.findViewById(R.id.attention_view);
                    if (myProps.getBaseUser().getIsAttention() == 1) {// 已关注
                        attentionView.setText("已关注");
                        attentionView.setTextColor(getResources().getColor(R.color.color_ffda44));
                        attentionView.setBackgroundResource(R.drawable.shape_round_25_stroke_ffda44);
                        attentionView.setOnClickListener(null);
                    } else {
                        attentionView.setText("关注");
                        attentionView.setTextColor(getResources().getColor(R.color.color_333333));
                        attentionView.setBackgroundResource(R.drawable.shape_round_25_solid_ffda44);
                        attentionView.setOnClickListener(new AttentionViewOnClickListener(attentionView, myProps.getBaseUser().getUserId()));
                    }

                    if (myProps.getBaseUser().getUserId() == currentUserId) {
                        attentionView.setVisibility(View.INVISIBLE);
                    }

                    layoutView.addView(itemView);

                }

            }

        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (InterfaceUrl.URL_ATTENTIONOPERATION.equals(apiName)) {
            if (object instanceof BaseResponse) {
                BaseResponse response = (BaseResponse) object;
                if (response.getCode() == 200 || response.getCode() == 202) {

                    if (mAttentionView != null) {
                        mAttentionView.setText("已关注");
                        mAttentionView.setTextColor(getResources().getColor(R.color.color_ffda44));
                        mAttentionView.setBackgroundResource(R.drawable.shape_round_25_stroke_ffda44);
                        mAttentionView.setOnClickListener(null);
                    }

                    mAttentionView = null;

                }
            }
        } else if (InterfaceUrl.URL_GETCURRENTGOLDSTATISTIC.equals(apiName)) {
            if (object instanceof GetCurrentGoldStatisticResponse) {
                GetCurrentGoldStatisticResponse response = (GetCurrentGoldStatisticResponse) object;
                if (response.getCode() == 200) {
                    // 直播时长
                    TextView activeTimeView = (TextView) findViewById(R.id.active_time_view);
                    activeTimeView.setText(response.getActiveTime());
                    initCurrentVotelistData(response);


                } else {
                    Utils.showToastShortTime(response.getMsg());
                }
            }
        }

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    class AttentionViewOnClickListener implements View.OnClickListener {

        private TextView attentionView;
        private long toUserId;

        public AttentionViewOnClickListener(TextView attentionView, long toUserId) {
            this.attentionView = attentionView;
            this.toUserId = toUserId;
        }

        @Override
        public void onClick(View v) {

            mAttentionView = this.attentionView;
            getUserPresenter().attentionOperation(1, currentUserId, toUserId);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAttentionView != null) {
            mAttentionView = null;
        }


    }

}
