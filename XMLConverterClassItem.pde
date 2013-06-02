class XMLConverterClassItem implements XMLConverter {

  public CityItem convertItem(XMLElement folder, int level){
    XMLElement[] versions = folder.getChildren();
    int lastVersion = versions[versions.length-1].getInt("num");
    int[] locs = new int[lastVersion];
    int[] methods = new int[lastVersion];
    int maxLoc = 0;
    int maxMethods = 0;
    int lastNum = -1;
    int lastLoc = 0;
    int lastMethods = 0;
    int firstClassVersion = 0;
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
    
      if (firstClassVersion == 0 && lastMethods > 0) {
        firstClassVersion = num+1;
      }
              
      if (lastMethods > maxMethods)
        maxMethods = lastMethods;
        
      if (lastLoc > maxLoc)
        maxLoc = lastLoc;        
    }

    if (maxLoc > 0) {  
      if (maxLoc > g_maxLoc)
        g_maxLoc = maxLoc;
              
      g_total_loc += maxLoc;
      g_total_subroutines += maxMethods;    
      g_total_objects += 1;
      return new ClassItem(folder.getString("name"), folder.getString("type"), level, locs, methods, maxLoc, maxMethods, firstClassVersion);
    }
    else
      return null;
  }
}
