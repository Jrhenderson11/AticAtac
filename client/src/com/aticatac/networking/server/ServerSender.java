package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ConcurrentModificationException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.networking.globals.Globals;
import com.aticatac.world.World;

public class ServerSender extends Thread {
	
	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private World model;
	private boolean running;
	private DatagramSocket socket;
	private UDPServer master;
	
	/**
	 * 
	 * @param newModel World to distribute
	 * @param newList List of clients to send data to
	 * @param newMaster UDPServer object to interact with
	 */
	public ServerSender(World newModel, CopyOnWriteArrayList<ConnectionInfo> newList, UDPServer newMaster) {
		this.model = newModel;
		this.clientList = newList;
		this.master = newMaster;
	}

	/**
	 * Thread run method, continously sends clients data
	 */
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
				if (master.getStatus() == Globals.IN_LIMBO && master.getLobbyInfo() !=null) {
					buffer = SerializationUtils.serialize(master.getLobbyInfo());
				} else if (master.getStatus() == Globals.IN_LOBBY) {
					// serve lobby object
					try {
						buffer = SerializationUtils.serialize(master.getLobby());
					} catch (ConcurrentModificationException e) {
						buffer = new byte[256];
					}
				} else {
					// serve game object
					buffer = SerializationUtils.serialize(model);
				}
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, client.getDestPort());

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
	
	
	/**
	 * forcibly sends every client on connection list a lobby object
	 */
	public void sendAllLobby() {
		InetAddress address;
		for (ConnectionInfo client : clientList) {
			address = client.getAddress();
			byte[] buffer;

			buffer = SerializationUtils.serialize(master.getLobby());
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, client.getDestPort());

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

	/**
	 * stops this running
	 */
	public void halt() {
		this.running = false;
	}

	/** sends a mesage to a client
	 * 
	 * @param origin client IP
	 * @param originPort client port
	 * @param msg message to send
	 */
	public void sendClientMessage(InetAddress origin, int originPort, String msg) {
		for (ConnectionInfo i : this.clientList) {
			if (i.getAddress().equals(origin) && (i.getOriginPort() == originPort)) {
		//		System.out.println("sending message to " + i.getAddress() + " " + msg);
				byte[] buffer = msg.getBytes();
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length, i.getAddress(), Globals.SERVER_PORT);
				try {
					socket.send(packet);
				} catch (IOException e) {
					System.out.println("error sending message from server");
					System.exit(-1);
					e.printStackTrace();
				}

			}
		}
	}	
	
	
}