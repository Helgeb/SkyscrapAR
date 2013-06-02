class XMLConverterPackageItem extends XMLConverter {
  
    public CityItem convertItem(PackageItem parent, XMLElement folder, int level) {
     return new PackageItem(parent, folder, level);
  }
}
