package com.tagames.main.towers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.tagames.main.TowerDefence;
import com.tagames.main.sprites.Npc;

/**
 * Created by Victor on 7/11/2017.
 */

public class Tower {
    private Texture base;
    private Texture head;
    private Vector2 position;
    public Sprite spriteBase;
    public Sprite spriteHead;
    private boolean isEngaged;
    private int range;
    private float startAngle = 90;
    public boolean creating;
    private Npc engagedNpc;
    private String towerType;
    private double reloadTime;
    private double timeSinceLastShot;

    private int[] colRow = new int[2];
    public String name;

    private int health;
    private int upgradeCost;
    private int cost;
    private int damage;

    private String projectileSkin;
    private int towerNumber;
    public boolean isUpgradable = true;
    private int tier = 1;
    private ShapeRenderer shapeRenderer;
    private Circle circle;

    public Tower(float x, float y, Boolean creating, int tower, int cost, String name){
        this.creating = creating;
        this.position = new Vector2(x, y);
        base = new Texture("towers/base" + tower + ".png");
        head = new Texture("towers/tower"+tower+"-head.png");
        this.spriteBase = new Sprite(base);
        this.spriteBase.setPosition(x,y);
        this.spriteHead = new Sprite(head);
        this.spriteHead.setPosition(x,y);
        this.isEngaged = false;
        this.range = 100;
        this.reloadTime = .25;
        this.towerType = "cannon";
        this.timeSinceLastShot = 0;

        this.name = ("Tower type " + tower);
        this.health =100;
        this.damage = 100;
        this.upgradeCost = 100;
        this.cost = cost;

        this.towerNumber = tower;
        this.name = name;
        circle = new Circle();
        circle.setRadius(range);
        shapeRenderer = new ShapeRenderer();
    }


    public void update(float dt) {
        timeSinceLastShot += dt;
    }

    public void setEngaged(boolean isEngaged, Npc engagedNpc) {
        this.isEngaged = isEngaged;
        this.engagedNpc = engagedNpc;
    }

    public void updateRotation(float degrees, float trackingX, float trackingY){
        if(!creating){
            spriteHead.setRotation(calculateAngle(position.y, trackingY, position.x, trackingX) - startAngle);
        }
    }

    public float getRotation(){
        return spriteHead.getRotation();
    }

    public void draw(Batch batch){
        //draws where pointer is if creating
        if (creating){
            position.x = (Gdx.input.getX() - spriteBase.getWidth()/2)-240;
            position.y = (((Gdx.input.getY() - TowerDefence.HEIGHT)*-1) - spriteBase.getHeight()/2)-400;
            spriteBase.setPosition(position.x, position.y);
            spriteHead.setPosition(position.x, position.y);
        }
        this.spriteBase.draw(batch);
        this.spriteHead.draw(batch);

    }
    public void drawRange(ShapeRenderer shapeRenderer){

        if(creating){
            float x = Gdx.input.getX();
            float y = ((Gdx.input.getY() - TowerDefence.HEIGHT)*-1);
            circle.setPosition(x, y);
            shapeRenderer.setColor(0,1,0,1);
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
        }
    }

    public Texture getTowerTexture() {
        return base;
    }

    public double getReloadTime() {
        return reloadTime;
    }

    public double getTimeSinceLastShot() {
        return timeSinceLastShot;
    }

    public String getTowerType() {
        return towerType;
    }

    public Npc getEngagedNpc() {
        return engagedNpc;
    }

    public boolean isEngaged() {
        return isEngaged;
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getRange() {
        return range;
    }

    private float calculateAngle(float y1, float y2, float x1, float x2) {
        float angle = 0;
        float y = y2 - y1;
        float x = x2 - x1;

        if (x == 0 || y == 0) {
            if (x == 0 && y > 0) {
                angle = 90;
            } else if (x == 0 && y < 0) {
                angle = 270;
            } else if (y == 0 && x > 0) {
                angle = 0;
            } else if (y == 0 && x < 0) {
                angle = 180;
            }
        } else if (x > 0 && y > 0) {
            angle = (float) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
        } else if (x < 0 && y > 0) {
            angle = (float) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 180;
        } else if (x < 0 && y < 0) {
            angle = (float) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 180;
        } else if (x > 0 && y < 0) {
            angle = (float) Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 360;
        }

        return angle;
    }

    public void dispose(){
        base.dispose();
        head.dispose();
    }

    public void setPosition(int x, int y){
        spriteBase.setPosition(x, y);
        spriteHead.setPosition(x, y);
    }

    public void setTransparent(){
        spriteBase.setAlpha(0.5f);
        spriteHead.setAlpha(0.5f);
    }
    public void resetTransparent(){
        spriteBase.setAlpha(1);
        spriteHead.setAlpha(1);
    }

    public void setTimeSinceLastShot() {
        timeSinceLastShot = 0;
    }

    public void setColRow(int col, int row){
        colRow[0] = col;
        colRow[1] = row;
    }
    public int[] getColRow(){
        return colRow;
    }

    public int getHealth() {
        return health;
    }

    public int getDamage() {
        return damage;
    }

    public String getProjectileSkin(){
        if(towerNumber == 3 || towerNumber == 4){
            towerNumber = 2;
        }
        return "projectile"+towerNumber+".png";
    }

    public int getCost(){
        return this.cost;
    }

    public int getUpgradeCost() {
        return upgradeCost;
    }

    public int getNumber(){
        return this.towerNumber;
    }

    public void upgrade1(){
        if (tier == 1){
            damage *= 2;
            health *=2;
            cost+= upgradeCost;
            spriteBase.setColor(Color.RED);
            upgradeCost *= 2;
            tier++;
        } else if (tier == 2){
            damage *= 2;
            health *=2;
            cost+=upgradeCost;
            spriteBase.setColor(Color.YELLOW);
            upgradeCost *= 2;
            tier++;
        } else if (tier == 3){
            damage *= 2;
            health *=2;
            cost += upgradeCost;
            spriteBase.setColor(Color.BLUE);
            upgradeCost *= 2;
            isUpgradable = false;
        }
    }

    public void upgrade2(){
        if (tier == 1){
            damage *= 10;
            cost+= upgradeCost;
            upgradeCost *= 2;
            tier++;
        } else if (tier == 2){
            damage *= 10;
            cost+=upgradeCost;
            upgradeCost *= 2;
            tier++;
        } else if (tier == 3){
            damage *= 2;
            cost += upgradeCost;
            upgradeCost *= 2;
            isUpgradable = false;
        }
    }

    public void upgrade3(){

    }

    public void upgrade4(){

    }

}
