
int maxPackageLevel = -1;

class PackageItem extends ClassItem implements MapModel {    
  MapLayout algorithm = new PivotBySplitSize();
  Mappable[] items;
  boolean layoutValid;
    
  public PackageItem(PackageItem parent, XMLElement folder, int level) {
    this.type = "Package";
    this.parent = parent;
    this.level = level;
    this.index = g_treemapItems.size();
    this.name = folder.getString("name");  

    g_total_packages += 1;
    g_treemapItems.add(this);
      
    if (level > maxPackageLevel)
      maxPackageLevel = level;
    
    XMLElement[] contents = folder.getChildren();
    Mappable[] itemsHelp = new Mappable[contents.length];
    int count = 0;
    for (XMLElement elem: contents) {
      
      ClassItem newItem = null;
      if (elem.getName().equals("class")) {
        newItem = new ClassItem(this, elem, level+1);
        if (newItem.getMethods() == 0)
           newItem = null;
      }
      else {  
        newItem = new PackageItem(this, elem, level+1);
        if (((PackageItem)newItem).getItemCount() == 0)
          newItem = null;
      }
 
      if (newItem != null) { 
          itemsHelp[count++] = newItem;
          size += newItem.getSize();
      }
    }
 
    if (count > 0 && shouldElementBeIncluded() ) {
      items = new Mappable[count];
      for (int i = 0; i < count; i++)
        items[i] = itemsHelp[i];
    }
  }
  
  boolean shouldElementBeIncluded() {
    if (this.name == null)
      return true;
    for (String excludedElement : excludedElements) {
      if (this.name.equals(excludedElement))
        return false;
    }
    return true;
  }

  
  /* MapModel interface */
  Mappable[] getItems() {
    return items;
  }

  int getItemCount() {
    if (items == null){
      return 0;
    }
    return items.length;
  }
  
  /* Drawing */
  
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
    float fracLevel = level / (float)maxPackageLevel;
    fill(red(PACKAGE_MIN_COLOR) * (1 - fracLevel) + red(PACKAGE_MAX_COLOR) * fracLevel,
    green(PACKAGE_MIN_COLOR) * (1 - fracLevel) + green(PACKAGE_MAX_COLOR) * fracLevel,
    blue(PACKAGE_MIN_COLOR) * (1 - fracLevel) + blue(PACKAGE_MAX_COLOR) * fracLevel);
    boxWithBounds(bounds.x, bounds.y, (level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 
                  PACKAGE_HEIGHT, PACKAGE_BASE_RATIO);
  
    for (Mappable item: items) {
      item.draw();
    }
  }
}
