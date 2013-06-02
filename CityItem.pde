class CityItem extends SimpleMapItem {
  int index = -1;
  
  CityItemDescription cityItemDescription;
  boolean isSelected;
  
  public CityItem(String name, String type, int level) {
      cityItemDescription = new CityItemDescription(name, type, level);
      this.index = g_treemapItems.size();
  }
  
  public String printTitleString() {
     return cityItemDescription.printTitleString();
  }  
  
  public void toggleSelect(int id) {
    if (!isSelected)
      g_nSelectedItems += 1;
    else
      g_nSelectedItems -= 1;
    isSelected = !isSelected;
    println("" + id + ": " + cityItemDescription.printNameAndLevel());
  }
  
  public boolean isSelected() {
    return isSelected;
  }

  void boxWithBounds(double x, double y, double z, double w, double h, double zz, double baseRatio) {
    float a = (float)(x + w/2);
    float b = (float)(y + h/2);
    float c = (float)(z + zz/2);
    translate(a, b, c);
    box((float)(w*baseRatio), (float)(h*baseRatio), (float)zz);
    translate(-a, -b, -c);
  }  
}
