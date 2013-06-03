package color;

import processing.core.PApplet;

public class CityColor {
	int cityColor;
	private PApplet applet;

	public CityColor(int cityColor, PApplet applet) {
		this.cityColor = cityColor;
		this.applet = applet;
	}

	public void fillColor() {
		applet.fill(cityColor);
	}
}