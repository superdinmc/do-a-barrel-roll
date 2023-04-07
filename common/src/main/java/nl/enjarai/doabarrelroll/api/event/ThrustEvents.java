package nl.enjarai.doabarrelroll.api.event;

public interface ThrustEvents {
    /**
     * Use this event to register inputs that modify thrust.
     */
    Event<ModifyThrustInputEvent> MODIFY_THRUST_INPUT = new Event<>();

    interface ModifyThrustInputEvent {
        double modify(double input);
    }

    static double modifyThrustInput(double input) {
        for (var listener : MODIFY_THRUST_INPUT.getListeners()) {
            input = listener.modify(input);
        }

        return input;
    }
}
