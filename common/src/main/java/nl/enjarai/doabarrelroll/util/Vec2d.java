package nl.enjarai.doabarrelroll.util;

public class Vec2d {
    public static final Vec2d ZERO = new Vec2d(0, 0);

    public final double x;
    public final double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d add(Vec2d other) {
        return new Vec2d(x + other.x, y + other.y);
    }

    public Vec2d subtract(Vec2d other) {
        return new Vec2d(x - other.x, y - other.y);
    }

    public Vec2d multiply(double scalar) {
        return new Vec2d(x * scalar, y * scalar);
    }

    public Vec2d divide(double scalar) {
        return new Vec2d(x / scalar, y / scalar);
    }

    public double dotProduct(Vec2d other) {
        return x * other.x + y * other.y;
    }

    public double crossProduct(Vec2d other) {
        return x * other.y - y * other.x;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public Vec2d negate() {
        return new Vec2d(-x, -y);
    }

    public Vec2d normalize() {
        return divide(length());
    }

    public Vec2d lerp(Vec2d other, double t) {
        return new Vec2d(x + (other.x - x) * t, y + (other.y - y) * t);
    }
}
