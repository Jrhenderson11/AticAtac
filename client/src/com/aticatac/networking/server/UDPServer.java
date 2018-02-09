package com.aticatac.networking.server;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.networking.model.Model;

public class UDPServer extends Thread{

	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ClientInfo> clientList;
	ServerReciever receiver;
	ServerSender sender;
	Model model;
	
	public UDPServer() {
	
		this.clientList = new CopyOnWriteArrayList<ClientInfo>();
		
	}

	public void run() {
		
		this.model = new Model(0, 0);
		this.receiver = new ServerReciever(model, clientList, this); 
		this.sender = new ServerSender(model, clientList);
		receiver.start();
		sender.start();
		System.out.println("thread started and waiting to join");
		try {
			receiver.join();
			sender.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		System.out.println("TestServer stopped");
		
	}
	
	public void halt() {
		this.receiver.halt();
		this.sender.halt();
	}

	

}
