package xmlConversion;

import model.*;
import processing.xml.XMLElement;
import application.draw.CityDrawer;

public class XMLConverterClassItem implements XMLConverter {
	
	public XMLConverterClassItem() {}
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		XMLElement[] versions = folder.getChildren();
		BuildingVersionCollection buildingVersionCollection = new BuildingVersionCollection();
		int maxLoc = 0;
		int maxMethods = 0;
		int lastNum = 0;
		int lastLoc = 0;
		int lastMethods = 0;
		for (XMLElement version : versions) {
			int num = version.getInt("num");
			int buildingHeight = version.getInt("loc");
			int buildingGroundSize = version.getInt("methods");
			buildingVersionCollection.add(num, new BuildingVersion(buildingHeight, buildingGroundSize));
			
			for (int i = lastNum + 1; i < num; i++) 
				buildingVersionCollection.add(i, new BuildingVersion(lastLoc, lastMethods));

			lastNum = num;
			lastLoc = buildingHeight;
			lastMethods = buildingGroundSize;

			if (lastMethods > maxMethods)
				maxMethods = lastMethods;

			if (lastLoc > maxLoc)
				maxLoc = lastLoc;
		}

		return CityFactory.createBuilding(folder.getString("name"), folder.getString("type"), 
	             level, buildingVersionCollection, cityDrawer, cityItemCollection, maxLoc, maxMethods);
	}
}