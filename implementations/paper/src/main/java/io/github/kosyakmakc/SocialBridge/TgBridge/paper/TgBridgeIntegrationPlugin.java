package io.github.kosyakmakc.SocialBridge.TgBridge.paper;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.kosyakmakc.SocialBridge.TgBridge.TgBridgeIntegrationModule;
import io.github.kosyakmakc.socialBridge.SocialBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IModuleLoader;

public class TgBridgeIntegrationPlugin extends JavaPlugin implements IModuleLoader {
    private TgBridgeIntegrationModule module;

    @Override
    public void onEnable() {
        if (module != null) {
            SocialBridge.INSTANCE.disconnectModule(module).join();
        }

        module = new TgBridgeIntegrationModule(this);
        SocialBridge.INSTANCE.connectModule(module).join();
    }

    @Override
    public void onDisable() {
        if (module != null) {
            SocialBridge.INSTANCE.disconnectModule(module).join();
            module = null;
        }
    }
}