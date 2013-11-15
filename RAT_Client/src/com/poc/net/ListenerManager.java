package com.poc.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.poc.packet.Packet;

public class ListenerManager extends Listener {

	private final ClientManager clientManager;
	private final PacketManager packetManager;
	
	public ListenerManager(ClientManager clientManager) {
		this.clientManager = clientManager;
		packetManager = new PacketManager(clientManager);
	}
	
	@Override
	public void received(Connection connection, Object object) {
		if (clientManager.getClient().isConnected())
			if (object instanceof Packet)
				packetManager.handlePacket((Packet) object);
	}
	
}
