package nl.enjarai.doabarrelroll.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import nl.enjarai.doabarrelroll.DoABarrelRoll;
import nl.enjarai.doabarrelroll.api.event.RollEvents;
import nl.enjarai.doabarrelroll.api.event.StarFox64Events;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

import java.util.concurrent.atomic.AtomicInteger;

public class StarFoxUtil {
    private static final Random random = Random.create();
    private static final Identifier barrelRollSoundId = DoABarrelRoll.id("do_a_barrel_roll");
    private static final SoundEvent barrelRollSound = SoundEvent.of(barrelRollSoundId);
    private static final double rollTol = 90.0;

    // Tracks the roll direction. 0 = no roll, 1 = right, -1 = left
    private static double rollTracker = 0;

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, barrelRollSoundId, barrelRollSound);

        StarFox64Events.DOES_A_BARREL_ROLL.register(StarFoxUtil::playBarrelRollSound);

        RollEvents.LATE_CAMERA_MODIFIERS.register((rotationDelta, currentRotation) -> {
            trackRoll(rotationDelta, currentRotation);
            return rotationDelta;
        }, 999999);
    }

    private static void trackRoll(RotationInstant rotationDelta, RotationInstant currentRotation) {
        var player = MinecraftClient.getInstance().player;
        if (player != null && isFoxMcCloud(player)) {
            double cRoll = currentRotation.getRoll();
            double dRoll = rotationDelta.getRoll();

            // If the player crosses the threshold in any direction, set the roll tracker to that direction
            if (cRoll < rollTol && cRoll + dRoll >= rollTol) {
                rollTracker = 1;
            } else if (cRoll > -rollTol && cRoll + dRoll <= -rollTol) {
                rollTracker = -1;
            } else if (rollTracker != 0) {
                // Reset roll tracker if the player starts turning the other way
                if (rollTracker * dRoll < 0) {
                    rollTracker = 0;
                }

                // If the player has the roll tracker set, and crosses the opposite
                // threshold in the direction of the roll tracker, do a barrel roll
                if ((rollTracker > 0 && cRoll <= -rollTol) || (rollTracker < 0 && cRoll >= rollTol)) {
                    StarFox64Events.doesABarrelRoll(player);
                    rollTracker = 0;
                }
            }
        } else {
            rollTracker = 0;
        }
    }

    public static boolean isFoxMcCloud(PlayerEntity player) {
        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        return chestStack.isOf(Items.ELYTRA) && chestStack.getName().getString().equals("Arwing");
    }

    public static void playBarrelRollSound(PlayerEntity player) {
        player.world.playSoundFromEntity(
                player, player, barrelRollSound, SoundCategory.PLAYERS,
                1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );
    }
}
