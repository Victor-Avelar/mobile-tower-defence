package com.tagames.main.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Victor on 7/7/2017.
 */

public class TestPlayer {
    private Vector2 position;
    private Vector2 newPosition;

    private Texture walkCycleTexture;
    private Animation walkCycleAnimation;
    private int speed = 100;

    private float pathX;
    private float pathY;
    private float distance;
    private float directionX;
    private float directionY;

    private Texture slashTexture;
    private Animation slashAnimation;
    public boolean isSlashing = false;


    public TestPlayer(float x, float y) {
        this.position = new Vector2(x, y);
        this.newPosition = new Vector2(position.x, position.y);

        this.walkCycleTexture = new Texture("sprites/golem_walk.png");
        TextureRegion walkCycleRegion = new TextureRegion(walkCycleTexture, walkCycleTexture.getWidth(), walkCycleTexture.getHeight());
        this.walkCycleAnimation = new Animation(walkCycleRegion, 7, 0.5f);

        this.slashTexture = new Texture("sprites/golem_attack.png");
        TextureRegion slashTextureRegion = new TextureRegion(slashTexture, slashTexture.getWidth(), slashTexture.getHeight());
        this.slashAnimation = new Animation(slashTextureRegion, 7, 0.9f);
    }

    public void update(float dt) {
        if(isSlashing){
            slashAnimation.update(dt);
        }

        if ((position.x != newPosition.x || position.y != newPosition.y) && !isSlashing) {
            walkCycleAnimation.update(dt);
            position.x += directionX * speed * dt;
            position.y += directionY * speed * dt;

            //this block of code is inefficient
            pathX = newPosition.x - position.x;
            pathY = newPosition.y - position.y;
            distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
            if (Math.floor(distance) <= 0) {
                position.x = newPosition.x;
                position.y = newPosition.y;
                walkCycleAnimation.resetAnimation();
            }
        }

    }

    public TextureRegion getCurrentFrame() {
        if(!isSlashing){
            return walkCycleAnimation.getFrame();
        }
        else{
            return slashAnimation.getFrame();
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return walkCycleAnimation.getWidth();
    }
    public int getHeight(){
        return walkCycleAnimation.getHeight();
    }

    public void dispose() {
        walkCycleTexture.dispose();
    }

    public void setNewPosition(float x, float y) {
        newPosition.x = x;
        newPosition.y = y;
        if (position.x != newPosition.x || position.y != newPosition.y) {
            pathX = newPosition.x - position.x;
            pathY = newPosition.y - position.y;

            distance = (float) Math.sqrt(pathX * pathX + pathY * pathY);
            directionX = pathX / distance;
            directionY = pathY / distance;
        }

        double angle = calculateAngle(position.y, newPosition.y, position.x, newPosition.x);
        setDirection(angle);

    }

    private void setDirection(Double angle){
        String direction;
        if (angle > 45 && angle <= 135) {
            direction = "up";
        } else if (angle > 135 && angle <= 225) {
            direction = "left";
        } else if (angle > 225 && angle <= 315) {
            direction = "down";
        } else {
            direction = "right";
        }
        walkCycleAnimation.setDirection(direction);
        slashAnimation.setDirection(direction);
    }
    private double calculateAngle(double y1, double y2, double x1, double x2) {
        double angle = 0;
        double y = y2 - y1;
        double x = x2 - x1;

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
            angle = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
        } else if (x < 0 && y > 0) {
            angle = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 180;
        } else if (x < 0 && y < 0) {
            angle = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 180;
        } else if (x > 0 && y < 0) {
            angle = Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1))) + 360;
        }

        return angle;
    }

}
