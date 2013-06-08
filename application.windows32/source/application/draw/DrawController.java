package application.draw;

import java.text.NumberFormat;

import processing.core.PMatrix3D;
import treemap.Treemap;
import application.CommitLog;
import application.SkyscrapAR;
import application.VersionController;
import application.draw.color.CityColorHandler;
import application.draw.geometry.CoordinateHandler;

public class DrawController {

	private SkyscrapAR skyscrapAR;
	private boolean recordingActive = false;

	private float heightScale = 1.0f;

	public VersionController versionController;
	public String titleString = "";
	
	private PMatrix3D lastMatrix = new PMatrix3D(0.03271547f, -0.9987524f, 0.037727464f, 7.3349524f, 0.9948697f, 0.028926386f, -0.09694087f,
												 6.203373f, 0.0957286f, 0.040705375f, 0.99457484f, -279.99384f, 0.0f, 0.0f, 0.0f, 1.0f);
	private CityColorHandler colorHandler;
	private int treemapHeight;
	private int treemapWidth;
	private CommitLog commitLog;
	private Treemap map;
	private CoordinateHandler coordinateHandler;
	
	public DrawController(SkyscrapAR skyscrapAR, CityColorHandler colorHandler, int treemapHeight, int treemapWidth, int maxVersion,
			CommitLog commitLog, Treemap map, CoordinateHandler coordinateHandler) {
		this.skyscrapAR = skyscrapAR;
		this.colorHandler = colorHandler;
		this.treemapHeight = treemapHeight;
		this.treemapWidth = treemapWidth;
		this.commitLog = commitLog;
		this.map = map;
		this.coordinateHandler = coordinateHandler;
		this.versionController = new VersionController(skyscrapAR, maxVersion);
	}

	public void draw() {
		versionController.tweenVersion();
		skyscrapAR.background(255);
		drawOnLastMarker();
		drawText();
		if (recordingActive)
			skyscrapAR.saveFrame("/output/seq-####.tga");
	}

	public void drawXmlTreemap3D() {
		skyscrapAR.lights();
		skyscrapAR.noStroke();
		skyscrapAR.pushMatrix();
		skyscrapAR.translate(0, 0, -6.0f);
		colorHandler.fillFloor();
		skyscrapAR.noStroke();
		skyscrapAR.strokeWeight(0);
		skyscrapAR.box((float) treemapWidth, (float) treemapHeight, 12.0f);
		skyscrapAR.popMatrix();
		skyscrapAR.stroke(0x33000000);
		map.draw();
		skyscrapAR.noLights();
	}

	public void drawModel() {
		coordinateHandler.applyTransformation();
		drawXmlTreemap3D();
	}

	public void drawOnLastMarker() {
		skyscrapAR.pushMatrix();
		skyscrapAR.resetMatrix();
		skyscrapAR.applyMatrix(lastMatrix.m00, lastMatrix.m01, lastMatrix.m02,
				lastMatrix.m03, lastMatrix.m10, lastMatrix.m11, lastMatrix.m12,
				lastMatrix.m13, lastMatrix.m20, lastMatrix.m21, lastMatrix.m22,
				lastMatrix.m23, lastMatrix.m30, lastMatrix.m31, lastMatrix.m32,
				lastMatrix.m33);
		drawModel();
		skyscrapAR.popMatrix();
	}
	
	private String getInfoText() {
		return "Packages: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_packages)
				+ " Objects: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_objects)
				+ " Subroutines: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_subroutines)
				+ " Total LOC: "
				+ NumberFormat.getInstance().format(skyscrapAR.g_total_loc);
	}
	
	public void drawText() {
		colorHandler.fillText();
		skyscrapAR.text(titleString, 10, 32);
		String mode = "scale=" + heightScale;
		skyscrapAR.text(mode + "\n" + getVersionText() + "\n" + getInfoText(), 10,	skyscrapAR.height - 50);
	}
	
	public String getVersionText() {
		return versionController.getVersionText(commitLog);

	}
		
	public void toggleRecording() {
		recordingActive = !recordingActive;		
	}

	public void incCurrentVersion(int increment) {
		versionController.incCurrentVersion(increment);
	}

	public double getCurrentVersion() {
		return versionController.getCurrentVersion();
	}	
}
