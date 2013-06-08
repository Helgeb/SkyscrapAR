package application.draw;

import application.draw.color.CityColorHandler;
import application.draw.geometry.CoordinateHandler;
import processing.core.PApplet;
import processing.core.PMatrix3D;
import treemap.Treemap;

public class DrawController {

	private Treemap map;
	private PApplet applet;
	private PMatrix3D lastMatrix = new PMatrix3D(0.03271547f, -0.9987524f, 0.037727464f, 7.3349524f, 0.9948697f, 0.028926386f, -0.09694087f,
			 6.203373f, 0.0957286f, 0.040705375f, 0.99457484f, -279.99384f, 0.0f, 0.0f, 0.0f, 1.0f);

	private int treemapHeight;
	private int treemapWidth;
	private CoordinateHandler coordinateHandler;
	private boolean recordingActive = false;
	private CityColorHandler colorHandler;
	private CityDrawer cityDrawer;

	
	public DrawController(Treemap map, PApplet applet, CityColorHandler colorHandler, CoordinateHandler coordinateHandler, CityDrawer cityDrawer) {
		this.map = map;
		this.applet = applet;
		this.colorHandler = colorHandler;
		this.coordinateHandler = coordinateHandler;
		this.cityDrawer = cityDrawer;
	}
	
	public void draw() {
		applet.background(255);
		drawOnLastMarker();
		cityDrawer.drawText();
		if (recordingActive)
			applet.saveFrame("/output/seq-####.tga");
	}
	
	public void drawXmlTreemap3D() {
		applet.lights();
		applet.noStroke();
		applet.pushMatrix();
		applet.translate(0, 0, -6.0f);
		colorHandler.fillFloor();
		applet.noStroke();
		applet.strokeWeight(0);
		applet.box((float) treemapWidth, (float) treemapHeight, 12.0f);
		applet.popMatrix();
		applet.stroke(0x33000000);
		map.draw();
		applet.noLights();
	}

	public void drawModel() {
		coordinateHandler.applyTransformation();
		drawXmlTreemap3D();
	}

	public void drawOnLastMarker() {
		applet.pushMatrix();
		applet.resetMatrix();
		applet.applyMatrix(lastMatrix.m00, lastMatrix.m01, lastMatrix.m02,
				lastMatrix.m03, lastMatrix.m10, lastMatrix.m11, lastMatrix.m12,
				lastMatrix.m13, lastMatrix.m20, lastMatrix.m21, lastMatrix.m22,
				lastMatrix.m23, lastMatrix.m30, lastMatrix.m31, lastMatrix.m32,
				lastMatrix.m33);
		drawModel();
		applet.popMatrix();
	}
	public void toggleRecording() {
		recordingActive = !recordingActive;		
	}
}
