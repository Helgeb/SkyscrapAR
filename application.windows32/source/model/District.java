package model;

import application.CityPicker;
import application.SkyscrapAR;
import application.draw.color.CityColorHandler;
import treemap.MapLayout;
import treemap.MapModel;
import treemap.Mappable;
import treemap.PivotBySplitSize;
import treemap.Rect;

public class District extends CityItem implements MapModel {
	private MapLayout algorithm = new PivotBySplitSize();
	private Mappable[] items;
	private boolean layoutValid;
	private CityPicker picker;
	private CityColorHandler cityColorHandler;
	private double PACKAGE_BASE_RATIO = 0.90f;

	public District(String name, int level, Mappable[] items, int itemSize, SkyscrapAR skyscrapAR, CityPicker picker, CityColorHandler cityColorHandler) {
		super(name, "Package", level, skyscrapAR);
		this.items = items;
		size = itemSize;
		this.picker = picker;
		this.cityColorHandler = cityColorHandler;
		skyscrapAR.g_treemapItems.add(this);
		skyscrapAR.g_total_packages += 1;
	}

	public Mappable[] getItems() {
		return items;
	}

	public int getItemCount() {
		return items.length;
	}

	public Rect rectRatio(Rect rect, double ratio) {
		double deltaw = rect.w * (1 - ratio) / 2;
		double deltah = rect.h * (1 - ratio) / 2;
		return new Rect(rect.x + deltaw, rect.y + deltah, rect.w * ratio, rect.h * ratio);
	}

	public void checkLayout() {
		if (!layoutValid) {
			if (getItemCount() != 0) {
				algorithm.layout(this, rectRatio(bounds, PACKAGE_BASE_RATIO));
			}
			layoutValid = true;
		}
	}

	public void draw() {
		checkLayout();
		picker.start(this.index);
		Rect bounds = this.getBounds();
		skyscrapAR.strokeWeight(1);
		skyscrapAR.stroke(0);
		cityColorHandler.fillPackageColor(cityItemDescription.calcFracLevel());
		boxWithBounds(bounds.x, bounds.y, (cityItemDescription.getLevel() - 1), bounds.w, bounds.h, 1.0f, PACKAGE_BASE_RATIO);
		for (Mappable item : items) {
			item.draw();
		}
	}
}
