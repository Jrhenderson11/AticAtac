package com.aticatac.networking.client;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UDPClient extends Thread {

	private String name;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	private ClientReceiver receiver;
	ClientSender sender;

	public UDPClient(String newName) {
		this.name = newName;
		try {
			this.address = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("so apparently localhost is unreachable...");
			System.exit(-1);
		}
		this.messageQueue = new LinkedBlockingQueue<String>();
	}

	public void run() {

		receiver = new ClientReceiver(name);
		sender = new ClientSender(name, address, messageQueue);

		sender.start();
		receiver.start();

		try {
			sender.join();
			receiver.join();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(name + " stopped");

	}

	public void sendData(String data) {
		try {
			this.messageQueue.put(data);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void halt() {
		sender.halt();
		sender.interrupt();
		receiver.halt();
		receiver.interrupt();
	}
}