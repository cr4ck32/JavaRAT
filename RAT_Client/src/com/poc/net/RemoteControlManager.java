package com.poc.net;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;

import javax.imageio.ImageIO;

import com.poc.packet.Packet4RCImage;

public class RemoteControlManager {

	private final ClientManager clientManager;
	private boolean controlling = false;
	private Robot robot;
	private final Point dim;
	
	public RemoteControlManager(ClientManager clientManager) {
		this.clientManager = clientManager;
		Toolkit toolkit =  Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		dim = new Point(d.width, d.height);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isControlling() {
		return controlling;
	}
	
	public void startRemoteControl() {
		controlling = true;
		System.out.println("Starting");
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						System.out.println("Sent Image");
						BufferedImage img = robot.createScreenCapture(new Rectangle(0, 0, dim.x, dim.y));
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						ImageIO.write(img, "jpg", baos);
						byte[] data = baos.toByteArray();
						Packet4RCImage p4 = new Packet4RCImage();
						p4.data = data;
						clientManager.getWriteManager().writeToServerUDP(p4);
						Thread.sleep(2000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		});
		t.start();
	}
	
	public void stopRemoteControl() {
		controlling = false;
	}
	
}
