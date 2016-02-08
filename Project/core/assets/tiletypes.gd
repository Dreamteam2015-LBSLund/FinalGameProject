Dirt: {
	isWalkable: true;
	isBuildable: true;
	sprite: dirtTile;
	props: stone, .2;
	gCost: 1;
}

Grass: {
	isWalkable: true;
	isBuildable: true;
	resources: wood, iron, diamond;
	props: stone, .1;
	sprite: grassTile;
	gCost: 0.8;
}

treeTrunk: {
	isWalkable: true;
	isBuildable: true;
	resources: wood, iron, diamond;
	props: tree, 1;
	sprite: grassTile;
	gCost: 0.8;
}

Water: {
	isWalkable: false;
	isBuildable: false;
	resources: water;
	sprite: waterTile;
}