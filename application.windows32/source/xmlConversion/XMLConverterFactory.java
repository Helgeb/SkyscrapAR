package xmlConversion;

import picking.Picker;
import application.CityPicker;
import application.SkyscrapAR;

public class XMLConverterFactory {

	private String[] excludedElements;
	private SkyscrapAR skyscrapAR;
	private CityPicker picker;

	public XMLConverterFactory(SkyscrapAR skyscrapAR, String[] excludedElements, CityPicker picker) {
		this.excludedElements = excludedElements;
		this.skyscrapAR = skyscrapAR;
		this.picker = picker;
	}
	
	public XMLConverter getPackageItemConverter() {
		return new XMLConverterPackageItem(this, excludedElements, skyscrapAR, picker);
	}
	
	private XMLConverter getClassItemConverter() {
		return new XMLConverterClassItem(skyscrapAR, picker);
	}
	
	public XMLConverter getXMLConverter(String name) {
		if (name.equals("class"))
			return getClassItemConverter();
		else
			return getPackageItemConverter();
	}
}