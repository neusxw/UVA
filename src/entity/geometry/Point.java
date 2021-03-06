package entity.geometry;

import java.text.DecimalFormat;

import data.SimUtils;

public class Point {
	public double x;
	public double y;
	public double z = 0;
	private LineSegment motherLine = null;
	private Point brotherPoint =null;

	public Point(){
		this.x = Double.NaN;
		this.y = Double.NaN;
	}

	public Point(double x,double y){
		this.x = x;
		this.y = y;
	}
	
	public Point(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point(double[] coord){
		this.x = coord[0];
		this.y = coord[1];
		if(coord.length==3) {
			this.z = coord[2];
		}
	}

	public double distanceToPoint(Point p) {
		return Math.sqrt((x - p.x)*(x - p.x)+(y - p.y)*(y - p.y));
	}

	public double directionToPoint(Point p) {
		return Math.atan2(p.y-y, p.x-x);
	}

	public double distanceToLine(Line line) {
		return line.distanceToPoint(this);
	}

	
	public double distanceToLineSegment(LineSegment lineSegment,String according) {
		if(according.toLowerCase().equals("mindis")) {
			return distanceToLineSegmentAccordingMinDis(lineSegment);
		}else if(according.toLowerCase().equals("midpoint")){
			return distanceToLineSegmentAccordingMidPoint(lineSegment);
		}
		return Double.NaN;
	}
	
	/**
	 * 将点到线段的距离定义为点到线段两端点距离中的最小值；
	 * @param lineSegment
	 * @return
	 */
	public double distanceToLineSegmentAccordingMinDis(LineSegment lineSegment) {
		double dis1 = distanceToPoint(lineSegment.endPoint1);
		double dis2 = distanceToPoint(lineSegment.endPoint2);
		if (dis1>=dis2) {
			return dis2;
		}else {
			return dis1;
		}
	}
	
	public double distanceToLineSegmentAccordingMidPoint(LineSegment lineSegment) {
		double x = (lineSegment.endPoint1.x+lineSegment.endPoint2.x)/2;
		double y = (lineSegment.endPoint1.y+lineSegment.endPoint2.y)/2;
		return distanceToPoint(new Point(x,y));
	}
	
	/*
	 * 判断一个点是在有向直线的左边、右边还是在该直线上
	 */
	public int positionToLine(Line line){
		if(isInLine(line)) {
			return SimUtils.IN;
		}else {
			Point foot = line.getFootOfPerpendicular(this);
			Line perpendicularLine = new Line(this,foot);
			if(SimUtils.doubleEqual(perpendicularLine.directionAngle+Math.PI/2, line.directionAngle)
					||SimUtils.doubleEqual(perpendicularLine.directionAngle-Math.PI*3/2, line.directionAngle)
					||SimUtils.doubleEqual(perpendicularLine.directionAngle+Math.PI*5/2, line.directionAngle)) {
				return SimUtils.LEFT;
			}else {
				return SimUtils.RIGHT;
			}
		}
	}

	public int positionToPolygon(Polygon polygon) {
		for(LineSegment lineSegment:polygon.edges) {
			if(isInLineSegment(lineSegment)) {
				return SimUtils.INBORDER;
			}
		}
		int verticesCount = polygon.vertexes.size();
		int nCross = 0;
		//做直线y=y0,看它与多边形各个边（线段）的相交情况，由单边交点数目判断点相对多边形的位置；
		for (int i = 0; i < verticesCount; ++ i) {
			Point p1 = polygon.vertexes.get(i);
			Point p2 = polygon.vertexes.get((i + 1) % verticesCount);
			//平行或在延长线上，不相交；
			if ( p1.y == p2.y ||y < Math.min(p1.y, p2.y)||y >= Math.max(p1.y, p2.y)) {
				continue;
			}
			double crossX = (y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y) + p1.x; // 求交点的 X 坐标
			if ( crossX > x ) { // 只统计单边交点
				nCross++;
			}
		}
		if(nCross%2==1) {
			return SimUtils.INNER;
		}else {
			return SimUtils.OUTTER;
		}
	}

	public boolean isInLine(Line line) {
		if(SimUtils.doubleEqual(line.A*x+line.B*y+line.C, 0)) {
			return true;
		}
		return false;
	}

	public boolean isInLineSegment(LineSegment line) {
		if(isInLine(line)) {
			if(SimUtils.doubleEqual(line.endPoint1.distanceToPoint(line.endPoint2),
					this.distanceToPoint(line.endPoint1)+this.distanceToPoint(line.endPoint2))) {
				return true;
			}
		}
		return false;
	}

	public boolean isNaN() {
		if(Double.isNaN(x)&&Double.isNaN(y)) {
			return true;
		}
		return false;
	}

	public boolean equals(Point point) {
		if (SimUtils.doubleEqual(x,point.x)&&
				SimUtils.doubleEqual(y,point.y)) {
			return true;
		}
		return false;
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0000000000");
		return "(" + df.format(x) + "," +df.format(y) + "," +df.format(z)+")";
	}
	
	public String toString(boolean highPrecision) {
		DecimalFormat df;
		if(highPrecision) {
			df = new DecimalFormat("0.00000000");
		}else {
			df = new DecimalFormat("0.00");
		}
		return "(" + df.format(x) + "," +df.format(y) + "," +df.format(z)+")";
	}

	public void print() {
		System.out.println(toString());
	}
	
	@Override
	public int hashCode() {
        // http://stackoverflow.com/questions/22826326/good-hashcode-function-for-2d-coordinates
        // http://www.cs.upc.edu/~alvarez/calculabilitat/enumerabilitat.pdf
        int tmp = (int) (y + ((x + 1) / 2));
        return Math.abs((int) (x + (tmp * tmp)));
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

	public LineSegment getMotherLine() {
		if(motherLine==null) {
			//System.out.println("error:该点没有母线！！！");
		}
		return motherLine;
	}

	public void setMotherLine(LineSegment motherLine) {
		this.motherLine = motherLine;
	}

	public Point getBrotherPoint() {
		if(brotherPoint==null) {
			System.out.println("error:该点没有兄弟节点！！！");
		}
		return brotherPoint;
	}

	public void setBrotherPoint(Point brotherPoint) {
		this.brotherPoint = brotherPoint;
	}
	
	
}
