package xmlConversion;

import picking.Picker;
import application.SkyscrapAR;

public class XMLConverterFactory {

	private String[] excludedElements;
	private SkyscrapAR skyscrapAR;
	private Picker picker;

	public XMLConverterFactory(SkyscrapAR skyscrapAR, String[] excludedElements, Picker picker) {
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