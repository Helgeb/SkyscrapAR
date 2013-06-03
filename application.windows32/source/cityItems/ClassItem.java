package cityItems;

import application.SkyscrapAR;
import processing.core.PApplet;
import treemap.Rect;

public class ClassItem extends CityItem {
	int[] methods;
	int[] locs;
	int firstMethods = 0;
	int firstClassVersion;
	int maxMethods = 0;
	int maxLoc = 0;

	public ClassItem(String name, String type, int level, int[] locs, int[] methods,
			int maxLoc, int maxMethods, int firstClassVersion, SkyscrapAR skyscrapAR) {
		super(name, type, level, skyscrapAR);
		this.locs = locs;
		this.methods = methods;
		this.maxMethods = maxMethods;
		this.maxLoc = maxLoc;
		this.firstClassVersion = firstClassVersion;
		//g_treemapItems.add(this);
		setSize(maxMethods);
	}

	public String printTitleString() {
		return super.printTitleString() + "\nLOC:"
				+ getIntForCurrentVersion("avloc") + " methods: "
				+ getIntForCurrentVersion("methods");
	}

	public int getMethods() {
		return maxMethods;
	}

	public int getIntForVersion(String attr, int version) {
		version = version - 1;
		int v = version;
		if (v > locs.length - 1) {
			v = locs.length - 1;
		}

		if (attr.equals("avloc"))
			return locs[v];
		else if (attr.equals("methods")) {
			return methods[v];
		} else
			throw new RuntimeException("Error");
	}

	public int getIntForCurrentVersion(String attr) {
		return getIntForVersion(attr, skyscrapAR.g_currentVersion);
	}

	public double getIntBetweenVersions(String attr, double version) {
		int version1 = PApplet.floor((float) version);
		int version2 = PApplet.ceil((float) version);
		double alpha = version - version1;

		int value1 = getIntForVersion(attr, version1);
		int value2 = getIntForVersion(attr, version2);

		return (1 - alpha) * value1 + alpha * value2;
	}

	public double getCurrentTweenInt(String attr) {
		return getIntBetweenVersions(attr, skyscrapAR.g_tweeningVersion);
	}

	public void draw() {
		Rect bounds = this.getBounds();

		skyscrapAR.stroke(1);
		skyscrapAR.strokeWeight(1);
		skyscrapAR.colorHandler.fillClassFloor();
		// box for largest version
		boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1)
				* skyscrapAR.PACKAGE_HEIGHT, bounds.w, bounds.h, 0.02f, skyscrapAR.CLASS_BASE_RATIO);

		double boxHeight;
		double currentLoc = getIntForCurrentVersion("avloc");
		double currentMethods = getIntForCurrentVersion("methods");

		if (currentLoc != 0) {
			boxHeight = 1 + (currentLoc / skyscrapAR.g_maxLoc) * skyscrapAR.CLASS_MAX_HEIGHT;

			boxHeight *= skyscrapAR.heightScale;
			if (boxHeight < 0)
				boxHeight = 0;

			double currentFactor = currentMethods / getSize();

			skyscrapAR.picker.start(this.index);
			skyscrapAR.colorHandler.fillClass(isSelected, cityItemDescription.type);
			boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1)
					* skyscrapAR.PACKAGE_HEIGHT, bounds.w, bounds.h, boxHeight,
					skyscrapAR.CLASS_BASE_RATIO * currentFactor);
		}
	}
}
