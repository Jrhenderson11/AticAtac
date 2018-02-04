package com.aticatac.networking.main;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import com.aticatac.networking.client.UDPClient;
import com.aticatac.networking.server.UDPServer;

public class NetworkingTest {

	public static void main(String[] args) {
/*
		UDPServer server = new UDPServer();
		InetAddress srvAddress = null;
		try {
			srvAddress = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			System.out.println("server unreachable on this network");
			System.exit(-1);
		}
		UDPClient client = new UDPClient("C1", srvAddress);
		UDPClient client2 = new UDPClient("C2", srvAddress);

		Date timer = new Date();
		long start = timer.getTime();

		server.start();

		client.start();
		client2.start();		

		client.sendData("join");
		client2.sendData("join");
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for (int i = 0; i < 999; i++) {
			client.sendData("ADD:1:1");
			client2.sendData("ADD:1:1");
		}
		client.sendData("stop");
		//client2.sendData("stop");
		
		long end = new Date().getTime();
		System.out.println("TIME:");
		System.out.println(end - start);

		try {
			server.join();
			
			client.halt();
			client.join();
			System.out.println("client 1 joined");
			client2.halt();
			System.out.println("c2 halted");
			client2.join();
			System.out.println("c2 joined");
			
		} catch (InterruptedException e) {
		}
		System.out.println("done");
		// client.close();
*/
	}

}
