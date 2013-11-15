package com.poc.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.poc.display.MainDisplay;
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

public class ServerManager {

	private final Server server;
	private final ListenerManager listenerManager;
	private final WriteManager writeManager;
	private final ClientManager clientManager;
	private final MainDisplay mainDisplay;
	
	public ServerManager(Server server) {
		this.server = server;
		registerClasses();
		listenerManager = new ListenerManager(this);
		writeManager = new WriteManager(this);
		clientManager = new ClientManager();
		server.addListener(listenerManager);
		mainDisplay = new MainDisplay(this);
	}
	
	private void registerClasses() {
		Kryo kryo = server.getKryo();
		
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
	
	public Server getServer() {
		return server;
	}
	
	public ClientManager getClientManager() {
		return clientManager;
	}

	public WriteManager getWriteManager() {
		return writeManager;
	}
	
	public MainDisplay getMainDisplay() {
		return mainDisplay;
	}
	
}
