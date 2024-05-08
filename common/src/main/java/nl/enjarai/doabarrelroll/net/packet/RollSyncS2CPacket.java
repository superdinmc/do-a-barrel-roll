package nl.enjarai.doabarrelroll.net.packet;

public interface RollSyncS2CPacket {
    int entityId();

    boolean rolling();

    float roll();
}
