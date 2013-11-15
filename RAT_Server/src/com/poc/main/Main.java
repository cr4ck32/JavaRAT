package com.poc.main;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;
import com.poc.net.ServerManager;

public class Main {

	public static void main(String[] args) throws IOException {
		Server server = new Server();
		server.start();
		server.bind(54555, 54777);
		new ServerManager(server);
	}
	
}
