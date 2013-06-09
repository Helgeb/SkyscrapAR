package model;

import application.SkyscrapAR;
import application.draw.CityDrawer;
import treemap.MapLayout;
import treemap.MapModel;
import treemap.Mappable;
import treemap.PivotBySplitSize;

public class District extends CityItem implements MapModel {
	private MapLayout algorithm = new PivotBySplitSize();
	private Mappable[] items;
	private boolean layoutValid;
	private CityDrawer cityDrawer;

	public District(String name, int level, Mappable[] items, int itemSize, SkyscrapAR skyscrapAR, 
				CityDrawer cityDrawer, CityItemCollection cityItemCollection) {
		super(name, "Package", level, skyscrapAR, cityItemCollection);
		this.items = items;
		size = itemSize;
		this.cityDrawer = cityDrawer;
		cityItemCollection.addDistrict(this);
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
