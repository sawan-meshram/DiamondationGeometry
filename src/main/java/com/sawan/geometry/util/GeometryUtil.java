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

	/**
	 * This function is used to {@code GeometryFactory} with <i>default</i> {@code SRID} is <b>4326</b>.
	 * @return a {@code GeometryFactory} object.
	 */
	public static GeometryFactory getGeometryFactory() {
		return getGeometryFactory(4326); //new GeometryFactory(new PrecisionModel(), 4326);
	}
	
	/**
	 * This function is used to {@code GeometryFactory} with defined {@code SRID}.
	 * @param SRID is CRS <i>(Coordinate Reference System)</i> projection.
	 * @return a {@code GeometryFactory} object.
	 */
	public static GeometryFactory getGeometryFactory(int SRID) {
		if(geomFactory == null || geomFactory.getSRID() != SRID) 
			geomFactory = new GeometryFactory(new PrecisionModel(), SRID);
		return geomFactory;
	}
	
	/**
	 * This method is used to convert WKT <i>(Well-Known Text)</i> to {@code Geometry}.
	 * @param WKT is input WKT <i>(Well-Known Text)</i> of {@code Geometry}.
	 * @return a {@code Geometry} object.
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
	
	/**
	 * This method is used to convert {@code Envelope} to {@code Geometry}.
	 * @param envelope is input {@code envelope}.
	 * @return a {@code Geometry} object.
	 */
	public static Geometry toGeometry(Envelope envelope) {
		return defaultGometryFactory.toGeometry(envelope);
	}
	
	/**
	 * This method is used to get {@code Envelope} of a input WKT <i>(Well-Known Text)</i>.
	 * @param WKT is input WKT <i>(Well-Known Text)</i> of {@code Geometry}.
	 * @return a {@code Envelope} object.
	 */
	public static Envelope getEnvelope(String WKT){
		return getEnvelope(toGeometry(WKT)); //.getEnvelopeInternal();
	}
	
	/**
	 * This method is used to get {@code Envelope} of a input {@code Geometry}.
	 * @param geom is input {@code Geometry}.
	 * @return a {@code Envelope} object.
	 */
	public static Envelope getEnvelope(Geometry geom){
		return geom.getEnvelopeInternal();
	}
	
	/**
	 * This method is used to get {@code BoundingBox} object of input WKT <i>(Well-Known Text)</i>.
	 * @param WKT is input WKT <i>(Well-Known Text)</i> of {@code Geometry}.
	 * @return a {@code BoundingBox} object.
	 */
	public static BoundingBox getBoundingBox(String WKT){
		return getBoundingBox(toGeometry(WKT));
	}
	
	/**
	 * This method is used to get {@code BoundingBox} object of input {@code Geometry}.
	 * @param geom is input {@code Geometry}.
	 * @return a {@code BoundingBox} object.
	 */
	public static BoundingBox getBoundingBox(Geometry geom){
		Envelope en = getEnvelope(geom);
		return new BoundingBox(en.getMinX(), en.getMinY(), en.getMaxX(), en.getMaxY());
	}

	/**
	 * This method is used to check if first {@code Envelope} is intersect to second {@code Envelope}.
	 * @param e1 is first {@code Envelope}.
	 * @param e2 is second {@code Envelope}.
	 * @return {@code True} if intersect. Otherwise, {@code False}.
	 */
	public static boolean intersect(Envelope e1, Envelope e2) {
		if(e1.intersects(e2))return true;
		return false;
	}
	
	/**
	 * This function is used to create and build {@code Point} geometry using {@code Coordinate}.
	 * @param coordinate is object of {@code Coordinate}
	 * @return a {@code Point} object.
	 */
	public static Geometry buildGeometryPoint(Coordinate coordinate) {
		return defaultGometryFactory.createPoint(coordinate);
	}

	/**
	 * This function is used to create and build {@code Point} geometry using {@code Latitude} & {@code Longitude}.
	 * @param lat is {@code Latitude}
	 * @param lon is {@code Longitude}
	 * @return a {@code Point} object.
	 */
	public static Geometry buildGeometryPoint(String lat, String lon) {
		return buildGeometryPoint(buildGeometryCoordinate(lat, lon)); //new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat)));
	}
	
	/**
	 * This function is used to create and build {@code Coordinate} geometry using {@code Latitude} & {@code Longitude}.
	 * @param lat is {@code Latitude}
	 * @param lon is {@code Longitude}
	 * @return a {@code Coordinate} object.
	 */
	public static Coordinate buildGeometryCoordinate(String lat, String lon) {
		return new Coordinate(Double.parseDouble(lon), Double.parseDouble(lat));
	}

	/**
	 * This function is used to create and build {@code LineString} geometry using {@code Collection} of {@code Coordinate}.
	 * @param coordinates is a {@code Collection} of {@code Coordinate}.
	 * @return a {@code LineString} object.
	 */
	public static Geometry buildGeometryLineString(Collection<Coordinate> coordinates) {
		return defaultGometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
	}
	
	/**
	 * This function is used to create and build {@code GeometryCollection} geometry using {@code Collection} of {@code Geometry}.
	 * @param collection is a {@code Collection} of {@code Geometry}.
	 * @return a {@code GeometryCollection} object.
	 */
	public static GeometryCollection buildGeometryCollection(Collection<Geometry> collection) {
		return defaultGometryFactory.createGeometryCollection(collection.toArray(new Geometry[collection.size()]));
	}
	
}
