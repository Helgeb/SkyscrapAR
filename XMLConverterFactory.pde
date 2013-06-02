class XMLConverterFactory {
  
  public XMLConverter getPackageItemConverter() {
    return new XMLConverterPackageItem();
  }
  
  public XMLConverter getXMLConverter(String name) {
     if (name.equals("class"))
       return new XMLConverterClassItem();
     else
       return getPackageItemConverter();
  }
}
