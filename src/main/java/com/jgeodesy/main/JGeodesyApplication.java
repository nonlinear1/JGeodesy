package com.jgeodesy.main;

import com.jgeodesy.base.SphericalPoint;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.util.GeodesyUtil;

/**
 * Created by omeruluoglu on 25.10.2019.
 */
public class JGeodesyApplication {

    public static void main(String[] args) {
        SphericalPoint sphericalPoint = new SphericalPoint(new Latitude(52.205), new Longitude(0.119));
        SphericalPoint sphericalPoint2 = new SphericalPoint(new Latitude(48.857), new Longitude(2.351));

        // 404.3×10³ m
        double distance = sphericalPoint.distanceTo(sphericalPoint2, GeodesyUtil.getRadiusOfWorld());
        System.out.println(distance);

        // 251.2 miles
        double distance2 = sphericalPoint.distanceTo(sphericalPoint2, 3959);
        System.out.println(distance2);

        // 156.2°
        double initialBearing = sphericalPoint.initialBearingTo(sphericalPoint2);
        System.out.println(initialBearing);

        // 157.9°
        double finalBearing = sphericalPoint.finalBearingTo(sphericalPoint2);
        System.out.println(finalBearing);

        // 50.5363°N, 001.2746°E
        SphericalPoint midpoint = sphericalPoint.midpointTo(sphericalPoint2);
        System.out.println(midpoint.getLatitude().getDegrees());
        System.out.println(midpoint.getLongitude().getDegrees());

        // 51.3721°N, 000.7073°E
        SphericalPoint intermediatePoint = sphericalPoint.intermediatePointTo(sphericalPoint2, 0.25);
        System.out.println(intermediatePoint.getLatitude().getDegrees());
        System.out.println(intermediatePoint.getLongitude().getDegrees());

        SphericalPoint point1 = new SphericalPoint(new Latitude(51.47788), new Longitude(-0.00147));
        // 51.5136°N, 000.0983°W
        SphericalPoint destinationPoint = point1.destinationPoint(7794, 300.7, GeodesyUtil.getRadiusOfWorld());
        System.out.println(destinationPoint.getLatitude().getDegrees());
        System.out.println(destinationPoint.getLongitude().getDegrees());

        SphericalPoint sphericalPoint3 = new SphericalPoint(new Latitude(51.8853), new Longitude(0.2545));
        SphericalPoint sphericalPoint4 = new SphericalPoint(new Latitude(49.0034), new Longitude(2.5735));

        // 50.9078°N, 004.5084°E
        SphericalPoint intersection = SphericalPoint.intersection(sphericalPoint3, 108.547, sphericalPoint4, 32.435);
        System.out.println(intersection.getLatitude().getDegrees());
        System.out.println(intersection.getLongitude().getDegrees());

        SphericalPoint sphericalPoint5 = new SphericalPoint(new Latitude(53.2611), new Longitude(-0.7972));
        SphericalPoint sphericalPoint6 = new SphericalPoint(new Latitude(53.3206), new Longitude(-1.7297));
        SphericalPoint sphericalPoint7 = new SphericalPoint(new Latitude(53.1887), new Longitude(0.1334));

        // -307.5 m
        double crossTrackDistance = sphericalPoint5.crossTrackDistanceTo(sphericalPoint6, sphericalPoint7, GeodesyUtil.getRadiusOfWorld());
        System.out.println(crossTrackDistance);
    }
}
