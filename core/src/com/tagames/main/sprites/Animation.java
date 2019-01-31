package com.tagames.main.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Victor on 7/7/2017.
 */

public class Animation {
    private Array<TextureRegion> frames;
    private float maxFrameTime;
    private float currentFrameTime;
    private int currentFrame;
    private int framesPerRow;
    private int startFrame;
    private int endFrame;
    private int frameWidth;
    private int frameHeight;
    private boolean isDeathAnimation = false;

    private int up = 0;
    private int down = 2;
    private int left = 1;
    private int right = 3;

    public Animation(TextureRegion region, int framesPerRow, float cycleTime){
        this.framesPerRow = framesPerRow;
        frames = new Array<TextureRegion>();
        frameWidth = region.getRegionWidth() / framesPerRow;
        frameHeight = region.getRegionHeight() / 4;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < framesPerRow; i++) {
                frames.add(new TextureRegion(region, i * frameWidth, j * frameHeight, frameWidth, region.getRegionHeight()/4));
            }
        }
        maxFrameTime = cycleTime / framesPerRow;

        setDirection("right");
    }

    public Animation(TextureRegion region, int framesPerRow, float cycleTime, boolean isDeathAnimation){
        this.isDeathAnimation = true;
        this.framesPerRow = framesPerRow;
        frames = new Array<TextureRegion>();
        frameWidth = region.getRegionWidth() / framesPerRow;
        frameHeight = region.getRegionHeight();
        for (int i = 0; i < framesPerRow; i++) {
            frames.add(new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight()));
        }

        maxFrameTime = cycleTime / framesPerRow;

        setDirection("doesn't matter, it's a death animation!");
    }

    public void update(float dt){
        currentFrameTime += dt;

        if(currentFrameTime > maxFrameTime){
            currentFrame++;
            currentFrameTime = 0;
        }
        if (currentFrame >= endFrame){
            currentFrame = startFrame;
        }
    }

    //pass in the row number the direction has, starting from 0. (Ex. if down is row 0 put in 0 for down)
    public void setDefaultDirections(int up, int down, int left, int right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
    }

    public void setDirection(String direction){
        if(!isDeathAnimation){
            if(direction.equals("up")){
                startFrame = up * framesPerRow;
            } else if(direction.equals("down")){
                startFrame = framesPerRow * down;
            } else if(direction.equals("left")){
                startFrame = framesPerRow * left;
            } else if(direction.equals("right")) {
                startFrame = framesPerRow * right;
            }
        } else{
            startFrame = 0;
        }
        currentFrame = startFrame;
        endFrame = currentFrame + framesPerRow;
    }

    public TextureRegion getFrame(){
        return frames.get(currentFrame);
    }
    public int getEndFrame(){
        return endFrame;
    }
    public int getCurrentFrame(){
        return currentFrame;
    }

    public void resetAnimation(){
        currentFrame = startFrame;
    }

    public int getWidth(){
        return this.frameWidth;
    }

    public int getHeight() {return this.frameHeight;}
}
