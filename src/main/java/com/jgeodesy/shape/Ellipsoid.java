package com.jgeodesy.shape;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omeruluoglu on 22.10.2019.
 */
public enum Ellipsoid {

    AIRY1830("Airy1830", 6377563.396, 6356256.909, 1.0 / 299.3249646),
    AIRY_MODIFIED("AiryModified",6377340.189, 6356034.448, 1.0 / 299.3249646),
    AUSTRALIA1966("Australia1966",6378160.0,6356774.719,1.0 / 298.25),
    BESSEL1841("Bessel1841",6377397.155, 6356078.962818, 1.0 / 299.1528128),
    CLARKE1866("Clarke1866",6378206.4, 6356583.8, 1.0 / 294.978698214),
    CLARKE1880("Clarke1880",6378249.145, 6356514.86954978,  1.0 / 293.465),
    CLARKE1880IGN("Clarke1880IGN",6378249.2, 6356515.0, 1.0 / 293.466021294),
    CLARKE1880MOD("Clarke1880Mod",6378249.145, 6356514.96582849,  1.0 / 293.4663),
    CPM1799("CPM1799",6375738.7,6356671.92557493,1.0 / 334.39),
    DELAMBRE1810("Delambre1810",6376428.0,6355957.92616372,1.0 / 311.5),
    ENGELIS1985("Engelis1985",6378136.05,6356751.32272154,1.0 / 298.2566),
    EVEREST1969("Everest1969",6377295.664,6356094.667915,1.0 / 300.8017),
    FISHER1968("Fisher1968",6378150.0,6356768.33724438,1.0 / 298.3),
    GEM10C("GEM10C",6378137.0,6356752.31424783,1.0 / 298.2572236),
    GRS67("GRS67",6378160.0,6356774.516,1.0 / 298.247167427),
    GRS80("GRS80",6378137.0,6356752.314140,1.0 / 298.257222101),
    HELMERT1906("Helmert1906",6378200.0,6356818.16962789,298.3),
    IERS1989("IERS1989",6378136.0,6356751.302,298.257),
    IERS1992TOPEX("IERS1992TOPEX",6378136.3,6356751.61659215,298.257223563),
    IERS2003("IERS2003",6378136.6,6356751.85797165,298.25642),
    INTL1924("Intl1924",6378388.0,6356911.946,1.0 / 297.0),
    INTL1967("Intl1967",6378157.5,6356772.2,298.24961539),
    KRASSOVSKI1940("Krassovski1940",6378245.0,6356863.01877305,298.3),
    MAUPERTUIS1738("Maupertuis1738",6397300.0,6363806.28272251,191.0),
    MERCURY1960("Mercury1960",6378166.0,6356784.28360711,298.3),
    MERCURY1968MOD("Mercury1968Mod",6378150.0,6356768.33724438,298.3),
    NWL1965("NWL1965",6378145.0,6356759.76948868,298.25),
    OSU86F("OSU86F",6378136.2,6356751.51693008,298.2572236),
    OSU91A("OSU91A",6378136.3,6356751.6165948,298.2572236),
    PLESSIS1817("Plessis1817",6397523.0,6355863.0,153.56512242),
    SGS85("SGS85",6378136.0,6356751.30156878,298.257),
    SOAMERICAN1969("SoAmerican1969",6378160.0,6356774.71919531,298.25),
    STRUVE1860("Struve1860",6378298.3,6356657.14266956,294.73),
    WGS60("WGS60",6378165.0,6356783.28695944,298.3),
    WGS66("WGS66",6378145.0,6356759.76948868,298.25),
    WGS72("WGS72",6378135.0, 6356750.5, 1.0 / 298.26),
    WGS84("WGS84",6378137.0, 6356752.314245, 1.0 / 298.257223563),
    SPHERE("Sphere",6371008.771415,6371008.771415,0.0),
    SPHERE_AUTHALIC("SphereAuthalic",6371000.0,6371000.0,0.0),
    SPHERE_POPULAR("SpherePopular",6378137.0,6378137.0,0.0);

    private static Map<String, Ellipsoid> ellipsoids = new HashMap<>();

    static {
        for (Ellipsoid ellipsoid : values()) {
            ellipsoids.put(ellipsoid.name(), ellipsoid);
        }
    }

    /**
     * Unique name
     */
    private final String name;
    /**
     * Major axis (a)
     */
    private final double a;
    /**
     * Minor axis (b)
     */
    private final double b;
    /**
     * Flattening (f)
     */
    private final double f;

    Ellipsoid(String name, double a, double b, double f) {
        this.name = name;
        this.a = a;
        this.b = b;
        this.f = f;
    }

    /**
     *
     * @param name name
     * @return Ellipsoid
     */
    public static Ellipsoid getEllipsoid(String name) {
        return ellipsoids.get(name);
    }

    public static Map<String, Ellipsoid> getEllipsoids() {
        return ellipsoids;
    }

    public String getName() {
        return name;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getF() {
        return f;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("a", a)
                .append("b", b)
                .append("f", f)
                .toString();
    }
}
