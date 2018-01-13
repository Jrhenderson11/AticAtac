package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.lang3.SerializationUtils;

import networking.globals.Globals;
import networking.model.Model;

public class ServerSender extends Thread {
	//	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
	private  ArrayList<InetAddress> clientList;
	private Model model;
	private boolean running;
	private DatagramSocket socket;
	
	
	public ServerSender(Model newModel, ArrayList<InetAddress> newList) {
		this.model = newModel;
		this.clientList = newList;	
	}
	
	public void run() {
		System.out.println("Server sender started");
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("error creating server socket");
		}
		System.out.println("got sender socket");
		running = true;
		while (running) {
			for (InetAddress client : clientList) {
				byte[] buffer = SerializationUtils.serialize(model);
				
				//byte[] buffer = byte(model);
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, client, Globals.PORT2);
				//System.out.println("sending model");
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
			
			
		}
	}
	
	public void halt() {
		this.running = false;
	}
	
	
}
