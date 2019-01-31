package com.tagames.main.sprites.enemies;

import com.tagames.main.map.Map;
import com.tagames.main.sprites.Npc;

/**
 * Created by DerekThompson on 7/19/17.
 */

public class Human extends Npc {

    public Human(Map map) {
        super(map, 100f, 150, "sprites/human.png", "human_death");
    }
}
