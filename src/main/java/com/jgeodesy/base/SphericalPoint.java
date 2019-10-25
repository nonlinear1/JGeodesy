package com.jgeodesy.base;

import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;

/**
 * Created by omeruluoglu on 24.10.2019.
 */
public class SphericalPoint extends Point {

    /**
     * Default initialize constructor
     */
    public SphericalPoint() {
        super();
    }

    /**
     * Initialize spherical point
     * @param latitude latitude
     * @param longitude longitude
     */
    public SphericalPoint(final Latitude latitude, final Longitude longitude) {
        super(latitude, longitude);
    }
}
