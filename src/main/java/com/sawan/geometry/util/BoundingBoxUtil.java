/**
 * @author Sawan Meshram
 */
package com.sawan.geometry.util;

import java.util.ArrayList;
import java.util.List;

import com.sawan.geometry.BoundingBox;

public class BoundingBoxUtil {
	
	/**
	 * This method is used to find out the gridded bounding box of a given {@code BoundingBox}.
	 * @param b is a input {@code BoundingBox}.
	 * @param gridX is value of grid on x-axis.
	 * @param gridY is value of grid on y-axis.
	 * @return {@code List} collection of {@code BoundingBox}.
	 * 
	 */
	public static List<BoundingBox> griddedBoundingBox(BoundingBox b, int gridX, int gridY){
//		System.out.println("BoundingBox Grid for 'X' = '"+ gridX + "' & 'Y' = '"+gridY +"'");
		double grid_x = ((b.getXmax() - b.getXmin()) / gridX);
		double grid_y = ((b.getYmax() - b.getYmin()) / gridY);
		
		List<BoundingBox> list = new ArrayList<>();

		for(int i = 0; i < gridX; i++){
			for(int j = 0; j < gridY; j++){
				
				double _xmin = b.getXmin() + (i * grid_x);
				double _ymin = b.getYmin() + (j * grid_y);
				
				double _xmax = _xmin + grid_x;
				double _ymax = _ymin + grid_y;
				
				list.add(new BoundingBox(_xmin, _ymin, _xmax, _ymax));
			}
		}
//		System.out.println("Total grid form = "+list.size());
		return list;
	}
	
	/**
	 * This method is used to find out the gridded bounding box of a given {@code BoundingBox}.
	 * @param b is a input {@code BoundingBox}.
	 * @param grid is value of grid on x-axis & y-axis.
	 * @return {@code List} collection of {@code BoundingBox}.
	 */
	public static List<BoundingBox> griddedBoundingBox(BoundingBox b, int grid){
		return griddedBoundingBox(b, grid, grid);
	}

	/**
	 * This function is used to create a {@code Polygon} WKT for this {@code BoundingBox}.
	 * @param b is BoundingBox.
	 * @return Polygon in WKT <i>(Well-Known Text)</i> format. 
	 */
	public static String createPolygon(BoundingBox b){
		return new StringBuilder().append("POLYGON ((")
				.append(b.getXmin()).append(" ").append(b.getYmin()).append(", ")
				.append(b.getXmax()).append(" ").append(b.getYmin()).append(", ")
				.append(b.getXmax()).append(" ").append(b.getYmax()).append(", ")
				.append(b.getXmin()).append(" ").append(b.getYmax()).append("))").toString();
	}
	
	/**
	 * This function is used to create a {@code MultiPolygon} WKT for this {@code BoundingBox}.
	 * @param listBoundingBox is Collection of boundingBox.
	 * @return MultiPolygon in WKT <i>(Well-Known Text)</i> format. 
	 */
	public static String createMultiPolygon(List<BoundingBox> listBoundingBox){
		
		StringBuilder sb = new StringBuilder().append("MULTIPOLYGON (");
		
		for(BoundingBox b : listBoundingBox){
			sb.append("((")
			.append(b.getXmin()).append(" ").append(b.getYmin()).append(", ")
			.append(b.getXmax()).append(" ").append(b.getYmin()).append(", ")
			.append(b.getXmax()).append(" ").append(b.getYmax()).append(", ")
			.append(b.getXmin()).append(" ").append(b.getYmax())
			.append("))")
			.append(", ");
		}
		return sb.replace(sb.lastIndexOf(", "), sb.lastIndexOf(", ")+2, ")").toString();
	}
	
	/**
	 * This method is used to check, if lat-long is within bounding box.
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 * @param latitude
	 * @param longitude
	 * @return {@code True} if lat-long is within bounding box. Otherwise, {@code False}.
	 */
	public static boolean IsWithinBoundaryBox(double xmin, double ymin, double xmax, double ymax, double latitude, double longitude){
		
		if (longitude > xmin && longitude < xmax &&
				latitude > ymin && latitude < ymax)
			return true;
		
		return false;
	}
	
	/**
	 * This method is used to check if bounding box is intersected with other bounding box.
	 * @param b1 is a main {@code BoundingBox}.
	 * @param b2 is a {@code BoundingBox} to check if it's intersect with main bounding box.
	 * @return
	 */
	public static boolean intersect(BoundingBox b1, BoundingBox b2) {
		return b1.getXmin() <= b2.getXmax() && b1.getXmax() >= b2.getXmin() && b1.getYmin() <= b2.getYmax() && b1.getYmax() >= b2.getYmin();
	}
}
