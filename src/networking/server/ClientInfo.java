package networking.server;

import java.net.InetAddress;

public class ClientInfo {
	
	private String name;
	private InetAddress address;
	private int port;
	
	public ClientInfo(String newName, InetAddress newAddress, int newPort) {
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
