package com.aticatac;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.sound.SoundManager;
import com.aticatac.ui.mainmenu.MainMenu;
import com.aticatac.utils.SystemSettings;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

    /**
     * Main method - calls the javafx execution method
     * @param args controls if sound is disabled for testing
     */
	UDPClient server;
	public static boolean soundEnabled;
	
    public static void main(String[] args) {
    	soundEnabled = true;
    	if (args.length>0 && args[0].equals("soundoff")) {
    		soundEnabled = false;
    	}
        //SystemSettings.setScreenHeight(480);
        //SystemSettings.setScreenWidth(720);
        Main.launch(args);
    }

    /**
     * Creates a connection between server and client
     * @return Client object
     */
	private UDPClient initialiseConnection() {
		InetAddress srvAddress = null;
		try {//172.22.204.79
			srvAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("server unreachable on this network");
			System.exit(-1);
		}
        UDPClient client = new UDPClient("C1", srvAddress);
		Thread th = new Thread(client);
		th.setDaemon(true);
		th.start();

		client.connect();

		System.out.println("Client started and connected");
		return client;
	}

    /**
     * Starts Main javafx thread. This method is not called directly.
     * @param primaryStage stage for javafx window interaction
     */
    @Override
    public void start(Stage primaryStage) {
    	
    	primaryStage.setResizable(false);
    	SystemSettings.setScreenHeight(480);
        SystemSettings.setScreenWidth(720);
        primaryStage.setWidth(SystemSettings.getScreenWidth());
        primaryStage.setHeight(SystemSettings.getScreenHeight());
    	server = this.initialiseConnection();
   	    primaryStage.setTitle("AticAtac");
        primaryStage.setScene(new MainMenu(new Group(), primaryStage, server));
        primaryStage.show();
        SoundManager m = new SoundManager(soundEnabled);
        
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                server.quit();
            	System.exit(0);
            }

        });  
        primaryStage.heightProperty().addListener((observable, oldValue, newValue) ->
                SystemSettings.setScreenHeight((int) ((double) newValue) - 15));
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) ->
                SystemSettings.setScreenWidth((int) ((double) newValue)));
      
    }
}
