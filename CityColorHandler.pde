class CityColorHandler {
  color cityColor;
  color PACKAGE_MIN_COLOR = #000000;
  color PACKAGE_MAX_COLOR = #FFFFFF;
  color CLASS_FLOOR_COLOR = #009900;
  color CLASS_CHANGED_COLOR = #990000;
  color CLASS_DEFAULT_COLOR = #6666CC;
  color CLASS_DEFAULT_COLOR_PROG = #009999;
  color CLASS_HIGHLIGHT_COLOR = #FFFF99;
  color FLOOR_COLOR = #000000;
  color TEXT_COLOR = #000000;
   
  public CityColorHandler() {}
  
  public void fillFloor() {
     (new CityColor(FLOOR_COLOR)).fillColor();
  }  
  public void fillText() {
     (new CityColor(TEXT_COLOR)).fillColor();
  }
  public void fillClassFloor() {
    (new CityColor(CLASS_FLOOR_COLOR)).fillColor();
  }
  public void fillClass(boolean isSelected, String type) {
     if (isSelected) 
       (new CityColor(CLASS_HIGHLIGHT_COLOR)).fillColor();
     else if (type.equals("CLAS"))
       (new CityColor(CLASS_DEFAULT_COLOR)).fillColor();    
     else  
       (new CityColor(CLASS_DEFAULT_COLOR_PROG)).fillColor();     
 }
 
 public void fillPackageColor(float fracLevel) {
   fill(red(PACKAGE_MIN_COLOR) * (1 - fracLevel) + red(PACKAGE_MAX_COLOR) * fracLevel,
        green(PACKAGE_MIN_COLOR) * (1 - fracLevel) + green(PACKAGE_MAX_COLOR) * fracLevel,
        blue(PACKAGE_MIN_COLOR) * (1 - fracLevel) + blue(PACKAGE_MAX_COLOR) * fracLevel);
 }
}
