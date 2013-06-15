package xmlConversion;

import java.util.Vector;

import model.*;
import processing.xml.XMLElement;
import treemap.Mappable;
import application.draw.CityDrawer;

public class XMLConverterPackageItem implements XMLConverter {

	private XMLConverterFactory converterFactory;
	private String[] excludedElements;
	
	public XMLConverterPackageItem(XMLConverterFactory converterFactory, String[] excludedElements) {
		this.converterFactory = converterFactory;
		this.excludedElements = excludedElements;
	}
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		String name = folder.getString("name");
		int itemSize = 0;

		if (shouldElementBeIncluded(name)) {
			XMLElement[] contents = folder.getChildren();
			Vector<CityItem> items = new Vector<CityItem>();
			for (XMLElement elem : contents) {
				XMLConverter converter = converterFactory.getXMLConverter(elem.getName(), excludedElements);
				CityItem item = converter.convertItem(elem, level + 1, cityDrawer, cityItemCollection);
				if (item != null) {
					items.add(item);
					itemSize += item.getSize();
				}
			}

			if (itemSize > 0)
				return new District(name, level, items.toArray(new Mappable[items.size()]), itemSize, cityDrawer, cityItemCollection);
		}
		return null;
	}

	public boolean shouldElementBeIncluded(String name) {
		if (name == null)
			return true;
		for (String excludedElement : excludedElements) {
			if (name.equals(excludedElement))
				return false;
		}
		return true;
	}


}