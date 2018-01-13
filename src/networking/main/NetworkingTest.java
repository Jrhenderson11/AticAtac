package networking.main;

import java.util.Date;

import networking.client.UDPClient;
import networking.server.UDPServer;

public class NetworkingTest {

	public static void main(String[] args) {

		UDPServer server = new UDPServer();
		UDPClient client = new UDPClient();
		Date timer = new Date();
		long start = timer.getTime();
		
		server.start();
		client.start();
		
		client.sendData("join");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i =0; i< 999; i++) {
			client.sendData("ADD:1:2");
		}
		client.sendData("stop");
		long end = new Date().getTime();
		System.out.println("TIME:");
		System.out.println(end-start);
		
		
		try {

			server.join();
			client.halt();
			client.join();
		} catch (InterruptedException e) {}
		System.out.println("done");
		//client.close();
		
	}

}
