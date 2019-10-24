package com.jgeodesy.shape;

import com.jgeodesy.base.Transform;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omeruluoglu on 23.10.2019.
 */
public class Datum {

    private static Map<String, Datum> datums = new HashMap<>();

    static {
        datums.put("BD72", new Datum(Ellipsoid.INTL1924, Transform.getTransforms().get("BD72")));
        datums.put("DHDN", new Datum(Ellipsoid.BESSEL1841, Transform.getTransforms().get("DHDN")));
        datums.put("ED50", new Datum(Ellipsoid.INTL1924, Transform.getTransforms().get("ED50")));
        datums.put("ETRS89", new Datum(Ellipsoid.GRS80, Transform.getTransforms().get("WGS84")));
        datums.put("GDA2020", new Datum(Ellipsoid.GRS80, Transform.getTransforms().get("WGS84")));
        datums.put("GRS80", new Datum(Ellipsoid.GRS80, Transform.getTransforms().get("WGS84")));
        datums.put("Irl1975", new Datum(Ellipsoid.AIRY_MODIFIED, Transform.getTransforms().get("Irl1975")));
        datums.put("Krassowsky1940", new Datum(Ellipsoid.KRASSOVSKI1940, Transform.getTransforms().get("Krassowsky1940")));
        datums.put("MGI", new Datum(Ellipsoid.BESSEL1841, Transform.getTransforms().get("MGI")));
        datums.put("NAD27", new Datum(Ellipsoid.CLARKE1866, Transform.getTransforms().get("NAD27")));
        datums.put("NAD83", new Datum(Ellipsoid.GRS80, Transform.getTransforms().get("NAD83")));
        datums.put("NTF", new Datum(Ellipsoid.CLARKE1880IGN, Transform.getTransforms().get("NTF")));
        datums.put("OSGB36", new Datum(Ellipsoid.AIRY1830, Transform.getTransforms().get("OSGB36")));
        datums.put("Potsdam", new Datum(Ellipsoid.BESSEL1841, Transform.getTransforms().get("Bessel1841")));
        datums.put("Sphere", new Datum(Ellipsoid.SPHERE, Transform.getTransforms().get("WGS84")));
        datums.put("TokyoJapan", new Datum(Ellipsoid.BESSEL1841, Transform.getTransforms().get("TokyoJapan")));
        datums.put("WGS72", new Datum(Ellipsoid.WGS72, Transform.getTransforms().get("WGS72")));
        datums.put("WGS84", new Datum(Ellipsoid.WGS84, Transform.getTransforms().get("WGS84")));
    }

    /**
     * Associated ellipsoid
     */
    private Ellipsoid ellipsoid;

    /**
     * Associated Helmert transform parameters
     */
    private Transform transform;

    /**
     * @param ellipsoid ellipsoid
     * @param transform transform
     */
    public Datum(Ellipsoid ellipsoid, Transform transform) {
        this.ellipsoid = ellipsoid;
        this.transform = transform;
    }

    /**
     * Check datum value
     *
     * @param name datum name
     * @return Datum
     */
    public static Datum getDatum(String name) {
        return datums.get(name);
    }

    public static Map<String, Datum> getDatums() {
        return datums;
    }

    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    public void setEllipsoid(Ellipsoid ellipsoid) {
        this.ellipsoid = ellipsoid;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }
}
