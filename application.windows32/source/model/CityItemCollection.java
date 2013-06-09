package model;

import java.text.NumberFormat;
import java.util.LinkedList;

public class CityItemCollection {

	private LinkedList<CityItem> treemapItems = new LinkedList<CityItem>();
	private double maxLoc = 0;
	private int totalLoc;
	private int totalObjects;
	private int totalPackages;
	private int totalSubroutines;
	
	public CityItem getItemById(int id) {
		if (id > -1 && id < treemapItems.size())
			return treemapItems.get(id);
		else
			return null;
	}
	

	public String getInfoText() {
		return "Packages: "  		+ NumberFormat.getInstance().format(totalPackages)
				+ " Objects: "		+ NumberFormat.getInstance().format(totalObjects)
				+ " Subroutines: "	+ NumberFormat.getInstance().format(totalSubroutines)
				+ " Total LOC: "	+ NumberFormat.getInstance().format(totalLoc);
	}


	public double getMaxLoc() {
		return maxLoc;
	}


	public void addDistrict(District district) {
		treemapItems.add(district);
		totalPackages += 1;
	}


	public int getNextIndex() {
		return treemapItems.size();
	}

	public void addBuilding(Building building, int buildingMaxLoc, int maxMethods) {
		treemapItems.add(building);
		if (buildingMaxLoc > maxLoc)
			maxLoc = buildingMaxLoc;

		totalLoc += buildingMaxLoc;
		totalSubroutines += maxMethods;
		totalObjects += 1;		
	}	
}
