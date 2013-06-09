package model;

import processing.core.PApplet;
import application.SkyscrapAR;
import application.draw.CityDrawer;

public class Building extends CityItem {
	ClassVersionCollection versions;

	
	boolean isSelected;
	private CityDrawer cityDrawer;

	public Building(String name, String type, int level, SkyscrapAR skyscrapAR, 
				ClassVersionCollection versions, CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		super(name, type, level, skyscrapAR, cityItemCollection);
		this.cityDrawer = cityDrawer;
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
	public void toggleSelect() {
		isSelected = !isSelected;
		PApplet.println(cityItemDescription.printNameAndLevel());		
	}

	public boolean isSelected() {
		return isSelected;
	}
	
	public void draw() {
		double version = cityDrawer.getCurrentVersion();
		double currentLoc = getIntForVersion("avloc", version);
		double currentMethods = getIntForVersion("methods", version);
		cityDrawer.drawBuilding(this.getBounds(), cityItemDescription.getLevel(), currentMethods, currentLoc, 
				getSize(), cityItemDescription.getType(), this.index, isSelected);
	}
}
