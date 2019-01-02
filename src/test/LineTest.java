package test;

import java.text.DecimalFormat;

import main.arithmetic.SimUtils;
import main.entity.Line;
import main.entity.LineSegment;
import main.entity.Point;

public class LineTest {

	public static void main(String[] args) {
		double x=1;
		double y=1;
		Point p0 = new Point(0,0);
		Point p1 = new Point(1,0);
		Point p2 = new Point(0,1);
		Point p3 = new Point(-1,0);
		Point p4 = new Point(0,-1);
		Point p5 = new Point(1,1);
		Point p6 = new Point(0.5,0.5);
		Point p7 = new Point(-0.5,-0.5);
		Point p8 = new Point(-1,-1);
		LineSegment line05 = new LineSegment(p0,p5);
		LineSegment line34 = new LineSegment(p3,p4);
		LineSegment line32 = new LineSegment(p3,p2);
		LineSegment line12 = new LineSegment(p1,p2);
		LineSegment line57 = new LineSegment(p5,p7);
		LineSegment line68 = new LineSegment(p6,p8);
		LineSegment ls1= new LineSegment(new Point(0.42,0.73),new Point(0.42,0.35));
		LineSegment ls2= new LineSegment(new Point(0.42,0.50),new Point(0.42,0.40));
		ls1.intersectionLineSegmentOfTwoLineSegments(ls2).print();
		System.out.println("OOOOOOOOO");
		ls2.intersectionLineSegmentOfTwoLineSegments(ls1).print();
	}

}
