package com.tagames.main.map;


import com.tagames.main.towers.Tower;

/**
 * Created by DerekThompson on 7/14/17.
 */

public class Map extends MapGrid{

    private String mapName;
    public int x;
    public int y;

    public Map(String mapName) {
        super(mapName);
        this.mapName = mapName;
    }

    public Tile[][] getMapGrid() {
        return this.mapGrid;
    }

    //checks is 3x3 square around the tile row,column has placeable tiles
    public boolean towerIsPlaceable(int column, int row){
        int[] colRow = checkEdge(column, row);
        column = colRow[0];
        row = colRow[1];
        for (int i = column +1; i >= column -1; i--) {
            for (int j = row-1; j <= row+1; j++) {
                if (!mapGrid[i][j].isBuildable){
                    return false;
                }
            }
        }
        return true;
    }

    public void setTowerTilesToTower(int column, int row, Tower tower){
        int[] colRow = checkEdge(column, row);
        column = colRow[0];
        row = colRow[1];
        for (int i = column +1; i >= column -1; i--) {
            for (int j = row-1; j <= row+1; j++) {
                mapGrid[i][j].isBuildable = false;
                mapGrid[i][j].setTower(tower);
            }
        }
    }
    public void resetTowerTilesToNull(int column, int row){
        int[] colRow = checkEdge(column, row);
        column = colRow[0];
        row = colRow[1];
        for (int i = column +1; i >= column -1; i--) {
            for (int j = row-1; j <= row+1; j++) {
                mapGrid[i][j].setTower(null);
                mapGrid[i][j].isBuildable = true;
            }
        }
    }
    public int[] checkEdge(int column, int row){
        int[] colRow = new int[2];
        if(row == 0){
            row++;
        }
        if(row == 49){
            row--;
        }
        if(column == 0){
            column++;
        }
        if(column == 29){
            column--;
        }
        colRow[0] = column;
        colRow[1] = row;
        return colRow;
    }

    public int setTowerX(int column){
        int[] colRow = checkEdge(column, 0);
        column = colRow[0];
        return column;
    }
    public int setTowerY(int row){
        int[] colRow = checkEdge(0, row);
        row = colRow[1];
        return row;
    }

    public Tower getTowerOnTile(int column, int row){
        return mapGrid[column][row].getTower();
    }

    public void checkTile(int x, int y){
        System.out.println(mapGrid[x][y].isBuildable);
    }

}
