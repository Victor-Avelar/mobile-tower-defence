package com.tagames.main.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Victor on 8/10/2017.
 */

public class Crystal {
    private TextureRegion textureRegion;
    private Animation animation, glowAnimation;
    private int x, y;
    private boolean glowing = false;
    private int timesHit = 0;
    public Crystal(int x, int y){
        this.x = x;
        this.y = y;
        Texture skin = new Texture(Gdx.files.internal("sprites/crystal.png"));
        TextureRegion skinRegion = new TextureRegion(skin, skin.getWidth(), skin.getHeight());
        this.animation = new Animation(skinRegion, 6, 1f, true);

        Texture skin2 = new Texture(Gdx.files.internal("sprites/crystal_glow.png"));
        TextureRegion skinRegion2 = new TextureRegion(skin2, skin2.getWidth(), skin2.getHeight());
        this.glowAnimation = new Animation(skinRegion2, 6, .1f, true);
    }


    public void draw(SpriteBatch batch, float delta){
        if(timesHit >0){
            glowAnimation.update(delta);
            batch.draw(glowAnimation.getFrame(), x, y);
            if(glowAnimation.getCurrentFrame() >= glowAnimation.getEndFrame()-1){
                timesHit--;
                animation.resetAnimation();
                glowAnimation.resetAnimation();
            }
        } else{
            animation.update(delta);
            //skinAnimation.resetAnimation();
            batch.draw(animation.getFrame(), x, y);
        }

    }
    public void hit(){
        timesHit++;
    }
}
