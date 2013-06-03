package geometry;

import processing.core.PApplet;

public class ZoomFactor {
	
	private PApplet applet;
	private float zoomFactor = 1.75f;

	public ZoomFactor(float zoomFactor, PApplet applet) {
		this.zoomFactor = zoomFactor;
		this.applet = applet;
	}

	public void applyZoom() {
		applet.applyMatrix(zoomFactor, 0, 0, 0, 0, zoomFactor, 0, 0, 0, 0, zoomFactor, 0, 0, 0, 0, 1);
	}

	private void setZoomFactor(float factor) {
		zoomFactor = factor;
		adjustFactorToUpperBound();
		adjustFactorToLowerBound();
	}

	private void adjustFactorToUpperBound() {
		if (zoomFactor > 5.0f)
			zoomFactor = 5.0f;
	}

	private void adjustFactorToLowerBound() {
		if (zoomFactor < 0.1f)
			zoomFactor = 0.1f;
	}

	public void incZoomFactor(float amount) {
		setZoomFactor(zoomFactor + amount);
		PApplet.println("zoom = " + zoomFactor);
	}
}
