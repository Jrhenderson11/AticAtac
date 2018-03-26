package com.aticatac.networking.server;

import java.net.InetAddress;

public class ConnectionInfo {

	private String id;
	private InetAddress address;
	private int destPort;
	private int originPort;
	
	/***
	 * 
	 * @param newId player Id
	 * @param newAddress their IP
	 * @param newDestPort destination port number
	 * @param newOriginPort source port number
	 */
	
	public ConnectionInfo(String newId, InetAddress newAddress, int newDestPort, int newOriginPort) {
		this.id = newId;
		this.address = newAddress;
		this.destPort = newDestPort;
		this.originPort = newOriginPort;
	} 
	
	/**
	 * 
	 * @return player ID
	 */
	public String getID() {
		return id;
	}

	/**
	 * 
	 * @param newID sets ID
	 */
	public void setID(String newID) {
		this.id = newID;
	}

	/**
	 * 
	 * @return IP address
	 */
	public InetAddress getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address new IP address
	 */
	public void setAddress(InetAddress address) {
		this.address = address;
	}

	/**
	 * 
	 * @return destination port
	 */
	public int getDestPort() {
		return destPort;
	}

	/**
	 * 
	 * @param destPort new destination port to set
	 */
	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}

	/**
	 * 
	 * @return source port number
	 */
	public int getOriginPort() {
		return originPort;
	}

	/**
	 * 
	 * @param originPort sets new source port
	 */
	public void setOriginPort(int originPort) {
		this.originPort = originPort;
	}
	
}