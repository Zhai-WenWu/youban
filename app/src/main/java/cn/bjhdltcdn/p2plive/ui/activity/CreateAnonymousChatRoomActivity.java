package cn.bjhdltcdn.p2plive.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.AnonymousChatRoomStatusEvent;
import cn.bjhdltcdn.p2plive.event.AnonymousMsgEvent;
import cn.bjhdltcdn.p2plive.httpresponse.GetLabelListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UpdateChatRoomResponse;
import cn.bjhdltcdn.p2plive.model.ChatInfo;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.ChatRoomPresenter;
import cn.bjhdltcdn.p2plive.mvp.presenter.DiscoverPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.adapter.RoomLabelTabsRecyclerViewAdapter;
import cn.bjhdltcdn.p2plive.ui.fragment.TitleFragment;
import cn.bjhdltcdn.p2plive.utils.RongIMutils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.FlowLayoutManager;
import cn.bjhdltcdn.p2plive.widget.LinearLayoutSpaceItemDecoration;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * 创建匿名聊天房
 * Created by ZHUDI on 2017/11/23.
 */

public class CreateAnonymousChatRoomActivity extends BaseActivity implements BaseView, RadioGroup.OnCheckedChangeListener {
    private DiscoverPresenter discoverPresenter;
    private ChatRoomPresenter chatRoomPresenter;
    private EditText titelEditView;
    private RecyclerView labelView;
    private RoomLabelTabsRecyclerViewAdapter roomLabelTabsRecyclerViewAdapter;
    /**
     * 校友限制(0--->不限制,1--->校友),
     */
    private int schoolLimit;
    /**
     * 性别限制(0--->不限制,1--->男,2--->女)
     */
    private int sexLimit;
    /**
     * 自己创建的聊天室
     */
    private ChatInfo chatInfo;

    public DiscoverPresenter getDiscoverPresenter() {
        if (discoverPresenter == null) {
            discoverPresenter = new DiscoverPresenter(this);
        }
        return discoverPresenter;
    }

    public ChatRoomPresenter getChatRoomPresenter() {
        if (chatRoomPresenter == null) {
            chatRoomPresenter = new ChatRoomPresenter(this);
        }
        return chatRoomPresenter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatInfo = getIntent().getParcelableExtra(Constants.Fields.CHAT_INFO);
        setContentView(R.layout.activity_create_room_anonymous);
        setTitle();
        init();
        getDiscoverPresenter().getLabelList(11);
    }

    private void init() {
        titelEditView = findViewById(R.id.edit_title);
        labelView = findViewById(R.id.recycler_label);
        final TextView createView = findViewById(R.id.tv_create);
        RadioGroup schoolmateRadioGroup = findViewById(R.id.rg_schoolmate);
        RadioGroup sexRadioGroup = findViewById(R.id.rg_sex);
        RadioButton allRadioButton = findViewById(R.id.rbtn_all);
        RadioButton schoolmateRadioButton = findViewById(R.id.rbtn_schoolmate);
        RadioButton allSexRadioButton = findViewById(R.id.rbtn_all_sex);
        RadioButton manRadioButton = findViewById(R.id.rbtn_man);
        RadioButton womenRadioButton = findViewById(R.id.rbtn_women);
        schoolmateRadioGroup.setOnCheckedChangeListener(this);
        sexRadioGroup.setOnCheckedChangeListener(this);
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(this);
        flowLayoutManager.setAutoMeasureEnabled(true);
        labelView.setLayoutManager(flowLayoutManager);
        labelView.addItemDecoration(new LinearLayoutSpaceItemDecoration(Utils.dp2px(20)));
        roomLabelTabsRecyclerViewAdapter = new RoomLabelTabsRecyclerViewAdapter(1);
        roomLabelTabsRecyclerViewAdapter.setOnClickListener(new RoomLabelTabsRecyclerViewAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                // 第一次查询
                int selectItemCount = roomLabelTabsRecyclerViewAdapter.getSelectItemCount();
                if (selectItemCount > 3) {
                    LabelInfo keywordInfo = roomLabelTabsRecyclerViewAdapter.getList().get(position);
                    keywordInfo.setIsSelect(0);
                    roomLabelTabsRecyclerViewAdapter.notifyItemChanged(position);
                    if (selectItemCount > 3) {
                        Utils.showToastShortTime("最多可选3个标签");
                    }
                }
            }
        });
        if (chatInfo != null) {
            if (!TextUtils.isEmpty(chatInfo.getChatName())) {
                titelEditView.setText(chatInfo.getChatName());
                createView.setBackgroundResource(R.color.color_ffee00);
            }
            if (chatInfo.getSchoolLimit() == 0) {
                allRadioButton.setChecked(true);
            } else if (chatInfo.getSchoolLimit() == 1) {
                schoolmateRadioButton.setChecked(true);
            }
            if (chatInfo.getSexLimit() == 0) {
                allSexRadioButton.setChecked(true);
            } else if (chatInfo.getSexLimit() == 1) {
                manRadioButton.setChecked(true);
            } else if (chatInfo.getSexLimit() == 2) {
                womenRadioButton.setChecked(true);
            }
        } else {
            allRadioButton.setChecked(true);
            allSexRadioButton.setChecked(true);
        }
        titelEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    createView.setBackgroundResource(R.color.color_ffee00);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        createView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isAllowClick()) {
                    if (TextUtils.isEmpty(getTitleEditText())) {
                        Utils.showToastShortTime("聊天主题不能为空");
                    } else {
                        String chatId = "0";
                        if (chatInfo != null) {
                            chatId = chatInfo.getChatId();
                        }
                        getChatRoomPresenter().updateChatRoom(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), chatId, getTitleEditText(), 0,
                                schoolLimit, sexLimit, roomLabelTabsRecyclerViewAdapter.getSelectLabelInfoList(), null);
                    }
                }
            }
        });

    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_GETLABELLIST:
                if (object instanceof GetLabelListResponse) {
                    GetLabelListResponse getLabelListResponse = (GetLabelListResponse) object;
                    if (getLabelListResponse.getCode() == 200) {
                        if (chatInfo != null) {
                            for (LabelInfo labelInfo : getLabelListResponse.getLabelList()) {
                                for (LabelInfo info : chatInfo.getLabelList()) {
                                    if (info.getLabelId() == labelInfo.getLabelId()) {
                                        labelInfo.setIsSelect(1);
                                    }
                                }
                            }
                        }
                        roomLabelTabsRecyclerViewAdapter.setList(getLabelListResponse.getLabelList());
                        labelView.setAdapter(roomLabelTabsRecyclerViewAdapter);
                    }
                }
                break;
            case InterfaceUrl.URL_UPDATECHATROOM:
                if (object instanceof UpdateChatRoomResponse) {
                    UpdateChatRoomResponse response = (UpdateChatRoomResponse) object;
                    Utils.showToastShortTime(response.getMsg());
                    if (response.getCode() == 200) {
                        final ChatInfo chatInfo = response.getChatInfo();
                        if (chatInfo != null) {
                            Constants.Object.CHATINFO = chatInfo;
                            RongIMutils.joinChatRoom(CreateAnonymousChatRoomActivity.this, chatInfo.getChatId(), false);
                            EventBus.getDefault().post(new AnonymousChatRoomStatusEvent(true));
                            UserInfo userInfo = new UserInfo(String.valueOf(chatInfo.getToBaseUser().getUserId()), chatInfo.getToBaseUser().getNickName(), Uri.parse(chatInfo.getToBaseUser().getUserIcon()));
                            RongIM.getInstance().refreshUserInfoCache(userInfo);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    EventBus.getDefault().post(new AnonymousMsgEvent(chatInfo.getToBaseUser(), null));
                                }
                            }, 1000);
                            finish();
                        }
                    }

                }
                break;
            default:
        }
    }

    private void setTitle() {
        TitleFragment titleFragment = (TitleFragment) getSupportFragmentManager().findFragmentById(R.id.title_fragment);
        titleFragment.setTitle("开启匿名聊天室", R.color.color_ffffff);
        titleFragment.setLeftViewTitle(R.mipmap.title_back_write_icon, new TitleFragment.LeftViewClick() {
            @Override
            public void onBackClick() {
                finish();
            }
        });
        titleFragment.setBackground(R.color.color_00000000);
    }


    /**
     * 获取标题
     *
     * @return
     */
    private String getTitleEditText() {
        return titelEditView.getText().toString();
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_schoolmate:
                if (checkedId == R.id.rbtn_all) {
                    schoolLimit = 0;
                } else if (checkedId == R.id.rbtn_schoolmate) {
                    schoolLimit = 1;
                }
                break;
            case R.id.rg_sex:
                if (checkedId == R.id.rbtn_all_sex) {
                    sexLimit = 0;
                } else if (checkedId == R.id.rbtn_man) {
                    sexLimit = 1;
                } else if (checkedId == R.id.rbtn_women) {
                    sexLimit = 2;
                }
                break;
            default:
        }
    }
}
