package com.poc.display;

import java.awt.Graphics2D;
import java.awt.Image;

import com.baseball435.jgame.window.Screen;
import com.baseball435.jgame.window.ScreenFactory;
import com.esotericsoftware.kryonet.Connection;

public class RemoteControlScreen extends Screen {

	private Image image;
	private final Connection con;
	
	public RemoteControlScreen(ScreenFactory screenFactory, Connection con) {
		super(screenFactory);
		this.con = con;
	}

	@Override
	public void onCreate() {
		
	}

	@Override
	public void onDraw(Graphics2D g2d) {
		if (image != null)
			g2d.drawImage(image, 0, 0, null);
	}

	@Override
	public void onUpdate() {
		
	}

	public void updateImage(Image image) {
		this.image = image;
	}
	
}
