package com.tagames.main.user_interface;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tagames.main.TowerDefence;
import com.tagames.main.states.PlayState;

/**
 * Created by Victor on 7/14/2017.
 */

public class TowerSelectionHUD {
    private Skin skin;
    public static Stage stage;
    public static Stage stage2;
    private TextureAtlas region;
    private PlayState playState;
    private Label moneyLabel, tower1Cost, tower2Cost, tower3Cost, tower4Cost;
    private int currentMoney;

    private boolean hidden = false;

    public TowerSelectionHUD(final PlayState playState) {
        this.playState = playState;
        skin = new Skin(Gdx.files.internal("hud/towers.json"));
        stage = new Stage(new ScreenViewport());
        stage2 = new Stage(new ScreenViewport());
        region = new TextureAtlas(Gdx.files.internal("hud/towers.atlas"));
        skin.addRegions(region);

        final Button tower1Button = new Button(skin, "tower1-style");
        final Button tower2Button = new Button(skin, "tower2-style");
        final Button tower3Button = new Button(skin, "tower3-style");
        final Button tower4Button = new Button(skin, "default");

        final Button closeButton = new Button(skin, "default");
        closeButton.setPosition(TowerDefence.WIDTH - closeButton.getWidth(), 0);

        Table table = new Table();
        table.setHeight(96);
        table.setWidth(196);
        table.setPosition(TowerDefence.WIDTH/2 - table.getWidth()/2, 0);
        table.setBackground(new TextureRegionDrawable(getTextureRegion(("table_background.png"))));

        closeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hidden = !hidden;
                return true;
            }
        });

        tower1Button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addTower(1);
                return true;
            }
        });

        tower2Button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addTower(2);
                return true;
            }
        });

        tower3Button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addTower(3);
                return true;
            }
        });

        tower4Button.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                addTower(4);
                return true;
            }
        });

        tower1Cost = new Label(String.valueOf(playState.towerTypes.get(1).getCost()), new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));
        tower2Cost = new Label(String.valueOf(playState.towerTypes.get(2).getCost()), new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));
        tower3Cost = new Label(String.valueOf(playState.towerTypes.get(3).getCost()), new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));
        tower4Cost = new Label(String.valueOf(playState.towerTypes.get(4).getCost()), new Label.LabelStyle(new BitmapFont(), Color.DARK_GRAY));

        table.add(tower1Cost);
        table.add(tower2Cost);
        table.add(tower3Cost);
        table.add(tower4Cost);
        table.row();
        table.add(tower1Button).padRight(1).padLeft(1);
        table.add(tower2Button).padRight(1);
        table.add(tower3Button).padRight(1);
        table.add(tower4Button).padRight(1);


//        button.addListener(new ClickListener(){
//
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                dialog.show(stage);
//                Timer.schedule(new Timer.Task(){
//                    @Override
//                    public void run(){
//                        dialog.hide();
//                    }
//                }, 5);
//            }
//        });

        currentMoney = playState.startMoney;
        moneyLabel = new Label("Money: "+String.format("%06d", playState.startMoney), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        stage.addActor(table);
        stage2.addActor(closeButton);
        stage2.addActor(moneyLabel);
    }

    private void addTower(int towerNumber){
        if(fundsAvailable(towerNumber)){
            playState.addTower(towerNumber);
        }
        else{
            playState.playErrorSound();
        }
    }

    private boolean fundsAvailable(int towerType){
        if(playState.currentMoney < playState.towerTypes.get(towerType).getCost()){
            return false;
        } else{
            return true;
        }
    }

    public void updateMoney(int currentMoney){
        moneyLabel.setText("Money: "+String.format("%06d", currentMoney));
    }

    public void show(){
        if(!hidden){
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
        stage2.act(Gdx.graphics.getDeltaTime());
        stage2.draw();
    }

    //public void upateCamera

    private TextureRegion getTextureRegion(String fname) {
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new TextureRegion(t);
    }
}
