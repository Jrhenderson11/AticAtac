package com.aticatac.networking.server;

import java.net.InetAddress;

public class ConnectionInfo {

	private String username;
	private InetAddress address;
	private int destPort;
	private int originPort;
	
	public ConnectionInfo(String newName, InetAddress newAddress, int newDestPort, int newOriginPort) {
		this.username = newName;
		this.address = newAddress;
		this.destPort = newDestPort;
		this.originPort = newOriginPort;
	} 
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getDestPort() {
		return destPort;
	}

	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}

	public int getOriginPort() {
		return originPort;
	}

	public void setOriginPort(int originPort) {
		this.originPort = originPort;
	}
	
}