package xmlConversion;

import application.SkyscrapAR;

public class XMLConverterFactory {

	private SkyscrapAR skyscrapAR;

	public XMLConverterFactory(SkyscrapAR skyscrapAR) {
		this.skyscrapAR = skyscrapAR;
	}
	
	public XMLConverter getPackageItemConverter(String[] excludedElements) {
		return new XMLConverterPackageItem(this, excludedElements, skyscrapAR);
	}
	
	private XMLConverter getClassItemConverter() {
		return new XMLConverterClassItem(skyscrapAR);
	}
	
	public XMLConverter getXMLConverter(String name, String[] excludedElements) {
		if (name.equals("class"))
			return getClassItemConverter();
		else
			return getPackageItemConverter(excludedElements);
	}
}