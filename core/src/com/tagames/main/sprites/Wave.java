package com.tagames.main.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tagames.main.map.Map;
import com.tagames.main.sprites.enemies.Beetle;
import com.tagames.main.sprites.enemies.Goblin;
import com.tagames.main.sprites.enemies.Golem;
import com.tagames.main.sprites.enemies.Human;
import com.tagames.main.sprites.enemies.Skeleton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by DerekThompson on 7/18/17.
 */

public class Wave {

    private Map map;
    private Queue<Npc> npcs;
    private ArrayList<Npc> builtNpcs;
    private String wavesFile;
    private int waveRequested;
    private int unitsInWave;
    private Random rand;
    private boolean randomWave;

    public Wave(String wavesFile, int waveRequested, Map map) {
        this.wavesFile = wavesFile;
        this.waveRequested = waveRequested;
        this.map = map;
        this.npcs = new LinkedList<Npc>();
        this.builtNpcs = new ArrayList<Npc>();
        this.rand = new Random();
        buildWave();
    }

    public void buildWave() {
        FileHandle handle = Gdx.files.internal("waves/" + wavesFile + waveRequested);
        Scanner read = new Scanner(handle.readString());
        int unitsOfType = 0;
        String unitType = new String();
        randomWave = read.nextBoolean();
        unitsInWave = read.nextInt();
        if (randomWave) {
            unitsOfType = read.nextInt();
            for (int i = 0; i < unitsOfType; i++) {
                builtNpcs.add(new com.tagames.main.sprites.enemies.Skeleton(map));
            }
            for (int i = 0; i < unitsOfType; i++) {
                builtNpcs.add(new com.tagames.main.sprites.enemies.Human(map));
            }
            for (int i = 0; i < unitsInWave; i++) {
                System.out.println(builtNpcs.size());
                npcs.add(builtNpcs.remove(rand.nextInt(builtNpcs.size())));
            }
        } else {
            while (read.hasNext()) {
                unitType = read.next();
                unitsOfType = read.nextInt();
                if (unitType.equals("skeleton")) {
                    for (int i = 0; i < unitsOfType; i++) {
                        npcs.add(new Skeleton(map));
                    }
                } else if (unitType.equals("human")) {
                    for (int i = 0; i <unitsOfType; i++) {
                        npcs.add(new Human(map));
                    }
                } else if (unitType.equals("golem")) {
                    for (int i = 0; i < unitsOfType; i++) {
                        npcs.add(new Golem(map));
                    }
                } else if (unitType.equals("goblin")) {
                    for (int i = 0; i < unitsOfType; i++) {
                        npcs.add(new Goblin(map));
                    }
                } else if (unitType.equals("beetle")) {
                    for (int i = 0; i < unitsOfType; i++) {
                        npcs.add(new Beetle(map));
                    }
                }
            }
        }
    }

    public Queue<Npc> getWaveNpcs() {
        return npcs;
    }

    public int getUnitsInWave() {
        return unitsInWave;
    }

    public int getWaveNumber() {
        return waveRequested;
    }
}
