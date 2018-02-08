package com.aticatac;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.utils.SystemSettings;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.stage.Stage;

public class Main extends Application{

    public static void main(String[] args) {

        SystemSettings.setScreenHeight(480);
        SystemSettings.setScreenWidth(720);

        Main.launch(args);
    }

	private UDPClient initialiseConnection(UDPClient client) {
		InetAddress srvAddress = null;
		try {
			srvAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("server unreachable on this network");
			System.exit(-1);
		}
		client = new UDPClient("C1", srvAddress);
		Thread th = new Thread(client);
		th.setDaemon(true);
		th.start();

		client.connect();
		//join lobby
		//client.joinLobby();
		//client.setStatus(Globals.IN_LOBBY);
		//client.sendData("start");

		System.out.println("Client started and connected");
		return client;
	}
    
    @Override
    public void start(Stage primaryStage) {
    	UDPClient server = null;
    	server = this.initialiseConnection(server);
   	    primaryStage.setTitle("AticAtac");
        primaryStage.setScene(new MainMenu(new Group(), primaryStage, server));
        primaryStage.show();

    }
}
