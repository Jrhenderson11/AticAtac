package com.aticatac.networking.packets;

import java.io.Serializable;

public class Packet implements Serializable{

	public char[] type = new char[4];
	public byte[] data;
	public int len;
	
	public Packet(String newType, byte[] newData) {
		type = newType.substring(0,3).toCharArray();
		len = newData.length;
		data = newData;
	}
	
	public String getType() {
		return new String(type);
	}
	
}
