package networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import networking.globals.Globals;

public class ClientSender extends Thread {

	private byte[] buffer = new byte[256];
	private InetAddress server;
	private DatagramSocket socket;
	private boolean running;
 	private BlockingQueue<String> messageQueue;
	
	public ClientSender(InetAddress newServer, BlockingQueue newQueue) {
		this.server = newServer;
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("client sender cannot acquire socket");
			e.printStackTrace();
		}
		this.messageQueue = newQueue;
	}

	public void run() {
		this.running = true;
		String message = "";
		while (running) {
			try {
				message = messageQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//send message once got
			buffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, Globals.PORT);
			//System.out.println("sending " + message);
			
			try {
				socket.send(packet);
			} catch (IOException e) {
				System.out.println("error sending from client");
				System.exit(-1);
				e.printStackTrace();
			}
			try {Thread.sleep(5);} catch (InterruptedException e1) {}
			//System.out.println("sent");
		}
		socket.close();
	}
	
	public String sendData(String msg) {
		
	
		return "";
	}
	public void halt() {
		this.running = false;
	}
}
