package nl.enjarai.doabarrelroll.fabric.data;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

public interface RollComponent extends Component, AutoSyncedComponent {
    double getRoll();
    void setRoll(double roll);
}
