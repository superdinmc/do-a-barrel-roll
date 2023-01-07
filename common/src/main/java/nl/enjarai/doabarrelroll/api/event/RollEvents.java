package nl.enjarai.doabarrelroll.api.event;

import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

public interface RollEvents {
    /**
     * If any listener returns true, roll will be unlocked.
     */
    Event<ShouldRollCheckEvent> SHOULD_ROLL_CHECK = new Event<>();

    interface ShouldRollCheckEvent {
        boolean shouldRoll();
    }

    static boolean shouldRoll() {
        for (var listener : SHOULD_ROLL_CHECK.getListeners()) {
            if (listener.shouldRoll()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Modifiers registered here will be applied <b>before</b> sensitivity. So will be affected by it.
     */
    Event<CameraModifiersEvent> EARLY_CAMERA_MODIFIERS = new Event<>();

    static RotationInstant earlyCameraModifiers(RotationInstant rotationDelta) {
        for (var listener : EARLY_CAMERA_MODIFIERS.getListeners()) {
            rotationDelta = listener.applyCameraModifiers(rotationDelta);
        }

        return rotationDelta;
    }

    /**
     * Modifiers registered here will be applied <b>after</b> sensitivity. So will not be affected by it.
     */
    Event<CameraModifiersEvent> LATE_CAMERA_MODIFIERS = new Event<>();

    static RotationInstant lateCameraModifiers(RotationInstant rotationDelta) {
        for (var listener : LATE_CAMERA_MODIFIERS.getListeners()) {
            rotationDelta = listener.applyCameraModifiers(rotationDelta);
        }

        return rotationDelta;
    }

    interface CameraModifiersEvent {
        RotationInstant applyCameraModifiers(RotationInstant rotationDelta);
    }
}
