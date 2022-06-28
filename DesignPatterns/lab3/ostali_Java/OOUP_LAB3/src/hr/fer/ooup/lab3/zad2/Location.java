package hr.fer.ooup.lab3.zad2;

public class Location implements Comparable<Location>{
	int row;
	int column;
	public Location(int row, int column){
		super();
		this.row = row;
		this.column = column;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	@Override
	public int compareTo(Location o) {
		int rowcmp = Integer.valueOf(this.row).compareTo(Integer.valueOf(o.row));
		if(rowcmp != 0) return rowcmp;
		return Integer.valueOf(this.column).compareTo(Integer.valueOf(o.column));
	}
}