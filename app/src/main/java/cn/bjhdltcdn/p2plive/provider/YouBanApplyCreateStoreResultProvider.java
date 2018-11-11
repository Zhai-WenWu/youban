package cn.bjhdltcdn.p2plive.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.JudgeCreateStoreAuthResponse;
import cn.bjhdltcdn.p2plive.model.YouBanApplyCreateStoreResultMessageModel;
import cn.bjhdltcdn.p2plive.mvp.presenter.GetStoreListPresenter;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.ui.activity.ApplyForShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.CreateShopActivity;
import cn.bjhdltcdn.p2plive.ui.activity.ShopDetailActivity;
import cn.bjhdltcdn.p2plive.utils.JsonUtil;
import cn.bjhdltcdn.p2plive.utils.ProgressDialogUtils;
import cn.bjhdltcdn.p2plive.utils.StringUtils;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import io.rong.imkit.YouBanApplyCreateStoreResultMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * 活动邀请函item
 * 自定义样式
 */

@ProviderTag(messageContent = YouBanApplyCreateStoreResultMessage.class)
public class YouBanApplyCreateStoreResultProvider extends IContainerItemProvider.MessageProvider implements BaseView{
    private GetStoreListPresenter getStoreListPresenter;
    private long userId;
    private Context context;
    public GetStoreListPresenter getGetStoreListPresenter() {
        if (getStoreListPresenter == null) {
            getStoreListPresenter = new GetStoreListPresenter(this);
        }
        return getStoreListPresenter;
    }
    @Override
    public void bindView(View view, int i, MessageContent messageContent, UIMessage uiMessage) {

        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

    }

    @Override
    public Spannable getContentSummary(MessageContent messageContent) {
        return new SpannableString("申请开店消息");
    }

    @Override
    public void onItemClick(View view, int i, MessageContent messageContent, UIMessage uiMessage) {
        context=view.getContext();
        Logger.d("view == " + view + "  uiMessage ==== " + uiMessage);

//        if (uiMessage.getMessageDirection() == Message.MessageDirection.RECEIVE) {
            if (messageContent instanceof YouBanApplyCreateStoreResultMessage) {

                YouBanApplyCreateStoreResultMessage youBanApplyCreateStoreResultMessage = (YouBanApplyCreateStoreResultMessage) messageContent;
                String content = youBanApplyCreateStoreResultMessage.getContent();

                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {
                    try {
                        YouBanApplyCreateStoreResultMessageModel messageModel = JsonUtil.getObjectMapper().readValue(content, YouBanApplyCreateStoreResultMessageModel.class);
                        userId = SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0);
                        if(messageModel.getMessageType()==1008){
                            //审核通过
                            if(messageModel.getStoreId()==0){
                                //未创建店铺
                                getGetStoreListPresenter().judgeCreateStoreAuth(userId);
                            }else{
                                //店铺详情界面
                                Intent intent5=new Intent(view.getContext(),ShopDetailActivity.class);
                                intent5.putExtra(Constants.Fields.STORE_ID,messageModel.getStoreId());
                                intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                view.getContext().startActivity(intent5);
                            }
                        }else if(messageModel.getMessageType()==1009){
                            //审核失败
                            getGetStoreListPresenter().judgeCreateStoreAuth(userId);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
//        }
    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View itemView = View.inflate(App.getInstance(), R.layout.text_message_item_layout, null);
        ItemViewHolder itemViewHolder = new ItemViewHolder();
        itemViewHolder.contentView = itemView.findViewById(R.id.content_view);
        itemViewHolder.forwardView = itemView.findViewById(R.id.forward_view);
        itemViewHolder.itemLayout = itemView.findViewById(R.id.item_layout);
        itemView.setTag(itemViewHolder);
        return itemView;

    }

    @Override
    public void bindView(View view, int i, Object object) {

        Logger.d("view == " + view + "  object ==== " + object);

        if (object instanceof UIMessage) {
            UIMessage uiMessage = (UIMessage) object;
            if (uiMessage.getContent() instanceof YouBanApplyCreateStoreResultMessage) {
                YouBanApplyCreateStoreResultMessage youBanApplyCreateStoreResultMessage = (YouBanApplyCreateStoreResultMessage) uiMessage.getContent();

                String content = youBanApplyCreateStoreResultMessage.getContent();
                Logger.json(content);

                if (!StringUtils.isEmpty(content)) {

                    try {

                        YouBanApplyCreateStoreResultMessageModel messageModel = JsonUtil.getObjectMapper().readValue(content, YouBanApplyCreateStoreResultMessageModel.class);

                        ItemViewHolder holder = (ItemViewHolder) view.getTag();

                        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_right);
                        } else {
                            holder.itemLayout.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
                        }

                        if (messageModel != null) {
                            holder.contentView.setText(messageModel.getMessageTips());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void updateView(String apiName, Object object) {
        if (apiName.equals(InterfaceUrl.URL_JUDGECREATESTOREAUTH)) {
            if (object instanceof JudgeCreateStoreAuthResponse) {
                JudgeCreateStoreAuthResponse judgeCreateStoreAuthResponse = (JudgeCreateStoreAuthResponse) object;
                if (judgeCreateStoreAuthResponse.getCode() == 200) {
                    //(1已开店,调用店铺详情; 2没有实名认证; 3已认证审核中; 4已认证审核拒绝; 5已认证未申请,调用申请开店; 6已认证申请审核中 7已认证申请拒绝,调用申请开店; 8已认证已申请未创建,调用创建店铺),
                    int isCreateStore=judgeCreateStoreAuthResponse.getIsCreateStore();
                    long storeId=judgeCreateStoreAuthResponse.getStoreId();
                    switch (isCreateStore){
                        case 1:
                            //已有店铺
                            Intent intent=new Intent(context,ShopDetailActivity.class);
                            intent.putExtra(Constants.Fields.STORE_ID,storeId);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;
                        case 2:
                            break;
                        case 3:
                            //实名认证审核中
                            break;
                        case 4:
                            break;
                        case 5:
                            //跳转申请开店界面
                            Intent intent1=new Intent(context, ApplyForShopActivity.class);
                            intent1.putExtra(Constants.Fields.STORE_ID,storeId);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                            break;
                        case 6:
                            //申请开店审核中
                            if(storeId!=0){
                                Intent intent5=new Intent(context,ShopDetailActivity.class);
                                intent5.putExtra(Constants.Fields.STORE_ID,storeId);
                                intent5.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent5);
                            }else{
                                Intent intent2=new Intent(context,CreateShopActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent2);
                            }
                            break;
                        case 7:
                            //申请开店拒绝 跳转申请开店界面
                            Intent intent3=new Intent(context, ApplyForShopActivity.class);
                            intent3.putExtra(Constants.Fields.STORE_ID,storeId);
                            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent3);
                            break;
                        case 8:
                            //申请开店已通过 但未创建店铺
                            Intent intent4=new Intent(context,CreateShopActivity.class);
                            intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent4);
                            break;
                    }

                } else {
                    Utils.showToastShortTime(judgeCreateStoreAuthResponse.getMsg());
                }
            }
        }
    }

    @Override
    public void showLoading() {
        Activity activity = null;
        if(context instanceof Activity){
            activity= (Activity) context;
        }
        ProgressDialogUtils.getInstance().showProgressDialog(activity);
    }

    @Override
    public void hideLoading() {
        ProgressDialogUtils.getInstance().hideProgressDialog();
    }


    static class ItemViewHolder {
        TextView contentView;
        TextView forwardView;
        View itemLayout;
    }

}
