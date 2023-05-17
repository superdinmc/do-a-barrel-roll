package nl.enjarai.doabarrelroll.data;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

public interface RollComponent extends Component, AutoSyncedComponent {
    double getRoll();

    void setRoll(double roll);

    double getLastRoll();

    void setLastRoll(double lastRoll);

    double getRoll(float tickDelta);

    boolean isFallFlying();

    void setFallFlying(boolean fallFlying);

    void tick();

    boolean hasClient();

    void setHasClient(boolean hasClient);
}
