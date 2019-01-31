package com.tagames.main.utillities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Victor on 8/7/2017.
 */

public class SoundHandler {
    public Sound placementSound;
    public Sound insufficientFunds;
    public Sound towerUpgrade;
    public Sound sellTower;

    public SoundHandler(){
        placementSound = Gdx.audio.newSound(Gdx.files.internal("sounds/menu_select.mp3"));
        insufficientFunds = Gdx.audio.newSound(Gdx.files.internal("sounds/metal_small.ogg"));
        towerUpgrade = Gdx.audio.newSound(Gdx.files.internal("sounds/tower_upgrade.mp3"));
        sellTower = Gdx.audio.newSound(Gdx.files.internal("sounds/coin.ogg"));
    }

}
