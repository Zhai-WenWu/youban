package cn.bjhdltcdn.p2plive.widget.imkit;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

public class GiftExtensionModule extends DefaultExtensionModule {
    private videoPlugin videoPlugin;
    private GiftPlugin giftPlugin;

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules =  super.getPluginModules(conversationType);
        pluginModules.remove(pluginModules.get(1));
        videoPlugin=new videoPlugin();
        pluginModules.add(videoPlugin);
        giftPlugin=new GiftPlugin();
        pluginModules.add(giftPlugin);
        return pluginModules;
    }
}