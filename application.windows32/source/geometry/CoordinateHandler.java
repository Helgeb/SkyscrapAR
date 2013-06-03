package geometry;

import processing.core.PApplet;

public class CoordinateHandler {

	private Rotation xRotation;
	private Rotation yRotation;
	private Rotation zRotation;
	private float xTranslation = 0;
	private float yTranslation = 0;
	private float zTranslation = 0;
	private ZoomFactor zoomFactor;
	private PApplet applet;

	public CoordinateHandler(PApplet applet) {
		this.applet = applet;
		zoomFactor = new ZoomFactor(1.75f, applet); 
		xRotation = new XRotation(applet);
		yRotation = new YRotation(applet);
		zRotation = new ZRotation(applet);
	}
	public void applyZoom() {
		zoomFactor.applyZoom();
	}

	public void applyXRotation() {
		xRotation.applyRotation();
	}

	public void applyYRotation() {
		yRotation.applyRotation();
	}

	public void applyZRotation() {
		zRotation.applyRotation();
	}

	public void applyTranslation() {
		applet.translate(xTranslation, yTranslation, zTranslation);
	}

	public void incZoomFactor(float amount) {
		zoomFactor.incZoomFactor(amount);
	}

	public void incXPosition(float amount) {
		xTranslation += amount;
	}

	public void incYPosition(float amount) {
		yTranslation += amount;
	}

	public void incZPosition(float amount) {
		zTranslation += amount;
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