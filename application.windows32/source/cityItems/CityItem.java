package cityItems;

import application.SkyscrapAR;
import processing.core.PApplet;
import treemap.SimpleMapItem;

public class CityItem extends SimpleMapItem {
	int index = -1;

	CityItemDescription cityItemDescription;
	boolean isSelected;

	protected SkyscrapAR skyscrapAR;

	public CityItem(String name, String type, int level, SkyscrapAR skyscrapAR) {
		cityItemDescription = new CityItemDescription(name, type, level, skyscrapAR);
		this.index = skyscrapAR.g_treemapItems.size();
		this.skyscrapAR = skyscrapAR;
	}

	public String printTitleString() {
		return cityItemDescription.printTitleString();
	}

	public void toggleSelect(int id) {
		isSelected = !isSelected;
		PApplet.println("" + id + ": " + cityItemDescription.printNameAndLevel());
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void boxWithBounds(double x, double y, double z, double w, double h,
			double zz, double baseRatio) {
		float a = (float) (x + w / 2);
		float b = (float) (y + h / 2);
		float c = (float) (z + zz / 2);
		skyscrapAR.translate(a, b, c);
		skyscrapAR.box((float) (w * baseRatio), (float) (h * baseRatio),
				(float) zz);
		skyscrapAR.translate(-a, -b, -c);
	}
}
