package com.poc.display;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetSocketAddress;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.esotericsoftware.kryonet.Connection;
import com.poc.net.ServerManager;
import com.poc.packet.Packet11SendKeys;
import com.poc.packet.Packet2Disconnect;
import com.poc.packet.Packet3RequestRC;
import com.poc.packet.Packet5Message;
import com.poc.packet.Packet6Url;
import com.poc.packet.Packet7Download;

public class MainDisplay {

	private final JFrame frame = new JFrame("Remote Controller");
	private final JList list;
	private final DefaultListModel listModel;
	private final JButton btnRemoteControl, btnMessage, btnInternet, btnDownload, btnTaskManager, btnSendKeys;
	private final ServerManager serverManager;
	private RCScreenStartup rcScreen;
	private TaskManagerDisplay tmDisplay;
	
	public MainDisplay(ServerManager serverManager) {
		this.serverManager = serverManager;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel label = new JLabel("Connected Clients:");
		btnRemoteControl = new JButton("Remote Control");
		btnMessage = new JButton("Send Message");
		btnInternet = new JButton("Open URL");
		btnDownload = new JButton("Download File");
		btnTaskManager = new JButton("Tasks Manager");
		btnSendKeys = new JButton("Send Keys");
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 150));
		listScroller.setBounds(25, 30, 250, 300);
		label.setBounds(25, 15, 150, 10);
		btnRemoteControl.setBounds(285, 40, 150, 25);
		btnMessage.setBounds(285, 80, 150, 25);
		btnInternet.setBounds(285, 120, 150, 25);
		btnDownload.setBounds(285, 160, 150, 25);
		btnTaskManager.setBounds(285, 200, 150, 25);
		btnSendKeys.setBounds(285, 240, 150, 25);
		addListeners();
		panel.add(listScroller);
		panel.add(btnRemoteControl);
		panel.add(btnMessage);
		panel.add(btnInternet);
		panel.add(btnDownload);
		panel.add(btnTaskManager);
		panel.add(btnSendKeys);
		panel.add(label);
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setSize(450,400);
		frame.setVisible(true);
	}
	
	public void addListeners() {
		btnRemoteControl.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					/*Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
					if (con != null && !controlling) {
						controlling = true;
						serverManager.getWriteManager().writeToConnectionTCP(con, new Packet3RequestRC());
						rcScreen = new RCScreenStartup(serverManager.getClientManager().getDimensions(con), con);     
					}*/
				}
			}
		);
		
		btnMessage.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String message = JOptionPane.showInputDialog(null, "What would you like to send the client?");
						Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
						if (con != null) {
							Packet5Message p5 = new Packet5Message();
							p5.message = message;
							serverManager.getWriteManager().writeToConnectionTCP(con, p5);
						}
					}
				}
			);
		btnSendKeys.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String message = JOptionPane.showInputDialog(null, "What would you like to type?");
						Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
						if (con != null) {
							Packet11SendKeys p11 = new Packet11SendKeys();
							p11.message = message;
							serverManager.getWriteManager().writeToConnectionTCP(con, p11);
						}
					}
				}
			);
		btnInternet.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String url = JOptionPane.showInputDialog(null, "What URL would you like to open?");
						Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
						if (con != null) {
							Packet6Url p6 = new Packet6Url();
							p6.url = url;
							serverManager.getWriteManager().writeToConnectionTCP(con, p6);
						}
					}
				}
			);
		btnDownload.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						String url = JOptionPane.showInputDialog(null, "What URL would you like to download from?");
						String filename = JOptionPane.showInputDialog(null, "Please enter the file name you would like it as.");
						Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
						if (con != null) {
							Packet7Download p7 = new Packet7Download();
							p7.url = url;
							p7.filename = filename;
							serverManager.getWriteManager().writeToConnectionTCP(con, p7);
						}
					}
				}
			);
		btnTaskManager.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent e){
						Connection con = serverManager.getClientManager().getConnectionFromAddress(((String) list.getSelectedValue()).replace("\n", ""));
						if (con != null) {
							tmDisplay = new TaskManagerDisplay(serverManager, con);
						}
					}
				}
			); 
	}
	
	public void updateClients(ArrayList<Connection> connected) {
		listModel.clear();
		for (Connection con : connected)
			listModel.addElement(con.getRemoteAddressTCP() + "\n");
	}
	
	public void updateImage(Image img) {
		rcScreen.updateImage(img);
		System.out.println("Got Image 1");
	}
	
	public TaskManagerDisplay getTaskManagerDisplay() {
		return tmDisplay;
	}
	
}
