package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.packets.Packet;
import com.aticatac.world.World;

public class ClientReceiver extends Thread {

	private String name;
	private World model;
	private boolean running;
	private byte[] buffer = new byte[100000];
	private DatagramSocket socket;

	private int port;

	private UDPClient master;

	/**
	 * makes a new receiver
	 * 
	 * @param newName
	 *            name of this client (useful for debugging stuff)
	 * @param newMaster
	 *            UDPClient that controls this thread
	 */
	public ClientReceiver(String newName, UDPClient newMaster) {
		this.name = newName;
		this.running = false;
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
		this.master = newMaster;
	}

	/**
	 * This is the run method, it keeps running in a loop at every iteration it
	 * listens for packets tries to cast them into the appropriate game state based
	 * off UDPClient.getStatus()
	 */
	@Override
	public void run() {
		this.running = true;
		System.out.println(name + " Listening");
		while (running) {

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			Packet frame;
			try {
				socket.receive(packet);
				frame = SerializationUtils.deserialize(packet.getData());
			} catch (IOException e) {
				System.out.println("IO error in Client Receiver Thread (Server Down)");
				break;
			}

			if (frame.getType().equals("ipp")) {
				try {
					master.setMyIP(InetAddress.getByName(frame.data.toString()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			} else if (frame.getType().equals("lbi")) {
				LobbyInfo newInfo = SerializationUtils.deserialize(frame.data);
				master.setLobbyInfo(newInfo);
			} else if (frame.getType().equals("lby")) {
				//System.out.println("I HAZ LOBBY");
				Lobby newLobby = SerializationUtils.deserialize(frame.data);
				if (master.getStatus() == Globals.IN_LIMBO) {
					 master.setLobbyInfo(new LobbyInfo(4, newLobby.getAll().size(), newLobby.ID, newLobby.NAME));
				}
				master.setLobby(newLobby);
				if (master.getStatus()==Globals.IN_LOBBY && newLobby.getStarted() == true) {
					System.out.println("start game");
					master.setStatus(Globals.IN_GAME);
				}
			} else if (frame.getType().equals("gam")) {
				this.model = SerializationUtils.deserialize(frame.data);
			} else {
				System.out.println("type: " + frame.getType());
			}

			if (master.getStatus() == Globals.IN_LOBBY) {
				if (this.model != null) {
					this.master.setStatus(Globals.IN_GAME);
				}
			} else if (master.getStatus() == Globals.IN_GAME) {
				// System.out.println("in game");
				if (this.master.getLobby() == null) {
					this.master.requestLobby();
				}
			}

		}
		socket.close();
		System.out.println(name + " r done");
	}

	/**
	 * starts this thread
	 */
	public void halt() {
		this.running = false;
		socket.close();
	}

	/**
	 * Return the most up to date model of the world
	 * 
	 * @return The world object
	 */
	public World getModel() {
		return this.model;
	}

	/**
	 * A method used for debugging over the networking
	 */
	private static String byteArrayToHexString(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}
}