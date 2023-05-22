package nl.enjarai.doabarrelroll.flight;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import nl.enjarai.doabarrelroll.DoABarrelRollClient;
import nl.enjarai.doabarrelroll.flight.util.RotationInstant;

public class ElytraMath {

    public static final double TORAD = Math.PI / 180;
    public static final double TODEG = 1 / TORAD;

    public static void changeElytraLookDirectly(ClientPlayerEntity player, RotationInstant rotation) {

    }

    public static Vec3d getAssumedLeft(float yaw) {
        yaw *= TORAD;
        return new Vec3d(-Math.cos(yaw), 0, -Math.sin(yaw));
    }

    public static Vec3d rotateAxisAngle(Vec3d v, Vec3d axis, double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        double t = 1.0 - c;

        //double l = axis.lengthSquared();
        //if (l == 0) return v;
        //if (l != 1) axis = axis.multiply(1/Math.sqrt(l));

        double x = (c + axis.x*axis.x*t) * v.getX(),
                y = (c + axis.y*axis.y*t) * v.getY(),
                z = (c + axis.z*axis.z*t) * v.getZ(),
                tmp1 = axis.x*axis.y*t,
                tmp2 = axis.z*s;
        y += (tmp1 + tmp2) * v.getX();
        x += (tmp1 - tmp2) * v.getY();
        tmp1 = axis.x*axis.z*t;
        tmp2 = axis.y*s;
        z += (tmp1 - tmp2) * v.getX();
        x += (tmp1 + tmp2) * v.getZ();
        tmp1 = axis.y*axis.z*t;
        tmp2 = axis.x*s;
        z += (tmp1 + tmp2) * v.getY();
        y += (tmp1 - tmp2) * v.getZ();

        return new Vec3d(x, y, z);

        // The above function explained simply does the following:
        // 1. Rotate the vector around the axis so that the axis is the y axis
    }

    public static double getRoll(float yaw, Vec3d left) {
//        double angle = -Math.acos(MathHelper.clamp(left.dotProduct(getAssumedLeft(yaw)), -1, 1)) * TODEG;
//        if (left.getY() < 0) angle *= -1;
//        return angle; TODO
        return 0;
    }
}
