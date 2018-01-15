package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

public class ClientReceiver extends Thread {

	private String name;
	private Model model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private DatagramSocket socket;
	private Model oldModel;
	private int port;

	public ClientReceiver(String newName) {
		this.name = newName;
		this.running = false;
		this.oldModel = new Model(0, 0);
		// select next open port
		this.port = Globals.CLIENT_PORT;
		while (true) {
			try {
				this.socket = new DatagramSocket(port);
				break;
			} catch (SocketException e) {
				this.port++;
			}
		}
		System.out.println(name + " bound to " + port);

	}

	public void run() {
		int count = 0;
		this.running = true;
		System.out.println(name + " Listening");
		
		while (running) {

			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in Client Receiver Thread (Server Down)");
				break;
			}

			// String received = new String(packet.getData(), 0, packet.getLength());
			model = SerializationUtils.deserialize(packet.getData());
			if ((model.getX() != oldModel.getX()) && (model.getY() != oldModel.getY())) {
				count++;
				System.out.println(name + " got new model:");
				System.out.println("X: " + model.getX());
				System.out.println("Y: " + model.getY());
				System.out.println(count);
			}

			oldModel = model;
		}
		socket.close();
		System.out.println(name + " r done");
	}

	public void halt() {
		this.running = false;
		socket.close();
	}

	public Model getModel() {
		return this.model;
	}

}
