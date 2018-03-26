package com.aticatac.networking.main;

import com.aticatac.networking.server.UDPServer;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameTestServer extends Application{

	
	//starts the server
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage s) {
		System.out.println("Starting game server");
		UDPServer server = new UDPServer();
		//server.call();
		Thread th = new Thread(server);
		th.setDaemon(true);
		th.start();
//		try {
//			server.join();
//		} catch (InterruptedException e) {
//		}
		System.out.println("Server done");


	}

}