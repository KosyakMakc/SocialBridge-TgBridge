package io.github.kosyakmakc.SocialBridge.TgBridge;

import dev.vanutp.tgbridge.common.Function1;
import dev.vanutp.tgbridge.common.TelegramBridge;
import dev.vanutp.tgbridge.common.TgbridgeEvents;
import dev.vanutp.tgbridge.common.models.TgbridgeTgChatMessageEvent;
import dev.vanutp.tgbridge.common.modules.AbstractModule;
import io.github.kosyakmakc.socialBridge.AuthSocial.AuthModule;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.Identifier;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.IdentifierType;
import io.github.kosyakmakc.socialBridgeTelegram.TelegramPlatform;
import kotlin.Pair;

public class SocialBridgeIntegration extends AbstractModule {
    private final TgBridgeIntegrationModule tgBridgeIntegrationModule;

    private Function1<TgbridgeTgChatMessageEvent> handler;

    public SocialBridgeIntegration(TgBridgeIntegrationModule tgBridgeIntegrationModule) {
        super(TelegramBridge.Companion.getINSTANCE());
        this.tgBridgeIntegrationModule = tgBridgeIntegrationModule;
    }

    @Override
    public String getPaperId() {
        return "SocialBridgeIntegration";
    }

    @Override
    public boolean shouldEnable() {
        return true;
    }

    @Override
    public void enable() {
        if (handler != null) {
            TgbridgeEvents.INSTANCE.getTG_CHAT_MESSAGE().removeListener(handler);
        }

        TgbridgeEvents.INSTANCE.getTG_CHAT_MESSAGE().addListener(handler = x -> {
            var bridge = tgBridgeIntegrationModule.getBridge();
            var tgPlatform = bridge.getSocialPlatform(TelegramPlatform.class);
            var socialUser = tgPlatform.tryGetUser(new Identifier(IdentifierType.Long, x.getMessage().getFrom().getId()), null).join();

            var authModuleUsername = x.getMessage().getFrom().getFullName(); // default tg name

            if (socialUser != null) {
                var module = bridge.getModule(AuthModule.class);
                var minecraftUser = module.tryGetMinecraftUser(socialUser, null).join();
                if (minecraftUser != null) {
                    authModuleUsername = minecraftUser.getName(); // or minecraft nick if authed
                }
            }

            x.setPlaceholders(x.getPlaceholders().addPlain(new Pair<String, String>(
                "authsocial-username",
                authModuleUsername)));
        });
    }

    @Override
    public void disable() {
        if (handler != null) {
            TgbridgeEvents.INSTANCE.getTG_CHAT_MESSAGE().removeListener(handler);
        }

        handler = null;
    }

    @Override
    public boolean getCanBeDisabled() {
        return true;
    }
}
