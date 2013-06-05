package cityItems;

import picking.Picker;
import application.SkyscrapAR;
import treemap.Rect;

public class ClassItem extends CityItem {
	ClassVersionCollection versions;
	private Picker picker;

	public ClassItem(String name, String type, int level, SkyscrapAR skyscrapAR, 
			         ClassVersionCollection versions, Picker picker) {
		super(name, type, level, skyscrapAR);
		this.picker = picker;
		skyscrapAR.g_treemapItems.add(this);
		this.versions = versions;
		setSize(versions.size());
	}

	public String printTitleString(double version) {
		return super.printTitleString() + "\nLOC:"
				+ getIntForVersion("avloc", version) + " methods: "
				+ getIntForVersion("methods", version);
	}

	private double getIntForVersion(String attr, double version) {
		return versions.getVersionValue(attr, version);
	}

	public void draw() {
		double version = 5;
		Rect bounds = this.getBounds();

		skyscrapAR.stroke(1);
		skyscrapAR.strokeWeight(1);
		skyscrapAR.colorHandler.fillClassFloor();
		// box for largest version
		boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1) * skyscrapAR.PACKAGE_HEIGHT, bounds.w, bounds.h, 0.02f, skyscrapAR.CLASS_BASE_RATIO);

		double boxHeight;
		double currentLoc = getIntForVersion("avloc", version);
		double currentMethods = getIntForVersion("methods", version);

		if (currentLoc != 0) {
			boxHeight = 1 + (currentLoc / skyscrapAR.g_maxLoc) * skyscrapAR.CLASS_MAX_HEIGHT;

			boxHeight *= skyscrapAR.heightScale;
			if (boxHeight < 0)
				boxHeight = 0;

			double currentFactor = currentMethods / getSize();

			picker.start(this.index);
			skyscrapAR.colorHandler.fillClass(isSelected, cityItemDescription.type);
			boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1) * skyscrapAR.PACKAGE_HEIGHT, bounds.w, bounds.h, boxHeight,skyscrapAR.CLASS_BASE_RATIO * currentFactor);
		}
	}
}
