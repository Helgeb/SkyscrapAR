class ClassItem extends CityItem {
  int[] methods;
  int[] locs;
  int firstMethods = 0;
  int firstClassVersion;
  int maxMethods = 0;
  int maxLoc = 0;
  
  int level;
  XMLElement xmlElement;
  
  ClassItem(PackageItem parent, XMLElement elem, int level) {
    super(elem.getString("name"), elem.getString("type"), level);
    this.parent = parent;
    this.xmlElement = elem;
    this.level = level;
    this.index = g_treemapItems.size();
            
    XMLElement[] versions = elem.getChildren();
    int lastVersion = versions[versions.length-1].getInt("num");

    g_total_objects += 1;
    
    locs = new int[lastVersion];
    methods = new int[lastVersion];
        
    int lastNum = -1;
    int lastLoc = 0;
    int lastMethods = 0;
    for (XMLElement version : versions) {
      int num = version.getInt("num") - 1;
      
      locs[num] = version.getInt("loc");
      methods[num] = version.getInt("methods");
      
      for (int i = lastNum+1; i < num; i++) {
        locs[i] = lastLoc;
        methods[i] = lastMethods;
      }
      
      lastNum = num;
      lastLoc = locs[num];
      lastMethods = methods[num];
    
      if (firstMethods == 0) {
        firstMethods = lastMethods;
        firstClassVersion = num+1;
      }
              
      if (lastMethods > maxMethods)
        maxMethods = lastMethods;
        
      if (lastLoc > maxLoc)
        maxLoc = lastLoc;        
    }

    if (maxLoc > g_maxLoc)
      g_maxLoc = maxLoc;
        
    if (maxMethods > 0 )
      g_treemapItems.add(this);
    setSize(maxMethods);
    
    g_total_loc += maxLoc;
    g_total_subroutines += maxMethods;    

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
    boxWithBounds(bounds.x, bounds.y, (level - 1) * PACKAGE_HEIGHT, bounds.w, bounds.h, 0.02, CLASS_BASE_RATIO);
    
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
      boxWithBounds(bounds.x, bounds.y, (level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, boxHeight, 
                    CLASS_BASE_RATIO * currentFactor);
    }
  }
}
