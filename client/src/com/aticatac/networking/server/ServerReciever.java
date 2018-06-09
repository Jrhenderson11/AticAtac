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
import com.aticatac.utils.SystemSettings;
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

	/**
	 *  Constructor
	 * @param newModel World object to process inpu
	 * @param newList List of valid clients to receive connections from
	 * @param newMaster UDPServer object to report to
	 */
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

	/**
	 * Thread method makes this run
	 */
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

	/**
	 * stops this running
	 */
	public void halt() {
		this.running = false;
	}

	/** Gets a connectionInfo object by its IP and port
	 * 
	 * @param origin IP address
	 * @param originPort port number
	 * @return connectionInfo obj with that ip and port
	 */
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

		if (data.equals("join")) {
			if (clientList.size() < Globals.MAX_CONNECTIONS) {
				// add to list + send confirmation

				String newName = "P" + Integer.toString(clientList.size() + 1);
				int numfound = 0;
				for (ConnectionInfo i : this.clientList) {
					if (i.getAddress().equals(origin)) {
						numfound++;
					}
				}
				port = Globals.CLIENT_PORT + (numfound);
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
		if (data.equals("whatismyip")) {
			master.replyIP(origin, originPort);
		} if (data.equals("init")) {

			ConnectionInfo info = this.getConnectionInfo(origin, originPort);
			master.joinLobby(info.getID(), origin, 2, this.getConnectionInfo(origin, originPort).getDestPort(),
					originPort);
		} else if(parts[0].equals("joinlobby")) {
			int lobbyNum = Integer.parseInt(parts[1]);
			
			
		} else if(parts[0].equals("makelobby")) {			
			master.makeLobby();
			System.out.println("lobby created");
		} else if (data.equals("ready")) {
			// SET LOBBY TO READY
			this.master.setClientReady(origin, originPort);

		} else if (data.equals("quit")) {
			//remove player from game
			master.removePlayer(origin, originPort);
			//remove player from lobby
			master.leaveLobby(origin, originPort);
			//remove from connection table
			this.master.removeConnection(origin, originPort);
			System.out.println("client quit successful");
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
			master.leaveLobby(origin, originPort);
		} else if (data.equals("leavegame")) {
			//quit game and lobby
			master.removePlayer(origin, originPort);
			master.leaveLobby(origin, originPort);
			System.out.println("client left game");
		} else if (data.equals("start")) {
			System.out.println("SERVER STARTING GAME");
			this.master.startGame();
		} else if (data.equals("stop")) {
			this.master.halt();

			// GAME
		} else if (parts[0].equals("input") && this.master.getStatus()==Globals.IN_GAME) {
			//System.out.println(data);
			ArrayList<KeyCode> input = new ArrayList<KeyCode>();
			for (String letter : parts[1].replaceAll("\\[", "").replaceAll(" ", "").replaceAll("\\]", "").split(",")) {
				input.add(KeyCode.getKeyCode(letter));
			}

			double dir = Double.parseDouble(parts[parts.length - 1]) / 1000;
			model.handleInput(input, dir, this.getConnectionInfo(origin, originPort).getID());
			model.update();
		} else if(parts[0].equals("click")) {
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);
			model.shoot( (int) SystemSettings.getDescaledX(x), (int) SystemSettings.getDescaledY(y) , this.getConnectionInfo(origin, originPort).getID());
		}
	}
}