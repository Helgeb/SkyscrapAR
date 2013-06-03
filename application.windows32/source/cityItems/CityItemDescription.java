package cityItems;

import application.SkyscrapAR;

public class CityItemDescription {
	String type;
	private String name;
	int level;
	private SkyscrapAR skyscrapAR;

	public CityItemDescription(String name, String type, int level, SkyscrapAR skyscrapAR) {
		this.skyscrapAR = skyscrapAR;
		this.type = type;
		this.level = level;
		this.name = name;
		if (level > skyscrapAR.maxLevel)
			skyscrapAR.maxLevel = level;
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
		return (float) level / (float) skyscrapAR.maxLevel;
	}
}