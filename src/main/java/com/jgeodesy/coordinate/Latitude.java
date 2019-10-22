package com.jgeodesy.coordinate;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 21.10.2019.
 */
public class Latitude extends Coordinate {

    /**
     *
     * @param degrees degrees
     */
    public Latitude(double degrees) {
        super(degrees);
        if (degrees > 90.0 || degrees < -90.0) {
            throw new ValueException("Latitude measurements range from 0 to (+/-) 90");
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
