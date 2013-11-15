package com.poc.net;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.esotericsoftware.kryonet.Connection;
import com.poc.packet.Packet;
import com.poc.packet.Packet1Connected;
import com.poc.packet.Packet2Disconnect;
import com.poc.packet.Packet4RCImage;
import com.poc.packet.Packet8Tasks;

public class PacketManager {

	private final ServerManager serverManager;
	
	public PacketManager(ServerManager serverManager) {
		this.serverManager = serverManager;
	}
	
	public void handlePacket(Packet packet, Connection connection) {
		if (packet instanceof Packet1Connected) {
			Packet1Connected p = (Packet1Connected) packet;
			System.out.println("Client Connected");
			serverManager.getClientManager().addClient(connection, p.width, p.height);
			serverManager.getMainDisplay().updateClients(serverManager.getClientManager().getConnected());
		} else if (packet instanceof Packet2Disconnect) {
			serverManager.getClientManager().removeClient(connection);
			serverManager.getMainDisplay().updateClients(serverManager.getClientManager().getConnected());
		} else if (packet instanceof Packet4RCImage) {
			Image img = null;
			try {
				img = ImageIO.read(new ByteArrayInputStream(((Packet4RCImage) packet).data));
			} catch (IOException e) {
				e.printStackTrace();
			}
			serverManager.getMainDisplay().updateImage(img);
		} else if (packet instanceof Packet8Tasks) {
			String[] tasks = ((Packet8Tasks) packet).tasks;
			if (serverManager.getMainDisplay().getTaskManagerDisplay() != null)
				serverManager.getMainDisplay().getTaskManagerDisplay().updateTasks(tasks);
		} 
	}
	
}
