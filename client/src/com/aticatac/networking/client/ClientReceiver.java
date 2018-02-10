package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.commons.lang3.SerializationUtils;

import com.aticatac.lobby.Lobby;
import com.aticatac.lobby.utils.LobbyInfo;
import com.aticatac.networking.globals.Globals;
import com.aticatac.networking.model.Model;
import com.aticatac.world.World;

public class ClientReceiver extends Thread {

	private String name;
	private World model;
	private boolean running;
	private byte[] buffer = new byte[100000];
	private DatagramSocket socket;
	
	private int port;

	private UDPClient master;
	
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
		int count = 0;
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
			if (master.getStatus() == Globals.IN_LIMBO) {
				// deserialise into lobby obj
				try {
					LobbyInfo newInfo = SerializationUtils.deserialize(packet.getData());
					master.setLobbyInfo(newInfo);
				} catch (Exception e) {
					
					System.out.println("cannot deserialise lobbyinfo (is it a model?)");
				}
				System.out.println("getting info");
				
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
				}
				System.out.println("in lobby");
			} else if (master.getStatus() == Globals.IN_GAME) {
				//make game model
				try {
					this.model = SerializationUtils.deserialize(packet.getData());
				} catch (Exception e) {
					System.out.println("uh - oh");
				}
				//System.out.println("in game");
				if (model == null) {
				} else {
				}
	}

		}
		socket.close();
		System.out.println(name + " r done");
		// return new Object();
	}

	public void halt() {
		this.running = false;
		socket.close();
	}

	public World getModel() {
		return this.model;
	}

}