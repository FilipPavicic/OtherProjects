package hr.fer.ooup.lab3.zad2;

public class LocationRange {
	Location start;
	Location end;
	public LocationRange(Location start, Location end) {
		super();
		if(start.compareTo(end) > 0) {
			Location t = start;
			start = end;
			end = t;
		}
		this.start = start;
		this.end = end;
	}
	public Location getStart() {
		return start;
	}
	public void reset(Location point) {
		if(this.end.compareTo(point) >= 0) this.start = point;
		else this.end = point;
		if(start.compareTo(end) > 0) {
			Location t = start;
			start = end;
			end = t;
		}	
	}
	public LocationRange sorted() {
		LocationRange lr = new LocationRange(start, end);
		if(lr.start.compareTo(lr.end) > 0) {
			Location t = lr.start;
			lr.start = lr.end;
			lr.end = t;
		}
		return lr;	
	}
	public boolean isStartEndSame() {
		return start.compareTo(end) == 0;
	}
}
