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
    private static final Version CompabilityVersion = new Version("0.10.0");

    private SocialBridgeIntegration integrationModule;

    public TgBridgeIntegrationModule(IModuleLoader loader, Version version) {
        super(loader, CompabilityVersion, version, ID, NAME);

        addDependancy(UUID.fromString("11752e9b-8968-42ca-8513-6ce3e52a27b4"), new Version("0.10.0")); // AuthSocial
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