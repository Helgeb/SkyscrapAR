package xmlConversion;

public class XMLConverterFactory {
	
	public XMLConverter getPackageItemConverter(String[] excludedElements) {
		return new XMLConverterPackageItem(this, excludedElements);
	}
	
	private XMLConverter getClassItemConverter() {
		return new XMLConverterClassItem();
	}
	
	public XMLConverter getXMLConverter(String name, String[] excludedElements) {
		if (name.equals("class"))
			return getClassItemConverter();
		else
			return getPackageItemConverter(excludedElements);
	}
}