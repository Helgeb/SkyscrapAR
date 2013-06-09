package xmlConversion;

import model.CityItem;
import model.CityItemCollection;
import processing.xml.XMLElement;
import application.draw.CityDrawer;

public interface XMLConverter {
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityItemCollection cityItemCollection);

}
