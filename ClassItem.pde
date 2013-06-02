class ClassItem extends CityItem {
  int[] methods;
  int[] locs;
  int firstMethods = 0;
  int firstClassVersion;
  int maxMethods = 0;
  int maxLoc = 0;
    
  ClassItem(String name, String type, int level, int[] locs, int[] methods, int maxLoc, int maxMethods, int firstClassVersion) {
    super(name, type, level);
    this.locs = locs;
    this.methods = methods;
    this.maxMethods = maxMethods;
    this.maxLoc = maxLoc;
    this.firstClassVersion = firstClassVersion;
    g_treemapItems.add(this);
    setSize(maxMethods);
  }

  public String printTitleString() {
     return super.printTitleString() + "\nLOC:" + getIntForCurrentVersion("avloc") + " methods: " + getIntForCurrentVersion("methods");
  } 
  
  int getMethods() {
    return maxMethods;
  }
      
  int getIntForVersion(String attr, int version) {
    version = version - 1;
    int v = version;
    if (v > locs.length - 1) {
      v = locs.length - 1;
    }
    
    if (attr.equals("avloc"))
      return locs[v];
    else if (attr.equals("methods")) {
      return methods[v];
    }
    else
      throw new RuntimeException("Error");
  }

  int getIntForCurrentVersion(String attr) {
    return getIntForVersion(attr, g_currentVersion);
  }
  
    double getIntBetweenVersions(String attr, double version) {
    int version1 = floor((float)version);
    int version2 = ceil((float)version);
    double alpha = version - version1;
    
    int value1 = getIntForVersion(attr, version1);
    int value2 = getIntForVersion(attr, version2);
    
    return (1-alpha)*value1 + alpha*value2;
  }
  
  double getCurrentTweenInt(String attr) {
    return getIntBetweenVersions(attr, g_tweeningVersion);
  }
  
  void draw() {
    Rect bounds = this.getBounds();
    
    stroke(1);
    strokeWeight(1);
    colorHandler.fillClassFloor();
    // box for largest version
    boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 0.02, CLASS_BASE_RATIO);
    
    double boxHeight;
    double currentLoc = getIntForCurrentVersion("avloc");
    double currentMethods = getIntForCurrentVersion("methods");

    if (currentLoc != 0) {
      boxHeight = 1 + (currentLoc / g_maxLoc) * CLASS_MAX_HEIGHT;
      
      boxHeight *= heightScale;
      if (boxHeight < 0) boxHeight = 0;
      
      double currentFactor = currentMethods / getSize();
                    
      picker.start(this.index);
      colorHandler.fillClass(isSelected, cityItemDescription.type);
      boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, boxHeight, 
                    CLASS_BASE_RATIO * currentFactor);
    }
  }
}
