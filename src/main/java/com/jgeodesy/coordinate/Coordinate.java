package com.jgeodesy.coordinate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Created by omeruluoglu on 21.10.2019.
 */
public class Coordinate {

    private static double radiansInDegree = Math.PI / 180.0;
    private double degrees;
    private double radians;

    /**
     * @param degrees degrees
     */
    public Coordinate(double degrees) {
        this.degrees = degrees;
    }

    /**
     * Converts from radians to degrees
     * @param radians radians
     * @return Degree number
     */
    public static double toDegrees(double radians) {
        return radians / radiansInDegree;
    }

    /**
     * Converts from degress to radians
     * @param degrees degrees
     * @return Radian number
     */
    public static double toRadians(double degrees) {
        return degrees * radiansInDegree;
    }

    public double getDegrees() {
        return degrees;
    }

    public void setDegrees(double degrees) {
        this.degrees = degrees;
    }

    public double getRadians() {
        return radians;
    }

    public void setRadians(double radians) {
        this.radians = radians;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Coordinate that = (Coordinate) o;

        return new EqualsBuilder()
                .append(degrees, that.degrees)
                .append(radians, that.radians)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(degrees)
                .append(radians)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("degrees", degrees)
                .append("radians", radians)
                .toString();
    }
}
