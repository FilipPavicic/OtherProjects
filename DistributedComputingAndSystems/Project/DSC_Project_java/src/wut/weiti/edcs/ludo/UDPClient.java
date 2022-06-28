package wut.weiti.edcs.ludo;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UDPClient implements Closeable {
	protected BlockingQueue<String>  queue = new LinkedBlockingQueue<String>();
	protected final List<Integer> ADDRESSES = Arrays.asList(20000,30000,40000,50000);
	protected InetAddress ip;
	protected boolean debug;
	protected Thread worker;
	protected Thread listener;
	protected int address; 
	DatagramSocket ds_send;

	public UDPClient(boolean debug) throws UnknownHostException, SocketException {
		super();
		this.debug = debug;
		this.address = getAddress(ADDRESSES);
		ip =  InetAddress.getByName("127.0.0.1");
		ds_send = new DatagramSocket();

		worker = createWorker();
		worker.start();

		listener = createListener();	
		listener.start();

	}



	public void readMessage(String message) {
		if (this.debug)
			System.out.println(message);
	}

	protected Thread createListener() {
		Thread listener = new Thread() {
			public void run() {
				try(DatagramSocket ds = new DatagramSocket(address)) {
					byte[] receive = new byte[1024];
					DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
					while (true) {
						ds.receive(DpReceive);
						int offset=DpReceive.getOffset();
						int length=DpReceive.getLength();
						String message = new String(receive,offset,length, StandardCharsets.UTF_8);
						
						queue.add(message);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			};
		};
		listener.setDaemon(true);
		return listener;
	}

	protected Thread createWorker() {
		Thread worker = new Thread() {
			public void run() {
				while (true) {
					String message = null;
					try {
						message = queue.take();
					} catch (InterruptedException e) {
						continue;
					}
					readMessage(message);
				}
			};
		};
		worker.setDaemon(true);
		return worker;
	}

	protected static boolean available(int port) {
		try (DatagramSocket ds = new DatagramSocket(port)) {
			return true;
		} catch (SocketException ignored) {
			return false;
		}
	}

	protected int getAddress(List<Integer> addresses) {		
		for (int a : addresses) {
			if(available(a)) {
				System.out.println("Get adress: "+ a);
				return a;
			}

		}
		throw new IllegalStateException("There is no free ports");
	}

	public void broadcast(Codes code, Collection<Integer> addresses, String message) {
		message = this.createMessage(code, message);
		byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
			for(Integer a: addresses) {
				if (a == this.address) continue;
				DatagramPacket DpSend = new DatagramPacket(bytes, bytes.length, ip, a);
				try {
					ds_send.send(DpSend);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		

	}



	protected String createMessage(Codes code, String message) {
		return code + "::" + this.address + "::" + message;
	}



	@Override
	public void close() throws IOException {
		ds_send.close();

	}




}
