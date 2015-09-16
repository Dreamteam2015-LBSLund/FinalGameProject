Dirt: {
	name: Dirt;
	isWalkable: true;
	isBuildable: true;
	sprite: dirtTile;
	gCost: 1;
}

Grass: {
	name: Grass;
	isWalkable: true;
	isBuildable: true;
	resources: wood, iron, diamond;
	sprite: grassTile;
	gCost: 0.8;
}

treeTrunk: {
	name: Tree;
	isWalkable: false;
	isBuildable: false;
	resources: tree;
	sprite: treeTrunkTile;
}

Water: {
	name: Water;
	isWalkable: false;
	isBuildable: false;
	resources: water;
	sprite: waterTile;
}