Dirt: {
	name: Dirt;
	isWalkable: true;
	isBuildable: true;
	sprite: dirtTile;
	props: stone, .2;
	gCost: 1;
}

Grass: {
	name: Grass;
	isWalkable: true;
	isBuildable: true;
	resources: wood, iron, diamond;
	props: tree, .7, stone, .7;
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