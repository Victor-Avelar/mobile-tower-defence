package com.tagames.main.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.tagames.main.TowerDefence;
import com.tagames.main.user_interface.MenuInterface;

/**
 * Created by Victor on 7/12/2017.
 */

public class MenuState implements Screen, InputProcessor {
    private Texture menuBackground;
    private Music introDrumLoop;
    private TowerDefence game;
    private MenuInterface menuInterface;


    public MenuState(TowerDefence game) {
        this.game = game;
        introDrumLoop = Gdx.audio.newMusic(Gdx.files.internal("sounds/menu_drum_loop.mp3"));
        introDrumLoop.setLooping(false);
        introDrumLoop.setVolume(0.5f);
        menuBackground = new Texture("temp_menu.png");

        this.menuInterface = new MenuInterface(this);
        InputMultiplexer im = new InputMultiplexer(MenuInterface.stage, this);
        Gdx.input.setInputProcessor(im);
    }

    @Override
    public void show() {
        introDrumLoop.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuInterface.update(delta);
        game.batch.begin();
        game.batch.draw(menuBackground, 0, 0);
        game.batch.end();
        menuInterface.show();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        introDrumLoop.dispose();
    }

    public void switchToPlayState(){
        this.dispose();
        menuInterface.dispose();
        menuBackground.dispose();
        introDrumLoop.dispose();
        game.setScreen(new PlayState(game));
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
