package application.draw.color;

import processing.core.PApplet;

public class CityColor {
	int cityColor;
	private PApplet applet;

	public CityColor(int cityColor, PApplet applet) {
		this.cityColor = cityColor;
		this.applet = applet;
	}

	public CityColor(float red, float blue, float green, PApplet applet) {
		cityColor = applet.color(red, green, blue);
		this.applet = applet;
	}
	
	public void fillColor() {
		applet.fill(cityColor);
	}
	
	private float red() {
		return applet.red(cityColor);
	}
	private float blue() {
		return applet.blue(cityColor);
	}
	private float green() {
		return applet.red(cityColor);
	}	
	private float mixFloats(float left, float right, float fracLevel) {
		return left * (1 - fracLevel) + right * fracLevel;
	}
	
	public CityColor add(CityColor otherColor, float fracLevel) {
		float red = mixFloats(red(), otherColor.red(), fracLevel);
		float blue = mixFloats(blue(), otherColor.blue(), fracLevel);
		float green = mixFloats(green(), otherColor.green(), fracLevel);
		return new CityColor(red, blue, green, applet);
	}
}