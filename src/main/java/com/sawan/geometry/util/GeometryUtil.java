/**
 * @author Sawan Meshram
 */
package com.sawan.geometry.util;

import java.util.Collection;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.sawan.geometry.BoundingBox;

public class GeometryUtil {
	
	private static WKTReader wktReader = new WKTReader();
	private static GeometryFactory defaultGometryFactory = new GeometryFactory();

	private static GeometryFactory geomFactory = null;

	public static GeometryFactory getGeometryFactory() {
		return getGeometryFactory(4326); //new GeometryFactory(new PrecisionModel(), 4326);
	}
	
	public static GeometryFactory getGeometryFactory(int SRID) {
		if(geomFactory == null || geomFactory.getSRID() != SRID) 
			geomFactory = new GeometryFactory(new PrecisionModel(), SRID);
		return geomFactory;
	}
	
	/**
	 * This method is used to return the geometry object by reading string WKT.
	 * @param wkt
	 * @return
	 */
	public static Geometry toGeometry(String WKT){
	    if(WKT == null || WKT.isEmpty()) return null;
	    
	    Geometry geometry = null;
		try {
			geometry = wktReader.read(WKT);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return geometry;
	}
	
	public static Envelope getEnvelope(String WKT){
		return getEnvelope(toGeometry(WKT)); //.getEnvelopeInternal();
	}
	
	public static Envelope getEnvelope(Geometry geom){
		return geom.getEnvelopeInternal();
	}
	
	
	/**
	 * This method is used to return BoundingBox object of input WKT.
	 * @param WKT
	 * @return
	 */
	public static BoundingBox getBoundingBox(String WKT){
		return getBoundingBox(toGeometry(WKT));
	}
	
	/**
	 * This method is used to return BoundingBox object of input Geometry.
	 * @param geom
	 * @return
	 */
	public static BoundingBox getBoundingBox(Geometry geom){
		Envelope en = getEnvelope(geom);
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}

	public static boolean intersect(Envelope e1, Envelope e2) {
		if(e1.intersects(e2))return true;
		return false;
	}
	/**
	 * This function is used to create and build {@code Point} geometry using {@code Coordinate}.
	 * @param coordinate is object of {@code Coordinate}
	 * @return {@code Point} geometry
	 * @since 23 July 2022
	 */
	public static Geometry buildGeometryPoint(Coordinate coordinate) {
		return defaultGometryFactory.createPoint(coordinate);
	}

	/**
	 * This function is used to create and build {@code Point} geometry using {@code Latitude} & {@code Longitude}.
	 * @param lat is {@code Latitude}
	 * @param lon is {@code Longitude}
	 * @return {@code Point} geometry
	 * @since 23 July 2022
	 */
	public static Geometry buildGeometryPoint(String lat, String lon) {
		return buildGeometryPoint(buildGeometryCoordinate(lat, lon)); //new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat)));
	}
	
	public static Coordinate buildGeometryCoordinate(String lat, String lon) {
		return new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat));
	}

	public static Geometry buildGeometryLineString(Collection<Coordinate> coordinates) {
		return defaultGometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
	}
	
	public static GeometryCollection buildGeometryCollection(Collection<Geometry> collection) {
		return defaultGometryFactory.createGeometryCollection(collection.toArray(new Geometry[collection.size()]));
	}
	
}
