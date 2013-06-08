package xmlConversion;

import java.util.Vector;

import model.CityItem;
import model.District;
import picking.Picker;
import processing.xml.XMLElement;
import treemap.Mappable;
import application.CityPicker;
import application.SkyscrapAR;
import application.draw.CityDrawer;
import application.draw.color.CityColorHandler;

public class XMLConverterPackageItem implements XMLConverter {

	private XMLConverterFactory converterFactory;
	private SkyscrapAR skyscrapAR;
	private String[] excludedElements;
	private CityPicker picker;
	
	public XMLConverterPackageItem(XMLConverterFactory converterFactory, String[] excludedElements, SkyscrapAR skyscrapAR, CityPicker picker) {
		this.converterFactory = converterFactory;
		this.excludedElements = excludedElements;
		this.skyscrapAR = skyscrapAR;
		this.picker = picker;
	}
	
	public CityItem convertItem(XMLElement folder, int level, CityDrawer drawController, CityColorHandler cityColorHandler) {
		String name = folder.getString("name");
		int itemSize = 0;

		if (shouldElementBeIncluded(name)) {
			XMLElement[] contents = folder.getChildren();
			Vector<Mappable> items = new Vector<Mappable>();
			for (XMLElement elem : contents) {
				XMLConverter converter = converterFactory.getXMLConverter(elem.getName());
				CityItem item = converter.convertItem(elem, level + 1, drawController, cityColorHandler);
				if (item != null) {
					items.add(item);
					itemSize += item.getSize();
				}
			}

			if (itemSize > 0)
				return new District(name, level, items.toArray(new Mappable[items.size()]), itemSize, skyscrapAR, picker, cityColorHandler);
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