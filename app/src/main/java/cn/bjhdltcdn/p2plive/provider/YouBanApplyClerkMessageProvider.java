package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindUserApplyStatusResponse;
import cn.bjhdltcdn.p2plive.httpresponse.UploadImageResponse;
import cn.bjhdltcdn.p2plive.model.ApplyClerkMessage;
import cn.bjhdltcdn.p2plive.model.ApplyClert;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyClerkActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyClerkForMasterActivity;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.YouBanStoreClertMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by xiawenquan on 18/5/30.
 * 申请店员消息
 */

@ProviderTag(messageContent = YouBanStoreClertMessage.class)
public class YouBanApplyClerkMessageProvider extends IContainerItemProvider.MessageProvider implements BaseView {

    private AppCompatActivity mContext;
    private GetStoreListPresenter getStoreListPresenter;
    private ApplyClert applyClert;

    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {

        YouBanStoreClertMessage youBanStoreClertMessage = (YouBanStoreClertMessage) messageContent;

        // 内容
        String content = youBanStoreClertMessage.getContent();

        Spannable spannable = null;

        if (!StringUtils.isEmpty(content)) {
            try {
                final ApplyClerkMessage applyClerkMessage = JsonUtil.getObjectMapper().readValue(content, ApplyClerkMessage.class);
                if (applyClerkMessage != null) {
                    String tipText = "";
                    switch (applyClerkMessage.getMessageType()) {
                        case 1010://申请店员
                        case 1011://申请通过
                        case 1012://申请未通过
                        case 1013://申请超时
                        case 1014://解雇店员
                            tipText = applyClerkMessage.getMessageTips();
                            break;
                    }

                    spannable = new SpannableString(tipText);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        return spannable;

    }

    @Override
    public void onItemClick(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.mContext = (AppCompatActivity) context;

        View itemView = View.inflate(App.getInstance(), R.layout.product_order_message_public_item_layout, null);

        ItemViewHolder viewHolder = new ItemViewHolder();

        viewHolder.itemLayout = itemView.findViewById(R.id.layout_view_1);
        viewHolder.tipTextView = itemView.findViewById(R.id.tip_text_view_1);
        viewHolder.btnView3 = itemView.findViewById(R.id.btn_view_3);

        itemView.setTag(viewHolder);

        return itemView;
    }

    @Override
    public void bindView(View view, int i, Object object) {

        if (object instanceof UIMessage) {
            final UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanStoreClertMessage) {
                YouBanStoreClertMessage youBanStoreClertMessage = (YouBanStoreClertMessage) uiMessage.getContent();

                final String content = youBanStoreClertMessage.getContent();
                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {
                    try {
                        final ApplyClerkMessage applyClerkMessage = JsonUtil.getObjectMapper().readValue(content, ApplyClerkMessage.class);
                        if (applyClerkMessage != null) {


                            ItemViewHolder holder = (ItemViewHolder) view.getTag();
                            if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                            } else {
                                holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                            }

                            // 提示语
                            holder.tipTextView.setText(Html.fromHtml(applyClerkMessage.getMessageTips()));

                            // 按钮
                            holder.btnView3.setVisibility(View.GONE);
                            switch (applyClerkMessage.getMessageType()) {
                                case 1010://申请店员
                                    holder.btnView3.setText("立即查看");
                                    holder.btnView3.setVisibility(View.VISIBLE);
                                    break;
                                case 1011://申请通过
                                    break;
                                case 1012://申请未通过
                                    break;
                                case 1013://申请超时
                                    holder.btnView3.setText("重新发送");
                                    holder.btnView3.setVisibility(View.VISIBLE);
                                    break;
                                case 1014://解雇店员
                                    break;
                            }

                            holder.btnView3.setOnClickListener(new View.OnClickListener() {

                                private Intent intent;

                                @Override
                                public void onClick(View v) {

                                    if (mContext != null) {
                                        switch (applyClerkMessage.getMessageType()) {
                                            case 1010:
                                                intent = new Intent(mContext, ApplyClerkForMasterActivity.class);
                                                intent.putExtra(Constants.Fields.APPLY_ID, applyClerkMessage.getApplyClert().getApplyId());
                                                mContext.startActivity(intent);
                                                break;
                                            case 1013:
                                                applyClert = applyClerkMessage.getApplyClert();
                                                getStoreListPresenter().findUserApplyStatus(SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0), applyClert.getStoreId());
                                                break;
                                        }
                                    }
                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    public GetStoreListPresenter getStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }

    @Override
    public void updateView(String apiName, Object object) {
        switch (apiName) {
            case InterfaceUrl.URL_APPLYCLERT:
                if (object instanceof BaseResponse) {
                    BaseResponse baseResponse = (BaseResponse) object;
                    Utils.showToastShortTime(baseResponse.getMsg());
                }
                break;
            case InterfaceUrl.URL_FINDUSERAPPLYSTATUS:
                if (object instanceof FindUserApplyStatusResponse) {
                    FindUserApplyStatusResponse findUserApplyStatusResponse = (FindUserApplyStatusResponse) object;
                    if (findUserApplyStatusResponse.getCode() == 200) {
                        int status = findUserApplyStatusResponse.getStatus();
                        if (status == 0 || status == 4) {
                            getStoreListPresenter().applyClert(applyClert.getUserId(), applyClert.getStoreId(), applyClert.getPhoneNumber(), applyClert.getAddr(), applyClert.getSelfDesc(), applyClert.getCardFrontImg(), applyClert.getCardBackImg());
                        }
                    } else {
                        Utils.showToastShortTime(findUserApplyStatusResponse.getMsg());
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

    static class ItemViewHolder {

        View itemLayout;
        TextView tipTextView;
        Button btnView3;


    }
}
