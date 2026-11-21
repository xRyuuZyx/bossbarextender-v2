package id.roleplay.bossbarextender;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

/**
 * Sends the scaled GUI dimensions to UIStudio only when that plugin advertises
 * its incoming channel. UIStudio divides the received height by three when
 * calculating its own BossBar row cap, so send height * 3: this gives it the
 * same number of rows that the client-side extender can actually render.
 */
public final class UiStudioScreenSync {
    private static final Identifier CHANNEL = Identifier.of("uistudio", "screen");

    private static ClientPlayNetworkHandler lastHandler;
    private static int lastWidth = -1;
    private static int lastHeight = -1;

    private UiStudioScreenSync() {
    }

    public static void initialize() {
        PayloadTypeRegistry.playC2S().register(ScreenSyncPayload.ID, ScreenSyncPayload.CODEC);
        ClientTickEvents.END_CLIENT_TICK.register(UiStudioScreenSync::onEndTick);
    }

    private static void onEndTick(MinecraftClient client) {
        ClientPlayNetworkHandler handler = client.getNetworkHandler();
        if (handler == null) {
            lastHandler = null;
            lastWidth = -1;
            lastHeight = -1;
            return;
        }

        // UIStudio is a Paper plugin, not a Fabric server mod. Only send when
        // Bukkit has advertised the plugin channel to this client.
        if (!ClientPlayNetworking.canSend(CHANNEL)) {
            return;
        }

        int width = Math.max(160, Math.min(8192, client.getWindow().getScaledWidth()));
        int actualHeight = Math.max(120, Math.min(1440, client.getWindow().getScaledHeight()));

        if (handler != lastHandler || width != lastWidth || actualHeight != lastHeight) {
            int virtualHeight = Math.min(4320, actualHeight * 3);
            ClientPlayNetworking.send(new ScreenSyncPayload(width, virtualHeight));

            lastHandler = handler;
            lastWidth = width;
            lastHeight = actualHeight;
        }
    }
}
