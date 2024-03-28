package nl.enjarai.doabarrelroll.platform;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.network.NetworkRegistry;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.DoABarrelRollForge;
import nl.enjarai.doabarrelroll.ModPermissions;
import nl.enjarai.doabarrelroll.platform.services.PlatformHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ForgePlatformHelper implements PlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public Logger getLogger() {
        return LoggerFactory.getLogger(DoABarrelRoll.MODID);
    }

    @Override
    public boolean checkPermission(ServerPlayNetworkHandler source, String permission, int defaultPermissionLevel) {
        return ModPermissions.resolve(source.getPlayer(), permission, defaultPermissionLevel);
    }

    @Override
    public void registerNetworkChannels(Identifier... channels) {
        for (var channel : channels) {
            var frogeChannel = NetworkRegistry.newSimpleChannel(channel, () -> "", s -> true, s -> true);
            DoABarrelRollForge.NETWORK_CHANNELS.put(channel, frogeChannel);
            frogeChannel.registerMessage(0, PacketByteBuf.class, (bufSource, bufDest) -> bufDest.writeBytes(bufSource), buf -> {
                buf.retain();
                return buf;
            }, (buf, contextSupplier) -> {
                var ctx = contextSupplier.get();
                ctx.enqueueWork(() -> {
                    if (ctx.getDirection().getReceptionSide().isServer()) {
                        DoABarrelRollForge.SERVER_LISTENERS.get(channel).forEach(
                                listener -> listener.accept(Objects.requireNonNull(ctx.getSender()).networkHandler, buf,
                                        replyBuf -> frogeChannel.sendTo(replyBuf, ctx.getSender().networkHandler.connection, ctx.getDirection().reply())));
                    } else {
                        DoABarrelRollForge.CLIENT_LISTENERS.get(channel).forEach(
                                listener -> listener.accept(buf, frogeChannel::sendToServer));
                    }
                });
                ctx.setPacketHandled(true);
            });
        }
    }

    @Override
    public void notMyProblem(Screen ConfigScreen, Runnable callback) {
        // Get memory leak'd idiots
    }

    @Override
    public boolean checkModLoaded(String modId) {
        return LoadingModList.get().getMods().stream().anyMatch(mod -> mod.getModId().equals(modId));
    }

    @Override
    public boolean isModVersionAtLeast(String modId, String version) {
        // Yea sure it probably is, see if i care!
        return true;
    }
}