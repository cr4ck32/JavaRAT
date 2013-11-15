package com.poc.net;

import com.esotericsoftware.kryonet.Connection;

public class WriteManager {

	private final ServerManager serverManager;
	
	public WriteManager(ServerManager serverManager) {
		this.serverManager = serverManager;
	}
	
	public void writeToConnectionTCP(Connection connection, Object object) {
		for (Connection con : serverManager.getServer().getConnections())
			if (con == connection)
				if (con.isConnected())
					con.sendTCP(object);
	}
	
	public void writeToConnectionUDP(Connection connection, Object object) {
		for (Connection con : serverManager.getServer().getConnections())
			if (con == connection)
				if (con.isConnected())
					con.sendUDP(object);
	}
	
}
