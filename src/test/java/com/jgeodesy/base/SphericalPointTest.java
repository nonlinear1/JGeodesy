package com.jgeodesy.base;

import com.jgeodesy.AbstractUnitTest;
import com.jgeodesy.BaseUnitTest;
import com.jgeodesy.coordinate.Latitude;
import com.jgeodesy.coordinate.Longitude;
import com.jgeodesy.util.GeodesyUtil;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by omeruluoglu on 30.10.2019.
 */
public class SphericalPointTest extends AbstractUnitTest implements BaseUnitTest {

    private SphericalPoint sphericalPoint1;
    private SphericalPoint sphericalPoint2;
    private SphericalPoint sphericalPoint5;
    private SphericalPoint sphericalPoint6;
    private SphericalPoint sphericalPoint7;
    private SphericalPoint sphericalPoint9;
    private SphericalPoint sphericalPoint10;
    private DecimalFormat decimalFormatter;
    private DecimalFormat decimalFormatter2;

    @Override
    @Before
    public void setUp() {
        sphericalPoint1 = new SphericalPoint(new Latitude(52.205), new Longitude(0.119));
        sphericalPoint2 = new SphericalPoint(new Latitude(48.857), new Longitude(2.351));
        sphericalPoint5 = new SphericalPoint(new Latitude(53.2611), new Longitude(-0.7972));
        sphericalPoint6 = new SphericalPoint(new Latitude(53.3206), new Longitude(-1.7297));
        sphericalPoint7 = new SphericalPoint(new Latitude(53.1887), new Longitude(0.1334));
        sphericalPoint9 = new SphericalPoint(new Latitude(51.127),new Longitude(1.338));
        sphericalPoint10 = new SphericalPoint(new Latitude(50.964),new Longitude(1.853));
        decimalFormatter = new DecimalFormat("#.#");
        decimalFormatter2 = new DecimalFormat("#.####");
        decimalFormatter.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormatter2.setRoundingMode(RoundingMode.HALF_UP);
    }

    @Override
    public void clean() {
    }

    @Test
    public void test_distanceTo() {
        double distance1 = sphericalPoint1.distanceTo(sphericalPoint2, GeodesyUtil.getRadiusOfWorld());
        double distance2 = sphericalPoint1.distanceTo(sphericalPoint2, 3959);
        BigDecimal bd1 = new BigDecimal(distance1).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bd2 = new BigDecimal(distance2).setScale(2, RoundingMode.HALF_UP);
        BigDecimal bd = new BigDecimal(distance1);
        assertEquals(0, Double.compare(bd1.doubleValue(), 404279.16));
        assertEquals(0, Double.compare(bd2.doubleValue(), 251.22));
    }

    @Test
    public void test_initialBearingTo() {
        double initBearing = sphericalPoint1.initialBearingTo(sphericalPoint2);
        String bearing = decimalFormatter.format(initBearing);
        assertEquals(bearing,"156.2");
    }

    @Test
    public void test_finalBearingTo() {
        double finalBearing = sphericalPoint1.finalBearingTo(sphericalPoint2);
        String bearing = decimalFormatter.format(finalBearing);
        assertEquals(bearing, "157.9");
    }

    @Test
    public void test_midpointTo() {
        SphericalPoint midpoint = sphericalPoint1.midpointTo(sphericalPoint2);
        String midLat = decimalFormatter2.format(midpoint.getLatitude().getDegrees());
        assertEquals(midLat, "50.5363");
        String midLon = decimalFormatter2.format(midpoint.getLongitude().getDegrees());
        assertEquals(midLon, "1.2746");
    }

    @Test
    public void test_intermediatePointTo() {
        SphericalPoint intermediatePoint = sphericalPoint1.intermediatePointTo(sphericalPoint2, 0.25);
        String lat = decimalFormatter2.format(intermediatePoint.getLatitude().getDegrees());
        assertEquals(lat, "51.3721");
        String lon = decimalFormatter2.format(intermediatePoint.getLongitude().getDegrees());
        assertEquals(lon, "0.7073");
    }

    @Test
    public void test_destinationPoint() {
        SphericalPoint point1 = new SphericalPoint(new Latitude(51.47788), new Longitude(-0.00147));
        SphericalPoint destinationPoint = point1.destinationPoint(7794, 300.7, GeodesyUtil.getRadiusOfWorld());
        String lat = decimalFormatter2.format(destinationPoint.getLatitude().getDegrees());
        assertEquals(lat, "51.5136");
        String lon = decimalFormatter2.format(destinationPoint.getLongitude().getDegrees());
        assertEquals(lon, "-0.0983");
    }

    @Test
    public void test_intersection() {
        SphericalPoint sphericalPoint3 = new SphericalPoint(new Latitude(51.8853), new Longitude(0.2545));
        SphericalPoint sphericalPoint4 = new SphericalPoint(new Latitude(49.0034), new Longitude(2.5735));
        SphericalPoint intersection = SphericalPoint.intersection(sphericalPoint3, 108.547, sphericalPoint4, 32.435);
        String lat = decimalFormatter2.format(intersection.getLatitude().getDegrees());
        assertEquals(lat, "50.9078");
        String lon = decimalFormatter2.format(intersection.getLongitude().getDegrees());
        assertEquals(lon, "4.5084");
    }

    @Test
    public void test_crossTrackDistanceTo() {
        SphericalPoint sphericalPoint5 = new SphericalPoint(new Latitude(53.2611), new Longitude(-0.7972));
        SphericalPoint sphericalPoint6 = new SphericalPoint(new Latitude(53.3206), new Longitude(-1.7297));
        SphericalPoint sphericalPoint7 = new SphericalPoint(new Latitude(53.1887), new Longitude(0.1334));
        double crossTrackDistance = sphericalPoint5.crossTrackDistanceTo(sphericalPoint6, sphericalPoint7, GeodesyUtil.getRadiusOfWorld());
        assertEquals(decimalFormatter.format(crossTrackDistance), "-307.5");
    }

    @Test
    public void test_alongTrackDistanceTo() {
        double trackDistance = sphericalPoint5.alongTrackDistanceTo(sphericalPoint6, sphericalPoint7, GeodesyUtil.getRadiusOfWorld());
        double kiloMeterDistance = GeodesyUtil.convertMeterToKiloMeter(trackDistance);
        assertEquals(decimalFormatter.format(trackDistance), "62331.5");
        assertEquals(decimalFormatter2.format(kiloMeterDistance), "62.3315");
    }

    @Test
    public void test_maxLatitude() {
        SphericalPoint sphericalPoint8 = new SphericalPoint(new Latitude(0.0), new Longitude(0.0));
        double maxLatitude = sphericalPoint8.maxLatitude(1);
        assertEquals(Double.valueOf(maxLatitude), Double.valueOf(89.0));
    }

    @Test
    public void test_rhumbDistanceTo() {
        double rhumbDistance = sphericalPoint9.rhumbDistanceTo(sphericalPoint10, GeodesyUtil.getRadiusOfWorld());
        assertEquals(decimalFormatter.format(rhumbDistance), "40307.7");
        double kiloMeterRhumbDistance = GeodesyUtil.convertMeterToKiloMeter(rhumbDistance);
        assertEquals(decimalFormatter2.format(kiloMeterRhumbDistance), "40.3077");
    }

    @Test
    public void test_rhumbBearingTo() {
        double bearing = sphericalPoint9.rhumbBearingTo(sphericalPoint10);
        assertEquals(decimalFormatter.format(bearing), "116.7");
    }

    @Test
    public void test_rhumbDestinationPoint() {
        SphericalPoint rhumbDestinationPoint = sphericalPoint9.rhumbDestinationPoint(40300, 116.7, GeodesyUtil.getRadiusOfWorld());
        String lat = decimalFormatter2.format(rhumbDestinationPoint.getLatitude().getDegrees());
        assertEquals(lat, "50.9642");
        String lon = decimalFormatter2.format(rhumbDestinationPoint.getLongitude().getDegrees());
        assertEquals(lon, "1.853");
    }

    @Test
    public void test_rhumbMidpointTo() {
        SphericalPoint rhumbMidpoint = sphericalPoint9.rhumbMidpointTo(sphericalPoint10);
        String lat = decimalFormatter2.format(rhumbMidpoint.getLatitude().getDegrees());
        assertEquals(lat, "51.0455");
        String lon = decimalFormatter2.format(rhumbMidpoint.getLongitude().getDegrees());
        assertEquals(lon, "1.5957");
    }

    @Test
    public void test_areaOf() {
        List<SphericalPoint> polygon = new ArrayList<>();
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(1.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(1.0)));
        double area = SphericalPoint.areaOf(polygon, GeodesyUtil.getRadiusOfWorld());
        assertEquals(decimalFormatter2.format(area), "6182469722.7308");
    }

    @Test
    public void test_crossingParallels() {
        Map<String, Double> parallels = SphericalPoint.crossingParallels(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)),
                new SphericalPoint(new Latitude(60.0), new Longitude(30.0)), 30);
        String lon1 = decimalFormatter2.format(parallels.get("lon1"));
        // TODO check the method
        assertEquals(lon1, "9.5941");
        String lon2 = decimalFormatter2.format(parallels.get("lon2"));
        // TODO check the method
        assertEquals(lon2, "170.4059");
        Map<String, Double> parallels2 = SphericalPoint.crossingParallels(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)),
                new SphericalPoint(new Latitude(10.0), new Longitude(60.0)), 60);
        assertEquals(null, parallels2);
        Map<String, Double> parallels3 = SphericalPoint.crossingParallels(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)),
                new SphericalPoint(new Latitude(0.0), new Longitude(0.0)), 60);
        assertEquals(null, parallels2);
    }
}
