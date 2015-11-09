house1: {
	type: home;
	
	materials: wood, stone;
	material-amount: 5, 3;
	
	sprite-finished: house;
	sprite-building: houseProgress;
	
	inhabitants: 4;
}

factory1: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: factory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product:         steel;
	product-per-run: 6;
	production-resources:        coal, iron;
	resource-per-run: 1,    5;
}

basicMine: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product:         coal, iron;
	product-per-run: 5;
	production-resources:        wood;
	resource-per-run: 1,    4;
}

explosivePowderMine: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product:         explosivePowder;
	product-per-run: 5;
	production-resources:        wood;
	resource-per-run: 1,    4;
}

diamondPowderMine: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product:         diamond;
	product-per-run: 5;
	production-resources:        wood;
	resource-per-run: 1,    4;
}

basicFarm: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	product:         food;
	product-per-run: 5;
	production-resources:        water;
	resource-per-run: 1,    4;
}

advancedFarm: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	product:         food;
	product-per-run: 15;
	production-resources:        water;
	resource-per-run: 1,    4;
}

wheatFarm: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	product:         wheat;
	product-per-run: 15;
	production-resources:        water;
	resource-per-run: 1,    4;
}

woodShop: {
 	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: woodShop;
	sprite-building: woodShopProgress;
	
	max-workers: 4;
	product:         weapons;
	product-per-run: 3;
	production-resources:        wood;
	resource-per-run: 1,    4;
 }
 
flourMill: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: flourMill;
	sprite-building: flourMillProgress;
	
	max-workers: 4;
	product:         flour;
	product-per-run: 15;
	production-resources:        wheat;
	resource-per-run: 1,    4;
}

bakery: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: backery;
	sprite-building: backeryProgress;
	
	max-workers: 4;
	product:         food;
	product-per-run: 15;
	production-resources:        flour;
	resource-per-run: 1,    4;
}

armyBarack: {
	type: home;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: militaryBarrack;
	sprite-building: militaryBarrackProgress;
	
	inhabitants: 4;
}