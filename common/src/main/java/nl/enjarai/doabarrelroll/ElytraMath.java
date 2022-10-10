package nl.enjarai.doabarrelroll;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import nl.enjarai.doabarrelroll.config.RotationInstant;

public class ElytraMath {

    public static final double TORAD = Math.PI / 180;
    public static final double TODEG = 1 / TORAD;

    public static void changeElytraLookDirectly(LocalPlayer player, RotationInstant rotation) {

        var pitch = rotation.getPitch();
        var yaw = rotation.getYaw();
        var roll = rotation.getRoll();

        Vec3 facing = player.getForward();
        DoABarrelRollClient.left = DoABarrelRollClient.left.subtract(facing.scale(DoABarrelRollClient.left.dot(facing))).normalize();

        // pitch
        facing = rotateAxisAngle(facing, DoABarrelRollClient.left, -0.15 * pitch * TORAD);

        // yaw
        Vec3 up = facing.cross(DoABarrelRollClient.left);
        facing = rotateAxisAngle(facing, up, 0.15 * yaw * TORAD);
        DoABarrelRollClient.left = rotateAxisAngle(DoABarrelRollClient.left, up, 0.15 * yaw * TORAD);

        // roll
        DoABarrelRollClient.left = rotateAxisAngle(DoABarrelRollClient.left, facing, 0.15 * roll * TORAD);


        double deltaY = -Math.asin(facing.y()) * TODEG - player.getXRot();
        double deltaX = -Math.atan2(facing.x(), facing.z()) * TODEG - player.getYRot();

        player.turn(deltaX / 0.15, deltaY / 0.15);

        // fix hand spasm when wrapping yaw value
        if (player.getYRot() < -90 && player.yBob > 90) {
            player.yBob -= 360;
            player.yBobO -= 360;
        } else if (player.getYRot() > 90 && player.yBob < -90) {
            player.yBob += 360;
            player.yBobO += 360;
        }
    }

    public static Vec3 getAssumedLeft(float yaw) {
        yaw *= TORAD;
        return new Vec3(-Math.cos(yaw), 0, -Math.sin(yaw));
    }

    public static Vec3 rotateAxisAngle(Vec3 v, Vec3 axis, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0 - c;

        //double l = axis.lengthSquared();
        //if (l == 0) return v;
        //if (l != 1) axis = axis.multiply(1/Math.sqrt(l));

        double x = (c + axis.x*axis.x*t) * v.x(),
                y = (c + axis.y*axis.y*t) * v.y(),
                z = (c + axis.z*axis.z*t) * v.z(),
                tmp1 = axis.x*axis.y*t,
                tmp2 = axis.z*s;
        y += (tmp1 + tmp2) * v.x();
        x += (tmp1 - tmp2) * v.y();
        tmp1 = axis.x*axis.z*t;
        tmp2 = axis.y*s;
        z += (tmp1 - tmp2) * v.x();
        x += (tmp1 + tmp2) * v.z();
        tmp1 = axis.y*axis.z*t;
        tmp2 = axis.x*s;
        z += (tmp1 + tmp2) * v.y();
        y += (tmp1 - tmp2) * v.z();

        return new Vec3(x, y, z);
    }

    public static double getRoll(float yaw, Vec3 left) {
        double angle = -Math.acos(Mth.clamp(left.dot(getAssumedLeft(yaw)), -1, 1)) * TODEG;
        if (left.y() < 0) angle *= -1;
        return angle;
    }
}
