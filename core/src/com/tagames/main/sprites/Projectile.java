package com.tagames.main.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tagames.main.TowerDefence;
import com.tagames.main.utillities.Conversions;

/**
 * Created by DerekThompson on 7/20/17.
 */

public class Projectile {

    private com.tagames.main.towers.Tower parentTower;
    private Npc targetNpc;
    private ProjectileManager projectileManager;
    private float speed;
    private double dmg;
    private boolean hitTarget;
    private String projectileType;
    private Texture projectileTexture;
    private Vector2 position;
    private double projectileLife;
    private World world;
    private BodyDef bodyDef;
    private Body body;
    private CircleShape circleShape;
    private FixtureDef fixtureDef;
    private Fixture fixture;

    private Sprite projectileSprite;


    public Projectile(com.tagames.main.towers.Tower parentTower, Npc targetNpc, ProjectileManager projectileManager, World world) {
        this.parentTower = parentTower;
        this.targetNpc = targetNpc;
        this.projectileManager = projectileManager;
        this.world = world;
        this.bodyDef = new BodyDef();
        position = new Vector2(parentTower.getPosition().x + parentTower.getTowerTexture().getWidth()/2,
                parentTower.getPosition().y + parentTower.getTowerTexture().getHeight()/2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Conversions.pixelCordToWorldCord(position));
        this.circleShape = new CircleShape();
        circleShape.setRadius(4f / TowerDefence.SCALINGFACTOR);
        this.body = world.createBody(bodyDef);
        fixtureDef = new FixtureDef();
        createFixture();
        buildProjectile();
        body.setBullet(true);
        body.setUserData(this);
        fixture.setUserData(this);
        body.setActive(true);
        body.setLinearVelocity(Conversions.velocityToPositionWorld(parentTower.getPosition(), targetNpc.getPosition(), speed, 10f, 10f));
    }

    public void createFixture() {
        fixtureDef.shape = circleShape;
        fixtureDef.density = 2f;
        fixtureDef.friction = 0.0f;
        fixtureDef.restitution = .2f;
        fixtureDef.isSensor = true;
        fixture = body.createFixture(fixtureDef);
    }

    public void buildProjectile() {
        if (parentTower.getTowerType().equals("cannon")) {
            speed = 200.0f;
            dmg = getParentTower().getDamage();
            projectileTexture = new Texture(getParentTower().getProjectileSkin());
            projectileSprite = new Sprite(projectileTexture);
            projectileSprite.setRotation(parentTower.getRotation());
        }
    }

    public void setPosition(Vector2 vec2) {
        this.position.x = vec2.x;
        this.position.y = vec2.y;
        projectileSprite.setPosition(this.position.x, this.position.y);
    }

    public void draw(SpriteBatch batch){
        projectileSprite.draw(batch);
    }

    public void update(float dt) {
        projectileLife += dt;
    }

    public double getDmg() {
        return dmg;
    }

    public void setHitTarget(boolean hitTarget) {
        this.hitTarget = hitTarget;
    }

    public boolean hitTarget() {
        return hitTarget;
    }

    public com.tagames.main.towers.Tower getParentTower() {
        return parentTower;
    }

    public Texture getProjectileTexture() {
        return projectileTexture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void dispose(){
        projectileTexture.dispose();
    }

}
