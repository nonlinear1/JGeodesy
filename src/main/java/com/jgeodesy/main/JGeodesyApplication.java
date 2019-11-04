package com.jgeodesy.main;

import com.jgeodesy.base.SphericalPoint;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.util.GeodesyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by omeruluoglu on 25.10.2019.
 */
public class JGeodesyApplication {

    private static final Logger logger = LoggerFactory.getLogger(JGeodesyApplication.class);

    /**
     * Main JGeodesy app
     * @param args arguments
     */
    public static void main(String[] args) {
        SphericalPoint sphericalPoint = new SphericalPoint(new Latitude(52.205), new Longitude(0.119));
        SphericalPoint sphericalPoint2 = new SphericalPoint(new Latitude(48.857), new Longitude(2.351));

        // 404.3×10³ m
        double distance = sphericalPoint.distanceTo(sphericalPoint2, GeodesyUtil.getRadiusOfWorld());
        logger.info("Distance: {}", distance);

        // 251.2 miles
        double distance2 = sphericalPoint.distanceTo(sphericalPoint2, 3959);
        logger.info("Distance 2: {}", distance2);

        // 156.2°
        double initialBearing = sphericalPoint.initialBearingTo(sphericalPoint2);
        logger.info("Initial Bearing: {}", initialBearing);

        // 157.9°
        double finalBearing = sphericalPoint.finalBearingTo(sphericalPoint2);
        logger.info("Final Bearing: {}", finalBearing);

        // 50.5363°N, 001.2746°E
        SphericalPoint midpoint = sphericalPoint.midpointTo(sphericalPoint2);
        logger.info("Midpoint Latitude: {}", midpoint.getLatitude().getDegrees());
        logger.info("Midpoint Longitude: {}", midpoint.getLongitude().getDegrees());

        // 51.3721°N, 000.7073°E
        SphericalPoint intermediatePoint = sphericalPoint.intermediatePointTo(sphericalPoint2, 0.25);
        logger.info("Intermediate Point Latitude: {}", intermediatePoint.getLatitude().getDegrees());
        logger.info("Intermediate Point Longitude: {}", intermediatePoint.getLongitude().getDegrees());

        SphericalPoint point1 = new SphericalPoint(new Latitude(51.47788), new Longitude(-0.00147));
        // 51.5136°N, 000.0983°W
        SphericalPoint destinationPoint = point1.destinationPoint(7794, 300.7, GeodesyUtil.getRadiusOfWorld());
        logger.info("Destination Point Latitude: {}", destinationPoint.getLatitude().getDegrees());
        logger.info("Destination Point Longitude: {}", destinationPoint.getLongitude().getDegrees());

        SphericalPoint sphericalPoint3 = new SphericalPoint(new Latitude(51.8853), new Longitude(0.2545));
        SphericalPoint sphericalPoint4 = new SphericalPoint(new Latitude(49.0034), new Longitude(2.5735));

        // 50.9078°N, 004.5084°E
        SphericalPoint intersection = SphericalPoint.intersection(sphericalPoint3, 108.547, sphericalPoint4, 32.435);
        logger.info("Intersection Point Latitude: {}", intersection.getLatitude().getDegrees());
        logger.info("Intersection Point Longitude: {}", intersection.getLongitude().getDegrees());

        SphericalPoint sphericalPoint5 = new SphericalPoint(new Latitude(53.2611), new Longitude(-0.7972));
        SphericalPoint sphericalPoint6 = new SphericalPoint(new Latitude(53.3206), new Longitude(-1.7297));
        SphericalPoint sphericalPoint7 = new SphericalPoint(new Latitude(53.1887), new Longitude(0.1334));

        // -307.5 m
        double crossTrackDistance = sphericalPoint5.crossTrackDistanceTo(sphericalPoint6, sphericalPoint7, GeodesyUtil.getRadiusOfWorld());
        logger.info("Cross track distance: {}", crossTrackDistance);

        // 62.331 km
        double trackDistance = sphericalPoint5.alongTrackDistanceTo(sphericalPoint6, sphericalPoint7, GeodesyUtil.getRadiusOfWorld());
        double kiloMeterDistance = GeodesyUtil.convertMeterToKiloMeter(trackDistance);
        logger.info("Track distance in kilometer: {}", kiloMeterDistance);

        // 89
        SphericalPoint sphericalPoint8 = new SphericalPoint(new Latitude(0.0), new Longitude(0.0));
        double maxLatitude = sphericalPoint8.maxLatitude(1);
        logger.info("Maximum latitude: {}", maxLatitude);

        //  40.31 km
        SphericalPoint sphericalPoint9 = new SphericalPoint(new Latitude(51.127),new Longitude(1.338));
        SphericalPoint sphericalPoint10 = new SphericalPoint(new Latitude(50.964),new Longitude(1.853));
        double rhumbDistance = sphericalPoint9.rhumbDistanceTo(sphericalPoint10, GeodesyUtil.getRadiusOfWorld());
        double kiloMeterRhumbDistance = GeodesyUtil.convertMeterToKiloMeter(rhumbDistance);
        logger.info("Rhumb distance in kilo meter: {}", kiloMeterRhumbDistance);

        // 116.7°
        double bearing = sphericalPoint9.rhumbBearingTo(sphericalPoint10);
        logger.info("Bearin:{}", bearing);

        // 50.9642°N, 001.8530°E
        SphericalPoint rhumbDestinationPoint = sphericalPoint9.rhumbDestinationPoint(40300, 116.7, GeodesyUtil.getRadiusOfWorld());
        logger.info("Rhump destination point latitude: {}", rhumbDestinationPoint.getLatitude().getDegrees());
        logger.info("Rhump destination point longitude: {}", rhumbDestinationPoint.getLongitude().getDegrees());

        // 51.0455°N, 001.5957°E
        SphericalPoint rhumbMidpoint = sphericalPoint9.rhumbMidpointTo(sphericalPoint10);
        logger.info("Rhumb midpoint lat: {}", rhumbMidpoint.getLatitude().getDegrees());
        logger.info("Rhumb midpoint lon: {}", rhumbMidpoint.getLongitude().getDegrees());

        // 6.18e9 m²
        List<SphericalPoint> polygon = new ArrayList<>();
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(1.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(1.0)));
        double area = SphericalPoint.areaOf(polygon, GeodesyUtil.getRadiusOfWorld());
        logger.info("Sum of polygon's area: {}", area);

        Map<String, Double> parallels = SphericalPoint.crossingParallels(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)),
                new SphericalPoint(new Latitude(60.0), new Longitude(30.0)), 30);
        // 30°00′00″N, 009°35′39″E
        logger.info("Parallel lon1: {}", parallels.get("lon1"));
        // 30°00′00″N, 170°24′21″E
        logger.info("Parallel lon2: {}", parallels.get("lon2"));
    }
}
