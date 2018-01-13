package networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import networking.globals.Globals;
import networking.model.Model;

public class ServerReciever extends Thread {

	private DatagramSocket socket;
	private Model model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ClientInfo> clientList;
	private CopyOnWriteArrayList<InetAddress> addressList;
	private UDPServer master;
	
	public ServerReciever(Model newModel, CopyOnWriteArrayList<ClientInfo> newList, UDPServer newMaster) {
		this.model = newModel;
		this.clientList = newList;
		this.master = newMaster;
		try {
			this.socket = new DatagramSocket(Globals.SERVER_PORT);
		} catch (SocketException e) {
			System.out.println("unable to lock port");
		}
		this.addressList = new CopyOnWriteArrayList<>();
		
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
			processData(received, packet.getAddress());
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
				
				String newName = "P" + Integer.toString(clientList.size()+1);
				int port = Globals.CLIENT_PORT + (clientList.size());
				ClientInfo newClient = new ClientInfo(newName, origin, port);
				this.clientList.add(newClient);
				this.addressList.add(origin);
				System.out.println("added client: " + newClient.toString());
				//send ACK
 			
			} else {
				//send RST
				
			}
		} else if (addressList.contains(origin)==false) {
			//reject as unknown client
			return;
		}
		
		
		if (data.equals("stop")) {
			this.master.halt();
		} else if (data.split(":")[0].equals("ADD")) {
			int addX = Integer.parseInt(data.split(":")[1]);
			int addY = Integer.parseInt(data.split(":")[2]);
			this.model.addToCoords(addX, addY);
		}
	}
}
