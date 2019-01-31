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
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tagames.main.towers.Tower;
import com.tagames.main.states.PlayState;

/**
 * Created by Victor on 7/19/2017.
 */

public class TowerMenu {
    private Skin skin;
    public static Stage stage;
    private TextureAtlas region;
    private PlayState playState;
    public static int WIDTH = 208;
    public static int HEIGHT = 160;

    private Label towerNameLabel, towerField1, towerField2, towerField3, towerField4, towerField5;
    private Table table;
    private int sellValue;

    public TowerMenu(final PlayState playState, final Tower tower, int x, int y){
        this.playState = playState;
        skin = new Skin(Gdx.files.internal("hud/uimenu.json"));
        stage = new Stage(new ScreenViewport());

        TextButton sellButton = new TextButton("Sell", skin, "default");
        TextButton upgradeButton = new TextButton("Upgrade", skin, "default");

        this.sellValue =(int)Math.floor(tower.getCost()*0.75);
        sellButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                playState.addMoney(sellValue);
                playState.removeTower(tower);
                playState.soundHandler.sellTower.play(playState.volume);
                return true;
            }
        });

        upgradeButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if ((playState.currentMoney > tower.getUpgradeCost()) && tower.isUpgradable){
                    playState.addMoney(tower.getUpgradeCost()*-1);
                    if (tower.getNumber() == 1){
                        tower.upgrade1();
                    } else if(tower.getNumber() == 2){
                        tower.upgrade2();
                    } else if(tower.getNumber() == 3){
                        tower.upgrade3();
                    } else if (tower.getNumber() == 4){
                        tower.upgrade4();
                    }
                    playState.soundHandler.towerUpgrade.play(playState.volume);
                    updateLabels(tower);
                } else{
                    playState.playErrorSound();
                }

                return true;
            }
        });

        upgradeButton.setPosition(x+8, y+7);
        sellButton.setPosition(x+upgradeButton.getWidth()+24, y+7);
        populateLabels(tower);
        towerField1.setPosition(x+16, y+HEIGHT-48);
        towerField2.setPosition(x+16+80, y+HEIGHT-48);
        towerField3.setPosition(x+16, y+HEIGHT-48-20);
        towerField4.setPosition(x+16, y+HEIGHT-48-40);
        towerField5.setPosition(x+16, y+HEIGHT-48-60);

        table = new Table();
        table.setDebug(true);
        table.setHeight(HEIGHT);
        table.setWidth(WIDTH);
        table.setPosition(x, y);
        table.setBackground(new TextureRegionDrawable(getTextureRegion(("hud/panel_blue.png"))));

        Container tittleWrapper = new Container(towerNameLabel);
        tittleWrapper.setTransform(true);
        tittleWrapper.setScale(1.2f,1.2f);
        tittleWrapper.setPosition(x+100, y+table.getHeight()-16);

        stage.addActor(table);
        stage.addActor(tittleWrapper);
        stage.addActor(upgradeButton);
        stage.addActor(sellButton);
        stage.addActor(towerField1);
        stage.addActor(towerField2);
        stage.addActor(towerField3);
        stage.addActor(towerField4);
        stage.addActor(towerField5);
        //stage.addActor()
    }

    private void populateLabels(Tower tower){
        towerNameLabel = new Label(tower.name, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        towerField1 = new Label("HP: " + tower.getHealth(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        towerField1.setAlignment(Align.left);
        towerField2 = new Label("DMG: " + tower.getDamage(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        towerField2.setAlignment(Align.left);
        towerField3 = new Label("Range: " + tower.getRange(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        if(!tower.isUpgradable){
            towerField4 = new Label("Max Upgrade", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        } else{
            towerField4 = new Label("Upgrade Cost: " + tower.getUpgradeCost(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        }
        towerField5 = new Label("Sell Value: " + sellValue, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
    }

    private void updateLabels(Tower tower){
        towerField1.setText("HP: " + tower.getHealth());
        towerField2.setText("DMG: " + tower.getDamage());
        towerField3.setText("Range: " + tower.getRange());
        if(!tower.isUpgradable){
            towerField4.setText("Max Upgrade");
        } else{
            towerField4.setText("Upgrade Cost: " + tower.getUpgradeCost());
        }
        towerField5.setText("Sell Value: " + ((int)Math.floor(tower.getCost()*0.75)));
    }

    public void show(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private TextureRegion getTextureRegion(String fname) {
        final Texture t = new Texture(Gdx.files.internal(fname));
        return new TextureRegion(t);
    }

    public void dispose(){
        skin.dispose();
        stage.dispose();
    }

}
