package com.jgeodesy.base;

import com.jgeodesy.coordinate.Coordinate;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.shape.Datum;
import com.jgeodesy.shape.Ellipsoid;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 23.10.2019.
 */
public class EllipsoidalPoint extends Point {

    private double height;
    private Datum datum;

    private static final String WGS84 = "WGS84";

    /**
     * Default datum initialize
     */
    public EllipsoidalPoint() {
        super();
        this.height = 0.0;
        this.datum = Datum.getDatum(WGS84);
    }

    /**
     * Constructs a point
     * @param latitude  given latitude
     * @param longitude given longitude
     * @param height    given height
     */
    public EllipsoidalPoint(final Latitude latitude, final Longitude longitude, final double height) {
        super(latitude, longitude);
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Datum getDatum() {
        return datum;
    }

    public void setDatum(Datum datum) {
        this.datum = datum;
    }

    /**
     * Converts ‘this’ cartesian (x/y/z) coordinate to (geodetic) latitude/longitude point on specified ellipsoid.
     * @param vector3D   3D vector
     * @param pointDatum point datum
     * @return EllipsoidalPoint Latitude/longitude point defined by cartesian coordinates, on given ellipsoid.
     */
    public EllipsoidalPoint convertToPoint(final Vector3D vector3D, Datum pointDatum) {
        double x = vector3D.getX();
        double y = vector3D.getY();
        double z = vector3D.getZ();

        // TODO Check datum type then what?
//        if (pointDatum.getEllipsoid() == Ellipsoid.WGS84) {
//        }
//            Ellipsoid currentEllipsoid = Ellipsoid.getEllipsoid(pointDatum.getEllipsoid().getName());
//            if (currentEllipsoid != Ellipsoid.WGS84)
//        }
        double a = pointDatum.getEllipsoid().getA();
        double b = pointDatum.getEllipsoid().getB();
        double f = pointDatum.getEllipsoid().getF();

        double e2 = 2.0 * f - f * f; // 1st eccentricity squared (a²−b²)/a²
        double epsilon2 = e2 / (1.0 - e2); // 2nd eccentricity squared ≡ (a²−b²)/b²
        double p = Math.sqrt(x * x + y * y); // distance from minor axis
        double r = Math.sqrt(p * p + z * z); // polar radius

        // parametric latitude (Bowring eqn 17, replacing tanBeta = z�a / p�b)
        double tanBeta = (b * z) / (a * p) * (1.0 + epsilon2 * b / r);
        double sinBeta = tanBeta / Math.sqrt(1 + tanBeta * tanBeta);
        double cosBeta = sinBeta / tanBeta;

        // geodetic latitude (Bowring eqn 18)
        double phi = Double.isNaN(cosBeta) ? 0 : Math.atan2(z + epsilon2 * b * sinBeta * sinBeta * sinBeta, p - e2 * a * cosBeta * cosBeta * cosBeta);

        // longitude
        double lambda = Math.atan2(y, x);

        // height above ellipsoid (Bowring eqn 7) [not currently used]
        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double nu = a / Math.sqrt(1 - e2 * sinPhi * sinPhi); // Length of the normal terminated by the minor axis
        double h = p * cosPhi + z * sinPhi - (a * a / nu);

        return new EllipsoidalPoint(new Latitude(Coordinate.toDegrees(phi)), new Longitude(Coordinate.toDegrees(lambda)), h);
    }

    /**
     * Converts ‘this’ point from (geodetic) latitude/longitude coordinates to (geocentric) cartesian (x/y/z) coordinates.
     * @return Vector3D Cartesian point equivalent to lat/lon point, with x, y, z in metres from earth centre.
     */
    public Vector3D convertToCartesianPoint() {
        double phi = getLatitude().getRadians();
        double lambda = getLongitude().getRadians();
        double h = this.height;

        Ellipsoid ellipsoid = this.datum.getEllipsoid();
        double a = ellipsoid.getA();
        double f = ellipsoid.getF();

        double sinPhi = Math.sin(phi);
        double cosPhi = Math.cos(phi);
        double sinLambda = Math.sin(lambda);
        double cosLambda = Math.cos(lambda);

        double eSq = 2.0 * f - f * f; // 1st eccentricity squared = (a²-b²)/a²
        double nu = a / Math.sqrt(1.0 - eSq * sinPhi * sinPhi); // Radius of curvature in prime vertical

        double x = (nu + h) * cosPhi * cosLambda;
        double y = (nu + h) * cosPhi * sinLambda;
        double z = (nu * (1.0 - eSq) + h) * sinPhi;
        return new Vector3D(x, y, z);
    }

    /**
     * Converts ‘this’ cartesian coordinate to new datum using Helmert 7-parameter transformation.
     * @param toDatum Datum this coordinate is to be converted to.
     * @return EllipsoidalPoint This point converted to new datum.
     */
    public EllipsoidalPoint convertToDatum(Datum toDatum) {
        Datum currentDatum = datum;

        if (currentDatum == toDatum)
            return this;

        Transform transform;
        if (currentDatum == Datum.getDatums().get(WGS84)) {
            // Converting from WGS 84
            transform = toDatum.getTransform();
        } else if (toDatum == Datum.getDatums().get(WGS84)) {
            // Converting to WGS 84; use inverse transform (don't overwrite original!)
            transform = currentDatum.getTransform();
        } else{
            // Neither this datum nor toDatum are WGS84: convert this to WGS84 first
            this.convertToDatum(Datum.getDatums().get(WGS84));
            transform = toDatum.getTransform();
        }

        Vector3D oldCartesian = this.convertToCartesianPoint(); // Convert polar to Cartesian
        Transform.applyTransform(oldCartesian, transform);
        return this.convertToPoint(oldCartesian, toDatum); // ...and convert Cartesian to polar
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        EllipsoidalPoint that = (EllipsoidalPoint) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(height, that.height)
                .append(datum, that.datum)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(height)
                .append(datum)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("height", height)
                .append("datum", datum)
                .toString();
    }
}
