package com.tagames.main.sprites.enemies;

import com.tagames.main.map.Map;
import com.tagames.main.sprites.Npc;

/**
 * Created by Victor on 7/30/2017.
 */

public class Golem extends Npc {
    public Golem(Map map) {
        super(map, 5f, 1000, "sprites/golem_walk.png", "golem_death", 100, 7, 6, 1f, "golem_death.wav");
    }
}
