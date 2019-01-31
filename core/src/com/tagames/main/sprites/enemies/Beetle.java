package com.tagames.main.sprites.enemies;

import com.tagames.main.map.Map;
import com.tagames.main.sprites.Npc;

/**
 * Created by Victor on 8/11/2017.
 */

public class Beetle extends Npc {

    public Beetle(Map map) {
        super(map, 20f, 100, "sprites/beetl_walk2.png", "goblin_death", 100, 4, 5, .1f, "golem_death.wav");
        getSkinAnimation().setDefaultDirections(1, 0, 3, 2);
    }
}
