package cn.bjhdltcdn.p2plive.widget.imkit;

import java.util.List;

import cn.bjhdltcdn.p2plive.ui.fragment.ChatConversationFragment;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * Created by Hu_PC on 2017/8/24.
 */

public class CallExtensionModule extends DefaultExtensionModule {
    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        pluginModules.remove(pluginModules.get(1));

        videoPlugin videoPlugin = new videoPlugin();
        pluginModules.add(videoPlugin);

//        GiftPlugin giftPlugin = new GiftPlugin();
//        pluginModules.add(giftPlugin);

        CallPlugin callPlugin = new CallPlugin();
        pluginModules.add(callPlugin);

        return pluginModules;
    }


}

