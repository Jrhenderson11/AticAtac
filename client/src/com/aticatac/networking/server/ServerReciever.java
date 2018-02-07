package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

import javafx.scene.paint.Color;

public class ServerReciever extends Thread {

	private DatagramSocket socket;
	private Model model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private CopyOnWriteArrayList<InetAddress> addressList;
	private UDPServer master;
	
	public ServerReciever(Model newModel, CopyOnWriteArrayList<ConnectionInfo> newList, UDPServer newMaster) {
		this.model = newModel;
		this.clientList = newList;
		this.master = newMaster;
		try {
			this.socket = new DatagramSocket(Globals.SERVER_PORT);
		} catch (SocketException e) {
			System.out.println("unable to lock port");
		}
		this.addressList = new CopyOnWriteArrayList<>();
	}

	public void run() {
		InetAddress origin;
		running = true;
		System.out.println("TestServer Listening");
		while (running) {

			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			//System.out.println("waiting for data");
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in TestServer Receiver Thread");
				e.printStackTrace();
				System.exit(-1);
			}

			String received = new String(packet.getData(), 0, packet.getLength());
			processData(received, packet.getAddress());
			// if recieved end terminate via running = false

		}
		System.out.println("stopping server reciever");
		socket.close();
	}
	
	public void halt() {
		this.running = false;
	}

	public ConnectionInfo getInfo(InetAddress address) {
		
		for (ConnectionInfo info:this.clientList) {
			System.out.println(info.getAddress());
			if (info.getAddress().equals(address)) {
				return info;
			}
		}
		System.out.println("invalid client address to search for");
		return null;
	}
	
	private void processData(String data, InetAddress origin) {
		//System.out.println(data);
		if (data.equals("join")) {
			if (clientList.size() < Globals.MAX_CONNECTIONS) {
				//add to list + send confirmation
				
				String newName = "P" + Integer.toString(clientList.size()+1);
				int port = Globals.CLIENT_PORT + (clientList.size());
				ConnectionInfo newClient = new ConnectionInfo(newName, origin, port);
				this.clientList.add(newClient);
				this.addressList.add(origin);
				System.out.println("added client: " + origin);
			}
		} else if (addressList.contains(origin)==false) {
			//reject as unknown client
			return;
		}
		
		String[] parts = data.split(":");
		if (data.equals("init")) {
			ConnectionInfo info = this.getInfo(origin);
			master.joinLobby(info.getName(), origin, 2);
		} else if (data.equals("start")) {
			this.master.startGame();
		} else if (data.equals("stop")) {
			this.master.halt();
		} else if (parts[0].equals("input")) {
			//client.sendData("input:"+moveUp + ":"+moveDown + ":"+moveLeft + ":"+moveRight + ":" + run + ":"+speed);
			boolean up = Boolean.parseBoolean(parts[1]);
			boolean down = Boolean.parseBoolean(parts[2]);
			boolean left = Boolean.parseBoolean(parts[3]);
			boolean right = Boolean.parseBoolean(parts[4]);
			boolean run = Boolean.parseBoolean(parts[5]);
			int speed = Integer.parseInt(parts[6]);
			model.update(up, down, left, right, run, speed);
		}
	}
}
