package model;

import java.util.HashMap;

import processing.core.PApplet;

public class BuildingVersionCollection {

	private HashMap<Integer, BuildingVersion> versions;
    private int maxGroundSize;
    
	public BuildingVersionCollection() {
		versions = new HashMap<Integer, BuildingVersion>();
		maxGroundSize = 0;
	}

	public double getVersionValue(String attr, double version) {
		int version1 = PApplet.floor((float) version);
		int version2 = PApplet.ceil((float) version);
		double alpha = version - version1;

		int value1 = getVersionValue(attr, version1);
		int value2 = getVersionValue(attr, version2);
		return (1 - alpha) * value1 + alpha * value2;
	}

	private int getVersionValue(String attr, int version) {
		if (version > versions.size())
			version = versions.size();
		BuildingVersion classVersion = versions.get(new Integer(version));
		if (classVersion == null)
			return 0;
		return classVersion.getValue(attr);
	}

	public void add(int version, BuildingVersion classVersion) {
		versions.put(new Integer(version), classVersion);
		int groundSize = classVersion.getValue("methods");
		if (groundSize > maxGroundSize)
			maxGroundSize = groundSize;
		
	}

	public int size() {
		return maxGroundSize;
	}

}
