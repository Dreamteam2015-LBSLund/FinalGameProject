house1: {
	type: home;
	
	materials: wood, stone;
	material-amount: 5, 3;
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
	inhabitants: 4;
}

factory1: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
	max-workers: 4;
	product:         steel;
	product-per-run: 5;
	production-resources:        coal, iron;
	resource-per-run: 1,    4;
}

basicMine: {
	type: factory;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
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
	
	sprite-finished: factory1;
	sprite-building: factoryProgress1;
	
	inhabitants: 4;
}