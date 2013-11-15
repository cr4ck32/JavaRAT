package com.poc.display;

import java.awt.Image;
import java.awt.Point;

import com.baseball435.jgame.window.JGame;
import com.esotericsoftware.kryonet.Connection;

public class RCScreenStartup {

	private final RemoteControlScreen rcScreen;
	
	public RCScreenStartup(Point dim, Connection con) {
		JGame game = new JGame(dim.x, dim.y);
		rcScreen = new RemoteControlScreen(game.getScreenFactory(), con);
		game.getScreenFactory().showScreen(rcScreen);
	}
	
	public void updateImage(Image img) {
		rcScreen.updateImage(img);
	}
	
}
