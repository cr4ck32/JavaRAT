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
import com.poc.packet.Packet10Kill;
import com.poc.packet.Packet2Disconnect;
import com.poc.packet.Packet3RequestRC;
import com.poc.packet.Packet5Message;
import com.poc.packet.Packet9RefreshTasks;

public class TaskManagerDisplay {

	private final JFrame frame = new JFrame("TaskManager");
	private final JList list;
	private final DefaultListModel listModel;
	private final JButton btnKill, btnRefresh;
	private final ServerManager serverManager;
	private final Connection con;
	
	public TaskManagerDisplay(ServerManager serverManager, Connection con) {
		this.serverManager = serverManager;
		this.con = con;
		frame.setResizable(false);
		JPanel panel = new JPanel();
		panel.setLayout(null);
		JLabel label = new JLabel("Tasks for " + con.getRemoteAddressTCP());
		btnKill = new JButton("Kill Task");
		btnRefresh = new JButton("Refresh");
		
		listModel = new DefaultListModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setVisibleRowCount(-1);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 150));
		listScroller.setBounds(25, 30, 250, 300);
		label.setBounds(25, 15, 150, 10);
		btnKill.setBounds(285, 140, 150, 25);
		btnRefresh.setBounds(285, 180, 150, 25);
		addListeners();
		panel.add(listScroller);
		panel.add(btnKill);
		panel.add(btnRefresh);
		panel.add(label);
		frame.add(panel);
		frame.setLocationRelativeTo(null);
		frame.setSize(450,400);
		frame.setVisible(true);
		refresh();
	}
	
	public void refresh() {
		serverManager.getWriteManager().writeToConnectionTCP(con, new Packet9RefreshTasks());
	}
	
	public void addListeners() {
		btnKill.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					String task = ((String) list.getSelectedValue()).replace("\n", "");
					Packet10Kill p10 = new Packet10Kill();
					p10.task = task;
					serverManager.getWriteManager().writeToConnectionTCP(con, p10);
				}
			}
		); 
		btnRefresh.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e){
					refresh();
				}
			}
		);
	}
	
	public void updateTasks(String[] tasks) {
		listModel.clear();
		for (String s : tasks)
			listModel.addElement(s + "\n");
	}
}
