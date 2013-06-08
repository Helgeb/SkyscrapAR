package model;

import processing.core.PApplet;
import treemap.Rect;
import application.CityPicker;
import application.SkyscrapAR;
import application.draw.CityDrawer;
import application.draw.color.CityColorHandler;

public class Building extends CityItem {
	ClassVersionCollection versions;
	private CityPicker picker;
	private double CLASS_BASE_RATIO = 0.70f;

	private double CLASS_MAX_HEIGHT = 120;
	
	boolean isSelected;
	private CityDrawer drawController;
	private CityColorHandler cityColorHandler;
	public Building(String name, String type, int level, SkyscrapAR skyscrapAR, 
			         ClassVersionCollection versions, CityPicker picker, CityDrawer drawController, CityColorHandler cityColorHandler) {
		super(name, type, level, skyscrapAR);
		this.picker = picker;
		this.drawController = drawController;
		this.cityColorHandler = cityColorHandler;
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
	public void toggleSelect(int id) {
		isSelected = !isSelected;
		PApplet.println("" + id + ": " + cityItemDescription.printNameAndLevel());
	}

	public boolean isSelected() {
		return isSelected;
	}
	public void draw() {
		double version = drawController.getCurrentVersion();
		Rect bounds = this.getBounds();

		skyscrapAR.stroke(1);
		skyscrapAR.strokeWeight(1);
		cityColorHandler.fillClassFloor();
		// box for largest version
		boxWithBounds(bounds.x, bounds.y, (cityItemDescription.getLevel() - 1), bounds.w, bounds.h, 0.02f, CLASS_BASE_RATIO);

		double boxHeight;
		double currentLoc = getIntForVersion("avloc", version);
		double currentMethods = getIntForVersion("methods", version);

		if (currentLoc != 0) {
			boxHeight = 1 + (currentLoc / skyscrapAR.g_maxLoc) * CLASS_MAX_HEIGHT;

			boxHeight *= skyscrapAR.heightScale;
			if (boxHeight < 0)
				boxHeight = 0;

			double currentFactor = currentMethods / getSize();

			picker.start(this.index);
			cityColorHandler.fillClass(isSelected, cityItemDescription.getType());
			boxWithBounds(bounds.x, bounds.y, (cityItemDescription.getLevel() - 1), bounds.w, bounds.h, boxHeight,CLASS_BASE_RATIO * currentFactor);
		}
	}
}
