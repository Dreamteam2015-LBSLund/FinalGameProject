Dirt: {
	name: Dirt;
	isWalkable: true;
	isBuildable: true;
	sprite: dirtTile;
	props: stone, .4;
	gCost: 1;
}

Grass: {
	name: Grass;
	isWalkable: true;
	isBuildable: true;
	resources: wood, iron, diamond;
	props: tree, .3, stone, .1;
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