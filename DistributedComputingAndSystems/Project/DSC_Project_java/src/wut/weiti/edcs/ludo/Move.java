package wut.weiti.edcs.ludo;

public class Move {
	public String player;
	public boolean cont;
	public String piece;
	public int position;
	
	public Move(String player, boolean cont, String piece, int position) {
		super();
		this.player = player;
		this.cont = cont;
		this.piece = piece;
		this.position = position;
	}
	
	
}
