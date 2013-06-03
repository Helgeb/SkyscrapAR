package xmlConversion;

import cityItems.CityItem;
import processing.core.PApplet;
import processing.xml.XMLElement;

public interface XMLConverter {
	
	public CityItem convertItem(XMLElement folder, int level);

}
