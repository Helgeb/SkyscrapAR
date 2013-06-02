class ClassEntity {
  
  int[] methods;
  int[] locs;
  int firstMethods = 0;
  int firstClassVersion;
  int maxMethods = 0;
  int maxLoc = 0;
  
  
  ClassEntity(XMLElement elem, int level) {
           
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
       
    g_total_loc += maxLoc;
    g_total_subroutines += maxMethods; 
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
}
