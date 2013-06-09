package xmlConversion;

import java.util.Vector;

import model.CityItem;
import model.CityItemCollection;
import model.District;
import processing.xml.XMLElement;
import treemap.Mappable;
import application.SkyscrapAR;
import application.draw.CityDrawer;

public class XMLConverterPackageItem implements XMLConverter {

	private XMLConverterFactory converterFactory;
	private SkyscrapAR skyscrapAR;
	private String[] excludedElements;
	
	public XMLConverterPackageItem(XMLConverterFactory converterFactory, String[] excludedElements, 
				SkyscrapAR skyscrapAR) {
		this.converterFactory = converterFactory;
		this.excludedElements = excludedElements;
		this.skyscrapAR = skyscrapAR;
	}
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		String name = folder.getString("name");
		int itemSize = 0;

		if (shouldElementBeIncluded(name)) {
			XMLElement[] contents = folder.getChildren();
			Vector<Mappable> items = new Vector<Mappable>();
			for (XMLElement elem : contents) {
				XMLConverter converter = converterFactory.getXMLConverter(elem.getName());
				CityItem item = converter.convertItem(elem, level + 1, cityDrawer, cityItemCollection);
				if (item != null) {
					items.add(item);
					itemSize += item.getSize();
				}
			}

			if (itemSize > 0)
				return new District(name, level, items.toArray(new Mappable[items.size()]), itemSize, 
																				skyscrapAR, cityDrawer, cityItemCollection);
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