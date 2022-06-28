package wut.weiti.edcs.ludo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {

	static boolean debug = true;
	public static void main(String[] args)  {
		String me; 
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter name: ");
		String message = sc.nextLine();
		me = message;

		try(PlayerClient client = new PlayerClient(debug,me)) {
			client.broadcast(Codes.HELLO, client.ADDRESSES, "");
			while(sc.hasNextLine()){
				
				String inputMessage = sc.nextLine();

				if (inputMessage.startsWith("exit")) {
					client.doCommand(Codes.EXIT,"",null);
					break;
				}

				else if (inputMessage.startsWith("invite")) {
					client.doCommand(Codes.INVITE,"",null);
				}
				else if (inputMessage.startsWith("acc_invite")) {
					client.doCommand(Codes.ACC_INVITE,"",null);
				}
				else if (inputMessage.startsWith("start")) {
					client.doCommand(Codes.START,"",null);
				}	
				else if (inputMessage.startsWith("play")) {
					try {
						client.doCommand(Codes.PLAYED_MOVE,"",null);
					} catch (ExitException e) {
						client.doCommand(Codes.EXIT,"",null);
						break;
					}
				}
				else if (inputMessage.startsWith("die")) {
					int dic = Integer.parseInt(inputMessage.split(" ")[1]);
					Tools.log(debug, Codes.TEST, "DIC FOR TESTING: "+dic);
					client.game.setDie_TEST_ONLY(dic == 0 ? null : dic);
				}else {
					client.broadcast(Codes.TEXT, client.online_addresses, inputMessage);
				}



				//client.broadcast(message);

			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
