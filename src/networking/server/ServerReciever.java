package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import networking.globals.Globals;
import networking.model.Model;

public class ServerReciever extends Thread {

	private DatagramSocket socket;
	private Model model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private ArrayList<InetAddress> clientList;
	
	
	public ServerReciever(Model newModel, ArrayList<InetAddress> newList) {
		this.model = newModel;
		this.clientList = newList;
		try {
			this.socket = new DatagramSocket(Globals.PORT);
		} catch (SocketException e) {
			// TODO: handle exception
			System.out.println("unable to lock port");
		}
		
	}

	public void run() {
		InetAddress origin;
		running = true;
		System.out.println("Server Listening");
		while (running) {

			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			//System.out.println("waiting for data");
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in Server Receiver Thread");
				e.printStackTrace();
				System.exit(-1);
			}

			String received = new String(packet.getData(), 0, packet.getLength());
			origin = packet.getAddress();
			processData(received, origin);
			// if recieved end terminate via running = false

		}
		System.out.println("stopping server reciever");
		socket.close();
	}
	
	public void halt() {
		this.running = false;
	}
	
	private void processData(String data, InetAddress origin) {
		//System.out.println(data);
		if (data.equals("join")) {
			if (clientList.size() < Globals.MAX_CONNECTIONS) {
				//add to list + send confirmation
				this.clientList.add(origin);
			}
		} else if (clientList.contains(origin)==false) {
			//reject as unkown client
			return;
		}
		
		
		if (data.equals("stop")) {
			this.halt();
		} else if (data.split(":")[0].equals("ADD")) {
			int addX = Integer.parseInt(data.split(":")[1]);
			int addY = Integer.parseInt(data.split(":")[2]);
			this.model.addToCoords(addX, addY);
		}
	}
}
