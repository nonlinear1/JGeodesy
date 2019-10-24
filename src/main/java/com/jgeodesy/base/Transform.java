package com.jgeodesy.base;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by omeruluoglu on 23.10.2019.
 */
public class Transform {

    private static Map<String, Transform> transforms = new HashMap<>();

    static {
        transforms.put("BD72", new Transform("BD72", 106.868628, -52.297783, 103.723893, -0.33657, -0.456955, -1.84218, 1.2727));
        transforms.put("Bessel1841", new Transform("Bessel1841", -582.0, -105.0, -414.0, -1.04, -0.35, 3.08, -8.3));
        transforms.put("Clarke1866", new Transform("Clarke1866", 8, -160, -176, 0.0, 0.0, 0.0, 0.0));
        transforms.put("DHDN", new Transform("DHDN", -591.28, -81.35, -396.39, 1.477, -0.0736, -1.458, -9.82));
        transforms.put("ED50", new Transform("ED50", 89.5, 93.8, 123.1, 0.0, 0.0, 0.156, -1.2));
        transforms.put("ETRS89", new Transform("ETRS89", 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        transforms.put("Irl1975", new Transform("Irl1975", -482.530, 130.596, -564.557, 1.042, 0.214, 0.631, -8.150));
        transforms.put("Krassowsky1940", new Transform("Krassowsky1940", -24.0, 123.0, 94.0, -0.02, 0.26, 0.13, -2.423));
        transforms.put("MGI", new Transform("MGI",-577.326,-90.129,-463.920,5.137,1.474,5.297,-2.423));
        transforms.put("NAD27", new Transform("NAD27",8,-160,-176,0.0,0.0,0.0,0.0));
        transforms.put("NAD83", new Transform("NAD83",1.004,-1.910,-0.515,0.0267,0.00034,0.011,-0.00150));
        transforms.put("NTF", new Transform("NTF",-168,-60,320,0.0,0.0,0.0,0.0));
        transforms.put("OSGB36", new Transform("OSGB36",-446.448,125.157,-542.060,-0.1502,-0.2470,-0.8421,20.4894));
        transforms.put("Potsdam", new Transform("Potsdam",-582.0,-105.0,-414.0,1.04,0.35,-3.08,-8.3));
        transforms.put("TokyoJapan", new Transform("TokyoJapan",148.0, -507.0, -685.0, 0.0, 0.0, 0.0, 0.0));
        transforms.put("WGS72", new Transform("WGS72", -4.5, 0.554, -0.22, 0.0, 0.0, 0.0, 0.0));
        transforms.put("WGS84", new Transform("WGS84"));
    }

    /**
     * Unique transform name
     */
    private String name;
    /**
     * X translation, (meter)
     */
    private Double tx;
    /**
     * Y translation, (meter)
     */
    private Double ty;
    /**
     * Z translation, (meter)
     */
    private Double tz;
    /**
     * X rotation, (radian)
     */
    private Double rx;
    /**
     * Y rotation, (radian)
     */
    private Double ry;
    /**
     * Z rotation, (radian)
     */
    private Double rz;
    /**
     * Scale parts-per notation (ppm)
     */
    private Double s;
    /**
     * Scale +1
     */
    private Double s1;
    /**
     * X rotation, (degree arc seconds)
     */
    private Double sx;
    /**
     * Y rotation, (degree arc seconds)
     */
    private Double sy;
    /**
     * Z rotation, (degree arc seconds)
     */
    private Double sz;

    /**
     * Helmert transformation
     * @param name unique name
     */
    public Transform(String name) {
        this.name = name;
        this.tx = 0.0;
        this.ty = 0.0;
        this.tz = 0.0;
        this.rx = 0.0;
        this.ry = 0.0;
        this.rz = 0.0;
        this.s = 0.0;
        this.s1 = 1.0;
        this.sx = 0.0;
        this.sy = 0.0;
        this.sz = 0.0;
    }

    /**
     * Helmert transformation
     *
     * @param name unique name
     * @param tx   X translation in meter
     * @param ty   Y translation in meter
     * @param tz   Z translation in meter
     * @param sx   X rotation in arc seconds
     * @param sy   Y rotation in arc seconds
     * @param sz   Z rotation in arc seconds
     * @param s    Scale ppm in double
     */
    public Transform(String name, double tx, double ty, double tz, double sx, double sy, double sz, double s) {
        this.name = name;
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.s = s;
        this.rx = Math.toRadians(sx / 3600.0);
        this.ry = Math.toRadians(sy / 3600.0);
        this.rz = Math.toRadians(sz / 3600.0);
        this.s1 = s * 1.e-6 + 1;
    }

    public static Map<String, Transform> getTransforms() {
        return transforms;
    }

    /**
     * Applies Helmert transform to the point using transform parameters
     * @param vector3D The vector to apply the transformation to
     * @param transform Transform to apply to the point.
     * @return Transformed vector point
     */
    public static Vector3D applyTransform(Vector3D vector3D, final Transform transform) {
        // Vector Point
        double x1 = vector3D.getX();
        double y1 = vector3D.getY();
        double z1 = vector3D.getZ();

        // Apply transform
        double x2 = transform.getTx() + x1 * transform.getS1() - y1 * transform.getRz() + z1 * transform.getRy();
        double y2 = transform.getTy() + x1 * transform.getRz() + y1 * transform.getS1() - z1 * transform.getRx();
        double z2 = transform.getTz() - x1 * transform.getRy() + y1 * transform.getRx() + z1 * transform.getS1();

        return new Vector3D(x2, y2, z2);
    }

    /**
     * Returns the inverse of this transform
     * @return Inverted transform
     */
    public Transform inverse() {
        return new Transform(this.name + "-Inverse", -this.tx, -this.ty, -this.tz, -this.sx, -this.sy, -this.sz, -this.s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTx() {
        return tx;
    }

    public void setTx(Double tx) {
        this.tx = tx;
    }

    public Double getTy() {
        return ty;
    }

    public void setTy(Double ty) {
        this.ty = ty;
    }

    public Double getTz() {
        return tz;
    }

    public void setTz(Double tz) {
        this.tz = tz;
    }

    public Double getRx() {
        return rx;
    }

    public void setRx(Double rx) {
        this.rx = rx;
    }

    public Double getRy() {
        return ry;
    }

    public void setRy(Double ry) {
        this.ry = ry;
    }

    public Double getRz() {
        return rz;
    }

    public void setRz(Double rz) {
        this.rz = rz;
    }

    public Double getS() {
        return s;
    }

    public void setS(Double s) {
        this.s = s;
    }

    public Double getS1() {
        return s1;
    }

    public void setS1(Double s1) {
        this.s1 = s1;
    }

    public Double getSx() {
        return sx;
    }

    public void setSx(Double sx) {
        this.sx = sx;
    }

    public Double getSy() {
        return sy;
    }

    public void setSy(Double sy) {
        this.sy = sy;
    }

    public Double getSz() {
        return sz;
    }

    public void setSz(Double sz) {
        this.sz = sz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Transform transform = (Transform) o;

        return new EqualsBuilder()
                .append(name, transform.name)
                .append(tx, transform.tx)
                .append(ty, transform.ty)
                .append(tz, transform.tz)
                .append(rx, transform.rx)
                .append(ry, transform.ry)
                .append(rz, transform.rz)
                .append(s, transform.s)
                .append(s1, transform.s1)
                .append(sx, transform.sx)
                .append(sy, transform.sy)
                .append(sz, transform.sz)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(tx)
                .append(ty)
                .append(tz)
                .append(rx)
                .append(ry)
                .append(rz)
                .append(s)
                .append(s1)
                .append(sx)
                .append(sy)
                .append(sz)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("tx", tx)
                .append("ty", ty)
                .append("tz", tz)
                .append("rx", rx)
                .append("ry", ry)
                .append("rz", rz)
                .append("s", s)
                .append("s1", s1)
                .append("sx", sx)
                .append("sy", sy)
                .append("sz", sz)
                .toString();
    }
}
