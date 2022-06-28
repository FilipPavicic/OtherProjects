package wut.weiti.edcs.ludo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.Scanner;

public class GameState {
	protected GameStatus gameStatus;
	protected String leader;
	protected String onMovePlayer;
	protected List<String> players;
	protected Map<String, Integer> playersIndex;
	protected Map<String,Map<String, Integer>> ps;
	protected int die_TEST_ONLY = 0;
	protected String piece_TEST_ONLY = null;
	
	public GameState() {
		gameStatus = GameStatus.NO;
		players = new ArrayList<String>();
		
	}
	
	public void setDie_TEST_ONLY(int die_TEST_ONLY) {
		this.die_TEST_ONLY = die_TEST_ONLY;
	}

	protected void startGame() {
		setPLayersIndex();
		startPosition();
	}
	protected void setPLayersIndex() {
		playersIndex = new HashMap<String, Integer>();
		int i = 0;
		for(String p :players) {
			playersIndex.put(p, i);
			i++;
		}
	}
	
	protected void startPosition() {
		ps = new HashMap<String, Map<String,Integer>>();
		for(String p :players) {
			Map<String, Integer> tmp = new HashMap<String, Integer>();
			tmp.put("a", 0);
			tmp.put("b", 0);
			tmp.put("c", 0);
			tmp.put("d", 0);
			ps.put(p, tmp);
			
		}
	}
	
	public String getNextPlayer(String player) {
		return this.players.get((this.players.indexOf(player) + 1) % this.players.size());
	}
	
	public int relativeToApsolute(int relative, int playerIndex) {
		if (relative == 0) return 0;
		if (relative <= 40) return (playerIndex * 10 + relative - 1) % 40 + 1;
		return relative + playerIndex * 10;
	}
	
	public Move play(String player) {
		int die  = die_TEST_ONLY == 0 ? ThreadLocalRandom.current().nextInt(1, 6 + 1) : die_TEST_ONLY;
		Tools.gamePrint("Your number on die is: "+ die);
		
		String[] pieces = getAvailablePieces(player, die);
		
		if (pieces.length == 0) {
			Tools.gamePrint("You can not move any of your pieces");
			return new Move(player, false, null, 0);
		}
		Tools.gamePrint("You can move on of following piceses: "+ Arrays.toString(pieces));
		String movePiece = piece_TEST_ONLY == null ? getMovePiece(pieces) : piece_TEST_ONLY;
		if (this.ps.get(player).get(movePiece) == 0 && die == 6)
			this.ps.get(player).put(movePiece, 1);
		else {
			Integer loc = this.ps.get(player).get(movePiece);
			this.ps.get(player).put(movePiece, loc + die);
			checkOthers(this.ps.get(player).get(movePiece), player);
		}
		boolean cont = die == 6;
		return new Move(player, cont, movePiece, this.ps.get(player).get(movePiece));
			
	}
	
	public void loadMove(Move move) {
		if (move.piece == null) return;
		this.ps.get(move.player).put(move.piece, move.position);
		checkOthers(this.ps.get(move.player).get(move.piece), move.player);
	}
	
	public String getWinner() {
		List<Integer> tmp = Arrays.asList(41,42,43,44);
		for (var p : this.players) {
			if (this.ps.get(p).values().containsAll(tmp)) return p;
		}
		return null;
	}

	protected void checkOthers(Integer relative, String player) {
		int absolute = this.relativeToApsolute(relative, this.playersIndex.get(player));
		this.ps.forEach((k,v) -> {
			if (k.equals(player)) return;
			v.forEach((piece, rel) -> {
				if (rel == 0) return;
				int piece_abs = relativeToApsolute(rel, playersIndex.get(k));
				if (absolute == piece_abs) {
					ps.get(k).put(k, 0);
				}
			});
		});
		
	}

	protected String getMovePiece(String[] pieces) {
		var list = Arrays.asList(pieces);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			System.out.println();
			String input = scanner.nextLine();
			if (input.equals("exit")) throw new ExitException();
			if (list.contains(input)) return input;
			Tools.gamePrint("Enter one of available pieces");
		}
		return null;
	}

	protected String[] getAvailablePieces(String player, int die) {
		var psPlayer = this.ps.get(player).entrySet();
		List<Integer> takenInHause = psPlayer.
				stream().
				filter((e) ->  e.getValue() > 40).
				map(e -> e.getValue()).
				collect(Collectors.toList());
		return psPlayer.
				stream().
				filter((e) -> 
					(e.getValue() == 0 && die == 6 ) ||
					(e.getValue() + die <= 40 && e.getValue() != 0) ||
					(e.getValue() + die > 40 &&  e.getValue() + die <= 44 && !takenInHause.contains(e.getValue() + die))
				).
				map(e -> e.getKey()).
				collect(Collectors.toList()).toArray(new String[] {});
	}
	public String stateToText() {
		return this.leader + "##"+
				Tools.str2(players) +"##"+
				Tools.str1(playersIndex) + "##"+
				this.onMovePlayer == null ? "None": onMovePlayer + "##" +
				Tools.str(this.ps);
				
	}
	
	public void setState(String text) {
		String[] split = text.split("##");
		this.leader = split[0];
		this.players = Tools.eval2(split[1]);
		this.playersIndex = Tools.eval1(split[2]);
		this.onMovePlayer = split[3];
		this.ps = Tools.eval(split[4]);
		this.gameStatus = GameStatus.YES;
		
	}
	
}
