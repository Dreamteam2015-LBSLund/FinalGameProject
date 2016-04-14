package com.dreamteam.villageTycoon.buildings;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer.Task;
import com.dreamteam.villageTycoon.characters.Inventory;
import com.dreamteam.villageTycoon.characters.SabotageKit;
import com.dreamteam.villageTycoon.characters.SabotageKitType;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.characters.SoldierType;
import com.dreamteam.villageTycoon.characters.WeaponType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.ActivationType;
import com.dreamteam.villageTycoon.characters.SabotageKitType.EffectType;
import com.dreamteam.villageTycoon.characters.WeaponType.Type;
import com.dreamteam.villageTycoon.effects.Debris;
import com.dreamteam.villageTycoon.effects.Explosion;
import com.dreamteam.villageTycoon.framework.Animation;
import com.dreamteam.villageTycoon.framework.GameObject;
import com.dreamteam.villageTycoon.framework.Rectangle;
import com.dreamteam.villageTycoon.framework.Scene;
import com.dreamteam.villageTycoon.map.Prop;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.map.Tile;
import com.dreamteam.villageTycoon.projectiles.Projectile;
import com.dreamteam.villageTycoon.projectiles.ProjectileType;
import com.dreamteam.villageTycoon.userInterface.CreateCharacterButton;
import com.dreamteam.villageTycoon.userInterface.DestroyBuildingButton;
import com.dreamteam.villageTycoon.userInterface.ProgressBar;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.GatherTask;
import com.dreamteam.villageTycoon.workers.GetPropTask;
import com.dreamteam.villageTycoon.workers.Worker;
import com.dreamteam.villageTycoon.frameworkTest.TestScene;
import com.dreamteam.villageTycoon.game.GameScene;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.ai.AIController2;
import com.dreamteam.villageTycoon.ai.PlayerController;
import com.dreamteam.villageTycoon.characters.Character;
import com.dreamteam.villageTycoon.characters.Corpse;

public class Building extends GameObject {

	private enum BuildState { Clearing, InProgress, Done };
	private float waitTime;
    private BuildingType type;
    private BuildState buildState;
    private Inventory<Resource> inputInventory, outputInventory;
    private ArrayList<Worker> workers;
    private City city;
    private boolean selected;
    private Animation selectedSign;
    //private ArrayList<Resource> toGather;
    private ArrayList<Prop> toClear;
    private int health;
    
    private Character characterToSpawn;
    private CreateCharacterButton createCharacterButton; 
    
    private DestroyBuildingButton destroyBuildingButton;
    private ProgressBar delayBar;
    
    private BuildingTaskProvider taskProvider;
	private float maxWaitTime;
    
	private final float BASE_CREATE_CHARACTER_DELAY = 10;
    
    private float createCharacterCount;
	
    //  position is tile at lower left corner
    public Building(Vector2 position, BuildingType type, City owner) {
    	super(position.add(new Vector2(.5f, .5f)), new Vector2(4, 3), new Animation(type.getBuildSprite())); // add .5 to position to align properly with tiles
    	owner.addBuilding(this);
    	this.city = owner;
    	inputInventory = new Inventory<Resource>();
    	outputInventory = new Inventory<Resource>();
		this.type = type;
		buildState = BuildState.Clearing;

		workers = new ArrayList<Worker>();
		
		health = type.getHealth();
		
		taskProvider = new BuildingTaskProvider(getConstructionResources(), this);

		selectedSign = new Animation(AssetManager.getTexture("test"), new Vector2(0.3f, 0.3f), new Color(0, 0, 1, 0.5f));
		selectedSign.setSize(this.getSize().x, this.getSize().y);
		
		setWaitTime(type.getBuildTime());
		
		toClear = new ArrayList<Prop>();
		this.setDepthBasedOnPosition();
		this.createCharacterButton = new CreateCharacterButton(new Vector2(0, 0), this);
		if(this.getCity().getController() instanceof PlayerController) this.destroyBuildingButton = new DestroyBuildingButton(new Vector2(300, 320), this);
		
		
		delayBar = new ProgressBar(new Rectangle(new Vector2(0, 0), new Vector2(500, 50)));
    }
    
    public void onAdd(Scene scene) {
    	super.onAdd(scene);
    	setTiles(); // this cant run in the constructor.
    }
    
    private void setWaitTime(float time) {
    	waitTime = time;
    	maxWaitTime = time;
    }
    
    private float getElapsedLeft() {
    	return maxWaitTime - waitTime;
    }
    
    private void setTiles() {
    	Tile[][] tiles = ((GameScene)getScene()).getMap().getTiles();
    	
    	ArrayList<Prop> props = new ArrayList<Prop>();
    	for (GameObject g : getScene().getObjects()) {
			if (g instanceof Prop) {
				props.add((Prop)g);
			}
		}    	
    	
    	for (int x = 0; x < (int)(getSize().x / Tile.WIDTH); x++) {
    		for (int y = 0; y < (int)(getSize().y / Tile.HEIGHT); y++) {
    			Tile t = tiles[x + (int)(getPosition().x / Tile.WIDTH)][y + (int)(getPosition().y / Tile.HEIGHT)];
    			t.build(this);

				for (Prop p : props) if (p.getTile() == t) {
					if (!toClear.contains(p)) toClear.add(p);
					Debug.print(this, "prop to clear added at tile " + p.getTile().getPosition());
				}
    		}
    	}
    }
    
    private void unsetTiles() {
    	Tile[][] tiles = ((GameScene)getScene()).getMap().getTiles();
    	for (int x = 0; x < (int)(getSize().x / Tile.WIDTH); x++) {
    		for (int y = 0; y < (int)(getSize().y / Tile.HEIGHT); y++) {
    			Tile t = tiles[x + (int)(getPosition().x / Tile.WIDTH)][y + (int)(getPosition().y / Tile.HEIGHT)];
    			t.unBuild();
    		}
    	}
    }
    
    public void setCity(City c) {
    	city.removeBuilding(this);
    	city = c;
    	city.addBuilding(this);
    }
    
    public boolean isBuilt() {
    	return buildState == BuildState.Done;
    }
    
    public Character trySpawn() {
    	if (createCharacterCount <= 0) {
    		return spawnCharacter();
    	} else return null;
    }
    
    private Character spawnCharacter() {
		createCharacterCount = BASE_CREATE_CHARACTER_DELAY;  
		return spawn();
    }
    
    private float getCharacterCountdownRate() {
    	return MathUtils.clamp(getWorkers().size(), 0.5f, getType().getMaxWorkers() + 0.5f) * 1;
    }
    
    public void update(float deltaTime) {
    	// TODO: instant build on Y press. remove before realease :^)
    	
    	selectedSign.setPosition(this.getPosition().x-0.5f, this.getPosition().y-0.5f);

    	if(health <= 0) { 
    		getScene().removeObject(this);
    		getScene().addObject(new Corpse(this.getPosition(), new Animation(AssetManager.getTexture("hole")), new Vector2(3, 3)));
    		int amount = GameScene.randomInt(5, 10);
    		for(int i = 0; i < amount; i++) {
    			getScene().addObject(new Debris(getPosition().cpy(), new Vector2(GameScene.randomInt(-16, 16), GameScene.randomInt(-16, 16)), GameScene.randomInt(4, 8), "debris"+GameScene.randomInt(1, 3), new Vector2(0.5f, 0.5f)));
    		}
    		getScene().addObject(new Explosion(this.getPosition().cpy(), Explosion.Type.EXPLOSION, 4, 0, 0));
    	}
    	
    	if(type.getType() == BuildingType.Type.Home && this.isBuilt()) {
			// TODO: this is the bug @indietom, #27
			if(getCity().getController() instanceof PlayerController && !((PlayerController)getCity().getController()).getBuildingPlacerNull()) {
				createCharacterButton.update(getScene());
			}
			
			System.out.println(createCharacterCount);
			if(createCharacterCount > 0) {
				System.out.println("delta: " + getCharacterCountdownRate() * deltaTime);
				createCharacterCount -= getCharacterCountdownRate() * deltaTime;
				delayBar.progress = createCharacterCount / BASE_CREATE_CHARACTER_DELAY;
			} 
    	}
    	
    	if(selected && destroyBuildingButton != null) {
    		destroyBuildingButton.update();
    	}

    	for(GameObject g : getScene().getObjects()) {
			if(g instanceof Projectile) {
				if(this.city != ((Projectile)g).getOwner().getCity()) {
					if(g.getHitbox().collision(getHitbox())) {
						onHit(((Projectile)g));
						((Projectile)g).onHit();
					}
				}
			}
    	}
    	// TODO: remove this
    	if (Gdx.input.isKeyJustPressed(Keys.Y)) {
    		inputInventory.add(type.getBuildResourcesArray());
    	}

    	if (buildState == BuildState.Clearing) {
    		if (toClear.size() > 0) assignGetPropTask(toClear);
    		else buildState = BuildState.InProgress;
    	} else if (buildState == BuildState.InProgress) {
    		// check if inventory contains all materials. if so, building is done (plus some work?)
    		if (isBuildingDone()) {
    			if (waitTime <= 0) {
	    			buildState = BuildState.Done;
	    			if (type.isFactory()) startProduction();
	    			inputInventory.remove(type.getBuildResourcesArray());
	    			outputInventory = inputInventory;
	    			inputInventory = new Inventory<Resource>();
	    			setSprite(type.getSprite());
    			} else incrementWaitTime(deltaTime);
    		}else {
    			assignGatherTask();
    		}
    	} else if (type.isFactory()) {
    		// regular production
    		// TODO: what did i break?
    		if (taskProvider.isDone()) { // removed && isProductionGatheringDone() to fix pistolFactory not starting work
    			if (!isProductionGatheringDone()) {
    				startProduction();
    			}
    		}
    		if (isProductionGatheringDone()) {
    			if (waitTime <= 0) {
	    			inputInventory.remove(type.getInputResourcesArray());
	    			outputInventory.add(type.getOutputResourceArray());
	    			startProduction();
    			} else incrementWaitTime(deltaTime);
    		} else {
    			assignGatherTask();
    		}
    	}
    }
    
    private void incrementWaitTime(float deltaTime) {
    	waitTime -= deltaTime * Math.min(type.getMaxWorkers(), workers.size());
    }
    
    public void onHit(Projectile projectile) {
    	health -= projectile.getDamage();
    }
    
    public void cancelGather(Resource r) {
    	//System.out.println("cancel, adding r " + r.getName() + ", " + toGather.size());
    	taskProvider.cancelTask(r);
    }
    
    public void gatherDone(Resource r) {
    	if (!taskProvider.onAdd(r)) outputInventory.add(r, 1);
    	else inputInventory.add(r, 1);
    }
    
    private ArrayList<Resource> getConstructionResources() {
    	return getInputInventoryDifference(type.getBuildResources(), type.getBuildAmount());
    }
    
    private ArrayList<Resource> getProductionResources() {
    	/*for (int i = 0; i < type.getProductionResources().length; i++) {
    		Debug.print(this, type.getProductionResources()[i].getName() + ": " + type.getInputResourceAmount()[i]);
    	}*/
    	return Resource.constructList(type.getProductionResources(), type.getInputResourceAmount());
    }
    
    private ArrayList<Resource> getInputInventoryDifference(Resource[] resources, int[] amounts) {
    	ArrayList<Resource> all = new ArrayList<Resource>();
    	for (int i = 0; i < resources.length; i++) {
    		for (int j = 0; j < amounts[i] - inputInventory.count(resources[i]); j++) {
    			all.add(resources[i]);
    		}
    	}
    	return all;
    }
    
    private void assignGetPropTask(ArrayList<Prop> props) {
    	for (Worker w : workers) {
			if (!w.hasTask()) {
				if (props.size() > 0) {
					w.setTask(new GetPropTask(this, props.remove(0)));
					Debug.print(this, "setting new get prop task, left = " + props.size());
				}
			}
		}
    }
    
    private void assignGatherTask() {
    	for (Worker w : workers) {
			if (!w.hasTask()) {
				GatherTask t = taskProvider.nextTask();
				if (t != null) w.setTask(t);
			}
		}
    }
    
    
    // reset the list of things to gather for production, so workers can get new tasks
    private void startProduction() {
    	if (!taskProvider.isDone()) System.out.println("WARNING: taskprovider reset when not done (Building: 269)"); // TODO: remove print
    	taskProvider.reset(getProductionResources());
    	setWaitTime(type.getProductionTime());
    }
    
    private boolean isProductionGatheringDone() {
    	if (!type.isFactory())  return true;
    	boolean hasAllResources = true;
    	for (int i = 0; i < type.getProductionResources().length; i++) {
    		if (inputInventory.count(type.getProductionResources()[i]) < type.getInputResourceAmount()[i]) {
    			hasAllResources = false;
    			break;
    		}
    	}
    	return hasAllResources;
    }
    
    public boolean addWorker(Worker w) {
    	if (!workers.contains(w)) {
    		workers.add(w);
    		return true;
    	}
    	return false;
    }
    
    public void removeWorker(Worker w) {
    	workers.remove(w);
    }
    
    private Character spawn() {
    	Character c = getCharacterToSpawn();
    	getScene().addObject(c);
    	return c;
    }
    
    public Character getCharacterToSpawn() {
    	if(this.type.getType() == BuildingType.Type.Home) {
			if (!type.getName().equals("house")) {
				return new Soldier(city, new Vector2(this.getPosition().x, this.getPosition().y), Soldier.FIST, 
						new SoldierType(1, 1, 1, 1, new Animation(AssetManager.getTexture("soldier"))), 
						new SabotageKit[]{ 
								new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)), 
								new SabotageKit(new SabotageKitType("motolv coctalil", 1, 1, "firekit", ActivationType.INSTANT, EffectType.FIRE)) 
				});
			} else {
				return new Worker(new Vector2(this.getPosition().x, this.getPosition().y), city);
			}

		}
    	else return null;
    }
    
    public void destroy() {
    	city.removeBuilding(this);
    }
    
    private boolean isBuildingDone() {
    	boolean hasAllResources = true;
    	for (int i = 0; i < type.getBuildResources().length; i++) {
    		if (inputInventory.count(type.getBuildResources()[i]) < type.getBuildAmount()[i]) {
    			hasAllResources = false;
    			break;
    		}
    	}
    	return buildState == BuildState.InProgress && hasAllResources;
    }
    
    public BuildingType getType() {
    	return type;
    }
    
    public void drawUi(SpriteBatch batch) {
    	if (selected || AIController2.drawDebug) {
    		AssetManager.font.draw(batch, city.getName(), getUiScreenCoords().x, getUiScreenCoords().y-20);
    		//inputInventory.drawList(getUiScreenCoords(), batch);
    		outputInventory.drawList(getUiScreenCoords().cpy().add(new Vector2(300, 0)), batch);
    		if (!taskProvider.isDone()) {
    			taskProvider.draw(new Vector2(getUiScreenCoords()), batch);
    		} else {
    			//TODO: make this a proper progress bar
    			AssetManager.font.draw(batch, Math.round(getElapsedLeft()) + "/" + Math.round(maxWaitTime) + "s", getUiScreenCoords().x, getUiScreenCoords().y);
    			delayBar.setPosition(getUiScreenCoords().add(new Vector2(0, -100)));
    			delayBar.hint = "production delay";
    			delayBar.progress = getElapsedLeft() / maxWaitTime;
    			delayBar.draw(batch);
    		}
    		
    		if(destroyBuildingButton != null) destroyBuildingButton.draw(batch);
    		
    		if(type.getType() == BuildingType.Type.Home && this.isBuilt() && selected) {
        		if(getCity().getController() instanceof PlayerController) {
        			if(!((PlayerController)getCity().getController()).getBuildingPlacerNull()) createCharacterButton.draw(batch);
        		}
        		
        		if(createCharacterCount > 0) {
        			delayBar.setPosition(getUiScreenCoords().add(new Vector2(0, -100)));
        			delayBar.draw(batch);
        			AssetManager.font.draw(batch, "SPAWN DELAY: " + Math.round(createCharacterCount), getUiScreenCoords().x, getUiScreenCoords().y-50);
        		}
        	}
    		
    		//AssetManager.smallFont.draw(batch, "time: " + waitTime + ", " + workers.size() + " workers, state = " + buildState + ", toGather = " + taskProvider.getProgress() + (type.isFactory() ? ", prod. res. = " + type.constructProductionResourcesList().size() : ""), getUiScreenCoords().x, getUiScreenCoords().y);
    	}
    }
    
    public void draw(SpriteBatch batch) {
    	if (selected) {
    		selectedSign.draw(batch);
    	}
    	    	
    	super.draw(batch);
    }
    
    public void onRemove() {
    	unsetTiles();
    	city.removeBuilding(this);
    	super.onRemove();
    }
    
    public void setSelected(boolean selected) {
    	this.selected = selected;
    }
    
    public boolean getSelected() {
    	return this.selected;
    }
    
    public CreateCharacterButton getCreateCharacterButton() {
    	return this.createCharacterButton;
    }
    
    public int getHealth() { 
    	return health;
    }

	public Inventory<Resource> getOutputInventory() {
		return outputInventory;
	}
	
	public Inventory<Resource> getInputInventory() {
		return inputInventory;
	}
	
	public City getCity() {
		return city;
	}
	
	public boolean obstructs(Character character) {
		if (character != null) return character.getCity() != city;
		else return true;
	}

	public ArrayList<Worker> getWorkers() {
		return workers;
	}
}
