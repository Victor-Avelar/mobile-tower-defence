package com.tagames.main.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tagames.main.TowerDefence;
import com.tagames.main.map.Map;
import com.tagames.main.sprites.Crystal;
import com.tagames.main.sprites.Npc;
import com.tagames.main.sprites.NpcManager;
import com.tagames.main.sprites.Projectile;
import com.tagames.main.sprites.ProjectileManager;
import com.tagames.main.sprites.TestPlayer;
import com.tagames.main.towers.Tower;
import com.tagames.main.sprites.TowerManager;
import com.tagames.main.towers.Tower1;
import com.tagames.main.towers.Tower2;
import com.tagames.main.towers.Tower3;
import com.tagames.main.towers.Tower4;
import com.tagames.main.user_interface.TowerMenu;
import com.tagames.main.user_interface.TowerSelectionHUD;
import com.tagames.main.utillities.Conversions;

import com.codeandweb.physicseditor.PhysicsShapeCache;
import com.tagames.main.utillities.SoundHandler;

import java.util.ArrayList;

/**
 * Created by Victor on 7/7/2017.
 */


public class PlayState implements Screen, InputProcessor, ContactListener {
    public static final World WORLD = new World(new Vector2(0,0), true);
    private Texture background;
    private TowerDefence game;
    private TestPlayer skeleton;
    private TowerSelectionHUD towerSelector;
    private int creatingTowerIndex = 0;
    private Map map;
    private NpcManager npcManager;
    private TowerManager towerManager;
    private ProjectileManager projectileManager;
    private Sprite gridOverlay;
    private TowerMenu towerMenu;
    private boolean towerMenuIsShowing = false;
    private InputMultiplexer im;

    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera physicsCamera;
    private PhysicsShapeCache physicsBodies;
    private ExtendViewport physicsViewport;
    private OrthographicCamera userCamera;
    private ExtendViewport userViewport;

    private Body body;
    public int startMoney = 3000;
    public String costOfTower1 = "100";
    public int currentMoney = startMoney;
    public float volume = 0.4f;
    private Tower newTower;
    public ArrayList<Tower> towerTypes = new ArrayList<Tower>();
    public SoundHandler soundHandler;
    private ShapeRenderer shapeRenderer;

    private ArrayList<Npc> deadNpcHandler = new ArrayList<Npc>();

    private Crystal crystal;

    public PlayState(TowerDefence game) {
        Tower1 tower1 = new Tower1(0, 0);
        Tower2 tower2 = new Tower2(0, 0);
        Tower3 tower3 = new Tower3(0, 0);
        Tower4 tower4 = new Tower4(0, 0);

        towerTypes.add(tower1);//index 0 will not be used
        towerTypes.add(1, tower1);
        towerTypes.add(2, tower2);
        towerTypes.add(3, tower3);
        towerTypes.add(4, tower4);
        shapeRenderer = new ShapeRenderer();

        this.game = game;
        WORLD.setContactListener(this);
        skeleton = new TestPlayer(300,300);
        towerSelector = new TowerSelectionHUD(this);
        map = new Map("EasyMap");
        npcManager = new NpcManager("wave", map);
        projectileManager = new ProjectileManager(WORLD);
        towerManager = new TowerManager(npcManager, projectileManager);
        gridOverlay = new Sprite(new Texture("grid_overlay.png"));
        gridOverlay.setCenter(0,0);
        gridOverlay.setAlpha(0.2f);

        soundHandler = new SoundHandler();

        crystal = new Crystal(-40, 220);

        im = new InputMultiplexer(TowerSelectionHUD.stage2, TowerSelectionHUD.stage, this);
        Gdx.input.setInputProcessor(im);
        physicsCamera = new OrthographicCamera();
        physicsViewport = new ExtendViewport((float)TowerDefence.WIDTH / TowerDefence.SCALINGFACTOR, (float)TowerDefence.HEIGHT / TowerDefence.SCALINGFACTOR, physicsCamera);
        debugRenderer = new Box2DDebugRenderer();
        userCamera = new OrthographicCamera();
        userViewport = new ExtendViewport(TowerDefence.WIDTH, TowerDefence.HEIGHT, userCamera);
    }


    @Override
    public void show() {
        this.background = new Texture("map.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        this.draw(delta);
        game.batch.end();
        drawTowerRanges();
        towerSelector.show();
        WORLD.step(delta, 6, 2);
        if (towerMenuIsShowing){
            towerMenu.show();
        }
        //debugRenderer.render(WORLD, physicsCamera.combined);
    }

    private void draw(float delta){
        game.batch.draw(background, -background.getWidth() / 2, -background.getHeight() / 2);
        crystal.draw(game.batch, delta);
        if(towerManager.getTowers().size() != 0){
            if(towerManager.getTowers().get(creatingTowerIndex).creating){
                gridOverlay.draw(game.batch);
            }
        }
        this.update(delta);
        drawTowers(delta);
        drawBodies(delta);
        handleDeath(delta);
        //game.batch.draw(skeleton.getCurrentFrame(), skeleton.getPosition().x - skeleton.getWidth()/2, skeleton.getPosition().y);
    }
    public void drawTowerRanges(){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int i = 0; i < towerManager.getTowers().size(); i++) {
            towerManager.getTowers().get(i).drawRange(shapeRenderer);

        }
        shapeRenderer.end();
    }

    private void drawBodies(float delta) {
        Array<Body> bodies = new Array<Body>();
        WORLD.getBodies(bodies);
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            if (b.getUserData().getClass().toString().equalsIgnoreCase(Projectile.class.toString())) {
                Projectile proj = (Projectile)b.getUserData();
                if (Math.sqrt(Math.pow(proj.getPosition().x - proj.getParentTower().getPosition().x, 2) +
                        Math.pow(proj.getPosition().y - proj.getParentTower().getPosition().y, 2)) >= proj.getParentTower().getRange() + 100 || proj.hitTarget()) {
                    bodies.removeIndex(i);
                    proj.dispose();
                    projectileManager.getProjectiles().remove(proj);
                    i--;
                } else {
                    proj.setPosition(Conversions.worldCordToPixelCord(b.getPosition()));
                    proj.draw(game.batch);
                }
            } else if(b.getUserData().getClass().getSuperclass().toString().equalsIgnoreCase(Npc.class.toString())) {
                Npc npc = (Npc)b.getUserData();
                if(npc.isDead() && !deadNpcHandler.contains(npc)){
                    deadNpcHandler.add(npc);
                    npc.playDeathSound();
                    WORLD.destroyBody(b);
                } else if (npc.reachedEnd() || npc.isDead()) {
                    bodies.removeIndex(i);
                    npc.dispose();
                    npcManager.getNpcs().remove(npc);
                    i--;
                } else {
                    npc.update(delta, game.batch);
                    npc.setPosition(b.getPosition().scl(TowerDefence.SCALINGFACTOR));
                    npc.getSkinAnimation().update(delta);
                    game.batch.draw(npc.getCurrentFrame(), npc.getPosition().x, npc.getPosition().y);
                }
            }
        }
    }

    private void handleDeath(float delta){
        if(deadNpcHandler.size() != 0){
            for(Npc npc : deadNpcHandler){
                if(!npc.finishedDeathAnimation()){
                    game.batch.draw(npc.getDeathAnimationFrame(), npc.getPosition().x, npc.getPosition().y);
                    npc.updateDeathAnimation(delta);
                }
                if(npc.finishedDeathAnimation()){
                    addMoney(npc.getMoneyOnDeath());
                }
            }
        }
        for (int i = 0; i < deadNpcHandler.size(); i++) {
            if(deadNpcHandler.get(i).finishedDeathAnimation() && deadNpcHandler.get(i).readyToDispose(delta)){
                deadNpcHandler.get(i).dispose();
                deadNpcHandler.get(i).dispose();
                deadNpcHandler.remove(i);
            }

        }
    }

    private void drawTowers(float delta){
        towerManager.update(delta);
        for(Tower tower: towerManager.getTowers()){
            tower.update(delta);
            tower.draw(game.batch);
        }
    }

    public void addTower(int tower){
        if(tower == 1){
            newTower = new Tower1(Gdx.input.getX(), getY());
        } else if (tower == 2){
            newTower = new Tower2(Gdx.input.getX(), getY());
        } else if (tower ==3){
            newTower = new Tower3(Gdx.input.getX(), getY());
        } else if (tower == 4){
            newTower = new Tower4(Gdx.input.getX(), getY());
        } else{
            System.out.println("tower has not been added to addTower method in playState");
        }
        //removes currently selected tower from towers array if it is not placed before a new tower
        //is selected
        if(towerManager.getTowers().size() != 0){
            if(towerIsBeingCreated()){
                towerManager.getTowers().get(creatingTowerIndex).dispose();
                towerManager.getTowers().remove(creatingTowerIndex);
                towerManager.getTowers().add(newTower);
                creatingTowerIndex = towerManager.getTowers().size() - 1;
            } else{
                towerManager.getTowers().add(newTower);
                creatingTowerIndex = towerManager.getTowers().size() - 1;
            }
        }else{
            towerManager.getTowers().add(newTower);
            creatingTowerIndex = towerManager.getTowers().size() - 1;
        }
    }

    public void removeTower(Tower tower){
        creatingTowerIndex--;
        int[] colRow = towerManager.getTowers().get(towerManager.getTowers().indexOf(tower)).getColRow();
        int col = colRow[0];
        int row = colRow[1];
        map.resetTowerTilesToNull(col, row);
        System.out.println("position x, y " + tower.getPosition().x + " " + tower.getPosition().y);
        towerManager.getTowers().get(towerManager.getTowers().indexOf(tower)).dispose();
        towerManager.getTowers().remove(tower);
        disposeTowerMenu();
    }

    public void addMoney(int money){
        currentMoney += money;
        towerSelector.updateMoney(currentMoney);
    }

    @Override
    public void resize(int width, int height) {
        physicsViewport.update(width, height);
        userViewport.update(width, height);
        game.batch.setProjectionMatrix(userCamera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        game.batch.dispose();
        background.dispose();
        skeleton.dispose();
        npcManager.dispose();
        towerManager.dispose();
    }

    private void update (float delta){
        skeleton.update(delta);
        npcManager.update(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        skeleton.isSlashing = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        skeleton.isSlashing = false;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        crystal.hit();
        System.out.println(Gdx.input.getX() + " " + getY());
        int column = (Gdx.input.getX()) / 16;
        int row = (getY() + 400) / 16;

        handleTowerMenu(column, row);

        if(towerIsBeingCreated()){
            //Handles snap to grid
            if (map.towerIsPlaceable(column, row)) {
                addMoney(-1*towerManager.getTowers().get(creatingTowerIndex).getCost());
                towerManager.getTowers().get(creatingTowerIndex).creating = false;
                soundHandler.placementSound.play();
                map.setTowerTilesToTower(column, row, towerManager.getTowers().get(creatingTowerIndex));
                towerManager.getTowers().get(creatingTowerIndex).setColRow(column, row);
                //This takes care of setting tower position if user clicks on an edge
                towerManager.getTowers().get(creatingTowerIndex).setPosition((map.setTowerX(column)*16-16)-240, (map.setTowerY(row)*16-16)-400);
            } else {
                towerManager.getTowers().get(creatingTowerIndex).setTransparent();
            }
        } else{
            skeleton.setNewPosition(getX(), getY());
        }

        return true;
    }

    private void showTowerMenu(Tower tower, int x, int y){
        if(x >= TowerMenu.WIDTH){
            x-= TowerMenu.WIDTH;
        }
        if(y >= TowerMenu.HEIGHT){
            y-= TowerMenu.HEIGHT;
        }
        towerMenu = new TowerMenu(this,tower, x, y);
        im.addProcessor(0, TowerMenu.stage);
        towerMenuIsShowing = true;
    }
    public void disposeTowerMenu(){
        towerMenuIsShowing = false;
        im.removeProcessor(0);
        towerMenu.dispose();
    }
    private void handleTowerMenu(int column, int row){
        if(!towerIsBeingCreated() && map.getTowerOnTile(column, row)!=null){
            showTowerMenu(map.getTowerOnTile(column, row), Gdx.input.getX(), getY()+400);
        }

        if(!towerIsBeingCreated() && map.getTowerOnTile(column, row)==null && towerMenuIsShowing){
            disposeTowerMenu();
        }
    }

    private int getY(){
        return ((Gdx.input.getY() - TowerDefence.HEIGHT)*-1) - 400;
    }
    private int getX(){
        return Gdx.input.getX() - 240;
    }

    private boolean towerIsBeingCreated(){
        if(towerManager.getTowers().size() != 0){
            return towerManager.getTowers().get(creatingTowerIndex).creating;
        } else{
            return false;
        }
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(towerManager.getTowers().size() != 0){
            towerManager.getTowers().get(creatingTowerIndex).resetTransparent();
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getUserData().getClass().getSuperclass().toString().equalsIgnoreCase("class com.tagames.main.sprites.Npc")
                && contact.getFixtureB().getUserData().getClass().toString().equalsIgnoreCase("class com.tagames.main.sprites.Projectile")) {
            Npc hitNpc = (Npc) contact.getFixtureA().getUserData();
            Projectile proj = (Projectile) contact.getFixtureB().getUserData();
            if (!proj.hitTarget()){
                hitNpc.hit(proj.getDmg());
                proj.setHitTarget(true);
                proj.getParentTower().setEngaged(false, null);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

//    public boolean sufficientMoney(int tower){
//        towerCost
//    }
    public void playErrorSound(){
        soundHandler.insufficientFunds.play(0.4f);
    }
}
