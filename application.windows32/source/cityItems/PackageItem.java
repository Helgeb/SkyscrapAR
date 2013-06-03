package cityItems;

import application.SkyscrapAR;
import treemap.MapLayout;
import treemap.MapModel;
import treemap.Mappable;
import treemap.PivotBySplitSize;
import treemap.Rect;

public class PackageItem extends CityItem implements MapModel {
	MapLayout algorithm = new PivotBySplitSize();
	Mappable[] items;
	boolean layoutValid;

	public PackageItem(String name, int level, Mappable[] items, int itemSize, SkyscrapAR skyscrapAR) {
		super(name, "Package", level, skyscrapAR);
		this.items = items;
		size = itemSize;
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
		return new Rect(rect.x + deltaw, rect.y + deltah, rect.w * ratio,
				rect.h * ratio);
	}

	public void checkLayout() {
		if (!layoutValid) {
			if (getItemCount() != 0) {
				algorithm.layout(this, rectRatio(bounds, skyscrapAR.PACKAGE_BASE_RATIO));
			}
			layoutValid = true;
		}
	}

	public void draw() {
		checkLayout();
		skyscrapAR.picker.start(this.index);
		Rect bounds = this.getBounds();
		skyscrapAR.strokeWeight(1);
		skyscrapAR.stroke(0);
		skyscrapAR.colorHandler.fillPackageColor(cityItemDescription.calcFracLevel());
		boxWithBounds(bounds.x, bounds.y, (cityItemDescription.level - 1)
				* skyscrapAR.PACKAGE_HEIGHT, bounds.w, bounds.h, skyscrapAR.PACKAGE_HEIGHT, skyscrapAR.PACKAGE_BASE_RATIO);
		for (Mappable item : items) {
			item.draw();
		}
	}
}
