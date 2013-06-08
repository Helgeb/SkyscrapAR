package application.draw.geometry;

import processing.core.PApplet;

public class Translation {

	private PApplet applet;
	private float xTranslation;
	private float yTranslation;
	private float zTranslation;
	
	public Translation(PApplet applet) {
		this.applet = applet;
	}

	public void applyTranslation() {
		applet.translate(xTranslation, yTranslation, zTranslation);		
	}

	public void incPosition(float xAmount, float yAmount, float zAmount) {
		xTranslation += xAmount;
		yTranslation += yAmount;
		zTranslation += zAmount;
	}

}
