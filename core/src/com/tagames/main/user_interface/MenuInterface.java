package com.tagames.main.user_interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tagames.main.states.MenuState;

/**
 * Created by Victor on 7/15/2017.
 */

public class MenuInterface {
    private Skin skin;
    public static Stage stage;
    private TextureAtlas region;
    private MenuState menuState;
    private int worldTimer = 0;
    private float timeCount = 0;
    private Label countdownLabel;

    public MenuInterface(final MenuState menuState){
        this.menuState = menuState;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());
        final TextButton button = new TextButton("Start Game", skin, "default");
        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        button.setWidth(175);
        button.setHeight(50);
        button.setPosition(150, 500);

        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState.switchToPlayState();
            }
        });

        stage.addActor(button);
        stage.addActor(countdownLabel);
    }

    public void update(float dt){
        timeCount += dt;
        if(timeCount >= 1){
            worldTimer++;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void show(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        skin.dispose();
    }
}
