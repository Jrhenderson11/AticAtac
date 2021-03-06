package com.aticatac.lobby;

import java.io.Serializable;
import java.net.InetAddress;

public class ClientInfo  implements Serializable {

    private final String id;
	private InetAddress address;
	private int desPort;
	private int originPort;
    private boolean ready;
    private int colour;	
   
    public ClientInfo(String id, boolean ready, int colour, InetAddress newAddress, int newDestPort, int newOriginPort) {
        this.id = id;
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
    	System.out.println("unreadied");
        ready = false;
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
 
	public int getColour() {
		return this.colour;
	}
	
	public String getID() {
		return this.id;
	}

    public boolean isReady() {
        return this.ready;
    }
    // TODO: maybe add connection strength and might want to store socket instead of ip

    @Override
    public boolean equals(Object clientInfo) {
    	if (clientInfo==null) return false;
    	return (((ClientInfo) clientInfo).getID().equals(id));
    }
}