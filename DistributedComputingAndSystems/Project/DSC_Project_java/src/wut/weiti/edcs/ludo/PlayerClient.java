package wut.weiti.edcs.ludo;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayerClient extends UDPClient {

	private String me;
	Set<Integer> online_addresses;
	GameState game;
	private Set<Integer> returnProcess;
	private static final List<Codes> broadcastToMe = Arrays.asList(
			Codes.PLAYED_MOVE, 
			Codes.RETURN,
			Codes.WINNER
			);


	public PlayerClient(boolean debug, String me) throws UnknownHostException, SocketException {
		super(debug);
		this.me = me;
		this.online_addresses = new HashSet<Integer>();
		returnProcess = new HashSet<Integer>();
		game = new GameState();

	}

	@Override
	public void broadcast(Codes code, Collection<Integer> addresses, String message) {
		super.broadcast(code, addresses, message);
		if (broadcastToMe.contains(code) && addresses.contains(this.address)) {
			String message_me = this.createMessage(code, message);
			this.readMessage(message_me);
		}
	}


	@Override
	public void readMessage(String message) {
		String[] split = message.split("::");
		
		Codes code = Codes.valueOf(split[0].toUpperCase());
		int sender = Integer.parseInt(split[1]);
		message = split.length < 3 ? "": split[2];
		switch (code) {
		case HELLO -> {
			this.online_addresses.add(sender);
			Tools.log(this.debug,code, "Change in online addresses", this.online_addresses.toString());
			this.broadcast(Codes.HELLO_RPL, Arrays.asList(sender), "");

			if (this.game.gameStatus.equals(GameStatus.INVITE) && this.me.equals(this.game.leader)) {
				this.broadcast(Codes.INVITE, Arrays.asList(sender), this.me);
			}
			if (this.game.gameStatus.equals(GameStatus.YES) &&
					this.me.equals(this.game.leader) &&
					this.game.ps.keySet().contains(message)					
					) {
				this.doCommand(Codes.RETURN, message,null);
				this.returnProcess.add(sender);
			}

		}
		case HELLO_RPL -> {
			this.online_addresses.add(sender);
			Tools.log(this.debug,code, "Change in online addresses", this.online_addresses.toString());
		}
		case EXIT -> {
			if (this.game.players.contains(message)) {
				if (message.equals(this.game.leader) && this.game.getNextPlayer(message) == this.me) {
					this.doCommand(Codes.LEADER, "", null);
				}
			}
			if (this.game.leader.equals(this.me) && message.equals(this.game.onMovePlayer)) {
				String nextPLayer = this.game.getNextPlayer(message);
				this.doCommand(Codes.ON_MOVE, nextPLayer, Arrays.asList(sender));
			}
			this.game.players.remove(message);
			this.online_addresses.remove(sender);
			Tools.log(this.debug,code, "Change in online addresses", this.online_addresses.toString());
		}
		case INVITE -> {
			if (!this.game.gameStatus.equals(GameStatus.YES)) {
				this.game.gameStatus = GameStatus.INVITE;
				this.game.leader = message;
				Tools.log(this.debug,code, "Recieve invite from "+ message);
			}
		}
		case ACC_INVITE -> {
			if (this.me.equals(this.game.leader)) {
				this.game.players.add(message);
				Tools.log(this.debug,code, "Players: "+ this.game.players);	
			}
		}
		case START -> {
			this.game.gameStatus = GameStatus.YES;
			this.game.players = Stream.of(message.split(" ")).collect(Collectors.toList());
			this.game.startGame();
			Tools.log(this.debug,code, "Players: "+ this.game.players);
		}
		case LEADER -> {
			if (this.game.gameStatus.equals(GameStatus.YES)){
				this.game.leader = message;
			}
			Tools.log(this.debug,code, "Changed Leader: "+ this.game.leader);
		}
		case ON_MOVE -> {
			if (this.game.gameStatus.equals(GameStatus.YES)){
				this.game.onMovePlayer = message;
				Tools.log(this.debug, code, "Players on move: "+ this.game.onMovePlayer);
				if (this.game.onMovePlayer.equals(this.me)) {
					Tools.gamePrint("Your turn to play, write play to play move");
				}
			}
		}
		case PLAYED_MOVE -> {
			if (this.game.gameStatus.equals(GameStatus.YES)){
				Move move = this.textToMove(message);
				this.game.loadMove(move);
				Tools.log(this.debug, code, "Recive move: "+ message);
				Tools.gamePrint(Tools.str(this.game.ps));
				this.sendOnMove(move);
			}
		}
		case WINNER -> {
			if (this.game.gameStatus.equals(GameStatus.YES)){
				Tools.gamePrint("Player: "+ message + " is the winner!!");
				this.game = new GameState();
			}
		}
		case UPDATE_STATE -> {
			this.game.setState(message);
			Tools.log(this.debug, code, "Updated state: "+ message);
		}
		case RETURN -> {
			this.game.players.add(message);
			if (message.equals(this.me)){
				Tools.log(this.debug, code, "I am back to the game");
			}
			else {
				Tools.log(this.debug, code, message +" is back to the game");
			}
		}
		case TEXT -> {
			Tools.log(this.debug,code, message);
		}

		default ->
		throw new IllegalArgumentException("Unexpected value: " + code);
		}
	}


	public void doCommand(Codes code, String extras, Collection<Integer> adresses) {
		switch (code) {
		case EXIT -> {
			broadcast(Codes.EXIT, this.online_addresses, this.me);

		}
		case HELLO -> {
			Tools.log(this.debug, code, "Sending Hello");
			broadcast(Codes.HELLO,this.ADDRESSES ,this.me);
		}
		case INVITE -> {
			if (this.game.gameStatus.equals(GameStatus.NO)){
				this.game.gameStatus = GameStatus.INVITE;
				this.game.leader = this.me;
				this.game.players.add(this.me);
				broadcast(Codes.INVITE, this.online_addresses, this.me);
				Tools.log(this.debug, Codes.INVITE, "Send Invitation:");
			}
			else {
				Tools.log(this.debug,Codes.INVITE,"Can not invite");
			}
		}
		case ACC_INVITE -> {
			if (this.game.gameStatus.equals(GameStatus.INVITE)){
				broadcast(Codes.ACC_INVITE, this.online_addresses, this.me);
			} else {
				Tools.log(this.debug, Codes.ACC_INVITE, "You have not been invited");
			}
		}
		case START -> {
			if (this.game.gameStatus.equals(GameStatus.INVITE)){
				if (this.game.leader.equals(this.me)) {
					this.game.gameStatus = GameStatus.YES;
					this.game.startGame();
					broadcast(Codes.START, this.online_addresses, this.game.players.stream().collect(Collectors.joining(" ")));
					Tools.log(this.debug, Codes.START, "Send Players: "+ this.game.players);
					this.doCommand(Codes.ON_MOVE, this.me,null);
				} else {
					Tools.log(this.debug, code, "Only the leader can start the game");
				}
			} else {
				Tools.log(this.debug, code, "Cannot start the game without invite");
			}
		}
		case LEADER -> {
			if (this.game.gameStatus.equals(GameStatus.YES)) {
				this.game.leader = this.me;
				broadcast(Codes.LEADER, this.online_addresses, this.me);
				Tools.log(this.debug,code, "Send Changed Leader: "+ this.game.leader);
			}
		}
		case ON_MOVE -> {
			if(this.game.gameStatus.equals(GameStatus.YES)) {
				this.game.onMovePlayer = extras;
				broadcast(Codes.ON_MOVE, this.online_addresses, this.game.onMovePlayer);
				if (this.game.onMovePlayer.equals(this.me)) {
					Tools.gamePrint("Your turn to play, write play to play move");
				}

			}
		}
		case PLAYED_MOVE -> {
			if (this.game.gameStatus.equals(GameStatus.YES)) {
				if ( this.game.onMovePlayer.equals(this.me)) {
					Move play = this.game.play(this.me);
					String text = this.moveToText(play);
					this.game.onMovePlayer = null;
					Tools.log(this.debug, code, "Sending move: "+ text);
					var a = new ArrayList<Integer>(this.online_addresses);
					a.add(this.address);
					broadcast(Codes.PLAYED_MOVE, a, text);

				}
				else {
					Tools.gamePrint("It is not your move.");
				}
			}
		}
		case UPDATE_STATE -> {
			broadcast(code,adresses,extras);
			Tools.log(this.debug, code, "Send Upadated state to adress: " + adresses);
		}
		case WINNER -> {
			if(this.game.gameStatus.equals(GameStatus.YES)) {
				broadcast(Codes.WINNER,adresses, extras);
			}
		}
		case RETURN -> {
			var a = new ArrayList<Integer>(this.online_addresses);
			a.add(this.address);
			broadcast(code,a, extras);
			Tools.log(this.debug, code, "Inform all players about the returning player");
		}

		default ->
		throw new IllegalArgumentException("Unexpected value: " + code);
		}
	}
	public String moveToText(Move move) {
		return this.me+ "<>"+ (move.cont ? "True" : "False")+"<>"+ (move.piece == null ? "None" :"['"+move.piece+"', "+move.position+"]");
	}

	public Move textToMove(String text) {
		String[] split = text.split("<>");
		if (split[2].equals("None")) return new Move(split[0], false, null, 0);
		String[] movePos = split[2].substring(1, split[2].length()-1).split(", ");
		return new Move(
				split[0], 
				Boolean.parseBoolean(split[1]),
				movePos[0].substring(1,movePos[0].length()-1),
				Integer.parseInt(movePos[1])
				);
	}

	public void sendOnMove(Move move) {
		if (this.game.leader.equals(this.me)) {
			Tools.log(this.debug, Codes.ON_MOVE, "Sending on move");
			if (returnProcess.size() != 0) {
				this.doCommand(Codes.UPDATE_STATE, this.game.stateToText(), this.returnProcess);
				this.returnProcess.clear();
			}
			String winnner = this.game.getWinner();
			if (winnner != null) {
				var a = new ArrayList<Integer>(this.online_addresses);
				a.add(this.address);
				this.doCommand(Codes.WINNER, winnner, a);
				return;
			}
			String nextPlayer = move.cont ? move.player : this.game.getNextPlayer(move.player);
			this.doCommand(Codes.ON_MOVE, nextPlayer, null);
		}
	}

	public void setMe(String me) {
		this.me = me;
	}

	public String getMe() {
		return me;
	}






}
