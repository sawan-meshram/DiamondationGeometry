/**
 * @author Sawan Meshram
 */
package com.sawan.geometry;

import java.util.ArrayList;
import java.util.List;

import com.sawan.geometry.util.BoundingBoxUtil;


public class BoundingBox {

	private double xmin = 0;
	private double ymin = 0;
	private double xmax = 0;
	private double ymax = 0;
	
	public BoundingBox() {
		super();
	}

	public BoundingBox(double xmin, double ymin, double xmax, double ymax){
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}

	/**
	 * This function is used to create a {@code Polygon} WKT for this {@code BoundingBox}.
	 * @return Polygon in WKT <i>(Well-Known Text)</i> format. 
	 */
	public String createPolygon(){
		return BoundingBoxUtil.createPolygon(this);
	}
	
	/**
	 * This function is used to create a {@code MultiPolygon} WKT for this {@code BoundingBox}.
	 * @return MultiPolygon in WKT <i>(Well-Known Text)</i> format. 
	 */
	public String createMultiPolygon(){
		List<BoundingBox> list = new ArrayList<>();
		list.add(this);
		return BoundingBoxUtil.createMultiPolygon(list);
	}
	
	public void setXmin(double xmin) {
		this.xmin = xmin;
	}

	public void setYmin(double ymin) {
		this.ymin = ymin;
	}

	public void setXmax(double xmax) {
		this.xmax = xmax;
	}

	public void setYmax(double ymax) {
		this.ymax = ymax;
	}
	
	public double getXmin() {
		return xmin;
	}

	public double getYmin() {
		return ymin;
	}

	public double getXmax() {
		return xmax;
	}

	public double getYmax() {
		return ymax;
	}

	@Override
	public String toString() {
		return "BoundingBox [xmin=" + xmin + ", ymin=" + ymin + ", xmax=" + xmax + ", ymax=" + ymax + "]";
	}
}
