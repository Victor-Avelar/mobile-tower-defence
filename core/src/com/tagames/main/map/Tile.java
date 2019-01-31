package com.tagames.main.map;

import com.tagames.main.towers.Tower;

/**
 * Created by DerekThompson on 7/14/17.
 */

public class Tile {

    boolean isBuildable;
    int unitsOnTile;
    Tower towerOnTile;

    public Tile(boolean isBuildable) {
        this.isBuildable = isBuildable;
        this.unitsOnTile = 0;
        this.towerOnTile = null;
    }

    public void setFalse(){
        this.isBuildable = false;
    }

    public void setTrue(){
        this.isBuildable = true;
    }

    public void setTower(Tower tower){
        this.towerOnTile = tower;
    }

    public Tower getTower(){
        return towerOnTile;
    }
}
