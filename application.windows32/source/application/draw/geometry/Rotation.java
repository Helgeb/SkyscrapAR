package application.draw.geometry;

import processing.core.PApplet;

public class Rotation {
	private SingleRotation xRotation;
	private SingleRotation yRotation;
	private SingleRotation zRotation;

	public Rotation(PApplet applet) {
		xRotation = new XRotation(applet);
		yRotation = new YRotation(applet);
		zRotation = new ZRotation(applet);
	}

	public void applyRotation() {
		xRotation.applyRotation();
		yRotation.applyRotation();
		zRotation.applyRotation();
	}

	public void incXRotation(float amount) {
		xRotation.incRotation(amount);
	}

	public void incYRotation(float amount) {
		yRotation.incRotation(amount);
	}

	public void incZRotation(float amount) {
		zRotation.incRotation(amount);
	}
}
