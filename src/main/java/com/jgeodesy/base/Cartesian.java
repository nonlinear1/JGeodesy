package com.jgeodesy.base;

import com.jgeodesy.coordinate.Coordinate;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.shape.Datum;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 22.10.2019.
 */
public class Cartesian extends Vector3D {

    /**
     * Creates cartesian coordinate representing ECEF (earth-centric earth-fixed) point
     * @param x X coordinate in metres (=> 0°N,0°E)
     * @param y Y coordinate in metres (=> 0°N,90°E)
     * @param z Z coordinate in metres (=> 90°N).
     */
    public Cartesian(double x, double y, double z) {
       super(x, y, z);
    }

    /**
     * Converts ‘this’ cartesian (x/y/z) coordinate to (geodetic) latitude/longitude point on specified ellipsoid.
     * @param vector3D   3D vector
     * @param pointDatum point datum
     * @return EllipsoidalPoint Latitude/longitude point defined by cartesian coordinates, on given ellipsoid.
     */
    public static EllipsoidalPoint convertToPoint(final Vector3D vector3D, Datum pointDatum) {
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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }
}
