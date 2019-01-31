package com.tagames.main.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Victor on 7/11/2017.
 */

public class MapGrid {

    protected Tile[][] mapGrid;
    private Scanner read;
    protected String mapName;
    private LinkedList<Vector2> path;

    public MapGrid(String mapName) {
        mapGrid = new Tile[30][50];
        this.mapName = mapName;
        path = new LinkedList<Vector2>();
        buildMap();
    }

    public void buildMap() {
        FileHandle handle = Gdx.files.internal(mapName);
        read = new Scanner(handle.readString());
        while (read.hasNextInt()) {
            path.add(new Vector2(read.nextFloat(), read.nextFloat()));
        }
        read.next();
        while (read.hasNext()) {
            int row = read.nextInt();
            int track = 0;
            int times = read.nextInt();
            for (int i = 0; i < times; i++) {
                boolean hold = read.nextBoolean();
                int repeat = read.nextInt();
                for (int j = 0; j < repeat; j++) {
                    mapGrid[track][row] = new Tile(hold);
                    track++;
                }
            }
        }
    }

    public LinkedList<Vector2> getPath() {
        return path;
    }

    public Tile[][] getMapGrid() {
        return mapGrid;
    }
}

