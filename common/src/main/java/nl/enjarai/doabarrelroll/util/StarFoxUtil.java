package nl.enjarai.doabarrelroll.util;

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

public class StarFoxUtil {
    private static final Random random = Random.create();
    private static final Identifier barrel_roll_sound_id = DoABarrelRoll.id("do_a_barrel_roll");
    private static final SoundEvent barrel_roll_sound = SoundEvent.of(barrel_roll_sound_id);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, barrel_roll_sound_id, barrel_roll_sound);

        StarFox64Events.DOES_A_BARREL_ROLL.register(StarFoxUtil::playBarrelRollSound);

        RollEvents.LATE_CAMERA_MODIFIERS.register((rotationDelta, currentRotation) -> {
            // TODO


            return rotationDelta;
        }, 999999);
    }

    public static boolean isFoxMcCloud(PlayerEntity player) {
        ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
        return chestStack.isOf(Items.ELYTRA) && chestStack.getName().getString().equals("Arwing");
    }

    public static void playBarrelRollSound(PlayerEntity player) {
        player.world.playSound(
                null, player.getBlockPos(), barrel_roll_sound, SoundCategory.PLAYERS,
                1.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F
        );
    }
}
