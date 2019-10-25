package com.jgeodesy.util;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 22.10.2019.
 */
public class GeodesyUtil {

    /**
     * Two pi number
     */
    private static final Double PI_TIMES_2 = Math.PI * 2;

    /**
     * Half of pi number
     */
    private static final Double HALF_PI = Math.PI / 2;

    /**
     * Quarter of pi number
     */
    private static final Double QUARTER_PI = Math.PI / 4;

    /**
     * Spherical earth radius in meter
     */
    private static final Double RADIUS_OF_WORLD = 6371e3;

    private GeodesyUtil() {
    }

    /**
     * Wrap constrain degrees to range -270..+90
     * @param degrees degrees
     * @return wrapped degrees within range
     */
    public static Double wrapTo90(final double degrees) {
        return angleWrap(degrees, 90, 360);
    }

    /**
     * Convert radians to degrees and wrap constrain degrees to range -270..+90
     * @param radian radian
     * @return wrapped degrees within range
     */
    public static Double wrapRadianTo90(final double radian) {
        return angleWrap(Math.toDegrees(radian), 90, 360);
    }

    /**
     * Wrap constrain degrees to range -180..+180
     * @param degrees degrees
     * @return wrapped degrees within range
     */
    public static Double wrapTo180(final double degrees) {
        return angleWrap(degrees, 180, 360);
    }

    /**
     * Convert radians to degrees and wrap constrain degrees to range -180..+180
     * @param degrees degrees
     * @return wrapped degrees within range
     */
    public static Double wrapRadianTo180(final double degrees) {
        return angleWrap(Math.toDegrees(degrees), 180, 360);
    }

    /**
     * Wrap constrain degrees to range 0..+180
     * @param degrees degrees
     * @return wrapped degrees within range
     */
    public static Double wrapTo360(final double degrees) {
        return angleWrap(degrees, 360, 360);
    }

    /**
     * Convert radians to degrees and wrap constrain degrees to range 0..+180
     * @param degrees degrees
     * @return wrapped degrees within range
     */
    public static Double wrapRadianTo360(final double degrees) {
        return angleWrap(Math.toDegrees(degrees), 360, 360);
    }

    /**
     * Wrap radians to range -PI..+PI
     * @param radian radian angle
     * @return wrapped radian
     */
    public static Double wrapToPI(final double radian) {
        return angleWrap(radian, Math.PI, PI_TIMES_2);
    }

    /**
     * Wrap radians to range 0..+2PI
     * @param radian radian angle
     * @return wrapped radian
     */
    public static Double wrapToPITimes2(final double radian) {
        return angleWrap(radian, PI_TIMES_2, PI_TIMES_2);
    }

    /**
     * Wrap radians to range -3PI/2..+PI/2
     * @param radian radian angle
     * @return wrapped radian
     */
    public static Double wrapToHalfPI(final double radian) {
        return angleWrap(radian, HALF_PI, PI_TIMES_2);
    }

    /**
     * Compute the tangent of half angle.
     * @param radian angle
     * @return result
     */
    public static Double tanHalfAngle(final double radian) {
        return Math.tan(radian * 0.5);
    }

    /**
     * Compute the tangent of half angle, 90 degrees rotated.
     * @param radian angle
     * @return result
     */
    public static Double tanHalfAngleRotated(final double radian) {
        return Math.tan((radian + HALF_PI) * 0.5);
    }

    /**
     * Convert and wrap degrees to radians -PI..+PI
     * @param degrees degrees
     * @return wrapped radian
     */
    public static Double radiansPI(final double degrees) {
        return angleWrap(Math.toRadians(degrees), Math.PI, PI_TIMES_2);
    }

    /**
     * Convert and wrap degrees to radians 0..+2PI
     * @param degrees degrees
     * @return wrapped radian
     */
    public static Double radiansPITimes2(final double degrees) {
        return angleWrap(Math.toRadians(degrees), PI_TIMES_2, PI_TIMES_2);
    }

    /**
     * Convert and wrap degrees to radians -3PI/2..+PI/2
     * @param degrees degrees
     * @return wrapped radian
     */
    public static Double radiansHalfPI(final double degrees) {
        return angleWrap(Math.toRadians(degrees), HALF_PI, PI_TIMES_2);
    }

    /**
     * Convert meter to statute miles (SM)
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToStatuteMile(final double meter) {
        // 6.21369949e-4 == 1.0 / 1609.344
        return meter * 6.21369949e-4;
    }

    /**
     * Convert meter to nautical miles (NM)
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToNauticalMile(final double meter) {
        // 5.39956804e-4 == 1.0 / 1852.0
        return meter * 5.39956804e-4;
    }

    /**
     * Convert meter to kilo meter (km).
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToKiloMeter(final double meter) {
        return meter * 1.0e-3;
    }

    /**
     * Convert meter to US Survey Feet (ft)
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToUSFeet(final double meter) {
        // US Survey == 3937./1200. = 3.2808333333333333
        return meter * 3.2808333333333333;
    }

    /**
     * Convert meter to International Feet (ft)
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToInternationalFeet(final double meter) {
        return meter * 3.2808399;
    }

    /**
     * Convert distance to angle along equator.
     * @param meter meter
     * @return result
     */
    public static Double convertMeterToDegrees(final double meter) {
        return Math.toDegrees(meter / RADIUS_OF_WORLD);
    }

    /**
     * Convert US Survey feet to meter.
     * @param feet feet
     * @return result
     */
    public static Double convertUSFeetToMeter(final double feet) {
        // US Survey 1200./3937. == 0.3048006096012192
        return feet * 0.3048006096012192;
    }

    /**
     * Convert US Survey feet to meter.
     * @param feet feet
     * @return result
     */
    public static Double convertInternationalFeetToMeter(final double feet) {
        return feet * 0.3048;
    }

    /**
     * Float mod operation
     * @param f1 float number
     * @param f2 float number
     * @return mod result
     */
    public static Double floatMod(final double f1, final double f2) {
        return f1 % f2;
    }

    private static Double angleWrap(final double degrees, final double range, final double upperLimit) {
        if (range > degrees && degrees >= (degrees - upperLimit))
            return degrees;
        double mod = degrees % upperLimit;
        if (mod > range)
            mod -= upperLimit;
        return mod;
    }

    public static Double getPiTimes2() {
        return PI_TIMES_2;
    }

    public static Double getHalfPi() {
        return HALF_PI;
    }

    public static Double getQuarterPi() {
        return QUARTER_PI;
    }

    public static Double getRadiusOfWorld() {
        return RADIUS_OF_WORLD;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
