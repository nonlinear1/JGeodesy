package com.jgeodesy.coordinate;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 21.10.2019.
 */
public class Longitude extends Coordinate {

    /**
     *
     * @param degrees degrees
     */
    public Longitude(double degrees) {
        super(degrees);
        if (degrees > 180.0 || degrees < -180.0) {
            throw new ValueException("Longitude measurements range from 0 to (+/-) 180");
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
