package com.aticatac.networking.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.BlockingQueue;

import com.aticatac.networking.globals.Globals;

import javafx.concurrent.Task;

public class ClientSender extends Task {

	private String name;
	private byte[] buffer = new byte[256];
	private InetAddress server;
	private DatagramSocket socket;
	private boolean running;
	private BlockingQueue<String> messageQueue;
	private int port;

	/** makes a new sender obj
	 * 
	 * @param newName the name: used for debug messages
	 * @param newServer: UDPClient that controls this thread
	 * @param newQueue: MessageQueue to send from
	 */
	public ClientSender(String newName, InetAddress newServer, BlockingQueue newQueue) {
		this.name = newName;
		this.server = newServer;
		try {
			this.socket = new DatagramSocket();
			this.port = socket.getLocalPort();
		} catch (SocketException e) {
			System.out.println("client sender cannot acquire socket");
			e.printStackTrace();
		}

		this.messageQueue = newQueue;
	}

	/**
	 * JavaFX version of run(), starts a thread
	 */
	@Override
	public Object call() {
		System.out.println("sender started");
		this.running = true;
		String message = "";
		while (running) {
			try {
				message = messageQueue.take();
			} catch (InterruptedException e) {
				break;
			}
			// send message once got
			buffer = message.getBytes();
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, Globals.SERVER_PORT);

			try {
				socket.send(packet);
			} catch (IOException e) {
				System.out.println("error sending from client");
				System.exit(-1);
				e.printStackTrace();
			}
		}
		System.out.println("stopping sender");
		socket.close();
		return new Object();
	}

	/**
	 * stops this thread
	 */
	public void halt() {
		this.running = false;
	}

	/**
	 * Return the port this is sending from (because this is dynamic)
	 * @return The number of the port the sender is sending from
	 */
	public int getPort() {
		return this.port;
	}
}
