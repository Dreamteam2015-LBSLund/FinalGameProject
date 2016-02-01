mine: {
	type: factory;
	
	health: 5;
	
	materials:     wood, stone;
	material-amount: 10, 3;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product: iron;
	product-per-run: 3;
	production-resources: wood;
	resource-per-run: 1;
}

farm: {
	type: factory;
	
	health: 5;
	
	materials:     wood, stone;
	material-amount: 6, 2;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	
	product: food;
	product-per-run: 1;
	production-resources: water;
	resource-per-run: 1;
}

woodShop: {
	type: factory;
	
	health: 10;
	
	materials: wood, stone;
	material-amount: 10, 5;
	
	max-workers: 4;
	
	sprite-finished: woodShop;
	sprite-building: woodShopProgress;
	
	product: wooden-sword;
	product-per-run: 1;
	production-resources: wood;
	resource-per-run: 2;
}

advancedFarm: {
	type: factory;
	
	health: 10;
	
	materials:     wood, stone, iron;
	material-amount: 10, 4, 2;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	
	product: food;
	product-per-run: 3;
	production-resources: water;
	resource-per-run: 1;
}

explosivePowderMine: {
	type: factory;
	
	health: 12;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product: explosive-powder;
	product-per-run: 1;
	production-resources: wood;
	resource-per-run: 2;
}

blacksmith: {
	type: factory;
	
	health: 10;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: factory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product: iron-sword;
	product-per-run: 1;
	production-resources: iron;
	resource-per-run: 2;
}

flourMill: {
	type: factory;
	
	health: 10;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: flourMill;
	sprite-building: flourMillProgress;
	
	max-workers: 4;
	product: flour;
	product-per-run: 2;
	production-resources: wheat;
	resource-per-run: 1;
}

bakery: {
	type: factory;
	
	health: 10;
	
	materials:     wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: backery;
	sprite-building: backeryProgress;
	
	max-workers: 4;
	product: food;
	product-per-run: 8;
	production-resources: flour;
	resource-per-run: 4;
}

wheatFarm: {
	type: factory;
	
	health: 10;
	
	materials:     wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 4;
	product: wheat;
	product-per-run: 1;
	production-resources: water;
	resource-per-run: 1;
}

house: {
	type: home;
	
	health: 8;	
	
	materials: wood, stone;
	material-amount: 5, 3;
	
	sprite-finished: house;
	sprite-building: houseProgress;
	
	inhabitants: 4;
}

armyBarack: {
	type: home;
	
	health: 10;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	sprite-finished: militaryBarrack;
	sprite-building: militaryBarrackProgress;
	
	inhabitants: 4;
}










diamondPowderMine?: {
	type: factory;
	
	health: 15;
	
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