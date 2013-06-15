package model;

import treemap.PivotBySplitSize;
import treemap.Treemap;

public class City {
	
	private Treemap cityMap;
	private CityItemCollection itemCollection;
	
	public City(District cityParent, CityItemCollection itemCollection, int width, int height) {
		this.itemCollection = itemCollection;
		cityMap = new Treemap(cityParent, 0, 0, width, height);
		cityMap.setLayout(new PivotBySplitSize());
		int TREEMAP_SIZE = 100;
		cityMap.updateLayout(-TREEMAP_SIZE / 2, -TREEMAP_SIZE / 2, TREEMAP_SIZE, TREEMAP_SIZE);
	}

	public String getInfoText() {
		return itemCollection.getInfoText();
	}

	public void draw() {
		cityMap.draw();
	}
}
