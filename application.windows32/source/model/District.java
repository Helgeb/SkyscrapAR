package model;

import application.draw.CityDrawer;
import treemap.MapLayout;
import treemap.MapModel;
import treemap.Mappable;
import treemap.PivotBySplitSize;
import treemap.SimpleMapItem;

public class District extends SimpleMapItem implements MapModel, CityItem {
	private MapLayout algorithm = new PivotBySplitSize();
	private Mappable[] items;
	private boolean layoutValid;
	private CityDrawer cityDrawer;
	private int index ;
	private CityItemDescription cityItemDescription;

	public District(String name, int level, Mappable[] items, int itemSize, 
				CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		cityItemDescription = new CityItemDescription(name,  "Package", level);
		this.items = items;
		this.index = cityItemCollection.getNextIndex();
		size = itemSize;
		this.cityDrawer = cityDrawer;
	}

	public String printTitleString() {
		return cityItemDescription.printTitleString();
	}
	
	public Mappable[] getItems() {
		return items;
	}

	public int getItemCount() {
		return items.length;
	}

	public void checkLayout() {
		if (!layoutValid) {
			if (getItemCount() != 0) {
				cityDrawer.setUpDistrictLayout(algorithm, this, bounds);
			}
			layoutValid = true;
		}
	}

	public void draw() {
		checkLayout();
		cityDrawer.drawDistrict(this.index, this.getBounds(), cityItemDescription.getLevel(), cityItemDescription.calcFracLevel());
		for (Mappable item : items) {
			item.draw();
		}
	}
}
