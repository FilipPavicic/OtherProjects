package hr.fer.ooup.lab4;
public class Point implements Comparable<Point>{
	private int x;
	private int y;
	
	public Point(int x, int y){
		super();
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public double distanceFromZero() {
		return Math.hypot(this.getX(), this.getY());
	}

	@Override
	public int compareTo(Point o) {
		return Double.compare(this.distanceFromZero(), o.distanceFromZero());
	}
	public void translate(Point delta) {
		this.x += delta.x;
		this.y += delta.y;
	}
	
	public Point duplicate() {
		return new Point(this.x, this.y);
	}
	
}
