package nl.enjarai.doabarrelroll.api.event;

import net.minecraft.client.network.ClientPlayerEntity;

public interface StarFox64Events {
    Event<DoesABarrelRollEvent> DOES_A_BARREL_ROLL = new Event<>();

    static void doesABarrelRoll(ClientPlayerEntity player) {
        for (var listener : DOES_A_BARREL_ROLL.getListeners()) {
            listener.onBarrelRoll(player);
        }
    }

    interface DoesABarrelRollEvent {
        void onBarrelRoll(ClientPlayerEntity player);
    }
}
