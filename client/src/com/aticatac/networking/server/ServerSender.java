package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

public class ServerSender extends Thread {
	//	DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
	private CopyOnWriteArrayList<ClientInfo> clientList;
	private Model model;
	private boolean running;
	private DatagramSocket socket;
	
	public ServerSender(Model newModel, CopyOnWriteArrayList<ClientInfo> newList) {
		this.model = newModel;
		this.clientList = newList;	
	}
	
	public void run() {
		InetAddress address;
		System.out.println("Server sender started");
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("error creating server socket");
		}
		System.out.println("got sender socket");
		running = true;
		while (running) {
			for (ClientInfo client : clientList) {
				address = client.getAddress();
				byte[] buffer = SerializationUtils.serialize(model);
				
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, client.getPort());
				
				try {
					int len = (packet.getLength() + 100);
					socket.setSendBufferSize(len);
				} catch (SocketException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				//System.out.println("sending model " + address.toString() +":"+ client.getPort());
				try {

					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("buffer length:");
					System.out.println(buffer.length);
					
					try {
						System.out.println("max allowed length");
						System.out.println(socket.getSendBufferSize());
					} catch (SocketException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}	
			
			
		}
		System.out.println("server sender stopped");
	}
	
	public void halt() {
		this.running = false;
	}
	
	
}
