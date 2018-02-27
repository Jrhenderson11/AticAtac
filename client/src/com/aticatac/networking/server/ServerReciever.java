package com.aticatac.networking.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aticatac.networking.globals.Globals;
import com.aticatac.utils.Controller;
import com.aticatac.world.AIPlayer;
import com.aticatac.world.Player;
import com.aticatac.world.World;

import javafx.scene.input.KeyCode;

/**
 * @author james
 *
 */
public class ServerReciever extends Thread {

	private DatagramSocket socket;
	private World model;
	private boolean running;
	private byte[] buffer = new byte[256];
	private CopyOnWriteArrayList<ConnectionInfo> clientList;
	private CopyOnWriteArrayList<InetAddress> addressList;
	private UDPServer master;

	public ServerReciever(World newModel, CopyOnWriteArrayList<ConnectionInfo> newList, UDPServer newMaster) {
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
		System.out.println("Server Listening");
		while (running) {
			// make packet
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			// System.out.println("waiting for data");
			try {
				socket.receive(packet);
			} catch (IOException e) {
				System.out.println("IO error in Server Receiver Thread");
				e.printStackTrace();
				System.exit(-1);
			}

			String received = new String(packet.getData(), 0, packet.getLength());
			processData(received, packet.getAddress(), packet.getPort());
			// if recieved end terminate via running = false

		}
		System.out.println("stopping server reciever");
		socket.close();
	}

	public void halt() {
		this.running = false;
	}

	private ConnectionInfo getConnectionInfo(InetAddress origin, int originPort) {
		for (ConnectionInfo info : this.clientList) {
			if (info.getAddress().equals(origin) && info.getOriginPort() == originPort) {
				return info;
			}
		}
		System.out.println("invalid client address to search for");
		return null;
	}

	/**
	 * @param data the string contained in the packet recieves
	 * @param origin address where the data is from
	 * @param originPort source port where data is from
	 */
	private void processData(String data, InetAddress origin, int originPort) {
		int port = Globals.CLIENT_PORT;
		// System.out.println(data);
		if (data.equals("join")) {
			if (clientList.size() < Globals.MAX_CONNECTIONS) {
				// add to list + send confirmation

				String newName = "P" + Integer.toString(clientList.size() + 1);
				port = Globals.CLIENT_PORT + (clientList.size());
				ConnectionInfo newClient = new ConnectionInfo(newName, origin, port, originPort);
				this.clientList.add(newClient);
				this.addressList.add(origin);
				System.out.println("added client: " + origin);
			}
		} else if (addressList.contains(origin) == false) {
			// reject as unknown client
			return;
		}

		String[] parts = data.split(":");
		// LOBBY STUFF
		if (data.equals("init")) {
			ConnectionInfo info = this.getConnectionInfo(origin, originPort);
			master.joinLobby(info.getID(), origin, 2, this.getConnectionInfo(origin, originPort).getDestPort(),
					originPort);
		} else if(parts[0].equals("joinlobby")) {
			int lobbyNum = Integer.parseInt(parts[1]);
			
			
		} else if (data.equals("ready")) {
			// SET LOBBY TO READY
			this.master.setClientReady(origin, originPort);

		} else if (data.equals("lobbypls")) {
			// SENDLOBBY
			this.master.sendAllLobby();

		} else if (data.equals("unready")) {
			// SET TO UNREADY
			this.master.setClientUnReady(origin, originPort);

		} else if (parts[0].equals("name")) {
			//this.master.startGame();

		} else if (parts[0].equals("kick")) {
			String id = parts[1];
			//TODO: check if this user is actually lobby leader
			this.master.getLobby().removeClient(id);

		} else if (data.equals("addAI")) {
			master.addAI();
					
		} else if (data.equals("leavelobby")) {
			//ConnectionInfo info = this.getConnectionInfo(origin, originPort);
			master.leaveLobby(origin, originPort);
		} else if (data.equals("start")) {
			this.master.startGame();
		} else if (data.equals("stop")) {
			this.master.halt();

			// GAME
		} else if (parts[0].equals("input")) {
			//System.out.println(data);
			ArrayList<KeyCode> input = new ArrayList<KeyCode>();
			for (String letter : parts[1].replaceAll("\\[", "").replaceAll(" ", "").replaceAll("\\]", "").split(",")) {
				input.add(KeyCode.getKeyCode(letter));
			}

			double dir = Double.parseDouble(parts[parts.length - 1]) / 1000;
			model.handleInput(input, dir, this.getConnectionInfo(origin, originPort).getID());
			model.update();
		} else if(parts[0].equals("click")) {
			model.shoot(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), this.getConnectionInfo(origin, originPort).getID());
		}
	}
}