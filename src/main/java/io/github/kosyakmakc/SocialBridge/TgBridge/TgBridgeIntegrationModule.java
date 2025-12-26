package io.github.kosyakmakc.SocialBridge.TgBridge;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import dev.vanutp.tgbridge.common.TelegramBridge;
import io.github.kosyakmakc.socialBridge.IBridgeModule;
import io.github.kosyakmakc.socialBridge.ISocialBridge;
import io.github.kosyakmakc.socialBridge.Commands.MinecraftCommands.IMinecraftCommand;
import io.github.kosyakmakc.socialBridge.Commands.SocialCommands.ISocialCommand;
import io.github.kosyakmakc.socialBridge.DatabasePlatform.DefaultTranslations.ITranslationSource;
import io.github.kosyakmakc.socialBridge.MinecraftPlatform.IModuleLoader;
import io.github.kosyakmakc.socialBridge.Utils.Version;

public class TgBridgeIntegrationModule implements IBridgeModule {
    public static final UUID ID = UUID.fromString("4952fe24-fd17-460b-8421-32ad5a739d86");
    private static final String NAME = "TgBridgeIntegrationModule";
    private static final Version CompabilityVersion = new Version("0.4.2");

    private final List<IMinecraftCommand> minecraftCommands = List.of();
    private final List<ISocialCommand> socialCommands = List.of();
    private final List<ITranslationSource> translations = List.of();
    private final IModuleLoader loader;

    private ISocialBridge bridge;
    private TelegramBridgeModule integrationModule;

    public TgBridgeIntegrationModule(IModuleLoader loader) {
        this.loader = loader;
    }

    @Override
    public CompletableFuture<Boolean> enable(ISocialBridge bridge) {
        this.bridge = bridge;
        integrationModule = new TelegramBridgeModule(this);
        TelegramBridge.Companion.getINSTANCE().addModule(integrationModule);;
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public CompletableFuture<Boolean> disable() {
        this.bridge = null;
        integrationModule.disable();
        integrationModule.destroy();
        return CompletableFuture.completedFuture(true);
    }

    @Override
    public ISocialBridge getBridge() {
        return bridge;
    }

    @Override
    public Collection<IMinecraftCommand> getMinecraftCommands() {
        return minecraftCommands;
    }

    @Override
    public Collection<ISocialCommand> getSocialCommands() {
        return socialCommands;
    }

    @Override
    public Collection<ITranslationSource> getTranslations() {
        return translations;
    }

    @Override
    public UUID getId() {
        return ID;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Version getCompabilityVersion() {
        return CompabilityVersion;
    }

    @Override
    public IModuleLoader getLoader() {
        return loader;
    }
}