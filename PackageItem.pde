class PackageItem extends CityItem implements MapModel {    
  MapLayout algorithm = new PivotBySplitSize();
  Mappable[] items;
  boolean layoutValid;
  
   public PackageItem(String name, int level, Mappable[] items, int itemSize) {
    super(name, "Package", level);
    this.items = items;
    size = itemSize;
    g_treemapItems.add(this);
    g_total_packages += 1;
  }
  
  Mappable[] getItems() {
    return items;
  }

  int getItemCount() {
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
  
  void draw() {
    checkLayout();
    picker.start(this.index);
    Rect bounds = this.getBounds();
    strokeWeight(1);
    stroke(0);
    colorHandler.fillPackageColor(cityItemDescription.calcFracLevel());
    boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 
                  PACKAGE_HEIGHT, PACKAGE_BASE_RATIO);
    for (Mappable item: items) {
      item.draw();
    }
  }
}
