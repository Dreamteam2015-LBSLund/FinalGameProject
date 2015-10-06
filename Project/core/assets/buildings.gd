house1: {
	type: home;
	
	materials: wood, stone;
	material-amount: 5, 3;
	
	sprite-building: test;
	sprite-finished: test;
	
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