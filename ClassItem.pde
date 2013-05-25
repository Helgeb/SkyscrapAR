
class ClassItem extends SimpleMapItem {
  color currentColor;
  int index = -1;
  String type;
  String name;
  
  int[] methods;
  int[] locs;
  int firstMethods = 0;
  int firstClassVersion;
  int maxMethods = 0;
  int maxLoc = 0;
  
  PackageItem parent;
  int level;
  XMLElement xmlElement;
  
  ClassItem() { }

  ClassItem(PackageItem parent, XMLElement elem, int level) {
    this.type = elem.getString("type");
    this.parent = parent;
    this.xmlElement = elem;
    this.level = level;
    this.index = g_treemapItems.size();
    this.name = elem.getString("name");
            
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

    this.currentColor = getDefaultColor();
  }

  boolean isSelected() {
    return this.currentColor == CLASS_HIGHLIGHT_COLOR;
  }
  
  int getMethods() {
    return maxMethods;
  }
  
int getDefaultColor() {
      return (this.type.equals("CLAS") ? CLASS_DEFAULT_COLOR : CLASS_DEFAULT_COLOR_PROG);
  }
  
  void toggleSelect() {
    if (this.currentColor == getDefaultColor()) {
      this.currentColor = CLASS_HIGHLIGHT_COLOR;
      g_nSelectedItems += 1;
    }
    else {
      this.currentColor = getDefaultColor();
      g_nSelectedItems -= 1;
    }
  }

  void boxWithBounds(double x, double y, double z, double w, double h, double zz, double baseRatio) {
    float a = (float)(x + w/2);
    float b = (float)(y + h/2);
    float c = (float)(z + zz/2);
    translate(a, b, c);
    box((float)(w*baseRatio), (float)(h*baseRatio), (float)zz);
    translate(-a, -b, -c);
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
    fill(CLASS_FLOOR_COLOR);
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
      fill(this.currentColor);
      boxWithBounds(bounds.x, bounds.y, (level-1) * PACKAGE_HEIGHT, bounds.w, bounds.h, boxHeight, 
                    CLASS_BASE_RATIO * currentFactor);
    }
  }
}
