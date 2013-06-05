package cityItems;

import java.util.HashMap;

import processing.core.PApplet;

public class ClassVersionCollection {

	private HashMap<Integer, ClassVersion> versions;
    private int maxMethods;
    
	public ClassVersionCollection() {
		versions = new HashMap<Integer, ClassVersion>();
		maxMethods = 0;
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
		ClassVersion classVersion = versions.get(new Integer(version));
		if (classVersion == null)
			return 0;
		return classVersion.getValue(attr);
	}

	public void add(int version, ClassVersion classVersion) {
		versions.put(new Integer(version), classVersion);
		int methods = classVersion.getValue("methods");
		if (methods > maxMethods)
			maxMethods = methods;
		
	}

	public int size() {
		return maxMethods;
	}

}
