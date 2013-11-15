package com.poc.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.poc.packet.Packet;

public class ListenerManager extends Listener {

	private final ServerManager serverManager;
	private final PacketManager packetManager;
	
	public ListenerManager(ServerManager serverManager) {
		this.serverManager = serverManager;
		packetManager = new PacketManager(serverManager);
	}
	
	@Override
	public void received (Connection connection, Object object) {
		if (object instanceof Packet) {
			packetManager.handlePacket((Packet) object, connection);
		}
	}

}
