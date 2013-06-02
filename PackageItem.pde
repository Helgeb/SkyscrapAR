int maxPackageLevel = -1;

class PackageItem extends ClassItem implements MapModel {    
  MapLayout algorithm = new PivotBySplitSize();
  Mappable[] items;
  boolean layoutValid;
  ObjectDescription packageDescription;
    
  public PackageItem(PackageItem parent, XMLElement folder, int level) {
    packageDescription = new ObjectDescription(folder.getString("name"), "Package", level);
    this.parent = parent;
    this.index = g_treemapItems.size();

    g_treemapItems.add(this);
    
    if (shouldElementBeIncluded()) {  
      if (packageDescription.level > maxPackageLevel)
        maxPackageLevel = packageDescription.level;
      
      g_total_packages += 1;
        
      XMLElement[] contents = folder.getChildren();
      Mappable[] itemsHelp = new Mappable[contents.length];
      int count = 0;
      for (XMLElement elem: contents) {
        
        ClassItem newItem = null;
        if (elem.getName().equals("class")) {
          newItem = new ClassItem(this, elem, packageDescription.level+1);
          if (newItem.getMethods() == 0)
             newItem = null;
        }
        else {  
          newItem = new PackageItem(this, elem, packageDescription.level+1);
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
    if (packageDescription.name == null)
      return true;
    for (String excludedElement : excludedElements) {
      if (packageDescription.name.equals(excludedElement))
        return false;
    }
    return true;
  }

  public String printTitleString() {
     return packageDescription.printTitleString();
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
    float fracLevel = packageDescription.level / (float)maxPackageLevel;
    colorHandler.fillPackageColor(fracLevel);
    boxWithBounds(bounds.x, bounds.y, (packageDescription.level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 
                  PACKAGE_HEIGHT, PACKAGE_BASE_RATIO);
  
    for (Mappable item: items) {
      item.draw();
    }
  }
}
