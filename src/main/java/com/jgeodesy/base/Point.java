package com.jgeodesy.base;

import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 21.10.2019.
 */
public class Point {

    private Latitude latitude;
    private Longitude longitude;

    /**
     * Initialize point
     */
    public Point() {
        this.latitude = new Latitude(0.0);
        this.longitude = new Longitude(0.0);
    }

    /**
     * @param latitude  latitude
     * @param longitude longitude
     */
    public Point(final Latitude latitude, final Longitude longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns true if two points are equal and false otherwise.
     * @param point other point
     * @return Boolean
     */
    public final Boolean isEqual(final Point point) {
        final double epsilon = 0.0001;
        return Math.abs(latitude.getDegrees() - point.getLatitude().getDegrees()) < epsilon
                && Math.abs(longitude.getDegrees() - point.getLongitude().getDegrees()) < epsilon;
    }

    /**
     * Returns true if two points are different and false otherwise.
     * @param point other point
     * @return Boolean
     */
    public final Boolean isNotEqual(final Point point) {
        return this != point;
    }

    public Latitude getLatitude() {
        return latitude;
    }

    public void setLatitude(Latitude latitude) {
        this.latitude = latitude;
    }

    public Longitude getLongitude() {
        return longitude;
    }

    public void setLongitude(Longitude longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Point point = (Point) o;

        return new EqualsBuilder()
                .append(latitude, point.latitude)
                .append(longitude, point.longitude)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(latitude)
                .append(longitude)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("latitude", latitude)
                .append("longitude", longitude)
                .toString();
    }
}
