package io.github.kosyakmakc.SocialBridge.TgBridge;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import dev.vanutp.tgbridge.common.TelegramBridge;
import io.github.kosyakmakc.socialBridge.ISocialBridge;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IModuleLoader;
import io.github.kosyakmakc.socialBridge.Modules.SocialModule;
import io.github.kosyakmakc.socialBridge.Utils.Version;

public class TgBridgeIntegrationModule extends SocialModule {
    public static final UUID ID = UUID.fromString("4952fe24-fd17-460b-8421-32ad5a739d86");
    private static final String NAME = "TgBridgeIntegrationModule";
    private static final Version CompabilityVersion = new Version("0.9.1");

    private SocialBridgeIntegration integrationModule;

    public TgBridgeIntegrationModule(IModuleLoader loader) {
        super(loader, CompabilityVersion, ID, NAME);
    }

    @Override
    public CompletableFuture<Boolean> enable(ISocialBridge bridge) {
        var result = super.enable(bridge);
        integrationModule = new SocialBridgeIntegration(this);
        TelegramBridge.Companion.getINSTANCE().addModule(integrationModule);
        return result;
    }

    @Override
    public CompletableFuture<Boolean> disable() {
        var result = super.disable();
        integrationModule.disable();
        return result;
    }
}