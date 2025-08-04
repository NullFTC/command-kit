// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package dev.nullftc.choreolib.geometry;

import java.util.Objects;

/**
 * Credit: <a href="https://github.com/wpilibsuite/allwpilib/blob/main/wpimath/src/main/java/edu/wpi/first/math/geometry/Twist2d.java">WPIMath</a>
 */
public class Twist2d {
    /** Linear "dx" component. */
    public double dx;

    /** Linear "dy" component. */
    public double dy;

    /** Angular "dtheta" component (radians). */
    public double dtheta;

    /** Default constructor. */
    public Twist2d() {}

    /**
     * Constructs a Twist2d with the given values.
     *
     * @param dx Change in x direction relative to robot.
     * @param dy Change in y direction relative to robot.
     * @param dtheta Change in angle relative to robot.
     */
    public Twist2d(double dx, double dy, double dtheta) {
        this.dx = dx;
        this.dy = dy;
        this.dtheta = dtheta;
    }

    @Override
    public String toString() {
        return String.format("Twist2d(dX: %.2f, dY: %.2f, dTheta: %.2f)", dx, dy, dtheta);
    }

    /**
     * Checks equality between this Twist2d and another object.
     *
     * @param obj The other object.
     * @return Whether the two objects are equal or not.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Twist2d)) {
            return false;
        }
        Twist2d other = (Twist2d) obj;
        return Math.abs(other.dx - dx) < 1E-9
                && Math.abs(other.dy - dy) < 1E-9
                && Math.abs(other.dtheta - dtheta) < 1E-9;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dx, dy, dtheta);
    }
}
