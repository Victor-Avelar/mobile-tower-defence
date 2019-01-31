package com.tagames.main.sprites;

import com.tagames.main.towers.Tower;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by DerekThompson on 7/19/17.
 */

//testing
public class TowerManager {
    private ArrayList<Tower> towers;
    private NpcManager npcManager;
    private ProjectileManager projectileManager;

    public TowerManager(NpcManager npcManager, ProjectileManager projectileManager) {
        this.towers = new ArrayList<Tower>();
        this.npcManager = npcManager;
        this.projectileManager = projectileManager;
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public void update(float dt) {
        for (int i = 0; i < towers.size(); i++) {
            com.tagames.main.towers.Tower tower = towers.get(i);
            if (tower.isEngaged()) {
                if (Math.sqrt(Math.pow(tower.getPosition().x - tower.getEngagedNpc().getPosition().x, 2) + Math.pow(tower.getPosition().y - tower.getEngagedNpc().getPosition().y, 2)) > tower.getRange()
                        || tower.getEngagedNpc().isDead()) {
                    tower.setEngaged(false, null);
                } else {
                    tower.updateRotation(0, tower.getEngagedNpc().getPosition().x, tower.getEngagedNpc().getPosition().y);
                    if (tower.getTimeSinceLastShot() > tower.getReloadTime()) {
                        projectileManager.addProjectile(tower, tower.getEngagedNpc());
                        tower.setTimeSinceLastShot();
                    }
                }
            } else if (!tower.creating) {
                checkNpcRange(towers.get(i));
            }
        }
    }
    public void checkNpcRange(com.tagames.main.towers.Tower tower) {
        if (npcManager.isWaveActive()) {
            HashSet<Npc> npcs = npcManager.getNpcs();
            double closestNpc = 0;
            Iterator<Npc> npcIterator = npcs.iterator();
            while (npcIterator.hasNext()) {
                Npc hold = npcIterator.next();
                double rangeCheck = Math.sqrt(Math.pow(tower.getPosition().x + tower.getTowerTexture().getWidth()
                        - hold.getPosition().x, 2) + Math.pow(tower.getPosition().y + tower.getTowerTexture().getHeight()
                        - hold.getPosition().y, 2));
                if (tower.getRange() >= rangeCheck && hold.getDistanceTraveled() > closestNpc) {
                    closestNpc = hold.getDistanceTraveled();
                    tower.setEngaged(true, hold);
                    tower.updateRotation(0, hold.getPosition().x, hold.getPosition().y);
                }
            }
        }
    }

    public ArrayList<com.tagames.main.towers.Tower> getTowers() {
        return towers;
    }

    public void dispose() {
        for (com.tagames.main.towers.Tower tower: towers) {
            tower.dispose();
        }
    }
}
