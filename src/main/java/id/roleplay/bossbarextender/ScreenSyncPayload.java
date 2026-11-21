package id.roleplay.bossbarextender;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

/** Payload format expected by UIStudio's uistudio:screen Bukkit plugin channel. */
public record ScreenSyncPayload(int magic, int width, int height) implements CustomPayload {
    public static final int MAGIC = 1_430_868_785;
    public static final Id<ScreenSyncPayload> ID =
            new Id<>(Identifier.of("uistudio", "screen"));

    public static final PacketCodec<RegistryByteBuf, ScreenSyncPayload> CODEC =
            PacketCodec.of(ScreenSyncPayload::write, ScreenSyncPayload::new);

    public ScreenSyncPayload(int width, int virtualHeight) {
        this(MAGIC, width, virtualHeight);
    }

    private ScreenSyncPayload(RegistryByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt());
    }

    private void write(RegistryByteBuf buf) {
        buf.writeInt(this.magic);
        buf.writeInt(this.width);
        buf.writeInt(this.height);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
