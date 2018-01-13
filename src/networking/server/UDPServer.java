package networking.server;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import networking.globals.Globals;
import networking.model.Model;

public class UDPServer extends Thread{

	private boolean running;
	private byte[] buffer = new byte[256];
	private ArrayList<InetAddress> clientList;
	
	public UDPServer() {
	
		this.clientList = new ArrayList<InetAddress>();
		
	}

	public void run() {
		
		Model model = new Model(0, 0);
		ServerReciever receiver = new ServerReciever(model, clientList); 
		ServerSender sender = new ServerSender(model, clientList);
		receiver.start();
		sender.start();
		System.out.println("thread started and waiting to join");
		try {
			receiver.join();
			sender.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("Threads joined");
		
	}

	

}
