package geometry;

import processing.core.PApplet;

public class CoordinateHandler {

	private Rotation rotationHandler;
	private Translation translation;
	private Zoom zoomFactor;

	public CoordinateHandler(PApplet applet) {
		zoomFactor = new Zoom(1.75f, applet); 
		rotationHandler = new Rotation(applet);
		translation = new Translation(applet);
	}

	public void incZoomFactor(float amount) {
		zoomFactor.incZoomFactor(amount);
	}

	public void incPosition(float xAmount, float yAmount, float zAmount) {
		translation.incPosition(xAmount, yAmount, zAmount);
	}

	public void applyTransformation() {
		zoomFactor.applyZoom();
		rotationHandler.applyRotation();
		translation.applyTranslation();
	}
	
	public void incRotation(float xAmount, float yAmount, float zAmount) {
		rotationHandler.incXRotation(xAmount);
		rotationHandler.incYRotation(yAmount);
		rotationHandler.incZRotation(zAmount);
	}
}