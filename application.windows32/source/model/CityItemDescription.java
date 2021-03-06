package model;


public class CityItemDescription {
	private String type;
	private String name;
	private int level;
	private static int maxLevel = -1;

	public CityItemDescription(String name, String type, int level) {
		this.type = type;
		this.level = level;
		this.name = name;
		if (level > maxLevel)
			maxLevel = level;
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

	public float calcFracLevel() {
		return (float) level / (float) maxLevel;
	}

	public int getLevel() {
		return level;
	}

	public String getType() {
		return type;
	}
}