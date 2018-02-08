package com.aticatac.lobby.utils;

import java.io.Serializable;
import java.net.InetAddress;

import javafx.scene.paint.Color;

public class ClientInfo  implements Serializable {

	private String username;
	private InetAddress address;
	private int desPort;
	private int originPort;
    private boolean ready;
    private int colour;	
  
    public ClientInfo(String username, boolean ready, int colour, InetAddress newAddress, int newDestPort, int newOriginPort) {
        this.username = username;
        this.ready = ready;
        this.colour = colour;
        this.address = newAddress;
		this.desPort = newDestPort;
		this.originPort = newOriginPort;
    }

    public void ready() {
        ready = true;
    }

    public void unready() {
        ready = false;
    }

    public String getUsername() {
    	return this.username;
    }
	
	public InetAddress getAddress() {
		return address;
	}

	public int getDestPort() {
		return desPort;
	}
	
	public int getOriginPort() {
		return originPort;
	}
 
    // TODO: maybe add connection strength and might want to store socket instead of ip

}