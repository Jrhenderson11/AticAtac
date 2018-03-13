package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.LobbyInfo;
import com.aticatac.networking.globals.Globals;
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
	 * @param newName 	name of this client (useful for debugging stuff)
	 * @param newMaster UDPClient that controls this thread
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
	
	@Override
	public void run() {
		this.running = true;
		System.out.println(name + " Listening");
		while (running) {

			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in Client Receiver Thread (Server Down)");
				break;
			}
			
			try {
				String s = new String(packet.getData(), 0,packet.getLength());
				if (s.contains("IP:")) {
					//System.out.println(s);
					master.setMyIP(InetAddress.getByName(s));
				} else {
				//	System.out.println(s);
				}
			} catch (Exception e) {
				System.out.println("fail");
			}
			if (master.getStatus() == Globals.IN_LIMBO) {
				// deserialise into lobby obj
				try {
					LobbyInfo newInfo = SerializationUtils.deserialize(packet.getData());
					master.setLobbyInfo(newInfo);
				} catch (Exception e) {
					
					//System.out.println("cannot deserialise lobbyinfo (is it a model?)");
					try {
						Lobby newLobby = SerializationUtils.deserialize(packet.getData());
						master.setLobbyInfo(new LobbyInfo(4, newLobby.getAll().size(), newLobby.ID, newLobby.NAME));
					} catch (Exception e2) {}
				}
				//System.out.println("getting info");
				
			} else if (master.getStatus() == Globals.IN_LOBBY) {
				// deserialise into lobby obj
				try {
					Lobby newLobby = SerializationUtils.deserialize(packet.getData());
					master.setLobby(newLobby);
					if (newLobby.getStarted()==true) {
						System.out.println("start game");
						master.setStatus(Globals.IN_GAME);
					}
				} catch (Exception e) {
					System.out.println("cannot deserialise lobby (is it a model?)");
					try {
						this.model = SerializationUtils.deserialize(packet.getData());
						this.master.setStatus(Globals.IN_GAME);
					} catch (Exception e2) {
					}
				}
				//System.out.println("in lobby");
			} else if (master.getStatus() == Globals.IN_GAME) {
				//make game model
				try {
					this.model = SerializationUtils.deserialize(packet.getData());
				} catch (Exception e) {
					try {
						Lobby newLobby = SerializationUtils.deserialize(packet.getData());
						master.setLobby(newLobby);
					} catch (Exception e2) {
						System.out.println("uh - oh");
						//e2.printStackTrace();
					}
					
				}
				//System.out.println("in game");
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
	 * returns most up to date model of the world
	 * @return
	 */
	public World getModel() {
		return this.model;
	}
	
	/** used for debugging stuff over network 
	 * 
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