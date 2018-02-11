package com.aticatac.networking.main;

import com.aticatac.networking.server.UDPServer;

public class GameTestServer {

	public static void main(String[] args) {
		System.out.println("Starting game server");
		UDPServer server = new UDPServer();
		server.start();
		try {
			server.join();
		} catch (InterruptedException e) {
		}
		System.out.println("Server done");


	}

}
