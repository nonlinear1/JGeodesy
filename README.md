# JGeodesy

Java library for Geodesic and trigonometric calculations

## Getting Started

This project is inspired by and based on [Erkir](https://github.com/vahancho/erkir) and [Geodesy functions](https://github.com/chrisveness/geodesy) and the owner of the project is [Chris Veness](https://github.com/chrisveness).

Library Contents;
- Simpler **trig**-based functions (distance, bearing, etc) based on a **spherical earth** model.
- More sophisticated **trig**-based functions (distance, bearing, etc) based on a more accurate **ellipsoidal earth** model.
- **Vector**-based functions mostly based on a **spherical** earth model, with some **ellipsoidal**
  functions.
- Functions for historical datum conversions (such as between NAD83, OSGB36, Irl1975, etc) and modern reference frame conversions (such as ITRF2014, ETRF2000, GDA94, etc), 
  and conversions between geodetic (latitude/longitude) coordinates and geocentric cartesian (x/y/z) 
  coordinates.
- 3d Vector manipulation functions (supporting cartesian (x/y/z) coordinates and n-vector geodesy).
- Functions for conversion between decimal degrees and (sexagesimal) degrees/minutes/seconds.

### Prerequisites

There are no special requirements.

### Installing

1\. Clone this repository

```bash
git clone https://github.com/omeruluoglu/JGeodesy.git
```

2\. Build and install the archetype

```bash
maven clean install
```

3\. Get the .jar file from `~/JGeodesy/target/JGeodesy.jar`

4\. Import JGeodesy jar library to project.

## Running the tests

There are unit tests. You can find them in the `src/test/` directory.
```bash
maven clean install
```

## Deployment

Import JGeodesy jar library to project.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [JUnit](http://junit.org/junit4/) - Unit Tests

## Usage Examples:

Find the distance from this point to destination point
```java
public class JGeodesyApplication {

    public static void main(String[] args) {
        SphericalPoint sphericalPoint = new SphericalPoint(new Latitude(52.205), new Longitude(0.119));
        SphericalPoint sphericalPoint2 = new SphericalPoint(new Latitude(48.857), new Longitude(2.351));
        double distance = sphericalPoint.distanceTo(sphericalPoint2, 6371e3);
    }
}
```
Find the distance travelling from this point to destination point along a rhumb line
```java
public class JGeodesyApplication {

    public static void main(String[] args) {
        SphericalPoint sphericalPoint = new SphericalPoint(new Latitude(51.127),new Longitude(1.338));
        SphericalPoint sphericalPoint2 = new SphericalPoint(new Latitude(50.964),new Longitude(1.853));
        double rhumbDistance = sphericalPoint.rhumbDistanceTo(sphericalPoint2, 6371e3);
        double kiloMeterRhumbDistance = GeodesyUtil.convertMeterToKiloMeter(rhumbDistance);
    }
}
```
Calculates the area of a spherical polygon
```java
public class JGeodesyApplication {

    public static void main(String[] args) {
        List<SphericalPoint> polygon = new ArrayList<>();
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(1.0), new Longitude(0.0)));
        polygon.add(new SphericalPoint(new Latitude(0.0), new Longitude(1.0)));
        double area = SphericalPoint.areaOf(polygon, GeodesyUtil.getRadiusOfWorld());
    }
}
```

## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire and create. Any contributions you make are greatly appreciated.
1. Fork the project
2. Create your Feature Branch (`git checkout -b feature/<FeatureName>`)
3. Commit your Changes (`git commit -m "Your commit message"`)
4. Push to the Branch (`git push origin feature/<FeatureName>`)
5. Open a Pull Request

## Authors

* [Omer Uluoglu](https://github.com/omeruluoglu)

See also the list of [contributors](https://github.com/omeruluoglu/JGeodesy/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Movable Type Scripts Latitude/Longitude Calculations Reference](http://www.movable-type.co.uk/scripts/latlong.html)
* [Erkir (armenian: Երկիր, means Earth)](https://github.com/vahancho/erkir)
* [Great Circles - Rhumbline](https://kavas.com/blog/great-circle-and-rhumbline)