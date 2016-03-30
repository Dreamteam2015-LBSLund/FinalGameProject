package com.dreamteam.villageTycoon.ai;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.dreamteam.villageTycoon.AssetManager;
import com.dreamteam.villageTycoon.buildings.Building;
import com.dreamteam.villageTycoon.buildings.BuildingType;
import com.dreamteam.villageTycoon.buildings.City;
import com.dreamteam.villageTycoon.characters.Soldier;
import com.dreamteam.villageTycoon.map.Resource;
import com.dreamteam.villageTycoon.utils.Debug;
import com.dreamteam.villageTycoon.workers.Worker;
import com.sun.org.apache.bcel.internal.generic.LASTORE;

public class AIController2 extends CityController {
	
	public static boolean drawDebug;
	
	private BuildingType[] expensiveFoodChain;

	private FSM stateMachine;
	private FSM foodMachine;

	public AIController2(City targetCity) {
		//ArrayList<Resource> r = new ArrayList<Resource>();
		//r.add(Resource.get("flour"));
		stateMachine = new FSM(new AttackState(new DoneState(), targetCity)); //new MakeResourceState(null, r);
		
		expensiveFoodChain = new BuildingType[] {
				BuildingType.getTypes().get("wheatFarm"),
				BuildingType.getTypes().get("flourMill"),
				BuildingType.getTypes().get("bakery")
		};
	
	}

	public void update(float dt) {
		if (foodMachine == null) {
			if (!hasFood()) {
				Debug.print(this, "need food!");
				foodMachine = new FSM(new GetFoodState(new DoneState()));
			} else if (!hasFoodProduction()) {
				Debug.print(this, "need food production!");
				foodMachine = new FSM(new MakeFoodProductionState(new DoneState()));
			}

			stateMachine.update();
		} else {
			foodMachine.update();
			if (foodMachine.isDone()) foodMachine = null;
		}
		
		
		// debug stuff
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			getCity().getScene().getCamera().position.set(new Vector3(getCity().getPosition(), 0));
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F3)) {
			Debug.print(this, "toggling");
			drawDebug = true;
		} else if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			drawDebug = false;
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		if (!drawDebug) return;
		String s = "";
		if (foodMachine != null) {
			s = "f; state: " + foodMachine.getState().getClass().getSimpleName() + " " + foodMachine.getState().getInfo();
		} else {
			s = "r; state: " + stateMachine.getState().getClass().getSimpleName() + " " + stateMachine.getState().getInfo();
		} 
		AssetManager.font.draw(batch, s, -400, -400);
	}
	
	public void init() {
		Debug.print(this, getCity().getWorkers().size() + " workers");
	}
	
	private boolean hasFood() {
		int n = getCity().getNoMaterials(Resource.get("food"));
		Debug.print(this, "have " + n + " food");
		return n >= getCity().getWorkers().size() + getCity().getSoldiers().size();
	}
	
	private boolean hasFoodProduction() {
		return hasCheapFoodChain() || hasExpensiveFoodChain();
	}
	
	private boolean hasCheapFoodChain() {
		Building b = getCity().getBuildingByType(BuildingType.getTypes().get("farm"));
		return b != null && b.isBuilt();
	}
	
	private boolean hasExpensiveFoodChain() {
		return hasBuildings(expensiveFoodChain);
	}
	
	private boolean hasBuildings(BuildingType[] types) {
		for (BuildingType t : types) {
			Building b = getCity().getBuildingByType(t);
			if (b == null || !b.isBuilt()) return false;
		}
		return true;
	}
	

	public abstract class State {
		
		protected State prevState;
		
		public State(State previous) {
			this.prevState = previous;
		}

		// returns next state, null if not done
		public abstract State update();
		
		public String getInfo() {
			return "";
		}
	}
	
	public class MakeWeaponsState extends State {

		public MakeWeaponsState(State previous) {
			super(previous);
			// hur ska detta fungera @indietom
		}

		public State update() {
			
			return null;
		}
	}
	
	
	public class GetFoodState extends State {

		public GetFoodState(State prState) {
			super(prState);
		}

		public State update() {
			if (!hasFoodProduction() || (!hasExpensiveFoodChain() && shouldMakeExpensiveFoodChain())) {
				Debug.print(this, "Making food production");
				return new MakeFoodProductionState(this);
			} else {
				if (getCity().getNoMaterials(Resource.get("food")) < getCity().getSoldiers().size() + getCity().getWorkers().size()) {
					ArrayList<Resource> food = new ArrayList<Resource>();
					food.add(Resource.get("food"));
					if (hasExpensiveFoodChain()) {
						Debug.print(this, "working at expensive place");
						return new MakeResourceState(this, food, getCity().getBuildingByType(BuildingType.getTypes().get("bakery")), getCity().getSoldiers().size() + getCity().getWorkers().size());
					} else {
						Debug.print(this, "working at shitty place");
						return new MakeResourceState(this, food);
					}
				} else {
					return prevState;
				}
			}
		}
	}
	
	private boolean shouldMakeExpensiveFoodChain() {
		return true;
	}
	
	public class MakeFoodProductionState extends State {

		public MakeFoodProductionState(State prState) {
			super(prState);
		}

		
		
		public State update() {
			if (shouldMakeExpensiveFoodChain()) {
				Debug.print(this, "making expensive thing");
				for (BuildingType t : expensiveFoodChain) {
					Building b = getCity().getBuildingByType(t);
					if (b == null || !b.isBuilt()) return new MakeFactoryState(this, t);
				}
				Debug.print(this, "done");
				return prevState;
			} else {
				Building b = getCity().getBuildingByType(BuildingType.getTypes().get("farm"));
				Debug.print(this, "making cheap thing");
				if (b == null || !b.isBuilt()) {
					return new MakeFactoryState(this, BuildingType.getTypes().get("farm"));
				}
				Debug.print(this, "done");
				return prevState;
			}
		}
	}
	
	// se https://github.com/Dreamteam2015-LBSLund/Village-Tycoon-RTS/blob/master/Documents/aiStates.md
	
	// send soldiers to attack the player
	public class AttackState extends State {
		
		private City target;
		
		public AttackState(State previous, City target) {
			super(previous);
			this.target = target;
		}

		public State update() {
			if (getCity().getSoldiers().size() == 0) {
				return new MakeSoldierState(this);
			} else {
				for (Soldier s : getCity().getSoldiers()) {
					if (!s.hasPath()) s.setPath(target.getPosition());
					Debug.print(this, "sending soldier");
				}
				return null;
			}
		}
	}
	
	// use soldier factories to make soldiers
	public class MakeSoldierState extends State {
		
		private final int numSoldiers = 5;
		
		public MakeSoldierState(State previous) {
			super(previous);
		}

		public State update() {
			if (getCity().hasBuildingType(BuildingType.getTypes().get("armyBarack"))) {
				// make some soldiers
				Debug.print(this, getCity().getSoldiers().size() + " soldiers");
				if (getCity().getSoldiers().size() >= numSoldiers) {
					return prevState; // done
				} else {
					/*for (Worker w : getCity().getWorkers()) {
						w.workAt(getCity().getBuildingByType(BuildingType.getTypes().get("armyBarack")));
					}*/
					Building b = getCity().getBuildingByType(BuildingType.getTypes().get("armyBarack"));
					if (b != null && b.isBuilt()) {
						Debug.print(this, "spawning soldiers");
						b.spawn();
					} else {
						getCity().addBuilding(new Building(getNextBuildingPosition(getCity()), BuildingType.getTypes().get("armyBarack"), getCity()), true);
					}
					return null;	
				}
			}
			else return new MakeFactoryState(this, BuildingType.getTypes().get("armyBarack"));
		}
		
		public String getInfo() {
			return getCity().getSoldiers().size() + "/" + numSoldiers;
		}
	}
	
	private static float a;
	private static Vector2 lastPos;
	public static Vector2 getNextBuildingPosition(City c) {
		if (lastPos == null) lastPos = c.getPosition();
		a = (float)(a + Math.PI / Math.pow(c.getBuildings().size(), 1/2f));
		Vector2 n = new Vector2(5, 0).setAngleRad(a).add(lastPos);
		lastPos = n;
		return n;
	}
	
	public class MakeFactoryState extends State {
		
		private BuildingType type;
		private Building b;
		
		public MakeFactoryState(State previous, BuildingType type) {
			super(previous);
			this.type = type;
		}

		public State update() {
			if (b == null)  {
				b = getCity().getBuildingByType(type);
				if (b != null) {
					Debug.print(this, "building at " + b.getPosition());
					if (!getCity().getScene().getObjects().contains(b)) {
						getCity().getScene().addObject(b);
					}
				} else {
					
				}
			}
			// if the building is not done
			if (b == null || !b.isBuilt()) {
				ArrayList<Resource> missing = singularize(getCity().missingResources(type.constructBuildResourceList()));
				/*Debug.print(this, "resources for " + type.getName() + ":");
				for (Object r : singularize(type.constructBuildResourceList())) {
					Debug.print(this, ((Resource)r).getName());
				}
				Debug.print(this, "Missing resources for " + type.getName() + ":");
				for (Object r : singularize(missing)) {
					Debug.print(this, ((Resource)r).getName());
				}*/
				// if all resources are available
				if (missing.size() == 0) {
					// if the building is not placed, place it
					Debug.print(this, "no missing resources for building");
					if (b == null) {
						Debug.print(this, "adding building " + type.getName());
						b = new Building(getNextBuildingPosition(getCity()), type, getCity());
						Debug.print(this, "new building at " + b.getPosition());
						getCity().addBuilding(b, true);
					}
					
					// assign workers to it
					for (Worker w : getCity().getWorkers()) {
						 w.workAt(b);
						//Debug.print(this, "assigning worker to " + b.getType().getName());
					}
					
					return null;
				} else {
					// resources aren't available; make factories for them
					return new MakeResourceState(this, missing);
				}
			} else {
				// done
				return prevState;
			}
		}
		
		public String getInfo() {
			return type.getName();
		}
	}
	
	public class MakeResourceState extends State {
		
		private ArrayList<Resource> resources;
		private int target;
		private Building b;
		
		public MakeResourceState(State previous, ArrayList<Resource> missing) {
			this(previous, missing, null, 15);
		}

		public MakeResourceState(State prevState, ArrayList<Resource> missing, Building building, int target) {
			super(prevState);
			this.resources = singularize(missing);
			Debug.print(this, "neeed resources: ");
			for (Resource r : resources) Debug.print(this, r.getName());
			this.b = building;
			this.target = target;
		}

		public State update() {
			if (resources.size() > 0) {
				Resource r = resources.get(0);
				Debug.print(this, "need resource " + r.getName());
				// hanterar bara en fabrik som producerar varje sak. b�r v�ga m�nga mot varandra?
				BuildingType type = BuildingType.factoryProduces(r);
				if (b == null || !b.isBuilt() || !arrayContains(b.getType().getProducts(), r)) b = getCity().getBuildingByType(type);
				// check for not available production resource
				if (b != null && b.isBuilt()) {
					ArrayList<Resource> missing = getCity().missingResources(b.getType().constructProductionResourcesList());
					if (missing.size() == 0) {
						for (Worker w : getCity().getWorkers()) {
							w.workAt(b);
							//Debug.print(this, "assigning worker to " + b.getType().getName());
						}
						int n = getCity().getNoMaterials(r);
						if (n >= target) {
							Debug.print(this, "have " + n + " materials, going to next");
							resources.remove(0);
							return null;
						} else {
							Debug.print(this, "have " + n + " materials, continuing work");
							return null;
						}
					} else {
						return new MakeResourceState(this, missing);
					}
				} else {
					return new MakeFactoryState(this, type);
				}
			} else {
				return prevState;
			}
		}
		
		public String getInfo() {
			String s = "";
			for (Resource r : resources) {
				s += r.getName() + " ";
			}
			return s;
		}
		
		private <T> boolean arrayContains(T[] arr, T thing) {
			for (T t : arr) {
				if (t == thing) return true;
			}
			return false;
		}
		
		private BuildingType getBuildPath(Resource r) {
			ArrayList<BuildingType> types = new ArrayList<BuildingType>();
			for (BuildingType t : BuildingType.getTypes().values()) {
				if (arrayContains(t.getProducts(), r)) {
					types.add(t);
				}
			}
			// (byggnader ing�ende + byggnader som beh�ver byggas) / (resursrate * antal arbetare) + bygga fler arbetare
			return null;
		}
	}
	

	class DoneState extends State {

		public DoneState() {
			super(null);
			// TODO Auto-generated constructor stub
		}
		
		public State update() {
			return null;
		}
		
	}
	
	public static ArrayList singularize(ArrayList l) {
		ArrayList ret = new ArrayList();
		for (Object o : l) {
			if (!ret.contains(o)) {
				ret.add(o);
			}
		}
		return ret;
	}
}

