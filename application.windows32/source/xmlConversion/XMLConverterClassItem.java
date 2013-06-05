package xmlConversion;

import application.SkyscrapAR;
import cityItems.CityItem;
import cityItems.ClassItem;
import cityItems.ClassVersion;
import cityItems.ClassVersionCollection;
import picking.Picker;
import processing.xml.XMLElement;

public class XMLConverterClassItem implements XMLConverter {
	
	private SkyscrapAR skyscrapAR;
	private Picker picker;

	public XMLConverterClassItem(SkyscrapAR skyscrapAR, Picker picker) {
		this.skyscrapAR = skyscrapAR;
		this.picker = picker;
	}
	
	public CityItem convertItem(XMLElement folder, int level) {
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
			if (maxLoc > skyscrapAR.g_maxLoc)
				skyscrapAR.g_maxLoc = maxLoc;

			skyscrapAR.g_total_loc += maxLoc;
			skyscrapAR.g_total_subroutines += maxMethods;
			skyscrapAR.g_total_objects += 1;
			return new ClassItem(folder.getString("name"), 
					             folder.getString("type"), 
					             level, skyscrapAR, classVersionCollection, picker);
		} else
			return null;
	}
}