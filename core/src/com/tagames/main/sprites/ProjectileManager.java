package com.tagames.main.sprites;

import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;

/**
 * Created by DerekThompson on 7/20/17.
 */

public class ProjectileManager {

    private HashSet<Projectile> projectiles;
    private World world;

    public ProjectileManager(World world) {
        projectiles = new HashSet<Projectile>();
        this.world = world;
    }

    public void addProjectile(com.tagames.main.towers.Tower tower, Npc targetNpc) {
        projectiles.add(new Projectile(tower, targetNpc, this, world));
    }

    public HashSet<Projectile> getProjectiles() {
        return projectiles;
    }

}