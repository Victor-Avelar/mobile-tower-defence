package com.tagames.main.sprites.enemies;

import com.tagames.main.map.Map;
import com.tagames.main.sprites.Npc;

/**
 * Created by DerekThompson on 7/19/17.
 */

public class Skeleton extends Npc {

    public Skeleton(Map map) {
        super(map, 10f, 100, "sprites/skeleton_body.png", "skeleton_death");
    }
}
