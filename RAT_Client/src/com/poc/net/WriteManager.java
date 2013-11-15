package com.poc.net;

public class WriteManager {

	private final ClientManager clientManager;
	
	public WriteManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
	public void writeToServerTCP(Object object) {
		if (clientManager.getClient().isConnected())
			clientManager.getClient().sendTCP(object);
	}
	
	public void writeToServerUDP(Object object) {
		if (clientManager.getClient().isConnected())
			clientManager.getClient().sendUDP(object);
	}
	
}
