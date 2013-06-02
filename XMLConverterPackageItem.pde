class XMLConverterPackageItem implements XMLConverter {
  
    public CityItem convertItem(XMLElement folder, int level) {
      String name = folder.getString("name");
      int itemSize = 0;
    
    if (shouldElementBeIncluded(name)) {  
      XMLElement[] contents = folder.getChildren();
      Mappable[] itemsHelp = new Mappable[contents.length];
      int count = 0;
      for (XMLElement elem: contents) {
        
        CityItem newItem = null;
        if (elem.getName().equals("class")) {
          ClassItem newClassItem = new ClassItem(elem, level+1);
          if (newClassItem.getMethods() == 0)
             newClassItem = null;
          newItem = newClassItem;
        }
        else {  
          newItem = convertItem(elem, level+1);
          if (((PackageItem)newItem).getItemCount() == 0)
            newItem = null;
        }
   
        if (newItem != null) { 
            itemsHelp[count++] = newItem;
            itemSize += newItem.getSize();
        }
      }
   
      if (count > 0 ) {
        Mappable[] items;
        items = new Mappable[count];
        for (int i = 0; i < count; i++)
          items[i] = itemsHelp[i];
        return new PackageItem(name, level, items, itemSize);
      }
    }
      return null;

    
  }
  
  boolean shouldElementBeIncluded(String name) {
    if (name == null)
      return true;
    for (String excludedElement : excludedElements) {
      if (name.equals(excludedElement))
        return false;
    }
    return true;
  }
  
}
