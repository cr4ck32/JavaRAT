package com.poc.net;

import java.awt.Point;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;

public class ClientManager {

	private final ArrayList<Connection> connected = new ArrayList<Connection>();
	private final Map<Connection, Point> dimensions = new HashMap<Connection, Point>();
	
	public ClientManager() {
		
	}
	
	public Connection getConnectionFromAddress(String ip) {
		for (Connection con : connected)
			if (con.getRemoteAddressTCP().toString().equalsIgnoreCase(ip))
				return con;
		return null;
	}
	
	public void addClient(Connection con, int width, int height) {
		connected.add(con);
		dimensions.put(con, new Point(width, height));
	}
	
	public void removeClient(Connection con) {
		connected.remove(con);
		dimensions.remove(con);
	}
	
	public ArrayList<Connection> getConnected() {
		return connected;
	}
	
	public Point getDimensions(Connection con) {
		return dimensions.get(con);
	}
	
}
