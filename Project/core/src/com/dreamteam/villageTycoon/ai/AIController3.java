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
import com.dreamteam.villageTycoon.utils.ResourceReader;
import com.dreamteam.villageTycoon.workers.Worker;

public class AIController3 extends CityController {

	private Command[] script; /* = new Command[] {
		new BuildCommand(BuildingType.getTypes().get("house")),
		new MakeWorkerCommand(10),
		new BuildCommand(BuildingType.getTypes().get("farm")),
		new MakeResourceCommand(Resource.get("food"), 10, null),
		new BuildCommand(BuildingType.getTypes().get("armyBarack")),
		new MakeSoldierCommand(10),
		new MakeWorkerCommand(30),
		new BuildCommand(BuildingType.getTypes().get("mine")),
		new MakeResourceCommand(Resource.get("iron"), 20, null),
		new BuildCommand(BuildingType.getTypes().get("advancedFarm")),
		new MakeResourceCommand(Resource.get("food"), 40, BuildingType.getTypes().get("advancedFarm")),
	};*/
	
	private City targetCity;

	private String text;
	
	public AIController3 (City targetCity) {
		this.targetCity = targetCity;
		String input = Gdx.files.internal("aiScript.gs").readString();
		String[] lines = input.split("\n");
		script = new Command[lines.length];
		Debug.print(this, "parsing script");
		for (int i = 0; i < lines.length; i++) {
			script[i] = parseLine(lines[i]);
		}
		text = "";
	}
	
	private Command parseLine(String l) {
		String[] split = l.split(" ");
		String[] cmds = new String[split.length];
		for (int i = 0; i < split.length; i++) {
			cmds[i] = ResourceReader.removeWhitespace(split[i]);
		}
		if (cmds[0].equals("make")) {
			Debug.print(this, cmds[4]);
			return new MakeResourceCommand(Resource.get(cmds[3]), Integer.parseInt(cmds[1]), Integer.parseInt(cmds[2]), (cmds[4].equals("none") ? null : BuildingType.getTypes().get(cmds[4])));
		} else if (cmds[0].equals("build")) {
			return new BuildCommand(BuildingType.getTypes().get(cmds[1]));
		} else if (cmds[0].equals("makeWorker")) {
			return new MakeWorkerCommand(Integer.parseInt(cmds[1]));
		} else if (cmds[0].equals("makeSoldier")) {
			return new MakeSoldierCommand(Integer.parseInt(cmds[1]));
		} else {
			System.out.println("ERROR: unrecognized ai command " + cmds[0]);
			return null;
		}
	}

	private int current;
	
	public void update(float dt) {
		text = "";
		for (int i = 0; i < script.length; i++) {
			if (script[i].isDone()) {
				//Debug.print(this, i + " is done, continuing");
				text += "\n" + script[i].getInfo();
				continue;
			}
			else {
				//Debug.print(this, "updating " + i);
				script[i].update();
				current = i;
				text += "\n updating " + i + ", " + script[i].getInfo();
				break;
			}
		}
	
		if (Gdx.input.isKeyJustPressed(Keys.F2)) {
			getCity().getScene().getCamera().position.set(new Vector3(getCity().getPosition(), 0));
		}
		
		if (Gdx.input.isKeyJustPressed(Keys.F3)) {
			Debug.print(this, "toggling");
			AIController2.drawDebug = true;
		} else if (Gdx.input.isKeyJustPressed(Keys.F4)) {
			AIController2.drawDebug = false;
		}
	}
	
	public void drawUi(SpriteBatch batch) {
		//AssetManager.font.draw(batch, "cmd " + current + "/" + script.length + " " + script[current].getClass().getSimpleName() + " " + script[current].getInfo(), -800, -400);
		AssetManager.smallFont.draw(batch, text, -800, 400);
	}

	
	class BuildCommand extends Command {
		private BuildingType t;
		private Building b;
		
		public BuildCommand(BuildingType t) {
			this.t = t;
		}
		
		public void update() {
			if (b == null) {
				b = new Building(AIController2.getNextBuildingPosition(getCity()), t, getCity());
				getCity().addBuilding(b, true);
			} if (b.getWorkers().size() < getCity().getWorkers().size()) {
				for (Worker w : getCity().getWorkers()) {
					w.workAt(b);
				}
			}
		}
		
		public boolean isDone() {
			if (b == null || !b.isBuilt()) {
				Building tmp = getCity().getBuildingByType(t);
				if (tmp != null && tmp.isBuilt()) {
					b = tmp;
					return true;
				}
			} else {
				return true;
			}
			return false;
		}
		
		public String getInfo() {
			return t.getName();
		}
	}
	
	public class MakeWorkerCommand extends Command {
		private int n;
		
		public MakeWorkerCommand(int n) {
			this.n = n;
		}
		
		public void update() {
			Building b = getCity().getBuildingByType(BuildingType.getTypes().get("house"));
			if (b != null && b.isBuilt()) {
				b.spawn();
			}
		}
		
		public boolean isDone() {
			Debug.print(this, getCity().getWorkers().size() + "");
			return getCity().getWorkers().size() >= n;
		}
	}
	
	public class MakeResourceCommand extends Command {
		private Resource r;
		private int min, max;
		private Building b;
		private BuildingType bt;
		private boolean active;
		
		public MakeResourceCommand(Resource r, int min, int max, BuildingType bt) {
			this.r = r;
			this.min = min;
			this.max = max;
			this.bt = bt;
			Debug.print(this, "new makeResource, bt = " + bt);
		}
		
		public void update() {
			if (b == null) {
				Debug.print(this, "building is null");
				Debug.print(this, bt == null ? "bt is null" : bt.getName());
				b = getCity().getBuildingByType((bt == null ? BuildingType.factoryProduces(r) : bt), true); // TODO: null somewhere?
			}
			if (b.getWorkers().size() < getCity().getWorkers().size()) {
				for (Worker w : getCity().getWorkers()) {
					w.workAt(b);
				}
			}
			active = true;
		}
		
		public boolean isDone() {
			return isDone(false);
		}
		
		public boolean isDone(boolean peek) {
			int res = getCity().getNoMaterials(r);
			boolean ret = active ? (res >= max) : (res > min);
			Debug.print(this, "have " + res + ", active: " + active); 
			if (!peek) active = false;
			return ret;
		}
		
		public String getInfo() {
			return r.getName() + " max: " + max + ", have " + getCity().getNoMaterials(r) + ", bt = " + bt;
		}
	}
	
	public class MakeSoldierCommand extends Command {
		private int n;
		
		public MakeSoldierCommand(int n) {
			this.n = n;
		}
		
		public void update() {
			Building b = getCity().getBuildingByType(BuildingType.getTypes().get("armyBarack"));
			if (b != null && b.isBuilt()) {
				b.spawn().setPath(targetCity.getPosition());
			}
		}
		
		public boolean isDone() {
			Debug.print(this, getCity().getSoldiers().size() + "");
			return getCity().getSoldiers().size() >= n;
		}
	}
	
	abstract class Command {
		// returns true if completed, otherwise false
		public abstract void update();
		public abstract boolean isDone();
		public String getInfo() { return ""; }
	}
}

