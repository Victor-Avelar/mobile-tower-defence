package com.tagames.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tagames.main.TowerDefence;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = TowerDefence.WIDTH;
		config.height = TowerDefence.HEIGHT;
		config.title = TowerDefence.TITLE;
		new LwjglApplication(new TowerDefence(), config);
	}
}
