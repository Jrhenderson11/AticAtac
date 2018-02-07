package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.lobby.utils.Lobby;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;

public class ServerSender extends Thread {
	
	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private Model model;
	private boolean running;
	private DatagramSocket socket;
	private UDPServer master;
	
	public ServerSender(Model newModel, CopyOnWriteArrayList<ConnectionInfo> newList, UDPServer newMaster) {
		this.model = newModel;
		this.clientList = newList;
		this.master = newMaster;
	}

	public void run() {
		InetAddress address;
		System.out.println("TestServer sender started");
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("error creating server socket");
		}
		System.out.println("got sender socket");
		running = true;
		while (running) {
			for (ConnectionInfo client : clientList) {
				address = client.getAddress();
				byte[] buffer;
				if (master.getStatus() == Globals.IN_LIMBO) {
					buffer = SerializationUtils.serialize(master.getLobbyInfo());
				} else if (master.getStatus() == Globals.IN_LOBBY) {
					// serve lobby object
					buffer = SerializationUtils.serialize(master.getLobby());
				} else {
					// serve game object
					buffer = SerializationUtils.serialize(model);
				}
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, client.getPort());

				try {
					int len = (packet.getLength() + 100);
					socket.setSendBufferSize(len);
				} catch (SocketException e2) {
					e2.printStackTrace();
				}
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
	
	public void sendAllLobby() {
		InetAddress address;
		for (ConnectionInfo client : clientList) {
			address = client.getAddress();
			byte[] buffer;

			buffer = SerializationUtils.serialize(master.getLobby());
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, client.getPort());

			try {
				int len = (packet.getLength() + 100);
				socket.setSendBufferSize(len);
			} catch (SocketException e2) {
				e2.printStackTrace();
			}

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

	public void halt() {
		this.running = false;
	}

}