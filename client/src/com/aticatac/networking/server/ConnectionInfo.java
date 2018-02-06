package com.aticatac.networking.server;

import java.io.Serializable;
import java.net.InetAddress;

public class ConnectionInfo{
	
	private String name;
	private InetAddress address;
	private int port;
	
	public ConnectionInfo(String newName, InetAddress newAddress, int newPort) {
		this.name = newName;
		this.address = newAddress;
		this.port = newPort;
	}
	
	public String getName() {
		return name;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}
	
}