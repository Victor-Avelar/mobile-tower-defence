package com.tagames.main.sprites;
import com.tagames.main.map.Map;

import java.util.HashSet;
import java.util.Random;


/**
 * Created by DerekThompson on 7/18/17.
 */

public class NpcManager {
    private Wave wave;
    private HashSet<Npc> onMapNpcs;
    private float timer;
    private float unitDelayTrack;
    private String wavesFile;
    private Map map;
    private int currentWave;
    private int unitsOnMap;
    private boolean waveActive;
    private Random rand;
    private double unitDelay;

    public NpcManager(String wavesFile, Map map) {
        this.wavesFile = wavesFile;
        this.map = map;
        this.currentWave = 1;
        this.unitDelayTrack = 2;
        this.wave = new Wave(wavesFile, currentWave, map);
        this.waveActive = false;
        this.unitsOnMap = 0;
        this.rand = new Random(10);
        this.unitDelay = rand.nextDouble();
    }

    public void update(float dt) {
        this.timer += dt;
        this.unitDelayTrack += dt;
        if (timer > 2.0 && !waveActive) {
            this.waveActive = true;
            this.timer = 0;
            this.onMapNpcs = new HashSet<Npc>();
        }
        if (waveActive && unitDelayTrack > unitDelay && !wave.getWaveNpcs().isEmpty()) {
            unitDelayTrack = 0;
            unitDelay = rand.nextDouble();
            wave.getWaveNpcs().peek().buildPhysics();
            onMapNpcs.add(wave.getWaveNpcs().remove());
            unitsOnMap++;
        }
    }

    public HashSet<Npc> getNpcs() {
        return onMapNpcs;
    }

    public int getUnitsOnMap() {
        return unitsOnMap;
    }

    public boolean isWaveActive() {
        return waveActive;
    }

    public void dispose() {
        for (Npc npc : onMapNpcs) {
            npc.dispose();
        }
    }
}
