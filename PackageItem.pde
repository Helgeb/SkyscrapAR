int maxPackageLevel = -1;

class PackageItem extends CityItem implements MapModel, VersionableItem {    
  MapLayout algorithm = new PivotBySplitSize();
  Mappable[] items;
  boolean layoutValid;
    
  public PackageItem(PackageItem parent, XMLElement folder, int level) {
    super(folder.getString("name"), "Package", level);
    this.parent = parent;
    this.index = g_treemapItems.size();

    g_treemapItems.add(this);
    
    if (shouldElementBeIncluded()) {  
      if (cityItemDescription.level > maxPackageLevel)
        maxPackageLevel = cityItemDescription.level;
      
      g_total_packages += 1;
        
      XMLElement[] contents = folder.getChildren();
      Mappable[] itemsHelp = new Mappable[contents.length];
      int count = 0;
      for (XMLElement elem: contents) {
        
        CityItem newItem = null;
        if (elem.getName().equals("class")) {
          ClassItem newClassItem = new ClassItem(this, elem, cityItemDescription.level+1);
          if (newClassItem.getMethods() == 0)
             newClassItem = null;
          newItem = newClassItem;
        }
        else {  
          newItem = new PackageItem(this, elem, cityItemDescription.level+1);
          if (((PackageItem)newItem).getItemCount() == 0)
            newItem = null;
        }
   
        if (newItem != null) { 
            itemsHelp[count++] = newItem;
            size += newItem.getSize();
        }
      }
   
      if (count > 0 ) {
        items = new Mappable[count];
        for (int i = 0; i < count; i++)
          items[i] = itemsHelp[i];
      }
    }
  }
  
  boolean shouldElementBeIncluded() {
    if (cityItemDescription.name == null)
      return true;
    for (String excludedElement : excludedElements) {
      if (cityItemDescription.name.equals(excludedElement))
        return false;
    }
    return true;
  }

  Mappable[] getItems() {
    return items;
  }

  int getItemCount() {
    if (items == null){
      return 0;
    }
    return items.length;
  }
    
  Rect rectRatio(Rect rect, double ratio) {
    double deltaw = rect.w * (1 - ratio)/2;
    double deltah = rect.h * (1 - ratio)/2;
    return new Rect(rect.x + deltaw, rect.y + deltah, rect.w*ratio, rect.h*ratio);
  }
  
  void checkLayout() {
    if (!layoutValid) {
      if (getItemCount() != 0) {
        algorithm.layout(this, rectRatio(bounds, PACKAGE_BASE_RATIO));
      }
      layoutValid = true;
    }
  }
  
  int getIntForVersion(String attr, int version) {
    return 0;
  }
  
  void draw() {
    checkLayout();
    
    picker.start(this.index);
    Rect bounds = this.getBounds();
    strokeWeight(1);
    stroke(0);
    float fracLevel = cityItemDescription.level / (float)maxPackageLevel;
    colorHandler.fillPackageColor(fracLevel);
    boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 
                  PACKAGE_HEIGHT, PACKAGE_BASE_RATIO);
  
    for (Mappable item: items) {
      item.draw();
    }
  }
}
