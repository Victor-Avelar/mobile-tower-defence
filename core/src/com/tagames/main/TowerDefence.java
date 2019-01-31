package com.tagames.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tagames.main.map.Map;
import com.tagames.main.states.MenuState;

public class TowerDefence extends Game {
	public SpriteBatch batch;

	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;
	public static String TITLE = "TowerDefence";
	public static final float SCALINGFACTOR = 100;




	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MenuState(this));
		this.screen.resize(100,100);
	}


	@Override
	public void render () {
		super.render();
	}

}
