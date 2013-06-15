package model;

import application.draw.CityDrawer;

public class CityFactory {

	
	
	public static CityItem createBuilding(String name, String type, int level, BuildingVersionCollection buildingVersionCollection,
			CityDrawer cityDrawer, CityItemCollection cityItemCollection, int maxLoc, int maxMethods) {
		
		if (maxMethods > 0) {
			CityItemDescription cityItemDescription = new CityItemDescription(name, type, level);
			Building building = new Building(cityItemDescription, buildingVersionCollection, cityDrawer, cityItemCollection.getNextIndex());
			cityItemCollection.addBuilding(building, maxLoc, maxMethods);
			return building;

		} else
			return null;		
	}
}
