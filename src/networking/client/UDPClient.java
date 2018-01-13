package networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UDPClient extends Thread{
	
	private DatagramSocket socket;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	
	
	public UDPClient() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e1) {
			System.out.println("Socket Exception in client");
		}
		try {
			address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("so apparently localhost is unreachable...");
			System.exit(-1);
		}
		messageQueue = new LinkedBlockingQueue<String>();
	}
	
	public void run() {
		
		ClientReceiver receiver = new ClientReceiver();
		ClientSender sender = new ClientSender(address, socket, messageQueue);
		
		sender.start();
		receiver.start();
		
		try {
			sender.join();
			receiver.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		socket.close();
		
		
		
	}
	
	public void sendData(String data) {
		try {
			this.messageQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		socket.close();
	}
}