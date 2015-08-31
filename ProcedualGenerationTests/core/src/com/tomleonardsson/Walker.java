package com.tomleonardsson;

import java.util.Random;

public class Walker {
	int x, y;
	
	int direction;
	int maxChangeDirectionCount;
	int changeDirectionCount;
	int tile;
	int lifeCount;
	int maxLifeCount;
	
	public boolean destroy;
	
	public Walker(int x, int y, int direction, int maxChangeDirectionCount, int tile, int maxLifeCount){
		this.x = x;
		this.y = y;
		
		this.direction = direction;
		
		this.maxLifeCount = maxLifeCount;
		
		this.maxChangeDirectionCount = maxChangeDirectionCount;
		this.tile = tile;
	}
	
	public void update(){
		if(Game.testWorld.map[x][y] == 2){
			destroy = true;
		}
		
		Game.testWorld.map[x][y] = tile;
		
		Random random = new Random();
		
		lifeCount += 1;
		
		changeDirectionCount += 1;
		if(changeDirectionCount >= maxChangeDirectionCount){
			direction = random.nextInt(4)+1;
			changeDirectionCount = 0;
		}
		
		switch(direction){
		case 1:
			x += 1;
			break;
		case 2:
			y -= 1;
			break;
		case 3:
			x -= 1;
			break;
		case 4:
			y += 1;
			break;
		}
		
		if(x == -1 || x == Game.testWorld.map.length || y == -1 || y == Game.testWorld.map[0].length){
			destroy = true;
		}
		
		if(lifeCount >= maxLifeCount){
			destroy = true;
		}
	}
}
