package id.roleplay.bossbarextender;

import net.fabricmc.api.ClientModInitializer;

public final class BossBarExtenderClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BossBarExtenderConfig.load();
        UiStudioScreenSync.initialize();
    }
}
