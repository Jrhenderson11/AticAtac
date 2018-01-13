package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.commons.lang3.SerializationUtils;

import networking.globals.Globals;
import networking.model.Model;

public class ClientReceiver extends Thread {

	private Model model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private DatagramSocket socket;
	private Model oldModel;
	
	
	public ClientReceiver() {
		this.running = false;
		this.oldModel = new Model(0, 0);
		try {
			this.socket = new DatagramSocket(Globals.PORT2);
		} catch (SocketException e) {
			System.out.println("cannot bind client port");
		}
	}
	
	public void run() {
		int count = 0;
		this.running = true;
		System.out.println("Client Listening");
		while (running) {
			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			//System.out.println("waiting for data");
			//System.out.println("client waiting for data");
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in Client Receiver Thread");
				e.printStackTrace();
			}

			//String received = new String(packet.getData(), 0, packet.getLength());
			model = SerializationUtils.deserialize(packet.getData());
			if ((model.getX() != oldModel.getX()) && (model.getY() != oldModel.getY())) {
				count++;
				System.out.println("got new model:");
				System.out.println("X: " + model.getX());
				System.out.println("Y: " + model.getY());
				System.out.println(count);
			}
		
			oldModel = model;
		}
		
	}
	
	public void halt() {
		this.running = false;
	}
	
	public Model getModel() {
		return this.model;
	}
	
	
	
	
}
