package com.aticatac.networking.client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aticatac.networking.model.Model;

public class UDPClient extends Thread {

	private String name;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	private ClientReceiver receiver;
	ClientSender sender;

	public UDPClient(String newName, InetAddress newAddress) {
		this.name = newName;
		this.address = newAddress;
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

	public Model getModel() {
		return this.receiver.getModel();
	}
}