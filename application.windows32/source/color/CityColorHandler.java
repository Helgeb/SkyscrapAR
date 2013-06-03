package color;

import processing.core.PApplet;

public class CityColorHandler {
	int cityColor;
	int PACKAGE_MIN_COLOR = 0xff000000;
	int PACKAGE_MAX_COLOR = 0xffFFFFFF;
	int CLASS_FLOOR_COLOR = 0xff009900;
	int CLASS_CHANGED_COLOR = 0xff990000;
	int CLASS_DEFAULT_COLOR = 0xff6666CC;
	int CLASS_DEFAULT_COLOR_PROG = 0xff009999;
	int CLASS_HIGHLIGHT_COLOR = 0xffFFFF99;
	int FLOOR_COLOR = 0xff000000;
	int TEXT_COLOR = 0xff000000;
	private PApplet applet;
	
	public CityColorHandler(PApplet applet) {
		this.applet = applet;
	}

	public void fillFloor() {
		(new CityColor(FLOOR_COLOR, applet)).fillColor();
	}

	public void fillText() {
		(new CityColor(TEXT_COLOR, applet)).fillColor();
	}

	public void fillClassFloor() {
		(new CityColor(CLASS_FLOOR_COLOR, applet)).fillColor();
	}

	public void fillClass(boolean isSelected, String type) {
		if (isSelected)
			(new CityColor(CLASS_HIGHLIGHT_COLOR, applet)).fillColor();
		else if (type.equals("CLAS"))
			(new CityColor(CLASS_DEFAULT_COLOR, applet)).fillColor();
		else
			(new CityColor(CLASS_DEFAULT_COLOR_PROG, applet)).fillColor();
	}

	public void fillPackageColor(float fracLevel) {
		applet.fill(applet.red(PACKAGE_MIN_COLOR) * (1 - fracLevel) + applet.red(PACKAGE_MAX_COLOR)
				* fracLevel, applet.green(PACKAGE_MIN_COLOR) * (1 - fracLevel)
				+ applet.green(PACKAGE_MAX_COLOR) * fracLevel, applet.blue(PACKAGE_MIN_COLOR)
				* (1 - fracLevel) + applet.blue(PACKAGE_MAX_COLOR) * fracLevel);
	}
}