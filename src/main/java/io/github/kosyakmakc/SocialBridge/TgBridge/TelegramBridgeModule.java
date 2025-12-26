package io.github.kosyakmakc.SocialBridge.TgBridge;

import dev.vanutp.tgbridge.common.Function1;
import dev.vanutp.tgbridge.common.TgbridgeEvents;
import dev.vanutp.tgbridge.common.models.TgbridgeTgChatMessageEvent;
import dev.vanutp.tgbridge.common.modules.ITgbridgeModule;
import io.github.kosyakmakc.socialBridge.AuthSocial.AuthModule;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.Identifier;
import io.github.kosyakmakc.socialBridge.SocialPlatforms.IdentifierType;
import io.github.kosyakmakc.socialBridgeTelegram.TelegramPlatform;
import kotlin.Pair;

public class TelegramBridgeModule implements ITgbridgeModule {
    private final TgBridgeIntegrationModule tgBridgeIntegrationModule;

    private Function1<TgbridgeTgChatMessageEvent> handler;

    public TelegramBridgeModule(TgBridgeIntegrationModule tgBridgeIntegrationModule) {
        this.tgBridgeIntegrationModule = tgBridgeIntegrationModule;
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
            var logger = bridge.getLogger();
            logger.info("TG_CHAT_MESSAGE");
            var tgPlatform = bridge.getSocialPlatform(TelegramPlatform.class);
            var socialUser = tgPlatform.tryGetUser(new Identifier(IdentifierType.Long, x.getMessage().getFrom().getId())).join();
            if (socialUser == null) {
                logger.info("TG_CHAT_MESSAGE socialUser not found");
                return;
            }
            
            var module = bridge.getModule(AuthModule.class);
            var minecraftUser = module.tryGetMinecraftUser(socialUser).join();

            if (minecraftUser == null) {
                logger.info("TG_CHAT_MESSAGE minecraftUser not found");
                return;
            }

            logger.info("TG_CHAT_MESSAGE placeholder put - AuthModule-username=" + minecraftUser.getName());
            x.getPlaceholders().addPlain(new Pair<String,String>("AuthModule-username", minecraftUser.getName()));
        });
    }

    @Override
    public void disable() {
        if (handler != null) {
            TgbridgeEvents.INSTANCE.getTG_CHAT_MESSAGE().removeListener(handler);
        }

        handler = null;
    }

    public void destroy() {

    }

    @Override
    public boolean getCanBeDisabled() {
        return true;
    }

}
