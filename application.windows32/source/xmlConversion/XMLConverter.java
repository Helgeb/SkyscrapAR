package xmlConversion;

import model.CityItem;
import processing.xml.XMLElement;
import application.draw.CityDrawer;
import application.draw.color.CityColorHandler;

public interface XMLConverter {
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityColorHandler cityColorHandler);

}
