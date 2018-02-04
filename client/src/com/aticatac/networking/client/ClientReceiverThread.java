package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

import javafx.concurrent.Task;

public class ClientReceiverThread extends Thread {

	private String name;
	private Model model;
	private boolean running;
	private byte[] buffer = new byte[100000];
	private DatagramSocket socket;
	private Model oldModel;
	private int port;

	public ClientReceiverThread(String newName) {
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

	@Override
	public void run() {
		int count = 0;
		this.running = true;
		System.out.println(name + " Listening");
		
		while (running) {

			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			System.out.println("waiting for data");
			try {
				socket.receive(packet);
				System.out.println("got packet");
			} catch (IOException e) {
				System.out.println("IO error in Client Receiver Thread (Server Down)");
				break;
			}
			System.out.println("deserializing");
			// String received = new String(packet.getData(), 0, packet.getLength());
			this.model = SerializationUtils.deserialize(packet.getData());
			
			System.out.println("got model");
			
			if ((model.getX() != oldModel.getX()) && (model.getY() != oldModel.getY())) {
				count++;
				System.out.println(name + " got new model:");
				System.out.println("X: " + model.getX());
				System.out.println("Y: " + model.getY());
				System.out.println(count);
			}

			oldModel = model;
			System.out.println("end loop");
		}
		socket.close();
		System.out.println(name + " r done");
		//return new Object();
	}

	public void halt() {
		this.running = false;
		socket.close();
	}

	public Model getModel() {
		return this.model;
	}

}
