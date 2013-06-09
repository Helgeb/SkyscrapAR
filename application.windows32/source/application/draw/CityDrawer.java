package application.draw;

import model.CityItemCollection;
import model.District;
import treemap.MapLayout;
import treemap.Rect;
import application.CityPicker;
import application.SkyscrapAR;
import application.VersionController;
import application.draw.color.CityColorHandler;

public class CityDrawer {

	private SkyscrapAR skyscrapAR;
	private CityPicker cityPicker;
	private CityItemCollection cityItemCollection;
	private CityColorHandler colorHandler;
	private VersionController versionController;
	
	private float heightScale = 1.0f;
	private double CLASS_BASE_RATIO = 0.70f;
	private double CLASS_MAX_HEIGHT = 120;
	private double PACKAGE_BASE_RATIO = 0.90f;

	
	public CityDrawer(SkyscrapAR skyscrapAR, CityColorHandler colorHandler, CityPicker cityPicker,
			CityItemCollection cityItemCollection, VersionController versionController) {
		this.skyscrapAR = skyscrapAR;
		this.colorHandler = colorHandler;
		this.cityPicker = cityPicker;
		this.cityItemCollection = cityItemCollection;
		this.versionController = versionController;
	}
	
	public double getCurrentVersion() {
		return versionController.getCurrentVersion();
	}

	private void boxWithBounds(double x, double y, double z, double w, double h, double zz, double baseRatio) {
		float a = (float) (x + w / 2);
		float b = (float) (y + h / 2);
		float c = (float) (z + zz / 2);
		skyscrapAR.translate(a, b, c);
		skyscrapAR.box((float) (w * baseRatio), (float) (h * baseRatio),(float) zz);
		skyscrapAR.translate(-a, -b, -c);
	}
	
	public void drawBuilding(Rect bounds, int level, double base, double height, double maxBase, String type, int index, boolean isSelected) {
		skyscrapAR.stroke(1);
		skyscrapAR.strokeWeight(1);
		colorHandler.fillClassFloor();
		// box for largest version
		boxWithBounds(bounds.x, bounds.y, (level - 1), bounds.w, bounds.h, 0.02f, CLASS_BASE_RATIO);
		double boxHeight;
		if (height != 0) {
			boxHeight = 1 + (height / cityItemCollection.getMaxLoc()) * CLASS_MAX_HEIGHT;

			boxHeight *= heightScale;
			if (boxHeight < 0)
				boxHeight = 0;

			double currentFactor = base / maxBase;

			cityPicker.start(index);
			colorHandler.fillClass(isSelected, type);
			boxWithBounds(bounds.x, bounds.y, (level - 1), bounds.w, bounds.h, boxHeight,CLASS_BASE_RATIO * currentFactor);
		}
	}

	public void drawDistrict(int index, Rect bounds, int level, float fracLevel) {
		cityPicker.start(index);
		skyscrapAR.strokeWeight(1);
		skyscrapAR.stroke(0);
		colorHandler.fillPackageColor(fracLevel);
		boxWithBounds(bounds.x, bounds.y, (level - 1), bounds.w, bounds.h, 1.0f, PACKAGE_BASE_RATIO);
	}
	
	private Rect rectRatio(Rect rect, double ratio) {
		double deltaw = rect.w * (1 - ratio) / 2;
		double deltah = rect.h * (1 - ratio) / 2;
		return new Rect(rect.x + deltaw, rect.y + deltah, rect.w * ratio, rect.h * ratio);
	}
	
	public void setUpDistrictLayout(MapLayout algorithm, District district, Rect bounds) {
		algorithm.layout(district, rectRatio(bounds, PACKAGE_BASE_RATIO));
	}

	public void incHeightScale(float increment) {
		heightScale += increment;
	}
}
