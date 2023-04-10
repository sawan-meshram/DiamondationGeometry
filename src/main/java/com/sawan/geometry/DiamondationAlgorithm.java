package com.sawan.geometry;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.TopologyException;

public class DiamondationAlgorithm {

	private static GeometryFactory geomFactory = new GeometryFactory();

	public enum GeometryType{
		ALL,
		MultiPolygon
	}
	
	/**
	 * This function is used to split a given {@code Geometry} such as {@code MultiPolygon} or {@code Polygon}. It is a grid based Splitted Geometry Algorithm.
	 * @param geom is input {@code Geometry}. It can be {@code Polygon} or {@code MultiPolygon}.
	 * @param grid is number that to be split on x-axis & y-axis.
	 * @param type is a {@code GeometryType}. User can asked to give a specified {@code GeometryType}.
	 * @return a {@code List} collection that contains splitted geometry of a given {@code Geometry}.<br>
	 * <p>If {@code List} size is <strong>'0'</strong>, then algorithm is not able to split input {@code Geometry}.
	 */
	public static List<Geometry> splitOriginalGeometry(Geometry geom, int grid, GeometryType type){
		return splitOriginalGeometry(geom, grid, grid, type);
	}
	/**
	 * This function is used to split a given {@code Geometry} such as {@code MultiPolygon} or {@code Polygon}. It is a grid based Splitted Geometry Algorithm.
	 * @param geom is input {@code Geometry}. It can be {@code Polygon} or {@code MultiPolygon}.
	 * @param gridX is number that to be split on x-axis.
	 * @param gridY is number that to be split on y-axis.
	 * @param type is a {@code GeometryType}. User can asked to give a specified {@code GeometryType}.
	 * @return a {@code List} collection that contains splitted geometry of a given {@code Geometry}.<br>
	 * <p>If {@code List} size is <strong>'0'</strong>, then algorithm is not able to split input {@code Geometry}.
	 */
	public static List<Geometry> splitOriginalGeometry(Geometry geom, int gridX, int gridY, GeometryType type){
		List<Geometry> list = new ArrayList<>();
		
		boolean otherGeomFlag = false;
		
		final Envelope envelope = geom.getEnvelopeInternal();
		
		//Algorithm
		double minX = envelope.getMinX();
		double maxX = envelope.getMaxX();
		
		double minY = envelope.getMinY();
		double maxY = envelope.getMaxY();
		
		double grid_x = ((maxX - minX) / gridX);
		double grid_y = ((maxY - minY) / gridY);
		
		
		for(int i = 0; i < gridX; i++){
			for(int j = 0; j < gridY; j++){
				
				double _xmin = minX + (i * grid_x);
				double _ymin = minY + (j * grid_y);
				
				double _xmax = _xmin + grid_x;
				double _ymax = _ymin + grid_y;
				
				Envelope env = new Envelope(_xmin, _xmax, _ymin, _ymax);
				
				Geometry splitGeom = null;
				try{
					splitGeom = geomFactory.toGeometry(env).intersection(geom);
				}catch(TopologyException ex){
					System.err.println(ex.getMessage());
					//if any splitted geometry is not able to get created, then we will not return the result.
					otherGeomFlag = true;
					break;
				}
				
				if(splitGeom.isEmpty())continue;
				
				/*
				 * Checking user preferred GeometryTypes
				 */
				if(type == GeometryType.ALL){
					list.add(splitGeom);
				}
				else if(type == GeometryType.MultiPolygon) {
					
					if(splitGeom instanceof Polygon)
						splitGeom = convertPolygonToMultiPolygon(splitGeom);
					
					if(splitGeom instanceof MultiPolygon) 
						list.add(splitGeom);
					
					else if(splitGeom instanceof GeometryCollection){
						Geometry collectionGeom = convertGeometryCollectionToMultiPolygon(splitGeom);
						if(collectionGeom != null)
							list.add(collectionGeom);
						else{
							try {
								throw new TopologyException("If there are more than one Point or LineString geometry, then we won't able to create MultiPolygon. Since, we are losing more geometry data.");
							}catch(TopologyException ex) {
								System.err.println(ex.getMessage());
							}
							/*
							 * if any splitted geometry is not able to get created, then we will not return the result.
							 */
							otherGeomFlag = true;
							break;

						}
					}
					/*
					 * If there is a geometry that it is not Polygon or MultiPolygon, or GeometryCollection, etc.
					 */
					else {
						try {
							throw new TopologyException("Found new shape, i.e. '"+splitGeom.getGeometryType()+"' and required 'MultiPolyon', therefore not processed.");
						}catch(TopologyException ex) {
							System.err.println(ex.getMessage());
						}
						otherGeomFlag = true;
						break;
					}
				}//EOF ELSE IF
				
			}//EOF for inner
			
			if(otherGeomFlag) break;
		}//EOF for outer
		
		//If it is True, then we are consider that the algorithm is not able to create a splitted geometries for given grid-x and grid-y.
		if(otherGeomFlag) list.clear();
		
		return list;
	}
	
	/**
	 * This function is used to make {@code GeometryCollection} to {@code MultiPolygon}.
	 * @param input is {@code GeometryCollection}.
	 * @return a new {@code MultiPolygon}.
	 */
	private static MultiPolygon convertGeometryCollectionToMultiPolygon(Geometry input){
		List<Geometry> list = new ArrayList<>();
		
		int countLine = 0;
		int countPoint = 0;
		for(int i = 0; i< input.getNumGeometries(); i++){
			//Only add Polygon or MultiPolygon to List
			if(input.getGeometryN(i) instanceof Polygon || input.getGeometryN(i) instanceof MultiPolygon)
				list.add(input.getGeometryN(i));
			
			else if(input.getGeometryN(i) instanceof LineString || input.getGeometryN(i) instanceof MultiLineString){
				countLine++;
			}
			else if(input.getGeometryN(i) instanceof Point || input.getGeometryN(i) instanceof MultiPoint){
				countPoint++;
			}
		}
		/*
		 * 
		 * If there are more than one Point or LineString then we don't consider this value MultiPolygon. 
		 * Since, we are losing more geometry data. 
		 */
		if(countLine > 1 || countPoint > 1){
			return null;
		}
		/*
		 * If there is more than one Geometry
		 */
		if(list.size() > 1){
			MultiPolygon result = (MultiPolygon) geomFactory.buildGeometry(list);
		    result = (MultiPolygon) result.union();
		    return result;
		}
		else if(list.size() == 1){
			return list.get(0) instanceof Polygon ? convertPolygonToMultiPolygon(list.get(0)) : (MultiPolygon) list.get(0);
		}
		return null;
	}
	
	/**
	 * This function is used to convert {@code Polygon} to {@code MultiPolygon}.
	 * @param input is {@code Polygon}.
	 * @return a new {@code MultiPolygon}.
	 */
	private static MultiPolygon convertPolygonToMultiPolygon(Geometry input){
		Polygon[] polys = new Polygon[1];
		polys[0] = (Polygon) input;	
		return geomFactory.createMultiPolygon(polys);
	}
}
