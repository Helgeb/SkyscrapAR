package model;

import processing.core.PApplet;
import treemap.SimpleMapItem;
import application.draw.CityDrawer;

public class Building extends SimpleMapItem implements CityItem {
	
	private BuildingVersionCollection versions;
	private CityItemDescription cityItemDescription;
	private boolean isSelected;
	private CityDrawer cityDrawer;
	private int index;

	public Building(CityItemDescription cityItemDescription, BuildingVersionCollection versions, CityDrawer cityDrawer, int index) {
		this.cityItemDescription = cityItemDescription;
		this.index = index;
		this.cityDrawer = cityDrawer;
		this.versions = versions;
		setSize(versions.size());
	}

	public String printTitleString() {
		double version = cityDrawer.getCurrentVersion();
		return cityItemDescription.printTitleString() + "\nLOC:"
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
		double currentHeight = getIntForVersion("avloc", version);
		double currentGroundSize = getIntForVersion("methods", version);
		cityDrawer.drawBuilding(this.getBounds(), cityItemDescription.getLevel(), currentGroundSize, currentHeight, 
				getSize(), cityItemDescription.getType(), index, isSelected);
	}
}
