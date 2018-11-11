package cn.bjhdltcdn.p2plive.widget.imkit;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.orhanobut.logger.Logger;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.ui.dialog.PrivateChatGiftDialogFragment;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

public class GiftPlugin implements IPluginModule {
    private String targetId;

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context,R.drawable.private_message_gift_icon);
    }

    @Override
    public String obtainTitle(Context context) {
        return "礼物";
    }

    @Override
    public void onClick(final Fragment fragment, final RongExtension rongExtension) {
        Logger.e("videoPlugin onClick", "弹出礼物界面");
        this.targetId = rongExtension.getTargetId();
        PrivateChatGiftDialogFragment dialogFragment = new PrivateChatGiftDialogFragment();
        dialogFragment.setTargetId(this.targetId);
        dialogFragment.show(fragment.getChildFragmentManager());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.e("onActivityResult", "返回聊天界面");
    }


}