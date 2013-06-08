package xmlConversion;

import model.CityItem;
import processing.xml.XMLElement;
import application.draw.DrawController;
import application.draw.color.CityColorHandler;

public interface XMLConverter {
	
	public CityItem convertItem(XMLElement folder, int level, DrawController cityDrawer, CityColorHandler cityColorHandler);

}
