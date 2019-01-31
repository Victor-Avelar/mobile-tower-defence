package com.tagames.main.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.utils.Array;
import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.tagames.main.Constants;
import com.tagames.main.TowerDefence;
import com.tagames.main.map.Map;
import com.tagames.main.states.PlayState;
import com.tagames.main.utillities.Conversions;
import java.util.ListIterator;

/**
 * Created by DerekThompson on 7/14/17.
 */

public abstract class Npc {
    private Vector2 position;
    private Texture skin;
    private TextureRegion skinRegion;
    private Animation skinAnimation;
    private Map testMap;
    private Vector2 nextPoint;
    private ListIterator path;
    private float speed;
    private double distanceTraveled;
    private boolean reachedEnd;
    private BodyDef bodyDef;
    private Body body;
    private FixtureDef fixtureDef;
    private Fixture fixture;
    private PolygonShape polygonShape;
    public float width;
    public float height;
    private boolean isDead;

    //healthbar stuff
    private int maxHealth;
    private int currentHealth;
    private Sprite healthBar;
    float healthBarSize;

    private Texture deathAnimationTexture;
    private int moneyOnDeath;
    public Animation deathAnimation;
    private boolean isDeathAnimationDone = false;
    private boolean moneyGiven = false;

    private Sound deathSound;

    private float disposeTimer = 0;

    public Npc(Map testMap, float speed, int maxHealth, String skinFile, String deathSkin) {
        buildNpc(testMap, speed, maxHealth, skinFile, deathSkin, 20, 9, 6, 0.5f, "skeleton_death.ogg");
    }

    public Npc(Map testMap, float speed, int maxHealth, String skinFile, String deathSkin,int moneyOnDeath, int skinFrames, int deathAnimationFrames, float animationSpeed, String deathSound) {
        buildNpc(testMap, speed, maxHealth, skinFile, deathSkin, moneyOnDeath, skinFrames, deathAnimationFrames, animationSpeed, deathSound);

    }

    public void buildNpc(Map testMap, float speed, int maxHealth, String skinFile, String deathSkin,int moneyOnDeath, int skinFrames, int deathAnimationframes, float animationSpeed, String deathSoundFile){
        this.testMap = testMap;
        this.reachedEnd = false;
        path = testMap.getPath().listIterator();

        nextPoint = (Vector2)path.next();
        position = new Vector2(nextPoint.x, nextPoint.y);
        this.width = 10;
        this.height = 10;
        this.speed = speed;
        this.isDead = false;

        skin = new Texture(skinFile);
        skinRegion = new TextureRegion(skin, skin.getWidth(), skin.getHeight());
        this.skinAnimation = new Animation(skinRegion, skinFrames, animationSpeed);

        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        healthBarSize = skinAnimation.getWidth()/2;
        healthBar = new Sprite(new Texture("health_bar.png"));
        healthBar.setSize(healthBarSize, healthBar.getHeight());
        float healthBarX = position.x-healthBar.getWidth()/2;
        float healthBarY = position.y+skinAnimation.getHeight();
        healthBar.setPosition(healthBarX, healthBarY);
        healthBar.setColor(Color.GREEN);

        this.moneyOnDeath = moneyOnDeath;

        deathAnimationTexture = new Texture("sprites/" + deathSkin + ".png");
        TextureRegion deathAnimationRegion = new TextureRegion(deathAnimationTexture, deathAnimationTexture.getWidth(), deathAnimationTexture.getHeight());
        deathAnimation = new Animation(deathAnimationRegion, deathAnimationframes, 0.7f, true);

        deathSound = Gdx.audio.newSound(Gdx.files.internal("sounds/"+deathSoundFile));
    }

    public void buildPhysics() {
        PhysicsShapeCache shapeCache = new PhysicsShapeCache("physicsObjects.xml");
        body = shapeCache.createBody("skeleton_wide", PlayState.WORLD, 1 / TowerDefence.SCALINGFACTOR, 1 / TowerDefence.SCALINGFACTOR);
        body.setUserData(this);
        body.setBullet(false);
        body.setType(BodyDef.BodyType.KinematicBody);
        body.setTransform(position.x / TowerDefence.SCALINGFACTOR, position.y / TowerDefence.SCALINGFACTOR, 0);
        Array<Fixture> fixtureList = body.getFixtureList();
        for (int i = 0; i < fixtureList.size; i++) {
            fixtureList.get(i).setUserData(this);
            fixtureList.get(i).getFilterData().categoryBits = Constants.NPC;
            fixtureList.get(i).getFilterData().maskBits = Constants.PROJ;
            fixtureList.get(i).setSensor(false);
        }
    }

    public void update(float dt, SpriteBatch batch) {
        distanceTraveled += dt * speed;
        if (currentHealth <= 0) {
            isDead = true;
        }
        if(Math.abs((double)nextPoint.x - position.x) < 3 && Math.abs((double)nextPoint.y - position.y) < 3) {
            if (path.hasNext()) {
                nextPoint = (Vector2) path.next();
                if (nextPoint.y - position.y > 3) {
                    skinAnimation.setDirection("up");
                } else if (nextPoint.x - width - position.x > 3) {
                    skinAnimation.setDirection("right");
                } else if (nextPoint.x - width - position.x < 3) {
                    skinAnimation.setDirection("left");
                }
            } else {
                reachedEnd = true;
            }
        }
        body.setLinearVelocity(Conversions.velocityToPositionWorld(position, nextPoint, speed));

        //renders health bar
        if(currentHealth < maxHealth){
            healthBar.setPosition(position.x+healthBarSize/2, position.y+skinAnimation.getHeight());
            healthBar.setSize(healthBarSize * ((float)currentHealth/maxHealth), healthBar.getHeight());
            if(currentHealth <= maxHealth/3){
                healthBar.setColor(Color.YELLOW);
            }
            if(currentHealth <= maxHealth/6){
                healthBar.setColor(Color.RED);
            }
            healthBar.draw(batch);
        }
    }
    public void setPosition(Vector2 vec2) {
        position.x = vec2.x;
        position.y = vec2.y;
    }


    public boolean isDead() {
        return isDead;
    }

    public void hit(double dmg) {
        currentHealth -= dmg;
    }

    public Body getBody() {
        return body;
    }

    public Animation getSkinAnimation() {
        return skinAnimation;
    }

    public TextureRegion getCurrentFrame() {
        return skinAnimation.getFrame();
    }

    public Vector2 getPosition() {
        return position;
    }

    public int getWidth() {
        return skinAnimation.getWidth();
    }


    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public boolean reachedEnd() {
        return reachedEnd;
    }

    public void dispose() {
        skin.dispose();
        healthBar.getTexture().dispose();
        //deathAnimationTexture.dispose(); // black box render issue
        deathSound.dispose();
    }

    public int getMoneyOnDeath(){
        if(!moneyGiven){
            moneyGiven = true;
            return moneyOnDeath;
        } else{
            return 0;
        }
    }

    public void updateDeathAnimation(float delta){
        if(!finishedDeathAnimation()){
            deathAnimation.update(delta);
            if (deathAnimation.getCurrentFrame() >= deathAnimation.getEndFrame() - 1){
                isDeathAnimationDone = true;
            }
        }
    }

    public boolean readyToDispose(float dt){
        if(disposeTimer >= 4){
            return true;
        }
        else {
            disposeTimer += dt;
            return false;
        }
    }

    public boolean finishedDeathAnimation(){
        return isDeathAnimationDone;
    }

    public TextureRegion getDeathAnimationFrame(){
        return deathAnimation.getFrame();
    }

    public void playDeathSound(){
        deathSound.play(.4f);
    }
}