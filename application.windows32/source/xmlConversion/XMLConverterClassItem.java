package xmlConversion;

import model.Building;
import model.CityItem;
import model.CityItemCollection;
import model.ClassVersion;
import model.ClassVersionCollection;
import processing.xml.XMLElement;
import application.SkyscrapAR;
import application.draw.CityDrawer;

public class XMLConverterClassItem implements XMLConverter {
	
	private SkyscrapAR skyscrapAR;

	public XMLConverterClassItem(SkyscrapAR skyscrapAR) {
		this.skyscrapAR = skyscrapAR;
	}
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		XMLElement[] versions = folder.getChildren();
		ClassVersionCollection classVersionCollection = new ClassVersionCollection();
		int maxLoc = 0;
		int maxMethods = 0;
		int lastNum = 0;
		int lastLoc = 0;
		int lastMethods = 0;
		int firstClassVersion = 0;
		for (XMLElement version : versions) {
			int num = version.getInt("num");
			int loc = version.getInt("loc");
			int methods = version.getInt("methods");
			classVersionCollection.add(num, new ClassVersion(loc, methods));
			
			for (int i = lastNum + 1; i < num; i++) 
				classVersionCollection.add(i, new ClassVersion(lastLoc, lastMethods));

			lastNum = num;
			lastLoc = loc;
			lastMethods = methods;

			if (firstClassVersion == 0 && lastMethods > 0) {
				firstClassVersion = num + 1;
			}

			if (lastMethods > maxMethods)
				maxMethods = lastMethods;

			if (lastLoc > maxLoc)
				maxLoc = lastLoc;
		}

		if (maxMethods > 0) {
			Building building = new Building(folder.getString("name"), folder.getString("type"), 
		             level, skyscrapAR, classVersionCollection, cityDrawer, cityItemCollection);
			cityItemCollection.addBuilding(building, maxLoc, maxMethods);
			return building;

		} else
			return null;
	}
}