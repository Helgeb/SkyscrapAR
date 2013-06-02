public class CityItemDescription {
  private String type;
  private String name;
  private int level;
  
  public CityItemDescription(String name, String type, int level) {
    this.type = type;
    this.level = level;
    this.name = name;
  }
    
  public String printTitleString() {
    return "[" + type + "] " + name;
  }
  
  public String printNameAndLevel() {
    return name + " level=" + level;
  }
  
  public boolean isClass() {
    return type.equals("CLAS");
  }
}
