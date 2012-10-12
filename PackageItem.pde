
int maxPackageLevel = -1;

class PackageItem extends ClassItem implements MapModel {    
  MapLayout algorithm = new PivotBySplitSize();
  Mappable[] items;
  boolean layoutValid;
    
  public PackageItem(PackageItem parent, XML folder, int level) {
//    super(parent, folder, level);      
    this.type = "Package";
    this.parent = parent;
    this.level = level;
    this.index = g_treemapItems.size();
    this.name = folder.getString("name");    
    
    if (parent == null || parent.name == null)
      this.fullName = this.name;
    else
      this.fullName = parent.fullName + "." + this.name;

    if (level > maxPackageLevel)
      maxPackageLevel = level;
    
    g_treemapItems.add(this);

    XML[] contents = getNonTextElements(folder.getChildren());
    items = new Mappable[contents.length];
    int count = 0;
    for (int i = 0; i < contents.length; i++) {
      XML elem = contents[i];

      ClassItem newItem = null;
      if (elem.getName().equals("class")) {
        newItem = new ClassItem(this, elem, level+1);
      }
      else {
        newItem = new PackageItem(this, elem, level+1);
      }
       
      items[count++] = newItem;
      size += newItem.getSize();
    }
  }
  
  /* MapModel interface */
  Mappable[] getItems() {
    return items;
  }

  int getItemCount() {
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
      // good place to write debug code.
      
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
    
//    picker.start(this.index);
    // TODO: draw the package (a quarter)
    Rect bounds = this.getBounds();
    strokeWeight(1);
    stroke(0);
    float fracLevel = level / (float)maxPackageLevel;
    fill(red(PACKAGE_MIN_COLOR) * (1 - fracLevel) + red(PACKAGE_MAX_COLOR) * fracLevel,
    green(PACKAGE_MIN_COLOR) * (1 - fracLevel) + green(PACKAGE_MAX_COLOR) * fracLevel,
    blue(PACKAGE_MIN_COLOR) * (1 - fracLevel) + blue(PACKAGE_MAX_COLOR) * fracLevel);
    boxWithBounds(bounds.x, bounds.y, (level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, PACKAGE_HEIGHT, PACKAGE_BASE_RATIO);
  
    for (int i = 0; i < items.length; i++) {
      items[i].draw();
    }
  }
}
