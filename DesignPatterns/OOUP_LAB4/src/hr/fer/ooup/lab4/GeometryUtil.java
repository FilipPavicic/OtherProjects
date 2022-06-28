package hr.fer.ooup.lab4;

public class GeometryUtil {

	public static double distanceFromPoint(Point point1, Point point2) {
		int dx = point1.getX() - point2.getX();
		int dy = point1.getY() - point2.getY();
		return Math.hypot(dx, dy);
	}
	
	public static double distanceFromLineSegment(Point s, Point e, Point p) {
		if(s.compareTo(e) > 0) {
			Point t = s;
			s = e;
			e = t;
		}
		
		if(isBellowOrAboveLine(s, e, p)) {
			return distanceBetweenLineAndPoint(s, e, p);
		}
		
		return Math.min(distanceFromPoint(s, p), distanceFromPoint(e, p));
	}
	
	private static boolean isBellowOrAboveLine(Point pointOnLine1, Point pointOnLine2, Point testPoint) {
		double k = coefficentLine(pointOnLine1, pointOnLine2);
		double kVertical = -1.0 / k;
		return testPoint.getY() > kVertical * (testPoint.getX() - pointOnLine1.getX()) + pointOnLine1.getY() &&
				testPoint.getY() < kVertical * (testPoint.getX() - pointOnLine2.getX()) + pointOnLine2.getY();
	}
	
	private static double coefficentLine(Point pointOnLine1, Point pointOnLine2) {
		return (pointOnLine2.getY() - pointOnLine1.getY()) * 1.0 / (pointOnLine2.getX() - pointOnLine1.getX());
	}
	
	private static double distanceBetweenLineAndPoint(Point pointOnLine1, Point pointOnLine2, Point point) { 
		double k = coefficentLine(pointOnLine1, pointOnLine2);
		if(k == Double.NEGATIVE_INFINITY || k == Double.POSITIVE_INFINITY) return Math.abs(point.getX() - pointOnLine1.getX());
		if(k ==Double.NaN) return distanceFromPoint(pointOnLine1, point);
		return Math.abs(-k * point.getX() + point.getY() - pointOnLine1.getY() + k * pointOnLine1.getX()) / Math.sqrt(k*k +1);
	}
	public static double selectinDistance(Point mousePoint, Rectangle boundingBox) {
		Point A = new Point(boundingBox.getX(),boundingBox.getY());
		Point B = new Point(A.getX(), A.getY() + boundingBox.getHeight());
		Point C = new Point(A.getX() + boundingBox.getWidth(), A.getY() + boundingBox.getHeight());
		Point D = new Point(A.getX() + boundingBox.getWidth(), A.getY());
		double AB =  GeometryUtil.distanceFromLineSegment(A, B, mousePoint);
		double BC =  GeometryUtil.distanceFromLineSegment(B, C, mousePoint);
		double CD =  GeometryUtil.distanceFromLineSegment(C, D, mousePoint);
		double DA =  GeometryUtil.distanceFromLineSegment(D, A, mousePoint);
		if(AB <= boundingBox.getHeight() && CD <= boundingBox.getHeight() 
				&& BC <= boundingBox.getWidth() && DA <=boundingBox.getWidth()) return 0.0;
		return Math.min(AB, Math.min(BC, Math.min(CD, DA)));
	}
}