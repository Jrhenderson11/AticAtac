package com.aticatac.networking.client;

import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.aticatac.networking.model.Model;

import javafx.concurrent.Task;

public class UDPClient extends Task {

	private String name;
	private InetAddress address;
	private byte[] buffer;
	private BlockingQueue<String> messageQueue;
	private ClientReceiverThread receiver;
	private ClientSender sender;

	public UDPClient(String newName, InetAddress newAddress) {
		this.name = newName;
		this.address = newAddress;
		this.messageQueue = new LinkedBlockingQueue<String>();
	}

	@Override
	public Object call() {

		receiver = new ClientReceiverThread(name);
		sender = new ClientSender(name, address, messageQueue);
		
		Thread sendThread = new Thread(sender);
		sendThread.setDaemon(true);
		sendThread.start();
		//Thread recThread = new Thread(receiver);
		//recThread.setDaemon(true);
		//recThread.start();
		receiver.start();
		
		try {
			receiver.join();
			
		} catch (Exception e) {
			
		}
		//sender.cancel();
		//receiver.cancel();
		
		//System.out.println(name + " stopped");
		return new Object();
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
		sender.cancel();
		receiver.halt();
		//receiver.cancel();
	}

	public Model getModel() {
		return this.receiver.getModel();
	}
}