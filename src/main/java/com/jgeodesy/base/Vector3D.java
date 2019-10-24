package com.jgeodesy.base;

import com.jgeodesy.coordinate.Coordinate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by omeruluoglu on 21.10.2019.
 */
public class Vector3D {

    private double x;
    private double y;
    private double z;

    /**
     * Creates invalid 3D-vector
     */
    public Vector3D() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    /**
     * Creates a 3D-Vector
     * @param x the component of vector
     * @param y the component of vector
     * @param z the component of vector
     */
    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Multiplies this vector with given vector to dot vector
     * @param vector3D Vector to be dotted with this vector.
     * @return Dot product
     */
    public final Double dot(final Vector3D vector3D) {
        return this.x * vector3D.x + this.y * vector3D.y + this.z * vector3D.z;
    }

    /**
     * Multiplies this vector with given vector to cross vector
     * @param vector3D Vector to be crossed with this vector.
     * @return Cross product
     */
    public final Vector3D cross(final Vector3D vector3D) {
        double crossX = this.y * vector3D.z - this.z * vector3D.y;
        double crossY = this.z * vector3D.x - this.x * vector3D.z;
        double crossZ = this.x * vector3D.y - this.y * vector3D.x;
        return new Vector3D(crossX, crossY, crossZ);
    }

    /**
     * Length (magnitude or norm) of vector.
     * @return Megnitude of vector
     */
    public final Double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Normalizes a vector
     * @return Normalised version of vector
     */
    public final Vector3D unit() {
        Double norm = this.length();
        if (norm.equals(1.0) || norm.equals(0.0))
            return this;
        return new Vector3D(this.x / norm, this.y / norm, this.z / norm);
    }

    /**
     * Calculates the angle between ‘this’ vector and supplied vector
     * @param vector Vector whose angle is to be determined from ‘this’ vector.
     * @param normal Plane normal: if supplied, angle is signed +ve if this->v is clockwise looking along n, -ve in opposite direction.
     * @return number Angle (in radians) between this vector and supplied vector (in range 0..π if n not supplied, range -π..+π if n supplied).
     */
    public final Double angleTo(final Vector3D vector, final Vector3D normal) {
        double sign = this.cross(vector).dot(normal) >= 0 ? 1 : -1;
        double sinTheta = this.cross(vector).length() * sign;
        double cosTheta = this.dot(vector);
        return Math.atan2(sinTheta, cosTheta);
    }

    /**
     * Rotates ‘this’ point around an axis by a specified angle.
     * @param axis The axis being rotated around.
     * @param angle The angle of rotation (in degrees).
     * @return The rotated point vector
     */
    public final Vector3D rotateAround(final Vector3D axis, final double angle) {
        // Convert degree to radian
        double theta = Coordinate.toRadians(angle);

        // en.wikipedia.org/wiki/Rotation_matrix#Rotation_matrix_from_axis_and_angle
        // en.wikipedia.org/wiki/Quaternions_and_spatial_rotation#Quaternion-derived_rotation_matrix
        Vector3D vector1 = this.unit();
        // x, y, z
        double[] polarMatrix = {vector1.getX(), vector1.getY(), vector1.getZ()};

        Vector3D axisVector = axis.unit();
        double axisVectorX = axisVector.getX();
        double axisVectorY = axisVector.getY();
        double axisVectorZ = axisVector.getZ();

        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double t = 1 - cosTheta;

        // Quaternion-derived rotation matrix
        double[][] rotationMatrix = {
                {
                    axisVectorX * axisVectorX * t + cosTheta,
                    axisVectorX * axisVectorY * t - axisVectorZ * sinTheta,
                    axisVectorX * axisVectorZ * t + axisVectorY * sinTheta
                },
                {
                    axisVectorY * axisVectorX * t + axisVectorZ * sinTheta,
                    axisVectorY * axisVectorY * t + cosTheta,
                    axisVectorY * axisVectorZ * t - axisVectorX * sinTheta
                },
                {
                    axisVectorZ * axisVectorX * t - axisVectorY * sinTheta,
                    axisVectorZ * axisVectorY * t + axisVectorX * sinTheta,
                    axisVectorZ * axisVectorZ * t + cosTheta
                }
        };

        // Multiple p * r matrix
        double[] polarRotationMatrix = {
                rotationMatrix[0][0] * polarMatrix[0] + rotationMatrix[0][1] * polarMatrix[1] + rotationMatrix[0][2] * polarMatrix[2],
                rotationMatrix[1][0] * polarMatrix[0] + rotationMatrix[1][1] * polarMatrix[1] + rotationMatrix[1][2] * polarMatrix[2],
                rotationMatrix[2][0] * polarMatrix[0] + rotationMatrix[2][1] * polarMatrix[1] + rotationMatrix[2][2] * polarMatrix[2]
        };
        // qv en.wikipedia.org/wiki/Rodrigues'_rotation_formula...
        return new Vector3D(polarRotationMatrix[0], polarRotationMatrix[1], polarRotationMatrix[2]);
    }

    /**
     * Vector substraction method
     * @param v1 first vector
     * @param v2 second vector
     * @return Result vector
     */
    public static Vector3D substraction(final Vector3D v1, final Vector3D v2) {
        return new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }

    /**
     * Vector addition method
     * @param v1 first vector
     * @param v2 second vector
     * @return Result vector
     */
    public static Vector3D addition(final Vector3D v1, final Vector3D v2) {
        return new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }

    /**
     * Multiplication of vector and scalar
     * @param v1 vector
     * @param number scalar number
     * @return Vector
     */
    public static Vector3D multiplicationVector(final Vector3D v1, double number) {
        return new Vector3D(v1.getX() * number, v1.getY() * number, v1.getZ() * number);
    }

    /**
     * Multiplication of scalar and vector
     * @param number scalar number
     * @param v1 vector
     * @return Result vector
     */
    public static Vector3D multiplicationScalar(final double number, final Vector3D v1) {
        return multiplicationVector(v1, number);
    }

    /**
     * Negates a vector to point in the opposite direction.
     * @param v1 vector
     * @return Result vector
     */
    public static Vector3D opposite(final Vector3D v1) {
        return new Vector3D(-v1.getX(), -v1.getY(), -v1.getZ());
    }

    /**
     * Division of vector and scalar
     * @param v1 vector
     * @param number scalar number
     * @return Result vector
     */
    public static Vector3D division(final Vector3D v1, final double number) {
        return new Vector3D(v1.getX() / number, v1.getY() / number, v1.getZ() / number);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        Vector3D vector3D = (Vector3D) o;

        return new EqualsBuilder()
                .append(x, vector3D.x)
                .append(y, vector3D.y)
                .append(z, vector3D.z)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(x)
                .append(y)
                .append(z)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("x", x)
                .append("y", y)
                .append("z", z)
                .toString();
    }
}
