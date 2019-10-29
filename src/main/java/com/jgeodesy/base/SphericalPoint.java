package com.jgeodesy.base;

import com.jgeodesy.coordinate.Coordinate;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.util.GeodesyUtil;

import java.util.List;
import java.util.Vector;

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

    /**
     * Returns the distance along the surface of the earth from ‘this’ point to destination point
     * Uses haversine formula: a = sin²(Δφ/2) + cosφ1·cosφ2 · sin²(Δλ/2); d = 2 · atan2(√a, √(a-1))
     * @param sphericalPoint Latitude/longitude of destination point
     * @param radius Radius of earth (defaults to mean radius in metres)
     * @return Distance between this point and destination point, in same units as radius
     */
    public Double distanceTo(final SphericalPoint sphericalPoint, double radius) {
        // see mathforum.org/library/drmath/view/51879.html for derivation
        double phi1 = getLatitude().getRadians();
        double lambda1 = getLongitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double lambda2 = sphericalPoint.getLongitude().getRadians();
        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;
        double a = Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0) +
                Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return radius * c;
    }

    /**
     * Returns the initial bearing from ‘this’ point to destination point
     * @param sphericalPoint Latitude/longitude of destination point
     * @return Initial bearing in degrees from north (0°..360°)
     */
    public Double initialBearingTo(final SphericalPoint sphericalPoint) {
        // see mathforum.org/library/drmath/view/55417.html for derivation
        double phi1 = getLatitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double deltaLambda = sphericalPoint.getLongitude().getRadians()- getLongitude().getRadians();
        double y = Math.sin(deltaLambda) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos(deltaLambda);
        double theta = Math.atan2(y, x);
        return GeodesyUtil.wrapTo360(Coordinate.toDegrees(theta));
    }

    /**
     * Returns final bearing arriving at destination point from ‘this’ point; the final bearing will
     * differ from the initial bearing by varying degrees according to distance and latitude
     * @param sphericalPoint Latitude/longitude of destination point
     * @return Final bearing in degrees from north (0°..360°).
     */
    public Double finalBearingTo(final SphericalPoint sphericalPoint) {
        // Get initial bearing from destination point to this point & reverse it by adding 180�
        double bearing = sphericalPoint.initialBearingTo(this) + 180;
        return GeodesyUtil.wrapTo360(bearing);
    }

    /**
     * Returns the midpoint between ‘this’ point and destination point.
     * @param sphericalPoint Latitude/longitude of destination point
     * @return Midpoint between this point and destination point.
     */
    public SphericalPoint midpointTo(final SphericalPoint sphericalPoint) {
        // φm = atan2( sinφ1 + sinφ2, √( (cosφ1 + cosφ2⋅cosΔλ)² + cos²φ2⋅sin²Δλ ) )
        // λm = λ1 + atan2(cosφ2⋅sinΔλ, cosφ1 + cosφ2⋅cosΔλ)
        // midpoint is sum of vectors to two points: mathforum.org/library/drmath/view/51822.html
        double phi1 = getLatitude().getRadians();
        double lambda1 = getLongitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double deltaLambda = sphericalPoint.getLongitude().getRadians() - getLongitude().getRadians();

        // get cartesian coordinates for the two points
        double bX = Math.cos(phi2) * Math.cos(deltaLambda);
        double bY = Math.cos(phi2) * Math.sin(deltaLambda);
        double bZ = Math.sin(phi2);

        double aX = Math.cos(phi1);
        double aY = 0.0;
        double aZ = Math.sin(phi1);

        double cX = aX + bX;
        double cY = aY + bY;
        double cZ = aZ + bZ;

        double phi3 = Math.atan2(cZ, Math.sqrt(cX * cX + cY * cY));
        double lambda3 = lambda1 + Math.atan2(cY, cY);

        double lat = Coordinate.toDegrees(phi3);
        double lon = Coordinate.toDegrees(lambda3);

        return new SphericalPoint(new Latitude(lat), new Longitude(lon));
    }

    /**
     * Returns the point at given fraction between ‘this’ point and given point
     * @param sphericalPoint Latitude/longitude of destination point
     * @param fraction Fraction between the two points (0 = this point, 1 = specified point)
     * @return Intermediate point between this point and destination point
     */
    public SphericalPoint intermediatePointTo(final SphericalPoint sphericalPoint, final Double fraction) {
        double phi1 = getLatitude().getRadians();
        double phi2 = sphericalPoint.getLatitude().getRadians();
        double lambda1 = getLongitude().getRadians();
        double lambda2 = sphericalPoint.getLongitude().getRadians();

        // Distance between points
        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        double a = Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0) +
                Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0);
        double sigma = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        double A = Math.sin((1.0 - fraction) * sigma) / Math.sin(sigma);
        double B = Math.sin(fraction * sigma) / Math.sin(sigma);

        double sinPhi1 = Math.sin(phi1);
        double sinPhi2 = Math.sin(phi2);
        double sinLambda1 = Math.sin(lambda1);
        double sinLambda2 = Math.sin(lambda2);
        double cosLambda1 = Math.cos(lambda1);
        double cosLambda2 = Math.cos(lambda2);
        double cosPhi1 = Math.cos(phi1);
        double cosPhi2 = Math.cos(phi2);

        double x = A * cosPhi1 * cosLambda1 + B * cosPhi2 * cosLambda2;
        double y = A * cosPhi1 * sinLambda1 + B * cosPhi2 * sinLambda2;
        double z = A * sinPhi1 + B * sinPhi2;

        double phi3 = Math.atan2(z, Math.sqrt(x * x + y * y));
        double lambda3 = Math.atan2(y, x);

        double lat = Coordinate.toDegrees(phi3);
        double lon = Coordinate.toDegrees(lambda3);
        return new SphericalPoint(new Latitude(lat), new Longitude(lon));
    }

    /**
     * Returns the destination point from ‘this’ point having travelled the given distance on the
     * given initial bearing (bearing normally varies around path followed)
     * @param distance Distance travelled, in same units as earth radius (default: metres)
     * @param bearing Initial bearing in degrees from north
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return Destination point
     */
    public SphericalPoint destinationPoint(final double distance, final double bearing, final double radius) {
        // sinφ2 = sinφ1⋅cosδ + cosφ1⋅sinδ⋅cosθ
        // tanΔλ = sinθ⋅sinδ⋅cosφ1 / cosδ−sinφ1⋅sinφ2
        // see mathforum.org/library/drmath/view/52049.html for derivation

        double sigma = distance / radius; // angular distance in radians
        double theta = Coordinate.toRadians(bearing);

        double phi1 = getLatitude().getRadians();
        double lambda1 = getLongitude().getRadians();

        double sinPhi1 = Math.sin(phi1);
        double cosSigma = Math.cos(sigma);
        double cosPhi1 = Math.cos(phi1);
        double sinSigma = Math.sin(sigma);
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);

        double sinPhi2 = sinPhi1 * cosSigma + cosPhi1 * sinSigma * cosTheta;
        double phi2 = Math.asin(sinPhi2);
        double y = sinTheta * sinSigma * cosPhi1;
        double x = cosSigma - sinPhi1 * sinPhi2;
        double lambda2 = lambda1 + Math.atan2(y, x);

        double lat = Coordinate.toDegrees(phi2);
        double lon = Coordinate.toDegrees(lambda2);

        return new SphericalPoint(new Latitude(lat), new Longitude(lon));
    }

    /**
     * Returns the point of intersection of two paths defined by point and bearing
     * @param sphericalPoint1 First point
     * @param bearing1 Initial bearing from first point.
     * @param sphericalPoint2 Second point
     * @param bearing2 Initial bearing from second point
     * @return Destination point (null if no unique intersection defined)
     */
    public static SphericalPoint intersection(final SphericalPoint sphericalPoint1, final double bearing1, final SphericalPoint sphericalPoint2, final double bearing2) {
        // see www.edwilliams.org/avform.htm#Intersection

        double phi1 = sphericalPoint1.getLatitude().getRadians();
        double lambda1 = sphericalPoint1.getLongitude().getRadians();
        double phi2 = sphericalPoint2.getLatitude().getRadians();
        double lambda2 = sphericalPoint2.getLongitude().getRadians();
        double theta13 = Coordinate.toRadians(bearing1);
        double theta23 = Coordinate.toRadians(bearing2);
        double deltaPhi = phi2 - phi1;
        double deltaLambda = lambda2 - lambda1;

        // angular distance p1-p2
        double sigma12 = 2.0 * Math.asin(Math.sqrt(Math.sin(deltaPhi / 2.0) * Math.sin(deltaPhi / 2.0) +
                Math.cos(phi1) * Math.cos(phi2) * Math.sin(deltaLambda / 2.0) * Math.sin(deltaLambda / 2.0)));
        // TODO Number.EPSILON smaller check
        if (sigma12 == 0) {
            return new SphericalPoint(new Latitude(sphericalPoint1.getLatitude().getDegrees()), new Longitude(sphericalPoint1.getLongitude().getDegrees()));
        }

        // initial/final bearings between points
        double cosThetaA = (Math.sin(phi2) - Math.sin(phi1)* Math.cos(sigma12)) / (Math.sin(sigma12) * Math.cos(phi1));
        double cosThetaB = (Math.sin(phi1) - Math.sin(phi2)* Math.cos(sigma12)) / (Math.sin(sigma12) * Math.cos(phi2));
        double thetaA = Math.acos(Math.min(Math.max(cosThetaA, -1.0), 1.0)); // protect against rounding errors
        double thetaB = Math.acos(Math.min(Math.max(cosThetaB, -1.0), 1.0)); // protect against rounding errors

        double theta12 = Math.sin(lambda2 - lambda1) > 0.0 ? thetaA : GeodesyUtil.getPiTimes2() - thetaA;
        double theta21 = Math.sin(lambda2 - lambda1) > 0.0 ? GeodesyUtil.getPiTimes2() - thetaB : thetaB;

        double alpha1 = theta13 - theta12; // angle 2-1-3
        double alpha2 = theta21 - theta23; // angle 1-2-3

        if (Math.sin(alpha1) == 0.0 && Math.sin(alpha2) == 0.0)
            return new SphericalPoint(); // Infinite intersections
        if (Math.sin(alpha1) * Math.sin(alpha2) < 0.0)
            return new SphericalPoint(); // Ambiguous intersection

        double alpha3 = Math.acos(-Math.cos(alpha1) * Math.cos(alpha2) + Math.sin(alpha1) * Math.sin(alpha2) * Math.cos(sigma12));
        double sigma13 = Math.atan2(Math.sin(sigma12) * Math.sin(alpha1) * Math.sin(alpha2), Math.cos(alpha2) + Math.cos(alpha1) * Math.cos(alpha3));
        double phi3 = Math.asin(Math.sin(phi1) * Math.cos(sigma13) + Math.cos(phi1) * Math.sin(sigma13) * Math.cos(theta13));
        double deltaLambda13 = Math.atan2(Math.sin(theta13) * Math.sin(sigma13) * Math.cos(phi1), Math.cos(sigma13) - Math.sin(phi1) * Math.sin(phi3));
        double lambda3 = lambda1 + deltaLambda13;

        double lat = Coordinate.toDegrees(phi3);
        double lon = Coordinate.toDegrees(lambda3);

        return new SphericalPoint(new Latitude(lat), new Longitude(lon)); // normalise to -180..+180
    }

    /**
     * Returns (signed) distance from ‘this’ point to great circle defined by start-point and end-point.
     * @param pathStart Start point of great circle path
     * @param pathEnd End point of great circle path
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return Distance to great circle (-ve if to left, +ve if to right of path)
     */
    public double crossTrackDistanceTo(final SphericalPoint pathStart, final SphericalPoint pathEnd, final double radius) {
        double d13 = pathStart.distanceTo(this, radius) / radius;
        double theta13 = Coordinate.toRadians(pathStart.initialBearingTo(this));
        double theta12 = Coordinate.toRadians(pathStart.initialBearingTo(pathEnd));
        double xt = Math.asin(Math.sin(d13) * Math.sin(theta13 - theta12));
        return xt * radius;
    }

    /**
     * Returns how far ‘this’ point is along a path from from start-point, heading towards end-point
     * That is, if a perpendicular is drawn from ‘this’ point to the (great circle) path,
     * the along-track distance is the distance from the start point to where the perpendicular crosses the path
     * @param pathStart Start point of great circle path
     * @param pathEnd End point of great circle path
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return Distance along great circle to point nearest ‘this’ point
     */
    public double alongTrackDistanceTo(final SphericalPoint pathStart, final SphericalPoint pathEnd, final double radius) {
        double d13 = pathStart.distanceTo(this, radius) / radius;
        double theta13 = Coordinate.toRadians(pathStart.initialBearingTo(this));
        double theta12 = Coordinate.toRadians(pathStart.initialBearingTo(pathEnd));

        double xt = Math.asin(Math.sin(d13) * Math.sin(theta13 - theta12));
        double at = Math.acos(Math.cos(d13) / Math.abs(Math.cos(xt)));
        double cosTheta = Math.cos(theta12 - theta13);
        if (cosTheta == 0.0)
            return 0.0;
        double dist = at * radius;
        return cosTheta > 0 ? dist : -dist;
    }

    /**
     * Returns maximum latitude reached when travelling on a great circle on given bearing from
     * ‘this’ point (‘Clairaut’s formula’). Negate the result for the minimum latitude (in the southern hemisphere)
     * The maximum latitude is independent of longitude; it will be the same for all points on a given latitude
     * @param bearing Initial bearing
     * @return Maximum latitude reached
     */
    public double maxLatitude(double bearing) {
        double theta = Coordinate.toRadians(bearing);
        double phi = getLatitude().getRadians();
        double phiMax = Math.acos(Math.abs(Math.sin(theta) * Math.cos(phi)));
        return Coordinate.toDegrees(phiMax);
    }

    /**
     * Returns the distance travelling from ‘this’ point to destination point along a rhumb line.
     * @param destinationPoint Latitude/longitude of destination point
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return Distance in km between this point and destination point (same units as radius)
     */
    public double rhumbDistanceTo(final SphericalPoint destinationPoint, final double radius) {
        // see www.edwilliams.org/avform.htm#Rhumb
        double phi1 = this.getLatitude().getRadians();
        double phi2 = destinationPoint.getLatitude().getRadians();
        double deltaPhi = phi2 - phi1;
        double deltaLambda = Math.abs(destinationPoint.getLongitude().getRadians() - this.getLongitude().getRadians());

        // If dLon over 180� take shorter rhumb line across the anti-meridian:
        if (deltaLambda > Math.PI)
            deltaLambda -= 2.0 * Math.PI;

        // On Mercator projection, longitude distances shrink by latitude; q is the 'stretch factor'
        // q becomes ill-conditioned along E-W line (0/0); use empirical tolerance to avoid it
        double deltaPsi = Math.log(Math.tan(phi2 / 2.0 + Math.PI / 4.0) / Math.tan(phi1 / 2.0 + Math.PI / 4.0));
        double q = Math.abs(deltaPsi) > 10e-12 ? deltaPhi / deltaPsi : Math.cos(phi1);

        // Distance is Pythagoras on 'stretched' Mercator projection
        double sigma = Math.sqrt(deltaPhi * deltaPhi + q * q * deltaLambda * deltaLambda); // angular distance in radians
        return sigma * radius;
    }

    /**
     * Returns the bearing from ‘this’ point to destination point along a rhumb line
     * @param destinationPoint Returns the bearing from ‘this’ point to destination point along a rhumb line
     * @return Bearing in degrees from north
     */
    public double rhumbBearingTo(SphericalPoint destinationPoint) {
        double phi1 = this.getLatitude().getRadians();
        double phi2 = destinationPoint.getLatitude().getRadians();
        double deltaLambda = destinationPoint.getLongitude().getRadians() - this.getLongitude().getRadians();
        // If dLon over 180� take shorter rhumb line across the anti-meridian:
        if (deltaLambda > Math.PI)
            deltaLambda -= 2.0 * Math.PI;

        if (deltaLambda < -Math.PI)
            deltaLambda += 2.0 * Math.PI;

        double deltaPsi = Math.log(Math.tan(phi2 / 2.0 + Math.PI / 4.0) / Math.tan(phi1 / 2.0 + Math.PI / 4.0));
        double theta = Math.atan2(deltaLambda, deltaPsi);
        return GeodesyUtil.wrapTo360(Coordinate.toDegrees(theta));
    }

    /**
     * Returns the destination point having travelled along a rhumb line from ‘this’ point the given distance on the given bearing
     * @param distance Distance travelled, in same units as earth radius (default: metres)
     * @param bearing Bearing in degrees from north
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return Destination point
     */
    public SphericalPoint rhumbDestinationPoint(final double distance, final double bearing, final double radius) {
        double sigma = distance / radius; // angular distance in radians
        double phi1 = this.getLatitude().getRadians();
        double lambda1 = this.getLongitude().getRadians();
        double theta = Coordinate.toRadians(bearing);

        double deltaPhi = sigma * Math.cos(theta);
        double phi2 = phi1 + deltaPhi;

        if (Math.abs(phi2) > GeodesyUtil.getHalfPi())
            phi2 = phi2 > 0 ? Math.PI - phi2 : -Math.PI - phi2;

        double deltaPsi = Math.log(Math.tan(phi2 / 2.0 + GeodesyUtil.getQuarterPi()) / Math.tan(phi1 / 2.0 + GeodesyUtil.getQuarterPi()));
        // E-W course becomes ill-conditioned with 0/0
        double q = Math.abs(deltaPsi) > 10e-12 ? deltaPhi / deltaPsi : Math.cos(phi1);
        double deltaLambda = sigma * Math.sin(theta) / q;
        double lambda2 = lambda1 + deltaLambda;

        double lat = Coordinate.toDegrees(phi2);
        double lon = Coordinate.toDegrees(lambda2);

        return new SphericalPoint(new Latitude(lat), new Longitude(lon)); // normalise to -180..+180
    }

    /**
     * Returns the loxodromic midpoint (along a rhumb line) between ‘this’ point and second point
     * @param secondPoint Latitude/longitude of second point
     * @return Midpoint between this point and second point
     */
    public SphericalPoint rhumbMidpointTo(SphericalPoint secondPoint) {
        // see mathforum.org/kb/message.jspa?messageID=148837
        double phi1 = this.getLatitude().getRadians();
        double lambda1 = this.getLongitude().getRadians();
        double phi2 = secondPoint.getLatitude().getRadians();
        double lambda2 = secondPoint.getLongitude().getRadians();

        if (Math.abs(lambda2 - lambda1) > Math.PI)
            lambda1 += GeodesyUtil.getPiTimes2(); // crossing anti-meridian

        double phi3 = (phi1 + phi2) / 2;
        double f1 = Math.tan(GeodesyUtil.getQuarterPi() + phi1 / 2.0);
        double f2 = Math.tan(GeodesyUtil.getQuarterPi() + phi2 / 2.0);
        double f3 = Math.tan(GeodesyUtil.getQuarterPi() + phi3 / 2.0);
        double lambda3 = ((lambda2 - lambda1) * Math.log(f3) + lambda1 * Math.log(f2) - lambda2 * Math.log(f1)) / Math.log(f2 / f1);

        if (!Double.isFinite(lambda3))
            lambda3 = (lambda1 + lambda2) / 2.0; // parallel of latitude

        double lat = Coordinate.toDegrees(phi3);
        double lon = Coordinate.toDegrees(lambda3);

        return new SphericalPoint(new Latitude(lat), new Longitude(lon)); // normalise to -180..+180
    }

    /**
     * Calculates the area of a spherical polygon where the sides of the polygon are great circle arcs joining the vertices
     * @param polygon Array of points defining vertices of the polygon
     * @param radius (Mean) radius of earth (defaults to radius in metres)
     * @return The area of the polygon in the same units as radius
     */
    public static double areaOf(final List<SphericalPoint> polygon, double radius) {
        // Uses method due to Karney: osgeo-org.1560.x6.nabble.com/Area-of-a-spherical-polygon-td3841625.html;
        // For each edge of the polygon, tan(E/2) = tan(Δλ/2)·(tan(φ₁/2)+tan(φ₂/2)) / (1+tan(φ₁/2)·tan(φ₂/2))
        // where E is the spherical excess of the trapezium obtained by extending the edge to the equator

        if (polygon.size() < 3)
            return 0.0;

        // Close polygon so that last point equals first point
        boolean closed = polygon.get(0) == polygon.get(polygon.size() - 1);
        if (!closed)
            polygon.add(polygon.get(0));

        double s = 0.0; // spherical excess in steradians
        for (int v = 0; v < polygon.size() - 1; v++) {
            double phi1 = polygon.get(v).getLatitude().getRadians();
            double phi2 = polygon.get(v + 1).getLatitude().getRadians();
            double deltaLambda = polygon.get(v + 1).getLongitude().getRadians() - polygon.get(v).getLongitude().getRadians();
            double e = 2.0 * Math.atan2(Math.tan(deltaLambda / 2.0) * (Math.tan(phi1 / 2.0) + Math.tan(phi2 / 2.0)),
            1.0 + Math.tan(phi1 / 2.0) * Math.tan(phi2 / 2.0));
            s += e;
        }

        // Whether polygon encloses pole: sum of course deltas around pole is 0° rather than
        // normal ±360°: blog.element84.com/determining-if-a-spherical-polygon-contains-a-pole.html
        // TODO: any better test than this?
        double sigmaDelta = 0.0;
        double previousBearing = polygon.get(0).initialBearingTo(polygon.get(1));
        for (int v = 0; v < polygon.size() - 1; v++) {
            double initBearing = polygon.get(v).initialBearingTo(polygon.get(v + 1));
            double finalBearing = polygon.get(v).finalBearingTo(polygon.get(v + 1));
            // TODO check wrapTo180 method
            sigmaDelta += GeodesyUtil.wrapTo180(initBearing - previousBearing);
            sigmaDelta += GeodesyUtil.wrapTo180(finalBearing - initBearing);
            previousBearing = finalBearing;
        }

        double initBearing = polygon.get(0).initialBearingTo(polygon.get(1));
        // TODO check wrapTo180 method
        sigmaDelta += GeodesyUtil.wrapTo180(initBearing - previousBearing);

        // TODO: fix (intermittent) edge crossing pole - eg (85,90), (85,0), (85,-90)
        if (Math.abs(sigmaDelta) < 90.0) // 0°-ish
            s = Math.abs(s) - GeodesyUtil.getPiTimes2();
        return Math.abs(s * radius * radius); // area in units of radius
    }
}
