mine: {
	type: factory;
	
	health: 20;
	
	materials:     wood, stone;
	material-amount: 10, 3;
	build-time: 10;
	
	sprite-finished: mine;
	sprite-building: mineProgress;
	
	max-workers: 4;
	product: iron;
	product-per-run: 3;
	production-resources: wood;
	resource-per-run: 1;
	production-time: 5;
}

farm: {
	type: factory;
	build-time: 10;
	
	health: 20;
	
	materials:     wood, stone;
	material-amount: 6, 2;
	
	sprite-finished: farm;
	sprite-building: farmProgress;
	
	max-workers: 8;
	
	product: food;
	product-per-run: 1;
	production-resources: water;
	resource-per-run: 1;
	production-time: 5;
}

woodShop: {
	type: factory;
	build-time: 15;
	
	health: 20;
	
	materials: wood, stone;
	material-amount: 10, 5;
	
	max-workers: 4;
	
	sprite-finished: woodShop;
	sprite-building: woodShopProgress;
	
	product: wooden-sword;
	product-per-run: 1;
	production-resources: wood;
	resource-per-run: 2;
	production-time: 5;
}

advancedFarm: {
	type: factory;
	build-time: 15;
	
	health: 20;
	
	materials:     wood, stone, iron;
	material-amount: 10, 4, 2;
	
	sprite-finished: advancedFarm;
	sprite-building: farmProgress;
	
	max-workers: 12;
	
	product: food;
	product-per-run: 3;
	production-resources: water;
	resource-per-run: 1;
	production-time: 5;
}

blacksmith: {
	type: factory;
	build-time: 15;
	
	health: 20;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: factory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product: iron-sword;
	product-per-run: 1;
	production-resources: iron;
	resource-per-run: 2;
	production-time: 7;
}

gunFactory: {
	type: factory;
	build-time: 20;
	
	health: 20;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: pistolFactory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product: gun;
	product-per-run: 1;
	production-resources: iron;
	resource-per-run: 2;
	production-time: 10;
}

rifleFactory: {
	type: factory;
	
	build-time: 20;
	
	health: 20;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: rifleFactory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product: rifle;
	product-per-run: 1;
	production-resources: iron;
	resource-per-run: 2;
	production-time: 10;
}

machineGunFactory: {
	type: factory;
	build-time: 25;
	
	health: 20;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: machineGunFactory;
	sprite-building: factoryProgress;
	
	max-workers: 4;
	product: machine-gun;
	product-per-run: 1;
	production-resources: iron;
	resource-per-run: 2;
	production-time: 12;
}

flourMill: {
	type: factory;
	build-time: 15;
	
	health: 20;
	
	materials: wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: flourMill;
	sprite-building: flourMillProgress;
	
	max-workers: 4;
	product: flour;
	product-per-run: 2;
	production-resources: wheat;
	resource-per-run: 1;
	production-time: 5;
}

bakery: {
	type: factory;
	
	build-time: 15;
	
	health: 20;
	
	materials:     wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: backery;
	sprite-building: backeryProgress;
	
	max-workers: 4;
	product: food;
	product-per-run: 8;
	production-resources: flour;
	resource-per-run: 4;
	production-time: 5;
}

wheatFarm: {
	type: factory;
	build-time: 15;
	
	health: 20;
	
	materials:     wood, stone, iron;
	material-amount: 10, 5, 3;
	
	sprite-finished: wheatFarm;
	sprite-building: farmProgress;
	
	max-workers: 12;
	product: wheat;
	product-per-run: 1;
	production-resources: water;
	resource-per-run: 1;
	production-time: 7;
}

house: {
	type: home;
	build-time: 10;
	
	health: 20;	
	
	materials: wood, stone;
	material-amount: 5, 3;
	
	max-workers: 4;
	
	sprite-finished: house;
	sprite-building: houseProgress;
	
	inhabitants: 4;
}

armyBarack: {
	type: home;
	build-time: 15;
	
	health: 20;
	
	materials:     wood, stone;
	material-amount: 10, 5;
	
	max-workers: 4;
	
	sprite-finished: militaryBarrack;
	sprite-building: militaryBarrackProgress;
	
	inhabitants: 4;
}