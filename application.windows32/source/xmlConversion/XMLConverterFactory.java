package xmlConversion;

import application.SkyscrapAR;

public class XMLConverterFactory {

	private String[] excludedElements;
	private SkyscrapAR skyscrapAR;

	public XMLConverterFactory(SkyscrapAR skyscrapAR, String[] excludedElements) {
		this.excludedElements = excludedElements;
		this.skyscrapAR = skyscrapAR;
	}
	
	public XMLConverter getPackageItemConverter() {
		return new XMLConverterPackageItem(this, excludedElements, skyscrapAR);
	}
	
	private XMLConverter getClassItemConverter() {
		return new XMLConverterClassItem(skyscrapAR);
	}
	
	public XMLConverter getXMLConverter(String name) {
		if (name.equals("class"))
			return getClassItemConverter();
		else
			return getPackageItemConverter();
	}
}